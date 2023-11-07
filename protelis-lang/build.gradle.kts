/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.protelisdoc)
}

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

val dokkaOutput = tasks.protelisdoc.get().outputDirectory

tasks.withType<org.danilopianini.gradle.mavencentral.JavadocJar> {
    dependsOn(tasks.protelisdoc)
    from(dokkaOutput)
}
