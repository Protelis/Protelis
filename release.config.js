var publishCmd = `
git tag -a -f \${nextRelease.version} \${nextRelease.version} -F CHANGELOG.md
./gradlew shadowJar --parallel || ./gradlew shadowJar --parallel || exit 2
./gradlew uploadKotlin release --parallel || ./gradlew uploadKotlin release --parallel || ./gradlew uploadKotlin release --parallel || exit 3
./gradlew publishKotlinOSSRHPublicationToGithubRepository --continue || true
surge build/docs/javadoc/ protelis-doc.surge.sh || exit 4
surge protelis-lang/build/protelis-docs/ protelis-lang-doc.surge.sh || exit 5
git push --force origin \${nextRelease.version} || exit 6
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
