import de.fayard.dependencies.bootstrapRefreshVersionsAndDependencies
import org.danilopianini.VersionAliases.justAdditionalAliases
buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    dependencies {
        classpath("de.fayard:dependencies:0.5.7")
        classpath("org.danilopianini:refreshversions-aliases:+")
    }
}
bootstrapRefreshVersionsAndDependencies(justAdditionalAliases)

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

rootProject.name = "protelis"
