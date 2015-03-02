/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.utils;

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
import it.unibo.alchemist.language.protelis.Op1;
import it.unibo.alchemist.language.protelis.Op2;
import it.unibo.alchemist.language.protelis.ProtelisStandaloneSetup;
import it.unibo.alchemist.language.protelis.RepCall;
import it.unibo.alchemist.language.protelis.Self;
import it.unibo.alchemist.language.protelis.TernaryOp;
import it.unibo.alchemist.language.protelis.UnaryOp;
import it.unibo.alchemist.language.protelis.Variable;
import it.unibo.alchemist.language.protelis.HoodOp;
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
import it.unibo.alchemist.model.implementations.nodes.ProtelisNode;
import it.unibo.alchemist.model.interfaces.IEnvironment;
import it.unibo.alchemist.model.interfaces.IReaction;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.NotImplementedException;
import org.danilopianini.lang.Pair;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;

/**
 * @author Danilo Pianini
 *
 */
public final class ParseUtils {

	private static final AtomicInteger IDGEN = new AtomicInteger();
	private static final XtextResourceSet XTEXT;
	private static final String UNCHECKED = "unchecked";
	private static final String ASSIGNMENT_NAME = "=";
	private static final String DOT_NAME = ".";
	private static final String METHOD_NAME = "#";
	private static final String REP_NAME = "rep";
	private static final String IF_NAME = "if";
	private static final String DT_NAME = "dt";
	private static final String PI_NAME = "pi";
	private static final String E_NAME = "e";
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

	static {
		final Injector guiceInjector = new ProtelisStandaloneSetup().createInjectorAndDoEMFRegistration();
		XTEXT = guiceInjector.getInstance(XtextResourceSet.class);
		XTEXT.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, true);
	}

	private ParseUtils() {
	}

	/**
	 * @param env environment
	 * @param node node
	 * @param reaction reaction
	 * @param program Protelis program
	 * @return a {@link Pair} of {@link AnnotatedTree} (the program) and {@link FunctionDefinition} (containing the available functions)
	 */
	public static Pair<AnnotatedTree<?>, Map<FasterString, FunctionDefinition>> parse(
			final IEnvironment<Object> env,
			final ProtelisNode node,
			final IReaction<Object> reaction,
			final String program) {
		final Resource r = XTEXT.createResource(URI.createURI("dummy:/" + IDGEN.getAndIncrement() + ".pt"));
		final InputStream in = new ByteArrayInputStream(program.getBytes(Charset.forName("UTF-8")));
		try {
			r.load(in, XTEXT.getLoadOptions());
		} catch (IOException e) {
			L.error("I/O error while reading in RAM: this must be tough.");
		}
		if (!r.getErrors().isEmpty()) {
			for (final Diagnostic d : r.getErrors()) {
				final StringBuilder b = new StringBuilder("Error at line ");
				b.append(d.getLine());
				b.append(": ");
				b.append(d.getMessage());
				L.error(b.toString());
			}
			throw new IllegalArgumentException("Your program has syntax errors. Feed me something usable, please.");
		}
		final Program root = (Program) r.getContents().get(0);
		/*
		 * Set the package
		 */
		
		/*
		 * Import the referenced methods
		 */
		final Stream<Import> istr = root.getImports().parallelStream();
		final Map<Pair<String, Integer>, Method> imports = new ConcurrentHashMap<>();
		istr.forEach(imp -> parseImport(imp, imports));
		/*
		 * Create the function headers
		 */
		Stream<FunctionDef> fds = root.getDefinitions().parallelStream();
		Map<FasterString, FunctionDefinition> functions;
		functions = fds.collect(Collectors.toMap(fd -> {
			return new FasterString(fd.getName());
		}, fd -> {
			return new FunctionDefinition(fd.getName(), extractArgs(fd));
		}));
		/*
		 * Create function bodies.
		 * Bodies may contain lambdas, lambdas are named using processing order, as a consequence function bodies must be evaluated sequentially.
		 */
		final AtomicInteger id = new AtomicInteger();
		fds = root.getDefinitions().stream();
		fds.forEach(fd -> {
			final FunctionDefinition def = functions.get(new FasterString(fd.getName()));
			def.setBody((AnnotatedTree<?>) parseBlock(fd.getBody(), imports, functions, env, node, reaction, id));
		});
		/*
		 * Create the main program
		 */
		return new Pair<>(parseBlock(root.getProgram(), imports, functions, env, node, reaction, id), functions);
	}

	private static <T> AnnotatedTree<?> parseBlock(final Block e, final Map<Pair<String, Integer>, Method> imports, final Map<FasterString, FunctionDefinition> functions, final IEnvironment<Object> env, final ProtelisNode node, final IReaction<Object> reaction, final AtomicInteger id) {
		final AnnotatedTree<?> first = parseStatement(e.getFirst(), imports, functions, env, node, reaction, id);
		Block next = e.getOthers();
		final List<AnnotatedTree<?>> statements = new LinkedList<>();
		statements.add(first);
		for (; next != null; next = next.getOthers()) {
			statements.add(parseStatement(next.getFirst(), imports, functions, env, node, reaction, id));
		}
		return new All(statements);
	}

	private static <T> AnnotatedTree<?> parseStatement(final Statement e, final Map<Pair<String, Integer>, Method> imports, final Map<FasterString, FunctionDefinition> defs, final IEnvironment<Object> env, final ProtelisNode node, final IReaction<Object> reaction, final AtomicInteger id) {
		if (e instanceof Expression) {
			return parseExpression((Expression) e, imports, defs, env, node, reaction, id);
		}
		if (e instanceof Declaration || e instanceof Assignment) {
			String name;
			final boolean isAssignment = e.getName().equals(ASSIGNMENT_NAME);
			if (isAssignment) {
				name = ((Assignment) e).getRefVar().getName();
			} else {
				name = e.getName();
			}
			return new CreateVar(name, parseExpression(e.getRight(), imports, defs, env, node, reaction, id), !isAssignment);
		}
		throw new NotImplementedException("Implement support for nodes of type: " + e.getClass());
	}
	
	private static void parseImport(final Import imp, final Map<Pair<String, Integer>, Method> imports) {
		try {
			final int initialsize = imports.size();
			final String classname = imp.getClass_();
			Stream<Method> ms = Arrays.stream(Class.forName(classname).getMethods());
			/*
			 * TODO: Check for return type and params: if param is Field and
			 * return type is not then L.warn()
			 */
			final String methodName = (imp.getMethod() == null) ? imp.getName() : imp.getMethod();
			if (imp.getMethod() == null && methodName.equals("*")) {
				//ms = ms.filter(m -> Modifier.isStatic(m.getModifiers()));
				ms.forEach(m -> imports.put(new Pair<>(m.getName(), m.getParameterCount() + (Modifier.isStatic(m.getModifiers()) ? 0 : 1)), m));
			} else {
				//ms = ms.filter(m -> Modifier.isStatic(m.getModifiers());
				ms = ms.filter(m -> m.getName().equals(methodName));
				ms.forEach(m -> imports.put(new Pair<>(imp.getName(), m.getParameterCount() + (Modifier.isStatic(m.getModifiers()) ? 0 : 1)), m));
			}
			if (imports.size() == initialsize) {
				L.warn("No method found for " + imp.getClass_() + "." + imp.getName());
			}
		} catch (SecurityException | ClassNotFoundException e) {
			L.error(e);
		}
	}

	private static MethodCall parseMethod(final String i, final List<Expression> args, final Map<Pair<String, Integer>, Method> imports, final Map<FasterString, FunctionDefinition> defs, final IEnvironment<Object> env, final ProtelisNode node, final IReaction<Object> reaction, final AtomicInteger id) {
		final Method mi = imports.get(new Pair<>(i, args.size()));
		final boolean ztatic = Modifier.isStatic(mi.getModifiers());
		final List<AnnotatedTree<?>> arguments = parseArgs(args, imports, defs, env, node, reaction, id);
		return new MethodCall(mi, arguments, ztatic);
	}

	private static FunctionCall parseFunction(final String f, final List<Expression> args, final Map<Pair<String, Integer>, Method> imports, final Map<FasterString, FunctionDefinition> defs, final IEnvironment<Object> env, final ProtelisNode node, final IReaction<Object> reaction, final AtomicInteger id) {
		final FunctionDefinition fdef = defs.get(new FasterString(f));
		final List<AnnotatedTree<?>> arguments = parseArgs(args, imports, defs, env, node, reaction, id);
		return new FunctionCall(fdef, arguments);
	}

	private static List<AnnotatedTree<?>> parseArgs(final List<Expression> args, final Map<Pair<String, Integer>, Method> imports, final Map<FasterString, FunctionDefinition> defs, final IEnvironment<Object> env, final ProtelisNode node, final IReaction<Object> reaction, final AtomicInteger id) {
		return args.stream().map(e -> parseExpression(e, imports, defs, env, node, reaction, id)).collect(Collectors.toList());
	}

	private static <T> AnnotatedTree<?> parseExpression(final Expression e, final Map<Pair<String, Integer>, Method> imports, final Map<FasterString, FunctionDefinition> defs, final IEnvironment<Object> env, final ProtelisNode node, final IReaction<Object> reaction, final AtomicInteger id) {
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
			final Function<Expression, AnnotatedTree<?>> f = (exp) -> parseExpression(exp, imports, defs, env, node, reaction, id);
			final List<Expression> expr = extractArgs((TupleVal) e);
			final Stream<AnnotatedTree<?>> treestr = expr.stream().map(f);
			final AnnotatedTree<?>[] args = treestr.toArray((i) -> new AnnotatedTree[i]);
			return new CreateTuple(args);
		}
		if (e instanceof VAR) {
			return new Variable(((VAR) e).getName(), node, env);
		}
		if (e == null) {
			throw new IllegalArgumentException("null expression, this is a bug.");
		}
		final String name = e.getName();
		if (name == null) {
			if (e.getFunction() != null) {
				return parseFunction(e.getFunction().getName(), extractArgs(e), imports, defs, env, node, reaction, id);
			}
			/*
			 * Envelope: recurse in
			 */
			final EObject val = e.getV();
			return parseExpression((Expression) val, imports, defs, env, node, reaction, id);
		}
		if (name.equals(METHOD_NAME)) {
			return parseMethod(e.getMethod(), extractArgs(e), imports, defs, env, node, reaction, id);
		}
		if (BINARY_OPERATORS.contains(name) && e.getLeft() != null && e.getRight() != null) {
			return new BinaryOp(name, parseExpression(e.getLeft(), imports, defs, env, node, reaction, id), parseExpression(e.getRight(), imports, defs, env, node, reaction, id));
		}
		if (UNARY_OPERATORS.contains(name) && e.getLeft() == null && e.getRight() != null) {
			return new UnaryOp(name, parseExpression(e.getRight(), imports, defs, env, node, reaction, id));
		}
		if (name.equals(REP_NAME)) {
			final RepInitialize ri = e.getInit().getArgs().get(0);
			final String x = ri.getX().getName();
			return new RepCall<>(new FasterString(x), parseExpression(ri.getW(), imports, defs, env, node, reaction, id), parseBlock(e.getBody(), imports, defs, env, node, reaction, id));
		}
		if (name.equals(IF_NAME)) {
			@SuppressWarnings(UNCHECKED)
			final AnnotatedTree<Boolean> cond = (AnnotatedTree<Boolean>) parseExpression(e.getCond(), imports, defs, env, node, reaction, id);
			@SuppressWarnings(UNCHECKED)
			final AnnotatedTree<T> then = (AnnotatedTree<T>) parseBlock(e.getThen(), imports, defs, env, node, reaction, id);
			@SuppressWarnings(UNCHECKED)
			final AnnotatedTree<T> elze = (AnnotatedTree<T>) parseBlock(e.getElse(), imports, defs, env, node, reaction, id);
			return new If<>(cond, then, elze);
		}
		if (name.equals(PI_NAME)) {
			return new Constant<>(Math.PI);
		}
		if (name.equals(E_NAME)) {
			return new Constant<>(Math.E);
		}
		if (name.equals(DT_NAME)) {
			return new Dt(reaction);
		}
		if (name.equals(SELF_NAME)) {
			return new Self();
		}
		if (name.equals(NBR_NAME)) {
			return new NBRCall(parseExpression(e.getArg(), imports, defs, env, node, reaction, id), env);
		}
		if (name.equals(ALIGNED_MAP)) {
			@SuppressWarnings(UNCHECKED)
			final AnnotatedTree<Field> arg = (AnnotatedTree<Field>) parseExpression(e.getArg(), imports, defs, env, node, reaction, id);
			@SuppressWarnings(UNCHECKED)
			final AnnotatedTree<FunctionDefinition> cond = (AnnotatedTree<FunctionDefinition>) parseExpression(e.getCond(), imports, defs, env, node, reaction, id);
			@SuppressWarnings(UNCHECKED)
			final AnnotatedTree<FunctionDefinition> op = (AnnotatedTree<FunctionDefinition>) parseExpression(e.getOp(), imports, defs, env, node, reaction, id);
			final AnnotatedTree<?> def = parseExpression(e.getDefault(), imports, defs, env, node, reaction, id);
			return new AlignedMap(arg, cond, op, def);
		}
		if (name.equals(LAMBDA_NAME)) {
			final FunctionDefinition lambda = new FunctionDefinition("l$" + id.getAndIncrement(), extractArgsFromLambda(e));
			final AnnotatedTree<?> body = parseBlock(e.getBody(), imports, defs, env, node, reaction, id);
			lambda.setBody(body);
			return new Constant<>(lambda);
		}
		if (name.equals(NBR_RANGE)) {
			return new NBRRange(env);
		}
		if (name.equals(EVAL_NAME)) {
			final AnnotatedTree<?> arg = parseExpression(e.getArg(), imports, defs, env, node, reaction, id);
			return new Eval(arg, env, node, reaction);
		}
		if (name.equals(DOT_NAME)) {
			final AnnotatedTree<?> target = parseExpression(e.getLeft(), imports, defs, env, node, reaction, id);
			final List<AnnotatedTree<?>> args = parseArgs(extractArgs(e), imports, defs, env, node, reaction, id);
			return new DotOperator(e.getMethodName(), target, args);
		}
		if (name.equals(MUX_NAME)) {
			final AnnotatedTree<?> cond = parseExpression(e.getCond(), imports, defs, env, node, reaction, id);
			final AnnotatedTree<?> then = parseBlock(e.getThen(), imports, defs, env, node, reaction, id);
			final AnnotatedTree<?> elze = parseBlock(e.getElse(), imports, defs, env, node, reaction, id);
			return new TernaryOp(MUX_NAME, cond, then, elze);
		}
		if (name.endsWith(HOOD_END)) {
			final String op = name.replace(HOOD_END, "");
			final HoodOp hop = HoodOp.get(op);
			if (hop == null) {
				throw new UnsupportedOperationException("Unsupported hood operation: " + op);
			}
			@SuppressWarnings(UNCHECKED)
			final AnnotatedTree<Field> arg = (AnnotatedTree<Field>) parseExpression(e.getArg(), imports, defs, env, node, reaction, id);
			return new HoodCall(arg, hop, e.getInclusive() != null);
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
	
	/**
	 * @param s
	 *            the string to filter
	 * @return the same string without spaces
	 */
	public static String filterSpaces(final String s) {
		return s.replaceAll("\\s+", "");
	}

}
