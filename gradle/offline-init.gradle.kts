allprojects {
    val javaLaunchers = mutableSetOf<Provider<JavaLauncher>>()
    var additional = false
    tasks.withType<Test>().configureEach {
        if (additional) {
            enabled = false
        }
        additional = true
        javaLaunchers.add(javaLauncher)
    }

    val resolveAllDependencies by tasks.registering {
        group = "build"
        description = "Resolve all dependencies"
        doLast {
            configurations.forEach { configuration ->
                if (configuration.isCanBeResolved) {
                    configuration.resolve()
                }
            }
            javaLaunchers.forEach { javaLauncher ->
                javaLauncher.get() // Force download
            }
        }
    }

    tasks.findByName("build")?.configure<Task> {
        dependsOn(resolveAllDependencies)
    }
}
