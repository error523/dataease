#!/bin/bash

java -jar -DrootPath="/opt/dataease/" -DkettlePath="/opt/dataease/kettle/" -DrootLogFile="/opt/dataease/dataease.properties" -Dspring.profiles.active=whole /opt/apps/backend-${APP_VERSION}.jar