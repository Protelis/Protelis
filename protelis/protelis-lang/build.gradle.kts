plugins {
    id("org.protelis.protelisdoc")
}
repositories {
    jcenter {
        content {
            includeGroup("org.jetbrains.dokka")
            includeGroup("com.soywiz.korlibs.korte")
            includeModule("org.jetbrains.kotlinx", "kotlinx-html-jvm")
            includeModule("org.jetbrains", "markdown")
        }
    }
}
apply(plugin = "org.protelis.protelisdoc")

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
    debug.set(false)
}

tasks.withType<org.danilopianini.gradle.mavencentral.JavadocJar> {
    dependsOn(tasks.generateProtelisDoc)
    from(tasks.generateProtelisDoc.get().outputDirectory)
}

tasks.generateProtelisDoc {
    dependsOn(project(":protelis-interpreter").tasks.jar)
}
