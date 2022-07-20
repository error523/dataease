FROM ghcr.io/graalvm/graalvm-ce:latest

ARG IMAGE_TAG

RUN mkdir -p /opt/apps /opt/dataease/data/feature/full /opt/dataease/drivers

ADD ./run-java.sh /opt/dataease/

ADD ./rootPath/dataease.properties /opt/dataease/

ADD mapFiles/* /opt/dataease/data/feature/full/

ADD drivers/* /opt/dataease/drivers/

ADD backend/target/backend-$IMAGE_TAG.jar /opt/apps

ENV JAVA_APP_JAR=/opt/apps/backend-$IMAGE_TAG.jar

ENV AB_OFF=true

ENV JAVA_OPTIONS="-Dfile.encoding=utf-8"

ENV APP_VERSION=$IMAGE_TAG

HEALTHCHECK --interval=15s --timeout=5s --retries=20 --start-period=30s CMD curl -f 127.0.0.1:8081

CMD ["/opt/dataease/run-java.sh"]
