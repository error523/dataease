#!/bin/bash

java -jar -DrootPath="/opt/dataease/" -DrootLogFile="/opt/dataease/dataease.properties" -Dspring.profiles.active=whole /opt/dataease/backend-${APP_VERSION}.jar