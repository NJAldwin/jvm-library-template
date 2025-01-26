# {{ cookiecutter.subproject1 }}

{{ cookiecutter.subproject1_readme_desc }}

[![Maven Central Version](https://img.shields.io/maven-central/v/{{ cookiecutter.maven_group }}/{{ cookiecutter.subproject1 }})](https://central.sonatype.com/artifact/{{ cookiecutter.maven_group }}/{{ cookiecutter.subproject1 }})
[![javadoc](https://javadoc.io/badge2/{{ cookiecutter.maven_group }}/{{ cookiecutter.subproject1 }}/javadoc.svg)](https://javadoc.io/doc/{{ cookiecutter.maven_group }}/{{ cookiecutter.subproject1 }})

https://central.sonatype.com/artifact/{{ cookiecutter.maven_group }}/{{ cookiecutter.subproject1 }}

Docs (javadoc.io): https://javadoc.io/doc/{{ cookiecutter.maven_group }}/{{ cookiecutter.subproject1 }}

Docs (GH pages): https://{{ cookiecutter.author_gh_user | lower }}.github.io/{{ cookiecutter.repo_gh_name }}/

```maven
<dependency>
    <groupId>{{ cookiecutter.maven_group }}</groupId>
    <artifactId>{{ cookiecutter.subproject1 }}</artifactId>
    <version>VERSION</version>
</dependency>
```

```gradle
implementation("{{ cookiecutter.maven_group }}:{{ cookiecutter.subproject1 }}:VERSION")
```
