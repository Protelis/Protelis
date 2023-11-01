/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

dependencies {
    api(libs.junit)
    api(project(":protelis-interpreter"))
    implementation(libs.alchemist.interfaces) {
        exclude(module = "asm-debug-all")
    }
    implementation(libs.alchemist.loading) {
        exclude(module = "asm-debug-all")
    }
    implementation(libs.commons.lang)
    implementation(libs.classgraph)
}

javaQA {
    checkstyle {
        additionalConfiguration.set(
            """
            <module name="SuppressWithPlainTextCommentFilter"/>
            """.trimIndent(),
        )
    }
}
