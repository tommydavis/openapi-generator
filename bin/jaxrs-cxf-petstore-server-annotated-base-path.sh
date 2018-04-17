#!/bin/sh

SCRIPT="$0"

while [ -h "$SCRIPT" ] ; do
  ls=`ls -ld "$SCRIPT"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    SCRIPT="$link"
  else
    SCRIPT=`dirname "$SCRIPT"`/"$link"
  fi
done

if [ ! -d "${APP_DIR}" ]; then
  APP_DIR=`dirname "$SCRIPT"`/..
  APP_DIR=`cd "${APP_DIR}"; pwd`
fi

executable="./modules/openapi-generator-cli/target/openapi-generator-cli.jar"

if [ ! -f "$executable" ]
then
  mvn clean package
fi

# if you've executed sbt assembly previously it will use that instead.
export JAVA_OPTS="${JAVA_OPTS} -XX:MaxPermSize=256M -Xmx1024M -DloggerPath=conf/log4j.properties"
ags="$@ generate --artifact-id swagger-cxf-annotated-basepath -t modules/openapi-generator/src/main/resources/JavaJaxRS/cxf -i modules/openapi-generator/src/test/resources/2_0/petstore.yaml -l jaxrs-cxf -o samples/server/petstore/jaxrs-cxf-annotated-base-path -DhideGenerationTimestamp=true,useAnnotatedBasePath=true"

java $JAVA_OPTS -jar $executable $ags
