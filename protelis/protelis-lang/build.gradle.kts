import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    id("org.protelis.protelisdoc") version Versions.org_protelis_protelisdoc_gradle_plugin
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

if (Os.isFamily(Os.FAMILY_WINDOWS)) {
    setOf("configureGenerateProtelisDoc", "generateProtelisDoc", "generateKotlinFromProtelis").map {
        tasks.getByName(it)?.enabled = false
    }
}
