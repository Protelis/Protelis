---

---
# Prerequisites

Protelis is a language hosted on the Java Virtual Machine, as such, it requires the Java JDK, version 8 or above.
We recommend using either OpenJDK or OpenJ9 (both are supported), you can find them on [the AdoptOpenJDK website](https://adoptopenjdk.net/).

In order to make sure you installed Java correctly, fire up a terminal and type: `java -version`, you should see something like:

```
java -version
openjdk version "1.8.0_222"
OpenJDK Runtime Environment (build 1.8.0_222-b05)
OpenJDK 64-Bit Server VM (build 25.222-b05, mixed mode)
```

Make sure the version is 1.8 or above.
If the version is older than that, please update your Java installation before proceeding.
You should also check `javac`, by launching `javac -version`. You should see something similar to:
```
javac 1.8.0_222
```

Once Java is in place, we can start!

# Eclipse plugin

In order to ease the writing of Protelis code, we provide [an Eclipse plugin for Protelis](https://marketplace.eclipse.org/content/protelis).
Four versions are available:

* Stable version
* Development version (unstable)
* Legacy version, with support for Java 7 (abandoned)
* Legacy development version, with support for Java 7 (abandoned and unstable)

Of course, we recommend the former option, unless you want to develop the language rather than *in* the language.
The plugin can be installed by the Eclipse Marketplace directly, by searching for Protelis.

Support for other IDEs is ongoing, at the moment however Protelis script are treated as plain text by most of the IDEs.
