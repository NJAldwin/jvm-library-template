pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "root"

fun includeProject(
    projectDirName: String,
    projectName: String,
) {
    val baseDir = File(settingsDir, projectDirName)
    val projectDir = File(baseDir, projectName)
    val buildFileName = "build.gradle.kts"

    assert(projectDir.isDirectory())
    assert(File(projectDir, buildFileName).isFile())

    include(projectName)
    project(":$projectName").projectDir = projectDir
    project(":$projectName").buildFileName = buildFileName
}

listOf("subprojects").forEach { dirName ->
    val subdir = File(rootDir, dirName)
    subdir.walkTopDown().maxDepth(1).forEach { dir ->
        val buildFile = File(dir, "build.gradle.kts")
        if (buildFile.exists()) {
            includeProject(dirName, dir.name)
        }
    }
}
