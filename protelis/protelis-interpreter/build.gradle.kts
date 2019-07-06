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
    implementation(Libs.spring_core)
}

eclipse {
    classpath {
        isDownloadSources = true
    }
}
