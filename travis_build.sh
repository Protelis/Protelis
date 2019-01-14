#!/bin/bash
set -e
openssl aes-256-cbc -K $encrypted_e9b2eb0f7f1a_key -iv $encrypted_e9b2eb0f7f1a_iv -in prepare_environment.sh.enc -out prepare_environment.sh -d
sh prepare_environment.sh
cd protelis
./gradlew clean check fatJar projectReport publish --scan
mkdir -p report
cp --parent */build/reports build/reports report -R
