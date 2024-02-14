FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

#RUN ./mvnw dependency:go-offline
RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency

RUN addgroup --system appuser && adduser -S -s /usr/sbin/nologin -G appuser appuser

COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

RUN chown -R appuser:appuser /app
USER appuser

ENTRYPOINT ["java","-cp","app:app/lib/*","checkoutapi.CheckoutApiApplication"]
