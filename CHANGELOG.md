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
