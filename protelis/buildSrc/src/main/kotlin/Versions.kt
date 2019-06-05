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

    const val guava: String = "27.1-jre" 

    const val com_gradle_build_scan_gradle_plugin: String = "2.3" 

    const val com_jfrog_bintray_gradle_plugin: String = "1.8.4" 

    const val ktlint: String = "0.32.0" // available: "0.33.0"

    const val commons_codec: String = "1.12" 

    const val commons_io: String = "2.6" 

    const val de_fayard_buildsrcversions_gradle_plugin: String = "0.3.2" 

    const val fst: String = "2.57" 

    const val classgraph: String = "4.8.37" 

    const val it_unibo_alchemist: String = "4.0.0" // available: "8.0.0-beta+0t3.1fcab"

    const val junit: String = "4.13-beta-3" 

    const val trove4j: String = "3.0.3" 

    const val streamsupport: String = "1.7.0" // available: "1.7.1"

    const val commons_lang3: String = "3.9" 

    const val commons_math3: String = "3.6.1" 

    const val org_danilopianini_git_sensitive_semantic_versioning_gradle_plugin: String = "0.2.2" 

    const val org_danilopianini_publish_on_central_gradle_plugin: String = "0.1.1" 

    const val org_eclipse_emf_ecore: String = "2.12.0" // available: "2.15.0"

    const val apiviz: String = "1.3.2.GA" 

    const val kotlin_scripting_compiler_embeddable: String = "1.3.21" // available: "1.3.31"

    const val kotlin_stdlib: String = "1.3.31" 

    const val org_jlleitschuh_gradle_ktlint_gradle_plugin: String = "8.0.0" 

    const val org_protelis_protelisdoc_gradle_plugin: String = "0.1.1-dev08+b8184c8" // available: "0.1.1"

    const val protelis_interpreter: String = "12.1.0" 

    const val protelis_parser: String = "9.1.0" 

    const val slf4j_api: String = "1.7.26" 

    const val spring_core: String = "5.1.7.RELEASE" 

    /**
     *
     *   To update Gradle, edit the wrapper file at path:
     *      ./gradle/wrapper/gradle-wrapper.properties
     */
    object Gradle {
        const val runningVersion: String = "5.4.1"

        const val currentVersion: String = "5.4.1"

        const val nightlyVersion: String = "5.6-20190605000047+0000"

        const val releaseCandidate: String = "5.5-rc-1"
    }
}
