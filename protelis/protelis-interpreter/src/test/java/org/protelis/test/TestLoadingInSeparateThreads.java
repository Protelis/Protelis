package org.protelis.test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.protelis.lang.ProtelisLoader;

import com.google.common.io.Files;

/**
 * This test ensures that module loading access is performed using thread-local
 * resource loading. This way, Protelis code can be injected in a multithreaded
 * environment, such as advanced and distributed simulation engines.
 */
public class TestLoadingInSeparateThreads {

    /**
     * Creates a new folder, copies resources, then tries to load a valid module in a fresh thread.
     * 
     * @throws IOException failure
     * @throws URISyntaxException failure
     * @throws InterruptedException failure
     * @throws ExecutionException failure
     */
    @Test
    public void testThreadDependentLoadModule() throws IOException, URISyntaxException, InterruptedException, ExecutionException {
        final Runnable c = () -> ProtelisLoader.parse("org:protelis:test");
        final File d1 = createDependenciesDirectory();
        try {
            final FutureTask<Void> ft1 = new FutureTask<>(c, null);
            final Thread t1 = new Thread(ft1);
            t1.setContextClassLoader(new URLClassLoader(new URL[] {d1.toURI().toURL()}));
            t1.start();
            ft1.get();
        } finally {
            FileUtils.deleteDirectory(d1);
        }
    }

    private File createDependenciesDirectory() throws IOException, URISyntaxException {
        final File d = Files.createTempDir();
        FileUtils.copyDirectory(new File(Thread.currentThread().getContextClassLoader().getResource("orgoriginal").toURI()), new File(d, "org"));
        return d;
    }

}
