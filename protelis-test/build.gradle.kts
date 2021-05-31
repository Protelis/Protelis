/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

dependencies {
    api(Libs.junit)
    api(project(":protelis-interpreter"))
    implementation(Libs.alchemist_interfaces) {
        exclude(module = "asm-debug-all")
    }
    implementation(Libs.alchemist_loading) {
        exclude(module = "asm-debug-all")
    }
    implementation(Libs.commons_lang3)
    implementation(Libs.classgraph)
}
