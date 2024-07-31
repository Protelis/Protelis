## [17.3.44](https://github.com/Protelis/Protelis/compare/17.3.43...17.3.44) (2024-07-31)

### Dependency updates

* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.94 ([c623c5b](https://github.com/Protelis/Protelis/commit/c623c5b1b9e9b13cc149d35ef12b8de33bfad3a3))
* **deps:** update plugin kotlin-qa to v0.65.0 ([080a377](https://github.com/Protelis/Protelis/commit/080a377edd99892289280d53f7d85c87c5b6dc5f))

### Documentation

* **deps:** update plugin protelisdoc to v3.0.56 ([6f89cf9](https://github.com/Protelis/Protelis/commit/6f89cf9b4d0164ba7d4bcd9ce13f87e05b9a8470))

## [17.3.43](https://github.com/Protelis/Protelis/compare/17.3.42...17.3.43) (2024-07-31)

### Documentation

* **deps:** update plugin protelisdoc to v3.0.55 ([2499dd1](https://github.com/Protelis/Protelis/commit/2499dd1a2c06b08b4a6167b6a877db749c1628bd))

## [17.3.42](https://github.com/Protelis/Protelis/compare/17.3.41...17.3.42) (2024-07-31)

### Dependency updates

* **deps:** update plugin java-qa to v1.60.2 ([ed8a734](https://github.com/Protelis/Protelis/commit/ed8a734b20b6b300a95571fa269323b57728e173))
* **deps:** update plugin kotlin-qa to v0.64.3 ([01846c4](https://github.com/Protelis/Protelis/commit/01846c4224f745de0812de2c77aef63da4237f78))

### Documentation

* **deps:** update plugin protelisdoc to v3.0.54 ([713b707](https://github.com/Protelis/Protelis/commit/713b707bb166850a4670f8e35b262b53a744a5d8))

## [17.3.41](https://github.com/Protelis/Protelis/compare/17.3.40...17.3.41) (2024-07-31)

### Dependency updates

* **deps:** update org.danilopianini.gradle-java-qa to 1.60.1 ([97db12e](https://github.com/Protelis/Protelis/commit/97db12e1518d909ad612b5249d43c5e6d04373cc))
* **deps:** update org.danilopianini.gradle-java-qa to 1.60.2-dev03-737f1da and remove redundant suppressions ([e3583d5](https://github.com/Protelis/Protelis/commit/e3583d5e303ec8b812f6303d4021de64ea32329b))
* **deps:** update org.danilopianini.gradle-java-qa to 1.60.2-dev04-1928fe1 ([68b232d](https://github.com/Protelis/Protelis/commit/68b232d32e1efcb36dfb5fd2430de4fe4ff85bbf))
* **deps:** update plugin java-qa to v1.59.0 ([547c5bb](https://github.com/Protelis/Protelis/commit/547c5bb0d8ccd5d75de568233f51ceb83efad111))
* **deps:** update plugin kotlin-qa to v0.64.1 ([6e00921](https://github.com/Protelis/Protelis/commit/6e009210fa5ae1e9fc4893942152f0c3544d70b0))

### Bug Fixes

* **interpreter:** declare compatibility with Protelis parser 11 ([f7d2f7b](https://github.com/Protelis/Protelis/commit/f7d2f7ba28525dce81b51bc41392291e12d3c219))
* **test:** fix bug in lexicographic ordering of class names ([97d7034](https://github.com/Protelis/Protelis/commit/97d7034343d466ea0de2e83db7dd1c53af3d2a81))

### Documentation

* **interpreter:** add a `<p>` tag to prevent white spaces from being ignored ([b4f88ec](https://github.com/Protelis/Protelis/commit/b4f88ec72fed4c4d2cb863c52c9da54ab7a181f3))
* **interpreter:** add a `<p>` tag to prevent white spaces from being ignored ([a28c7ba](https://github.com/Protelis/Protelis/commit/a28c7ba615bb387d39c04aedb1df26211d72f33f))

### Performance improvements

* **interpreter:** avoid concatenating nonliterals in a String Buffer ([8f45833](https://github.com/Protelis/Protelis/commit/8f4583394d6a8af61766e93215d0e37c2ebdb0c3))
* **interpreter:** initialize a larger StringBuilder ([e6fc531](https://github.com/Protelis/Protelis/commit/e6fc53194874af0eb0a97642bdee40177ddcb129))
* **interpreter:** remove dangling javadoc comment in `ProtelisRuntimeException` ([1c2a627](https://github.com/Protelis/Protelis/commit/1c2a627dcce029d8eeee6ed033deae8cbe88383b))
* **interpreter:** use a larger string buffer in `OpUtils` ([2923ac0](https://github.com/Protelis/Protelis/commit/2923ac07d8cbb0400d0cc79d0c00a02355554547))

### Revert previous changes

* **interpreter:** fix Protelis exception management error introduced with a style-fix commit ([0239b33](https://github.com/Protelis/Protelis/commit/0239b3352aa8319f276ef66481398b0355bbc751))

### Tests

* **test:** fix broken debug logging call ([6aca47b](https://github.com/Protelis/Protelis/commit/6aca47b3d23236cff67b0492fc54993ea1ea1a2b))

### Build and continuous integration

* **deps:** update danysk/build-check-deploy-gradle-action action to v3.5.1 ([8c22005](https://github.com/Protelis/Protelis/commit/8c22005e478d38e6df095e2b0499bb950b7ca3eb))

### Style improvements

* **interpreter:** add missing @Nonnull annotations in `LazyField` ([f011707](https://github.com/Protelis/Protelis/commit/f011707778211851376a89d498eebc4971c5b68f))
* **interpreter:** avoid concatenating nonliterals in a StringBuffer/StringBuilder constructor or append() ([8237a99](https://github.com/Protelis/Protelis/commit/8237a99bb9e4bbdc3c71e443251a5620d0cf46ec))
* **interpreter:** avoid unnecessary qualifier ([2d02fa6](https://github.com/Protelis/Protelis/commit/2d02fa6e429daacebfe305dc08876e90b062486f))
* **interpreter:** fix usage of nullability annotations in `ReflectionUtils` ([38de187](https://github.com/Protelis/Protelis/commit/38de187142b225a15f3d025ca86b1b94758653ea))
* **interpreter:** make local variable final ([55de6e6](https://github.com/Protelis/Protelis/commit/55de6e6c2b3a5cd8c2422c6edb5f7e71ea64fe76))
* **interpreter:** make local variable final in `Option` ([d1244c3](https://github.com/Protelis/Protelis/commit/d1244c39f9c1be0fa03712a7f690bdaf0cc3026e))
* **interpreter:** prefer `Map` over `HashMap` in `FunctionCall` ([f0dd9b4](https://github.com/Protelis/Protelis/commit/f0dd9b46da984db2975dc54fde42e94121de573b))
* **interpreter:** prefer method chaining for string buffer ([3f133fd](https://github.com/Protelis/Protelis/commit/3f133fd0484128158a33d918a8e05c6a300f42da))
* **interpreter:** remove never thrown exception from the method signature in `LazyField` ([9860f76](https://github.com/Protelis/Protelis/commit/9860f767a2e917f660e8a0226dd6fb41e60d1268))
* **interpreter:** remove unnecessary casts in `Tuples` ([98b1c54](https://github.com/Protelis/Protelis/commit/98b1c54fc3f71196eb2f6c51be1155a7e045da60))
* **interpreter:** remove unnecessary qualifier `JavaInteroperabilityUtils` ([7672fd0](https://github.com/Protelis/Protelis/commit/7672fd0709e0465b953f696cb074c53ffec18b7f))
* **interpreter:** remove unnecessary qualifier `Op2` ([7b47e05](https://github.com/Protelis/Protelis/commit/7b47e05aa5a7b01dc95fa080e95fea8ba60ed4ff))
* **interpreter:** remove unused formal parameter in `Op2`'s method ([9e1beba](https://github.com/Protelis/Protelis/commit/9e1bebafe02f629f45e38d3e6f91db7f867e0471))
* **interpreter:** remove unused formal parameter in `Op2`'s method ([24e04a9](https://github.com/Protelis/Protelis/commit/24e04a97988b7bede05b5591e7d9e4307bf2d33b))
* **interpreter:** remove unused import in `ReflectionUtils` ([2ad9a65](https://github.com/Protelis/Protelis/commit/2ad9a655d720a2bcb74c13a671fbc8bebb0ea839))
* **interpreter:** replace explicit type arguments with a diamond ([39c598d](https://github.com/Protelis/Protelis/commit/39c598d16c910c2065630c5d7292079b786d6360))
* **interpreter:** replace explicit type arguments with a diamond: `new CacheLoader<>()` ([eda110b](https://github.com/Protelis/Protelis/commit/eda110b8d4fc52f99ad648817cc20f6f9aa5fba5))
* **interpreter:** replace lambdas with method references in `Op2` ([af91c17](https://github.com/Protelis/Protelis/commit/af91c17f88d3e27ead41288e3a06b0c33acc0e1a))
* **interpreter:** simplify optional chain ([51f7e96](https://github.com/Protelis/Protelis/commit/51f7e968bcd4f18797a9e83681e74b6352c7a15d))
* **interpreter:** suppress PMD warning on Protelis Exception creation ([c314c3a](https://github.com/Protelis/Protelis/commit/c314c3aa3bcf315497813c8a719e80181fe9d959))
* **interpreter:** suppress PMD warning on unused parameter ([ec35e12](https://github.com/Protelis/Protelis/commit/ec35e127f8c2b24747c4793c823fe736b4388377))
* **interpreter:** use `Optional.of` in `LazyField` ([067d800](https://github.com/Protelis/Protelis/commit/067d80087932905a72f553628269dfbec5fa3680))
* **interpreter:** use diamond operator in `LazyField` ([e6e8aac](https://github.com/Protelis/Protelis/commit/e6e8aac018273d1224448f959b0bd95084c4fa69))
* **interpreter:** use diamond operator in `LazyField` ([1b051dd](https://github.com/Protelis/Protelis/commit/1b051dd5bb7eabe3f983d74af11ca16544aaa4c5))
* **interpreter:** use diamond operator in `LazyField` ([837ab36](https://github.com/Protelis/Protelis/commit/837ab36b948097fa24fe5983623cf4d5d71408fb))
* **interpreter:** use diamond operator in `ReflectionUtils` ([90e905f](https://github.com/Protelis/Protelis/commit/90e905f70b9781a85bd7d12640a15e398692c864))
* **test:** deal with variables that PMD thinks are final ([2ecc11d](https://github.com/Protelis/Protelis/commit/2ecc11d4195f764b6d2132fdf51fc12301eb1966))
* **test:** do not use unnecessary qualifier `ReflectionUtils` inside the same class ([8adc07c](https://github.com/Protelis/Protelis/commit/8adc07cadb82268d392ecb7f5a38591746c61662))
* **test:** fix "Unnecessary explicit array creation for varargs method call" ([fa1b1d8](https://github.com/Protelis/Protelis/commit/fa1b1d817c859806ed1979088cde5ea8bc70bfb0))
* **test:** fix "Unnecessary explicit array creation for varargs method call" ([6273acb](https://github.com/Protelis/Protelis/commit/6273acb94bae1b3e60fbacb5c805da87a9314057))
* **test:** fix "Unnecessary explicit boxing" ([cecfd31](https://github.com/Protelis/Protelis/commit/cecfd31028c25def862aae8ba80baffcaaea33f7))
* **test:** fix indentation ([81cb2d7](https://github.com/Protelis/Protelis/commit/81cb2d7d1dc2d44b48be717b2a6813354a011b55))
* **test:** fix wrong method indentation ([f195c03](https://github.com/Protelis/Protelis/commit/f195c0330a909c714e54415f0310af83faadde55))
* **test:** make local variable final ([762c8c1](https://github.com/Protelis/Protelis/commit/762c8c17cff2a3047a34845277cd409c32ae45de))
* **test:** make test result parsing cleaner ([711008f](https://github.com/Protelis/Protelis/commit/711008fc680a0f37688f0963eb533693787aa7f3))
* **test:** remove redundant casts ([a8b6c55](https://github.com/Protelis/Protelis/commit/a8b6c5501a32304687aa2b15bd0e459d5c1a7593))
* **test:** remove singular field ([dc74c56](https://github.com/Protelis/Protelis/commit/dc74c561821733f944138d4826bbb08340179ad4))
* **test:** remove unnecessary double conversion ([6a7b29f](https://github.com/Protelis/Protelis/commit/6a7b29fd95a0c65ffd1baaf90c0294f7dbbac53a))
* **test:** replace instanceof with null checks ([d23ceb5](https://github.com/Protelis/Protelis/commit/d23ceb58a06542bb933b546aa0a2469b056bb3cb))
* **test:** replace lambda with method reference ([e18b67d](https://github.com/Protelis/Protelis/commit/e18b67dc4ba7fa1d4f613a2298477ac0494eb8b8))
* **test:** replace lambda with method reference ([ca75e12](https://github.com/Protelis/Protelis/commit/ca75e124052a9b532e8625e97f4ecdf42e242710))
* **test:** replace lambda with method reference ([c907a3c](https://github.com/Protelis/Protelis/commit/c907a3ccb4eb563fa8fecdf9050a986455d24eab))
* **test:** replace lambda with method reference ([33b980d](https://github.com/Protelis/Protelis/commit/33b980d7e602982371437149897b26378de3f88e))
* **test:** replace URL with HTML link ([d4456d8](https://github.com/Protelis/Protelis/commit/d4456d86f7b8cafbac077310d7b40513c0349218))
* **test:** suppress PMD warning on ImmutableMap used as type ([ceb7eea](https://github.com/Protelis/Protelis/commit/ceb7eea1e18530e75bc883413099051b9436167d))

## [17.3.40](https://github.com/Protelis/Protelis/compare/17.3.39...17.3.40) (2024-07-30)

### Dependency updates

* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.93 ([4e985b5](https://github.com/Protelis/Protelis/commit/4e985b5d7dc2b6db347dbe179d1987184316d9a3))
* **deps:** update node.js to 20.16 ([f069536](https://github.com/Protelis/Protelis/commit/f0695367a6a18521a19447e4fd924a78e748d4cd))

### Documentation

* **deps:** update plugin protelisdoc to v3.0.53 ([1113fa9](https://github.com/Protelis/Protelis/commit/1113fa9c7627f69b5c0a0d8f8e14e28b45a8fe98))

## [17.3.39](https://github.com/Protelis/Protelis/compare/17.3.38...17.3.39) (2024-07-26)

### Dependency updates

* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.92 ([460fc2b](https://github.com/Protelis/Protelis/commit/460fc2baccfff5064b592d68c0e11a76e02f35d0))
* **deps:** update plugin com.gradle.develocity to v3.17.6 ([026e470](https://github.com/Protelis/Protelis/commit/026e4708caa4cb33bc6bd90e8397923e1e1829cc))
* **deps:** update plugin kotlin-qa to v0.64.0 ([dd82ed7](https://github.com/Protelis/Protelis/commit/dd82ed7672e0d765828d73eb95a5062dd17aee52))
* **deps:** update plugin multijvmtesting to v1.1.1 ([4790ec7](https://github.com/Protelis/Protelis/commit/4790ec77dc49c923d72f4a9730c235588b2d2861))
* **deps:** update plugin multijvmtesting to v1.2.2 ([a01282b](https://github.com/Protelis/Protelis/commit/a01282b79340185597200cf103345e8574ebdfec))

### Documentation

* **deps:** update plugin protelisdoc to v3.0.52 ([4dfad04](https://github.com/Protelis/Protelis/commit/4dfad04337b94010be1f37092bf19fb5ce5151cf))

### Build and continuous integration

* **deps:** update danysk/build-check-deploy-gradle-action action to v3.5.0 ([0f63563](https://github.com/Protelis/Protelis/commit/0f6356305175b5d63076a3ec2198a351487c9f94))

## [17.3.38](https://github.com/Protelis/Protelis/compare/17.3.37...17.3.38) (2024-07-18)

### Dependency updates

* **deps:** update dependency org.apache.commons:commons-lang3 to v3.15.0 ([af28dbe](https://github.com/Protelis/Protelis/commit/af28dbe67d48657bcd02edde62eed32a4d287ecf))
* **deps:** update plugin kotlin-qa to v0.62.4 ([17a0ad8](https://github.com/Protelis/Protelis/commit/17a0ad87cf2af9947a5645f819440b287df0a529))
* **deps:** update plugin kotlin-qa to v0.63.0 ([de70387](https://github.com/Protelis/Protelis/commit/de7038786b302c3b1d82801563ff4ba99d9b38f3))
* **deps:** update plugin publishoncentral to v5.1.4 ([6d7d1ba](https://github.com/Protelis/Protelis/commit/6d7d1ba168db6233554675cc46e1ffcd56a60bbe))

### Documentation

* **deps:** update plugin protelisdoc to v3.0.51 ([3a72a26](https://github.com/Protelis/Protelis/commit/3a72a260432e486e122fb2aa71d6a9d0df9d06bf))

### Build and continuous integration

* **deps:** update danysk/build-check-deploy-gradle-action action to v3.4.0 ([8138f43](https://github.com/Protelis/Protelis/commit/8138f436312398e0755f08bbb8662c66306aa87c))

## [17.3.37](https://github.com/Protelis/Protelis/compare/17.3.36...17.3.37) (2024-07-17)

### Dependency updates

* **deps:** update dependency gradle to v8.9 ([c032c3e](https://github.com/Protelis/Protelis/commit/c032c3e4510efd48fe790fda60f7e982c23849e7))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.91 ([74ed244](https://github.com/Protelis/Protelis/commit/74ed244627de363db743cd5cd6e276ba4b098699))
* **deps:** update plugin java-qa to v1.57.1 ([64d7968](https://github.com/Protelis/Protelis/commit/64d7968c7d51f22e0b49515b0b56687e8e242c97))
* **deps:** update plugin multijvmtesting to v1 ([c90d279](https://github.com/Protelis/Protelis/commit/c90d279b94f327eb8467a4a28a61e0ccb89a731e))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v2.0.8 ([08d9987](https://github.com/Protelis/Protelis/commit/08d9987f3234729dc02a0941a506a947b1f4a2fb))

### Documentation

* **deps:** update plugin protelisdoc to v3.0.50 ([7cac979](https://github.com/Protelis/Protelis/commit/7cac97963bc97fef9753ce4f9547ff40d8874520))

### Build and continuous integration

* **deps:** update danysk/build-check-deploy-gradle-action action to v3.1.1 ([bdac01d](https://github.com/Protelis/Protelis/commit/bdac01d78fd8a6b28fe5c6a72f065971300ce31b))
* **deps:** update danysk/build-check-deploy-gradle-action action to v3.3.0 ([0529976](https://github.com/Protelis/Protelis/commit/0529976af3dd0d9daf2db464e5a2a6e876649ee7))

## [17.3.36](https://github.com/Protelis/Protelis/compare/17.3.35...17.3.36) (2024-07-16)

### Dependency updates

* **deps:** update dependency com.github.spotbugs:spotbugs-annotations to v4.8.6 ([625ecba](https://github.com/Protelis/Protelis/commit/625ecba10c84e9a229338019dce4715a14216d56))
* **deps:** update dependency commons-codec:commons-codec to v1.17.1 ([b451b39](https://github.com/Protelis/Protelis/commit/b451b394dee11ed20e17f0e5df441d8c6fcefa16))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.174 ([6559694](https://github.com/Protelis/Protelis/commit/65596948912336a3a3bff66aa47f58d72bf1427e))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.86 ([aceb096](https://github.com/Protelis/Protelis/commit/aceb096d11e8c852ed4d14a5e596db234e9a4d3a))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.87 ([08fef0c](https://github.com/Protelis/Protelis/commit/08fef0c1200df3588cecbbf666f320f0e573e2bc))
* **deps:** update node.js to 20.15 ([083ceb3](https://github.com/Protelis/Protelis/commit/083ceb35b4ace036c4fe5ac60b38c58b094c2d2d))
* **deps:** update plugin com.gradle.develocity to v3.17.5 ([55df915](https://github.com/Protelis/Protelis/commit/55df91514528b5390f2614ec4e34da0e4bd14b17))
* **deps:** update plugin java-qa to v1.53.0 ([6c15472](https://github.com/Protelis/Protelis/commit/6c15472baedca2f02580b4356db1a7dbd41388ab))
* **deps:** update plugin java-qa to v1.55.0 ([2a9bef4](https://github.com/Protelis/Protelis/commit/2a9bef4905436fcd0e28217f76ec4d774f729637))
* **deps:** update plugin tasktree to v4 ([91d1017](https://github.com/Protelis/Protelis/commit/91d1017f1c527fc93c875f038fa9cca6f0440a4b))

### Documentation

* **deps:** update plugin protelisdoc to v3.0.49 ([925cd18](https://github.com/Protelis/Protelis/commit/925cd180c71baa501145bc51fd4df09e3a926c96))

### Build and continuous integration

* **deps-dev:** bump braces from 3.0.2 to 3.0.3 ([66e1b15](https://github.com/Protelis/Protelis/commit/66e1b154873949adc749569c94a4e3b24341a846))
* **deps:** update actions/checkout action to v4.1.7 ([22082fc](https://github.com/Protelis/Protelis/commit/22082fc86aa670b22f3839119c1ce47c550877f3))
* **deps:** update danysk/action-checkout action to v0.2.19 ([17b9f5d](https://github.com/Protelis/Protelis/commit/17b9f5d3e1d54237f0b80bab7071ef285c665da5))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.4.21 ([df378f6](https://github.com/Protelis/Protelis/commit/df378f6ece2bacc79e4c2b96b4d0eade0dd77bb1))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.4.22 ([525c8dd](https://github.com/Protelis/Protelis/commit/525c8dd959983a68b38c63b340033cc66bd2803c))
* **deps:** update danysk/build-check-deploy-gradle-action action to v3 ([9f3087a](https://github.com/Protelis/Protelis/commit/9f3087aacfe64e1d51ac5f998499a74614ea6624))
* **deps:** update danysk/build-check-deploy-gradle-action action to v3.1.0 ([8b1d797](https://github.com/Protelis/Protelis/commit/8b1d7976204d28f8a60804aabed6988a0bc575e2))
* install the version of node from the package.json ([886c8ae](https://github.com/Protelis/Protelis/commit/886c8ae1aa652832d88c825bdb14d6ec36729848))
* read the Central username from the repository's actions secrets ([35f5ff7](https://github.com/Protelis/Protelis/commit/35f5ff7ee0ab979534afcac687676894de157daa))
* **release:** switch to a ECMA6-compliant semantic release configuration ([ce4dc66](https://github.com/Protelis/Protelis/commit/ce4dc66bf74e01b04d46e22a6c3e08dcfc041902))

## [17.3.35](https://github.com/Protelis/Protelis/compare/17.3.34...17.3.35) (2024-06-06)


### Dependency updates

* **deps:** update dependency io.github.classgraph:classgraph to v4.8.173 ([875f264](https://github.com/Protelis/Protelis/commit/875f2643c4a0da916c7adb75d03fcdefb848889b))
* **deps:** update plugin kotlin-qa to v0.62.3 ([e89f7d5](https://github.com/Protelis/Protelis/commit/e89f7d580d2c1f31d0693c88ed934d76c3cefedd))
* **deps:** update plugin publishoncentral to v5.1.3 ([a99ef47](https://github.com/Protelis/Protelis/commit/a99ef476c8ac3cdd20c587744a624af69dee1331))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.48 ([9beb0f9](https://github.com/Protelis/Protelis/commit/9beb0f920069c92c4c52c93544d2c5e5e737033f))

## [17.3.34](https://github.com/Protelis/Protelis/compare/17.3.33...17.3.34) (2024-06-04)


### Dependency updates

* **deps:** update plugin publishoncentral to v5.1.2 ([c7482a3](https://github.com/Protelis/Protelis/commit/c7482a38cfe4d9f7690f4b339c7ec87800da3846))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.47 ([86e1eec](https://github.com/Protelis/Protelis/commit/86e1eeca42ad6209beb73ad3b6f996638f2fb2cd))

## [17.3.33](https://github.com/Protelis/Protelis/compare/17.3.32...17.3.33) (2024-06-01)


### Documentation

* **deps:** update plugin protelisdoc to v3.0.46 ([bd3757f](https://github.com/Protelis/Protelis/commit/bd3757fa2d458ac5e571317adbb0340752413fd0))

## [17.3.32](https://github.com/Protelis/Protelis/compare/17.3.31...17.3.32) (2024-06-01)


### Dependency updates

* **deps:** update dependency gradle to v8.8 ([8903a88](https://github.com/Protelis/Protelis/commit/8903a886215d78cf9b551c18134b828ee9cbd91f))
* **deps:** update plugin gitsemver to v3.1.7 ([74cf0d4](https://github.com/Protelis/Protelis/commit/74cf0d410b5240d53b433f6e39dadb93e0f1ee07))
* **deps:** update plugin kotlin-qa to v0.62.1 ([4a720a0](https://github.com/Protelis/Protelis/commit/4a720a0273c219c4060f983685e37bbc5868904d))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v2.0.7 ([655d66d](https://github.com/Protelis/Protelis/commit/655d66dec7ed4590f01e6a5af4ab57179235e897))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.45 ([5fc9918](https://github.com/Protelis/Protelis/commit/5fc9918b59d5e42f5b3d53a9f0000bd05c0a8c74))

## [17.3.31](https://github.com/Protelis/Protelis/compare/17.3.30...17.3.31) (2024-05-31)


### Dependency updates

* **core-deps:** update dependency com.google.guava:guava to v33.2.1-jre ([5001573](https://github.com/Protelis/Protelis/commit/500157333766a2ea31460534cc6a41c6a902b3cc))
* **deps:** update dependency org.jetbrains.kotlin.jvm to v2 ([e5b2e62](https://github.com/Protelis/Protelis/commit/e5b2e62a512a560e22a73e2bc5a3a2624de9744c))
* **deps:** update node.js to 20.13 ([e93c6da](https://github.com/Protelis/Protelis/commit/e93c6da840b3dc7abcf3c96e26399f73cac8c59f))
* **deps:** update node.js to 20.14 ([764a8a8](https://github.com/Protelis/Protelis/commit/764a8a83248dd28bbb2a83d4effaabf0e241abe5))
* **deps:** update plugin com.gradle.develocity to v3.17.3 ([964c8e0](https://github.com/Protelis/Protelis/commit/964c8e070a9932db769d3489e5d3cb1fb070a348))
* **deps:** update plugin com.gradle.develocity to v3.17.4 ([44b532f](https://github.com/Protelis/Protelis/commit/44b532fc7b1bf0006d16b9172e9aa6bea52514f2))
* **deps:** update plugin gitsemver to v3.1.6 ([9f2d881](https://github.com/Protelis/Protelis/commit/9f2d88181d1fdf76a3e1b1217fe41de03aa22956))
* **deps:** update plugin java-qa to v1.51.0 ([1f8c286](https://github.com/Protelis/Protelis/commit/1f8c286b9433b25538abc9eb6502084020e095c2))
* **deps:** update plugin java-qa to v1.52.0 ([fbad5f4](https://github.com/Protelis/Protelis/commit/fbad5f420eb43b3d509bfa5887244275109b7aee))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v2.0.6 ([c9b4d37](https://github.com/Protelis/Protelis/commit/c9b4d37dd7aafeca81fc014234e1cd107bf46279))


### Build and continuous integration

* **deps:** update actions/checkout action to v4.1.5 ([34db38b](https://github.com/Protelis/Protelis/commit/34db38bc8b434919dcec8fd6fe15227b3784b957))
* **deps:** update actions/checkout action to v4.1.6 ([2eeb2ad](https://github.com/Protelis/Protelis/commit/2eeb2ad366ec0f940b57217b2b10cea591c27b6e))
* **deps:** update danysk/action-checkout action to v0.2.17 ([8bd3212](https://github.com/Protelis/Protelis/commit/8bd3212279cd648f498f15745de73673f749ac35))
* **deps:** update danysk/action-checkout action to v0.2.18 ([e785761](https://github.com/Protelis/Protelis/commit/e785761575e0ad5d0d538f197374004321717ba3))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.4.19 ([ebfcf17](https://github.com/Protelis/Protelis/commit/ebfcf17752cdfaa0edcd59c11120b10cb6b27b61))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.4.20 ([197c4ca](https://github.com/Protelis/Protelis/commit/197c4caebd4c9440c4664023edf741beb03d8ef9))

## [17.3.30](https://github.com/Protelis/Protelis/compare/17.3.29...17.3.30) (2024-05-07)


### Dependency updates

* **deps:** update plugin kotlin-qa to v0.62.0 ([67cbe39](https://github.com/Protelis/Protelis/commit/67cbe3971afffcc540279e9db7cd9809bca49ccd))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.44 ([7f2fcf0](https://github.com/Protelis/Protelis/commit/7f2fcf07f3b82fe3f10ce3ac71c50f62216b6391))

## [17.3.29](https://github.com/Protelis/Protelis/compare/17.3.28...17.3.29) (2024-05-07)


### Dependency updates

* **deps:** update dependency org.jetbrains.kotlin.jvm to v1.9.24 ([66ad8b2](https://github.com/Protelis/Protelis/commit/66ad8b25c515665a8217249875c35681c3ba53ee))
* **deps:** update plugin gitsemver to v3.1.5 ([ebe2b84](https://github.com/Protelis/Protelis/commit/ebe2b84876bcb1309adbd5f7d2d8985e358299fb))
* **deps:** update plugin kotlin-qa to v0.61.1 ([c71c191](https://github.com/Protelis/Protelis/commit/c71c1919f4099a89e98517913dec4198d127f124))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v2.0.5 ([94b467a](https://github.com/Protelis/Protelis/commit/94b467abb85c5d828e113c6d0ac3fe3a08dc6bc6))
* **deps:** update plugin publishoncentral to v5.1.1 ([e2ee3bf](https://github.com/Protelis/Protelis/commit/e2ee3bf4d05ca25f7f84af42e47a536b9e5f8463))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.43 ([d9877da](https://github.com/Protelis/Protelis/commit/d9877da5c3b1fc9f25fdffb19698f242e7cffb0b))

## [17.3.28](https://github.com/Protelis/Protelis/compare/17.3.27...17.3.28) (2024-05-07)


### Dependency updates

* **deps:** update dependency com.github.spotbugs:spotbugs-annotations to v4.8.5 ([8ca7845](https://github.com/Protelis/Protelis/commit/8ca78456031219e4c37f1364dcea8f286f6f0fa3))
* **deps:** update plugin java-qa to v1.48.0 ([174a9cf](https://github.com/Protelis/Protelis/commit/174a9cf6c4d565c0a0e2a9b0645ba823ac027a95))
* **deps:** update plugin java-qa to v1.49.0 ([8243b86](https://github.com/Protelis/Protelis/commit/8243b863acbd74f2138611501b99dfc6a17604bb))
* **deps:** update plugin java-qa to v1.50.0 ([f6e9844](https://github.com/Protelis/Protelis/commit/f6e984405648e1a3dc22b41c31746182a2a24267))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.42 ([836003c](https://github.com/Protelis/Protelis/commit/836003c015e39cbcc8c5e207a953dac19fbca6bb))


### Build and continuous integration

* **deps:** update danysk/build-check-deploy-gradle-action action to v2.4.18 ([85b8c54](https://github.com/Protelis/Protelis/commit/85b8c54aab36cb4e74085cf8840a744a4b8bc27a))

## [17.3.27](https://github.com/Protelis/Protelis/compare/17.3.26...17.3.27) (2024-05-03)


### Dependency updates

* **core-deps:** update dependency com.google.guava:guava to v33.2.0-jre ([161ed72](https://github.com/Protelis/Protelis/commit/161ed7241e01e26190468f36fc3e24915b3c781e))
* **deps:** update dependency ch.qos.logback:logback-classic to v1.5.5 ([1164145](https://github.com/Protelis/Protelis/commit/1164145c463517d7143cb1ebc9924420ab68624c))
* **deps:** update dependency ch.qos.logback:logback-classic to v1.5.6 ([dfa73fb](https://github.com/Protelis/Protelis/commit/dfa73fb25218792d94813a5364c7307339adb32b))
* **deps:** update dependency commons-codec:commons-codec to v1.17.0 ([1e09d80](https://github.com/Protelis/Protelis/commit/1e09d806fd599faf12290b49b98421e6e2eb5ca3))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.171 ([2bf4015](https://github.com/Protelis/Protelis/commit/2bf4015caab670ed8c8d881e48744b0203fcc9a5))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.172 ([d72df1e](https://github.com/Protelis/Protelis/commit/d72df1e5d7cef1da427a8473b11c0cf09abf1dbd))
* **deps:** update dependency org.slf4j:slf4j-api to v2.0.13 ([5346e50](https://github.com/Protelis/Protelis/commit/5346e509572b133024d64d85dcce372bdca7c937))
* **deps:** update plugin com.gradle.develocity to v3.17.2 ([f9370a3](https://github.com/Protelis/Protelis/commit/f9370a31ea99b0693828faeea7eaecd70f07e3d7))
* **deps:** update plugin java-qa to v1.45.0 ([bd34649](https://github.com/Protelis/Protelis/commit/bd34649d4b88c2e4ba3e8e0187dd701c997628d3))
* **deps:** update plugin java-qa to v1.46.0 ([4784894](https://github.com/Protelis/Protelis/commit/47848941204d9ec8714d583cd7d65e5ab08bc6bc))
* **deps:** update plugin java-qa to v1.46.1 ([7e7cf77](https://github.com/Protelis/Protelis/commit/7e7cf778c7320f368dc4b1f4c0ec046cce5b4de6))
* **deps:** update plugin java-qa to v1.47.0 ([53b0789](https://github.com/Protelis/Protelis/commit/53b07892c571fc304782159e511fe771af4bca12))


### Build and continuous integration

* **deps:** update actions/checkout action to v4.1.3 ([754088c](https://github.com/Protelis/Protelis/commit/754088c86bc9008f1cc4f104d8113211b98d50ec))
* **deps:** update actions/checkout action to v4.1.4 ([00376f9](https://github.com/Protelis/Protelis/commit/00376f99f0db943d8323c11820abaa9c0c2d8285))
* **deps:** update danysk/action-checkout action to v0.2.15 ([b97199c](https://github.com/Protelis/Protelis/commit/b97199c423f71bfb8537ab8d2d7f18ff2f5be8d1))
* **deps:** update danysk/action-checkout action to v0.2.16 ([7496456](https://github.com/Protelis/Protelis/commit/7496456ebec3c79450bfddcbe8c54bee2f7f450a))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.4.13 ([9dd3e47](https://github.com/Protelis/Protelis/commit/9dd3e476b97274817e957f53c4623c6292384ea3))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.4.14 ([d5eaef8](https://github.com/Protelis/Protelis/commit/d5eaef8d37c76ec59966947bb563a52a2b9dc8ec))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.4.15 ([b335a51](https://github.com/Protelis/Protelis/commit/b335a51430bb4c8fb58ae2b762d7cb4becdc7eaa))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.4.16 ([f2a56f2](https://github.com/Protelis/Protelis/commit/f2a56f2f6a78256b33938845f4fcfcfc4a766da7))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.4.17 ([89206d0](https://github.com/Protelis/Protelis/commit/89206d0f9f8a2a5e8db5baa3b11d0c249410db85))

## [17.3.26](https://github.com/Protelis/Protelis/compare/17.3.25...17.3.26) (2024-04-09)


### Dependency updates

* **deps:** update dependency ch.qos.logback:logback-classic to v1.5.4 ([c72ca4e](https://github.com/Protelis/Protelis/commit/c72ca4ed6d6ebe48fb4b48ee04be112df1d9a29f))
* **deps:** update dependency com.github.spotbugs:spotbugs-annotations to v4.8.4 ([1901134](https://github.com/Protelis/Protelis/commit/1901134372b5117be523efb488ecfde60eb7d1e7))
* **deps:** update dependency commons-io:commons-io to v2.16.1 ([290a43c](https://github.com/Protelis/Protelis/commit/290a43c0870c03c501fca053948273e25f9975b6))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.170 ([c6d26d1](https://github.com/Protelis/Protelis/commit/c6d26d1d615f8cb1a549ecb94096e1261df3df7b))
* **deps:** update plugin com.gradle.enterprise to v3.17.1 ([e8479df](https://github.com/Protelis/Protelis/commit/e8479df862cbf785ca668b3c630366dc799ca7d8))
* **deps:** update plugin java-qa to v1.44.0 ([4bc27d2](https://github.com/Protelis/Protelis/commit/4bc27d2d6dd33562037dd70fcb24bf0ba45ca468))
* **deps:** update plugin multijvmtesting to v0.5.8 ([8144cef](https://github.com/Protelis/Protelis/commit/8144cefe9c539135f0c59a5b72c503c798a4baba))
* **deps:** update plugin publishoncentral to v5.1.0 ([d17c20e](https://github.com/Protelis/Protelis/commit/d17c20eec2bc72eba0c2a39b82127d9ebffe5354))


### Bug Fixes

* **interpreter:** modify a potentially dangerous implementation of `Option.empty()` using the enum singleton pattern ([23a0e24](https://github.com/Protelis/Protelis/commit/23a0e24056d48af35da1284f929772be94174e1e))


### Tests

* **test:** ignore a bad implementation of singleton, it is never serialized in tests ([bd9f133](https://github.com/Protelis/Protelis/commit/bd9f1337680c1135cc91c0df794b73645e59f924))


### Build and continuous integration

* **deps:** update danysk/build-check-deploy-gradle-action action to v2.4.12 ([3062233](https://github.com/Protelis/Protelis/commit/3062233278caf5cefdaf7ab88eddba3b329532f9))
* switch from Gradle Enterprise to Develocity ([57fff2e](https://github.com/Protelis/Protelis/commit/57fff2e04e117de7ae0a58214975bfc069ee7be4))


### Style improvements

* **interpreter:** finalize static nested class ([26edc2d](https://github.com/Protelis/Protelis/commit/26edc2d65d5c01a4aa68da05c7539c9fdcc21bbc))
* **interpreter:** make method final ([4a86392](https://github.com/Protelis/Protelis/commit/4a86392719cfff11ec822d113bafce13debecd8d))

## [17.3.25](https://github.com/Protelis/Protelis/compare/17.3.24...17.3.25) (2024-04-03)


### Bug Fixes

* never cancel a release in progress ([4a5c7b8](https://github.com/Protelis/Protelis/commit/4a5c7b8cbc74a7d60d94b61be763fcdc9e57a2da))

## [17.3.24](https://github.com/Protelis/Protelis/compare/17.3.23...17.3.24) (2024-04-03)


### Dependency updates

* **core-deps:** update dependency org.ow2.asm:asm to v9.7 ([c7ef997](https://github.com/Protelis/Protelis/commit/c7ef9970cac51e334a05a3bc626d7348a099b769))
* **deps:** update dependency commons-io:commons-io to v2.16.0 ([666a5d7](https://github.com/Protelis/Protelis/commit/666a5d7cf7d1984295c9f603c38946e2183819dd))
* **deps:** update node.js to 20.12 ([4ecbae6](https://github.com/Protelis/Protelis/commit/4ecbae67f9a20d6f1fd2ce6d7ebe06dd2d197c23))
* **deps:** update plugin com.gradle.enterprise to v3.17 ([459e527](https://github.com/Protelis/Protelis/commit/459e52751cc111e12a1a1049fa44e1278fff6d4b))
* **deps:** update plugin java-qa to v1.42.0 ([838bde9](https://github.com/Protelis/Protelis/commit/838bde90b9bf86a1e03077e78dd27141ccde7952))
* **deps:** update plugin tasktree to v3 ([77e0362](https://github.com/Protelis/Protelis/commit/77e0362d1b14dcf589f4d8d681263a0f990ce71f))


### Build and continuous integration

* add ASM to the core dependencies ([b149f2c](https://github.com/Protelis/Protelis/commit/b149f2cda7f6f15e459fa8aa801530b78e846018))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.4.11 ([a900962](https://github.com/Protelis/Protelis/commit/a9009624de69bdba8433b625eb89063acafef318))
* manually constrain the version of ASM to work around eclipse/xtext[#2686](https://github.com/Protelis/Protelis/issues/2686) and the general problem of Xtext not updating its dependencies frequently ([c78854f](https://github.com/Protelis/Protelis/commit/c78854fbe39058d1317b62b145be6c9a5996477f))

## [17.3.23](https://github.com/Protelis/Protelis/compare/17.3.22...17.3.23) (2024-03-25)


### Dependency updates

* **deps:** update plugin kotlin-qa to v0.61.0 ([33feac3](https://github.com/Protelis/Protelis/commit/33feac333c0945db7b15d1e56ed6c2cfe46a8c8d))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.41 ([f17434e](https://github.com/Protelis/Protelis/commit/f17434e047083e7da82f534fce3388501ff262ab))

## [17.3.22](https://github.com/Protelis/Protelis/compare/17.3.21...17.3.22) (2024-03-23)


### Dependency updates

* **deps:** update dependency gradle to v8.7 ([e077ea2](https://github.com/Protelis/Protelis/commit/e077ea232e9030ef01a4aebb0304c9b59dfc0d83))
* **deps:** update plugin gitsemver to v3.1.4 ([94380bd](https://github.com/Protelis/Protelis/commit/94380bde1d21d1abf8ba20a453b721c8d31e9fe6))
* **deps:** update plugin java-qa to v1.40.0 ([07bc4a4](https://github.com/Protelis/Protelis/commit/07bc4a45651611696f777e90853f413b6a44c268))
* **deps:** update plugin java-qa to v1.41.0 ([57a170c](https://github.com/Protelis/Protelis/commit/57a170c936d9902a8ab75dd5edff43915ccccab8))
* **deps:** update plugin kotlin-qa to v0.60.4 ([9d749aa](https://github.com/Protelis/Protelis/commit/9d749aadb9b6ebd8fdd0244e5b25ce4d898d63f8))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v2.0.4 ([c297312](https://github.com/Protelis/Protelis/commit/c29731264a44b5d5fc64c5928d50d345c14ddce2))
* **deps:** update plugin publishoncentral to v5.0.26 ([4378588](https://github.com/Protelis/Protelis/commit/43785882a616e4e9fb8ced66bf2a9a36054fabd3))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.40 ([a7cab44](https://github.com/Protelis/Protelis/commit/a7cab4483ceed6eeb78afde5d4a56175493db0be))


### Build and continuous integration

* **deps:** update danysk/build-check-deploy-gradle-action action to v2.4.10 ([48f773f](https://github.com/Protelis/Protelis/commit/48f773f97c5d27b53257e946e47bf8a322ce8346))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.4.8 ([79a7b65](https://github.com/Protelis/Protelis/commit/79a7b65c5a6b9cb7770d9bd46afabc9a233e0685))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.4.9 ([301ff2f](https://github.com/Protelis/Protelis/commit/301ff2f68120275b1c867b368c6b30af9778291c))

## [17.3.21](https://github.com/Protelis/Protelis/compare/17.3.20...17.3.21) (2024-03-13)


### Dependency updates

* **core-deps:** update dependency com.google.guava:guava to v33.1.0-jre ([ec71adf](https://github.com/Protelis/Protelis/commit/ec71adf4e08c3ef328d7642a4b5173ead984ea72))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.168 ([80b7186](https://github.com/Protelis/Protelis/commit/80b718642c9a8d0f29366009aaec2872a0cdfb4d))
* **deps:** update plugin java-qa to v1.39.0 ([d9997de](https://github.com/Protelis/Protelis/commit/d9997deb8d781f7a289e5edf32a6f0e5d85f52b2))


### Build and continuous integration

* **deps:** update danysk/build-check-deploy-gradle-action action to v2.4.7 ([e3afdb5](https://github.com/Protelis/Protelis/commit/e3afdb5259f25ef1309efb9fdd17ffc0b10b88cb))

## [17.3.20](https://github.com/Protelis/Protelis/compare/17.3.19...17.3.20) (2024-03-07)


### Dependency updates

* **deps:** update plugin gitsemver to v3.1.3 ([5c41310](https://github.com/Protelis/Protelis/commit/5c4131099f615396167e147aa30ade1bce2315f0))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.38 ([9963755](https://github.com/Protelis/Protelis/commit/99637557bc425090e6a3686f1108b902aa96c785))

## [17.3.19](https://github.com/Protelis/Protelis/compare/17.3.18...17.3.19) (2024-03-07)


### Dependency updates

* **deps:** update dependency io.github.classgraph:classgraph to v4.8.167 ([8a2b7c9](https://github.com/Protelis/Protelis/commit/8a2b7c9ed3e6d3d915ad13dbd811808917c033b6))
* **deps:** update dependency org.jetbrains.kotlin.jvm to v1.9.23 ([8c20c87](https://github.com/Protelis/Protelis/commit/8c20c8703b9d05fcf1e3028222d47897ecb6cd33))
* **deps:** update plugin kotlin-qa to v0.60.3 ([f1c7c9f](https://github.com/Protelis/Protelis/commit/f1c7c9fc5347180732a37df132c065e092d609c1))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v2.0.3 ([bd156fb](https://github.com/Protelis/Protelis/commit/bd156fb5893141417ced29a46553ba4066f212a2))
* **deps:** update plugin publishoncentral to v5.0.25 ([c1f302a](https://github.com/Protelis/Protelis/commit/c1f302aef454df78939df7afc5948931f3c4421e))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.37 ([8e38862](https://github.com/Protelis/Protelis/commit/8e38862f590a1eb8bcf0cbd3b3e17812d99bba47))

## [17.3.18](https://github.com/Protelis/Protelis/compare/17.3.17...17.3.18) (2024-03-04)


### Dependency updates

* **deps:** update plugin kotlin-qa to v0.60.2 ([2c37a09](https://github.com/Protelis/Protelis/commit/2c37a09309cc249a11f227140a4c689cce432436))
* **deps:** update plugin publishoncentral to v5.0.24 ([9c100c7](https://github.com/Protelis/Protelis/commit/9c100c72549c80bd12b1eac225e6f6071e8b7ea3))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.36 ([790d7d2](https://github.com/Protelis/Protelis/commit/790d7d2a8732787a6b314eec9b7aea76ed1ca7d6))

## [17.3.17](https://github.com/Protelis/Protelis/compare/17.3.16...17.3.17) (2024-03-04)


### Dependency updates

* **deps:** update dependency ch.qos.logback:logback-classic to v1.5.0 ([b9a072d](https://github.com/Protelis/Protelis/commit/b9a072da991398ab1b76a39ee17764cc287da15f))
* **deps:** update dependency ch.qos.logback:logback-classic to v1.5.1 ([08497c5](https://github.com/Protelis/Protelis/commit/08497c56a95c99e964e19b0285a96e72ea893598))
* **deps:** update dependency ch.qos.logback:logback-classic to v1.5.2 ([02f900f](https://github.com/Protelis/Protelis/commit/02f900f80c396d01d109211e78d3a712d293b0c2))
* **deps:** update dependency ch.qos.logback:logback-classic to v1.5.3 ([aff7a37](https://github.com/Protelis/Protelis/commit/aff7a37193f1e01da651093e7f4a16ed6a9f46b8))
* **deps:** update plugin gitsemver to v3.1.2 ([876d632](https://github.com/Protelis/Protelis/commit/876d63279e7c2cc58bf4dc3748cfc67ce085a829))
* **deps:** update plugin java-qa to v1.37.0 ([03ff163](https://github.com/Protelis/Protelis/commit/03ff163f66a0bf344cf5a94ae08a79c2ddf972ec))
* **deps:** update plugin java-qa to v1.38.0 ([543a44a](https://github.com/Protelis/Protelis/commit/543a44adede6428b1339660be622f48aad5c2b7f))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v2.0.2 ([53f0329](https://github.com/Protelis/Protelis/commit/53f0329365abf44255cfdc68343df396a98fa230))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.35 ([375b490](https://github.com/Protelis/Protelis/commit/375b490c4edebea6bbd065aad60a51f7935ce986))


### Build and continuous integration

* **deps:** update danysk/build-check-deploy-gradle-action action to v2.4.1 ([bad9d9c](https://github.com/Protelis/Protelis/commit/bad9d9cb6204d30e654d693b4baafde99df8781e))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.4.2 ([83d75d5](https://github.com/Protelis/Protelis/commit/83d75d584449197ca229ab3a75770503fed85f43))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.4.3 ([f1bbc82](https://github.com/Protelis/Protelis/commit/f1bbc8204a9ad34f7ec84cfa20cc55d5a4643bf5))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.4.4 ([bf76f82](https://github.com/Protelis/Protelis/commit/bf76f8209375846687a6c4c4fdb363eb3fa3f4fb))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.4.5 ([b3d0ff1](https://github.com/Protelis/Protelis/commit/b3d0ff16f03fb2e166bb0dece823d5d48d249cde))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.4.6 ([c10067d](https://github.com/Protelis/Protelis/commit/c10067db1ceecca1f2fb25617d275f7257c486fa))

## [17.3.16](https://github.com/Protelis/Protelis/compare/17.3.15...17.3.16) (2024-02-09)


### Documentation

* **deps:** update plugin protelisdoc to v3.0.34 ([99dc490](https://github.com/Protelis/Protelis/commit/99dc4907b427287faad89325ea44f379444ef583))

## [17.3.15](https://github.com/Protelis/Protelis/compare/17.3.14...17.3.15) (2024-02-09)


### Bug Fixes

* **release:** disable publication on surge.sh ([912c1b0](https://github.com/Protelis/Protelis/commit/912c1b0f27ba884172907b0e90f2828cd8908842))

## [17.3.14](https://github.com/Protelis/Protelis/compare/17.3.13...17.3.14) (2024-02-09)


### Bug Fixes

* **release:** do not force annotated tags on release ([0f559e8](https://github.com/Protelis/Protelis/commit/0f559e8180f7f578adf2112381d986ae8525c56f))

## [17.3.13](https://github.com/Protelis/Protelis/compare/17.3.12...17.3.13) (2024-02-09)


### Dependency updates

* **deps:** update dependency commons-codec:commons-codec to v1.16.1 ([2f4274b](https://github.com/Protelis/Protelis/commit/2f4274bfb66416687d08e91dd157b2292982fe16))
* **deps:** update dependency it.unibo.alchemist:alchemist-loading to v30 ([8b25507](https://github.com/Protelis/Protelis/commit/8b255079c60d2bd0c3f81cf35e398f5086a3ef96))
* **deps:** update dependency it.unibo.alchemist:alchemist-loading to v30.1.2 ([47a1cc0](https://github.com/Protelis/Protelis/commit/47a1cc018508d844df53eb41308f9ba57a99af92))
* **deps:** update dependency it.unibo.alchemist:alchemist-loading to v30.1.3 ([15932f1](https://github.com/Protelis/Protelis/commit/15932f1525bd4c0d239da0e58fa67005c59b5785))
* **deps:** update dependency org.slf4j:slf4j-api to v2.0.12 ([d5c51ed](https://github.com/Protelis/Protelis/commit/d5c51ed50e9c8d59e53f4245b02ab5cad39369ab))
* **deps:** update plugin kotlin-qa to v0.60.0 ([5ecb1d7](https://github.com/Protelis/Protelis/commit/5ecb1d75238a5e9e362a22546b6a587ea2fc73e5))
* **deps:** update plugin kotlin-qa to v0.60.1 ([e807694](https://github.com/Protelis/Protelis/commit/e80769411e8e19f934078fd21941305414b669b8))
* **deps:** update plugin multijvmtesting to v0.5.8 ([9aad71a](https://github.com/Protelis/Protelis/commit/9aad71a94da390502a8545f33818e89efb46a9d1))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v2.0.1 ([fabc277](https://github.com/Protelis/Protelis/commit/fabc277904cd96a55b88c69cd3d22840021f5578))


### Revert previous changes

* Revert "chore(deps): update dependency it.unibo.alchemist:alchemist-loading t…" ([238e061](https://github.com/Protelis/Protelis/commit/238e061460ec47150dc5e8fdd7edd25c82ffa83e))
* Revert "chore(deps): update dependency it.unibo.alchemist:alchemist-loading t…" ([442e099](https://github.com/Protelis/Protelis/commit/442e099106f1fbe2db45a69d8f6df76248c274e4))
* Revert "chore(deps): update dependency it.unibo.alchemist:alchemist-loading t…" ([9d09983](https://github.com/Protelis/Protelis/commit/9d09983422009c21e87a6ce6b74a98716cac2327))
* revert broken plugin update ([abc5de3](https://github.com/Protelis/Protelis/commit/abc5de30678f07779b893b27019f54ba15b5dce8))


### Build and continuous integration

* **deps:** update danysk/build-check-deploy-gradle-action action to v2.3.0 ([66b641a](https://github.com/Protelis/Protelis/commit/66b641a4090eb842dbf5b4beb4fe6c27d134a86a))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.3.1 ([f097c55](https://github.com/Protelis/Protelis/commit/f097c55f1fbbcb31f3ac78038b14f06ddb6da87c))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.3.2 ([42dc55d](https://github.com/Protelis/Protelis/commit/42dc55dd04789104341c07afc0cbb77c43755229))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.4.0 ([e8ee307](https://github.com/Protelis/Protelis/commit/e8ee307a914bd4e32ee52892fee8b520efefa64f))
* stop using the broken gradle execution action ([98b0625](https://github.com/Protelis/Protelis/commit/98b0625115b36b840c342900152d9a9789defeeb))

## [17.3.12](https://github.com/Protelis/Protelis/compare/17.3.11...17.3.12) (2024-02-03)


### Dependency updates

* **deps:** update dependency gradle to v8.6 ([0fa6b96](https://github.com/Protelis/Protelis/commit/0fa6b96600ac54271d253f6e2015aeda560f5d79))
* **deps:** update plugin gitsemver to v3.1.1 ([5691764](https://github.com/Protelis/Protelis/commit/569176477eaf1bb3fa89e16610e535f6d5a0e835))
* **deps:** update plugin kotlin-qa to v0.59.1 ([8b54c78](https://github.com/Protelis/Protelis/commit/8b54c7852f3191e6b54acdf17ee8af60a8c2cbed))
* **deps:** update plugin publishoncentral to v5.0.23 ([d247aa0](https://github.com/Protelis/Protelis/commit/d247aa036b478326588e9f3f4ed341b0f5fc098b))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.33 ([11e8f29](https://github.com/Protelis/Protelis/commit/11e8f2973b50f00aeeacf6d8e5600ba070c0eef0))


### Build and continuous integration

* **deps:** update danysk/build-check-deploy-gradle-action action to v2.2.17 ([63ad0fa](https://github.com/Protelis/Protelis/commit/63ad0faab5a275d645c6c52ce16b66a491f82d68))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.2.18 ([c12ed26](https://github.com/Protelis/Protelis/commit/c12ed26a5f2859df0ba8b04ef27198c5509094d2))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.2.19 ([ca8f9b5](https://github.com/Protelis/Protelis/commit/ca8f9b53c1e8459acf6a2e4d24d2344dd349ce5e))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.2.20 ([37a5256](https://github.com/Protelis/Protelis/commit/37a5256f346a2c308277a580213396da14298a97))

## [17.3.11](https://github.com/Protelis/Protelis/compare/17.3.10...17.3.11) (2024-01-31)


### Dependency updates

* **deps:** update plugin com.gradle.enterprise to v3.16.2 ([97b195f](https://github.com/Protelis/Protelis/commit/97b195fd9bf62f7e30c3ee4838951f0ce389aa97))
* **deps:** update plugin gitsemver to v3 ([3bb2007](https://github.com/Protelis/Protelis/commit/3bb2007c6fc47b9252b822b1e6c808d18a5ac22e))
* **deps:** update plugin gitsemver to v3.1.0 ([063e7a0](https://github.com/Protelis/Protelis/commit/063e7a0bed2ea73be05c9454d1c861e7cededed1))
* **deps:** update plugin java-qa to v1.35.0 ([970d633](https://github.com/Protelis/Protelis/commit/970d633199363f6bfc4f935e276264c5e78428b0))
* **deps:** update plugin java-qa to v1.36.0 ([d2c27f7](https://github.com/Protelis/Protelis/commit/d2c27f78c2e11feabe2dacc5e9231d60c572fe9a))
* **deps:** update plugin kotlin-qa to v0.59.0 ([4425343](https://github.com/Protelis/Protelis/commit/4425343f1bf658634619601f5ec35843d843fb68))
* **deps:** update plugin org.gradle.toolchains.foojay-resolver-convention to v0.8.0 ([97a6dd6](https://github.com/Protelis/Protelis/commit/97a6dd664df8cbbc3f782e8e099ea67e84320f0b))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.31 ([2ad3c86](https://github.com/Protelis/Protelis/commit/2ad3c86d7f0e447baf0e7371f069a52851d99a28))


### Build and continuous integration

* **deps:** update danysk/build-check-deploy-gradle-action action to v2.2.13 ([342af9c](https://github.com/Protelis/Protelis/commit/342af9cc258323950b8c4d9f136fda86b342c058))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.2.14 ([5772a13](https://github.com/Protelis/Protelis/commit/5772a13d2b2c18781c70e42ccd557866fdb03ec7))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.2.15 ([1c92c04](https://github.com/Protelis/Protelis/commit/1c92c040ad67a2230639d4ff5f8806430dc91195))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.2.16 ([d936561](https://github.com/Protelis/Protelis/commit/d936561bf71b2da221af9c9fd08fa16882bafca3))

## [17.3.10](https://github.com/Protelis/Protelis/compare/17.3.9...17.3.10) (2024-01-10)


### Dependency updates

* **deps:** update dependency org.slf4j:slf4j-api to v2.0.10 ([7bfedc6](https://github.com/Protelis/Protelis/commit/7bfedc6a67304a2fe34ead32e22262c358a45529))
* **deps:** update dependency org.slf4j:slf4j-api to v2.0.11 ([72c3a3a](https://github.com/Protelis/Protelis/commit/72c3a3a22ed5c0930ecc22bcfbd781c2630f90d3))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.85 ([1cd45b7](https://github.com/Protelis/Protelis/commit/1cd45b7202712357895d6885fbc46f3c74cb8def))
* **deps:** update node.js to 20.11 ([23c25b2](https://github.com/Protelis/Protelis/commit/23c25b26807a67b935fb3cea5bdd58d18edd17c9))
* **deps:** update plugin java-qa to v1.31.1 ([c1967a0](https://github.com/Protelis/Protelis/commit/c1967a0e6c4c7cfd7a5d5bf07a2f327ae7175c23))
* **deps:** update plugin java-qa to v1.32.0 ([d484eee](https://github.com/Protelis/Protelis/commit/d484eee901cb62056ae64485907dccb017010200))
* **deps:** update plugin java-qa to v1.33.0 ([ad630ef](https://github.com/Protelis/Protelis/commit/ad630ef25d00adacd8cb1ecd90ac1d6c0c04c7cb))
* **deps:** update plugin java-qa to v1.34.0 ([b34af60](https://github.com/Protelis/Protelis/commit/b34af60dd47612c065dc2aad04a17258f915eab9))
* **deps:** update plugin kotlin-qa to v0.58.0 ([4df4b17](https://github.com/Protelis/Protelis/commit/4df4b17a69ef4b7a449ae179feb94acfb64a391e))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.1.17 ([0f05d53](https://github.com/Protelis/Protelis/commit/0f05d53c80453cc61fe3f141fe6f55cbde1e9bb0))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v2 ([09ec110](https://github.com/Protelis/Protelis/commit/09ec1101bbd373e5321e68cc782a07557ede4113))
* **deps:** update plugin publishoncentral to v5.0.22 ([757341a](https://github.com/Protelis/Protelis/commit/757341aee42630f4886152002f79b49c2ae8c5fb))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.30 ([1032639](https://github.com/Protelis/Protelis/commit/1032639551b83612304e876caf1da87193b6dc5a))

## [17.3.9](https://github.com/Protelis/Protelis/compare/17.3.8...17.3.9) (2023-12-21)


### Documentation

* **deps:** update plugin protelisdoc to v3.0.29 ([2519c53](https://github.com/Protelis/Protelis/commit/2519c53c519309e8cbc52d83bd69af05cf080885))

## [17.3.8](https://github.com/Protelis/Protelis/compare/17.3.7...17.3.8) (2023-12-21)


### Dependency updates

* **deps:** update dependency org.jetbrains.kotlin.jvm to v1.9.22 ([dc751ab](https://github.com/Protelis/Protelis/commit/dc751ab5b12bf4d2930ad897d31e3c6d319bafe1))
* **deps:** update plugin gitsemver to v2.0.5 ([38f6c64](https://github.com/Protelis/Protelis/commit/38f6c6425be125e5ccc72713dae4de6d7c0f211b))
* **deps:** update plugin java-qa to v1.31.0 ([d7cd3b8](https://github.com/Protelis/Protelis/commit/d7cd3b85e8a973af00334aa919fe65e627119c7e))
* **deps:** update plugin kotlin-qa to v0.57.1 ([b480700](https://github.com/Protelis/Protelis/commit/b48070064a6c0ae7a5cf7ef227103cebb9a37fbc))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.28 ([a85f382](https://github.com/Protelis/Protelis/commit/a85f3823049a703c81b628cb2d89e42770be7b74))

## [17.3.7](https://github.com/Protelis/Protelis/compare/17.3.6...17.3.7) (2023-12-19)


### Dependency updates

* **core-deps:** update dependency com.google.guava:guava to v33 ([690d6a4](https://github.com/Protelis/Protelis/commit/690d6a4a1d70ca0c9f34979b0ed2280f97623fe8))
* **deps:** update dependency com.github.spotbugs:spotbugs-annotations to v4.8.3 ([b0f479c](https://github.com/Protelis/Protelis/commit/b0f479c70d71784b4966248c919d3dd6f8a49a86))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.81 ([09752d6](https://github.com/Protelis/Protelis/commit/09752d62d6b45f7060e1da6f362ec82edfbdca47))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.82 ([5196c3f](https://github.com/Protelis/Protelis/commit/5196c3f556e20a1a5c179c10133d1ccac6f34fd3))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.83 ([7ec58a7](https://github.com/Protelis/Protelis/commit/7ec58a779bd8a378fce9dc74dfc2a8c36cd5a498))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.84 ([ceb6431](https://github.com/Protelis/Protelis/commit/ceb6431fd79b9ebc7081f8fbf3885a6df005193b))
* **deps:** update plugin com.gradle.enterprise to v3.16.1 ([1120037](https://github.com/Protelis/Protelis/commit/11200372772cb669c2de5ffd9fc153c39e093b44))
* **deps:** update plugin java-qa to v1.29.0 ([c0d462b](https://github.com/Protelis/Protelis/commit/c0d462b00471e5ff850841af97f41b34bb094831))

## [17.3.6](https://github.com/Protelis/Protelis/compare/17.3.5...17.3.6) (2023-12-11)


### Dependency updates

* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.79 ([64791cb](https://github.com/Protelis/Protelis/commit/64791cbb12338a4113a18be9036da17222a113f5))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.80 ([69ca7f5](https://github.com/Protelis/Protelis/commit/69ca7f57d64eaccd103c6ef38da471e686351f71))
* **deps:** update plugin com.gradle.enterprise to v3.16 ([93a9690](https://github.com/Protelis/Protelis/commit/93a9690dcd77f5c93a04195841da42448434e814))
* **deps:** update plugin kotlin-qa to v0.57.0 ([3f152b6](https://github.com/Protelis/Protelis/commit/3f152b6de26e6d9d3586c1a12b1edd1eef26f999))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.27 ([13e60db](https://github.com/Protelis/Protelis/commit/13e60dbac1e85b4b1c8ab37b1dbf42bca25c633e))

## [17.3.5](https://github.com/Protelis/Protelis/compare/17.3.4...17.3.5) (2023-12-02)


### Dependency updates

* **deps:** update dependency ch.qos.logback:logback-classic to v1.4.14 ([4096e2d](https://github.com/Protelis/Protelis/commit/4096e2ddf9aedc32a3dd32494939181463b5ec24))
* **deps:** update plugin java-qa to v1.28.0 ([e3b3a2c](https://github.com/Protelis/Protelis/commit/e3b3a2c996202c37e1a78b5c2e228324f58cc226))
* **deps:** update plugin kotlin-qa to v0.56.0 ([2b5a806](https://github.com/Protelis/Protelis/commit/2b5a8064c0b34a4a552ec46fa1719d99a009a3d3))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.26 ([5b3b54d](https://github.com/Protelis/Protelis/commit/5b3b54d75e59e0c16d24100ca6e11ae69cdef60d))

## [17.3.4](https://github.com/Protelis/Protelis/compare/17.3.3...17.3.4) (2023-11-30)


### Dependency updates

* **deps:** update dependency ch.qos.logback:logback-classic to v1.4.12 ([d5e7fbd](https://github.com/Protelis/Protelis/commit/d5e7fbd1a2c704da9b5c4c6178b9b3c6554109cd))
* **deps:** update dependency ch.qos.logback:logback-classic to v1.4.13 ([7c9d5a0](https://github.com/Protelis/Protelis/commit/7c9d5a03c88f1a01ccd142e65edd01514a38e1de))
* **deps:** update dependency com.github.spotbugs:spotbugs-annotations to v4.8.2 ([4d8b8ad](https://github.com/Protelis/Protelis/commit/4d8b8ad272f324dcb5a367c4d18037d91469b199))
* **deps:** update dependency commons-io:commons-io to v2.15.1 ([221fcba](https://github.com/Protelis/Protelis/commit/221fcba14ecd7ab60f05dfcf8264c391731dc0dd))
* **deps:** update dependency gradle to v8.5 ([ef1be0a](https://github.com/Protelis/Protelis/commit/ef1be0a14cfbdcb12285f5648ac3b421d3b1897d))
* **deps:** update plugin gitsemver to v2.0.4 ([2fbdb3e](https://github.com/Protelis/Protelis/commit/2fbdb3ee4c4972592a893476ceb082785cacfdc9))
* **deps:** update plugin java-qa to v1.26.0 ([f791f91](https://github.com/Protelis/Protelis/commit/f791f913145cc684d923d8943d5c5f004cc67b85))
* **deps:** update plugin java-qa to v1.27.0 ([59e5c88](https://github.com/Protelis/Protelis/commit/59e5c88ab0e2a1860d9ae511a5d3ddea35e85525))
* **deps:** update plugin kotlin-qa to v0.55.2 ([a805c59](https://github.com/Protelis/Protelis/commit/a805c59cac8ca767fd8c45544f9783306f4077db))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.1.16 ([f3db20c](https://github.com/Protelis/Protelis/commit/f3db20c5b2357cbcdd1b80f394b1c0e0d9861eb2))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.25 ([5f9f2e3](https://github.com/Protelis/Protelis/commit/5f9f2e35309c7881e378cee3082c7ccc19479198))


### Build and continuous integration

* **deps:** update danysk/build-check-deploy-gradle-action action to v2.2.12 ([706af12](https://github.com/Protelis/Protelis/commit/706af12d32f7878e839160e8fa2bcad7ec2f8048))

## [17.3.3](https://github.com/Protelis/Protelis/compare/17.3.2...17.3.3) (2023-11-26)


### Dependency updates

* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.78 ([cd59e58](https://github.com/Protelis/Protelis/commit/cd59e581d2162bb20a1f990de81ed0d3f26cca0c))
* **deps:** update plugin kotlin-qa to v0.55.1 ([7ba6dc2](https://github.com/Protelis/Protelis/commit/7ba6dc216fea1000fcc25f177074b3219745cc4c))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.23 ([d7d61f4](https://github.com/Protelis/Protelis/commit/d7d61f45e1fe431bc7424781b4501d9025808d64))

## [17.3.2](https://github.com/Protelis/Protelis/compare/17.3.1...17.3.2) (2023-11-23)


### Dependency updates

* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.1.15 ([196929a](https://github.com/Protelis/Protelis/commit/196929a8c1c8588113abf10f55e7857508d34528))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.22 ([b86af91](https://github.com/Protelis/Protelis/commit/b86af91468801080058171a917925e0ec2d587b9))

## [17.3.1](https://github.com/Protelis/Protelis/compare/17.3.0...17.3.1) (2023-11-23)


### Dependency updates

* **deps:** update dependency org.apache.commons:commons-lang3 to v3.14.0 ([a738e27](https://github.com/Protelis/Protelis/commit/a738e275f2ca0ab58e647f1fbd20d1e513325f36))
* **deps:** update dependency org.jetbrains.kotlin.jvm to v1.9.21 ([5a7c5c8](https://github.com/Protelis/Protelis/commit/5a7c5c8bd893b3f167ce59119e4ab1eb011deb92))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.77 ([cef910f](https://github.com/Protelis/Protelis/commit/cef910facb4648df9254dfe9c57318d67291c83e))
* **deps:** update node.js to 20.10 ([1ff7c85](https://github.com/Protelis/Protelis/commit/1ff7c850023545bd408abe1efb12202fb8660073))
* **deps:** update plugin gitsemver to v2.0.3 ([4e354b2](https://github.com/Protelis/Protelis/commit/4e354b2edc4b6f9160e1936bf045b26960270952))
* **deps:** update plugin kotlin-qa to v0.54.1 ([b3582fd](https://github.com/Protelis/Protelis/commit/b3582fd5bd06e15388e74ff3777d430015aee08e))
* **deps:** update plugin publishoncentral to v5.0.20 ([027a385](https://github.com/Protelis/Protelis/commit/027a38503f20e2007277fed6f098bbe1dae930ba))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.21 ([4446169](https://github.com/Protelis/Protelis/commit/4446169114e16479a906ab252a0925ac3af4003c))

## [17.3.0](https://github.com/Protelis/Protelis/compare/17.2.3...17.3.0) (2023-11-21)


### Dependency updates

* **api-deps:** update dependency org.protelis:protelis.parser to v11.0.3 ([6be8f81](https://github.com/Protelis/Protelis/commit/6be8f8157e36f0c32eef73540cafb45d04cf29c9))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.165 ([47a4030](https://github.com/Protelis/Protelis/commit/47a4030ff7b28260e364e7faf2dfbe8aae7e1630))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.72 ([1bb27bd](https://github.com/Protelis/Protelis/commit/1bb27bd0e94dbc14b352479bc5bc73143743540a))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.73 ([521c34b](https://github.com/Protelis/Protelis/commit/521c34bf5e95423f39a25dcdd4f05dd3e61a5535))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.74 ([8c535ba](https://github.com/Protelis/Protelis/commit/8c535ba5ccecde8de738cdfd155a16abb96d4c3a))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.75 ([67fa702](https://github.com/Protelis/Protelis/commit/67fa7027ed8093cc763b0e50714600c803b1c5ae))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.76 ([f869a3f](https://github.com/Protelis/Protelis/commit/f869a3f7f568c9359229b98267346495f5bc750b))
* **deps:** update plugin gitsemver to v2.0.2 ([df232d0](https://github.com/Protelis/Protelis/commit/df232d05c845f482097a9ed0e55cec82f488fb55))
* **deps:** update plugin java-qa to v1.24.1 ([b1e0de2](https://github.com/Protelis/Protelis/commit/b1e0de223e5820c9fc521ffa92392be13c5d4f6a))
* **deps:** update plugin java-qa to v1.25.0 ([3addd30](https://github.com/Protelis/Protelis/commit/3addd30801d4d9a69e8ed90a9cf7cfaf638ec7bd))
* **deps:** update plugin publishoncentral to v5.0.19 ([e991063](https://github.com/Protelis/Protelis/commit/e991063a9b7790acb8d61cb2b51610f2759ed40e))

## [17.2.3](https://github.com/Protelis/Protelis/compare/17.2.2...17.2.3) (2023-11-07)


### Documentation

* **deps:** update plugin protelisdoc to v3.0.20 ([efe4026](https://github.com/Protelis/Protelis/commit/efe4026fb411011087f13b990493d90805f771d0))

## [17.2.2](https://github.com/Protelis/Protelis/compare/17.2.1...17.2.2) (2023-11-07)


### Dependency updates

* **deps:** update dependency com.github.spotbugs:spotbugs-annotations to v4.8.1 ([f3c8115](https://github.com/Protelis/Protelis/commit/f3c81157d3316c8720c7ae3439ca5f81dee426aa))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.164 ([21c201f](https://github.com/Protelis/Protelis/commit/21c201fd486078f929523d642a0a18447f6eba76))
* **deps:** update dependency org.jetbrains.kotlin.jvm to v1.9.20 ([4e46b70](https://github.com/Protelis/Protelis/commit/4e46b70cf25aab1b1d1ba25877226b2f87f19810))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.71 ([aed275a](https://github.com/Protelis/Protelis/commit/aed275add478efd52ecb159173bd8eb574b36ba1))
* **deps:** update plugin java-qa to v1.23.0 ([818e4ed](https://github.com/Protelis/Protelis/commit/818e4edae9c3fcf242d420163e4686767813b4a6))
* **deps:** update plugin java-qa to v1.24.0 ([c3d1fe7](https://github.com/Protelis/Protelis/commit/c3d1fe7a707f99ac294ca52f602a9e343135d33a))
* **deps:** update plugin kotlin-qa to v0.54.0 ([410784d](https://github.com/Protelis/Protelis/commit/410784d02f058d165788c7f77a6dbe310910f943))
* **deps:** update plugin publishoncentral to v5.0.18 ([7ec4962](https://github.com/Protelis/Protelis/commit/7ec496294005dd4826e621b1562e637daf2b5117))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.20-dev02-592cb93 ([#1152](https://github.com/Protelis/Protelis/issues/1152)) ([dd993f0](https://github.com/Protelis/Protelis/commit/dd993f09570b37bcff3629b295af329ff0f1c3c6))


### Build and continuous integration

* **deps:** update danysk/build-check-deploy-gradle-action action to v2.2.11 ([976c3a9](https://github.com/Protelis/Protelis/commit/976c3a9d7f0f35505d316d23e341cd8ecd7a5f97))
* disable the fail-fast strategy for matrix jobs ([#1158](https://github.com/Protelis/Protelis/issues/1158)) ([1374c8f](https://github.com/Protelis/Protelis/commit/1374c8f78011e46cdb5f851cdc66c31a600451dc))
* remove deprecated call to `rootProject.buildDir` ([8688d09](https://github.com/Protelis/Protelis/commit/8688d095460610c3f1ed922bb186b49337dd868d))

## [17.2.1](https://github.com/Protelis/Protelis/compare/17.2.0...17.2.1) (2023-11-01)


### Dependency updates

* **deps:** update dependency commons-io:commons-io to v2.15.0 ([d865c46](https://github.com/Protelis/Protelis/commit/d865c46cfeb7d954c03b0647efd8b51d51c5536c))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.69 ([926f017](https://github.com/Protelis/Protelis/commit/926f0174f368b2cb24331dd9035d256adb01d70c))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.70 ([7ecef14](https://github.com/Protelis/Protelis/commit/7ecef140766180e7b0a9b2439b86745ee7b24dab))
* **deps:** update node.js to 20.9 ([ef983c0](https://github.com/Protelis/Protelis/commit/ef983c0eb7c8de08e6a64f31d95287eeebca4fb0))
* **deps:** update node.js to v20 ([7dc5572](https://github.com/Protelis/Protelis/commit/7dc5572cdfb897f00b84880af5b96f2f35695c57))
* **deps:** update plugin gitsemver to v2 ([a052a83](https://github.com/Protelis/Protelis/commit/a052a83190cee8e03464729c45157d8f969ceb92))
* **deps:** update plugin gitsemver to v2.0.1 ([a48a139](https://github.com/Protelis/Protelis/commit/a48a1397d07e495dbb22673261703489a8a82776))
* **deps:** update plugin java-qa to v1.22.0 ([5f0423a](https://github.com/Protelis/Protelis/commit/5f0423a2295c3d09b7ef73b1b82cd30ad210195d))
* **deps:** update plugin java-qa to v1.22.1 ([9cf5757](https://github.com/Protelis/Protelis/commit/9cf575724d770d887ba7dceeb6316b09b0183058))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.1.13 ([936785b](https://github.com/Protelis/Protelis/commit/936785b95f77aa4ace0eb7c2224a8ea8bd27fd0d))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.1.14 ([28e9907](https://github.com/Protelis/Protelis/commit/28e9907f93f23268c72f72d92e51a2d5b39404ec))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.15 ([a671818](https://github.com/Protelis/Protelis/commit/a67181864958adf2030113407ca1a1a635688bf1))


### Build and continuous integration

* **deps:** update danysk/action-checkout action to v0.2.14 ([e26eb97](https://github.com/Protelis/Protelis/commit/e26eb976059a430b184a2a28ff72e9d5c4f74853))

## [17.2.0](https://github.com/Protelis/Protelis/compare/17.1.2...17.2.0) (2023-10-20)


### Dependency updates

* **api-deps:** update dependency org.protelis:protelis.parser to v11.0.2 ([065c671](https://github.com/Protelis/Protelis/commit/065c6719c0b606a1d6031ecb5b66fa64f7dfae88))
* **deps:** update dependency com.github.spotbugs:spotbugs-annotations to v4.8.0 ([0957fc9](https://github.com/Protelis/Protelis/commit/0957fc9f2242caf8b4b24f3ec88b000207512e46))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.163 ([310f103](https://github.com/Protelis/Protelis/commit/310f1032f04b118677f49ef672fa32cc19882862))
* **deps:** update plugin gitsemver to v1.1.11 ([8d7aa12](https://github.com/Protelis/Protelis/commit/8d7aa12d861e5ef7b7d2d50f78ee38109c9ae91f))
* **deps:** update plugin gitsemver to v1.1.12 ([565e251](https://github.com/Protelis/Protelis/commit/565e2511f0e8e2f4e64e9715f43cf9e0d7e18056))
* **deps:** update plugin gitsemver to v1.1.15 ([6981a1a](https://github.com/Protelis/Protelis/commit/6981a1ad9ee3d3b15861692458197ab2bea022c9))
* **deps:** update plugin java-qa to v1.20.0 ([d320bc7](https://github.com/Protelis/Protelis/commit/d320bc7b62284b4136639175211baa1d4eee6fae))
* **deps:** update plugin java-qa to v1.21.0 ([72f5382](https://github.com/Protelis/Protelis/commit/72f53824c716558fa9d279fc40fbfdf7b77e8b89))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.1.11 ([2864857](https://github.com/Protelis/Protelis/commit/286485791f2a1461e4b63e11154743ec43fea067))


### Build and continuous integration

* **deps:** update actions/checkout action to v4.1.1 ([56e993f](https://github.com/Protelis/Protelis/commit/56e993f3452aa68a9a0d48d58551ab3b956ccff1))

## [17.1.2](https://github.com/Protelis/Protelis/compare/17.1.1...17.1.2) (2023-10-10)


### Dependency updates

* **core-deps:** update dependency com.google.guava:guava to v32.1.3-jre ([9079fa2](https://github.com/Protelis/Protelis/commit/9079fa2890dc36b72f4c761c76f3c20b507ce945))
* **deps:** update dependency ch.qos.logback:logback-classic to v1.4.10 ([35e15fd](https://github.com/Protelis/Protelis/commit/35e15fd0b9e289fff6f63a7d9a951a737acb3c6a))
* **deps:** update dependency ch.qos.logback:logback-classic to v1.4.11 ([8e2c51a](https://github.com/Protelis/Protelis/commit/8e2c51aeb99a20c56821b3a701d0b2934af0d5cc))
* **deps:** update dependency ch.qos.logback:logback-classic to v1.4.9 ([d12a770](https://github.com/Protelis/Protelis/commit/d12a770e4eb9a527d8628a8a215c3b19c17ea85d))
* **deps:** update dependency commons-io:commons-io to v2.14.0 ([c4bfce5](https://github.com/Protelis/Protelis/commit/c4bfce547c3c390ca96ae929646fba7e81dd43dd))
* **deps:** update dependency gradle to v8.3 ([2aa1268](https://github.com/Protelis/Protelis/commit/2aa12687ea428651c6dd38b1a43704bae52d22e1))
* **deps:** update dependency gradle to v8.4 ([f1a20d8](https://github.com/Protelis/Protelis/commit/f1a20d8c66929c20a4ecdb85e13739b8e0588254))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.162 ([7ab38e0](https://github.com/Protelis/Protelis/commit/7ab38e033ed8f947f44219147017569d33a45fe7))
* **deps:** update dependency org.slf4j:slf4j-api to v2.0.9 ([01ef3f2](https://github.com/Protelis/Protelis/commit/01ef3f2e31c4a5e0aaf48d84f003c8c7aa7ab476))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.44 ([4150179](https://github.com/Protelis/Protelis/commit/415017968bad85361ab5e63eab9ba70a83db0800))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.45 ([9463666](https://github.com/Protelis/Protelis/commit/946366604536b6a6146fe5d11c04220c7c6dd28e))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.46 ([5ae269a](https://github.com/Protelis/Protelis/commit/5ae269ab6d9006caea368f5b9faefeaca2b9c701))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.47 ([8e9ca64](https://github.com/Protelis/Protelis/commit/8e9ca646cf2760ccea6b70f2cd1544382fe86d4b))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.48 ([6d58d28](https://github.com/Protelis/Protelis/commit/6d58d2810e816a9c9151221448c804793fcfb6ae))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.49 ([2de51d6](https://github.com/Protelis/Protelis/commit/2de51d621d272cc27f66f1b73efb7e80a2e8636a))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.50 ([e0d0aa1](https://github.com/Protelis/Protelis/commit/e0d0aa10818d64bf33e91e4b44bf26f042052855))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.51 ([f3da6f3](https://github.com/Protelis/Protelis/commit/f3da6f30a676139945b1a6a401a097d27bdd6c38))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.53 ([8202ab0](https://github.com/Protelis/Protelis/commit/8202ab08b1e2e309d15da4e82823c6a5dccbe988))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.54 ([57af673](https://github.com/Protelis/Protelis/commit/57af673407d10f06d62e07a053b1ccd82c26397d))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.55 ([52575c1](https://github.com/Protelis/Protelis/commit/52575c1009c75aeed90194b5255b396e48aebcbc))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.56 ([86da95f](https://github.com/Protelis/Protelis/commit/86da95f6b912b7c9a576ae89855f8c3ba85a828d))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.58 ([759f8de](https://github.com/Protelis/Protelis/commit/759f8def3143f822ba0600d9b15ca695194b4db8))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.59 ([a87f1fe](https://github.com/Protelis/Protelis/commit/a87f1fead84e6ffba8717e7a4a824c950656b074))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.60 ([87ab129](https://github.com/Protelis/Protelis/commit/87ab1291c1f2c5e041d04b036f7071bb613e450f))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.61 ([4ee16f4](https://github.com/Protelis/Protelis/commit/4ee16f47d7ddb076ed97f1a78027dea9473029b9))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.62 ([b4e9057](https://github.com/Protelis/Protelis/commit/b4e9057be87ef2d335ef6fd972a13c0f9be2fe50))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.63 ([a8515a7](https://github.com/Protelis/Protelis/commit/a8515a77e71a0ad1fc642d3f43f8d4c4a31e3bb5))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.64 ([7b3e34e](https://github.com/Protelis/Protelis/commit/7b3e34ef7ee09cb7c8cc3ab7aa9f68a1a0c45615))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.65 ([6800cf5](https://github.com/Protelis/Protelis/commit/6800cf5b8b7bac2df9e9df7c3e518c99ce95754e))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.66 ([a440119](https://github.com/Protelis/Protelis/commit/a440119b65edb023d923433346f56727d0e9b7f1))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.67 ([7b4f555](https://github.com/Protelis/Protelis/commit/7b4f5557b0fd73d8e53ffd684bcda0ddad90fc21))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.68 ([2cbb89c](https://github.com/Protelis/Protelis/commit/2cbb89c9e9e1c44df4c70d710fe862f6add4f653))
* **deps:** update node.js to 18.18 ([3f09d4b](https://github.com/Protelis/Protelis/commit/3f09d4bc0d6bd4b302ba9622bbcaef690d26d6d9))
* **deps:** update plugin com.gradle.enterprise to v3.15 ([b7088f4](https://github.com/Protelis/Protelis/commit/b7088f4255f229b3ef756777c7eb90778909192b))
* **deps:** update plugin com.gradle.enterprise to v3.15.1 ([79be7f9](https://github.com/Protelis/Protelis/commit/79be7f98872756ef91a7f7491be2ef7dce8c8e26))
* **deps:** update plugin java-qa to v1.13.0 ([42b000b](https://github.com/Protelis/Protelis/commit/42b000bd24f6438a7f9e0e95d93bd8f8b369a8db))
* **deps:** update plugin java-qa to v1.14.0 ([a5204a5](https://github.com/Protelis/Protelis/commit/a5204a5e185ed5c052b40b367b848e5bfde4719d))
* **deps:** update plugin java-qa to v1.15.0 ([5ea84b8](https://github.com/Protelis/Protelis/commit/5ea84b8ab4ff42df2d78c25e46bfa1fdd8936b21))
* **deps:** update plugin java-qa to v1.16.0 ([81f8e50](https://github.com/Protelis/Protelis/commit/81f8e500b50c7a452b99e40741bae029650bfa0f))
* **deps:** update plugin java-qa to v1.17.0 ([93d02a4](https://github.com/Protelis/Protelis/commit/93d02a40d4efbdb182757e5e1354d5db0a9f5923))
* **deps:** update plugin java-qa to v1.18.0 ([17fb8c9](https://github.com/Protelis/Protelis/commit/17fb8c9debcce8da3a2c26a6117333b8f19cd408))
* **deps:** update plugin java-qa to v1.19.0 ([9d6a4e5](https://github.com/Protelis/Protelis/commit/9d6a4e5077a9ef2a4387368920f5018b91b5cc47))
* **deps:** update plugin multijvmtesting to v0.5.5 ([42fa80b](https://github.com/Protelis/Protelis/commit/42fa80b5827c41eb93f9e87d518e08494ee7f3ac))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.1.10 ([bf91bde](https://github.com/Protelis/Protelis/commit/bf91bde4f296910827d523d566b514acadd5e34e))
* **deps:** update plugin org.gradle.toolchains.foojay-resolver-convention to v0.7.0 ([d10bc01](https://github.com/Protelis/Protelis/commit/d10bc01dd3000fde54073be16f5029d8852a44c7))


### Build and continuous integration

* **deps:** update actions/checkout action to v3.6.0 ([1c49cf5](https://github.com/Protelis/Protelis/commit/1c49cf5a1df0e6a10f97100bde5f414db43540f7))
* **deps:** update actions/checkout action to v4 ([4a40fb4](https://github.com/Protelis/Protelis/commit/4a40fb4bcb97c966388d23ae66ace63772dde6a4))
* **deps:** update actions/checkout action to v4.1.0 ([bc6537c](https://github.com/Protelis/Protelis/commit/bc6537c5c42a194ce7a71dfea9a6a6de31e6f9c1))
* **deps:** update danysk/action-checkout action to v0.2.11 ([94288d3](https://github.com/Protelis/Protelis/commit/94288d33bacb4c215590090869b8a14cf8fd5256))
* **deps:** update danysk/action-checkout action to v0.2.12 ([91997a6](https://github.com/Protelis/Protelis/commit/91997a6a9e01a0bc99d92841914f0e595f295b03))
* **deps:** update danysk/action-checkout action to v0.2.13 ([22ebbde](https://github.com/Protelis/Protelis/commit/22ebbdecd065118f29ddbc0845883a172ba2b859))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.2.10 ([5c901f1](https://github.com/Protelis/Protelis/commit/5c901f184a1e6f141727d8ad58991204a5c958a6))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.2.9 ([249c199](https://github.com/Protelis/Protelis/commit/249c1990587b10acc0e9767f897ad00117749d48))

## [17.1.1](https://github.com/Protelis/Protelis/compare/17.1.0...17.1.1) (2023-07-31)


### Dependency updates

* **core-deps:** update dependency com.google.guava:guava to v32.1.2-jre ([339ab61](https://github.com/Protelis/Protelis/commit/339ab61d9c9b60c86067b51c641f12bba4a7eb32))
* **deps:** update plugin java-qa to v1.12.0 ([150cf9c](https://github.com/Protelis/Protelis/commit/150cf9cf006c8139ca2f125c3b5b6c5c5fef88fd))

## [17.1.0](https://github.com/Protelis/Protelis/compare/17.0.4...17.1.0) (2023-07-31)


### Features

* **interpreter:** provide a builtin method to convert fields to Java maps ([d752a81](https://github.com/Protelis/Protelis/commit/d752a8102d61e1fa3f2bd2f4eb5c84d5c0418279))
* **interpreter:** publicly expose the min function ([5645ddd](https://github.com/Protelis/Protelis/commit/5645dddf7b50ec49e245a74f8633864780a560eb))
* **lang:** implement and test Bounded Election: https://doi.org/10.1109/ACSOS55765.2022.00026 ([4084f61](https://github.com/Protelis/Protelis/commit/4084f6191caf53e8381f91b7720fff3334822d59))


### Bug Fixes

* **test:** remove never-thrown exception ([08007ce](https://github.com/Protelis/Protelis/commit/08007ced57321cdcdcd4c44806616aae25c80bd4))


### Build and continuous integration

* **deps:** update danysk/build-check-deploy-gradle-action action to v2.2.7 ([632675a](https://github.com/Protelis/Protelis/commit/632675a054604db56a54b3759598b870358ebf2e))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.2.8 ([fc9f133](https://github.com/Protelis/Protelis/commit/fc9f1338dcd46e92717a436fbd97c1e41508aede))


### Dependency updates

* **deps:** update dependency gradle to v8.2.1 ([7f1b294](https://github.com/Protelis/Protelis/commit/7f1b2941b43f7468f2f29a9a1e732582b369d680))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.161 ([de5a735](https://github.com/Protelis/Protelis/commit/de5a735588109d0f8d28c6ef3cb614df643ceed8))
* **deps:** update dependency org.apache.commons:commons-lang3 to v3.13.0 ([cb5ac07](https://github.com/Protelis/Protelis/commit/cb5ac07259507b91e4ca553ac6b862a8243cc62c))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.39 ([8b49860](https://github.com/Protelis/Protelis/commit/8b49860143ae01771a9844754346a7d86a78df81))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.40 ([207e4ed](https://github.com/Protelis/Protelis/commit/207e4ed6c9f88043a9d686ae32e2e7fa865e0f7b))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.41 ([8ca8f5e](https://github.com/Protelis/Protelis/commit/8ca8f5ed21f50d4d051b34f7f6c5ad3ce724cf0b))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.42 ([92682e0](https://github.com/Protelis/Protelis/commit/92682e0afb7f8d26d51534f6d8d5caec57a77fde))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.43 ([d459505](https://github.com/Protelis/Protelis/commit/d4595059e14b93abed268ed0eeb490201a4fc8db))
* **deps:** update node.js to 18.17 ([35a18e4](https://github.com/Protelis/Protelis/commit/35a18e437bd03e133be9b5c61d59e8cd27bfc638))
* **deps:** update plugin com.gradle.enterprise to v3.14 ([3a3e50d](https://github.com/Protelis/Protelis/commit/3a3e50da146dc6d284ad6dd3ebdb504ded631092))
* **deps:** update plugin com.gradle.enterprise to v3.14.1 ([c3e179a](https://github.com/Protelis/Protelis/commit/c3e179acf1fd9d400fe8acfb3440ebe4dd123445))
* **deps:** update plugin multijvmtesting to v0.5.3 ([94176e7](https://github.com/Protelis/Protelis/commit/94176e77392956fa001b5bc9f889fb85ba9f131c))
* **deps:** update plugin multijvmtesting to v0.5.4 ([cf95c7b](https://github.com/Protelis/Protelis/commit/cf95c7b3c7defa65da33a1944dea04fb31dd2865))
* **deps:** update plugin org.gradle.toolchains.foojay-resolver-convention to v0.6.0 ([104c83d](https://github.com/Protelis/Protelis/commit/104c83d954fe9dfeaba6bc17e28689216be59352))
* **deps:** update plugin publishoncentral to v5.0.10 ([ac9f60a](https://github.com/Protelis/Protelis/commit/ac9f60a12bc438d46805d93f596ae5623ebcc255))
* **deps:** update plugin publishoncentral to v5.0.8 ([97fc83c](https://github.com/Protelis/Protelis/commit/97fc83ce4261192282f971a9ca795c43ad72e012))
* **deps:** update plugin publishoncentral to v5.0.9 ([6a19e80](https://github.com/Protelis/Protelis/commit/6a19e806aa28aaa46bff51da3be7693e032fe9a3))


### Tests

* improve error message ([0f2c357](https://github.com/Protelis/Protelis/commit/0f2c3571dfa42335468ca8588585b04b484bc387))
* improve error messages when simulations fail by printing types ([80fd743](https://github.com/Protelis/Protelis/commit/80fd743c0fca74149409bee737b23d37a0b14405))


### Style improvements

* **test:** explicitly check for non-null result of resource loading ([37eecbd](https://github.com/Protelis/Protelis/commit/37eecbdce1f80af985b4a699ac3098649f6706a6))
* **test:** explicitly check for non-null values when loading files ([90b2aea](https://github.com/Protelis/Protelis/commit/90b2aeae005f77b2ebfbafffad36788cbb0c9710))
* **test:** improve vertical spacing ([54f5de3](https://github.com/Protelis/Protelis/commit/54f5de39c0b3e78b366ce0fdf5658e90cbfefaa0))
* **test:** prefer the diamond operator ([37c16a0](https://github.com/Protelis/Protelis/commit/37c16a055189f354d077b5b851f2d424cefc5733))
* **test:** remove redundant escapes in regexes ([7c36f33](https://github.com/Protelis/Protelis/commit/7c36f335a84ddca0f99d9de500c90314bced7df2))
* **test:** remove unnecessary call to toString ([12f950a](https://github.com/Protelis/Protelis/commit/12f950a2ea3349394557b3551693893e4f802eb8))


### Documentation

* **lang:** fix boundedElection parameter documentation ([5ff57a8](https://github.com/Protelis/Protelis/commit/5ff57a86c8b5d37b410d300b3c236aaf28a53a8d))
* **test:** remove documentation of never-thrown exception ([300919f](https://github.com/Protelis/Protelis/commit/300919fdef43070749df062f7718a3e24eefb7d7))

## [17.0.4](https://github.com/Protelis/Protelis/compare/17.0.3...17.0.4) (2023-06-30)


### Dependency updates

* **core-deps:** update dependency com.google.guava:guava to v32.1.1-jre ([5ad3528](https://github.com/Protelis/Protelis/commit/5ad35284f038361b565f2d34a8af751d6a8adf63))
* **deps:** update dependency gradle to v8.2 ([accfda9](https://github.com/Protelis/Protelis/commit/accfda9589f6d3b5cb3e1b8d71b98f0217b00ff8))

## [17.0.3](https://github.com/Protelis/Protelis/compare/17.0.2...17.0.3) (2023-06-29)


### Build and continuous integration

* **deps:** update actions/checkout action to v3.5.3 ([5e272bb](https://github.com/Protelis/Protelis/commit/5e272bb9ed20ab774caaeaed7b85737d726fd9aa))
* **deps:** update danysk/action-checkout action to v0.2.10 ([9205ec1](https://github.com/Protelis/Protelis/commit/9205ec1a70c081758573c89454a397e22fc0ef00))


### Dependency updates

* **core-deps:** update dependency com.google.guava:guava to v32.1.0-jre ([a1dcee6](https://github.com/Protelis/Protelis/commit/a1dcee6dd1a96195b235385ae5bf6497ff987ae6))
* **deps:** update dependency ch.qos.logback:logback-classic to v1.4.8 ([fa45edf](https://github.com/Protelis/Protelis/commit/fa45edf427675c6f2ddc509d31a77489311b80d3))
* **deps:** update dependency commons-codec:commons-codec to v1.16.0 ([1f03bab](https://github.com/Protelis/Protelis/commit/1f03bab3c7e29d3dd713c98b9979f165bb14dd13))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.33 ([5d58231](https://github.com/Protelis/Protelis/commit/5d58231a95f82daa6ee37faf2a5552fcfef1b94a))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.34 ([f24682d](https://github.com/Protelis/Protelis/commit/f24682dcce15729eb4528ed285de496721a176ea))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.35 ([f09cc59](https://github.com/Protelis/Protelis/commit/f09cc592ee7c6ebb964de15f035623cbaff288a8))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.36 ([6db8bfc](https://github.com/Protelis/Protelis/commit/6db8bfc6ab4f46acdfa7e2eade0e1c00dd1e5bcc))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.37 ([b034f28](https://github.com/Protelis/Protelis/commit/b034f28e0cf76f65cc4673a30e160435fce9b443))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.38 ([f93d381](https://github.com/Protelis/Protelis/commit/f93d3810bcd49a4511f0a7aa32febcc240aec65b))
* **deps:** update plugin com.gradle.enterprise to v3.13.4 ([42d9154](https://github.com/Protelis/Protelis/commit/42d9154f44b8497f5a1eea6a880ceadede913f3f))
* **deps:** update plugin java-qa to v1.11.0 ([a199c47](https://github.com/Protelis/Protelis/commit/a199c474abaa6f80ff58dd62ad4ec4ad944aa7d9))
* **deps:** update plugin publishoncentral to v5.0.6 ([0ed6c85](https://github.com/Protelis/Protelis/commit/0ed6c85ddda7b5e4a182d2561da7b144b4bcb79d))
* **deps:** update plugin publishoncentral to v5.0.7 ([098edae](https://github.com/Protelis/Protelis/commit/098edae8c1e47b8b0c48c6ac2564dab10aea2a85))

## [17.0.2](https://github.com/Protelis/Protelis/compare/17.0.1...17.0.2) (2023-06-09)


### Dependency updates

* **core-deps:** update dependency com.google.guava:guava to v32.0.1-jre ([1d65171](https://github.com/Protelis/Protelis/commit/1d65171027efa6c5a3603abb8d51f64f9a882a25))
* **deps:** update dependency commons-io:commons-io to v2.13.0 ([d616dab](https://github.com/Protelis/Protelis/commit/d616dabcba7b7ccbfc98a3fc78f41084dc64870d))
* **deps:** update dependency org.jetbrains.kotlin.jvm to v1.8.22 ([05503ed](https://github.com/Protelis/Protelis/commit/05503ed3bc7259c2b54cae95f6c1722cee515c94))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.27 ([45e62ce](https://github.com/Protelis/Protelis/commit/45e62ce7eeb71e305b670e80b14b6b2d751140fd))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.28 ([6399043](https://github.com/Protelis/Protelis/commit/639904331b77d60e2dbacb98fe2beb8cd8bd1657))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.29 ([5f97139](https://github.com/Protelis/Protelis/commit/5f97139de6be889eeccab0ebc7154f75574b1bfb))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.30 ([933ef50](https://github.com/Protelis/Protelis/commit/933ef50e4296570b20e49581039cc786513ee3af))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.32 ([7f1fca1](https://github.com/Protelis/Protelis/commit/7f1fca1080c67a9a5ad54468781bfc59b8d2e1b3))
* **deps:** update plugin gitsemver to v1.1.10 ([aa4a26d](https://github.com/Protelis/Protelis/commit/aa4a26ddeadaff157fa2744972a978d532683306))
* **deps:** update plugin java-qa to v1.10.0 ([b602b64](https://github.com/Protelis/Protelis/commit/b602b642e77c112dc1bbbdf3ba4188ba72a4cff5))
* **deps:** update plugin multijvmtesting to v0.5.2 ([a84b795](https://github.com/Protelis/Protelis/commit/a84b7953d9915dc9008c9ae79b0d8a7fc73d7ef8))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.1.8 ([c040690](https://github.com/Protelis/Protelis/commit/c04069079e578485f70b82be7f7bdb3b9d3a97ac))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.1.9 ([7c54ff9](https://github.com/Protelis/Protelis/commit/7c54ff94fd03adec28ad2d1e4a88ae6052f77e63))

## [17.0.1](https://github.com/Protelis/Protelis/compare/17.0.0...17.0.1) (2023-05-27)


### Style improvements

* **test:** delete redundant suppressions ([ec69e83](https://github.com/Protelis/Protelis/commit/ec69e83b9be287cbe5fa562002f96da692eb9956))
* **test:** remove unnecessary semicolon ([278e29e](https://github.com/Protelis/Protelis/commit/278e29ef512bd4da52f39425e0c77cd33eb774da))
* **test:** suppress PMD false positive ([bf1996a](https://github.com/Protelis/Protelis/commit/bf1996a2bc5d90872167f8595b350fa45cf34183))


### Build and continuous integration

* **deps:** update actions/checkout action to v3.4.0 ([52474db](https://github.com/Protelis/Protelis/commit/52474dbd43bb34a6661e240edda6248d50121ed3))
* **deps:** update actions/checkout action to v3.5.0 ([84f3488](https://github.com/Protelis/Protelis/commit/84f3488e0cf312d69ba454ac4dbfd362222eac5f))
* **deps:** update actions/checkout action to v3.5.1 ([c627d04](https://github.com/Protelis/Protelis/commit/c627d04d594c3c6f9366abc31fbaa79712c60532))
* **deps:** update actions/checkout action to v3.5.2 ([b690c06](https://github.com/Protelis/Protelis/commit/b690c06183519967792a42fa074ed40f1717e5b3))
* **deps:** update danysk/action-checkout action to v0.2.6 ([906f635](https://github.com/Protelis/Protelis/commit/906f635a664092c725cda3e1b573f4ed633b27cb))
* **deps:** update danysk/action-checkout action to v0.2.7 ([8699cbd](https://github.com/Protelis/Protelis/commit/8699cbda86ac265b3aa71ad935a7f7e67ae18e59))
* **deps:** update danysk/action-checkout action to v0.2.8 ([ea99e59](https://github.com/Protelis/Protelis/commit/ea99e590aa506a865ee0441f231ea639f34279b5))
* **deps:** update danysk/action-checkout action to v0.2.9 ([20a1c75](https://github.com/Protelis/Protelis/commit/20a1c7508934e513b4e9e5cd7f142b1c8743927d))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.2.0 ([b842bc4](https://github.com/Protelis/Protelis/commit/b842bc438da497a96434e27d3ae516ff3f7080bc))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.2.1 ([f134f65](https://github.com/Protelis/Protelis/commit/f134f657f9a9cfa16dbb503cadcfd975a12096d4))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.2.2 ([3f97e09](https://github.com/Protelis/Protelis/commit/3f97e0929eee7258ba26aa187dc103aabed12ffb))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.2.3 ([7b540ff](https://github.com/Protelis/Protelis/commit/7b540ff995d7499ae0879c1eb6001881174dd793))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.2.4 ([ea45de7](https://github.com/Protelis/Protelis/commit/ea45de7771a378f22302486785393ec9aa8f0999))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.2.5 ([d84d770](https://github.com/Protelis/Protelis/commit/d84d7706c7c7b64b8666280a6f48857d39efd455))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.2.6 ([6ea4342](https://github.com/Protelis/Protelis/commit/6ea4342183f7a60390d5061ff4d56685e68d985e))
* **mergify:** disable auto-rebasing due to Mergifyio/mergify[#5074](https://github.com/Protelis/Protelis/issues/5074) ([b61dc03](https://github.com/Protelis/Protelis/commit/b61dc03d18b27014f4ede3b4e08a3a360e9f834c))
* **mergify:** disable auto-rebasing due to Mergifyio/mergify[#5074](https://github.com/Protelis/Protelis/issues/5074) ([01198b9](https://github.com/Protelis/Protelis/commit/01198b92088679ad0088f0a3e0d1492f91bdbbbe))


### Dependency updates

* **core-deps:** update dependency com.google.guava:guava to v32 ([b5ff958](https://github.com/Protelis/Protelis/commit/b5ff9589157498a95ca7ba33edcf22ce62842cbe))
* **deps:** update dependency ch.qos.logback:logback-classic to v1.4.5 ([588b5bb](https://github.com/Protelis/Protelis/commit/588b5bb56ac74316f4a91c1a469b203915c0b4e2))
* **deps:** update dependency ch.qos.logback:logback-classic to v1.4.6 ([6fd707f](https://github.com/Protelis/Protelis/commit/6fd707fc1ab97d28bd401261ffdf1b420602ff72))
* **deps:** update dependency ch.qos.logback:logback-classic to v1.4.7 ([73f4c7a](https://github.com/Protelis/Protelis/commit/73f4c7a3ec8cb839906f84c6570945bbe3bf37c5))
* **deps:** update dependency commons-io:commons-io to v2.12.0 ([ec5077e](https://github.com/Protelis/Protelis/commit/ec5077e5aeda441b3857352d15f073528d5e6662))
* **deps:** update dependency gradle to v8 ([580bf13](https://github.com/Protelis/Protelis/commit/580bf13f6677bb3c3b28f3bbe0f719df304af1f3))
* **deps:** update dependency gradle to v8.0.2 ([4f971b3](https://github.com/Protelis/Protelis/commit/4f971b3bd4269ca91d09bb5618e930b2e2ba4b3f))
* **deps:** update dependency gradle to v8.1 ([80a3602](https://github.com/Protelis/Protelis/commit/80a3602e720c684df1847a25a36aca48a9cc09e3))
* **deps:** update dependency gradle to v8.1.1 ([a13bdf5](https://github.com/Protelis/Protelis/commit/a13bdf573eff143eb347e2dc1afbcc958ae27474))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.155 ([e40b2f0](https://github.com/Protelis/Protelis/commit/e40b2f018db58c3a69c22563d4d47dd05848df15))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.156 ([367e449](https://github.com/Protelis/Protelis/commit/367e4498067836485d79d3c7e57dbfebd5831a49))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.157 ([ccbd8fd](https://github.com/Protelis/Protelis/commit/ccbd8fd755e74336f287578c22b986f342a05646))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.158 ([b6cf932](https://github.com/Protelis/Protelis/commit/b6cf9323adc0735904269d1e3d37abfc154a5ae7))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.159 ([bd1a131](https://github.com/Protelis/Protelis/commit/bd1a131893b63a7b12ead6d037bf0509a1805e06))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.160 ([4d495e7](https://github.com/Protelis/Protelis/commit/4d495e74b5de56c16089ec3c143041d529f172bc))
* **deps:** update dependency org.jetbrains.kotlin.jvm to v1.8.20 ([c516283](https://github.com/Protelis/Protelis/commit/c516283b29aafdc4cd1447dd401a66fbea1197a3))
* **deps:** update dependency org.jetbrains.kotlin.jvm to v1.8.21 ([36a4c0f](https://github.com/Protelis/Protelis/commit/36a4c0fffbea09d00c069f8b6a1c629114d95484))
* **deps:** update dependency org.slf4j:slf4j-api to v2.0.7 ([e2495eb](https://github.com/Protelis/Protelis/commit/e2495ebeb7e837e3d13debdfbf078fea0911fe55))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.17 ([e583288](https://github.com/Protelis/Protelis/commit/e583288107f21c5eff619a0f35686f5e67a45e4e))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.19 ([f7f8cf1](https://github.com/Protelis/Protelis/commit/f7f8cf1a838823acf92e7ab86a94c6167e46292c))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.20 ([1a73b53](https://github.com/Protelis/Protelis/commit/1a73b53625418dac3c39fd1b3a76133641ba7508))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.21 ([f178ee5](https://github.com/Protelis/Protelis/commit/f178ee530668a4c7f9af405788c99767609dbea5))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.22 ([e7a64f8](https://github.com/Protelis/Protelis/commit/e7a64f8fef7c71dd55efb7d2fb8b4600617c9844))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.23 ([d1f7650](https://github.com/Protelis/Protelis/commit/d1f7650aa52ccdee998a56cf70c9686196f0de87))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.24 ([d756709](https://github.com/Protelis/Protelis/commit/d7567097cbc67a27865cffcda6c8aff1f3e18609))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.25 ([104e03c](https://github.com/Protelis/Protelis/commit/104e03cea7666b8c161cfd82f11eabac471c3cbc))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.26 ([b35eb2f](https://github.com/Protelis/Protelis/commit/b35eb2fb3da34918498839e2b1ea14b2c5ffc43c))
* **deps:** update node.js to 18.15 ([b7cfd70](https://github.com/Protelis/Protelis/commit/b7cfd7090b48155036f39fcb99a93fc959cd4e91))
* **deps:** update node.js to 18.16 ([17b9dcc](https://github.com/Protelis/Protelis/commit/17b9dcc13e6d8e7785e15cfc010b209f25ce2790))
* **deps:** update plugin com.gradle.enterprise to v3.12.4 ([398cdf7](https://github.com/Protelis/Protelis/commit/398cdf7c040d572c2ec2312b4d72d36af26e3b27))
* **deps:** update plugin com.gradle.enterprise to v3.12.5 ([e1d5e7d](https://github.com/Protelis/Protelis/commit/e1d5e7d01cfc50bdff2b3fe7bbcf8eec19fe51d7))
* **deps:** update plugin com.gradle.enterprise to v3.12.6 ([6714103](https://github.com/Protelis/Protelis/commit/67141038e314d066ae97fcea870594c60d3fc073))
* **deps:** update plugin com.gradle.enterprise to v3.13 ([622aa17](https://github.com/Protelis/Protelis/commit/622aa17087c1fc44cd4941a2edfd0562b6829bfa))
* **deps:** update plugin com.gradle.enterprise to v3.13.1 ([f98ed9c](https://github.com/Protelis/Protelis/commit/f98ed9c71af27a60f39e0f8d607a06abcc691c17))
* **deps:** update plugin com.gradle.enterprise to v3.13.2 ([cb71434](https://github.com/Protelis/Protelis/commit/cb7143495d6ea0c197ae49abcbef6078a8d08744))
* **deps:** update plugin com.gradle.enterprise to v3.13.3 ([8727b3b](https://github.com/Protelis/Protelis/commit/8727b3bf7c14e1ea1dfa7a6789db646f91ae5379))
* **deps:** update plugin gitsemver to v1.1.1 ([f0fa858](https://github.com/Protelis/Protelis/commit/f0fa8584597ffafbf09fbbe203f879b2da4f1453))
* **deps:** update plugin gitsemver to v1.1.2 ([1c82a08](https://github.com/Protelis/Protelis/commit/1c82a08bc265b2f8d3e027fd66713c55c4075bf7))
* **deps:** update plugin gitsemver to v1.1.4 ([1c29169](https://github.com/Protelis/Protelis/commit/1c29169e3f4015218b33de9ba2643da5d5d00791))
* **deps:** update plugin gitsemver to v1.1.5 ([661af69](https://github.com/Protelis/Protelis/commit/661af69bb60a78e75009eee76416059c904d388b))
* **deps:** update plugin gitsemver to v1.1.6 ([2f2e9f0](https://github.com/Protelis/Protelis/commit/2f2e9f0212e332be1f17387a979b77a8e8a0ebfc))
* **deps:** update plugin gitsemver to v1.1.7 ([ac5cfcf](https://github.com/Protelis/Protelis/commit/ac5cfcf50a63e962fe01ad866a86ef44650735d4))
* **deps:** update plugin gitsemver to v1.1.8 ([1b17293](https://github.com/Protelis/Protelis/commit/1b172935eb21c03d7a5b10c6ec011ceb0007df5d))
* **deps:** update plugin gitsemver to v1.1.9 ([eaff972](https://github.com/Protelis/Protelis/commit/eaff97245b995adbccedce999054952987efc36a))
* **deps:** update plugin java-qa to v0.43.1 ([8c8725d](https://github.com/Protelis/Protelis/commit/8c8725db7b10cb2f33ac3be04049d98494eff369))
* **deps:** update plugin java-qa to v0.44.0 ([43a3013](https://github.com/Protelis/Protelis/commit/43a301300724ba2e1ab2ea4130fa89097222f804))
* **deps:** update plugin java-qa to v1 ([83e33d3](https://github.com/Protelis/Protelis/commit/83e33d3c5dc399a10caa91348f4ff5182c14491d))
* **deps:** update plugin java-qa to v1.1.0 ([5f05840](https://github.com/Protelis/Protelis/commit/5f058409dc4fd45e2259f9b663d3e943867550b4))
* **deps:** update plugin java-qa to v1.2.0 ([1428c1e](https://github.com/Protelis/Protelis/commit/1428c1e6ce7a388fa99c1f8e432ad4826521c39e))
* **deps:** update plugin java-qa to v1.3.0 ([a0e6f95](https://github.com/Protelis/Protelis/commit/a0e6f957247bb8734c1f17e7a32f7403b90d1d3c))
* **deps:** update plugin java-qa to v1.4.0 ([9722217](https://github.com/Protelis/Protelis/commit/97222179a4d358bf6db1453bef8337cb09c29f34))
* **deps:** update plugin java-qa to v1.5.0 ([9e9db7b](https://github.com/Protelis/Protelis/commit/9e9db7b240ad0ce99bb82d8f50f7b164a97c99db))
* **deps:** update plugin java-qa to v1.6.0 ([4998dce](https://github.com/Protelis/Protelis/commit/4998dce7704df45dc8de79cb35bfd13b30ab1e75))
* **deps:** update plugin java-qa to v1.7.0 ([f8175fc](https://github.com/Protelis/Protelis/commit/f8175fcf791dde79b04e0e2345ee0ee9d2c34538))
* **deps:** update plugin java-qa to v1.8.0 ([3a7d0ea](https://github.com/Protelis/Protelis/commit/3a7d0ea80629397efc8f3a52e7d6e77640f4c211))
* **deps:** update plugin java-qa to v1.9.0 ([5bc963a](https://github.com/Protelis/Protelis/commit/5bc963a91de92ebd85872581764c147ce45665fc))
* **deps:** update plugin kotlin-qa to v0.34.2 ([b203d48](https://github.com/Protelis/Protelis/commit/b203d489efefd615b27e9505c8c6cbf996a1d6fd))
* **deps:** update plugin kotlin-qa to v0.35.0 ([fb6fa09](https://github.com/Protelis/Protelis/commit/fb6fa09fd431282ccdcab7c2e8caa0509188b033))
* **deps:** update plugin kotlin-qa to v0.36.1 ([2dd48d2](https://github.com/Protelis/Protelis/commit/2dd48d2efd29c2443af222797cb3d5e1647b5a6e))
* **deps:** update plugin kotlin-qa to v0.37.0 ([6621c71](https://github.com/Protelis/Protelis/commit/6621c71f5b6116caffce73236be83a38e257c586))
* **deps:** update plugin multijvmtesting to v0.4.17 ([7697b4a](https://github.com/Protelis/Protelis/commit/7697b4ac4f601d973e1263700a7c2022a649f56f))
* **deps:** update plugin multijvmtesting to v0.4.19 ([4224f86](https://github.com/Protelis/Protelis/commit/4224f8644b76deeb3aa8ee3e295386cb2b830185))
* **deps:** update plugin multijvmtesting to v0.4.20 ([7be38c0](https://github.com/Protelis/Protelis/commit/7be38c00a37610478aa1798645f01363cfe2c33a))
* **deps:** update plugin multijvmtesting to v0.4.21 ([fa86c86](https://github.com/Protelis/Protelis/commit/fa86c86ce0f1d93e3c3e5747b66fe51874b544fb))
* **deps:** update plugin multijvmtesting to v0.4.22 ([385620c](https://github.com/Protelis/Protelis/commit/385620c2fbf047b41d9af0be6d8bd13cdddc7b2b))
* **deps:** update plugin multijvmtesting to v0.4.23 ([27f8bb6](https://github.com/Protelis/Protelis/commit/27f8bb676349a990c40f80d9cb132854a3b5a5aa))
* **deps:** update plugin multijvmtesting to v0.5.0 ([4569030](https://github.com/Protelis/Protelis/commit/4569030409b828f5311a2e6d1cf96c1e88606c54))
* **deps:** update plugin multijvmtesting to v0.5.1 ([a699c47](https://github.com/Protelis/Protelis/commit/a699c47adffea09486423238549bfddebe06d4f6))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.1.5 ([0f819c0](https://github.com/Protelis/Protelis/commit/0f819c025bd1e61fd1ac2f23963fe95ca8365420))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.1.6 ([2842016](https://github.com/Protelis/Protelis/commit/284201615a729829d68979838d0078f04b694e3f))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.1.7 ([ff60419](https://github.com/Protelis/Protelis/commit/ff60419e72e27cd1b774e6e63b4e1c070b578a8f))
* **deps:** update plugin org.gradle.toolchains.foojay-resolver-convention to v0.5.0 ([e615693](https://github.com/Protelis/Protelis/commit/e615693f0bf2b306439501717b22c760ac8adfdc))
* **deps:** update plugin publishoncentral to v3.3.0 ([af782e4](https://github.com/Protelis/Protelis/commit/af782e4714273637c343a00290ca45518c9abc80))
* **deps:** update plugin publishoncentral to v3.3.1 ([ba675f8](https://github.com/Protelis/Protelis/commit/ba675f8ea468c23fd530d7b75dcabe999b577109))
* **deps:** update plugin publishoncentral to v3.3.2 ([12a099c](https://github.com/Protelis/Protelis/commit/12a099cd70ba3a6fa9032d44e2b4f0653656bad3))
* **deps:** update plugin publishoncentral to v3.3.3 ([cf2c530](https://github.com/Protelis/Protelis/commit/cf2c5300afc34a60e4a0c78b2f665777a1d53b3f))
* **deps:** update plugin publishoncentral to v3.4.0 ([16aa6b4](https://github.com/Protelis/Protelis/commit/16aa6b4a7d58a934e7d7edab32ea7d0c6adf0cdd))
* **deps:** update plugin publishoncentral to v4 ([819f34f](https://github.com/Protelis/Protelis/commit/819f34f9b87dffe8d9e6bceba50ceb888b15fa0f))
* **deps:** update plugin publishoncentral to v4.0.1 ([24d12e4](https://github.com/Protelis/Protelis/commit/24d12e48f07c349494f66a40ad068ba5291cd3fa))
* **deps:** update plugin publishoncentral to v4.1.0 ([ee3dd2d](https://github.com/Protelis/Protelis/commit/ee3dd2d315302b0edccd197ee124b103a21e21b8))
* **deps:** update plugin publishoncentral to v4.1.1 ([1ed1ccd](https://github.com/Protelis/Protelis/commit/1ed1ccd9ffe4ff225df3c4a50ec61440a0758f89))
* **deps:** update plugin publishoncentral to v5 ([cd48250](https://github.com/Protelis/Protelis/commit/cd48250de900078d6a2821c490f05dc77261d3cf))
* **deps:** update plugin publishoncentral to v5.0.2 ([f48d19a](https://github.com/Protelis/Protelis/commit/f48d19af9fbc293004e48be6929547a4fb3d9063))
* **deps:** update plugin publishoncentral to v5.0.3 ([372ed1d](https://github.com/Protelis/Protelis/commit/372ed1d856b2bf88ac89f50ca32ff8eac997d74f))
* **deps:** update plugin publishoncentral to v5.0.4 ([c99a630](https://github.com/Protelis/Protelis/commit/c99a6306a7c5e4c056a7676292f921cea6934100))
* **deps:** update plugin publishoncentral to v5.0.5 ([b127d62](https://github.com/Protelis/Protelis/commit/b127d62621979bcc55e9d41b32853b37b50b4208))
* **deps:** update plugin shadowjar to v8 ([ab96ed8](https://github.com/Protelis/Protelis/commit/ab96ed866c2eca13519fd2cbe982a8e1b3a852b2))
* **deps:** update plugin shadowjar to v8.1.0 ([bf50bd1](https://github.com/Protelis/Protelis/commit/bf50bd165b5dac5711303012d8fb8f5ff7de5af6))
* **deps:** update plugin shadowjar to v8.1.1 ([38c4534](https://github.com/Protelis/Protelis/commit/38c45343173b87110b7b6460afbea8f265cc1a68))

## [17.0.0](https://github.com/Protelis/Protelis/compare/16.4.1...17.0.0) (2023-02-21)


### ⚠ BREAKING CHANGES

* add support for Java 19, switch to Java 11 as minimum requirement

### Dependency updates

* **api-deps:** update dependency org.protelis:protelis.parser to v11 ([4a734e1](https://github.com/Protelis/Protelis/commit/4a734e17a7ea65b969fac1e9bb08f9a690aac7b2))
* **deps:** update dependency org.jetbrains.kotlin.jvm to v1.8.10 ([109546b](https://github.com/Protelis/Protelis/commit/109546bc3b8ba814f3e5ad8fcb0c733ad4fcbb1c))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.16 ([09bbd7c](https://github.com/Protelis/Protelis/commit/09bbd7ce33921baf8ccb692cad63d784893041c4))
* **deps:** update node.js to 18.13 ([e3cb9a3](https://github.com/Protelis/Protelis/commit/e3cb9a397f01aebc593fab03cb1a8e3faa4221d0))
* **deps:** update node.js to 18.14 ([4ef0028](https://github.com/Protelis/Protelis/commit/4ef00287eb2cbd8d5c170cdfcfe65e55d761706f))
* **deps:** update plugin com.gradle.enterprise to v3.11.4 ([897cac5](https://github.com/Protelis/Protelis/commit/897cac58de96df088536b13f313565b82283224a))
* **deps:** update plugin com.gradle.enterprise to v3.12.3 ([0872201](https://github.com/Protelis/Protelis/commit/0872201abff67c8c1c127829fcc5da6ba063b111))
* **deps:** update plugin gitsemver to v1 ([4789bb9](https://github.com/Protelis/Protelis/commit/4789bb91b4e095aa2c96944a0fe684e88fc1d5bb))
* **deps:** update plugin gitsemver to v1.0.2 ([c94ec95](https://github.com/Protelis/Protelis/commit/c94ec95e22494e2eb70c7dbfbb904f7f1cb64196))
* **deps:** update plugin kotlin-qa to v0.34.0 ([e95dd39](https://github.com/Protelis/Protelis/commit/e95dd396941087232ef8bd54b59328a72193360d))
* **deps:** update plugin kotlin-qa to v0.34.1 ([b6d45d2](https://github.com/Protelis/Protelis/commit/b6d45d20ae6fa1ebfb232a2bea49afb420bafc5b))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.1.2 ([b8945f0](https://github.com/Protelis/Protelis/commit/b8945f009473827174199a44128d3587992f549b))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.1.3 ([d454e63](https://github.com/Protelis/Protelis/commit/d454e6386aabd36651eae8219e0bedd3f2d7c5c9))
* **deps:** update plugin publishoncentral to v2.0.13 ([e6b6955](https://github.com/Protelis/Protelis/commit/e6b6955e0bbd7c0cdfd1a6f00015677c19435747))
* **deps:** update plugin publishoncentral to v3 ([bd8467e](https://github.com/Protelis/Protelis/commit/bd8467eb11a2a10a9f6a80a03a0090e96a9c4594))
* **deps:** update plugin publishoncentral to v3.2.3 ([871f512](https://github.com/Protelis/Protelis/commit/871f512c5a9e47805deefe0390860bbbd177cf8f))
* **deps:** update plugin publishoncentral to v3.2.4 ([5d4dc9a](https://github.com/Protelis/Protelis/commit/5d4dc9ac28f97febe4fcb98ef890c508d130a528))
* **deps:** update plugin tasktree to v2.1.1 ([f8683ca](https://github.com/Protelis/Protelis/commit/f8683cac3264352e276d7541498fad9c8f40e340))


### Build and continuous integration

* add support for Java 19, switch to Java 11 as minimum requirement ([44f10f1](https://github.com/Protelis/Protelis/commit/44f10f10578de7591556102ec8b9df3d9d24fc21))
* apply org.gradle.toolchains.foojay-resolver-convention ([f49d421](https://github.com/Protelis/Protelis/commit/f49d421d574a6c904cb4116ed9f374f4682d520e))
* **deps:** update danysk/action-checkout action to v0.2.5 ([7a05900](https://github.com/Protelis/Protelis/commit/7a05900d1d5d9b0b4e7397e40d6e48f4111110d1))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.21 ([bc27264](https://github.com/Protelis/Protelis/commit/bc27264260623631e6a3159047974575b411991c))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.22 ([8bc0971](https://github.com/Protelis/Protelis/commit/8bc09713dcfc5806dc8be64bb11b5b98514b0ffc))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.23 ([afb7dbf](https://github.com/Protelis/Protelis/commit/afb7dbfe4956d39ad3c564b9485f3e65720973aa))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.24 ([d55721c](https://github.com/Protelis/Protelis/commit/d55721c2ecc02d479c3e8ada0c5010e361af1efe))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.25 ([0beb772](https://github.com/Protelis/Protelis/commit/0beb7722dda1e0bf53341fa297f3b158146e16d3))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.26 ([e5bce87](https://github.com/Protelis/Protelis/commit/e5bce872205aaae4118c13eaa2ba83268e32c567))
* drop dry-deployed packages to relieve Sonatype OSSRH ([d080be1](https://github.com/Protelis/Protelis/commit/d080be1e6ebf2856002a6b559f59f08034a49901))
* **mergify:** update mergify ([7e866c1](https://github.com/Protelis/Protelis/commit/7e866c1b287642d8d1ef2014fabe7bb0bd674ac6))


### Style improvements

* **test:** remove redundant escapes in regex ([3b5a2fd](https://github.com/Protelis/Protelis/commit/3b5a2fdb96bd39b7cabb20a17965f1397af976df))
* **test:** replace statement lambda with expression lambda ([b5967e7](https://github.com/Protelis/Protelis/commit/b5967e72b1e7c60b789280a06bc6841146e71220))


### Documentation

* **javadoc:** switch to Javadoc 11 ([bb6766f](https://github.com/Protelis/Protelis/commit/bb6766f85dfa07aa40ec750d5ab4f07c01eeeb6d))

## [16.4.1](https://github.com/Protelis/Protelis/compare/16.4.0...16.4.1) (2023-01-06)


### Bug Fixes

* **interpreter:** improve error messages when operators are non-applicable ([18fda1b](https://github.com/Protelis/Protelis/commit/18fda1be2c003ef231685cec0bb148930cc38aa3))


### Build and continuous integration

* configure mergify ([bd9c13a](https://github.com/Protelis/Protelis/commit/bd9c13aa912e43450fe1768eefb92fe4457f583b))
* **deps:** update actions/checkout action to v3.1.0 ([c46807f](https://github.com/Protelis/Protelis/commit/c46807fa8ccbc26178cec28af663fa4baba5e058))
* **deps:** update actions/checkout action to v3.2.0 ([9ce3b8e](https://github.com/Protelis/Protelis/commit/9ce3b8e731346353034dea1142ec37978c7bbb4c))
* **deps:** update actions/checkout action to v3.3.0 ([8585d85](https://github.com/Protelis/Protelis/commit/8585d850c157a26d08527beeb9e1f8d65216b208))
* **deps:** update danysk/action-checkout action to v0.2.2 ([0e9323d](https://github.com/Protelis/Protelis/commit/0e9323db9793594fa532408f8076311876b8bc7f))
* **deps:** update danysk/action-checkout action to v0.2.3 ([b8b97a4](https://github.com/Protelis/Protelis/commit/b8b97a48bbdde82ff7210c94f0d9cfe664cf354b))
* **deps:** update danysk/action-checkout action to v0.2.4 ([3b92be2](https://github.com/Protelis/Protelis/commit/3b92be27034330f571dc87538fe46eb1b2cfd35c))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.1 ([0bdb6aa](https://github.com/Protelis/Protelis/commit/0bdb6aaa8161e241ecc3b8077d92b391ef9022ae))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.10 ([1bf6fce](https://github.com/Protelis/Protelis/commit/1bf6fcee93bc88096e42ff73e86593c63c82903b))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.11 ([0ba8f42](https://github.com/Protelis/Protelis/commit/0ba8f421eab7d636242da159b95e2d0c68ab1417))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.12 ([5e1bf2a](https://github.com/Protelis/Protelis/commit/5e1bf2a3c3c44e7e9720271e03cc802b1d49b120))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.13 ([8e74d2a](https://github.com/Protelis/Protelis/commit/8e74d2af056e91e7ff7978e509a1aca2787dc30d))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.14 ([6b951ae](https://github.com/Protelis/Protelis/commit/6b951ae564e5e8eb2a7cd565867ea1e597a74f60))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.15 ([6bae98b](https://github.com/Protelis/Protelis/commit/6bae98ba4b4d2e39cf44c67f07557afd5aa386c8))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.16 ([c54cf2b](https://github.com/Protelis/Protelis/commit/c54cf2be9c9b7ad20d0b6370a1f999a2f8baf4c7))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.17 ([7f50e69](https://github.com/Protelis/Protelis/commit/7f50e69996849e6fb3e437e75ea9aafa1aafe2d7))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.18 ([ba0ba46](https://github.com/Protelis/Protelis/commit/ba0ba460447021d0403046cc98a2565e52ec77dc))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.19 ([870df36](https://github.com/Protelis/Protelis/commit/870df36df80670edd0464cd73dc91d7863d4706b))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.2 ([bba3b81](https://github.com/Protelis/Protelis/commit/bba3b81b80779f179040096ae45289878aa3bb8d))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.20 ([0e8a4c3](https://github.com/Protelis/Protelis/commit/0e8a4c34cb570a4e50a21850ab2f122e15deca36))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.3 ([ea677ac](https://github.com/Protelis/Protelis/commit/ea677ac0bef05959fed815cb60f267b622d6c09d))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.4 ([b155256](https://github.com/Protelis/Protelis/commit/b155256a00fd9a9a297127996ca568480dc7d3ed))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.5 ([92af453](https://github.com/Protelis/Protelis/commit/92af4530bb479b2d242a244b306f21829074388d))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.6 ([f711382](https://github.com/Protelis/Protelis/commit/f711382311807e48c917d6c794266453115ece31))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.7 ([8b91af1](https://github.com/Protelis/Protelis/commit/8b91af1b710ff11c57155c992f423c5ba6f38a44))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.8 ([deff0f2](https://github.com/Protelis/Protelis/commit/deff0f2d5446df91bd2e8001c91eac8dccdbdc85))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.9 ([6b98135](https://github.com/Protelis/Protelis/commit/6b98135dea3d3e55a33c8d456a9b8cda799d6d4a))
* do not run any secrets-requiring CI step on PR builds, as internal PRs have branch builds, and Dependabot has no access to secrets anyway ([ecbb1c5](https://github.com/Protelis/Protelis/commit/ecbb1c50a9ad27241fe520589641999a39c60825))
* ignore the mergify configuration updates ([81e9ef7](https://github.com/Protelis/Protelis/commit/81e9ef711a0f0a9d440bd7b61bdc22b2a0a34e14))


### Dependency updates

* **deps:** bump npm from 8.5.3 to 8.13.2 ([177cf19](https://github.com/Protelis/Protelis/commit/177cf1972b77033c9fc8819b9dcd621aa5384aa6))
* **deps:** bump semantic-release from 19.0.2 to 19.0.3 ([#724](https://github.com/Protelis/Protelis/issues/724)) ([50c4cc3](https://github.com/Protelis/Protelis/commit/50c4cc3476f1deb0504c3ced7b5284fb298b31ae))
* **deps:** bump semver-regex from 3.1.3 to 3.1.4 ([590004a](https://github.com/Protelis/Protelis/commit/590004af27b3c07464f100ab1f5971a0e6895842))
* **deps:** update dependency ch.qos.logback:logback-classic to v1.3.0 ([cebf567](https://github.com/Protelis/Protelis/commit/cebf567dfa0e09d556ed31d988426a7f0d23be0d))
* **deps:** update dependency ch.qos.logback:logback-classic to v1.3.0-beta0 ([854942b](https://github.com/Protelis/Protelis/commit/854942b17331edb9282b4c46703bea2117e5cc8a))
* **deps:** update dependency ch.qos.logback:logback-classic to v1.3.1 ([708b2fa](https://github.com/Protelis/Protelis/commit/708b2fad6d309230c74e157853ee71dad81f9bc3))
* **deps:** update dependency ch.qos.logback:logback-classic to v1.3.2 ([a17ae1d](https://github.com/Protelis/Protelis/commit/a17ae1d2a4c158fc8b8a0d233e111b714568cc50))
* **deps:** update dependency ch.qos.logback:logback-classic to v1.3.3 ([1ca1363](https://github.com/Protelis/Protelis/commit/1ca1363421922351d4c8afdbbee905cdbc4a47dd))
* **deps:** update dependency ch.qos.logback:logback-classic to v1.3.4 ([80d7609](https://github.com/Protelis/Protelis/commit/80d7609dee1f2ed24be8406e3299deb9a50d2133))
* **deps:** update dependency ch.qos.logback:logback-classic to v1.3.5 ([4c49ea3](https://github.com/Protelis/Protelis/commit/4c49ea33b3a33738927a5c29c0b2616787e1bf31))
* **deps:** update dependency com.github.spotbugs:spotbugs-annotations to v4.7.2 ([5d13fb6](https://github.com/Protelis/Protelis/commit/5d13fb6915b17ef7368955faeb9042d84d53773d))
* **deps:** update dependency com.github.spotbugs:spotbugs-annotations to v4.7.3 ([b65e812](https://github.com/Protelis/Protelis/commit/b65e812c92252ca3f9c05ef4fa4382a04331127d))
* **deps:** update dependency gradle to v7.5 ([49fcd47](https://github.com/Protelis/Protelis/commit/49fcd47971a43f1cba8f18bf4bdaf57e302d76a9))
* **deps:** update dependency gradle to v7.5.1 ([0eb1863](https://github.com/Protelis/Protelis/commit/0eb186316185a8f3147654d08db88afe98e132fb))
* **deps:** update dependency gradle to v7.6 ([02e0f97](https://github.com/Protelis/Protelis/commit/02e0f97df41ebcb700b5be64e8304748415f2740))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.150 ([564c1fc](https://github.com/Protelis/Protelis/commit/564c1fc005b3f6e867824e51cbce60e8dfbb1317))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.151 ([121c830](https://github.com/Protelis/Protelis/commit/121c83091e2765111cbf4b4a7cd2057cab3dbc39))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.152 ([3497115](https://github.com/Protelis/Protelis/commit/3497115137c6bd8833d10b5dfc6be4dfd7c318b7))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.153 ([0b6984f](https://github.com/Protelis/Protelis/commit/0b6984f2a8cccdc54f8b3f6e740b76b9e83e91c8))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.154 ([6be34b5](https://github.com/Protelis/Protelis/commit/6be34b53931189ae74cb59bd4e43ed5e6d73535c))
* **deps:** update dependency org.jetbrains.kotlin.jvm to v1.7.20 ([d8c871f](https://github.com/Protelis/Protelis/commit/d8c871feb2e0f05832c8a0eb8527bf05ab5494fc))
* **deps:** update dependency org.jetbrains.kotlin.jvm to v1.7.21 ([30d41c3](https://github.com/Protelis/Protelis/commit/30d41c3cb78af1cfb5a551d3ec5c364507a0c9a5))
* **deps:** update dependency org.jetbrains.kotlin.jvm to v1.7.22 ([9f21fbb](https://github.com/Protelis/Protelis/commit/9f21fbb0807ae6d0327339adffd81a28ca8eafd8))
* **deps:** update dependency org.jetbrains.kotlin.jvm to v1.8.0 ([b71c696](https://github.com/Protelis/Protelis/commit/b71c696df5208ecb40567ad584fb3d5137c6571b))
* **deps:** update dependency org.slf4j:slf4j-api to v2 ([73eff83](https://github.com/Protelis/Protelis/commit/73eff83640dd77a5e12ae1c1e1d280fd43bfaeae))
* **deps:** update dependency org.slf4j:slf4j-api to v2.0.1 ([a658c07](https://github.com/Protelis/Protelis/commit/a658c077686c7f1afb1da053992bfaf0a33ea39c))
* **deps:** update dependency org.slf4j:slf4j-api to v2.0.2 ([f6ba2ae](https://github.com/Protelis/Protelis/commit/f6ba2ae484a078c5b7c2a83fa5e0dd52f65fc0a3))
* **deps:** update dependency org.slf4j:slf4j-api to v2.0.3 ([bc63e91](https://github.com/Protelis/Protelis/commit/bc63e911ae7619ecb8a8a24912634befe2e7a902))
* **deps:** update dependency org.slf4j:slf4j-api to v2.0.4 ([74c98d2](https://github.com/Protelis/Protelis/commit/74c98d2e32fd6bd73232d00f51322797fc84ee46))
* **deps:** update dependency org.slf4j:slf4j-api to v2.0.5 ([1029b2d](https://github.com/Protelis/Protelis/commit/1029b2d31897442a85ada7255aae7f9d759094dc))
* **deps:** update dependency org.slf4j:slf4j-api to v2.0.6 ([38f1adf](https://github.com/Protelis/Protelis/commit/38f1adf64a3473e0002fee4bc043c2c53b098632))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.10 ([f7dad02](https://github.com/Protelis/Protelis/commit/f7dad028a89caa8d8379b9a0ca0a492a17e6a3f1))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.11 ([e7afc2f](https://github.com/Protelis/Protelis/commit/e7afc2f40a123658c01e483a877f272e61a7747a))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.12 ([c36ac7c](https://github.com/Protelis/Protelis/commit/c36ac7cac78d02ea693dc13d4da1818c5c9d35c9))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.13 ([21c9736](https://github.com/Protelis/Protelis/commit/21c973649a1e3fd9c14ee9cf2932e3d7f8f395e7))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.14 ([923a131](https://github.com/Protelis/Protelis/commit/923a1312d8c3ae1fbb607f74a1ea3ce8d5fab828))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.15 ([9edb51b](https://github.com/Protelis/Protelis/commit/9edb51b1af2ab9654bae818d47c50a1cd7439e56))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.9 ([3ca4e81](https://github.com/Protelis/Protelis/commit/3ca4e81f1f11696fd96bdec86575be7ac0713bd0))
* **deps:** update node.js to 16.16 ([7c7589c](https://github.com/Protelis/Protelis/commit/7c7589c725a4d3cfe243db22542393a8049e62ab))
* **deps:** update node.js to 16.17 ([ba8ccd4](https://github.com/Protelis/Protelis/commit/ba8ccd46601925adf3fc50b6b5cbced747713dec))
* **deps:** update node.js to 16.18 ([92c2ef1](https://github.com/Protelis/Protelis/commit/92c2ef1af7916556a4176baadf3a3d903147aa80))
* **deps:** update node.js to 18.12 ([ae76325](https://github.com/Protelis/Protelis/commit/ae76325b77402d00c186643999072008f6551a67))
* **deps:** update node.js to v18 ([73b02d1](https://github.com/Protelis/Protelis/commit/73b02d177ef378472e9d3219d074b4101bae1db4))
* **deps:** update plugin com.gradle.enterprise to v3.10.3 ([f5d1ef5](https://github.com/Protelis/Protelis/commit/f5d1ef5ff87e1033039cbb6b060f281a460187b3))
* **deps:** update plugin com.gradle.enterprise to v3.11 ([7ef79be](https://github.com/Protelis/Protelis/commit/7ef79be25807ce1f51c8b9bd31692b1a87c7adae))
* **deps:** update plugin com.gradle.enterprise to v3.11.1 ([8433e21](https://github.com/Protelis/Protelis/commit/8433e211a619652dd4c01ee5c20e3664bf1c33cf))
* **deps:** update plugin kotlin-qa to v0.20.3 ([f6d9d1d](https://github.com/Protelis/Protelis/commit/f6d9d1dfd30976ab178c36936b064ad8f5eb8077))
* **deps:** update plugin kotlin-qa to v0.20.4 ([67b89a7](https://github.com/Protelis/Protelis/commit/67b89a70664f309b159c7ccfbeed4f6245b11806))
* **deps:** update plugin kotlin-qa to v0.21.0 ([47e377f](https://github.com/Protelis/Protelis/commit/47e377fb108b8ba3608d074a652c1e68abded70f))
* **deps:** update plugin kotlin-qa to v0.22.0 ([2722bf5](https://github.com/Protelis/Protelis/commit/2722bf577f5282a78ec96724a20a1ac24fb16427))
* **deps:** update plugin kotlin-qa to v0.22.1 ([8c05f6b](https://github.com/Protelis/Protelis/commit/8c05f6bcd0ad6be33ab1c9ea5ecff4d93ca214b5))
* **deps:** update plugin kotlin-qa to v0.22.2 ([a498467](https://github.com/Protelis/Protelis/commit/a498467f8d0adea917125e77c7470e256a8772ce))
* **deps:** update plugin kotlin-qa to v0.23.0 ([d3a1ae0](https://github.com/Protelis/Protelis/commit/d3a1ae09a270e802490f30e5b099751e657acd72))
* **deps:** update plugin kotlin-qa to v0.23.1 ([6cb20b6](https://github.com/Protelis/Protelis/commit/6cb20b65dd2d9916c260061822c7d1b211575f32))
* **deps:** update plugin kotlin-qa to v0.23.2 ([314f17f](https://github.com/Protelis/Protelis/commit/314f17f5931503ecd3c0c66002f124ea3809f10b))
* **deps:** update plugin kotlin-qa to v0.24.0 ([0161b6d](https://github.com/Protelis/Protelis/commit/0161b6d5b88793cbe8c1dc597cba5a8f97544867))
* **deps:** update plugin kotlin-qa to v0.25.0 ([00a01fe](https://github.com/Protelis/Protelis/commit/00a01fe57bb927b21608683f0f8190bf438ae122))
* **deps:** update plugin kotlin-qa to v0.25.1 ([fa6b345](https://github.com/Protelis/Protelis/commit/fa6b3452557e34df7082b350230e40c29f066323))
* **deps:** update plugin kotlin-qa to v0.26.0 ([31e9fc8](https://github.com/Protelis/Protelis/commit/31e9fc83982415074ccde742f855608f8579995c))
* **deps:** update plugin kotlin-qa to v0.26.1 ([b3a24d0](https://github.com/Protelis/Protelis/commit/b3a24d05e23d286e2b92df29d130212229f2fef4))
* **deps:** update plugin kotlin-qa to v0.27.0 ([944654e](https://github.com/Protelis/Protelis/commit/944654ed5b725464ab31e107cc85530e311b84a7))
* **deps:** update plugin kotlin-qa to v0.27.1 ([226ed2d](https://github.com/Protelis/Protelis/commit/226ed2d7ab0681f83c1c46e34153a127b76c887c))
* **deps:** update plugin kotlin-qa to v0.28.0 ([ed3e5a2](https://github.com/Protelis/Protelis/commit/ed3e5a2755daaec97d717a2919f54732d61d3a48))
* **deps:** update plugin kotlin-qa to v0.29.0 ([1385243](https://github.com/Protelis/Protelis/commit/1385243f35db09691d011871be91835f2f66da1d))
* **deps:** update plugin kotlin-qa to v0.29.1 ([33e8a85](https://github.com/Protelis/Protelis/commit/33e8a850bcfbe8b175b3e1e40d9f6ebbc3828278))
* **deps:** update plugin kotlin-qa to v0.29.2 ([84f1c34](https://github.com/Protelis/Protelis/commit/84f1c34636a0cd741b7b8ef2ada391623ca3c6de))
* **deps:** update plugin multijvmtesting to v0.4.6 ([307a5a8](https://github.com/Protelis/Protelis/commit/307a5a8dca15b9fbb3277998b1397a8a4a27b909))
* **deps:** update plugin multijvmtesting to v0.4.7 ([cf17ea3](https://github.com/Protelis/Protelis/commit/cf17ea3b0e5f377084eca9c863a22f2ce45e79c1))
* **deps:** update plugin multijvmtesting to v0.4.8 ([95e5de8](https://github.com/Protelis/Protelis/commit/95e5de895239a3e1c2a13b9a98d1524591889a12))
* **deps:** update plugin multijvmtesting to v0.4.9 ([3548c6e](https://github.com/Protelis/Protelis/commit/3548c6e42942558cea8cf5b5d6358171595574f0))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.15 ([86f3ef9](https://github.com/Protelis/Protelis/commit/86f3ef91a5480dc852e35f56aedbf3b45a3ec351))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.16 ([7cbf314](https://github.com/Protelis/Protelis/commit/7cbf314afa6195fe07361114ec27c75967530d76))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.17 ([8e02e50](https://github.com/Protelis/Protelis/commit/8e02e50b7e89d87a113f6b09612556bd96175fe6))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.18 ([6afb1fa](https://github.com/Protelis/Protelis/commit/6afb1fa6a9827ef7ae772b23ea37760d4489c886))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.19 ([eb337c6](https://github.com/Protelis/Protelis/commit/eb337c6935d53a1ded64e71b8d0cc01ca9edf33f))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.20 ([34e8a60](https://github.com/Protelis/Protelis/commit/34e8a60aadd79f82b082ccc8159c0b6354b98b87))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.21 ([990e1c5](https://github.com/Protelis/Protelis/commit/990e1c552842eda3b72e00c4d378add4a3078f4d))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.22 ([4db069f](https://github.com/Protelis/Protelis/commit/4db069f4a034ad50d99f2cde9b2143b26967df09))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.23 ([e47b19d](https://github.com/Protelis/Protelis/commit/e47b19dc8a88b500081bca91bc0db76b9dccbafa))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.24 ([8941038](https://github.com/Protelis/Protelis/commit/8941038648c6751fa61f1a222b54c5524a807a68))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.25 ([8def9e7](https://github.com/Protelis/Protelis/commit/8def9e700862a035b8329ef3b1602171217c773a))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.1.0 ([9b787b1](https://github.com/Protelis/Protelis/commit/9b787b1c3cddea172cefef6e568be198e75df9f9))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.1.1 ([13ba053](https://github.com/Protelis/Protelis/commit/13ba05384b2a183e004726f7fb108674d65fcd77))
* **deps:** update plugin publishoncentral to v2.0.10 ([3fdee09](https://github.com/Protelis/Protelis/commit/3fdee09f8562022889032158030c02ecc4b98956))
* **deps:** update plugin publishoncentral to v2.0.11 ([f7761de](https://github.com/Protelis/Protelis/commit/f7761debf46bf57c7aa51b7c5f7d06e2767318e9))
* **deps:** update plugin publishoncentral to v2.0.12 ([3fea537](https://github.com/Protelis/Protelis/commit/3fea5371999f964c13dcc8acd1ee27f900194526))
* **deps:** update plugin publishoncentral to v2.0.4 ([ab9c59d](https://github.com/Protelis/Protelis/commit/ab9c59d254c123b53d5d4bf832e5e98f932921d0))
* **deps:** update plugin publishoncentral to v2.0.5 ([661ee74](https://github.com/Protelis/Protelis/commit/661ee74ec0d6dc9d3b96d05e6297e1fce74048f0))
* **deps:** update plugin publishoncentral to v2.0.6 ([a0c22e7](https://github.com/Protelis/Protelis/commit/a0c22e728c1a7253d5636000d6a083391365f64b))
* **deps:** update plugin publishoncentral to v2.0.7 ([4248fec](https://github.com/Protelis/Protelis/commit/4248fec093a0e4b540b457b1a7432cf8194a90ab))
* **deps:** update plugin publishoncentral to v2.0.8 ([efc330e](https://github.com/Protelis/Protelis/commit/efc330e06552a8dc3f39c639826ca1406e77c285))
* **deps:** update plugin publishoncentral to v2.0.9 ([9decf17](https://github.com/Protelis/Protelis/commit/9decf174557947fb9f94155c7c7d85feff43da73))


### General maintenance

* **interpreter:** fix typo in comment ([8a638a1](https://github.com/Protelis/Protelis/commit/8a638a1d7658656f8ac1c78b1577c999e4f3a8db))


### Style improvements

* **interpreter:** fix indentation in AlignedMap ([c1a24f5](https://github.com/Protelis/Protelis/commit/c1a24f5515bc7252d0f280466190186b25f66a33))
* **interpreter:** fix indentation in AlignedMap ([ec8e156](https://github.com/Protelis/Protelis/commit/ec8e15630cf466f65c026d0a457dcf9b61e465a8))


### Performance improvements

* **interpreter:** make the reified sub-field in AlignedMap a constant (no need for a variable to resolve) ([25f283d](https://github.com/Protelis/Protelis/commit/25f283d7689ddd565abdfa4c237b0a630f2abaab))
* **interpreter:** provide a faster implementation of ArrayTupleImpl.filter ([e16476a](https://github.com/Protelis/Protelis/commit/e16476a5cc4b181f39fd2287b8bf93fdc99ef3f5))
* **interpreter:** provide a faster implementation of ArrayTupleImpl.fold ([b019df3](https://github.com/Protelis/Protelis/commit/b019df3006615900b9bb4df89c44bfc2b27fa7c4))
* **interpreter:** provide a faster implementation of ArrayTupleImpl.reduce ([eb71080](https://github.com/Protelis/Protelis/commit/eb710806a9d00d9deb3c63edf0d8ae3f2be3afe4))
* **interpreter:** simplify ArrayTupleImpl.map ([2608a06](https://github.com/Protelis/Protelis/commit/2608a0655f35a9b5e6f7aaff1bcb1708257e8608))

## [16.4.0](https://github.com/Protelis/Protelis/compare/16.3.0...16.4.0) (2022-07-07)


### Features

* **build:** switch to the Kotlin compiler/plugin ([909fac6](https://github.com/Protelis/Protelis/commit/909fac68c0aa5b922895a424e754b886f911fa61))


### Bug Fixes

* **release:** run protelisdoc before attempting to publish the protelis-lang docs ([e9a7917](https://github.com/Protelis/Protelis/commit/e9a79175183b5093d07c1255c7faf834d3ff2314))


### Dependency updates

* **deps:** drop guice, fixes [#572](https://github.com/Protelis/Protelis/issues/572) ([125aa93](https://github.com/Protelis/Protelis/commit/125aa938737c2cadcf3d72442fee6dc5d3420b86))


### General maintenance

* **hooks:** run ktlintCheck before approving commits ([c134a2d](https://github.com/Protelis/Protelis/commit/c134a2df5139881ab30dbfd708a4865a7e83e132))

## [16.3.0](https://github.com/Protelis/Protelis/compare/16.2.0...16.3.0) (2022-07-07)


### Features

* **release:** publish Kotlin publications in place of Java's ([ded7209](https://github.com/Protelis/Protelis/commit/ded7209196ee782b6c1857f5af238d52b432ff2f))

## [16.2.0](https://github.com/Protelis/Protelis/compare/16.1.2...16.2.0) (2022-07-07)


### Bug Fixes

* **release:** update the CI infrastructure for publish-on-central 2.x.x ([#722](https://github.com/Protelis/Protelis/issues/722)) ([8d43f48](https://github.com/Protelis/Protelis/commit/8d43f48205b5041673cab687852a25c323c956ce))


### Build and continuous integration

* **deps:** update danysk/build-check-deploy-gradle-action action to v2.0.3 ([e11bac0](https://github.com/Protelis/Protelis/commit/e11bac0ad2fe4aaae692574bff0335511fadb52e))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.1.0 ([bc2ff52](https://github.com/Protelis/Protelis/commit/bc2ff5214977e2733983f0b68da8594d354c552d))


### Dependency updates

* **api-deps:** update dependency org.protelis:protelis.parser to v10.2.14 ([92abf31](https://github.com/Protelis/Protelis/commit/92abf316489fc4cff513bdc4c437832d9638cf84))
* **api-deps:** update dependency org.protelis:protelis.parser to v10.2.15 ([5815b60](https://github.com/Protelis/Protelis/commit/5815b607e25f3ed138783e08a62d8eec996345e9))
* **deps:** update dependency com.github.spotbugs:spotbugs-annotations to v4.7.1 ([7bdb059](https://github.com/Protelis/Protelis/commit/7bdb0594f9eee7a45e55cd5378359396384cc3a3))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.148 ([d9d4125](https://github.com/Protelis/Protelis/commit/d9d412515f1d500940ffd31c6e37ece052779987))
* **deps:** update dependency io.github.classgraph:classgraph to v4.8.149 ([85bf967](https://github.com/Protelis/Protelis/commit/85bf967e0e0d221ed9315c5456c007a7fc5d0dad))
* **deps:** update dependency org.jetbrains.kotlin.jvm to v1.7.10 ([3339045](https://github.com/Protelis/Protelis/commit/3339045d0604ce9e91a8f226ef5ed71f829498ce))
* **deps:** update plugin multijvmtesting to v0.4.4 ([110b282](https://github.com/Protelis/Protelis/commit/110b28289df298f31f02566366bdd99ce90694d3))
* **deps:** update plugin multijvmtesting to v0.4.5 ([aec7581](https://github.com/Protelis/Protelis/commit/aec7581cbe83a9fc764e6dacb9928a6208341f94))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.14 ([6747ae0](https://github.com/Protelis/Protelis/commit/6747ae00a5258c0ed298ff55c0449ab7ddb942c9))
* **deps:** update plugin publishoncentral to v0.8.3 ([ca84bc0](https://github.com/Protelis/Protelis/commit/ca84bc0d91060cca0942153b0c643e5b3080de12))
* **deps:** update plugin publishoncentral to v1 ([7918bc7](https://github.com/Protelis/Protelis/commit/7918bc77dc790e528246691fc69193c42cd51051))
* **deps:** update plugin publishoncentral to v2 ([b2e881a](https://github.com/Protelis/Protelis/commit/b2e881adc94b6467d413fd5de8b426dd8a641363))
* **deps:** update plugin publishoncentral to v2.0.2 ([cfe972d](https://github.com/Protelis/Protelis/commit/cfe972d7cc9a779cb79384b177d60f95f230cda5))
* **deps:** update plugin publishoncentral to v2.0.3 ([b93deb3](https://github.com/Protelis/Protelis/commit/b93deb387a01abfe5518674f8c4bbd1a2d23088b))

## [16.1.2](https://github.com/Protelis/Protelis/compare/16.1.1...16.1.2) (2022-06-11)


### Build and continuous integration

* **deps:** update danysk/build-check-deploy-gradle-action action to v1.2.16 ([1f388f6](https://github.com/Protelis/Protelis/commit/1f388f6d5b4dfc2d4a43dc3a5e7442ad986a247d))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2 ([5cb45b1](https://github.com/Protelis/Protelis/commit/5cb45b1983873e20fa6f065227f55e3c468465e8))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.0.1 ([415f3c3](https://github.com/Protelis/Protelis/commit/415f3c328bb9d9cf65017d424c19e2417a6df765))
* **deps:** update danysk/build-check-deploy-gradle-action action to v2.0.2 ([0b1d112](https://github.com/Protelis/Protelis/commit/0b1d1128db44e7ae281f3a867191c90d1f71dee2))


### Dependency updates

* **deps:** update dependency io.github.classgraph:classgraph to v4.8.147 ([2ab42eb](https://github.com/Protelis/Protelis/commit/2ab42ebf68eab66c7838dc15713bdf6867adc178))
* **deps:** update dependency org.jetbrains.kotlin.jvm to v1.7.0 ([c81f696](https://github.com/Protelis/Protelis/commit/c81f696f026d04b8d82b8285b96a5e069e6df056))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.7 ([1f45d59](https://github.com/Protelis/Protelis/commit/1f45d59ad74761c6091752443fd7337902fdd35b))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.8 ([5fa738b](https://github.com/Protelis/Protelis/commit/5fa738b8a88a862fb9b1e7e8ea4adb449f7758a2))
* **deps:** update plugin com.gradle.enterprise to v3.10.2 ([a33c9fa](https://github.com/Protelis/Protelis/commit/a33c9fa0e536fd50aec926f659ffd68cb721634a))
* **deps:** update plugin kotlin-qa to v0.19.0 ([bfdcdeb](https://github.com/Protelis/Protelis/commit/bfdcdeb7386db6960b52a85c9a37ff9dbb32ffed))
* **deps:** update plugin kotlin-qa to v0.19.1 ([4e8cdc5](https://github.com/Protelis/Protelis/commit/4e8cdc5978f004ec3124750068ac3ec41329d657))
* **deps:** update plugin multijvmtesting to v0.4.1 ([dfc2c09](https://github.com/Protelis/Protelis/commit/dfc2c092f3cc13c64fdf9ba910b48749014cc803))
* **deps:** update plugin multijvmtesting to v0.4.2 ([eb961de](https://github.com/Protelis/Protelis/commit/eb961deb0ff19d8de792735efb060a0741aadcb1))
* **deps:** update plugin multijvmtesting to v0.4.3 ([248e10c](https://github.com/Protelis/Protelis/commit/248e10ce21415a4811be0314c23bca1cdb2e96a1))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.11 ([f4d6fe8](https://github.com/Protelis/Protelis/commit/f4d6fe8f8b7327245f4cd99e8d7873cc4ae09698))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v1.0.12 ([42a554f](https://github.com/Protelis/Protelis/commit/42a554f7fb3676cc075e36daf55de7a7b5670b31))
* **deps:** update plugin publishoncentral to v0.8.0 ([c31930a](https://github.com/Protelis/Protelis/commit/c31930a68b15b17f450d6037ab5eb202f47234f5))
* **deps:** update plugin publishoncentral to v0.8.2 ([1420e48](https://github.com/Protelis/Protelis/commit/1420e48e8d360e1986f4277b9891c2b7f01a544e))


### Documentation

* **deps:** update plugin protelisdoc to v3.0.8 ([b56eb17](https://github.com/Protelis/Protelis/commit/b56eb17f9415dc590bb0889b1d0f068f88f80b38))

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
