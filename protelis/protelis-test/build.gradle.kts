dependencies {
    api(project(":protelis-interpreter"))
    implementation("it.unibo.alchemist:alchemist-interfaces:${extra["alchemistVersion"]}") {
        exclude(module = "asm-debug-all")
    }
    implementation("it.unibo.alchemist:alchemist-loading:${extra["alchemistVersion"]}") {
        exclude(module = "asm-debug-all")
    }
    implementation("junit:junit:${extra["junitVersion"]}")
    implementation("org.apache.commons:commons-lang3:${extra["lang3Version"]}")
}
