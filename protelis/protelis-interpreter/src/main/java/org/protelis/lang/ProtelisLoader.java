/*******************************************************************************
 * Copyright (C) 2014, 2015, Danilo Pianini and contributors
 * listed in the project's build.gradle or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE.txt in this project's top directory.
 *******************************************************************************/
package org.protelis.lang;

import static java8.util.Optional.empty;
import static java8.util.stream.StreamSupport.stream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.apache.commons.codec.binary.Base64;
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
import org.protelis.lang.interpreter.impl.DotOperator;
import org.protelis.lang.interpreter.impl.Env;
import org.protelis.lang.interpreter.impl.Eval;
import org.protelis.lang.interpreter.impl.FunctionCall;
import org.protelis.lang.interpreter.impl.GenericHoodCall;
import org.protelis.lang.interpreter.impl.HoodCall;
import org.protelis.lang.interpreter.impl.If;
import org.protelis.lang.interpreter.impl.JvmConstant;
import org.protelis.lang.interpreter.impl.MethodCall;
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
import org.protelis.parser.protelis.Call;
import org.protelis.parser.protelis.DoubleVal;
import org.protelis.parser.protelis.ExprList;
import org.protelis.parser.protelis.Expression;
import org.protelis.parser.protelis.FunctionDef;
import org.protelis.parser.protelis.GenericHood;
import org.protelis.parser.protelis.IfWithoutElse;
import org.protelis.parser.protelis.ImportSection;
import org.protelis.parser.protelis.Lambda;
import org.protelis.parser.protelis.Local;
import org.protelis.parser.protelis.Mux;
import org.protelis.parser.protelis.NBR;
import org.protelis.parser.protelis.ProtelisImport;
import org.protelis.parser.protelis.ProtelisModule;
import org.protelis.parser.protelis.Rep;
import org.protelis.parser.protelis.RepInitialize;
import org.protelis.parser.protelis.Scalar;
import org.protelis.parser.protelis.Share;
import org.protelis.parser.protelis.ShareInitialize;
import org.protelis.parser.protelis.SideEffect;
import org.protelis.parser.protelis.Statement;
import org.protelis.parser.protelis.StringVal;
import org.protelis.parser.protelis.TupleVal;
import org.protelis.parser.protelis.VarDef;
import org.protelis.parser.protelis.VarDefList;
import org.protelis.parser.protelis.VarUse;
import org.protelis.parser.protelis.Yield;
import org.protelis.vm.ProtelisProgram;
import org.protelis.vm.impl.SimpleProgramImpl;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.google.common.base.Charsets;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import com.google.inject.Injector;

import java8.util.Maps;
import java8.util.Optional;
import java8.util.function.BinaryOperator;
import java8.util.function.Functions;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

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
    private static final String PROTELIS_FILE_EXTENSION = "pt";
    private static final LoadingCache<Object, Reference> REFERENCES = CacheBuilder.newBuilder()
        .expireAfterAccess(1, TimeUnit.MINUTES)
        .build(new CacheLoader<Object, Reference>() {
            @Override
            public Reference load(final Object key) {
                return new Reference(key);
            }
        });
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

    private static List<AnnotatedTree<?>> callArgs(final Call call, final ProgramState env) {
        return exprListArgs(call.getArgs(), env);
    }

    private static List<AnnotatedTree<?>> exprListArgs(final ExprList l, final ProgramState env) {
        return Optional.ofNullable(l)
                .map(ExprList::getArgs)
                .map(StreamSupport::stream)
                .map(s -> s.map(e -> Dispatch.expression(e, env))
                        .collect(Collectors.<AnnotatedTree<?>>toList()))
                .orElse(Collections.emptyList());
    }

    private static List<VarDef> extractArgs(final FunctionDef e) {
        return e.getArgs() != null && e.getArgs().getArgs() != null ? e.getArgs().getArgs() : Collections.emptyList();
    }

    private static void loadResourcesRecursively(final XtextResourceSet target, final String programURI)
            throws IOException {
        loadResourcesRecursively(target, programURI, new LinkedHashSet<>());
    }

    private static void loadResourcesRecursively(
            final XtextResourceSet target,
            final String programURI,
            final Set<String> alreadyInQueue) throws IOException {
        final String realURI = (programURI.startsWith("/") ? "classpath:" : "") + programURI;
        if (LOADED_RESOURCES.get().getIfPresent(realURI) == null && !alreadyInQueue.contains(realURI)) {
            alreadyInQueue.add(realURI);
            final URI uri = URI.createURI(realURI);
            final org.springframework.core.io.Resource protelisFile = RESOLVER.get().getResource(realURI);
            final InputStream is = protelisFile.getInputStream();
            final String ss = IOUtils.toString(is, "UTF-8");
            is.close();
            final Matcher matcher = REGEX_PROTELIS_IMPORT.matcher(ss);
            while (matcher.find()) {
                final int start = matcher.start(1);
                final int end = matcher.end(1);
                final String imp = ss.substring(start, end);
                final String classpathResource = "classpath:/" + imp.replace(":", "/") + "." + PROTELIS_FILE_EXTENSION;
                loadResourcesRecursively(target, classpathResource, alreadyInQueue);
            }
            LOADED_RESOURCES.get().put(realURI, target.getResource(uri, true));
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
         * Function definitions are in place, now create function bodies.
         */
        final Map<Reference, FunctionDefinition> refToFun = stream(nameToFun.keySet())
                .collect(Collectors.toMap(ProtelisLoader::toR, nameToFun::get, throwException(), LinkedHashMap::new));
        final ProgramState programState = new ProgramState(refToFun);
        Maps.forEach(nameToFun, (fd, fun) -> fun.setBody(Dispatch.block(
                Objects.requireNonNull(
                    fd.getBody(),
                    "The program " + root.getName() + " cannot be created because the required function "
                    + ((ProtelisModule) fd.eContainer()).getName() + ":" + fd.getName() + " has errors in its body"),
                programState)
            )
        );
        /*
         * Create the main program
         */
        return new SimpleProgramImpl(root, Dispatch.block(root.getProgram(), programState), refToFun);
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
    public static ProtelisProgram parse(final String program) {
        if (Objects.requireNonNull(program, "null is not a valid Protelis program, not a valid Protelis module").isEmpty()) {
            throw new IllegalArgumentException("The empty string is not a valid program, nor a valid module name");
        }
        try {
            if (REGEX_PROTELIS_MODULE.matcher(Objects.requireNonNull(program, "The Protelis Program can not be null"))
                    .matches()) {
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
        return parse(resourceFromURIString(programURI));
    }

    private static List<Diagnostic> recursivelyCollectErrors(final Resource resource) {
        return StreamSupport.parallelStream(resource.getResourceSet().getResources())
                .map(Resource::getErrors)
                .filter(err -> !err.isEmpty())
                .flatMap(StreamSupport::stream)
                .collect(Collectors.toList());
    }

    private static void recursivelyInitFunctions(final ProtelisModule module,
            final Map<FunctionDef, ? super FunctionDefinition> nameToFun) {
        recursivelyInitFunctions(module, nameToFun, new LinkedHashSet<>());
    }

    private static void recursivelyInitFunctions(
            final ProtelisModule module,
            final Map<FunctionDef, ? super FunctionDefinition> nameToFun,
            final Set<ProtelisModule> completed) {
        if (!completed.contains(module)) {
            completed.add(module);
            /*
             * Init imports functions, in reverse order
             */
            final ImportSection imports = module.getImports();
            if (imports != null) {
                stream(imports.getImportDeclarations())
                    .filter(it -> it instanceof ProtelisImport)
                    .map(it -> (ProtelisImport) it)
                    .forEach(it -> recursivelyInitFunctions(it.getModule(), nameToFun, completed));
            }
            /*
             * Init local functions
             */
            nameToFun.putAll(stream(module.getDefinitions())
                .collect(Collectors.toMap(
                    Functions.identity(),
                    fd -> new FunctionDefinition(Optional.of(module), fd.getName(), toR(extractArgs(fd)))
                ))
            );
        }
    }

    /**
     * @param program
     *            the program in String format
     * @return a dummy:/ resource that can be used to interpret the program
     */
    public static Resource resourceFromString(final String program) {
        final URI uri = URI.createURI("dummy:/protelis-generated-program-"
        + Hashing.sha512().hashString(program, StandardCharsets.UTF_8)
        + ".pt");
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

    private static Resource resourceFromURIString(final String programURI) throws IOException {
        loadResourcesRecursively(XTEXT.get(), programURI);
        final String realURI = (programURI.startsWith("/") ? "classpath:" : "") + programURI;
        final URI uri = URI.createURI(realURI);
        return XTEXT.get().getResource(uri, true);
    }

    private static <T> BinaryOperator<T> throwException() {
        return (x, y) -> {
            throw new IllegalStateException("This is a bug in Protelis.");
        };
    }

    private static List<Reference> toR(final List<?> l) {
        return stream(l).map(ProtelisLoader::toR).collect(Collectors.toList());
    }

    private static Reference toR(final Object o) {
        try {
            return REFERENCES.get(o);
        } catch (ExecutionException e) {
            throw new IllegalStateException("Unable to create a reference for " + o, e);
        }
    }

    private static class Dispatch {

        private static AnnotatedTree<?> block(@Nonnull final Block block, @Nonnull final ProgramState state) {
            if (block instanceof Statement) {
                return statement((Statement) block, state);
            }
            final List<AnnotatedTree<?>> statements = new LinkedList<>();
            for (Block b = (Block) block; b != null; b = b.getNext()) {
                statements.add(block(b.getFirst(), state));
            }
            return new All(metadataFor(block), statements);
        }

        @SuppressWarnings("unchecked")
        private static <T> AnnotatedTree<T> blockUnsafe(@Nonnull final Block block, @Nonnull final ProgramState state) {
            return (AnnotatedTree<T>) block(block, state);
        }

        private static AnnotatedTree<?> builtin(@Nonnull final Builtin expression, @Nonnull final ProgramState state) {
            final Metadata meta = metadataFor(expression);
            if (expression instanceof org.protelis.parser.protelis.AlignedMap) {
                final org.protelis.parser.protelis.AlignedMap alMap = (org.protelis.parser.protelis.AlignedMap) expression;
                return new AlignedMap(
                    meta,
                    expression(alMap.getArg(), state),
                    expression(alMap.getCond(), state),
                    expression(alMap.getOp(), state),
                    expression(alMap.getDefault(), state));
            }
            if (expression instanceof org.protelis.parser.protelis.Env) {
                return new Env(meta);
            }
            if (expression instanceof org.protelis.parser.protelis.Eval) {
                return new Eval(meta, expression(((org.protelis.parser.protelis.Eval) expression).getArg(), state));
            }
            if (expression instanceof BuiltinHoodOp) {
                final BuiltinHoodOp hood = (BuiltinHoodOp) expression;
                return new HoodCall(meta, expression(hood.getArg(), state),
                        HoodOp.get(hood.getName().replace(HOOD_END, "")),
                        hood.isInclusive());
            }
            if (expression instanceof GenericHood) {
                final GenericHood hood = (GenericHood) expression;
                final boolean inclusive = hood.getName().length() > 4;
                final AnnotatedTree<?> nullResult = expression(hood.getDefault(), state);
                final AnnotatedTree<Field> field = expression(hood.getArg(), state);
                final EObject ref = hood.getReference();
                if (ref == null) {
                    return new GenericHoodCall(meta, inclusive, lambda(hood.getOp(), state), nullResult, field);
                }
                if (ref instanceof VarUse) {
                    final VarUse refVar = (VarUse) ref;
                    if (refVar.getReference() instanceof JvmOperation) {
                        return new GenericHoodCall(meta, inclusive, (JvmOperation) refVar.getReference(), nullResult, field);
                    }
                    return new GenericHoodCall(meta, inclusive, variableUnsafe(refVar, state), nullResult, field);
                } else {
                    throw new IllegalStateException("Unexpected type of function in hood call: " + ref.getClass());
                }
            }
            if (expression instanceof Mux) {
                final Mux mux = (Mux) expression;
                return new TernaryOp(meta, mux.getName(), expression(mux.getCond(), state), block(mux.getThen(), state), block(mux.getElse(), state));
            }
            if (expression instanceof org.protelis.parser.protelis.Self) {
                return new Self(meta);
            }
            throw new IllegalStateException("Unknown builtin of type " + expression.getClass().getSimpleName());
        }

        @SuppressWarnings("unchecked")
        private static <T> AnnotatedTree<T> expression(final Expression expression, final ProgramState state) {
            return (AnnotatedTree<T>) expressionRaw(expression, state);
        }

        private static AnnotatedTree<?> expressionRaw(final Expression expression, final ProgramState state) {
            if (expression.getV() != null) {
                return primary(expression.getV(), state);
            }
            final Metadata meta = metadataFor(expression);
            final String methodName = expression.getMethodName();
            if (methodName != null) {
                final AnnotatedTree<?> target = expression(expression.getLeft(), state);
                final List<AnnotatedTree<?>> arguments = exprListArgs(expression.getArgs(), state);
                return new DotOperator(meta, methodName, target, arguments);
            }
            if (expression.getLeft() == null) {
                return new UnaryOp(meta, expression.getName(), expression(expression.getRight(), state));
            }
            final AnnotatedTree<?> left = expression(expression.getLeft(), state);
            if (expression.getRight() == null) {
                return new UnaryOp(meta, expression.getName(), left);
            }
            return new BinaryOp(meta, expression.getName(), left, expression(expression.getRight(), state));
        }

        private static AnnotatedTree<FunctionDefinition> lambda(final Lambda expression, final ProgramState state) {
            final EObject argobj = expression.getArgs();
            final List<VarDef> args = argobj == null ? Collections.emptyList()
                    : argobj instanceof VarDef ? Lists.newArrayList((VarDef) expression.getArgs())
                    : ((VarDefList) argobj).getArgs();
            final AnnotatedTree<?> body = block(expression.getBody(), state);
            final List<AnnotatedTree<?>> bodyEntities = new ArrayList<>();
            bodyEntities.add(body);
            for (int i = 0; i < bodyEntities.size(); i++) { //NOPMD: it can't be a foreach, it changes the collection while iterating.
                bodyEntities.addAll(bodyEntities.get(i).getBranches());
            }
            final String base = Base64.encodeBase64String(
                    Hashing.sha256().hashString(bodyEntities.toString(), Charsets.UTF_8).asBytes());
            final FunctionDefinition lambda = new FunctionDefinition(empty(), "$" + base, toR(args));
            lambda.setBody(body);
            return new Constant<>(metadataFor(expression), lambda);
        }

        private static AnnotatedTree<?> local(@Nonnull final Local expression, @Nonnull final ProgramState state) {
            if (expression instanceof Lambda) {
                return lambda((Lambda) expression, state);
            }
            if (expression instanceof Builtin) {
                return builtin((Builtin) expression, state);
            }
            if (expression instanceof Scalar) {
                return scalar((Scalar) expression, state);
            }
            throw new IllegalStateException("Unknown local of type " + expression.getClass().getSimpleName());
        }

        private static AnnotatedTree<?> primary(final EObject val, final ProgramState state) {
            if (val instanceof VarUse) {
                return variable((VarUse) val, state);
            }
            if (val instanceof Local) {
                return local((Local) val, state);
            }
            if (val instanceof Expression) {
                return expression((Expression) val, state);
            }
            final Metadata meta = metadataFor(val);
            if (val instanceof Call) {
                final Call call = (Call) val;
                final EObject ref = call.getReference();
                if (ref instanceof JvmOperation) {
                    return new MethodCall(meta, (JvmOperation) ref, callArgs(call, state));
                }
                return new FunctionCall(meta, state.resolveFunction(toR(ref)), callArgs(call, state));
            }
            if (val instanceof Rep) {
                final Rep rep = (Rep) val;
                final RepInitialize init = rep.getInit();
                final Optional<Reference> local = Optional.of(toR(init.getX()));
                final Optional<AnnotatedTree<Object>> yield = Optional.ofNullable(rep.getYields())
                        .map(Yield::getBody)
                        .map(it -> blockUnsafe(it, state));
                return new ShareCall<>(meta, local, empty(), expression(init.getW(), state), block(rep.getBody(), state), yield);
            }
            if (val instanceof Share) {
                final Share s = (Share) val;
                final ShareInitialize init = s.getInit();
                final Optional<Reference> local = Optional.ofNullable(init.getLocal()).map(ProtelisLoader::toR);
                final Optional<Reference> field = Optional.ofNullable(init.getField()).map(ProtelisLoader::toR);
                final Optional<AnnotatedTree<Object>> yield = Optional.ofNullable(s.getYields())
                        .map(Yield::getBody)
                        .map(it -> blockUnsafe(it, state));
                return new ShareCall<>(meta, local, field, expression(init.getW(), state), block(s.getBody(), state), yield);
            }
            if (val instanceof NBR) {
                return new NBRCall(meta, expression(((NBR) val).getArg(), state));
            }
            if (val instanceof org.protelis.parser.protelis.If) {
                final org.protelis.parser.protelis.If ifop = (org.protelis.parser.protelis.If) val;
                return new If<>(meta, expression(ifop.getCond(), state),
                        blockUnsafe(ifop.getThen(), state),
                        block(ifop.getElse(), state));
            }
            throw new IllegalStateException("Unknown primary expression type of type " + val.getClass().getSimpleName());
        }

        private static AnnotatedTree<?> scalar(@Nonnull final Scalar expression, @Nonnull final ProgramState state) {
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
                return new CreateTuple(meta, exprListArgs(((TupleVal) expression).getArgs(), state));
            }
            throw new IllegalStateException("Unknown scalar of type " + expression.getClass().getSimpleName());
        }

        private static AnnotatedTree<?> sideEffect(@Nonnull final SideEffect statement, @Nonnull final ProgramState state) {
            final Metadata meta = metadataFor(statement);
            if (statement instanceof Assignment) {
                final Assignment assignment = (Assignment) statement;
                return new AssignmentOp(meta, toR(assignment.getRefVar()), expression(assignment.getRight(), state));
            }
            if (statement instanceof IfWithoutElse) {
                final IfWithoutElse ifOp = (IfWithoutElse) statement;
                final List<AnnotatedTree<?>> then = ifOp.getThen().stream()
                        .map(it -> statement(it, state))
                        .collect(java.util.stream.Collectors.toList());
                final AnnotatedTree<?> thenBranch = then.size() == 1 ? then.get(0)
                        : new All(meta, then);
                return new If<>(meta, expression(ifOp.getCond(), state), thenBranch, null);
            }
            if (statement instanceof VarDef) {
                return variableDefinition((VarDef) statement, state);
            }
            throw new IllegalStateException("Unknown side effect of type " + statement.getClass().getSimpleName());
        }

        private static AnnotatedTree<?> statement(@Nonnull final Statement expression, @Nonnull final ProgramState state) {
            if (expression instanceof Expression) {
                return expression((Expression) expression, state);
            }
            return sideEffect((SideEffect) expression, state);
        }

        private static AnnotatedTree<?> variable(final VarUse expression, final ProgramState state) {
            final Metadata meta = metadataFor(expression);
            final EObject ref = ((VarUse) expression).getReference();
            if (ref instanceof JvmFeature) {
                return new JvmConstant(meta, new JVMEntity((JvmFeature) ref));
            }
            return new Variable(meta, toR(ref));
        }

        private static AssignmentOp variableDefinition(final VarDef definition, final ProgramState state) {
            return new AssignmentOp(metadataFor(definition), toR(definition), expression(definition.getRight(), state));
        }

        @SuppressWarnings("unchecked")
        private static <T> AnnotatedTree<T> variableUnsafe(final VarUse expression, final ProgramState state) {
            return (AnnotatedTree<T>) variable(expression, state);
        }

    }

    private static final class ProgramState {
        private final Map<Reference, FunctionDefinition> functions;
        private ProgramState(final Map<Reference, FunctionDefinition> functions) {
            this.functions = functions;
        }
        public FunctionDefinition resolveFunction(final Reference r) {
            return functions.get(r);
        }
    }

}
