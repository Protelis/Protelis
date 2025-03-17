package org.protelis.lang.datatype

import org.eclipse.xtext.common.types.JvmFeature
import org.eclipse.xtext.common.types.JvmField
import org.eclipse.xtext.common.types.JvmOperation
import java.io.Serializable
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/**
 * A runtime Protelis view of a [JvmFeature].
 */
data class JVMEntity(
    private val memberName: String,
    private val memberType: SupportedEntityTypes,
    private val typeName: String,
) : Serializable {

    private companion object {
        private const val serialVersionUID = 1L
    }

    @Transient
    private var value: Any? = null

    @Transient
    private var type: Class<*>? = null

    constructor(feature: JvmFeature) : this(
        feature.simpleName,
        when (feature) {
            is JvmField -> SupportedEntityTypes.FIELD
            is JvmOperation -> SupportedEntityTypes.METHOD
            else -> error("Unknown JvmFeature type ${feature::class.java} ($feature)")
        },
        feature.declaringType.qualifiedName,
    ) {
        if (feature is JvmField) {
            value = feature.constantValue
        }
    }

    constructor(method: Method) : this(
        method.name,
        SupportedEntityTypes.METHOD,
        method.declaringClass.name,
    )

    /**
     * @return the simple name of this entity
     */
    fun getMemberName(): String = memberName

    /**
     * @return The type this entity belongs to
     */
    fun getType(): Class<*> = type ?: try {
        Class.forName(typeName).also { type = it }
    } catch (e: ClassNotFoundException) {
        error(e)
    }

    /**
     * @return the resolution of the entity: returns this in case of callable entity
     * (such as a method), and the value of the referenced field otherwise.
     */
    fun getValue(): Any? {
        if (value == null) {
            value = try {
                when (memberType) {
                    SupportedEntityTypes.FIELD -> {
                        val field = getType().getField(memberName)
                        if (Modifier.isStatic(field.modifiers)) {
                            field.get(null)
                        } else {
                            error("Protelis cannot access non-static fields such as $field")
                        }
                    }
                    SupportedEntityTypes.METHOD -> this
                }
            } catch (e: NoSuchFieldException) {
                error(e)
            } catch (e: SecurityException) {
                error(e)
            } catch (e: IllegalAccessException) {
                error(e)
            }
        }
        return value
    }

    override fun toString(): String = "$memberType:$typeName.$memberName"

    /**
     * Represents the type of a [JVMEntity].
     */
    enum class SupportedEntityTypes {
        /** Represents a field in a JVM entity. */
        FIELD,

        /** Represents a method in a JVM entity. */
        METHOD,
    }
}
