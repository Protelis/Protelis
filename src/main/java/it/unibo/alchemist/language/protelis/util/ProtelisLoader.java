/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.language.protelis.util;

import it.unibo.alchemist.language.protelis.AlignedMap;
import it.unibo.alchemist.language.protelis.All;
import it.unibo.alchemist.language.protelis.BinaryOp;
import it.unibo.alchemist.language.protelis.Constant;
import it.unibo.alchemist.language.protelis.CreateTuple;
import it.unibo.alchemist.language.protelis.CreateVar;
import it.unibo.alchemist.language.protelis.DotOperator;
import it.unibo.alchemist.language.protelis.Dt;
import it.unibo.alchemist.language.protelis.Eval;
import it.unibo.alchemist.language.protelis.FunctionCall;
import it.unibo.alchemist.language.protelis.FunctionDefinition;
import it.unibo.alchemist.language.protelis.HoodCall;
import it.unibo.alchemist.language.protelis.If;
import it.unibo.alchemist.language.protelis.MethodCall;
import it.unibo.alchemist.language.protelis.NBRCall;
import it.unibo.alchemist.language.protelis.NBRRange;
import it.unibo.alchemist.language.protelis.NumericConstant;
import it.unibo.alchemist.language.protelis.ProtelisStandaloneSetup;
import it.unibo.alchemist.language.protelis.Random;
import it.unibo.alchemist.language.protelis.RepCall;
import it.unibo.alchemist.language.protelis.Self;
import it.unibo.alchemist.language.protelis.TernaryOp;
import it.unibo.alchemist.language.protelis.UnaryOp;
import it.unibo.alchemist.language.protelis.Variable;
import it.unibo.alchemist.language.protelis.datatype.Field;
import it.unibo.alchemist.language.protelis.interfaces.AnnotatedTree;
import it.unibo.alchemist.language.protelis.protelis.Assignment;
import it.unibo.alchemist.language.protelis.protelis.Block;
import it.unibo.alchemist.language.protelis.protelis.BooleanVal;
import it.unibo.alchemist.language.protelis.protelis.Declaration;
import it.unibo.alchemist.language.protelis.protelis.DoubleVal;
import it.unibo.alchemist.language.protelis.protelis.Expression;
import it.unibo.alchemist.language.protelis.protelis.FunctionDef;
import it.unibo.alchemist.language.protelis.protelis.Import;
import it.unibo.alchemist.language.protelis.protelis.Program;
import it.unibo.alchemist.language.protelis.protelis.RepInitialize;
import it.unibo.alchemist.language.protelis.protelis.Statement;
import it.unibo.alchemist.language.protelis.protelis.StringVal;
import it.unibo.alchemist.language.protelis.protelis.TupleVal;
import it.unibo.alchemist.language.protelis.protelis.VAR;
import it.unibo.alchemist.utils.FasterString;
import it.unibo.alchemist.utils.L;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.StringInputStream;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.google.inject.Injector;

/**
 * @author Danilo Pianini
 *
 */
public final class ProtelisLoader {

	private static final AtomicInteger IDGEN = new AtomicInteger();
	private static final XtextResourceSet XTEXT = createResourceSet();
	private static final Pattern REGEX_PROTELIS_IMPORT = Pattern.compile("import\\s+((?:\\w+:)*\\w+)\\s+", Pattern.DOTALL);
	private static final PathMatchingResourcePatternResolver RESOLVER = new PathMatchingResourcePatternResolver();
	private static final String PROTELIS_FILE_EXTENSION = "pt";
	private static final String UNCHECKED = "unchecked";
	private static final String ASSIGNMENT_NAME = "=";
	private static final String DOT_NAME = ".";
	private static final String REP_NAME = "rep";
	private static final String IF_NAME = "if";
	private static final String DT_NAME = "dt";
	private static final String PI_NAME = "pi";
	private static final String E_NAME = "e";
	private static final String RAND_NAME = "random";
	private static final String LAMBDA_NAME = "->";
	private static final String SELF_NAME = "self";
	private static final String EVAL_NAME = "eval";
	private static final String NBR_NAME = "nbr";
	private static final String NBR_RANGE = "nbrRange";
	private static final String ALIGNED_MAP = "alignedMap";
	private static final String MUX_NAME = "mux";
	private static final String HOOD_END = "Hood";
	private static final List<String> BINARY_OPERATORS = Arrays.stream(Op2.values()).map(Op2::toString).collect(Collectors.toList());
	private static final List<String> UNARY_OPERATORS = Arrays.stream(Op1.values()).map(Op1::toString).collect(Collectors.toList());

	private ProtelisLoader() {
	}

	/**
	 * @param programURI
	 *            Protelis program file to execute. It must be a either a valid
	 *            {@link URI} string, for instance
	 *            "file:///home/user/protelis/myProgram" or a location relative
	 *            to the classpath. In case, for instance,
	 *            "/my/package/myProgram.pt" is passed, it will be automatically
	 *            get converted to "classpath:/my/package/myProgram.pt". All the
	 *            Protelis modules your program relies upon must be included in
	 *            your Java classpath. The Java classpath scanning is done
	 *            automatically by this constructor, linking is performed by
	 *            Xtext transparently. {@link URI}s of type "platform:/" are
	 *            supported, for those who work within an Eclipse environment.
	 * @return a {@link Pair} of {@link AnnotatedTree} (the program) and
	 *         {@link FunctionDefinition} (containing the available functions)
	 */
	public static IProgram parse(final String programURI) {
		return parse(resourceFromURIString(programURI));
	}
	
	private static Resource resourceFromURIString(final String programURI) {
		loadResourcesRecursively(XTEXT, programURI);
		final String realURI = (programURI.startsWith("/") ? "classpath:" : "") + programURI;
		final URI uri = URI.createURI(realURI);
		return XTEXT.getResource(uri, true);
	}
	
	private static void loadResourcesRecursively(final XtextResourceSet target, final String programURI) {
		loadResourcesRecursively(target, programURI, new LinkedHashSet<>());
	}
	
	private static void loadResourcesRecursively(final XtextResourceSet target, final String programURI, final Set<String> alreadyInQueue) {
		final String realURI = (programURI.startsWith("/") ? "classpath:" : "") + programURI;
		if (!alreadyInQueue.contains(realURI)) {
			alreadyInQueue.add(realURI);
			final URI uri = URI.createURI(realURI);
			final org.springframework.core.io.Resource protelisFile = RESOLVER.getResource(realURI);
			try {
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
			} catch (final IOException e) {
				L.warn("Cannot load " + protelisFile);
				L.warn(e);
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
		final XtextResourceSet xrs = createResourceSet();
		final Resource r = xrs.createResource(URI.createURI("dummy:/protelis-generated-program-" + IDGEN.getAndIncrement() + ".pt"));
		final InputStream in = new StringInputStream(program);
		try {
			r.load(in, xrs.getLoadOptions());
		} catch (IOException e) {
			L.error("I/O error while reading in RAM: this must be tough.");
		}
		return r;
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
	public static IProgram parse(final Resource resource) {
		if (!resource.getErrors().isEmpty()) {
			for (final Diagnostic d : resource.getErrors()) {
				final StringBuilder b = new StringBuilder("Error at line ");
				b.append(d.getLine());
				b.append(": ");
				b.append(d.getMessage());
				L.error(b.toString());
			}
			throw new IllegalArgumentException("Your program has syntax errors. Feed me something usable, please.");
		}
		final Program root = (Program) resource.getContents().get(0);
		/*
		 * Create the function headers.
		 * 
		 * 1) Take all the imports in reverse order (the first declared is the
		 * one actually declared in case of name conflict), insert them with the
		 * two possible names (fully qualified and short).
		 * This operation must be recursive (for dealing with imports of
		 * imports).
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
		funToFun.forEach((fd, fun) -> fun.setBody((AnnotatedTree<?>) parseBlock(fd.getBody(), nameToFun, funToFun, id)));
		/*
		 * Create the main program
		 */
		return new SimpleProgramImpl(root, parseBlock(root.getProgram(), nameToFun, funToFun, id), nameToFun);
	}
	
	private static void recursivelyInitFunctions(
			final Program module,
			final Map<FasterString, FunctionDefinition> nameToFun,
			final Map<FunctionDef, FunctionDefinition> funToFun) {
		recursivelyInitFunctions(module, nameToFun, funToFun, new HashSet<>(), true);
	}
	
	private static void recursivelyInitFunctions(
			final Program module,
			final Map<FasterString, FunctionDefinition> nameToFun,
			final Map<FunctionDef, FunctionDefinition> funToFun,
			final Set<Program> completed,
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

	private static <T> AnnotatedTree<?> parseStatement(final Statement e,
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
	
	private static MethodCall parseMethod(final JvmOperation jvmOp, final List<Expression> args,
			final Map<FasterString, FunctionDefinition> nameToFun,
			final Map<FunctionDef, FunctionDefinition> funToFun,
			final AtomicInteger id) {
		final boolean ztatic = jvmOp.isStatic();
		final List<AnnotatedTree<?>> arguments = parseArgs(args, nameToFun, funToFun, id);
		final String classname = jvmOp.getDeclaringType().getQualifiedName();
		try {
			final Class<?> clazz = Class.forName(classname);
			/*
			 * TODO: Check for return type and params: if param is Field and
			 * return type is not then L.warn()
			 */
			Stream<Method> methods = Arrays.stream(clazz.getMethods());
			if (ztatic) {
				methods = methods.filter(m -> Modifier.isStatic(m.getModifiers()));
			}
			/*
			 * Same number of arguments
			 */
			final int parameterCount = jvmOp.getParameters().size();
			methods = methods.filter(m -> m.getParameterCount() == parameterCount);
			/*
			 * Same name
			 */
			final String methodName = jvmOp.getSimpleName();
			methods = methods.filter(m -> m.getName().equals(methodName));
			/*
			 * There should be only one left - otherwise we have overloading,
			 * and to properly deal with that we need type checking. The
			 * following collection operation is for debug and warning purposes,
			 * and should be removed as soon as we have a proper way to deal
			 * with overloading in place. TODO
			 */
			final List<Method> res = methods.collect(Collectors.toList());
			if (res.size() > 1) {
				final StringBuilder sb = new StringBuilder(64);
				sb.append("Method ");
				sb.append(jvmOp.getQualifiedName());
				sb.append('/');
				sb.append(parameterCount);
				sb.append(" is overloaded by:\n");
				res.forEach(m -> {
					sb.append(m.toString());
					sb.append('\n'); // NOPMD
				});
				sb.append("Protelis can not (yet) properly deal with that.");
				L.warn(sb.toString());
			}
			if (res.isEmpty()) {
				throw new IllegalStateException("Can not bind any method that satisfies the name " + jvmOp.getQualifiedName()  + "/" + parameterCount + ".");
			}
			return new MethodCall(res.get(0), arguments, ztatic);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Class " + classname + " could not be found in classpath.");
		} catch (SecurityException e) {
			throw new IllegalStateException("Class " + classname + " could not be loaded due to security permissions.");
		} catch (Error e) {
			throw new IllegalStateException("An error occured while loading class " + classname + ".");
		}
		
	}

	private static FunctionCall parseFunction(final FunctionDef f, final List<Expression> args,
			final Map<FasterString, FunctionDefinition> nameToFun,
			final Map<FunctionDef, FunctionDefinition> funToFun,
			final AtomicInteger id) {
		final FunctionDefinition fun = funToFun.get(f);
		Objects.requireNonNull(fun);
		final List<AnnotatedTree<?>> arguments = parseArgs(args, nameToFun, funToFun, id);
		return new FunctionCall(fun, arguments);
	}

	private static List<AnnotatedTree<?>> parseArgs(final List<Expression> args,
			final Map<FasterString, FunctionDefinition> nameToFun,
			final Map<FunctionDef, FunctionDefinition> funToFun,
			final AtomicInteger id) {
		return args.stream().map(e -> parseExpression(e, nameToFun, funToFun, id)).collect(Collectors.toList());
	}

	private static <T> AnnotatedTree<?> parseExpression(final Expression e,
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
				throw new IllegalStateException("I do not know how I should interpret a call to a " + eRef.getClass().getSimpleName() + " object.");
			}
		}
		final String name = e.getName();
		if (name == null) {
			/*
			 * Envelope: recurse in
			 */
			final EObject val = e.getV();
			return parseExpression((Expression) val, nameToFun, funToFun, id);
		}
		if (BINARY_OPERATORS.contains(name) && e.getLeft() != null && e.getRight() != null) {
			return new BinaryOp(name, parseExpression(e.getLeft(), nameToFun, funToFun, id), parseExpression(e.getRight(), nameToFun, funToFun, id));
		}
		if (UNARY_OPERATORS.contains(name) && e.getLeft() == null && e.getRight() != null) {
			return new UnaryOp(name, parseExpression(e.getRight(), nameToFun, funToFun, id));
		}
		if (name.equals(REP_NAME)) {
			final RepInitialize ri = e.getInit().getArgs().get(0);
			final String x = ri.getX().getName();
			return new RepCall<>(new FasterString(x), parseExpression(ri.getW(), nameToFun, funToFun, id), parseBlock(e.getBody(), nameToFun, funToFun, id));
		}
		if (name.equals(IF_NAME)) {
			@SuppressWarnings(UNCHECKED)
			final AnnotatedTree<Boolean> cond = (AnnotatedTree<Boolean>) parseExpression(e.getCond(), nameToFun, funToFun, id);
			@SuppressWarnings(UNCHECKED)
			final AnnotatedTree<T> then = (AnnotatedTree<T>) parseBlock(e.getThen(), nameToFun, funToFun, id);
			@SuppressWarnings(UNCHECKED)
			final AnnotatedTree<T> elze = (AnnotatedTree<T>) parseBlock(e.getElse(), nameToFun, funToFun, id);
			return new If<>(cond, then, elze);
		}
		if (name.equals(PI_NAME)) {
			return new Constant<>(Math.PI);
		}
		if (name.equals(E_NAME)) {
			return new Constant<>(Math.E);
		}
		if (name.equals(RAND_NAME)) {
			return new Random();
		}
		if (name.equals(DT_NAME)) {
			return new Dt();
		}
		if (name.equals(SELF_NAME)) {
			return new Self();
		}
		if (name.equals(NBR_NAME)) {
			return new NBRCall(parseExpression(e.getArg(), nameToFun, funToFun, id));
		}
		if (name.equals(ALIGNED_MAP)) {
			@SuppressWarnings(UNCHECKED)
			final AnnotatedTree<Field> arg = (AnnotatedTree<Field>) parseExpression(e.getArg(), nameToFun, funToFun, id);
			@SuppressWarnings(UNCHECKED)
			final AnnotatedTree<FunctionDefinition> cond = (AnnotatedTree<FunctionDefinition>) parseExpression(e.getCond(), nameToFun, funToFun, id);
			@SuppressWarnings(UNCHECKED)
			final AnnotatedTree<FunctionDefinition> op = (AnnotatedTree<FunctionDefinition>) parseExpression(e.getOp(), nameToFun, funToFun, id);
			final AnnotatedTree<?> def = parseExpression(e.getDefault(), nameToFun, funToFun, id);
			return new AlignedMap(arg, cond, op, def);
		}
		if (name.equals(LAMBDA_NAME)) {
			final FunctionDefinition lambda = new FunctionDefinition("l$" + id.getAndIncrement(), extractArgsFromLambda(e));
			final AnnotatedTree<?> body = parseBlock(e.getBody(), nameToFun, funToFun, id);
			lambda.setBody(body);
			return new Constant<>(lambda);
		}
		if (name.equals(NBR_RANGE)) {
			return new NBRRange();
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
		if (name.endsWith(HOOD_END)) {
			final String op = name.replace(HOOD_END, "");
			final HoodOp hop = HoodOp.get(op);
			if (hop == null) {
				throw new UnsupportedOperationException("Unsupported hood operation: " + op);
			}
			@SuppressWarnings(UNCHECKED)
			final AnnotatedTree<Field> arg = (AnnotatedTree<Field>) parseExpression(e.getArg(), nameToFun, funToFun, id);
			return new HoodCall(arg, hop, e.isInclusive());
		}
		throw new UnsupportedOperationException("Unsupported operation: " + (e.getName() != null ? e.getName() : "Unknown"));
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
		return e.getLambdaArgs() != null && e.getLambdaArgs().getArgs() != null ? e.getLambdaArgs().getArgs() : Collections.emptyList();
	}
	
}
