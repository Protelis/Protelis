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
    outputFormat.set("javadoc")
    debug.set(false)
}
