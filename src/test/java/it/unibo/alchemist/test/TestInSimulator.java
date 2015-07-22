/*
 * Copyright (C) 2010-2014, Danilo Pianini and contributors
 * listed in the project's pom.xml file.
 * 
 * This file is part of Alchemist, and is distributed under the terms of
 * the GNU General Public License, with a linking exception, as described
 * in the file LICENSE in the Alchemist distribution's top directory.
 */
package it.unibo.alchemist.test;

import static org.junit.Assert.*;
import it.unibo.alchemist.core.implementations.Simulation;
import it.unibo.alchemist.core.interfaces.ISimulation;
import it.unibo.alchemist.language.EnvironmentBuilder;
import it.unibo.alchemist.language.protelis.ProtelisDSLStandaloneSetup;
import it.unibo.alchemist.language.protelis.datatype.Field;
import it.unibo.alchemist.model.implementations.actions.ProtelisProgram;
import it.unibo.alchemist.model.implementations.times.DoubleTime;
import it.unibo.alchemist.model.interfaces.IEnvironment;
import it.unibo.alchemist.model.interfaces.INode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.Charsets;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.generator.InMemoryFileSystemAccess;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.google.inject.Injector;


public class TestInSimulator {
	
	private static final XtextResourceSet XTEXT;
	private static final Injector INJECTOR;
	
	static {
		new org.eclipse.emf.mwe.utils.StandaloneSetup().setPlatformUri(".");
		INJECTOR = new ProtelisDSLStandaloneSetup().createInjectorAndDoEMFRegistration();
		XTEXT = INJECTOR.getInstance(XtextResourceSet.class);
		XTEXT.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
	}
	
	@Test
	public void testSimple01() throws InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, SAXException, IOException, ParserConfigurationException, InterruptedException, ExecutionException { 
		runSimulation("simple01.psim", 2, checkProgramValueOnAll(v -> assertEquals(1.0, v)));
	}
	
	@Test
	public void testNbr01() throws InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, SAXException, IOException, ParserConfigurationException, InterruptedException, ExecutionException { 
		runSimulation("nbr01.psim", 2, checkProgramValueOnAll(v -> {
			assertTrue(v instanceof Field);
			final Field res = (Field) v;
			res.valIterator().forEach(fval -> assertEquals(1.0, fval));
		}));
	}
	
	@SafeVarargs
	private static <T> void runSimulation(final String relativeFilePath, final double finalTime, final Consumer<IEnvironment<Object>>... checkProcedures) throws InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, SAXException, IOException, ParserConfigurationException, InterruptedException, ExecutionException  {
		Resource res = XTEXT.getResource(URI.createURI("classpath:/simulations/" + relativeFilePath), true);
		IGenerator generator = INJECTOR.getInstance(IGenerator.class);
		InMemoryFileSystemAccess fsa = INJECTOR.getInstance(InMemoryFileSystemAccess.class);
		generator.doGenerate(res, fsa);
		Collection<CharSequence> files = fsa.getTextFiles().values();
		if (files.size() != 1) {
			fail();
		}
		final ByteArrayInputStream strIS = new ByteArrayInputStream(files.stream().findFirst().get().toString().getBytes(Charsets.UTF_8));
		final IEnvironment<Object> env = EnvironmentBuilder.build(strIS).get().getEnvironment();
		final ISimulation<Object> sim = new Simulation<>(env, new DoubleTime(finalTime));
		sim.play();
		/*
		 * Use this thread: intercept failures.
		 */
		sim.run();
		Arrays.stream(checkProcedures).forEachOrdered(p -> p.accept(env));
	}
	
	private static <T> Consumer<IEnvironment<T>> checkOnNodes(final Consumer<INode<T>> proc) {
		return env -> env.forEach(n -> {
			proc.accept(n);
		});
	}
	
	private static <T> Consumer<IEnvironment<T>> checkProgramValueOnAll(final Consumer<Object> proc) {
		return checkOnNodes(checkProtelisProgramValue(proc));
	}
	
	private static <T> Consumer<INode<T>> checkProtelisProgramValue(final Consumer<Object> check) {
		return n -> n.forEach(r -> {
			r.getActions().parallelStream()
				.filter(a -> a instanceof ProtelisProgram)
				.forEach(a -> {
					check.accept(n.getConcentration((ProtelisProgram) a));
				});
			});
	}

}
