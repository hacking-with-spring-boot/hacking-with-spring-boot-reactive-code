#!/bin/bash

pushd `dirname $1`
./mvnw clean package
popd
