#!/bin/bash
set -e
openssl aes-256-cbc -K $encrypted_e9b2eb0f7f1a_key -iv $encrypted_e9b2eb0f7f1a_iv -in prepare_environment.sh.enc -out prepare_environment.sh -d
sh prepare_environment.sh
cd protelis
JAVA_VERSION=$(java -version 2>&1)
if [[ $JAVA_VERSION == 'openjdk version "1.8.0'* ]]
then
  ./gradlew clean check fatJar projectReport publish --scan
else
  ./gradlew clean check install fatJar --scan
fi
mkdir -p report
cp --parent */build/reports build/reports report -R
