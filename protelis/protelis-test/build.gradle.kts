dependencies {
    api(project(":protelis-interpreter"))
    implementation(Libs.alchemist_interfaces) {
        exclude(module = "asm-debug-all")
    }
    implementation(Libs.alchemist_loading) {
        exclude(module = "asm-debug-all")
    }
    api(Libs.junit)
    implementation(Libs.commons_lang3)
}
