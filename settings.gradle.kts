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
    "protelis-test",
)

plugins {
    id("com.gradle.develocity") version "3.17.3"
    id("org.danilopianini.gradle-pre-commit-git-hooks") version "2.0.5"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

develocity {
    buildScan {
        termsOfUseUrl = "https://gradle.com/terms-of-service"
        termsOfUseAgree = "yes"
        uploadInBackground = !System.getenv("CI").toBoolean()
    }
}

gitHooks {
    commitMsg { conventionalCommits() }
    preCommit {
        tasks("checkstyleMain", "checkstyleTest", "ktlintCheck")
    }
    createHooks(overwriteExisting = true)
}
