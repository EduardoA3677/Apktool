import java.io.ByteArrayOutputStream

val version = "3.0.0"
val suffix = "SNAPSHOT"

// Strings embedded into the build.
var gitRevision by extra("")
var apktoolVersion by extra("")

defaultTasks("build", "shadowJar", "proguard")

// Functions
val gitDescribe: String? by lazy {
    try {
        val process = ProcessBuilder("git", "describe", "--tags", "--match", "v*", "--exclude", "*SNAPSHOT*")
            .directory(rootProject.projectDir)
            .start()
        val output = process.inputStream.bufferedReader().readText().trim()
        process.waitFor()
        if (process.exitValue() == 0) output.replace("-g", "-") else null
    } catch (e: Exception) {
        null
    }
}

val gitBranch: String? by lazy {
    try {
        val process = ProcessBuilder("git", "rev-parse", "--abbrev-ref", "HEAD")
            .directory(rootProject.projectDir)
            .start()
        val output = process.inputStream.bufferedReader().readText().trim()
        process.waitFor()
        if (process.exitValue() == 0) output else null
    } catch (e: Exception) {
        null
    }
}

if ("release" !in gradle.startParameter.taskNames) {
    val hash = gitDescribe

    if (hash == null) {
        gitRevision = "dirty"
        apktoolVersion = "$version-$suffix"
        project.logger.lifecycle("Building SNAPSHOT (no .git folder found)")
    } else {
        gitRevision = hash
        apktoolVersion = "$hash-SNAPSHOT"
        project.logger.lifecycle("Building SNAPSHOT ($gitBranch): $gitRevision")
    }
} else {
    gitRevision = ""
    apktoolVersion = if (suffix.isNotEmpty()) "$version-$suffix" else version;
    project.logger.lifecycle("Building RELEASE ($gitBranch): $apktoolVersion")
}

plugins {
    `java-library`
    if (JavaVersion.current().isJava11Compatible) {
        alias(libs.plugins.vanniktech.maven.publish) apply false
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    val mavenProjects = arrayOf(
        "brut.j.common", "brut.j.util", "brut.j.dir", "brut.j.xml", "brut.j.yaml",
        "apktool-lib", "apktool-cli"
    )

    if (project.name in mavenProjects && JavaVersion.current().isJava11Compatible) {
        apply(from = "${rootProject.projectDir}/gradle/scripts/publishing.gradle")
    }
}

tasks.register("release") {
    // Used for official releases.
}

tasks.register("printVersion") {
    doLast {
        println(apktoolVersion)
    }
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-Xlint:-options")
    options.compilerArgs.add("--release 8")

    options.encoding = "UTF-8"
}
