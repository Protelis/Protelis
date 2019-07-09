import kotlin.String

/**
 * Find which updates are available by running
 *     `$ ./gradlew buildSrcVersions`
 * This will only update the comments.
 *
 * YOU are responsible for updating manually the dependency version. */
object Versions {
    const val logback_classic: String = "1.3.0-alpha4" 

    const val com_github_spotbugs_gradle_plugin: String = "1.6.9" // available: "2.0.0"

    const val spotbugs_annotations: String = "3.1.12" 

    const val guava: String = "27.1-jre" // available: "28.0-jre"

    const val com_gradle_build_scan_gradle_plugin: String = "2.3" 

    const val com_jfrog_bintray_gradle_plugin: String = "1.8.4" 

    const val ktlint: String = "0.32.0" // available: "0.33.0"

    const val commons_codec: String = "1.12" 

    const val commons_io: String = "2.6" 

    const val de_fayard_buildsrcversions_gradle_plugin: String = "0.3.2" 

    const val fst: String = "2.57" 

    const val classgraph: String = "4.8.37" // available: "4.8.43"

    const val it_unibo_alchemist: String = "4.0.0" // available: "8.2.0"

    const val junit: String = "4.13-beta-3" 

    const val trove4j: String = "3.0.3" 

    const val commons_lang3: String = "3.9" 

    const val commons_math3: String = "3.6.1" 

    const val org_danilopianini_git_sensitive_semantic_versioning_gradle_plugin: String = "0.2.2" 

    const val org_danilopianini_publish_on_central_gradle_plugin: String = "0.1.1" 

    const val apiviz: String = "1.3.2.GA" 

    const val org_jetbrains_kotlin_jvm_gradle_plugin: String = "1.3.31" // available: "1.3.41"

    const val org_jetbrains_kotlin: String = "1.3.31" // available: "1.3.41"

    const val org_jlleitschuh_gradle_ktlint_gradle_plugin: String = "8.0.0" // available: "8.1.0"

    const val protelis_parser: String = "9.1.2" 

    const val slf4j_api: String = "1.7.26" 

    const val spring_core: String = "5.1.7.RELEASE" // available: "5.1.8.RELEASE"

    /**
     *
     *   To update Gradle, edit the wrapper file at path:
     *      ./gradle/wrapper/gradle-wrapper.properties
     */
    object Gradle {
        const val runningVersion: String = "5.4.1"

        const val currentVersion: String = "5.5"

        const val nightlyVersion: String = "5.6-20190709000031+0000"

        const val releaseCandidate: String = ""
    }
}
