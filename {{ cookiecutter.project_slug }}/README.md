# {{ cookiecutter.repo_gh_name }}

{{ cookiecutter.readme_desc }}

[![Maven Central Version](https://img.shields.io/maven-central/v/{{ cookiecutter.maven_group }}/{{ cookiecutter.subproject1 }})](https://central.sonatype.com/artifact/{{ cookiecutter.maven_group }}/{{ cookiecutter.subproject1 }})
[![javadoc](https://javadoc.io/badge2/{{ cookiecutter.maven_group }}/{{ cookiecutter.subproject1 }}/javadoc.svg)](https://javadoc.io/doc/{{ cookiecutter.maven_group }}/{{ cookiecutter.subproject1 }})

https://central.sonatype.com/artifact/{{ cookiecutter.maven_group }}/{{ cookiecutter.subproject1 }}

https://central.sonatype.com/artifact/{{ cookiecutter.maven_group }}/{{ cookiecutter.subproject2 }}

Docs (javadoc.io):
 - https://javadoc.io/doc/{{ cookiecutter.maven_group }}/{{ cookiecutter.subproject1 }}
 - https://javadoc.io/doc/{{ cookiecutter.maven_group }}/{{ cookiecutter.subproject2 }}

Docs (GH pages): https://{{ cookiecutter.author_gh_user | lower }}.github.io/{{ cookiecutter.repo_gh_name }}/

```maven
<dependency>
    <groupId>{{ cookiecutter.maven_group }}</groupId>
    <artifactId>{{ cookiecutter.subproject1 }}</artifactId>
    <version>VERSION</version>
</dependency>
<dependency>
    <groupId>{{ cookiecutter.maven_group }}</groupId>
    <artifactId>{{ cookiecutter.subproject2 }}</artifactId>
    <version>VERSION</version>
</dependency>
```

```gradle
implementation("{{ cookiecutter.maven_group }}:{{ cookiecutter.subproject1 }}:VERSION")
implementation("{{ cookiecutter.maven_group }}:{{ cookiecutter.subproject2 }}:VERSION")
```

## Building

Use `./gradlew` to build the project.

e.g.

```console
./gradlew --stacktrace clean ktlintFormat build dependencyUpdates ktlintCheck test assemble publish makeDocs
```

## Repository/Publishing Setup

- set up and publish a GPG key
- add GPG key information to GitHub (`Secrets and variables -> Actions`) in `JRELEASER_GPG_PASSPHRASE`, `JRELEASER_GPG_PUBLIC_KEY`, and `JRELEASER_GPG_SECRET_KEY`
- set up a Maven Central account
- perform DNS TXT verification in Maven Central for the group ID's domain
- create a token in Maven Central
- add the Maven credentials to GitHub (`Secrets and variables -> Actions`) in `JRELEASER_MAVENCENTRAL_TOKEN` and `JRELEASER_MAVENCENTRAL_USERNAME`
- set up the `gh-pages` branch for github-pages (`Environments`)

## Publishing

To publish a release, update the version number in `build.gradle.kts`, then create a new version tag pointing to the latest commit in `master`.  Tags must be in [semver](https://semver.org/) format with a `v` prefix (i.e. `vMAJOR.MINOR.PATCH`).

The GitHub action will automatically build and publish the release to Maven Central and GitHub Pages, then create a GitHub Release.

Tags with a prerelease version (e.g. `-alpha.1`) will be marked as prereleases in GitHub and will not be linked from the `/stable` link in the docs.

## Etc

Based on https://github.com/NJAldwin/maven-central-test
