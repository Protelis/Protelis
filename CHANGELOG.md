### [16.1.1](https://github.com/Protelis/Protelis/compare/16.1.0...16.1.1) (2022-05-22)


### Bug Fixes

* **protelis-lang:** patch the code of the S block, that used to exhibit non-self-stabilizing (oscillatory) behavior. The implementation differed from the one in the literature, and should now be aligned ([4db458b](https://github.com/Protelis/Protelis/commit/4db458b1b82c944fc0bd55114bdb5dd395555d5c))


### Dependency updates

* **deps:** update dependency ch.qos.logback:logback-classic to v1.3.0-alpha16 ([46b6776](https://github.com/Protelis/Protelis/commit/46b67765b11300d8d95da19d87592c61a67d1add))
* **deps:** update plugin com.gradle.enterprise to v3.10.1 ([33429fd](https://github.com/Protelis/Protelis/commit/33429fd5813a5d163c8d31aba263abc4739a29ef))


### Style improvements

* **interpreter:** improve indentation in ShareCall and AbstractProtelisAST ([87e8e7c](https://github.com/Protelis/Protelis/commit/87e8e7cbbab4074280503b3a24f34831209a0978))

## [16.1.0](https://github.com/Protelis/Protelis/compare/16.0.0...16.1.0) (2022-05-18)


### Features

* **interpreter:** improve parsing error description ([#673](https://github.com/Protelis/Protelis/issues/673)) ([9d09325](https://github.com/Protelis/Protelis/commit/9d09325da1e65b16306a72818888f343cbf5fe31))

## [16.0.0](https://github.com/Protelis/Protelis/compare/15.4.2...16.0.0) (2022-05-18)


### ⚠ BREAKING CHANGES

* **interpreter:** make `ProtelisProgram` read-only and its implementation immutable. This is a change in the API of Protelis, and thus causes a breaking change. To adapt your software to the change, collect the computation result of `ProtelisProgram.compute`, instead of calling `compute` followed by `getCurrentValue`. If you are interfacing with protelis through the `ProtelisVM`, then there should be no change to be performed, as the VM way to interact with Protelis remains unchanged (call `runCycle` followed by `getCurrentValue`). In other words, this commit enlarges the portion of Protelis that behaves immutably to the whole program, leaving `ProtelisVM` as the first component with a mutable state.

### Features

* **interpreter:** make `ProtelisProgram` read-only and its implementation immutable. This is a change in the API of Protelis, and thus causes a breaking change. To adapt your software to the change, collect the computation result of `ProtelisProgram.compute`, instead of calling `compute` followed by `getCurrentValue`. If you are interfacing with protelis through the `ProtelisVM`, then there should be no change to be performed, as the VM way to interact with Protelis remains unchanged (call `runCycle` followed by `getCurrentValue`). In other words, this commit enlarges the portion of Protelis that behaves immutably to the whole program, leaving `ProtelisVM` as the first component with a mutable state. ([ee6ea6f](https://github.com/Protelis/Protelis/commit/ee6ea6f3cc0dc045e2a037546eee33c5494cd8a7))


### Bug Fixes

* **interpreter:** create a `toString` method for `Eval` ([ec4e175](https://github.com/Protelis/Protelis/commit/ec4e175ba1df5e00322e81b4cc8196b03993fc5b))
* **interpreter:** remove the last piece of state in `AbstractPersistedTree`. It was being used solely for converting to String, so the change should have no portability / upgrade issues. ([e4a8f47](https://github.com/Protelis/Protelis/commit/e4a8f47d1f20cc354a111138fa8b38f439475128))


### Dependency updates

* **deps:** update plugin multijvmtesting to v0.4.0 ([8d4c7a3](https://github.com/Protelis/Protelis/commit/8d4c7a3d96eacec025c0e4288826ad480c47e82b))


### Style improvements

* **interpreter:** improve indentation in `ShareCall` ([7e31d95](https://github.com/Protelis/Protelis/commit/7e31d95d23084b24a065a70558cc4d3fad00edfd))


### General maintenance

* **interpreter:** simplify `ProtelisLoader` as `ProtelisProgram` is now unmodifiable ([d4398fa](https://github.com/Protelis/Protelis/commit/d4398fac1cce5a4a59df621e19e81098a46b90d7))


### Documentation

* **interpreter:** add a summary fragment to `ProtelisProgram.getName`'s Javadoc ([c3a8e87](https://github.com/Protelis/Protelis/commit/c3a8e8717624ab0db57e9ed3b2842c943cfe4c75))

### [15.4.2](https://github.com/Protelis/Protelis/compare/15.4.1...15.4.2) (2022-05-17)


### Bug Fixes

* **interpreter:** fix a critical bug with the new flyweight implementation ([54660d2](https://github.com/Protelis/Protelis/commit/54660d2e249e342481d723d84586b934d2e923b9))


### Build and continuous integration

* ignore dependabot branch builds ([4ed6c33](https://github.com/Protelis/Protelis/commit/4ed6c33206772ade77f4bd481c4fe5adcc4e7df8))


### Dependency updates

* **deps:** bump minimist from 1.2.5 to 1.2.6 ([fcf2d27](https://github.com/Protelis/Protelis/commit/fcf2d278b8ca4ceff648749606d707e7cffbc48f))

### [15.4.1](https://github.com/Protelis/Protelis/compare/15.4.0...15.4.1) (2022-05-16)


### Bug Fixes

* **interpreter:** apply Sonatype Lift's suggestion on ProtelisLoader (String.split(String) has surprising behavior) ([3b9d7d1](https://github.com/Protelis/Protelis/commit/3b9d7d1c8f37c4da216fbc7a1b99cd55c10be8f0))


### Performance improvements

* **interpreter:** use a flyweight when parsing ([01b9502](https://github.com/Protelis/Protelis/commit/01b9502ff3678cde0a5ead4974c5c29366bd3489))

## [15.4.0](https://github.com/Protelis/Protelis/compare/15.3.3...15.4.0) (2022-05-16)


### Features

* **interpreter:** add methods to convert Protelis binary operators and functions to Java's ([918ef83](https://github.com/Protelis/Protelis/commit/918ef837a53d4d37a874505baccf9d90430612ad))
* **interpreter:** implement `Tuple.fold` and `Tuple.flatMap` ([656f862](https://github.com/Protelis/Protelis/commit/656f86212ee16ad8f947ea4bdf84b95e91b83878))
* **interpreter:** offer a pre-implementation of reduce ([edce27f](https://github.com/Protelis/Protelis/commit/edce27f670b1c49bb838d51fe3eaec8bfd61596a))


### Bug Fixes

* **interpreter:** fix comparable types selection ([0bcc027](https://github.com/Protelis/Protelis/commit/0bcc027503aa5bb80a303e21412a2cab89f5820f))
* **interpreter:** mark JavaInteroperabilityUtils.runProtelisFunction as Nonnull ([be1544c](https://github.com/Protelis/Protelis/commit/be1544c734451aab3cbbf7c6d1576ee6727f1a3a))


### Build and continuous integration

* **deps:** update danysk/build-check-deploy-gradle-action action to v1.2.14 ([08dc254](https://github.com/Protelis/Protelis/commit/08dc254ea6a442a2ec9f21a232d0ff9f2917fd5c))
* **deps:** update danysk/build-check-deploy-gradle-action action to v1.2.15 ([d47cb81](https://github.com/Protelis/Protelis/commit/d47cb81ce7a969ecd46a093bdf13a1182442ed41))


### Dependency updates

* **deps:** update dependency ch.qos.logback:logback-classic to v1.3.0-alpha15 ([b765251](https://github.com/Protelis/Protelis/commit/b765251b6ce7d274183b16106db6b585c82a6e34))
* **deps:** update dependency com.github.spotbugs:spotbugs-annotations to v4.7.0 ([9fdf3be](https://github.com/Protelis/Protelis/commit/9fdf3be4ee41d690c09252575769ed29786ac06f))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.6 ([dbcb594](https://github.com/Protelis/Protelis/commit/dbcb5947d51b39452e7428a47b3fef6609f431b9))
* **deps:** update node.js to 16.15 ([9295d4c](https://github.com/Protelis/Protelis/commit/9295d4c6953c0d9ff910c76e09039b5102c60d83))
* **deps:** update plugin java-qa to v0.23.0 ([a110b3f](https://github.com/Protelis/Protelis/commit/a110b3fe8f8fa96d2ec9093521b5e0cdf14ad644))
* **deps:** update plugin kotlin-qa to v0.17.0 ([8b0920e](https://github.com/Protelis/Protelis/commit/8b0920e3af70c4467922636d991b315b5684af7e))
* **deps:** update plugin kotlin-qa to v0.18.0 ([3254a28](https://github.com/Protelis/Protelis/commit/3254a2846916bbe8f575fbc33a9934f12c640adb))


### Refactoring

* **interpreter:** use the default implementation of Tuple.reduce ([5d2b987](https://github.com/Protelis/Protelis/commit/5d2b987531ad8444447370637b76ebd45ee8fc35))


### General maintenance

* **interpreter:** fix typo in comment in ArrayTupleImpl ([046b48e](https://github.com/Protelis/Protelis/commit/046b48e4de61851e013dbbb59e00e56a0de04c7b))
* **interpreter:** update copyright year in Builtins ([704b49f](https://github.com/Protelis/Protelis/commit/704b49f9f94b677e9295aa95d4ec47c7dfc2ce08))
* remove unneded submodules ([5ace409](https://github.com/Protelis/Protelis/commit/5ace4095ffac683e7b773fc59d4e429709339df2))


### Style improvements

* **interpreter:** add missing in ArrayTupleImpl.iterator @Nonnull annotation ([581115d](https://github.com/Protelis/Protelis/commit/581115d3fcaf28bace32ae5e236551d84df888a6))
* **interpreter:** fix the indentation in `runMethodWithProtelisArguments` ([0497235](https://github.com/Protelis/Protelis/commit/049723577218152bd92978dc0b5da2d683b8efd8))
* **interpreter:** fix the indentation in `runProtelisFunction` ([343a88d](https://github.com/Protelis/Protelis/commit/343a88d1bacc42bcf76d98d6e21fd4c39b4e24b0))
* **interpreter:** fix the indentation in `runStaticMethodWithProtelisArguments` ([12ab50c](https://github.com/Protelis/Protelis/commit/12ab50c59d2512717ed0a88c1571d0dd964e2e1d))
* **interpreter:** fix warning when calling murmur3_32 from Guava in ArrayTupleImpl ([ba13db7](https://github.com/Protelis/Protelis/commit/ba13db7b3201918058db6b8575063a725039faef))
* **interpreter:** improve indentation and parameter clarity in `runProtelisFunctionWithJavaArguments` ([3c27f7f](https://github.com/Protelis/Protelis/commit/3c27f7f84edd818684580376b4c5f0a7b84410b4))
* **interpreter:** improve indentation and parameter clarity in `runProtelisFunctionWithJavaArguments` ([69a3d98](https://github.com/Protelis/Protelis/commit/69a3d9866c35220227d6d2058773bc928a62cf6c))
* **interpreter:** improve indentation in Builtins ([7a6f21e](https://github.com/Protelis/Protelis/commit/7a6f21e002584203af9bfd49fe52c6d624564ff2))
* **interpreter:** improve indentation in Builtins.byReflection ([ea09eea](https://github.com/Protelis/Protelis/commit/ea09eea37b42be292f0006501558aa504a1d35ff))
* **interpreter:** improve indentation in Builtins.foldHood ([ccbdcc6](https://github.com/Protelis/Protelis/commit/ccbdcc6ca7ad97944f60f16592d1f5c5ec286d12))
* **interpreter:** improve indentation in Builtins.foldHoodPlusSelf ([d70fdee](https://github.com/Protelis/Protelis/commit/d70fdeecafece30e1c65283a479c83db4dfe2cec))
* **interpreter:** improve indentation in Builtins.reduceHood ([bd4cc9b](https://github.com/Protelis/Protelis/commit/bd4cc9b019e934b5fcac6e8b9878c2fa852d8f22))
* **interpreter:** improve indentation in Builtins.reductionFunction ([fe453bf](https://github.com/Protelis/Protelis/commit/fe453bf20dbdfcc4e9e5a85965c73ec43fde6bdd))
* **interpreter:** infer explicit type arguments in `Tuple.flatMap` ([201908d](https://github.com/Protelis/Protelis/commit/201908dc9f8dfb94faab4240f48a444d0c8c764d))
* **interpreter:** mark as final all parameters of default methods in Tuple ([4048aac](https://github.com/Protelis/Protelis/commit/4048aac6fdbbb8115c1d7b266c47d6b398b62748))
* **interpreter:** remove trailing spaces in Builtins ([f94499b](https://github.com/Protelis/Protelis/commit/f94499b4e26007586748404d582b562b68b11809))
* **interpreter:** suppress warning from Guava ([768a116](https://github.com/Protelis/Protelis/commit/768a116f3d60b604e7940f5c28089619914d0c06))
* **interpreter:** use better parameter names in JavaInteroperabilityUtils.runProtelisFunction ([3ed4f79](https://github.com/Protelis/Protelis/commit/3ed4f792dd2130bca0be9bac8d7a666675ac144d))
* **interpreter:** use clearer parameter names in `runMethodWithProtelisArguments` ([95e145d](https://github.com/Protelis/Protelis/commit/95e145d3a615d3f8386b2217156e2b2de951a32d))
* **interpreter:** use clearer parameter names in `runStaticMethodWithProtelisArguments` ([35af019](https://github.com/Protelis/Protelis/commit/35af01985b1ad2d5225505ce1439dc37aa39eda0))


### Documentation

* **interpreter:** add a summary documentation fragment and improve a parameter name in JavaInteroperabilityUtils.runMethodWithProtelisArguments ([2389603](https://github.com/Protelis/Protelis/commit/2389603cc4d06c823b982e7a3a4de2e3a59fd7f8))
* **interpreter:** add a summary documentation fragment in Builtins.noneButSelf ([c925f91](https://github.com/Protelis/Protelis/commit/c925f91a997171ab75aaa923284f4a47ac34eb96))
* **interpreter:** add a summary documentation fragment in JavaInteroperabilityUtils.runProtelisFunction ([7ad0a53](https://github.com/Protelis/Protelis/commit/7ad0a53d5341f70a47916a1544ae396a44c0843a))
* **interpreter:** add a summary documentation fragment in JavaInteroperabilityUtils.runStaticMethodWithProtelisArguments ([481d3e4](https://github.com/Protelis/Protelis/commit/481d3e4b1bfc79e83f2cc7ef607f243d68b1b1c4))
* **interpreter:** add a summary documentation fragment to Builtins.none ([9a020ea](https://github.com/Protelis/Protelis/commit/9a020ea010301fd436e251e4da802366b0dd4631))
* **interpreter:** add a summary documentation fragment to JavaInteroperabilityUtils.runProtelisFunctionWithJavaArguments ([423c2f2](https://github.com/Protelis/Protelis/commit/423c2f242ba20145a67debab0c7b4ab40aacb25c))
* **interpreter:** add a summary documentation fragment to JavaInteroperabilityUtils.runProtelisFunctionWithJavaArguments ([a6e3699](https://github.com/Protelis/Protelis/commit/a6e3699cdaa521e72c379d49b699a6a36067fa74))
* **interpreter:** fix bad use of '>' in Builtins' documentation ([d1f9742](https://github.com/Protelis/Protelis/commit/d1f9742cec0b026e1a281ab576c2fce148b60f77))
* **interpreter:** fix links in Builtins internal documentation ([343f667](https://github.com/Protelis/Protelis/commit/343f667bfb1ba801b09366115e96e328c4854711))
* **interpreter:** fix missing semicolon in Builtins.foldHood documentation ([2be0e22](https://github.com/Protelis/Protelis/commit/2be0e223a1b9de707839f7a9e98da292dc34f98c))

### [15.3.3](https://github.com/Protelis/Protelis/compare/15.3.2...15.3.3) (2022-04-26)


### Build and continuous integration

* **deps:** update actions/checkout action to v3.0.2 ([8128a3b](https://github.com/Protelis/Protelis/commit/8128a3bd6c4896c8827f8bbeac7e0ee6059e31a2))
* **deps:** update danysk/build-check-deploy-gradle-action action to v1.2.13 ([ce81466](https://github.com/Protelis/Protelis/commit/ce81466537171bf077201d81c59fcba8f1b4a7a2))


### Dependency updates

* **deps:** update plugin kotlin-qa to v0.16.2 ([5a9b1bb](https://github.com/Protelis/Protelis/commit/5a9b1bb79e293aef6f37baa0bfb7ac7ff9a53c8f))
* **deps:** update plugin multijvmtesting to v0.3.7 ([6d54467](https://github.com/Protelis/Protelis/commit/6d544678c16aecfde7dcb41139d46b297c77b651))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.10 ([4b13359](https://github.com/Protelis/Protelis/commit/4b1335910e11995dfbca91dd6214d297a767f896))
* **deps:** update plugin publishoncentral to v0.7.19 ([19fa9c0](https://github.com/Protelis/Protelis/commit/19fa9c0eace04b5d07179c3596777de6a0c2815c))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.7 ([b621183](https://github.com/Protelis/Protelis/commit/b62118363b79b0bf744b75491d1832dc4809c628))

### [15.3.2](https://github.com/Protelis/Protelis/compare/15.3.1...15.3.2) (2022-04-20)


### Dependency updates

* **deps:** update dependency io.github.classgraph:classgraph to v4.8.144 ([be7a12b](https://github.com/Protelis/Protelis/commit/be7a12b80610bcf6ed86ad6c19867eaaee1b5a23))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.145 ([11ff96a](https://github.com/Protelis/Protelis/commit/11ff96a24f5a133ab2467e189bfa69bd1eb59f1b))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.146 ([259d423](https://github.com/Protelis/Protelis/commit/259d42305f716114f67d1b170310e82d005d1f71))
* **deps:** update plugin com.gradle.enterprise to v3.10 ([e22d113](https://github.com/Protelis/Protelis/commit/e22d1138163a29abfb7e425acf66426be9271d37))
* **deps:** update plugin kotlin-jvm to v1.6.21 ([efb657d](https://github.com/Protelis/Protelis/commit/efb657d9378ffef31a314e9dc51f76841ce1d11c))
* **deps:** update plugin kotlin-qa to v0.16.0 ([fbee7aa](https://github.com/Protelis/Protelis/commit/fbee7aa30a482edde2c867c193921ed89cd95c0e))
* **deps:** update plugin kotlin-qa to v0.16.1 ([ac27410](https://github.com/Protelis/Protelis/commit/ac274107fb4884c3c72f8b729575a7d15b06bd13))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.9 ([08e82ab](https://github.com/Protelis/Protelis/commit/08e82ab145090bbdb79b55a27851f08bd8f11fe5))
* **deps:** update plugin publishoncentral to v0.7.18 ([1177c23](https://github.com/Protelis/Protelis/commit/1177c23751e221dcc8f8d3f3edae7b2212b6c3b4))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.6 ([4eff83c](https://github.com/Protelis/Protelis/commit/4eff83c9c1bec0819ee87bfd13c9fa1ff156dcc1))

### [15.3.1](https://github.com/Protelis/Protelis/compare/15.3.0...15.3.1) (2022-04-15)


### Dependency updates

* **deps:** update plugin kotlin-qa to v0.14.2 ([56d3783](https://github.com/Protelis/Protelis/commit/56d37839a38a48accda1c2178ffea434078ecad0))
* **deps:** update plugin kotlin-qa to v0.15.0 ([1b4d056](https://github.com/Protelis/Protelis/commit/1b4d056c9fb1203dced4cd44038f98859eb3541c))
* **deps:** update plugin kotlin-qa to v0.15.1 ([ad7aedc](https://github.com/Protelis/Protelis/commit/ad7aedc1bca2cebc787df170226bb2efe7a19a34))
* **deps:** update plugin multijvmtesting to v0.3.5 ([cf8f212](https://github.com/Protelis/Protelis/commit/cf8f21287b89c35d7238693c6651142280be1a01))
* **deps:** update plugin multijvmtesting to v0.3.6 ([74de613](https://github.com/Protelis/Protelis/commit/74de613c04b83255ca5adc2ec7820f68412f2d54))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.7 ([0894e8b](https://github.com/Protelis/Protelis/commit/0894e8b96b1ba68dcb5deac71f607536455eb13c))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.8 ([b848b21](https://github.com/Protelis/Protelis/commit/b848b2112f3275490e03ffd3afd2164baf5960be))
* **deps:** update plugin publishoncentral to v0.7.16 ([b87933c](https://github.com/Protelis/Protelis/commit/b87933c41e51a4b970285f16e7dcfd2cd44413dc))
* **deps:** update plugin publishoncentral to v0.7.17 ([446f86e](https://github.com/Protelis/Protelis/commit/446f86e3bf91738b96f6b1b16cefd6e8a0c25c4f))


### Build and continuous integration

* **deps:** update actions/checkout action to v3.0.1 ([474fc87](https://github.com/Protelis/Protelis/commit/474fc87fc65e2390d79d9db3aabfdac6a47eac8e))
* **deps:** update danysk/build-check-deploy-gradle-action action to v1.2.10 ([26c96df](https://github.com/Protelis/Protelis/commit/26c96df418a07867f20cb2e17e5951bf52f3f086))
* **deps:** update danysk/build-check-deploy-gradle-action action to v1.2.12 ([35b898b](https://github.com/Protelis/Protelis/commit/35b898b48e1d1acf1d49fea9efdfe7b0ef184029))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.5 ([1e2059a](https://github.com/Protelis/Protelis/commit/1e2059a749efc9de8d5fe05e44035e1abe0c0f3b))

## [15.3.0](https://github.com/Protelis/Protelis/compare/15.2.4...15.3.0) (2022-04-02)


### Dependency updates

* **api-deps:** update dependency org.protelis:protelis.parser to v10.2.13 ([1d744df](https://github.com/Protelis/Protelis/commit/1d744df4f00c749c799a3ceeee305b2105368a30))

### [15.2.4](https://github.com/Protelis/Protelis/compare/15.2.3...15.2.4) (2022-04-02)


### Build and continuous integration

* **deps:** update danysk/build-check-deploy-gradle-action action to v1.2.8 ([1b3a3b8](https://github.com/Protelis/Protelis/commit/1b3a3b800292b4be7cdd5916312ad65db7c3f0c8))
* **deps:** update danysk/build-check-deploy-gradle-action action to v1.2.9 ([15ea6f4](https://github.com/Protelis/Protelis/commit/15ea6f4bab2ec47d3038e40151e5c451f9ab39e4))


### Dependency updates

* **deps:** update plugin kotlin-jvm to v1.6.20 ([e0251af](https://github.com/Protelis/Protelis/commit/e0251aff81720c78932e3bcf6d6b6ff6477b6ae1))
* **deps:** update plugin publishoncentral to v0.7.15 ([66adb78](https://github.com/Protelis/Protelis/commit/66adb782662070de256c8fcf1126407f9d833d9c))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.4 ([3e89f99](https://github.com/Protelis/Protelis/commit/3e89f99e5141c85dc67329fd58a3d87cbf19d52b))

### [15.2.3](https://github.com/Protelis/Protelis/compare/15.2.2...15.2.3) (2022-04-01)


### Build and continuous integration

* **deps:** update danysk/build-check-deploy-gradle-action action to v1.2.7 ([27b030c](https://github.com/Protelis/Protelis/commit/27b030c7ee261b91897bed23567b27b2427b8ff8))


### Dependency updates

* **deps:** update dependency gradle to v7.4.2 ([8a88dd8](https://github.com/Protelis/Protelis/commit/8a88dd85a78e4ded329749eed5a4b1852ba17604))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.143 ([f868565](https://github.com/Protelis/Protelis/commit/f86856598a51511c6b1cddfeeb90f96da2d1ca50))
* **deps:** update plugin com.gradle.enterprise to v3.9 ([43cc203](https://github.com/Protelis/Protelis/commit/43cc203a7e008bb6d6310f5a0cbc087e84d0974f))
* **deps:** update plugin java-qa to v0.22.0 ([a04a327](https://github.com/Protelis/Protelis/commit/a04a3272ed51063ea74d672c2e5a8551e0599eb8))
* **deps:** update plugin kotlin-qa to v0.13.0 ([f61375c](https://github.com/Protelis/Protelis/commit/f61375cf4737f0d966ede6d0025a83bbcdce909c))
* **deps:** update plugin kotlin-qa to v0.14.0 ([de52da4](https://github.com/Protelis/Protelis/commit/de52da4cda76fb22d50b6f24dbc3c1232a508d79))
* **deps:** update plugin kotlin-qa to v0.14.1 ([f14939a](https://github.com/Protelis/Protelis/commit/f14939a41b122186cfb69e0c5cdba8003b5b3ad0))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.6 ([49977f3](https://github.com/Protelis/Protelis/commit/49977f3e9e898252537838c39e131a1067706def))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.3 ([22c9605](https://github.com/Protelis/Protelis/commit/22c96053dda2e5074d7a783c7bc8c94d40d2a544))

### [15.2.2](https://github.com/Protelis/Protelis/compare/15.2.1...15.2.2) (2022-03-11)


### Build and continuous integration

* **deps:** update actions/checkout action to v3 ([beb8215](https://github.com/Protelis/Protelis/commit/beb8215728595703b066aab73fea54e3f386ae64))


### Dependency updates

* **deps:** update dependency com.github.spotbugs:spotbugs-annotations to v4.6.0 ([a4cd6d7](https://github.com/Protelis/Protelis/commit/a4cd6d7b17cb78e6e5c1aa8fca6b988bdcffbe92))
* **deps:** update dependency com.google.inject:guice to v5.1.0 ([4efa922](https://github.com/Protelis/Protelis/commit/4efa9220fd819f733dd4c9b00cf3fcf99bcaadaa))
* **deps:** update dependency gradle to v7.4.1 ([45a030e](https://github.com/Protelis/Protelis/commit/45a030e1c5ca20dc332cab83c32ad8d33cea5e10))
* **deps:** update plugin java-qa to v0.21.0 ([3e523c7](https://github.com/Protelis/Protelis/commit/3e523c7267dcca663325d2e31ac01f01415b9653))
* **deps:** update plugin kotlin-qa to v0.12.0 ([e9a094d](https://github.com/Protelis/Protelis/commit/e9a094d02a6423383126dd54883170e2a2c947d7))
* **deps:** update plugin kotlin-qa to v0.12.1 ([07d3d1c](https://github.com/Protelis/Protelis/commit/07d3d1c62957f613fb14520cdee4016881abca11))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.5 ([ff26254](https://github.com/Protelis/Protelis/commit/ff2625482374d9831e264081f9c438c678b7b7b1))
* **deps:** update plugin publishoncentral to v0.7.14 ([7482a6e](https://github.com/Protelis/Protelis/commit/7482a6e49a1eb9750c58ca8be1e1f0d7c00d7a6d))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.2 ([7a92e26](https://github.com/Protelis/Protelis/commit/7a92e26f4190b323a2d8d2e0d17c597cbc95185b))

### [15.2.1](https://github.com/Protelis/Protelis/compare/15.2.0...15.2.1) (2022-03-08)


### Dependency updates

* **deps:** update dependency ch.qos.logback:logback-classic to v1.3.0-alpha14 ([f40c252](https://github.com/Protelis/Protelis/commit/f40c2522a49d92b6413c1ba0c4c1cf3183e2298a))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.141 ([a70a6d0](https://github.com/Protelis/Protelis/commit/a70a6d03d33240bf681de441d2c940fe1d91612f))
* **deps:** update dependency org.slf4j:slf4j-api to v1.7.36 ([bf0803c](https://github.com/Protelis/Protelis/commit/bf0803cb7f7b9f18794f1fbbb504d6c5979d2f9f))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.1 ([2db960f](https://github.com/Protelis/Protelis/commit/2db960f52ec88edf13229dddc29cc7c70d64816e))

## [15.2.0](https://github.com/Protelis/Protelis/compare/15.1.2...15.2.0) (2022-03-07)


### Build and continuous integration

* **renovate:** configure renovate ([2b6167d](https://github.com/Protelis/Protelis/commit/2b6167d5fd03a21f92173e50067417ef861e35c5))
* **renovate:** enable renovate on forks ([14e455d](https://github.com/Protelis/Protelis/commit/14e455dd267c6d6a2cfb9ef291a57e01fe0f67d8))
* **renovate:** updates to protelisdoc trigger a docs-type update ([0c89648](https://github.com/Protelis/Protelis/commit/0c8964881ef9aac5d3175085b62cdc98aa629dac))


### General maintenance

* **build:** remove commented line ([f656c82](https://github.com/Protelis/Protelis/commit/f656c824a07df6646093a16ed7d8ccd565f614ea))


### Dependency updates

* **api-deps:** update dependency org.protelis:protelis.parser to v10.2.12 ([acabf6b](https://github.com/Protelis/Protelis/commit/acabf6b4a8abdc641a5a557fdfd1d5612aff4ede))

### [15.1.2](https://github.com/Protelis/Protelis/compare/15.1.1...15.1.2) (2022-03-06)


### Bug Fixes

* **ci-surge:** export the login information ([3ed60c1](https://github.com/Protelis/Protelis/commit/3ed60c1b59aa6a265abd913e964f6aa8d7579289))


### Build and continuous integration

* **release:** do not search for secrets in the wrong deploy phase ([f815df0](https://github.com/Protelis/Protelis/commit/f815df09932023c7f7cf9c7093a123dea5c138b3))
* **release:** limit the process to 20 minutes ([3008a9b](https://github.com/Protelis/Protelis/commit/3008a9b26ae35290e0455d911383027d4cfe8fe4))

### [15.1.1](https://github.com/Protelis/Protelis/compare/15.1.0...15.1.1) (2022-03-06)


### Bug Fixes

* **interpreter:** fail clearly in case of deserialization issues with LazyField ([bef1f7b](https://github.com/Protelis/Protelis/commit/bef1f7b578aa9980947b5435086b35fd82e1ee86))
* **interpreter:** intercept a potential null pointer exception when reducing ([04f3c2b](https://github.com/Protelis/Protelis/commit/04f3c2b780008712c377c67ae7f24969ad774b35))
* **interpreter:** prevent potential null pointer exception in unboxing of Boolean ([11d552f](https://github.com/Protelis/Protelis/commit/11d552f38f5aa2602bf547d81cc33777f1d4663d))


### Refactoring

* **interpreter:** break long line ([5e800c6](https://github.com/Protelis/Protelis/commit/5e800c6e9fefac4efc77bbb251f95131214782a3))
* **interpreter:** break long line ([3bf0288](https://github.com/Protelis/Protelis/commit/3bf028825f79b6e854acdcc57fcf7355a5138288))
* **interpreter:** break long line ([276ae0f](https://github.com/Protelis/Protelis/commit/276ae0f5091d16efa3e5c6c4167ca69ec47f8fae))
* **interpreter:** let the compiler infer a type argument ([ecb8c41](https://github.com/Protelis/Protelis/commit/ecb8c418860c2116f9f52961565a62e7d0b8f600))
* **interpreter:** let the compiler infer the type argument ([6e58fc9](https://github.com/Protelis/Protelis/commit/6e58fc97e7b4fe662d68ed74147c4535a51d8867))
* **interpreter:** prefer javax annotations over JetBrain's ([ea40536](https://github.com/Protelis/Protelis/commit/ea405367d4b467045e98485c48720396eb10c16f))
* **interpreter:** prefer the diamond operator ([0dafa64](https://github.com/Protelis/Protelis/commit/0dafa645f3b93708493aa1ae48bca24d804627d6))
* **interpreter:** reduce code duplication ([5c14fd5](https://github.com/Protelis/Protelis/commit/5c14fd5fc8867b8d4f71421ba1371c285b4782f6))
* **interpreter:** remove never-thrown exceptions from the flatMap method signature ([78c7a3e](https://github.com/Protelis/Protelis/commit/78c7a3e9525646090dc9e5fe21e34c11b3259677))
* **interpreter:** replace lambda with method reference ([353cba0](https://github.com/Protelis/Protelis/commit/353cba03c304eca16829d29518de6743e923114e))
* **interpreter:** suppress false positive ([4837d78](https://github.com/Protelis/Protelis/commit/4837d78fe0e81cd9ee8bfed3c6cc146454e23e0d))
* **interpreter:** use a clearer parameter name ([f231fc7](https://github.com/Protelis/Protelis/commit/f231fc7bcdee0db925c4bdfe3d5b8ca4601b4661))
* **interpreter:** use clearer parameter names ([1033f69](https://github.com/Protelis/Protelis/commit/1033f69143069f76b209b9d3be1f3cfe2594111a))


### General maintenance

* **gitignore:** ignore all .gradle folders ([2444c55](https://github.com/Protelis/Protelis/commit/2444c559600e6cabd60457e71a129747556bd9a8))
* **interpreter:** break long line ([9370b48](https://github.com/Protelis/Protelis/commit/9370b482465583e8c7b0f30d94a2a948265648a1))
* **interpreter:** break long line ([822388b](https://github.com/Protelis/Protelis/commit/822388b4dc586e5714fa401ca299c27c7e76d30a))
* **interpreter:** drop outdated docs ([1570e1b](https://github.com/Protelis/Protelis/commit/1570e1bf6b1518c53d3aaa7d290d46f970302744))
* **interpreter:** honor @Nonnull annotation ([4764380](https://github.com/Protelis/Protelis/commit/476438072c40e2a424af95e3779967d4d68f0a76))
* **interpreter:** remove commented line ([f4c1148](https://github.com/Protelis/Protelis/commit/f4c114873300ff26cdff90abcd40d11c9bea650c))
* **interpreter:** remove redundant suppression ([2311579](https://github.com/Protelis/Protelis/commit/2311579a6c19bfd75af7d753a006fa9fd4286c82))
* **interpreter:** suppress unused warning, the method is part of the public API ([1cceaef](https://github.com/Protelis/Protelis/commit/1cceaefbdd02bb5b55129acf51a5f7cd809e680e))
* **interpreter:** suppress warning as the behavior is desired ([601ff9a](https://github.com/Protelis/Protelis/commit/601ff9a6acc1e7cba799e3294d6ced44f338294a))
* **interpreter:** suppress warning that will be there until legacy hood calls get dropped ([6a96114](https://github.com/Protelis/Protelis/commit/6a96114974b09b9aadfc94d96823102afa968b81))


### Dependency updates

* **deps:** upgrade Gradle to 7.4 ([f339f2e](https://github.com/Protelis/Protelis/commit/f339f2e3adbee13a97a6daecc9cb9f4d2a4d16f4))


### Style improvements

* **interpreter:** break long line ([540cfb4](https://github.com/Protelis/Protelis/commit/540cfb4c0dd9bad53fb153926688e26562f91658))
* **interpreter:** break long line ([aa24832](https://github.com/Protelis/Protelis/commit/aa248323cb20287c2259759a5328d1cf556210ee))
* **interpreter:** break long line ([fb4a9ff](https://github.com/Protelis/Protelis/commit/fb4a9ff0f6d61102bc3796ade01f360adf63f96b))
* **interpreter:** break long line ([c3d4365](https://github.com/Protelis/Protelis/commit/c3d436599a9c54db3530c097d53e5b0e3917d1f4))
* **interpreter:** break long line ([911e5d1](https://github.com/Protelis/Protelis/commit/911e5d16f2e370cf754db6e84b2dcd93ec796007))
* **interpreter:** break long line ([a98f8b4](https://github.com/Protelis/Protelis/commit/a98f8b461224ca1e444aa1b249f5722e0e4309f1))
* **interpreter:** break long line ([a4c96ec](https://github.com/Protelis/Protelis/commit/a4c96ec39a7be8c68203f3b743ccdd3c88c0dcc0))
* **interpreter:** break long line ([3f1032e](https://github.com/Protelis/Protelis/commit/3f1032ee93c6becac0fbe2ba3d91eb9cd2a0246d))
* **interpreter:** break long line ([4a5df02](https://github.com/Protelis/Protelis/commit/4a5df0266126aa921e84eae2abef898a3291464b))
* **interpreter:** break long line ([eb4c0eb](https://github.com/Protelis/Protelis/commit/eb4c0eb68a0716f0408e9e7cc686ae8f63f474f4))
* **interpreter:** break long line ([9a223d5](https://github.com/Protelis/Protelis/commit/9a223d5c83eec7e8543d045a90dc9d8625d77ca8))
* **interpreter:** break long line ([6863fe5](https://github.com/Protelis/Protelis/commit/6863fe51e29db49a9942fc530ece8a8f0485e224))
* **interpreter:** break long line ([d3ae4ec](https://github.com/Protelis/Protelis/commit/d3ae4ecf09c0bb1c589bbb0368497172228f7ea9))
* **interpreter:** let the compiler figure out a type argument ([94864e2](https://github.com/Protelis/Protelis/commit/94864e234518f05b1891fa285a66f4fc6c4bb3b5))
* **interpreter:** make call more functional ([c10d7ce](https://github.com/Protelis/Protelis/commit/c10d7ce61c0c559dd7f00c92e4ed413b64f22f79))
* **interpreter:** optimize imports in Option ([7779043](https://github.com/Protelis/Protelis/commit/77790438897c91020a04fe7c677810e109b5f590))
* **interpreter:** style fixes in HoodOp ([97a614a](https://github.com/Protelis/Protelis/commit/97a614aa81dcf5a0087b46147f819026b1f515cc))
* **test:** break long line ([d8cb7bc](https://github.com/Protelis/Protelis/commit/d8cb7bc00c6f0764ac7da3ecfff343839e891c18))
* **test:** style fixes across test sources ([78da7f0](https://github.com/Protelis/Protelis/commit/78da7f0d5870a9ce5c5f6c23d9dd1cbf6181d849))
* **test:** style fixes in InfrastructureTester ([ddb73f7](https://github.com/Protelis/Protelis/commit/ddb73f7146e5ffc3523b4cdd17a11f73a4990c69))
* **test:** style fixes in TestEqual ([8a314ba](https://github.com/Protelis/Protelis/commit/8a314bae17a031fcc1b67614afa1507e6ba8ef27))
* **test:** suppress a PMD false positive ([7794f36](https://github.com/Protelis/Protelis/commit/7794f36b9b2c11457e757a2912187122798a0862))
* **test:** suppress checkstyle on Alchemist-inherited class ([191d7b2](https://github.com/Protelis/Protelis/commit/191d7b25343a6a169375278a62fc691f584b672f))


### Documentation

* fix the javadoc and disable apiviz ([369b379](https://github.com/Protelis/Protelis/commit/369b37924bf9d18d9f195cdd070837c996622015))
* **interpreter:** break long lines ([a960a0a](https://github.com/Protelis/Protelis/commit/a960a0a30368057ff76dec3e0ed22e8ae3c25bdb))
* **interpreter:** break long lines ([f4e9778](https://github.com/Protelis/Protelis/commit/f4e9778902dede9900ee1e427f1df2f07ef81012))
* **interpreter:** document Either ([20dc368](https://github.com/Protelis/Protelis/commit/20dc368bff21109f6335f566331aaf60908a03c8))
* **interpreter:** document Field.Builder ([4ddfa01](https://github.com/Protelis/Protelis/commit/4ddfa013376df99af17976e81ea1955a659a4a67))
* **interpreter:** document get/set Persistent ([3d35b38](https://github.com/Protelis/Protelis/commit/3d35b3899e24abb9492030c561691fdfea888ef0))
* **interpreter:** document getStoredState ([f03e2b4](https://github.com/Protelis/Protelis/commit/f03e2b422676995bb3c9c1d2a36a032e909fdd4f))
* **interpreter:** document HashingFunnel ([8961355](https://github.com/Protelis/Protelis/commit/896135555f88c580a2f1fbb04bd7950c871aa0fd))
* **interpreter:** document Java8CompatibleFunnel ([9bc9524](https://github.com/Protelis/Protelis/commit/9bc95245a1a27fab0aa2d70865f184080fef39e7))
* **interpreter:** document JVMEntity constructor ([b319891](https://github.com/Protelis/Protelis/commit/b31989166c805ce250c62eb267feb21233f54224))
* **interpreter:** fix typo ([70f260a](https://github.com/Protelis/Protelis/commit/70f260adcbd5eb3a0df636ca945a085162b41726))
* **interpreter:** remove outdated documentation on private method ([9c06be6](https://github.com/Protelis/Protelis/commit/9c06be623d14cb52feaadfdb6fdc4376ac28bbdc))
* **test:** document getError ([fb7627f](https://github.com/Protelis/Protelis/commit/fb7627fb12159e92109414669d538fbf91b11471))


### Build and continuous integration

* **deps:** update the build action to 1.2.6 ([ef507e0](https://github.com/Protelis/Protelis/commit/ef507e0d3cecc561780178bd6a32089b47bc8fc5))
* **deps:** update to build-check-deploy-gradle-action@1.2.5 ([e9814c2](https://github.com/Protelis/Protelis/commit/e9814c20ce1f18154b81676a085e2a8b62453734))
* disable the cronjob ([7a6366e](https://github.com/Protelis/Protelis/commit/7a6366ee58bcd60ab25d848d86e4f40dc33e8a27))
* modernize the build ([3d07863](https://github.com/Protelis/Protelis/commit/3d078638e9ff2d8c9c8fc5ac6dca2dcaed45778c))
* provide secrets to sign artifacts for dry-deployment ([40d38c8](https://github.com/Protelis/Protelis/commit/40d38c8ce55f6fd11b5ae39e5cd7b3ea6d2b1476))
* switch to semantic release ([fcbc480](https://github.com/Protelis/Protelis/commit/fcbc48044edb07985ad666eb64a32dd583f5fe9e))

# CHANGELOG

## 10.1.0
* Removes protelis-test from protelis compile dependencies

## 10.1.1
* Make protelis-lang actually available

## 10.1.2
* Fix calls to non existing self.dt()

## 10.1.3
* Fix error in CRF gradient

## 10.1.4
* Always evaluate self as the current ExecutionContext

## 10.2.0
* Fixed memory leak with nbr(self) inside alignedMaps
* Fixed bug that left files opened in case many Portelis programs were instanced at the same time
* Improved references print out (the original name is preserved)
* Improvements to protelis-lang
* Added Tuple.toArray

## 10.3.0
* Fixed bug with wrong initialization of first stack frame
* Fixed bugs with dependency ranges

## 10.3.1
* Fix alignment bug
* Improve gradient testing

## 10.4.0
* Fix bug in unionHood
* Fix several bugs in protelis-lang
* Work with vararg methods

## 10.5.0
* Fixed bug with unionHood
* Added publishSubscribeWithPotential
* Improved protelis-lang library
* Raised Guava requirement to 19.0

## 10.5.1
* New BIS gradient
* Once from Proto
* Add comparable UIDs
* Improved S
* hopBroadcast
* Improved documentation of protelis-lang
* Internal safety checks
* Improved code quality

# 10.5.2
* Use Threadlocals in ProtelisLoader to allow for parallel loading of different programs with different class loaders

## 10.6.0
* General improvement to code quality
* Lots of classes correctly finalized
* Fixed error with lambda serialization and execution of tests in Eclipse Photon as per #157
* Improve reporting on method names on invocation errors as per #153
* Verify accessibility on methods in private inner classes as per #154

## 10.7.0
* Separate versions for Java7 (legacy)

## 11.0.0
* Protelis stacktraces
* Share block
* Improved protelis-lang
* Improved internal architecture

## 11.1.0
* Allow imports in any order #133
* Importing static variables from Java #139
* Avoid wrappers for Java methods in first-class functions #180

## 12.0.0 
* Upgrade to Gradle 5
* Retrolambda incompatibility solved
* Fix #195
* Improved build system
* Bytecode / Instruction Set for Protelis
* Protelis infrastructural support for efficient networking, see #166
* Fix #83 (bug in alignment)
* Fix #96 (closure implementation)
* Fix issue #104 (Broken field alignment on variable restriction)
* Fix issue #106 (Broken meanHood)
* protelis-interpreter now exposes API transitive dependencies as API
* Better alignedMap performance (in time and size), see #205
* Fix #82 (broken eval alignment)
* API Cleaning

## 12.1.0
* Hardlink Java features (methods and fields)
* Upgrade to Gradle 5.4.1
* Document usage of HashingCodePathFactory

## 12.2.0 
* Upgraded to Parser 9.1.0
* Protelis Option type and null-factory for better Java interoperability
* Improved code quality

# 13.0.0
* Kotlin style lambda
* Fix #75
* Invocation without apply
* Invocation with lambda out-of-parenthesis
* Fixed critical performance bug
* Lambda alignment based on qualified name

# 13.0.1
* Load resources via thread inheritable resource loader

# 13.0.2
* Always load resources using the current thread class loader

# 13.0.3
* Fix bug #262, preventing short function syntax

# 13.1.0
* Protelis has now its [Unit Type](https://en.wikipedia.org/wiki/Unit_type), as per #275 and #278.
The Unit type is associated solely to Java method calls that return `void`
This introduces a non-retrocompatible change when using Protelis Options: `fromNullable(methodReturningVoid()).isEmpty()` was previously returning `true`,
as void methods where considered equivalent to methods returning `null`.
Now, such invocation returns instead a `Present(Unit)` `Option`, and, consequently, the same invocation returns `false`.
For any other use, this change widens the pool of semantically allowed sentences in the language,
so non-working code doe to null strictness could be working now,
but there should be no other non-retrocompatible change.
* Eclipse plugin should now notify when used in conjunction with an unsupported version of the interpreter, fixing #245
* Protelis stacktraces are now printed as part of the Java exception message as per #270, allowing for the traces to get displayed when logging only messages.
This fixes #271, #257, and #231.
Code depending on parsing the ProtelisRuntimeException message may break.
* String representation of `DefaultTimeEfficientCodePath` changed and is now much clearer,
and usable for debug (if who debugs knows the internals of the interpreter, of course).
Code depending on converting `CodePath` to Strings may get broken. See #281.
* Auto-arcs (namely, a device being neighbor of itself) produced by `NetworkManager`s are now detected,
the anomaly is internally intercepted and corrected, and a warning message is pushed in the logs. See #281.
* `optionally` is now a builtin, produces an `Option` out of a computation, allowing for method chaining, e.g.
`optionally(methodMayReturnNull()).map { it.toString() }.orElse("null")`.
Code importing java code with static methods named `optionally` may shadow the new builtin.
* Protelis can now access Java `public` `static` fields yielding `null`. Of course,
to be brought into the interpreter, they need to get wrapped in an `Option`.
* Failure in invoking 0-ary methods now fails fast, see #281.
* Improved and updated README.md, fixes #255.
* Tests added for foldMin, foldMax, foldUnion, see #276
* Improved build system, with in-memory armoured signing on the CI
* Exceptions on method invocation now print Java-style braces. Code matching error messages may be broken.

## 13.1.1
* Fixed multiInstance over-sharing, by preventing extension outside of the list of sources passed in.
Only software using multiInstance or other functions that rely on multiInstance (e.g. multiGradient) may be affected.
Information share will now remain bounded to the expected area.
Software relying on the over-sharing bug to work should get properly fixed (it's a very corner case, though).

## 13.2.0
* Java 14 support
* Protelis-parser 10.0.2, Xtext 2.21.0
* Gradle 6.3
* Switch from buildSrcVersions to refreshVersions
* Improved build system and continuous integration

## 13.2.1
* Protelis now uses Kotlin 1.3.71 to build its ProtelisDoc.

## 13.3.0
* Apache Commons Lang 3 has been upgraded to 3.10. Projects using Apache Commons Lang 3 and Protelis should see the [Apache Commons Lang 3 changelog for 3.10](http://commons.apache.org/proper/commons-lang/release-notes/RELEASE-NOTES-3.10.txt). No other change in Protelis, full backward compatibility expected.

##  13.3.1
* Upgraded Classgraph to 4.8.68. This should not impact any Protelis user. No changes in syntax, semantics, or tooling
* Upgraded ProtelisDoc to 0.2.1.

## 13.3.2
* Upgraded Classgraph to 4.8.69. This should not impact any Protelis user. No changes in syntax, semantics, or tooling

## 13.3.3
* Upgraded Kotlin to 1.3.72. No relevant change for our users, it is only used for mapping types when the protelis-lang protelisdoc gets generated
* Upgraded Guava to 29.0-jre. Users in need of older versions may still force their favorite version of Guava, no change in code for Protelis has been performed. Please check the changelog of Guava 29.0-jre to see if any relevant change applies to you: https://github.com/google/guava/releases/tag/v29.0

## 13.3.4
* Upgraded Classgraph to 4.8.70. No known relevant change for Protelis users.

## 13.3.5
* Upgraded Classraph to 4.8.71. No relevant change for Protelis' users. Classgraph changelog available at https://github.com/classgraph/classgraph/releases/tag/classgraph-4.8.71

## 13.3.6
* Upgraded Classgraph to 4.8.72. No relevant change for Protelis' users. Classgraph changelog available at https://github.com/classgraph/classgraph/releases/tag/classgraph-4.8.72

## 13.3.7
* Upgraded Classgraph to 4.8.73. Improves performance. No changes for Protelis users.

## 13.3.8
* Updated some build tools (Gradle and Spotbugs). No changes for Protelis users.
* Guava upgraded to 29.0-jre. No changes in code, so backward compatibility is still guaranteed, but the newer guava will get pulled automatically unless otherwise specified if you use Protelis in a Gradle project. Check the [changelog of Guava 29.0](https://github.com/google/guava/releases/tag/v29.0) to see if and how this may affect you.

## 13.3.9
* Upgraded commons-io to 2.7. Since no relevant change was introduced, projects using Protelis and requiring commons-io 2.6 can still work correctly
* Upgraded tools used for testing, QA, and build automation: Gradle, SpotBugs, and Classgraph. No impact on end users of Protelis.

## 14.0.0
* Protelis switches to the newer Higher Order Field Calculus semantics,
dropping superscripts, and with state persistence offloaded to the context.
A description of the changes is available in #368.
The new API features a refactored name (ProtelisAST),
and drops superscripting: who extended the interpreter core will need to follow the new architecture
and update their code.
In particular, nodes of the abstract syntax tree that need to retain state across rounds
should inherit from AbstractPersistedTree and use the `saveState` and `loadState` methods.
* The `copy()` method on AST nodes has been dropped.
There is no longer any need to duplicate nodes, as they are now stateless,
hence the method is useless and perilous, and has been dropped.
* All the formerly working code under 13.3.x should be still working with 14.0.0.
The only relevant change is on the alignment capabilities of `Tuple`'s `map`, `filter`, and `reduce` operations:
it is now allowed to make stateful function calls, solving bug #367.
* A few minor tweaks to the build system, and upgraded Gradle.

## 14.1.0
* Protelis now supports Java 15
* Updated Xtext to 2.23.0
* Updated commons-lang to 3.11, classgraph to 4.8.89, commons-codec to 1.15, commons-io to 2.8.0, guava to 30.0-jre. None of them should cause issues, and Protelis allows for forcing the previous versions.
* Improved ProtelisDoc documentation, based on Dokka 1.4
* Artifacts published on Maven Central now have additional secure hashes
* Minor code quality improvements
* Protelis is now built on Ubuntu Focal

## 14.1.1
* Protelis now have an appropriate .gitattributes file improving development under Windows
* Protelis dropped Travis CI due to the recent pricing policy change, which is hostile to FOSS
* Protelis is now built on GitHub Actions

## 14.1.2
* Improved testing in environments with a locale other than en_US
* Upgraded classgraph to 4.8.93
* Upgraded spotbugs-annotations to 4.2.0
* Upgraded publish-on-central to 0.4.0

## 14.1.3
* Upgraded classgraph to 4.8.98
* Minor changes to improve the code quality
* Upgraded publish-on-central to 0.4.1
* Upgraded Gradle to 6.8
* Modularized PMD rules (use git submodule update to download and update them)
* Update PMD to 6.30.0

## 14.1.4
* Protelis can now be found, both stable and beta releases, on github packages.
This allows for easy experimentation with any version.
Please note that at the moment of writing, GitHub Packages requires a token with package:read permissions to be provided even for accessing public package releases.
Also, the redistributable jar is now generated via shadowJar, improving the final executable size.

## 14.1.5
* Improved automation of fat-jar releases

## 14.1.6
* API extension: it is now possible, from an AbstractExecutionContext, to safely (non-disruptively) access the current shared state.
* Build system update: Gradle is now at 6.8.1

## 14.1.7
* Slight improvement to Tuple performance, see 6c551cd1bb73a2d7bab8d10110343fa69d84279b
* Implemented Tuple.zip, see 6ba6efa655ef7f82233157a3f7ed73bd1bd0fce2
* Minor code cleaning, see 2837b5098dcf007c1f5211a42e4f5bcd407eb4a0 and a378c151130543a783ab766f30273bd3cbb0f29b
* Upgraded Classgraph to 4.8.102 92f130c41131c4007ebd0c0cdf575bb0a04d2d98

## 14.1.8
* Fixes #457, providing inspection access for static code checking
* Fields can now be manipulated more efficiently in Java via `getIfPresent`
* Recovered the main artifact generation
* PMD upgraded to 6.31.0
* publis-on-central upgraded to 0.4.2
* Gradle upgraded to 6.8.2
* Ktlint upgraded to 10.0.0
* Spotbugs Annotations upgraded to 4.2.1
* JUnit upgraded to 4.13.2

## 14.1.9
* Functionally equivalent to 14.1.8. The only difference is that `HashingCodePath.toString()` now returns a Base64 view of the hash, rather than resorting to the default `Object.toString()` implementation.
This method was not supposed to be used for purposes other than debug anyway
(and it was pretty useless in its original form),
so this release should be for all users a de facto drop-in replacement.

## 15.0.0
* Initial support for Java 16
* Protelis parser updated to 10.2.0, which pulls in Xtext 2.26.0.M1 and Guice 5.0.1
* `AlignedMap` has changed and now requires an external hashing provider. A default implementation has been written and it is currently hard-coded as default
* Protelis no longer depends on FST

## 15.1.0

Introduces a simplified version of the per-neighbor communication, [see #536](https://github.com/Protelis/Protelis/pull/536).
This release should be a drop-in replacement of Protelis 15.0.0:
the new per-neighbor communication in `share` exploits a previously forbidden situation,
in which the `share` block evaluation returned a field.

Other changes:
* Google Guice 5.0.1 is forced in as a runtime environment, improving support for Java 16
* Small performance improvement when operating with fields
