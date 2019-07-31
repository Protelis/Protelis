/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang;

import static org.protelis.lang.ProtelisLoadingUtilities.IT;
import static org.protelis.lang.ProtelisLoadingUtilities.argumentsToExpressionStream;
import static org.protelis.lang.ProtelisLoadingUtilities.referenceFor;
import static org.protelis.lang.ProtelisLoadingUtilities.referenceListFor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.io.IOUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.xtext.common.types.JvmFeature;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.StringInputStream;
import org.protelis.lang.datatype.Field;
import org.protelis.lang.datatype.FunctionDefinition;
import org.protelis.lang.datatype.JVMEntity;
import org.protelis.lang.interpreter.AnnotatedTree;
import org.protelis.lang.interpreter.impl.AlignedMap;
import org.protelis.lang.interpreter.impl.All;
import org.protelis.lang.interpreter.impl.AssignmentOp;
import org.protelis.lang.interpreter.impl.BinaryOp;
import org.protelis.lang.interpreter.impl.Constant;
import org.protelis.lang.interpreter.impl.CreateTuple;
import org.protelis.lang.interpreter.impl.Env;
import org.protelis.lang.interpreter.impl.Eval;
import org.protelis.lang.interpreter.impl.GenericHoodCall;
import org.protelis.lang.interpreter.impl.HoodCall;
import org.protelis.lang.interpreter.impl.If;
import org.protelis.lang.interpreter.impl.Invoke;
import org.protelis.lang.interpreter.impl.JvmConstant;
import org.protelis.lang.interpreter.impl.NBRCall;
import org.protelis.lang.interpreter.impl.Self;
import org.protelis.lang.interpreter.impl.ShareCall;
import org.protelis.lang.interpreter.impl.TernaryOp;
import org.protelis.lang.interpreter.impl.UnaryOp;
import org.protelis.lang.interpreter.impl.Variable;
import org.protelis.lang.interpreter.util.HoodOp;
import org.protelis.lang.interpreter.util.Reference;
import org.protelis.lang.loading.Metadata;
import org.protelis.parser.ProtelisStandaloneSetup;
import org.protelis.parser.protelis.Assignment;
import org.protelis.parser.protelis.Block;
import org.protelis.parser.protelis.BooleanVal;
import org.protelis.parser.protelis.Builtin;
import org.protelis.parser.protelis.BuiltinHoodOp;
import org.protelis.parser.protelis.Declaration;
import org.protelis.parser.protelis.DoubleVal;
import org.protelis.parser.protelis.Expression;
import org.protelis.parser.protelis.ExpressionList;
import org.protelis.parser.protelis.FunctionDef;
import org.protelis.parser.protelis.GenericHood;
import org.protelis.parser.protelis.IfWithoutElse;
import org.protelis.parser.protelis.InvocationArguments;
import org.protelis.parser.protelis.It;
import org.protelis.parser.protelis.Lambda;
import org.protelis.parser.protelis.LongLambda;
import org.protelis.parser.protelis.MethodCall;
import org.protelis.parser.protelis.Mux;
import org.protelis.parser.protelis.NBR;
import org.protelis.parser.protelis.OldLongLambda;
import org.protelis.parser.protelis.OldShortLambda;
import org.protelis.parser.protelis.ProtelisModule;
import org.protelis.parser.protelis.Rep;
import org.protelis.parser.protelis.RepInitialize;
import org.protelis.parser.protelis.Scalar;
import org.protelis.parser.protelis.Share;
import org.protelis.parser.protelis.ShareInitialize;
import org.protelis.parser.protelis.Statement;
import org.protelis.parser.protelis.StringVal;
import org.protelis.parser.protelis.TupleVal;
import org.protelis.parser.protelis.VarDef;
import org.protelis.parser.protelis.VarDefList;
import org.protelis.parser.protelis.VarUse;
import org.protelis.parser.protelis.Yield;
import org.protelis.vm.ProtelisProgram;
import org.protelis.vm.impl.SimpleProgramImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.hash.Hashing;
import com.google.inject.Injector;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Main entry-point class for loading/parsing Protelis programs.
 */
public final class ProtelisLoader {

    private static final String HOOD_END = "Hood";
    private static final ThreadLocal<Cache<String, Resource>> LOADED_RESOURCES = new ThreadLocal<Cache<String, Resource>>() {
        @Override
        protected Cache<String, Resource> initialValue() {
            return CacheBuilder.newBuilder()
                    .expireAfterAccess(1, TimeUnit.MINUTES)
                    .build();
        }
    };
    private static final Logger LOGGER = LoggerFactory.getLogger(ProtelisLoader.class);
    private static final String OPEN_J9_EMF_WORKED_AROUND = "Working around OpenJ9 + Eclipse EMF bug."
            + "See: https://bugs.eclipse.org/bugs/show_bug.cgi?id=549084"
            + "and https://github.com/eclipse/openj9/issues/6370";
    private static final String PROTELIS_FILE_EXTENSION = "pt";
    private static final Pattern REGEX_PROTELIS_IMPORT = Pattern.compile("^\\s*import\\s+((?:\\w+:)*\\w+)\\s+", Pattern.MULTILINE);
    private static final Pattern REGEX_PROTELIS_MODULE = Pattern.compile("(?:\\w+:)*\\w+");
    private static final ThreadLocal<PathMatchingResourcePatternResolver> RESOLVER = new ThreadLocal<PathMatchingResourcePatternResolver>() {
        @Override
        protected PathMatchingResourcePatternResolver initialValue() {
            return new PathMatchingResourcePatternResolver();
        }
    };

    private static final ThreadLocal<XtextResourceSet> XTEXT = new ThreadLocal<XtextResourceSet>() {
        @Override
        protected XtextResourceSet initialValue() {
            final Injector guiceInjector = new ProtelisStandaloneSetup().createInjectorAndDoEMFRegistration();
            final XtextResourceSet xtext = guiceInjector.getInstance(XtextResourceSet.class);
            xtext.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
            return xtext;
        }
    };

    private ProtelisLoader() {
    }

    private static void loadResourcesRecursively(final XtextResourceSet target, final String programURI)
            throws IOException {
        loadResourcesRecursively(target, programURI, new LinkedHashSet<>());
    }

    @SuppressFBWarnings(value = "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE", justification = "False positive")
    private static void loadResourcesRecursively(
            final XtextResourceSet target,
            final String programURI,
            final Set<String> alreadyInQueue) throws IOException {
        final String realURI = (programURI.startsWith("/") ? "classpath:" : "") + programURI;
        if (LOADED_RESOURCES.get().getIfPresent(realURI) == null && !alreadyInQueue.contains(realURI)) {
            alreadyInQueue.add(realURI);
            final URI uri = workAroundOpenJ9EMFBug(() -> URI.createURI(realURI));
            final org.springframework.core.io.Resource protelisFile = RESOLVER.get().getResource(realURI);
            if (protelisFile.exists()) {
                try (InputStream is = protelisFile.getInputStream()) {
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
                LOADED_RESOURCES.get().put(realURI, workAroundOpenJ9EMFBug(() -> target.getResource(uri, true)));
            } else {
                throw new IllegalStateException("expected resource " + realURI + " was not found");
            }
        }
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

    private static Metadata metadataFor(final EObject origin) {
        final INode grammarElement = NodeModelUtils.getNode(origin);
        final int startLine = grammarElement.getStartLine();
        final int endLine = grammarElement.getEndLine();
        return new Metadata() {
            private static final long serialVersionUID = 1L;
            @Override
            public int getEndLine() {
                return endLine;
            }
            @Override
            public int getStartLine() {
                return startLine;
            }
        };
    }

    /**
     * @param resource
     *            the {@link Resource} containing the program to execute
     * @return a {@link ProtelisProgram}
     */
    public static ProtelisProgram parse(final Resource resource) {
        Objects.requireNonNull(resource);
        if (!resource.getErrors().isEmpty()) {
            final String moduleName = Optional.ofNullable(resource.getContents())
                    .map(it -> it.get(0))
                    .map(it -> (ProtelisModule) it)
                    .map(ProtelisModule::getName)
                    .orElse("without declared module");
            final StringBuilder sb = new StringBuilder("Program " + moduleName
                    + " from resource " + resource.getURI()
                    + " cannot be created because of the following errors:\n");
            for (final Diagnostic d : recursivelyCollectErrors(resource)) {
                sb.append("Error");
                if (d.getLocation() != null) {
                    final String place = d.getLocation().split("#")[0];
                    sb.append(" in ");
                    sb.append(place);
                }
                try {
                    final int line = d.getLine();
                    sb.append(", line ");
                    sb.append(line);
                } catch (final UnsupportedOperationException e) { // NOPMD 
                    // The line information is not available
                }
                try {
                    final int column = d.getColumn();
                    sb.append(", column ");
                    sb.append(column);
                } catch (final UnsupportedOperationException e) { // NOPMD 
                    // The column information is not available
                }
                sb.append(": ");
                sb.append(d.getMessage());
                sb.append('\n');
            }
            throw new IllegalArgumentException(sb.toString());
        }
        final ProtelisModule root = (ProtelisModule) resource.getContents().get(0);
        Objects.requireNonNull(Objects.requireNonNull(root).getProgram(),
                "The provided resource does not contain any main program, and can not be executed.");
//        /*
//         * Create the function headers.
//         * 
//         * 1) Take all the imports in reverse order (the first declared is the
//         * one actually declared in case of name conflict), insert them with the
//         * two possible names (fully qualified and short). This operation must
//         * be recursive (for dealing with imports of imports).
//         * 
//         * 2) Override conflicting names with local names
//         */
//        final Map<FunctionDef, FunctionDefinition> nameToFun = new LinkedHashMap<>();
//        recursivelyInitFunctions(root, nameToFun);
//        /*
//         * Function definitions are in place, now create function bodies.
//         */
//        final Map<Reference, FunctionDefinition> refToFun = nameToFun.keySet().stream()
//                .collect(Collectors.toMap(ProtelisLoader::referenceFor, nameToFun::get, throwException(), LinkedHashMap::new));
//        final ProgramState programState = new ProgramState(refToFun);
//        // TODO: this deals with cyclic function calls. We can probably do better than this.
//        nameToFun.forEach((fd, fun) -> fun.setBody(Dispatch.block(
//                Objects.requireNonNull(
//                    fd.getBody(),
//                    "The program " + root.getName() + " cannot be created because the required function "
//                    + ((ProtelisModule) fd.eContainer()).getName() + ":" + fd.getName() + " has errors in its body"),
//                programState)
//            )
//        );
//        /*
//         * Create the main program
//         */
        return new SimpleProgramImpl(root, Dispatch.block(root.getProgram()));
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
     * @throws IOException 
     * @throws IllegalArgumentException
     *             when the program has errors
     */
    public static ProtelisProgram parse(final String program) {
        if (Objects.requireNonNull(program, "null is not a valid Protelis program, not a valid Protelis module").isEmpty()) {
            throw new IllegalArgumentException("The empty string is not a valid program, nor a valid module name");
        }
        try {
            if (REGEX_PROTELIS_MODULE.matcher(program).matches()) {
                final String programURI = "classpath:/" + program.replace(':', '/') + "." + PROTELIS_FILE_EXTENSION;
                final Optional<ProtelisProgram> programResource = resourceFromURIString(programURI)
                        .map(ProtelisLoader::parse);
                if (programResource.isPresent()) {
                    return programResource.get();
                }
            }
            return resourceFromURIString(program)
                .map(ProtelisLoader::parse)
                .orElseGet(() -> parseAnonymousModule(program));
        } catch (IOException e) {
            throw new IllegalStateException(program + " looks like an URI, but its resolution failed (see cause)", e);
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
     * @return a {@link ProtelisProgram}
     * @throws IllegalArgumentException
     *             when the program has errors
     */
    public static ProtelisProgram parseAnonymousModule(final String program) {
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
    public static ProtelisProgram parseURI(final String programURI) throws IOException {
        return parse(resourceFromURIString(programURI).orElseThrow(IllegalArgumentException::new));
    }

    private static List<Diagnostic> recursivelyCollectErrors(final Resource resource) {
        return resource.getResourceSet().getResources().parallelStream()
                .map(Resource::getErrors)
                .filter(err -> !err.isEmpty())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

//    private static void recursivelyInitFunctions(final ProtelisModule module,
//            final Map<FunctionDef, ? super FunctionDefinition> nameToFun) {
//        recursivelyInitFunctions(module, nameToFun, new LinkedHashSet<>());
//    }
//
//    private static void recursivelyInitFunctions(
//            final ProtelisModule module,
//            final Map<FunctionDef, ? super FunctionDefinition> nameToFun,
//            final Set<ProtelisModule> completed) {
//        if (!completed.contains(module)) {
//            completed.add(module);
//            /*
//             * Init imports functions, in reverse order
//             */
//            final ImportSection imports = module.getImports();
//            if (imports != null) {
//                imports.getImportDeclarations().stream()
//                    .filter(it -> it instanceof ProtelisImport)
//                    .map(it -> (ProtelisImport) it)
//                    .forEach(it -> recursivelyInitFunctions(it.getModule(), nameToFun, completed));
//            }
//            /*
//             * Init local functions
//             */
//            nameToFun.putAll(module.getDefinitions().stream()
//                .collect(Collectors.toMap(
//                    Function.identity(),
//                    fd -> new FunctionDefinition(Optional.of(module), fd.getName(), toR(extractArgs(fd)))
//                ))
//            );
//        }
//    }

    /**
     * @param program
     *            the program in String format
     * @return a dummy:/ resource that can be used to interpret the program
     */
    public static Resource resourceFromString(final String program) {
        final String programId = "dummy:/protelis-generated-program-"
            + Hashing.sha512().hashString(program, StandardCharsets.UTF_8)
            + ".pt";
        final URI uri = workAroundOpenJ9EMFBug(() -> URI.createURI(programId));
        synchronized (XTEXT) {
            Resource r = XTEXT.get().getResource(uri, false);
            if (r == null) {
                try (InputStream in = new StringInputStream(program)) {
                    loadStringResources(XTEXT.get(), in);
                } catch (IOException e) {
                    throw new IllegalStateException("Couldn't get resource associated with anonymous program: "
                            + e.getMessage(), e);
                }
                r = XTEXT.get().createResource(uri);
                try (InputStream in = new StringInputStream(program)) {
                    r.load(in, XTEXT.get().getLoadOptions());
                } catch (IOException e) {
                    throw new IllegalStateException("I/O error while reading in RAM: this must be tough.", e);
                }
            }
            return r;
        }
    }

    private static Optional<Resource> resourceFromURIString(final String programURI) throws IOException {
        final String realURI = (programURI.startsWith("/") ? "classpath:" : "") + programURI;
        if (RESOLVER.get().getResource(realURI).exists()) {
            loadResourcesRecursively(XTEXT.get(), programURI);
            final URI uri = workAroundOpenJ9EMFBug(() -> URI.createURI(realURI));
            return Optional.of(XTEXT.get().getResource(uri, true));
        } else {
            return Optional.empty();
        }
    }

    private static <R> R workAroundOpenJ9EMFBug(final Supplier<R> fun) {
        try {
            return fun.get();
        } catch (AssertionError e) {
            LOGGER.warn(OPEN_J9_EMF_WORKED_AROUND, e);
            return fun.get();
        }
    }

    private static class Dispatch {

        private static final Cache<EObject, FunctionDefinition> VIRTUAL_METHOD_TABLE = CacheBuilder.newBuilder().weakKeys().build();
        private static AnnotatedTree<?> block(@Nonnull final Block block) {
            final List<Statement> statements = block.getStatements();
            if (statements.size() == 1) {
                return statement(statements.get(0));
            }
            return new All(metadataFor(block), statements.stream().map(it -> statement(it)).collect(Collectors.toList()));
        }

        @SuppressWarnings("unchecked")
        private static <T> AnnotatedTree<T> blockUnsafe(@Nonnull final Block block) {
            return (AnnotatedTree<T>) block(block);
        }

        private static AnnotatedTree<?> alignedMap(@Nonnull final org.protelis.parser.protelis.AlignedMap alMap) {
            return new AlignedMap(
                    metadataFor(alMap),
                    expression(alMap.getArg()),
                    expression(alMap.getCond()),
                    expression(alMap.getOp()),
                    expression(alMap.getDefault()));
        }

        private static AnnotatedTree<?> builtin(@Nonnull final Builtin expression) {
            if (expression instanceof org.protelis.parser.protelis.AlignedMap) {
                return alignedMap((org.protelis.parser.protelis.AlignedMap) expression);
            }
            final Metadata meta = metadataFor(expression);
            if (expression instanceof org.protelis.parser.protelis.Env) {
                return new Env(meta);
            }
            if (expression instanceof org.protelis.parser.protelis.Eval) {
                return new Eval(meta, expression(((org.protelis.parser.protelis.Eval) expression).getArg()));
            }
            if (expression instanceof BuiltinHoodOp) {
                final BuiltinHoodOp hood = (BuiltinHoodOp) expression;
                return new HoodCall(meta, expression(hood.getArg()),
                        HoodOp.get(hood.getName().replace(HOOD_END, "")),
                        hood.isInclusive());
            }
            if (expression instanceof GenericHood) {
                final GenericHood hood = (GenericHood) expression;
                final boolean inclusive = hood.getName().length() > 4;
                final AnnotatedTree<Object> nullResult = expression(hood.getDefault());
                final AnnotatedTree<Field<Object>> field = expression(hood.getArg());
                final VarUse ref = hood.getReference();
                if (ref == null) {
                    return new GenericHoodCall(meta, inclusive, lambda(hood.getOp()), nullResult, field);
                }
                if (ref.getReference() instanceof JvmOperation) {
                    return new GenericHoodCall(meta, inclusive, (JvmOperation) ref.getReference(), nullResult, field);
                }
                return new GenericHoodCall(meta, inclusive, variableUnsafe(ref), nullResult, field);
            }
            if (expression instanceof It) {
                return new Variable(meta, IT);
            }
            if (expression instanceof Mux) {
                final Mux mux = (Mux) expression;
                return new TernaryOp(meta, mux.getName(), expression(mux.getCond()), block(mux.getThen()), block(mux.getElse()));
            }
            if (expression instanceof org.protelis.parser.protelis.Self) {
                return new Self(meta);
            }
            throw new IllegalStateException("Unknown builtin of type " + expression.getClass().getSimpleName());
        }

        @SuppressWarnings("unchecked")
        private static <T> AnnotatedTree<T> expression(final Expression expression) {
            return (AnnotatedTree<T>) expressionRaw(expression);
        }

        private static AnnotatedTree<?> expressionRaw(final Expression expression) {
            if (expression instanceof Builtin) {
                return builtin((Builtin) expression);
            }
            if (expression instanceof org.protelis.parser.protelis.If) {
                return ifOp((org.protelis.parser.protelis.If) expression);
            }
            if (expression instanceof Lambda) {
                return lambda((Lambda) expression);
            }
            if (expression instanceof NBR) {
                return nbr((NBR) expression);
            }
            if (expression instanceof Rep) {
                return rep((Rep) expression);
            }
            if (expression instanceof Scalar) {
                return scalar((Scalar) expression);
            }
            if (expression instanceof Share) {
                return share((Share) expression);
            }
            if (expression instanceof VarUse) {
                return variable((VarUse) expression);
            }
            /*
             * Pure expression
             */
            final List<EObject> elements = expression.getElements();
            final Metadata meta = metadataFor(expression);
            switch (elements.size()) {
                case 1: return new UnaryOp(meta, expression.getName(), expression((Expression) elements.get(0)));
                case 2: 
                final AnnotatedTree<?> first = expression((Expression) expression.getElements().get(0));
                final EObject second = expression.getElements().get(1);
                if (expression.getName() == null && second instanceof InvocationArguments) {
                    // Invoke
                    final InvocationArguments invokeArgs = (InvocationArguments) second;
                    // TODO: Drop "apply", and allow only standard invocations with better system
                    return new Invoke(meta, "apply", first, invocationArguments(invokeArgs));
                }
                if (".".equals(expression.getName()) && second instanceof MethodCall) {
                    // Method call
                    final MethodCall method = (MethodCall) second;
                    return new Invoke(meta, method.getName(), first, invocationArguments(method.getArguments()));
                }
                if (expression.getName() != null) {
                    return new BinaryOp(meta, expression.getName(), first, expression((Expression) second));
                }
            default: throw new IllegalStateException("Unknown AST node " + expression);
            }
        }

        private static List<AnnotatedTree<?>> invocationArguments(@Nonnull final InvocationArguments args) {
            return argumentsToExpressionStream(args)
                .map(Dispatch::expression)
                .collect(ImmutableList.toImmutableList());
        }

        private static Constant<FunctionDefinition> lambda(@Nonnull final Lambda expression) {
            final List<VarDef> arguments = expression instanceof LongLambda
                ? ((LongLambda) expression).getArgs().getArgs()
                : expression instanceof OldLongLambda
                    ? Optional.ofNullable(((OldLongLambda) expression).getArgs()).<List<VarDef>>map(VarDefList::getArgs).orElseGet(Collections::emptyList)
                    : expression instanceof OldShortLambda
                        ? Collections.singletonList(((OldShortLambda) expression).getSingleArg())
                        : Collections.emptyList();
            final FunctionDefinition lambda = new FunctionDefinition(expression, referenceListFor(arguments), () -> block(expression.getBody()));
            return new Constant<>(metadataFor(expression), lambda);
        }

        private static AnnotatedTree<?> local(@Nonnull final Local expression) {
            if (expression instanceof Lambda) {
                return lambda((Lambda) expression);
            }
            if (expression instanceof Builtin) {
                return builtin((Builtin) expression);
            }
            if (expression instanceof Scalar) {
                return scalar((Scalar) expression);
            }
            throw new IllegalStateException("Unknown local of type " + expression.getClass().getSimpleName());
        }

        private static ShareCall<?, ?> rep(final Rep rep) {
            final Metadata meta = metadataFor(rep);
            final RepInitialize init = rep.getInit();
            final Optional<Reference> local = Optional.of(referenceFor(init.getX()));
            final Optional<AnnotatedTree<Object>> yield = Optional.ofNullable(rep.getYields())
                    .map(Yield::getBody)
                    .map(it -> blockUnsafe(it));
            return new ShareCall<>(meta, local, Optional.empty(), expression(init.getW()), block(rep.getBody()), yield);
        }

        private static ShareCall<?, ?> share(final Share share) {
            final ShareInitialize init = share.getInit();
            final Optional<Reference> local = Optional.ofNullable(init.getLocal()).map(ProtelisLoadingUtilities::referenceFor);
            final Optional<Reference> field = Optional.ofNullable(init.getField()).map(ProtelisLoadingUtilities::referenceFor);
            final Optional<AnnotatedTree<Object>> yield = Optional.ofNullable(share.getYields())
                    .map(Yield::getBody)
                    .map(it -> blockUnsafe(it));
            return new ShareCall<>(metadataFor(share), local, field, expression(init.getW()), block(share.getBody()), yield);
        }

        private static <T> NBRCall<T> nbr(final NBR nbr) {
            return new NBRCall<>(metadataFor(nbr), expression(nbr.getArg()));
        }

        private static AnnotatedTree<?> ifOp(final org.protelis.parser.protelis.If ifOp) {
            return new If<>(metadataFor(ifOp), expression(ifOp.getCond()),
                    blockUnsafe(ifOp.getThen()),
                    block(ifOp.getElse()));
        }

        private static AnnotatedTree<?> scalar(@Nonnull final Scalar expression) {
            final Metadata meta = metadataFor(expression);
            if (expression instanceof BooleanVal) {
                return new Constant<>(meta, ((BooleanVal) expression).isVal());
            }
            if (expression instanceof DoubleVal) {
                return new Constant<>(meta, ((DoubleVal) expression).getVal());
            }
            if (expression instanceof StringVal) {
                return new Constant<>(meta, ((StringVal) expression).getVal());
            }
            if (expression instanceof TupleVal) {
                return new CreateTuple(meta, expressionList(((TupleVal) expression).getArgs()));
            }
            throw new IllegalStateException("Unknown scalar of type " + expression.getClass().getSimpleName());
        }

        private static List<AnnotatedTree<?>> expressionList(@Nullable final ExpressionList list) {
            return Optional.ofNullable(list)
                .<List<Expression>>map(ExpressionList::getArgs)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(Dispatch::expression)
                .collect(ImmutableList.toImmutableList());
        }

        private static AnnotatedTree<?> statement(@Nonnull final Statement statement) {
            if (statement instanceof Expression) {
                return expression((Expression) statement);
            }
            if (statement instanceof Declaration) {
                return declaration((Declaration) statement);
            }
            if (statement instanceof Assignment) {
                return assignment((Assignment) statement);
            }
            if (statement instanceof IfWithoutElse) {
                return ifWithoutElse((IfWithoutElse) statement);
            }
            throw new IllegalStateException("Unknown statement of type " + statement.getClass().getSimpleName());
        }

        private static If<?> ifWithoutElse(final IfWithoutElse ifOp) {
            final Metadata meta = metadataFor(ifOp);
            final List<AnnotatedTree<?>> then = ifOp.getThen().stream()
                    .map(it -> statement(it))
                    .collect(Collectors.toList());
            final AnnotatedTree<?> thenBranch = then.size() == 1
                    ? then.get(0)
                    : new All(meta, then);
            return new If<>(meta, expression(ifOp.getCond()), thenBranch, null);
        }

        private static AnnotatedTree<?> variable(@Nonnull final VarUse expression) {
            /*
             * VarUse can reference:
             * 
             * - JvmFeature (imported methods or fields)
             * 
             * - FunctionDef (imported or locally defined Protelis functions)
             * 
             * - VarDef (variables defined in scope, parameters in scope)
             * 
             * The former can be treated as constants. They are immutable and do not require restriction (as they cannot bind a Field).
             */
            final Metadata meta = metadataFor(expression);
            final EObject ref = expression.getReference();
            if (ref instanceof JvmFeature) {
                /*
                 * JVMFeature is not serializable
                 */
                return new JvmConstant(meta, new JVMEntity((JvmFeature) ref));
            }
            if (ref instanceof FunctionDef) {
                final FunctionDef functionDefinition = (FunctionDef) ref;
                FunctionDefinition target;
                try {
                    target = VIRTUAL_METHOD_TABLE.get(functionDefinition,
                            () -> new FunctionDefinition(functionDefinition, () -> block(functionDefinition.getBody())));
                    return new Constant<>(meta, target);
                } catch (ExecutionException e) {
                    throw new IllegalStateException(e);
                }
            }
            return new Variable(meta, referenceFor(ref));
        }

        private static AssignmentOp assignment(final Assignment assignment) {
            return new AssignmentOp(metadataFor(assignment), referenceFor(assignment.getRefVar()), expression(assignment.getRight()));
        }

        private static AssignmentOp declaration(final Declaration declaration) {
            final VarDef name = declaration.getName();
            return new AssignmentOp(metadataFor(declaration), referenceFor(name), expression(declaration.getRight()));
        }

        @SuppressWarnings("unchecked")
        private static <T> AnnotatedTree<T> variableUnsafe(final VarUse expression) {
            return (AnnotatedTree<T>) variable(expression);
        }

    }

}
