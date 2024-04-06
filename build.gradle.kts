plugins {
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.20"
}


repositories {
    mavenCentral()
}

group = "org.mai"
version = "0.1"

java.sourceCompatibility = JavaVersion.VERSION_17

dependencies {
    // Log
    implementation("ch.qos.logback:logback-core:1.4.14")
    testImplementation("ch.qos.logback:logback-classic:1.4.14")

    // Apache XML
    implementation("org.apache.poi:poi:5.2.5")
    implementation("org.apache.poi:poi-ooxml:5.2.5")

    // Parser
    implementation("org.jsoup:jsoup:1.16.2")

    testImplementation("com.h2database:h2")

    // Validations
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")

    // Lombok
    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")

    // WebSocket
    implementation("org.springframework.boot:spring-boot-starter-websocket")

    // Base Spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Data Base
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql:42.6.0")

    // Authorization
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
    implementation("org.springframework.boot:spring-boot-starter-websocket")

    // Testing
    testImplementation("junit:junit:4.13.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Преобразование объектов
    implementation("org.modelmapper:modelmapper:3.2.0")

    // Kafka
    implementation("org.springframework.kafka:spring-kafka:3.1.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")

}

tasks.test  {
    useJUnitPlatform()
}

tasks {
    // Конфигурация задачи сборки JAR
    withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
        archiveFileName.set("smartCampus.jar") // Имя вашего JAR-файла
        enabled = true
    }
}