import org.gradle.api.internal.HasConvention
import org.jetbrains.grammarkit.tasks.GenerateLexer
import org.jetbrains.grammarkit.tasks.GenerateParser
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ideaVersion = "2018.3.3" //prop("ideaVersion")


repositories {
    mavenCentral()
    maven("https://dl.bintray.com/jetbrains/markdown")
    maven("https://plugins.gradle.org/m2/")
}

plugins {
    idea
    kotlin("jvm") version "1.3.30"
    id("org.jetbrains.intellij") version "0.4.7"
    id("org.jetbrains.grammarkit") version "2018.3"
    id("de.undercouch.download") version "3.4.3"
    id("net.saliman.properties") version "1.4.6"
    id("com.palantir.git-version") version "0.11.0"
}

idea {
    module {
        generatedSourceDirs.add(file("src/main/gen"))
    }
}

apply {
    plugin("idea")
    plugin("kotlin")
    plugin("org.jetbrains.grammarkit")
    plugin("org.jetbrains.intellij")
}

group = "com.lfrobeen"
version = "1.1.0"

intellij {
    pluginName = "intellij-datalog"

    version = ideaVersion
    updateSinceUntilBuild = false
    instrumentCode = false
}


val generateLexer = task<GenerateLexer>("generateLexer") {
    source = "src/main/grammars/datalog.flex"
    targetDir = "src/main/gen/com/lfrobeen/datalog/lang/lexer"
    targetClass = "DatalogLexer"
    purgeOldFiles = true
}

val generateParser = task<GenerateParser>("generateParser") {
    source = "src/main/grammars/datalog.bnf"
    targetRoot = "src/main/gen"
    pathToParser = "/datalog/lang/parser/DatalogParser.java"
    pathToPsiRoot = "/datalog/lang/psi"
    purgeOldFiles = true
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        languageVersion = "1.3"
        apiVersion = "1.3"
        freeCompilerArgs = listOf("-Xjvm-default=enable")
    }

    dependsOn(
        generateLexer,
        generateParser
    )
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        languageVersion = "1.3"
        apiVersion = "1.3"
        freeCompilerArgs = listOf("-Xjvm-default=enable")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

sourceSets {
    main {
        java.srcDirs("src/main/gen")
        kotlin.srcDirs("src/main/kotlin")
        resources.srcDirs("src/main/resources")
    }
    test {
        kotlin.srcDirs("src/test/kotlin")
        resources.srcDirs("src/test/resources")
    }
}

val SourceSet.kotlin: SourceDirectorySet
    get() =
        (this as HasConvention)
            .convention
            .getPlugin(KotlinSourceSet::class.java)
            .kotlin
