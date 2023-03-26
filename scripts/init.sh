#!/bin/sh

mvn clean compile package
ret=$?
if [ $ret -ne 0 ]; then
exit $ret
fi

java -jar target/parking-lot-1.0.0-SNAPSHOT.jar
