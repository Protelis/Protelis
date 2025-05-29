import org.gradle.api.plugins.quality.AbstractCodeQualityTask

allprojects {
    val javaLaunchers = mutableSetOf<Provider<JavaLauncher>>()
    tasks.withType<Test>().configureEach {
        javaLaunchers.add(javaLauncher)
    }
    tasks.withType<AbstractCodeQualityTask>().configureEach {
        javaLaunchers.add(javaLauncher)
    }

    val resolveAllDependencies by tasks.registering {
        group = "build"
        description = "Resolve all dependencies"
        doLast {
            println("Resolving all dependencies...")
            configurations.forEach { configuration ->
                if (configuration.isCanBeResolved) {
                    configuration.resolve()
                }
            }
            println("Resolving all toolchains...")
            javaLaunchers.forEach { javaLauncher ->
                javaLauncher.get() // Force download
            }
        }
    }

    tasks.configureEach {
        if (this != resolveAllDependencies.get()) {
            enabled = false
        }
    }
}
