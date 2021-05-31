/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

plugins {
    id("org.protelis.protelisdoc")
}
repositories {
    jcenter {
        content {
            includeGroup("org.jetbrains.dokka")
            includeGroup("com.soywiz.korlibs.korte")
            includeModule("org.jetbrains.kotlinx", "kotlinx-html-jvm")
            includeModule("org.jetbrains", "markdown")
        }
    }
}
apply(plugin = "org.protelis.protelisdoc")

dependencies {
    implementation(project(":protelis-interpreter"))
    testImplementation(project(":protelis-test"))
    protelisdoc(kotlin("stdlib"))
}

sourceSets {
    main {
        resources {
            srcDir("src/main/protelis")
        }
    }
    test {
        resources {
            srcDir("src/main/protelis")
            srcDir("src/test/resources")
        }
    }
}

protelisdoc {
    baseDir.set("$projectDir/src/main/protelis")
    debug.set(false)
}

tasks.withType<org.danilopianini.gradle.mavencentral.JavadocJar> {
    dependsOn(tasks.generateProtelisDoc)
    from(tasks.generateProtelisDoc.get().outputDirectory)
}

tasks.generateProtelisDoc {
    dependsOn(project(":protelis-interpreter").tasks.jar)
}
