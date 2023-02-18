/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */
rootProject.name = "protelis"

include(
    "protelis-interpreter",
    "protelis-lang",
    "protelis-test"
)

plugins {
    id("com.gradle.enterprise") version "3.12.3"
    id("org.danilopianini.gradle-pre-commit-git-hooks") version "1.1.1"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishAlways()
    }
}

gitHooks {
    commitMsg { conventionalCommits() }
    preCommit {
        tasks("checkstyleMain", "checkstyleTest", "ktlintCheck")
    }
    createHooks(overwriteExisting = true)
}
