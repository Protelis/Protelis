/*
 * Copyright (c) 2025, Danilo Pianini and contributors listed in the project's build.gradle.kts file.
 *
 *  This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 *  with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `java-library`
    signing
    `maven-publish`
    alias(libs.plugins.gitSemVer)
    alias(libs.plugins.java.qa)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.qa)
    alias(libs.plugins.multiJvmTesting)
    alias(libs.plugins.protelisdoc)
    alias(libs.plugins.publishOnCentral)
    alias(libs.plugins.shadowJar)
    alias(libs.plugins.taskTree)
}

apply(plugin = "com.github.johnrengelman.shadow")

val scmUrl = "git:git@github.com:Protelis/Protelis"

val Provider<PluginDependency>.id get() = get().pluginId

val minJavaVersion = 17

allprojects {

    with(rootProject.libs.plugins) {
        apply(plugin = gitSemVer.id)
        apply(plugin = java.qa.id)
        apply(plugin = kotlin.jvm.id)
        apply(plugin = kotlin.qa.id)
        apply(plugin = multiJvmTesting.id)
        apply(plugin = publishOnCentral.id)
        apply(plugin = taskTree.id)
        apply(plugin = shadowJar.id)
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        with(rootProject.libs) {
            compileOnly(spotbugs.annotations)
            testImplementation(junit)
            testImplementation(slf4j)
            testRuntimeOnly(logback)
            constraints {
                implementation(asm) {
                    because("Xtext often remains stuck to old versions of ASM")
                }
            }
        }
    }

    multiJvm {
        jvmVersionForCompilation.set(minJavaVersion)
        maximumSupportedJvmVersion.set(latestJava)
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    tasks.withType<AbstractCopyTask>().configureEach {
        duplicatesStrategy = DuplicatesStrategy.WARN
    }

    tasks.withType<Test>().configureEach {
        failFast = true
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            events("passed", "skipped", "failed", "standardError")
        }
    }

    tasks.withType<Javadoc>().configureEach {
        isFailOnError = true
        options {
            check(this is CoreJavadocOptions)
            javadocTool.set(
                javaToolchains.javadocToolFor {
                    languageVersion.set(JavaLanguageVersion.of(minJavaVersion))
                },
            )
            addStringOption("Xdoclint:all", "-Xdoclint:-missing")
            encoding = "UTF-8"
            memberLevel = JavadocMemberLevel.PROTECTED
            val title = "Protelis ${project.version} Javadoc API"
            windowTitle(title)
        }
    }

    if (System.getenv("CI") == true.toString()) {
        signing {
            val signingKey: String? by project
            val signingPassword: String? by project
            useInMemoryPgpKeys(signingKey, signingPassword)
        }
    }

    publishOnCentral {
        fun String.fromProperties(): String = extra[this].toString()
        projectDescription.set("projectDescription".fromProperties())
        projectLongName.set("longName".fromProperties())
        licenseName.set("licenseName".fromProperties())
        licenseUrl.set("licenseUrl".fromProperties())
        projectUrl.set("http://www.protelis.org")
        scmConnection.set(scmUrl)
        repository("https://maven.pkg.github.com/protelis/protelis") {
            user.set(System.getenv("GITHUB_ACTOR") ?: "DanySK")
            password.set(System.getenv("GITHUB_TOKEN"))
        }
    }

    publishing.publications {
        withType<MavenPublication>().configureEach {
            pom {
                developers {
                    developer {
                        name.set("Danilo Pianini")
                        email.set("danilo.pianini@unibo.it")
                        url.set("https://danysk.github.io")
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
}

subprojects.forEach { subproject -> rootProject.evaluationDependsOn(subproject.path) }

dependencies {
    api(project(":protelis-interpreter"))
    api(project(":protelis-lang"))
}

tasks.withType<Javadoc>().configureEach {
    subprojects.forEach { subproject ->
        val subJavadoc = subproject.tasks.javadoc
        dependsOn(subJavadoc)
        source(subJavadoc.map { it.source })
        options.classpath(subJavadoc.map { it.classpath.files.toList() }.get())
    }
}

tasks.withType<ShadowJar>().configureEach {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to "Protelis",
                "Implementation-Version" to rootProject.version,
                "Automatic-Module-Name" to "org.protelis",
            ),
        )
    }
    exclude("ant_tasks/")
    exclude("about_files/")
    exclude("help/about/")
    exclude("build")
    exclude(".gradle")
    exclude("build.gradle")
    exclude("gradle")
    exclude("gradlew")
    exclude("gradlew.bat")
    isZip64 = true
    mergeServiceFiles()
    destinationDirectory.set(rootProject.layout.buildDirectory.dir("shadow"))
}

/*
 * Work around for:

* What went wrong:
Execution failed for task ':buildDashboard'.
> Could not create task ':ktlintKotlinScriptCheck'.
   > Cannot change dependencies of configuration ':ktlint' after it has been resolved.

 */
tasks.withType<GenerateBuildDashboard>().forEach { it.dependsOn(tasks.ktlintKotlinScriptCheck) }
