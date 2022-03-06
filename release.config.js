var publishCmd = `
git tag -a -f \${nextRelease.version} \${nextRelease.version} -F CHANGELOG.md
git push --force origin \${nextRelease.version} || exit 1
./gradlew shadowJar --parallel || ./gradlew shadowJar --parallel || exit 2
./gradlew releaseJavaMavenOnMavenCentralNexus --parallel || exit 3
./gradlew publishJavaMavenPublicationToGithubRepository --continue || true
surge build/docs/javadoc/ protelis-doc.surge.sh || exit 4
surge protelis-lang/build/protelis-docs/ protelis-lang-doc.surge.sh || exit 5
`
var config = require('semantic-release-preconfigured-conventional-commits');
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
module.exports = config
