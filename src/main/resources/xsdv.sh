#!/bin/bash
# call xsdv
java -jar /usr/lib/${project.artifactId}/${project.build.finalName}.jar "$@"
