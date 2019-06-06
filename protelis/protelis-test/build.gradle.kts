dependencies {
    api(Libs.junit)
    api(project(":protelis-interpreter"))
    implementation(Libs.alchemist_interfaces) {
        exclude(module = "asm-debug-all")
    }
    implementation(Libs.alchemist_loading) {
        exclude(module = "asm-debug-all")
    }
    implementation(Libs.commons_lang3)
    implementation(Libs.classgraph)
}
