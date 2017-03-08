#!/bin/bash
set -e
./gradlew check projectReport uploadArchives
./gradlew fatJar
mkdir report
cp --parent */build/reports build/reports report -R
