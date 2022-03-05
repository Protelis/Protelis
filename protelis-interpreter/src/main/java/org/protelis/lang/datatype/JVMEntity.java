/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.lang.datatype;

import java.io.Serializable;
import java.lang.reflect.Method;
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
    @SuppressFBWarnings("SE_TRANSIENT_FIELD_NOT_RESTORED")
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
     * @param method the {@link Method} that this {@link JVMEntity} represents
     */
    public JVMEntity(final Method method) {
        typeName = method.getDeclaringClass().getName();
        memberName = method.getName();
        memberType = SupportedEntityTypes.METHOD;
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
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "The field is immutable")
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
                        final java.lang.reflect.Field field = getType().getField(memberName);
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
                throw new IllegalStateException(e);
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
