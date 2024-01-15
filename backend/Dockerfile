FROM bellsoft/liberica-runtime-container:jdk-17-musl AS builder

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY src ./src

RUN apk add --no-cache curl unzip \
    && curl -L https://services.gradle.org/distributions/gradle-7.3-bin.zip -o gradle.zip \
    && unzip gradle.zip \
    && rm gradle.zip

ENV PATH="/app/gradle-7.3/bin:${PATH}"

RUN gradle build

FROM bellsoft/liberica-runtime-container:jdk-17-musl

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
