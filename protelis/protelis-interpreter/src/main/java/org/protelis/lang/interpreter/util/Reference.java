package org.protelis.lang.interpreter.util;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import org.danilopianini.lang.HashUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.protelis.parser.protelis.FunctionDef;
import org.protelis.parser.protelis.ProtelisModule;
import org.protelis.parser.protelis.VarDef;

import java8.util.Optional;

/**
 * Implements a Serializable reference to an Object. This implementation is
 * fragile, and should be substituted as soon as the Java board decides what to
 * do with sun.misc.Unsafe (that might get dropped).
 */
public final class Reference implements Serializable {

    private static final long serialVersionUID = 8294777860793746504L;
    private final Serializable uid;
    private String strRep;

    /**
     * @param obj the object to refer to
     */
    public Reference(final Object obj) {
        if (obj instanceof VarDef) {
            final VarDef var = (VarDef) obj;
            long hash = HashUtils.hash64(var.getName());
            for (EObject container = var.eContainer(); container != null; container = container.eContainer()) {
                try {
                    hash ^= HashUtils.hash64(container.getClass().getMethod("getName").invoke(container));
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                        | NoSuchMethodException | SecurityException e) {
                    hash = HashUtils.hash64(hash);
                }
            }
            uid = hash;
            strRep = var.getName();
        } else if (obj instanceof JvmIdentifiableElement) {
            final JvmIdentifiableElement method = (JvmIdentifiableElement) obj;
            strRep = method.getQualifiedName();
            uid = strRep;
        } else if (obj instanceof FunctionDef) {
            final FunctionDef function = (FunctionDef) obj;
            final ProtelisModule container = (ProtelisModule) function.eContainer();
            final String name = Optional.ofNullable(container.getName()).orElse("default-module") + ":";
            strRep = name + function.getName();
            uid = strRep;
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
        return obj instanceof Reference && ((Reference) obj).uid.equals(uid);
    }

    @Override
    public String toString() {
        if (strRep == null) {
            strRep = "Var@" + uid;
        }
        return strRep;
    }

}
