package org.protelis.lang.datatype;

import java.io.Serializable;
import java.lang.reflect.Modifier;

import org.eclipse.xtext.common.types.JvmFeature;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmOperation;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * A runtime Protelis view of a {@link JvmFeature}.
 *
 */
public final class JVMEntity implements Serializable {

    private static final long serialVersionUID = 1;
    private final String memberName;
    private final SupportedEntityTypes memberType;
    private final String typeName;
    @SuppressFBWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
    private transient Object value;
    private transient Class<?> type;

    /**
     * @param feature the feature to be used at runtime
     */
    public JVMEntity(final JvmFeature feature) {
        typeName = feature.getDeclaringType().getQualifiedName();
        memberName = feature.getSimpleName();
        if (feature instanceof JvmField) {
            memberType = SupportedEntityTypes.FIELD;
            value = ((JvmField) feature).getConstantValue();
        } else if (feature instanceof JvmOperation) {
            memberType = SupportedEntityTypes.METHOD;
        } else {
            throw new IllegalArgumentException("Unknown JvmFeature type " + feature.getClass() + " (" + feature + ')');
        }
    }

    /**
     * @return the simple name of this entity
     */
    public String getMemberName() {
        return memberName;
    }

    /**
     * @return The type this entity belongs to
     */
    public Class<?> getType() {
        if (type == null) {
            try {
                type = Class.forName(typeName);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }
        }
        return type;
    }

    /**
     * @return the resolution of the entity: returns this in case of callable entity
     *         (such as a method), and the value of the referenced field otherwise.
     */
    public Object getValue() {
        if (value == null) {
            try {
                switch (memberType) {
                    case FIELD:
                        java.lang.reflect.Field field = getType().getField(memberName);
                        if (Modifier.isStatic(field.getModifiers())) {
                            value = field.get(null);
                        } else {
                            throw new IllegalStateException("Protelis cannot access non-static fields such as " + field);
                        }
                        break;
                    case METHOD:
                        value = this;
                        break;
                    default: throw new  IllegalStateException("Fix Protelis code, it's bugged.");
                }
            } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return value;
    }

    @Override
    public String toString() {
        return memberType + ":" + typeName + "." + memberName;
    }

    enum SupportedEntityTypes {
        FIELD,
        METHOD
    }

}
