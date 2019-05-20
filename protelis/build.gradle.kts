import com.jfrog.bintray.gradle.tasks.BintrayUploadTask

plugins {
    id("de.fayard.buildSrcVersions") version
        "0.3.2"
    `java-library`
    id("org.danilopianini.build-commons") version "0.4.0"
    id("com.jfrog.bintray") version "1.8.4"
    id("com.gradle.build-scan") version "2.3"
}

val isJava7Legacy = project.hasProperty("java7Legacy") || System.getenv("JAVA7LEGACY") == "true"
if (isJava7Legacy) {
    println("This build will generate the *LEGACY*, Java-7 compatible, build of Protelis")
}

apply(plugin = "project-report")

allprojects {
    if (isJava7Legacy) {
        val isBeta = project.version.toString().endsWith("-beta")
        project.version = project.version.toString().dropLast(5) + "-j7" + ("-beta".takeIf { isBeta } ?: "")
    }

    apply(plugin = "java-library")
    apply(plugin =  "org.danilopianini.build-commons")

    dependencies {
        implementation("org.slf4j:slf4j-api:${extra["slf4jVersion"]}")
        testImplementation("junit:junit:${extra["junitVersion"]}")
        testRuntimeOnly("ch.qos.logback:logback-classic:${extra["logbackVersion"]}")
        doclet("org.jboss.apiviz:apiviz:${extra["apivizVersion"]}")
    }

    publishing.publications {
        withType<MavenPublication> {
            pom {
                developers {
                    developer {
                        name.set("Danilo Pianini")
                        email.set("danilo.pianini@unibo.it")
                        url.set("http://www.danilopianini.org")
                    }
                    developer {
                        name.set("Jacob Beal")
                        email.set("jakebeal@gmail.com")
                        url.set("http://web.mit.edu/jakebeal/www/")
                    }
                    developer {
                        name.set("Matteo Francia")
                        email.set("matteo.francia2@studio.unibo.it")
                        url.set("https://github.com/w4bo")
                    }
                }
                contributors {
                    contributor {
                        name.set("Mirko Viroli")
                        email.set("mirko.viroli@unibo.it")
                        url.set("http://mirkoviroli.apice.unibo.it/")
                    }
                    contributor {
                        name.set("Kyle Usbeck")
                        email.set("kusbeck@bbn.com")
                        url.set("https://dist-systems.bbn.com/people/kusbeck/")
                    }
                }
            }
        }
    }
    /*
     * Use Bintray for beta releases
     */
    apply(plugin = "com.jfrog.bintray")
    val apiKeyName = "BINTRAY_API_KEY"
    val userKeyName = "BINTRAY_USER"
    bintray {
        user = System.getenv(userKeyName)
        key = System.getenv(apiKeyName)
        override = true
        setPublications("main")
        with(pkg) {
            repo = "Protelis"
            name = project.name
            userOrg = "protelis"
            vcsUrl = "${extra["scmRootUrl"]}/${extra["scmRepoName"]}"
            setLicenses("GPL-3.0-or-later")
            with(version) {
                name = project.version.toString()
            }
        }
    }
    tasks.withType<BintrayUploadTask> {
        onlyIf {
            val hasKey = System.getenv(apiKeyName) != null
            val hasUser = System.getenv(userKeyName) != null
            if (!hasKey) {
                println("The $apiKeyName environment variable must be set in order for the bintray deployment to work")
            }
            if (!hasUser) {
                println("The $userKeyName environment variable must be set in order for the bintray deployment to work")
            }
            hasKey && hasUser
        }
    }
}

subprojects.forEach { subproject -> rootProject.evaluationDependsOn(subproject.path) }
/*
 * Remove tasks that should not exist in subprojects
 */
//subprojects.each { it.tasks.remove(wrapper) }

/*
 * Running a task on the parent project implies running the same task first on any subproject
 */
//tasks.each { task ->
//    subprojects.each { subproject ->
//        def subtask = subproject.tasks.findByPath("${task.name}")
//        if (subtask != null) {
//            task.dependsOn(subtask)
//        }
//    }
//}

dependencies {
    api(project(":protelis-interpreter"))
    api(project(":protelis-lang"))
}

tasks.withType<Javadoc> {
    dependsOn(subprojects.map{ it.tasks.javadoc })
    source(subprojects.map{ it.tasks.javadoc.get().source })
}

tasks.register<Jar>("fatJar") {
    archiveBaseName.set("${rootProject.name}-redist")
    isZip64 = true
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }) {
        // remove all signature files
        exclude("META-INF/")
        exclude("ant_tasks/")
        exclude("about_files/")
        exclude("help/about/")
        exclude("build")
        exclude(".gradle")
        exclude("build.gradle")
        exclude("gradle")
        exclude("gradlew")
        exclude("gradlew.bat")
    }
    with(tasks.jar.get() as CopySpec)
}

apply(plugin = "com.gradle.build-scan")
buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
}

//defaultTasks("clean", "build", "check", "javadoc", "projectReport", "buildDashboard", "fatJar", "signMainPublication")
