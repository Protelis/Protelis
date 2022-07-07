/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */
dependencies {
    api(libs.trove4j)
    api(libs.commons.math)
    api(libs.guava)
    api(libs.protelis.parser)
    implementation(libs.commons.codec)
    implementation(libs.commons.io)
    implementation(libs.commons.lang)
    implementation(libs.slf4j)
}
