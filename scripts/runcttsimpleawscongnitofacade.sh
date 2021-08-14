#!/bin/bash
set -x
export APP_HOME='/home/devel1/IdeaProjects/cttsimpleawscongnitofacade'
export LOGS_HOME="${APP_HOME}/logs"
/usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -jar ${APP_HOME}/binaries/cttsimpleawscongnitofacade-1.0.0.jar server ${APP_HOME}/main-configuration.yml
set +x
