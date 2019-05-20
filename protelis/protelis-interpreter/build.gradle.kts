import me.tatarka.RetrolambdaExec
import me.tatarka.RetrolambdaTask

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        val isJava7Legacy = project.hasProperty("java7Legacy") || System.getenv("JAVA7LEGACY") == "true"
        if (isJava7Legacy) {
            classpath("me.tatarka:gradle-retrolambda:3.7.1")
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
    api("net.sf.trove4j:trove4j:${extra["trove4jVersion"]}")
    api("net.sourceforge.streamsupport:streamsupport:${extra["streamVersion"]}")
    api("org.apache.commons:commons-math3:${extra["math3Version"]}")
    api("org.eclipse.emf:org.eclipse.emf.ecore:${extra["emfVersion"]}")
    api("com.google.guava:guava:${extra["guavaVersion"]}")
    implementation("commons-codec:commons-codec:${extra["commonsCodecVersion"]}")
    implementation("commons-io:commons-io:${extra["commonsIOVersion"]}")
    implementation("de.ruedigermoeller:fst:${extra["fstVersion"]}")
    implementation("org.apache.commons:commons-lang3:${extra["lang3Version"]}")
    implementation("org.springframework:spring-core:${extra["springVersion"]}")
    /*
     * Legacy dependencies picking
     */
    if (isJava7Legacy) {
        println("Configuring legacy dependencies")
        api("org.eclipse.xtext:org.eclipse.xtext.common.types:2.10.0")
        api("org.protelis:protelis.parser:${extra["parserVersion"]}.java7legacy")
        compileOnly("com.google.code.findbugs:findbugs:${extra["spotBugsLegacy"]}")
    } else {
        api("org.protelis:protelis.parser:${extra["parserVersion"]}")
        api("org.eclipse.xtext:org.eclipse.xtext.common.types:${extra["xtextVersion"]}")
        compileOnly("com.github.spotbugs:spotbugs-annotations:${extra["spotBugsVersion"]}")
    }
}

eclipse {
    classpath {
        isDownloadSources = true
    }
}
