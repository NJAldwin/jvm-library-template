# Template for JVM Library

This project is a `cookiecutter` template for creating a Gradle project that can be published to Maven Central.

It will generate a project with two subprojects and some placeholder code.  Once properly configured, the project will auto-deploy releases to Maven Central, as well as building and deploying docs to GitHub Pages.

For more information on the generated project, refer to https://github.com/NJAldwin/maven-central-test

## Prereqs

Requires `cookiecutter`.  Install with `pipx`.  See [pipx installation instructions](https://github.com/pypa/pipx?tab=readme-ov-file#install-pipx).
```console
pipx install cookiecutter
```

## Usage

```console
cookiecutter gh:NJAldwin/jvm-library-template
```

Then follow the instructions in the generated README.

## This Template

This template is based on https://github.com/NJAldwin/maven-central-test -- for more information, refer to that repo.
