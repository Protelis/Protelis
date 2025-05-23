allprojects {
    tasks.withType<Test>().configureEach { enabled = false }
}
