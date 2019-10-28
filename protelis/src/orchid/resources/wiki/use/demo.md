---

---
# Protelis Demo projects

In order to exemplify the use of Protelis in a stand-alone project, [we provide a demo application](https://github.com/Protelis/Protelis-Demo).
The demo application shows how Protelis can be used in your project, namely, how to invoke Protelis code from Java or Kotlin.

## Importing Protelis in your own project

Protelis is distributed on [Maven Central](https://search.maven.org/artifact/org.protelis/protelis/).
The best way to import it is through a dependency manager / build system for Java.
We warmly recommend Gradle, but Apache Maven, Apache Ivy, Scala SBT, and other tools work as well.
The main advantage in using a dependency management system is that it will take care of downloading all transitive dependencies.
In fact, Protelis depends on a number of libraries that need to be in your classpath at runtime for the interpreter and parser to work.

### Importing in Gradle

In your dependency section, just import Protelis as:

```kotlin
dependencies {
    implementation("org.protelis:protelis:{{ site.version }}")
}
```

### Importing in Maven

Include Protelis among your dependencies, with:

```xml
<dependency>
  <groupId>org.protelis</groupId>
  <artifactId>protelis</artifactId>
  <version>{{ site.version }}</version>
</dependency>
``` 

### Importing in Scala SBT

Declare Protelis as a dependency by:

```scala
libraryDependencies += "org.protelis" % "protelis" % "{{ site.version }}"
```

### Importing manually

If you really cannot use a dependency manager, you can still import Protelis in your project "the old way",
by importing the Protelis "Fat Jar" in your classpath.
This mechanism is way more fragile than going through a dependency manager however
(for instance, different versions of a library may get in the classpath, and,
depending on the order in which classes are loaded, the system may or may not work as expected).
You can obtain the latest fatjar in [the Protelis release page on GitHub](https://github.com/Protelis/Protelis/releases/latest).


