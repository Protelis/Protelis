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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.NotImplementedException;
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
import org.protelis.lang.interpreter.impl.NumericConstant;
import org.protelis.lang.interpreter.impl.RepCall;
import org.protelis.lang.interpreter.impl.Self;
import org.protelis.lang.interpreter.impl.TernaryOp;
import org.protelis.lang.interpreter.impl.UnaryOp;
import org.protelis.lang.interpreter.impl.Variable;
import org.protelis.lang.util.HoodOp;
import org.protelis.lang.util.Op1;
import org.protelis.lang.util.Op2;
import org.protelis.parser.ProtelisStandaloneSetup;
import org.protelis.parser.protelis.Assignment;
import org.protelis.parser.protelis.Block;
import org.protelis.parser.protelis.BooleanVal;
import org.protelis.parser.protelis.Declaration;
import org.protelis.parser.protelis.DoubleVal;
import org.protelis.parser.protelis.Expression;
import org.protelis.parser.protelis.FunctionDef;
import org.protelis.parser.protelis.Import;
import org.protelis.parser.protelis.Module;
import org.protelis.parser.protelis.RepInitialize;
import org.protelis.parser.protelis.Statement;
import org.protelis.parser.protelis.StringVal;
import org.protelis.parser.protelis.TupleVal;
import org.protelis.parser.protelis.VAR;
import org.protelis.vm.ProtelisProgram;
import org.protelis.vm.impl.SimpleProgramImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.google.inject.Injector;

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
    private static final String ASSIGNMENT_NAME = "=";
    private static final String DOT_NAME = ".";
    private static final String REP_NAME = "rep";
    private static final String IF_NAME = "if";
    private static final String PI_NAME = "pi";
    private static final String E_NAME = "e";
    private static final String LAMBDA_NAME = "->";
    private static final String SELF_NAME = "self";
    private static final String ENV_NAME = "env";
    private static final String EVAL_NAME = "eval";
    private static final String NBR_NAME = "nbr";
    private static final String ALIGNED_MAP = "alignedMap";
    private static final String MUX_NAME = "mux";
    private static final String HOOD_NAME = "hood";
    private static final String HOOD_END = "Hood";
    private static final List<String> BINARY_OPERATORS = Arrays.stream(Op2.values())
            .map(Op2::toString).collect(Collectors.toList());
    private static final List<String> UNARY_OPERATORS = Arrays.stream(Op1.values())
            .map(Op1::toString).collect(Collectors.toList());

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
        final Map<FasterString, FunctionDefinition> nameToFun = new LinkedHashMap<>();
        final Map<FunctionDef, FunctionDefinition> funToFun = new LinkedHashMap<>();
        recursivelyInitFunctions(root, nameToFun, funToFun);
        /*
         * Function definitions are in place, now create function bodies. Bodies
         * may contain lambdas, lambdas are named using processing order, as a
         * consequence function bodies must be evaluated sequentially.
         */
        final AtomicInteger id = new AtomicInteger();
        funToFun.forEach(
                (fd, fun) -> fun.setBody((AnnotatedTree<?>) parseBlock(fd.getBody(), nameToFun, funToFun, id)));
        /*
         * Create the main program
         */
        return new SimpleProgramImpl(root, parseBlock(root.getProgram(), nameToFun, funToFun, id), nameToFun);
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
            final Map<FasterString, FunctionDefinition> nameToFun,
            final Map<FunctionDef, FunctionDefinition> funToFun) {
        recursivelyInitFunctions(module, nameToFun, funToFun, new HashSet<>(), true);
    }

    private static void recursivelyInitFunctions(
            final Module module,
            final Map<FasterString, FunctionDefinition> nameToFun,
            final Map<FunctionDef, FunctionDefinition> funToFun,
            final Set<Module> completed,
            final boolean initPrivate) {
        if (!completed.contains(module)) {
            completed.add(module);
            /*
             * Init imports functions, in reverse order
             */
            final EList<Import> imports = module.getProtelisImport();
            for (int i = imports.size() - 1; i >= 0; i--) {
                final Import protelisImport = imports.get(i);
                recursivelyInitFunctions(protelisImport.getModule(), nameToFun, funToFun, completed, false);
            }
            /*
             * Init local functions
             */
            final Stream<FunctionDef> fds = module.getDefinitions().parallelStream();
            fds.filter(fd -> initPrivate || fd.isPublic()).forEachOrdered(fd -> {
                final String fname = fd.getName();
                final String fullName = module.getName() + ":" + fname;
                final FasterString ffname = new FasterString(fname);
                final FasterString ffullName = new FasterString(fullName);
                final FunctionDefinition def = new FunctionDefinition(ffullName, extractArgs(fd));
                nameToFun.put(ffullName, def);
                nameToFun.put(ffname, def);
                funToFun.put(fd, def);
            });
        }
    }

    private static <T> AnnotatedTree<?> parseBlock(
            final Block e,
            final Map<FasterString, FunctionDefinition> nameToFun,
            final Map<FunctionDef, FunctionDefinition> funToFun,
            final AtomicInteger id) {
        final AnnotatedTree<?> first = parseStatement(e.getFirst(), nameToFun, funToFun, id);
        Block next = e.getOthers();
        final List<AnnotatedTree<?>> statements = new LinkedList<>();
        statements.add(first);
        for (; next != null; next = next.getOthers()) {
            statements.add(parseStatement(next.getFirst(), nameToFun, funToFun, id));
        }
        return new All(statements);
    }

    private static <T> AnnotatedTree<?> parseStatement(
            final Statement e,
            final Map<FasterString, FunctionDefinition> nameToFun,
            final Map<FunctionDef, FunctionDefinition> funToFun,
            final AtomicInteger id) {
        if (e instanceof Expression) {
            return parseExpression((Expression) e, nameToFun, funToFun, id);
        }
        if (e instanceof Declaration || e instanceof Assignment) {
            String name;
            final boolean isAssignment = e.getName().equals(ASSIGNMENT_NAME);
            if (isAssignment) {
                name = ((Assignment) e).getRefVar().getName();
            } else {
                name = e.getName();
            }
            return new CreateVar(name, parseExpression(e.getRight(), nameToFun, funToFun, id), !isAssignment);
        }
        throw new NotImplementedException("Implement support for nodes of type: " + e.getClass());
    }

    private static MethodCall parseMethod(
            final JvmOperation jvmOp,
            final List<Expression> args,
            final Map<FasterString, FunctionDefinition> nameToFun,
            final Map<FunctionDef, FunctionDefinition> funToFun,
            final AtomicInteger id) {
        final List<AnnotatedTree<?>> arguments = parseArgs(args, nameToFun, funToFun, id);
        final String classname = jvmOp.getDeclaringType().getQualifiedName();
        try {
            return new MethodCall(jvmOp, arguments);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Class " + classname + " could not be found in classpath.");
        } catch (SecurityException e) {
            throw new IllegalStateException("Class " + classname + " could not be loaded due to security permissions.");
        } catch (Error e) {
            throw new IllegalStateException("An error occured while loading class " + classname + ".");
        }

    }

    private static FunctionCall parseFunction(
            final FunctionDef f,
            final List<Expression> args,
            final Map<FasterString, FunctionDefinition> nameToFun,
            final Map<FunctionDef, FunctionDefinition> funToFun,
            final AtomicInteger id) {
        final FunctionDefinition fun = funToFun.get(f);
        Objects.requireNonNull(fun);
        final List<AnnotatedTree<?>> arguments = parseArgs(args, nameToFun, funToFun, id);
        return new FunctionCall(fun, arguments);
    }

    private static List<AnnotatedTree<?>> parseArgs(final List<Expression> args,
            final Map<FasterString, FunctionDefinition> nameToFun, final Map<FunctionDef, FunctionDefinition> funToFun,
            final AtomicInteger id) {
        return args.stream().map(e -> parseExpression(e, nameToFun, funToFun, id)).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private static <T> AnnotatedTree<?> parseExpression(
            final Expression e,
            final Map<FasterString, FunctionDefinition> nameToFun,
            final Map<FunctionDef, FunctionDefinition> funToFun,
            final AtomicInteger id) {
        if (e instanceof DoubleVal) {
            return new NumericConstant(((DoubleVal) e).getVal());
        }
        if (e instanceof StringVal) {
            return new Constant<>(((StringVal) e).getVal());
        }
        if (e instanceof BooleanVal) {
            return new Constant<>(((BooleanVal) e).isVal());
        }
        if (e instanceof TupleVal) {
            final Function<Expression, AnnotatedTree<?>> f = (exp) -> parseExpression(exp, nameToFun, funToFun, id);
            final List<Expression> expr = extractArgs((TupleVal) e);
            final Stream<AnnotatedTree<?>> treestr = expr.stream().map(f);
            final AnnotatedTree<?>[] args = treestr.toArray((i) -> new AnnotatedTree[i]);
            return new CreateTuple(args);
        }
        if (e instanceof VAR) {
            return new Variable(((VAR) e).getName());
        }
        if (e == null) {
            throw new IllegalArgumentException("null expression, this is a bug.");
        }
        final Optional<EObject> ref = Optional.ofNullable(e.getReference());
        final String name = e.getName();
        if (name == null) {
            if (ref.isPresent()) {
                /*
                 * Function or method call
                 */
                final EObject eRef = ref.get();
                if (eRef instanceof JvmOperation) {
                    final JvmOperation m = (JvmOperation) eRef;
                    return parseMethod(m, extractArgs(e), nameToFun, funToFun, id);
                } else if (eRef instanceof FunctionDef) {
                    final FunctionDef fun = (FunctionDef) eRef;
                    return parseFunction(fun, extractArgs(e), nameToFun, funToFun, id);
                } else {
                    throw new IllegalStateException(
                            "I do not know how I should interpret a call to a "
                            + eRef.getClass().getSimpleName() + " object.");
                }
            }
            /*
             * Envelope: recurse in
             */
            final EObject val = e.getV();
            return parseExpression((Expression) val, nameToFun, funToFun, id);
        }
        if (BINARY_OPERATORS.contains(name) && e.getLeft() != null && e.getRight() != null) {
            return new BinaryOp(name, parseExpression(e.getLeft(), nameToFun, funToFun, id),
                    parseExpression(e.getRight(), nameToFun, funToFun, id));
        }
        if (UNARY_OPERATORS.contains(name) && e.getLeft() == null && e.getRight() != null) {
            return new UnaryOp(name, parseExpression(e.getRight(), nameToFun, funToFun, id));
        }
        if (name.equals(REP_NAME)) {
            final RepInitialize ri = e.getInit().getArgs().get(0);
            final String x = ri.getX().getName();
            return new RepCall<>(
                    new FasterString(x),
                    parseExpression(ri.getW(), nameToFun, funToFun, id),
                    parseBlock(e.getBody(), nameToFun, funToFun, id));
        }
        if (name.equals(IF_NAME)) {
            final AnnotatedTree<Boolean> cond = (AnnotatedTree<Boolean>)
                parseExpression(e.getCond(), nameToFun, funToFun, id);
            final AnnotatedTree<T> then = (AnnotatedTree<T>) parseBlock(e.getThen(), nameToFun, funToFun, id);
            final AnnotatedTree<T> elze = (AnnotatedTree<T>) parseBlock(e.getElse(), nameToFun, funToFun, id);
            return new If<>(cond, then, elze);
        }
        if (name.equals(PI_NAME)) {
            return new Constant<>(Math.PI);
        }
        if (name.equals(E_NAME)) {
            return new Constant<>(Math.E);
        }
        if (name.equals(SELF_NAME)) {
            return new Self();
        }
        if (name.equals(ENV_NAME)) {
            return new Env();
        }
        if (name.equals(NBR_NAME)) {
            return new NBRCall(parseExpression(e.getArg(), nameToFun, funToFun, id));
        }
        if (name.equals(ALIGNED_MAP)) {
            final AnnotatedTree<Field> arg = (AnnotatedTree<Field>)
                parseExpression(e.getArg(), nameToFun, funToFun, id);
            final AnnotatedTree<FunctionDefinition> cond = (AnnotatedTree<FunctionDefinition>)
                parseExpression(e.getCond(), nameToFun, funToFun, id);
            final AnnotatedTree<FunctionDefinition> op = (AnnotatedTree<FunctionDefinition>)
                parseExpression(e.getOp(), nameToFun, funToFun, id);
            final AnnotatedTree<?> def = parseExpression(e.getDefault(), nameToFun, funToFun, id);
            return new AlignedMap(arg, cond, op, def);
        }
        if (name.equals(LAMBDA_NAME)) {
            final FunctionDefinition lambda = new FunctionDefinition("l$" + id.getAndIncrement(),
                    extractArgsFromLambda(e));
            final AnnotatedTree<?> body = parseBlock(e.getBody(), nameToFun, funToFun, id);
            lambda.setBody(body);
            return new Constant<>(lambda);
        }
        if (name.equals(EVAL_NAME)) {
            final AnnotatedTree<?> arg = parseExpression(e.getArg(), nameToFun, funToFun, id);
            return new Eval(arg);
        }
        if (name.equals(DOT_NAME)) {
            final AnnotatedTree<?> target = parseExpression(e.getLeft(), nameToFun, funToFun, id);
            final List<AnnotatedTree<?>> args = parseArgs(extractArgs(e), nameToFun, funToFun, id);
            return new DotOperator(e.getMethodName(), target, args);
        }
        if (name.equals(MUX_NAME)) {
            final AnnotatedTree<?> cond = parseExpression(e.getCond(), nameToFun, funToFun, id);
            final AnnotatedTree<?> then = parseBlock(e.getThen(), nameToFun, funToFun, id);
            final AnnotatedTree<?> elze = parseBlock(e.getElse(), nameToFun, funToFun, id);
            return new TernaryOp(MUX_NAME, cond, then, elze);
        }
        if (name.startsWith(HOOD_NAME)) {
            assert e.getDefault() != null;
            assert e.getArg() != null;
            final AnnotatedTree<?> defVal = parseExpression(e.getDefault(), nameToFun, funToFun, id);
            final AnnotatedTree<Field> target = (AnnotatedTree<Field>) parseExpression(e.getArg(), nameToFun, funToFun, id);
            final boolean inclusive = !name.equals(HOOD_NAME);
            if (ref.isPresent()) {
                final EObject eRef = ref.get();
                if (eRef instanceof JvmOperation) {
                    final JvmOperation fun = (JvmOperation) eRef;
                    try {
                        return new GenericHoodCall(inclusive, fun, defVal, target);
                    } catch (ClassNotFoundException e1) {
                        L.error("No class in the classpath can be found for " + fun);
                        throw new IllegalStateException(fun + " unavailable in the classpath.");
                    }
                } else {
                    assert eRef instanceof FunctionDef;
                    final AnnotatedTree<FunctionDefinition> fun = new Constant<>(funToFun.get((FunctionDef) eRef));
                    return new GenericHoodCall(inclusive, fun, defVal, target);
                }
            } else {
                /*
                 * Lambda
                 */
                assert e.getOp() != null;
                final AnnotatedTree<FunctionDefinition> fun = (AnnotatedTree<FunctionDefinition>)
                        parseExpression(e.getOp(), nameToFun, funToFun, id);
                return new GenericHoodCall(inclusive, fun, defVal, target);
            }
        }
        if (name.endsWith(HOOD_END)) {
            final String op = name.replace(HOOD_END, "");
            final HoodOp hop = HoodOp.get(op);
            if (hop == null) {
                throw new UnsupportedOperationException("Unsupported hood operation: " + op);
            }
            final AnnotatedTree<Field> arg = (AnnotatedTree<Field>)
                    parseExpression(e.getArg(), nameToFun, funToFun, id);
            return new HoodCall(arg, hop, e.isInclusive());
        }
        throw new UnsupportedOperationException(
                "Unsupported operation: " + (e.getName() != null ? e.getName() : "Unknown"));
    }

    private static List<Expression> extractArgs(final Expression e) {
        return e.getArgs() != null && e.getArgs().getArgs() != null ? e.getArgs().getArgs() : Collections.emptyList();
    }

    private static List<Expression> extractArgs(final TupleVal e) {
        return e.getArgs() != null && e.getArgs().getArgs() != null ? e.getArgs().getArgs() : Collections.emptyList();
    }

    private static List<VAR> extractArgs(final FunctionDef e) {
        return e.getArgs() != null && e.getArgs().getArgs() != null ? e.getArgs().getArgs() : Collections.emptyList();
    }

    private static List<VAR> extractArgsFromLambda(final Expression e) {
        return e.getLambdaArgs() != null && e.getLambdaArgs().getArgs() != null
                ? e.getLambdaArgs().getArgs()
                : Collections.emptyList();
    }

}
