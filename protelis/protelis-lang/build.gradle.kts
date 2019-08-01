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
Protelis2KotlinDoc {
    baseDir.set("$projectDir/src/main/protelis") // base dir from which recursively looking for .pt files
    outputFormat.set("javadoc") // Dokka's output format (alternative: 'html')
    debug.set(false)
}

if (Os.isFamily(Os.FAMILY_WINDOWS)) {
    setOf("configureGenerateProtelisDoc", "generateProtelisDoc", "generateKotlinFromProtelis").map {
//        tasks.getByName(it)?.enabled = false
    }
}
