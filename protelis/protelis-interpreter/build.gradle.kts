/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    api(Libs.trove4j)
    api(Libs.commons_math3)
    api(Libs.guava)
    api(Libs.protelis_parser)
    implementation(Libs.commons_codec)
    implementation(Libs.commons_io)
    implementation(Libs.fst)
    implementation(Libs.commons_lang3)
    implementation(Libs.slf4j_api)
}

eclipse {
    classpath {
        isDownloadSources = true
    }
}
