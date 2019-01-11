#!/bin/bash

find . -name pom.xml -exec ./build.bash {} \; -print
