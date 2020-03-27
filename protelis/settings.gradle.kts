import de.fayard.dependencies.bootstrapRefreshVersionsAndDependencies

rootProject.name = "protelis"

buildscript {
    repositories { gradlePluginPortal() }
    dependencies.classpath("de.fayard:dependencies:0.5.7")
}

bootstrapRefreshVersionsAndDependencies()

include(
        "protelis-interpreter",
        "protelis-lang",
        "protelis-test"
)

plugins {
    id("com.gradle.enterprise") version "3.2"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}
