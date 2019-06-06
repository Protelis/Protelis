plugins {
    id("org.protelis.protelisdoc") version "0.1.1-dev0f+1c1a147"
}

dependencies {
    implementation(project(":protelis-interpreter"))
    testImplementation(project(":protelis-test"))
    implementation(kotlin("stdlib"))
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