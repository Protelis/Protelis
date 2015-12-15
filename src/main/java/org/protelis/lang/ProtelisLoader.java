/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.math3.util.Pair;
import org.danilopianini.lang.util.FasterString;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.diagnostics.AbstractDiagnostic;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.StringInputStream;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.interpreter.impl.AlignedMap;
import org.protelis.lang.interpreter.impl.All;
import org.protelis.lang.interpreter.impl.BinaryOp;
import org.protelis.lang.interpreter.impl.Constant;
import org.protelis.lang.interpreter.impl.CreateTuple;
import org.protelis.lang.interpreter.impl.CreateVar;
import org.protelis.lang.interpreter.impl.DotOperator;
import org.protelis.lang.interpreter.impl.Env;
import org.protelis.lang.interpreter.impl.Eval;
import org.protelis.lang.interpreter.impl.FunctionCall;
import org.protelis.lang.interpreter.impl.GenericHoodCall;
import org.protelis.lang.interpreter.impl.HoodCall;
import org.protelis.lang.interpreter.impl.If;
import org.protelis.lang.interpreter.impl.MethodCall;
import org.protelis.lang.interpreter.impl.NBRCall;
import org.protelis.lang.interpreter.impl.RepCall;
import org.protelis.lang.interpreter.impl.Self;
import org.protelis.lang.interpreter.impl.TernaryOp;
import org.protelis.lang.interpreter.impl.UnaryOp;
import org.protelis.lang.interpreter.impl.Variable;
import org.protelis.lang.util.HoodOp;
import org.protelis.lang.util.Reference;
import org.protelis.parser.ProtelisStandaloneSetup;
import org.protelis.parser.protelis.Assignment;
import org.protelis.parser.protelis.Block;
import org.protelis.parser.protelis.BooleanVal;
import org.protelis.parser.protelis.BuiltinHoodOp;
import org.protelis.parser.protelis.Call;
import org.protelis.parser.protelis.DoubleVal;
import org.protelis.parser.protelis.ExprList;
import org.protelis.parser.protelis.Expression;
import org.protelis.parser.protelis.FunctionDef;
import org.protelis.parser.protelis.GenericHood;
import org.protelis.parser.protelis.Import;
import org.protelis.parser.protelis.Lambda;
import org.protelis.parser.protelis.Module;
import org.protelis.parser.protelis.Mux;
import org.protelis.parser.protelis.NBR;
import org.protelis.parser.protelis.Pi;
import org.protelis.parser.protelis.Rep;
import org.protelis.parser.protelis.StringVal;
import org.protelis.parser.protelis.TupleVal;
import org.protelis.parser.protelis.VarDef;
import org.protelis.parser.protelis.VarDefList;
import org.protelis.parser.protelis.VarUse;
import org.protelis.vm.ProtelisProgram;
import org.protelis.vm.impl.SimpleProgramImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import com.google.inject.Injector;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Main entry-point class for loading/parsing Protelis programs.
 */
public final class ProtelisLoader {

    private static final Logger L = LoggerFactory.getLogger("Protelis Loader");
    private static final AtomicInteger IDGEN = new AtomicInteger();
    private static final XtextResourceSet XTEXT = createResourceSet();
    private static final Pattern REGEX_PROTELIS_MODULE = Pattern.compile("(?:\\w+:)*\\w+");
    private static final Pattern REGEX_PROTELIS_IMPORT = Pattern.compile("import\\s+((?:\\w+:)*\\w+)\\s+",
            Pattern.DOTALL);
    private static final PathMatchingResourcePatternResolver RESOLVER = new PathMatchingResourcePatternResolver();
    private static final String PROTELIS_FILE_EXTENSION = "pt";
    private static final String HOOD_END = "Hood";

    private ProtelisLoader() {
    }

    /**
     * @param program
     *            Protelis module, program file or program to be prepared for
     *            execution. It must be one of:
     * 
     *            i) a valid Protelis qualifier name (Java like name, colon
     *            separated);
     * 
     *            ii) a valid {@link URI} string;
     * 
     *            iii) a valid Protelis program.
     * 
     *            Those possibilities are checked in order.
     * 
     *            The URI String can be in the form of a URL like
     *            "file:///home/user/protelis/myProgram" or a location relative
     *            to the classpath. In case, for instance,
     *            "/my/package/myProgram.pt" is passed, it will be automatically
     *            get converted to "classpath:/my/package/myProgram.pt". All the
     *            Protelis modules your program relies upon must be included in
     *            your Java classpath. The Java classpath scanning is done
     *            automatically by this constructor, linking is performed by
     *            Xtext transparently. {@link URI}s of type "platform:/" are
     *            supported, for those who work within an Eclipse environment.
     * @return an {@link ProtelisProgram} comprising the constructed program
     * @throws IllegalArgumentException
     *             when the program has errors
     */
    public static ProtelisProgram parse(final String program) throws IllegalArgumentException {
        try {
            if (REGEX_PROTELIS_MODULE.matcher(program).matches()) {
                return parseURI("classpath:/" + program.replace(':', '/') + "." + PROTELIS_FILE_EXTENSION);
            }
            return parseURI(program);
        } catch (IOException e) {
            return parseAnonymousModule(program);
        }
    }

    /**
     * @param program
     *            A valid Protelis program to be prepared for execution.
     * 
     *            All the Protelis modules your program relies upon must be
     *            included in your Java classpath. The Java classpath scanning
     *            is done automatically by this constructor, linking is
     *            performed by Xtext transparently. {@link URI}s of type
     *            "platform:/" are supported, for those who work within an
     *            Eclipse environment.
     * @return a {@link Pair} of {@link AnnotatedTree} (the program) and
     *         {@link FunctionDefinition} (containing the available functions)
     * @throws IllegalArgumentException
     *             when the program has errors
     */
    public static ProtelisProgram parseAnonymousModule(final String program) throws IllegalArgumentException {
        return parse(resourceFromString(program));
    }

    /**
     * @param programURI
     *            Protelis program file to be prepared for execution. It must be
     *            a either a valid {@link URI} string, for instance
     *            "file:///home/user/protelis/myProgram" or a location relative
     *            to the classpath. In case, for instance,
     *            "/my/package/myProgram.pt" is passed, it will be automatically
     *            get converted to "classpath:/my/package/myProgram.pt". All the
     *            Protelis modules your program relies upon must be included in
     *            your Java classpath. The Java classpath scanning is done
     *            automatically by this constructor, linking is performed by
     *            Xtext transparently. {@link URI}s of type "platform:/" are
     *            supported, for those who work within an Eclipse environment.
     * @return a new {@link ProtelisProgram}
     * @throws IOException
     *             when the resource cannot be found
     * @throws IllegalArgumentException
     *             when the program has errors
     */
    public static ProtelisProgram parseURI(final String programURI) throws IOException, IllegalArgumentException {
        return parse(resourceFromURIString(programURI));
    }

    private static Resource resourceFromURIString(final String programURI) throws IOException {
        loadResourcesRecursively(XTEXT, programURI);
        final String realURI = (programURI.startsWith("/") ? "classpath:" : "") + programURI;
        final URI uri = URI.createURI(realURI);
        return XTEXT.getResource(uri, true);
    }

    private static void loadResourcesRecursively(final XtextResourceSet target, final String programURI)
            throws IOException {
        loadResourcesRecursively(target, programURI, new LinkedHashSet<>());
    }

    private static void loadResourcesRecursively(final XtextResourceSet target, final String programURI,
            final Set<String> alreadyInQueue) throws IOException {
        final String realURI = (programURI.startsWith("/") ? "classpath:" : "") + programURI;
        if (!alreadyInQueue.contains(realURI)) {
            alreadyInQueue.add(realURI);
            final URI uri = URI.createURI(realURI);
            final org.springframework.core.io.Resource protelisFile = RESOLVER.getResource(realURI);
            final InputStream is = protelisFile.getInputStream();
            final String ss = IOUtils.toString(is, "UTF-8");
            final Matcher matcher = REGEX_PROTELIS_IMPORT.matcher(ss);
            while (matcher.find()) {
                final int start = matcher.start(1);
                final int end = matcher.end(1);
                final String imp = ss.substring(start, end);
                final String classpathResource = "classpath:/" + imp.replace(":", "/") + "." + PROTELIS_FILE_EXTENSION;
                loadResourcesRecursively(target, classpathResource, alreadyInQueue);
            }
            target.getResource(uri, true);
        }
    }

    /**
     * @param program
     *            the program in String format
     * @return a dummy:/ resource that can be used to interpret the program
     */
    public static Resource resourceFromString(final String program) {
        final XtextResourceSet xrs = XTEXT;
        InputStream in = new StringInputStream(program);
        try {
            loadStringResources(xrs, in);
        } catch (IOException e) {
            L.error("Couldn't get resources associated with anonymous program", e);
        }
        final URI uri = URI.createURI("dummy:/protelis-generated-program-" + IDGEN.getAndIncrement() + ".pt");
        final Resource r = xrs.createResource(uri);
        in = new StringInputStream(program);
        try {
            r.load(in, xrs.getLoadOptions());
        } catch (IOException e) {
            L.error("I/O error while reading in RAM: this must be tough.", e);
        }
        return r;
    }

    private static void loadStringResources(final XtextResourceSet target, final InputStream is) throws IOException {
        final Set<String> alreadyInQueue = new LinkedHashSet<>();
        final String ss = IOUtils.toString(is, "UTF-8");
        final Matcher matcher = REGEX_PROTELIS_IMPORT.matcher(ss);
        while (matcher.find()) {
            final int start = matcher.start(1);
            final int end = matcher.end(1);
            final String imp = ss.substring(start, end);
            final String classpathResource = "classpath:/" + imp.replace(":", "/") + "." + PROTELIS_FILE_EXTENSION;
            loadResourcesRecursively(target, classpathResource, alreadyInQueue);
        }
    }

    private static XtextResourceSet createResourceSet() {
        new org.eclipse.emf.mwe.utils.StandaloneSetup().setPlatformUri(".");
        final Injector guiceInjector = new ProtelisStandaloneSetup().createInjectorAndDoEMFRegistration();
        final XtextResourceSet xtext = guiceInjector.getInstance(XtextResourceSet.class);
        xtext.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
        return xtext;
    }

    /**
     * @param resource
     *            the {@link Resource} containing the program to execute
     * @return a {@link Pair} of {@link AnnotatedTree} (the program) and
     *         {@link FunctionDefinition} (containing the available functions)
     */
    public static ProtelisProgram parse(final Resource resource) {
        Objects.requireNonNull(resource);
        if (!resource.getErrors().isEmpty()) {
            for (final Diagnostic d : recursivelyCollectErrors(resource)) {
                final AbstractDiagnostic ad = (AbstractDiagnostic) d;
                final String place = ad.getUriToProblem().toString().split("#")[0];
                L.error("Error in " + place + " at line " + d.getLine() + ": " + d.getMessage());
            }
            throw new IllegalArgumentException("Protelis program cannot run because it has errors.");
        }
        final Module root = (Module) resource.getContents().get(0);
        assert root != null;
        Objects.requireNonNull(root.getProgram(), "The provided resource does not contain any main program, and can not be executed.");
        /*
         * Create the function headers.
         * 
         * 1) Take all the imports in reverse order (the first declared is the
         * one actually declared in case of name conflict), insert them with the
         * two possible names (fully qualified and short). This operation must
         * be recursive (for dealing with imports of imports).
         * 
         * 2) Override conflicting names with local names
         */
        final Map<FunctionDef, FunctionDefinition> nameToFun = new LinkedHashMap<>();
        recursivelyInitFunctions(root, nameToFun);
        /*
         * Function definitions are in place, now create function bodies. Bodies
         * may contain lambdas, lambdas are named using processing order, as a
         * consequence function bodies must be evaluated sequentially.
         */
        final Map<Reference, FunctionDefinition> refToFun = nameToFun.keySet().stream()
                .collect(Collectors.toMap(ProtelisLoader::toR, nameToFun::get));
        nameToFun.forEach((fd, fun) -> fun.setBody(Dispatch.translate(fd.getBody(), refToFun)));
        /*
         * Create the main program
         */
        return new SimpleProgramImpl(root, Dispatch.translate(root.getProgram(), refToFun), refToFun);
    }

    private static <E> Stream<E> flatten(
            final E target,
            final Function<? super E, ? extends Stream<? extends E>> extractor) {
        return Stream.concat(Stream.of(target), extractor.apply(target).flatMap(el -> flatten(el, extractor)));
    }

    private static List<AnnotatedTree<?>> callArgs(final Call call, final Map<Reference, FunctionDefinition> env) {
        return exprListArgs(call.getArgs(), env);
    }

    private static List<AnnotatedTree<?>> exprListArgs(final ExprList l, final Map<Reference, FunctionDefinition> env) {
        return Optional.ofNullable(l)
                .map(ExprList::getArgs)
                .map(List::stream)
                .map(s -> s.map(e -> Dispatch.translate(e, env)))
                .map(s -> s.collect(Collectors.<AnnotatedTree<?>>toList()))
                .orElse(Collections.emptyList());
    }

    @SuppressFBWarnings(value = "SE_BAD_FIELD", justification = "This enum is not meant to get Serialized")
    private enum Dispatch {
        ALIGNED_MAP((e, m) -> {
            final org.protelis.parser.protelis.AlignedMap alMap = (org.protelis.parser.protelis.AlignedMap) e;
            return new AlignedMap(
                translate(alMap.getArg(), m),
                translate(alMap.getCond(), m),
                translate(alMap.getOp(), m),
                translate(alMap.getDefault(), m));
        }),
        ASSIGNMENT((e, m) -> new CreateVar(toR(((Assignment) e).getRefVar()), translate(((Assignment) e).getRight(), m), false)),
        BLOCK((e, m) -> new All(
                flatten((Block) e, b -> b.getOthers() == null ? Stream.empty() : Stream.<Block>of(b.getOthers()))
                .map(b -> b.getFirst())
                .map(s -> translate(s, m))
                .collect(Collectors.toList()))),
        BOOLEAN((e, m) -> new Constant<>(((BooleanVal) e).isVal())),
        BUILTIN_HOOD((e, m) -> {
            final BuiltinHoodOp hood = (BuiltinHoodOp) e;
            return new HoodCall(
                    translate(hood.getArg(), m),
                    HoodOp.get(hood.getName().replace(HOOD_END, "")),
                    hood.isInclusive());
        }),
        CALL_METHOD((e, m) -> new MethodCall((JvmOperation) ((Call) e).getReference(), callArgs((Call) e, m))),
        CALL_FUNCTION((e, m) -> new FunctionCall(m.get(toR(((Call) e).getReference())), callArgs((Call) e, m))),
        DECLARATION((e, m) -> new CreateVar(toR(e), translate(((VarDef) e).getRight(), m), true)),
        DOUBLE((e, m) -> new Constant<>(((DoubleVal) e).getVal())),
        E((e, m) -> e instanceof org.protelis.parser.protelis.E ? new Constant<>(Math.E) : null),
        ENV((e, m) -> e instanceof org.protelis.parser.protelis.Env ? new Env() : null),
        EVAL((e, m) -> new Eval(translate(((org.protelis.parser.protelis.Eval) e).getArg(), m))),
        EXPRESSION((e, m) -> {
            final Expression exp = (Expression) e;
            if (exp.getMethodName() != null) {
                return new DotOperator(exp.getMethodName(), translate(exp.getLeft(), m), exprListArgs(exp.getArgs(), m));
            }
            if (exp.getV() != null) {
                return translate(exp.getV(), m);
            }
            if (exp.getLeft() == null) {
                return new UnaryOp(exp.getName(), translate(exp.getRight(), m));
            }
            if (exp.getRight() == null) {
                return new UnaryOp(exp.getName(), translate(exp.getLeft(), m));
            }
            return new BinaryOp(exp.getName(), translate(exp.getLeft(), m), translate(exp.getRight(), m));
        }),
        GENERIC_HOOD((e, m) -> {
            final GenericHood hood = (GenericHood) e;
            final boolean inclusive = hood.getName().length() > 4;
            final AnnotatedTree<?> nullResult = translate(hood.getDefault(), m);
            final AnnotatedTree<Field> field = translate(hood.getArg(), m);
            final EObject ref = hood.getReference();
            if (hood.getReference() == null) {
                return new GenericHoodCall(inclusive, translate(hood.getOp(), m), nullResult, field);
            }
            if (ref instanceof JvmOperation) {
                return new GenericHoodCall(inclusive, (JvmOperation) ref, nullResult, field);
            }
            return new GenericHoodCall(inclusive, new Constant<>(m.get(toR(hood.getReference()))), nullResult, field);
        }),
        IF((e, m) -> {
            final org.protelis.parser.protelis.If ifop = (org.protelis.parser.protelis.If) e;
            return new If<>(translate(ifop.getCond(), m),
                    translate(ifop.getThen(), m),
                    translate(ifop.getElse(), m));
        }),
        LAMBDA((e, m) -> {
            final Lambda l = (Lambda) e;
            final EObject argobj = l.getArgs();
            final List<VarDef> args = argobj == null ? Collections.emptyList()
                    : argobj instanceof VarDef ? Lists.newArrayList((VarDef) l.getArgs())
                    : ((VarDefList) argobj).getArgs();
            final AnnotatedTree<?> body = translate(l.getBody(), m);
            final String base = Base64.encodeBase64String(
                    Hashing.sha512().hashString(body.toString(), Charsets.UTF_8).asBytes());
            final FunctionDefinition lambda = new FunctionDefinition("λ" + base, toR(args));
            lambda.setBody(body);
            return new Constant<>(lambda);
        }),
        MUX((e, m) -> {
            final Mux mux = (Mux) e;
            return new TernaryOp(mux.getName(),
                    translate(mux.getCond(), m),
                    translate(mux.getThen(), m),
                    translate(mux.getElse(), m));
        }),
        NBR((e, m) -> new NBRCall(translate(((NBR) e).getArg(), m))),
        PI((e, m) -> e instanceof Pi ? new Constant<>(Math.PI) : null),
        REP((e, m) -> new RepCall<>(toR(((Rep) e).getInit().getX()),
                    translate(((Rep) e).getInit().getW(), m),
                    translate(((Rep) e).getBody(), m))),
        SELF((e, m) -> e instanceof org.protelis.parser.protelis.Self ? new Self() : null),
        STRING((e, m) -> new Constant<>(((StringVal) e).getVal())),
        TUPLE((e, m) -> new CreateTuple(exprListArgs(((TupleVal) e).getArgs(), m))),
        VARIABLE((e, m) -> new Variable(toR(((VarUse) e).getReference())));

        private BiFunction<EObject, Map<Reference, FunctionDefinition>, AnnotatedTree<?>> translator;

        Dispatch(final BiFunction<EObject, Map<Reference, FunctionDefinition>, AnnotatedTree<?>> translator) {
            this.translator = translator;
        }

        @SuppressWarnings("unchecked")
        public static <T> AnnotatedTree<T> translate(final EObject o, final Map<Reference, FunctionDefinition> functions) {
            return Arrays.stream(values())
                .map(dispatch -> {
                    try {
                        return Optional.of((AnnotatedTree<T>) dispatch.translator.apply(o, functions));
                    } catch (RuntimeException e) {
                        return Optional.<AnnotatedTree<T>>empty();
                    }
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny()
                .get();
        }

    }

    private static Iterable<Diagnostic> recursivelyCollectErrors(final Resource resource) {
        return recursivelyCollectErrors(resource, new ArrayList<>(), new HashSet<>());
    }

    private static Iterable<Diagnostic> recursivelyCollectErrors(
            final Resource resource,
            final List<Diagnostic> errors,
            final Set<Resource> completed) {
        /*
         * Mark as visited
         */
        completed.add(resource);
        /*
         * Walk linked resources
         */
        for (final Resource r : resource.getResourceSet().getResources()) {
            if (!completed.contains(r)) {
                recursivelyCollectErrors(r, errors, completed);
            }
        }
        /*
         * Add local errors (last, so that the "deeper" errors are listed first)
         */
        errors.addAll(resource.getErrors());
        return errors;
    }

    private static void recursivelyInitFunctions(final Module module,
            final Map<FunctionDef, FunctionDefinition> nameToFun) {
        recursivelyInitFunctions(module, nameToFun, new LinkedHashSet<>());
    }

    private static void recursivelyInitFunctions(
            final Module module,
            final Map<FunctionDef, FunctionDefinition> nameToFun,
            final Set<Module> completed) {
        if (!completed.contains(module)) {
            completed.add(module);
            /*
             * Init imports functions, in reverse order
             */
            final EList<Import> imports = module.getProtelisImport();
            for (int i = imports.size() - 1; i >= 0; i--) {
                final Import protelisImport = imports.get(i);
                recursivelyInitFunctions(protelisImport.getModule(), nameToFun, completed);
            }
            /*
             * Init local functions
             */
            nameToFun.putAll(module.getDefinitions().stream()
                .collect(Collectors.toMap(
                    Function.identity(),
                    fd -> new FunctionDefinition(
                            new FasterString(Optional.ofNullable(module.getName()).orElse("") + fd.getName()),
                            toR(extractArgs(fd))
                    )
                ))
            );
        }
    }

    private static List<VarDef> extractArgs(final FunctionDef e) {
        return e.getArgs() != null && e.getArgs().getArgs() != null ? e.getArgs().getArgs() : Collections.emptyList();
    }

    private static Reference toR(final Object o) {
        return new Reference(o);
    }

    private static List<Reference> toR(final List<?> l) {
        return l.stream().map(Reference::new).collect(Collectors.toList());
    }

}
