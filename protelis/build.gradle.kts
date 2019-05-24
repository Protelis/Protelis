import com.github.spotbugs.SpotBugsTask
import com.jfrog.bintray.gradle.tasks.BintrayUploadTask
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    id("de.fayard.buildSrcVersions") version
        Versions.de_fayard_buildsrcversions_gradle_plugin
    id("org.danilopianini.git-sensitive-semantic-versioning") version
        Versions.org_danilopianini_git_sensitive_semantic_versioning_gradle_plugin
    eclipse
    `java-library`
    jacoco
    id("com.github.spotbugs") version Versions.com_github_spotbugs_gradle_plugin
    pmd
    checkstyle
    id("org.jlleitschuh.gradle.ktlint") version Versions.org_jlleitschuh_gradle_ktlint_gradle_plugin
    id("org.protelis.protelisdoc") version "0.1.0"
    signing
    `maven-publish`
    id("org.danilopianini.publish-on-central") version Versions.org_danilopianini_publish_on_central_gradle_plugin
    id("com.jfrog.bintray") version Versions.com_jfrog_bintray_gradle_plugin
    id("com.gradle.build-scan") version Versions.com_gradle_build_scan_gradle_plugin
}

apply(plugin = "com.gradle.build-scan")
apply(plugin = "com.jfrog.bintray")
apply(plugin = "com.gradle.build-scan")

val scmUrl = "git:git@github.com:Protelis/Protelis"

allprojects {

    apply(plugin = "org.danilopianini.git-sensitive-semantic-versioning")
    apply(plugin = "eclipse")
    apply(plugin = "java-library")
    apply(plugin = "jacoco")
    apply(plugin = "com.github.spotbugs")
    apply(plugin = "checkstyle")
    apply(plugin = "pmd")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "org.jetbrains.kotlin.jvm")
//    apply(plugin = "org.protelis.protelisdoc")
    apply(plugin = "project-report")
    apply(plugin = "build-dashboard")
    apply(plugin = "signing")
    apply(plugin = "maven-publish")
    apply(plugin = "org.danilopianini.publish-on-central")
    apply(plugin = "com.jfrog.bintray")

    gitSemVer {
        version = computeGitSemVer()
    }

    repositories {
        mavenCentral()
    }

    val doclet by configurations.creating
    dependencies {
        compileOnly(Libs.spotbugs_annotations)
        testImplementation(Libs.junit)
        testImplementation(Libs.slf4j_api)
        testRuntimeOnly(Libs.logback_classic)
        doclet(Libs.apiviz)
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.withType<Test> {
        failFast = true
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            events("passed", "skipped", "failed", "standardError")
        }
    }

    spotbugs {
        isIgnoreFailures = true
        effort = "max"
        reportLevel = "low"
        val excludeFile = File("${project.rootProject.projectDir}/config/spotbugs/excludes.xml")
        if (excludeFile.exists()) {
            excludeFilterConfig = project.resources.text.fromFile(excludeFile)
        }
    }

    tasks.withType<SpotBugsTask> {
        reports {
            xml.setEnabled(false)
            html.setEnabled(true)
        }
    }

    pmd {
        setIgnoreFailures(true)
        ruleSets = listOf()
        ruleSetConfig = resources.text.fromFile("${project.rootProject.projectDir}/config/pmd/pmd.xml")
    }

    ktlint {
        filter {
            exclude {
                it.file.path.toString().contains("protelis2kotlin")
            }
        }
    }

    tasks.withType<Javadoc> {
        isFailOnError = false
        options {
            val title = "Protelis ${project.version} Javadoc API"
            windowTitle(title)
            docletpath = doclet.files.toList()
            doclet("org.jboss.apiviz.APIviz")
            if (this is CoreJavadocOptions) {
                addBooleanOption("nopackagediagram", true)
            }
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
            vcsUrl = scmUrl
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

dependencies {
    api(project(":protelis-interpreter"))
    api(project(":protelis-lang"))
}

tasks.withType<Javadoc> {
    dependsOn(subprojects.map { it.tasks.javadoc })
    source(subprojects.map { it.tasks.javadoc.get().source })
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

buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
}
