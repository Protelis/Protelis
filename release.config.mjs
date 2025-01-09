const publishCmd = `
echo 'Creating shadowJar and protelisdoc...'
./gradlew protelisdoc shadowJar --parallel || ./gradlew shadowJar --parallel || exit 2
echo '...assemblage done.'
echo 'Releasing on Maven Central...'
./gradlew uploadAll releaseStagingRepositoryOnMavenCentral --parallel || exit 3
echo '...released.'
echo 'Releasing on GitHub packages...'
./gradlew publishAllToGithubRepository --continue || true
echo '...released.'
#echo 'Publishing the javadocs on Surge...'
#surge build/docs/javadoc/ protelis-doc.surge.sh || exit 4
#surge protelis-lang/build/protelis-docs/ protelis-lang-doc.surge.sh || exit 5
#echo '...published.'
echo 'All done.'
`
import config from 'semantic-release-preconfigured-conventional-commits' with { type: "json" };
config.plugins.push(
    ["@semantic-release/exec", {
        "publishCmd": publishCmd,
    }],
    ["@semantic-release/github", {
        "assets": [
            { "path": "build/shadow/*-all.jar" },
        ]
    }],
    "@semantic-release/git",
)
export default config
