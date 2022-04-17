import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ideaVersion = "2021.3.1" //prop("ideaVersion")

group = "com.lfrobeen"
version = "1.1.0"

plugins {
    idea
    kotlin("jvm") version "1.6.20"
    id("org.jetbrains.intellij") version "1.5.2"
    id("org.jetbrains.grammarkit") version "2021.2.2"
    id("de.undercouch.download") version "3.4.3"
    id("net.saliman.properties") version "1.4.6"
    id("com.palantir.git-version") version "0.11.0"
}


apply {
    plugin("idea")
    plugin("kotlin")
    plugin("org.jetbrains.grammarkit")
    plugin("org.jetbrains.intellij")
}

repositories {
    mavenCentral()
    maven("https://dl.bintray.com/jetbrains/markdown")
    maven("https://plugins.gradle.org/m2/")
}

idea {
    module {
        generatedSourceDirs.add(file("src/main/gen"))
    }
}

intellij {
    pluginName.set("intellij-datalog")
    version.set(ideaVersion)
    updateSinceUntilBuild.set(false)
    instrumentCode.set(false)
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

sourceSets {
    main {
        java.srcDirs("src/main/gen")
        resources.srcDirs("src/main/resources")
    }
    test {
        resources.srcDirs("src/test/resources")
    }
}

kotlin {
    sourceSets {
        main {
            kotlin.srcDirs("src/main/kotlin")
        }
        test {
            kotlin.srcDirs("src/test/kotlin")
        }
    }
}

val generateDatalogLexer = task<GenerateLexerTask>("generateDatalogLexer") {
    source.set("src/main/grammars/datalog.flex")
    targetDir.set("src/main/gen/com/lfrobeen/datalog/lang/lexer")
    targetClass.set("DatalogLexer")
    purgeOldFiles.set(true)
}

val generateDatalogParser = task<GenerateParserTask>("generateDatalogParser") {
    source.set("src/main/grammars/datalog.bnf")
    targetRoot.set("src/main/gen")
    pathToParser.set("/datalog/lang/parser/DatalogParser.java")
    pathToPsiRoot.set("/datalog/lang/psi")
    purgeOldFiles.set(true)
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
        languageVersion = "1.6"
        apiVersion = "1.5"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }

    dependsOn(
        generateDatalogLexer,
        generateDatalogParser
    )
}


tasks.withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}