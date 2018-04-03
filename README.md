# Maven Project Template

# Table of Contents
1. Overview
2. Using this template

## Overview

This is the Maven Project Template providing the most common configuration for open source projects building in Travis CI and publishing to JFrog Bintray.

## Using this template

Use this template by replacing the parts to fit the needs of your project:

1. Copy all files to your project
2. Do an `git update-index --chmod=+x etc/before.sh` and commit
3. Replace this `README.md` with a documentation about your project
4. Check the `LICENSE`  and replace the file if needed.
5. Check the `pom.xml` and replace
    - Maven coordinates `artifactId` and `version`
    - License information if needed
    - Developers
    - Package in `project.distributionManagement.repository.url`
    - Dependencies