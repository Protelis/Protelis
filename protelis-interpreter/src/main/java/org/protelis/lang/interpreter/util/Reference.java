/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

package org.protelis.lang.interpreter.util;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.util.ITextRegionWithLineInformation;
import org.protelis.parser.protelis.FunctionDef;
import org.protelis.parser.protelis.ProtelisModule;
import org.protelis.parser.protelis.VarDef;

import com.google.common.collect.ImmutableList;

/**
 * Implements a Serializable reference to an Object.
 */
public final class Reference implements Serializable {

    private static final long serialVersionUID = 8294777860793746504L;
    private final Serializable uid;
    private String strRep;

    /**
     * @param obj the object to refer to
     */
    public Reference(final Object obj) {
        if (obj instanceof String) {
            strRep = obj.toString();
            uid = strRep;
        } else  if (obj instanceof VarDef) {
            final VarDef var = (VarDef) obj;
            final ITextRegionWithLineInformation node = NodeModelUtils.getNode(var).getTextRegionWithLineInformation();
            EObject container = var.eContainer();
            while (!(container.eContainer() == null || container instanceof ProtelisModule)) {
                container = container.eContainer();
            }
            final String module = Optional.ofNullable(((ProtelisModule) container).getName()).orElse("$");
            uid = new Handler<>(obj, ImmutableList.of(
                    node.getLineNumber(),
                    node.getEndLineNumber(),
                    node.getOffset(),
                    node.getLength(),
                    var.getName(),
                    module));
            strRep = var.getName();
        } else if (obj instanceof JvmIdentifiableElement) {
            final JvmIdentifiableElement method = (JvmIdentifiableElement) obj;
            strRep = method.getIdentifier();
            uid = new Handler<>(obj, strRep);
        } else if (obj instanceof FunctionDef) {
            final FunctionDef function = (FunctionDef) obj;
            final ProtelisModule container = (ProtelisModule) function.eContainer();
            final String name = Optional.ofNullable(container.getName()).orElse("default-module") + ":";
            strRep = name + function.getName();
            uid = new Handler<>(obj, strRep);
        } else if (obj instanceof Serializable) {
            uid = (Serializable) Objects.requireNonNull(obj);
        } else {
            throw new IllegalArgumentException("Cannot refer to " + obj);
        }
    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return this == obj || obj instanceof Reference && ((Reference) obj).uid.equals(uid);
    }

    @Override
    public String toString() {
        if (strRep == null) {
            strRep = "Var@" + uid;
        }
        return strRep;
    }

    private static final class Handler<T, S extends Serializable> implements Serializable {
        private static final long serialVersionUID = 1L;
        private final S serializableState;
        private final Class<?> targetClass;
        private Handler(final T target, final S state) {
            targetClass = Objects.requireNonNull(target).getClass();
            serializableState = Objects.requireNonNull(state);
        }
        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof Handler) {
                final Handler<?, ?> other = (Handler<?, ?>) obj;
                return targetClass.equals(other.targetClass)
                    && serializableState.equals(other.serializableState);
            }
            return false;
        }
        @Override
        public int hashCode() {
            return serializableState.hashCode();
        }
    }

}
