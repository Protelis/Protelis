plugins {
    id("org.protelis.protelisdoc") version "0.1.0"
}


dependencies {
    implementation(project(":protelis-interpreter"))
    testImplementation(project(":protelis-test"))
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
