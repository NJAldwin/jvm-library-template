import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.github.benmanes.gradle.versions.updates.gradle.GradleReleaseChannel
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import org.jreleaser.model.Active
import java.net.URI

plugins {
    kotlin("jvm") version "1.9.25"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.2"
    id("com.github.ben-manes.versions") version "0.51.0"
    id("org.jreleaser") version "1.15.0"
    id("maven-publish")
    id("org.jetbrains.dokka") version "1.9.20"
}

buildscript {
    dependencies {
        classpath("org.jetbrains.dokka:dokka-base:1.9.20")
    }
}

group = "{{ cookiecutter.maven_group }}"
// after updating this, make sure to push a new git tag
// (would be nice to eventually automate)
version = "{{ cookiecutter.version }}"

val ghUser = "{{ cookiecutter.author_gh_user }}"
val ghRepo = "{{ cookiecutter.repo_gh_name }}"
val ghUrl = "https://github.com/$ghUser/$ghRepo"

val topDesc = "{{ cookiecutter.project_description }}"

allprojects {
    repositories {
        mavenCentral()
    }
}

// no top-level jar
tasks.named("jar") {
    enabled = false
}

tasks.withType<DependencyUpdatesTask> {
    gradleReleaseChannel = GradleReleaseChannel.CURRENT.id

    rejectVersionIf {
        listOf("alpha", "beta", "rc", "cr", "m", "eap", "pr", "dev").any {
            candidate.version.contains(it, ignoreCase = true)
        }
    }
}

tasks.named("ktlintCheck") {
    mustRunAfter("ktlintFormat")
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "org.jetbrains.dokka")

    kotlin {
        jvmToolchain(8)
        explicitApi()
    }

    tasks.withType<KotlinJvmCompile> {
        compilerOptions {
            allWarningsAsErrors.set(true)
        }
    }

    java {
        withJavadocJar()
        withSourcesJar()
    }

    // build javadoc jar via dokka
    tasks.named<Jar>("javadocJar") {
        dependsOn("dokkaJavadoc")
        from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    }
    tasks.dokkaJavadoc.configure {
        failOnWarning.set(true)
        dokkaSourceSets {
            configureEach {
                reportUndocumented.set(true)
                skipEmptyPackages.set(true)
                skipDeprecated.set(false)
                noStdlibLink.set(false)
                noJdkLink.set(false)
                jdkVersion.set(8)
                includes.from("Module.md")

                // (use externalDocumentationLink here to add links to e.g. kotlinx libs)

                sourceLink {
                    localDirectory = rootDir
                    remoteUrl = URI("$ghUrl/tree/v${project.version}").toURL()
                    remoteLineSuffix = "#L"
                }
            }
        }
    }

    // html for GH pages
    tasks.named<org.jetbrains.dokka.gradle.DokkaTaskPartial>("dokkaHtmlPartial").configure {
        failOnWarning.set(true)
        dokkaSourceSets {
            configureEach {
                reportUndocumented.set(true)
                skipEmptyPackages.set(true)
                skipDeprecated.set(false)
                noStdlibLink.set(false)
                noJdkLink.set(false)
                jdkVersion.set(8)
                includes.from("Module.md")

                // (use externalDocumentationLink here to add links to e.g. kotlinx libs)

                sourceLink {
                    localDirectory = rootDir
                    remoteUrl = URI("$ghUrl/tree/v${project.version}").toURL()
                    remoteLineSuffix = "#L"
                }
            }
        }

        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            footerMessage = "&copy; {{ cookiecutter.copyright_start_year }} {{ cookiecutter.author_name }}"
        }
    }

    version = rootProject.version
    tasks.withType<Jar> {
        archiveBaseName.set(project.name)
        archiveVersion.set(project.version.toString())
    }

    ktlint {
        verbose.set(true)
        outputToConsole.set(true)
        coloredOutput.set(true)
        reporters {
            reporter(ReporterType.PLAIN)
            reporter(ReporterType.CHECKSTYLE)
            reporter(ReporterType.JSON)
            reporter(ReporterType.HTML)
        }
    }

    tasks.test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

// match semver `x.y.z-something`
val isPrereleasePattern = """\d+\.\d+\.\d+-.+"""

jreleaser {
    dryrun.set(System.getenv("CI").isNullOrBlank())

    project {
        name.set("{{ cookiecutter.repo_gh_name }}")
        description.set(topDesc)
        version.set(rootProject.version.toString())
        authors.set(listOf("{{ cookiecutter.author_name }}"))
        license.set("MIT")
        inceptionYear.set("{{ cookiecutter.copyright_start_year }}")
        links {
            homepage = ghUrl
        }
    }

    release {
        github {
            repoOwner.set(ghUser)
            name.set(ghRepo)
            branch.set("master")

            // skip tag because we're running release on tag creation
            skipTag.set(true)
            prerelease {
                pattern.set(isPrereleasePattern)
            }
        }
    }

    signing {
        active.set(Active.ALWAYS)
        armored.set(true)
        verify.set(true)
    }

    deploy {
        maven {
            mavenCentral.create("sonatype") {
                active.set(Active.ALWAYS)
                url.set("https://central.sonatype.com/api/v1/publisher")
                subprojects.filter { it.plugins.hasPlugin("java") }.forEach { subproject ->
                    stagingRepositories.add("${subproject.layout.buildDirectory.get()}/staging-deploy")
                }
                applyMavenCentralRules.set(true)
                retryDelay.set(20)
                maxRetries.set(90)
            }
        }
    }

    distributions {
        subprojects.filter { it.plugins.hasPlugin("java") }.forEach { subproject ->
            create(subproject.name) {
                project {
                    description.set(
                        providers.provider {
                            subproject.description ?: topDesc
                        },
                    )
                }
                artifact {
                    path.set(subproject.tasks.named<Jar>("jar").get().archiveFile.get().asFile)
                }
                artifact {
                    path.set(subproject.tasks.named<Jar>("sourcesJar").get().archiveFile.get().asFile)
                    platform.set("java-sources")
                }
                artifact {
                    path.set(subproject.tasks.named<Jar>("javadocJar").get().archiveFile.get().asFile)
                    platform.set("java-docs")
                }
            }
        }
    }
}

subprojects {
    plugins.withId("java") {
        apply(plugin = "maven-publish")
        publishing {
            publications {
                create<MavenPublication>("maven") {
                    groupId = rootProject.group.toString()
                    artifactId = project.name
                    version = rootProject.version.toString()

                    from(components["java"])

                    pom {
                        name.set(project.name)
                        description.set(
                            providers.provider {
                                project.description ?: topDesc
                            },
                        )
                        url.set(rootProject.jreleaser.project.links.homepage)

                        inceptionYear.set(rootProject.jreleaser.project.inceptionYear.get())
                        licenses {
                            license {
                                name.set(rootProject.jreleaser.project.license.get())
                                url.set("https://opensource.org/licenses/${rootProject.jreleaser.project.license.get()}")
                            }
                        }
                        developers {
                            developer {
                                id.set(rootProject.jreleaser.release.github.repoOwner.get())
                                name.set(rootProject.jreleaser.project.authors.get().joinToString())
                            }
                        }
                        scm {
                            connection.set("scm:git:$ghUrl.git")
                            developerConnection.set("scm:git:ssh://github.com/$ghUser/$ghRepo.git")
                            url.set(ghUrl)
                        }
                    }
                }
            }

            repositories {
                maven {
                    url = layout.buildDirectory.dir("staging-deploy").get().asFile.toURI()
                }
            }
        }
    }
}

val docsDir = layout.buildDirectory.dir("docs").get().asFile
val versionedDocsDir = file("$docsDir/v${rootProject.version}")

tasks.dokkaHtmlMultiModule.configure {
    doFirst {
        versionedDocsDir.mkdirs()
    }
    outputDirectory.set(versionedDocsDir)
    moduleName.set("{{ cookiecutter.repo_gh_name }}")

    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        footerMessage = "&copy; {{ cookiecutter.copyright_start_year }} {{ cookiecutter.author_name }}"
    }
}

// master docs task, generates each submodule's docs and creates indices
// root -> /stable -> /vx.y.z (excluding prereleases)
// also:   /latest -> /vx.y.z-pre
tasks.register("makeDocs") {
    dependsOn("dokkaHtmlMultiModule")

    val latestDir = file("$docsDir/latest")
    val stableDir = file("$docsDir/stable")

    doFirst {
        versionedDocsDir.mkdirs()
    }

    doLast {
        if (!isPrereleasePattern.toRegex().matches(rootProject.version.toString())) {
            stableDir.mkdirs()
            file("$stableDir/index.html").writeText(
                """
                <html>
                <head>
                    <title>Latest Stable Documentation</title>
                    <meta http-equiv="cache-control" content="no-cache, no-store, must-revalidate" />
                    <meta http-equiv="refresh" content="0; URL=../v${rootProject.version}/" />
                </head>
                <body>
                    <p>If you are not redirected, click <a href="../v${rootProject.version}/">here</a> to access the latest stable documentation.</p>
                </body>
                </html>
                """.trimIndent(),
            )
        }

        latestDir.mkdirs()
        file("$latestDir/index.html").writeText(
            """
            <html>
            <head>
                <title>Latest Documentation</title>
                <meta http-equiv="cache-control" content="no-cache, no-store, must-revalidate" />
                <meta http-equiv="refresh" content="0; URL=../v${rootProject.version}/" />
            </head>
            <body>
                <p>If you are not redirected, click <a href="../v${rootProject.version}/">here</a> to access the latest documentation.</p>
            </body>
            </html>
            """.trimIndent(),
        )

        file("$docsDir/index.html").writeText(
            """
            <html>
            <head>
                <title>Documentation Index</title>
                <meta http-equiv="cache-control" content="no-cache, no-store, must-revalidate" />
                <meta http-equiv="refresh" content="0; URL=stable/" />
            </head>
            <body>
                <p>If you are not redirected, click <a href="stable/">here</a> to access the latest stable documentation.</p>
            </body>
            </html>
            """.trimIndent(),
        )
    }
}
