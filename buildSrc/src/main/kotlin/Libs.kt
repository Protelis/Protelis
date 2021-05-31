/*
 * Copyright (C) 2021, Danilo Pianini and contributors listed in the project's build.gradle.kts or pom.xml file.
 *
 * This file is part of Protelis, and is distributed under the terms of the GNU General Public License,
 * with a linking exception, as described in the file LICENSE.txt in this project's top directory.
 */

import kotlin.String

fun modularizedLibrary(base: String, module: String = "", separator: String = "-") = when {
    module.isEmpty() -> base
    else -> base + separator + module
} + ":_"

fun pmdModule(module: String = "") = modularizedLibrary("net.sourceforge.pmd:pmd", module)

object Libs {


    /**
     * https://spotbugs.github.io/
     */
    const val spotbugs_annotations: String = "com.github.spotbugs:spotbugs-annotations:_"

    /**
     * https://github.com/DanySK/alchemist-interfaces
     */
    const val alchemist_interfaces: String = "it.unibo.alchemist:alchemist-interfaces:_"

    /**
     * https://github.com/DanySK/alchemist-loading
     */
    const val alchemist_loading: String = "it.unibo.alchemist:alchemist-loading:_"

    /**
     * http://logback.qos.ch
     */
    const val logback_classic: String = "ch.qos.logback:logback-classic:_"

    /**
     * http://protelis.org
     */
    const val protelis_parser: String = "org.protelis:protelis.parser:_"

    /**
     * https://commons.apache.org/proper/commons-codec/
     */
    const val commons_codec: String = "commons-codec:commons-codec:_"
    
    /**
     * http://commons.apache.org/proper/commons-lang/
     */
    const val commons_lang3: String = "org.apache.commons:commons-lang3:_"

    /**
     * http://commons.apache.org/proper/commons-math/
     */
    const val commons_math3: String = "org.apache.commons:commons-math3:_"

    /**
     * https://github.com/classgraph/classgraph
     */
    const val classgraph: String = "io.github.classgraph:classgraph:_"

    /**
     * http://commons.apache.org/proper/commons-io/
     */
    const val commons_io: String = "commons-io:commons-io:_"

    /**
     * http://www.slf4j.org
     */
    const val slf4j_api: String = "org.slf4j:slf4j-api:_"

    /**
     * http://trove4j.sf.net
     */
    const val trove4j: String = "net.sf.trove4j:trove4j:_"

    /**
     * http://www.jboss.org/apiviz/
     */
    const val apiviz: String = "org.jboss.apiviz:apiviz:_"

    /**
     * https://github.com/pinterest/ktlint
     */
    const val ktlint: String = "com.pinterest:ktlint:_"

    /**
     * https://github.com/google/guava
     */
    const val guava: String = "com.google.guava:guava:_"

    /**
     * http://junit.org
     */
    const val junit: String = "junit:junit:_"

    /**
     * http://ruedigermoeller.github.io/fast-serialization/
     */
    const val fst: String = "de.ruedigermoeller:fst:_"
}
