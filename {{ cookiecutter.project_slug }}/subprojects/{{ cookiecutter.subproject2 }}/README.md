# {{ cookiecutter.subproject2 }}

{{ cookiecutter.subproject2_readme_desc }}

[![Maven Central Version](https://img.shields.io/maven-central/v/{{ cookiecutter.maven_group }}/{{ cookiecutter.subproject2 }})](https://central.sonatype.com/artifact/{{ cookiecutter.maven_group }}/{{ cookiecutter.subproject2 }})
[![javadoc](https://javadoc.io/badge2/{{ cookiecutter.maven_group }}/{{ cookiecutter.subproject2 }}/javadoc.svg)](https://javadoc.io/doc/{{ cookiecutter.maven_group }}/{{ cookiecutter.subproject2 }})

https://central.sonatype.com/artifact/{{ cookiecutter.maven_group }}/{{ cookiecutter.subproject2 }}

Docs (javadoc.io): https://javadoc.io/doc/{{ cookiecutter.maven_group }}/{{ cookiecutter.subproject2 }}

Docs (GH pages): https://{{ cookiecutter.author_gh_user | lower }}.github.io/{{ cookiecutter.repo_gh_name }}/

```maven
<dependency>
    <groupId>{{ cookiecutter.maven_group }}</groupId>
    <artifactId>{{ cookiecutter.subproject2 }}</artifactId>
    <version>VERSION</version>
</dependency>
```

```gradle
implementation("{{ cookiecutter.maven_group }}:{{ cookiecutter.subproject2 }}:VERSION")
```
