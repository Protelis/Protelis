import me.tatarka.RetrolambdaExec
import me.tatarka.RetrolambdaTask

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        val isJava7Legacy = project.hasProperty("java7Legacy") || System.getenv("JAVA7LEGACY") == "true"
        if (isJava7Legacy) {
            classpath(Libs.me_tatarka_retrolambda_gradle_plugin)
        }
    }
}
plugins {
    id("me.tatarka.retrolambda") version "3.7.1"
}
val isJava7Legacy = project.hasProperty("java7Legacy") || System.getenv("JAVA7LEGACY") == "true"
if (isJava7Legacy) {
    apply(plugin = "me.tatarka.retrolambda")
    retrolambda {
        javaVersion = JavaVersion.VERSION_1_7
        defaultMethods(true)
        incremental(true)
    }
} else {
    tasks.withType<RetrolambdaTask> {
        println(this)
        enabled = false
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    api(Libs.trove4j)
    api(Libs.streamsupport)
    api(Libs.commons_math3)
    api(Libs.org_eclipse_emf_ecore)
    api(Libs.guava)
    implementation(Libs.commons_codec)
    implementation(Libs.commons_io)
    implementation(Libs.fst)
    implementation(Libs.commons_lang3)
    implementation(Libs.spring_core)
    /*
     * Legacy dependencies picking
     */
    if (isJava7Legacy) {
        println("Configuring legacy dependencies")
        api("org.eclipse.xtext:org.eclipse.xtext.common.types:2.10.0")
        api("${Libs.protelis_parser}.java7legacy")
        compileOnly("com.google.code.findbugs:findbugs:${extra["spotBugsLegacy"]}")
    } else {
        api(Libs.protelis_parser)
        api(Libs.org_eclipse_xtext_common_types)
        compileOnly(Libs.spotbugs_annotations)
    }
}

eclipse {
    classpath {
        isDownloadSources = true
    }
}
