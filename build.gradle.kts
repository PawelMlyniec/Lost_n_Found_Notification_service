import com.google.protobuf.gradle.*

plugins {
    id("org.springframework.boot") version "2.4.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("com.google.protobuf") version "0.8.15"
    id("org.checkerframework") version "0.5.17"
    id("com.github.imflog.kafka-schema-registry-gradle-plugin") version "1.2.0"
    java
    idea
}

group = "com.pw"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("http://packages.confluent.io/maven/")
}

buildscript {
    repositories {
        jcenter()
        maven("http://packages.confluent.io/maven/")
        maven("https://plugins.gradle.org/m2/")
        maven("https://jitpack.io")
    }
}

sourceSets {
    main {
        java {
            setSrcDirs(listOf("src/main/java", "build/generated/source/openapi/src/main/java"))
        }
        resources {
            setSrcDirs(listOf("src/main/resources", "build/generated/source/openapi/src/main/resources"))
        }
    }
}

schemaRegistry {
    url.set(System.getenv("SCHEMA_REGISTRY_URL") ?: "http://localhost:9099/")
    credentials {
        username.set(System.getenv("SCHEMA_REGISTRY_USERNAME") ?: "registry-user")
        password.set(System.getenv("SCHEMA_REGISTRY_PASSWORD") ?: "<password>")
    }
    download {
        subject("tms-text-messages-proto-TextMessageSentProto", "src/main/proto")
        subject("lrs-lost-reports-proto-ItemsMatchedProto", "src/main/proto")
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.6.1"
    }
}

dependencies {
    // Spring
    implementation("org.springframework.boot", "spring-boot-starter-web")
    annotationProcessor("org.springframework.boot", "spring-boot-configuration-processor")

    // Security
    implementation("org.springframework.boot", "spring-boot-starter-security")
    implementation("org.springframework.boot", "spring-boot-starter-oauth2-resource-server")

    // Kafka
    implementation("org.springframework.kafka", "spring-kafka")

    // MongoDB
    implementation("org.springframework.boot", "spring-boot-starter-data-mongodb")

    // Lombok
    compileOnly("org.projectlombok", "lombok")
    annotationProcessor("org.projectlombok", "lombok")

    // Protobuf
    implementation("com.google.protobuf:protobuf-java:3.6.1")
    implementation("io.confluent", "kafka-protobuf-serializer", "5.5.1")

    // Metrics
    implementation("org.springframework.boot", "spring-boot-starter-actuator", "2.4.0")
    implementation("io.micrometer", "micrometer-registry-prometheus", "latest.release")

    // Swagger
    implementation("io.swagger", "swagger-annotations", "1.6.2")
    implementation("org.openapitools", "jackson-databind-nullable", "0.2.1")

    // Apache Commons
    implementation("org.apache.commons", "commons-lang3", "3.12.0")

    // Emails
    implementation("org.simplejavamail", "simple-java-mail", "5.0.0")
    implementation("net.markenwerk", "utils-mail-dkim", "1.3.1")
    implementation("commons-validator", "commons-validator", "1.7")

    // Guava
    implementation("com.google.guava", "guava", "30.1.1-jre")

    // Tests
    testImplementation("org.springframework.boot", "spring-boot-starter-test")
    testImplementation("org.springframework.kafka", "spring-kafka-test")
}

tasks {

    bootJar {
        archiveFileName.set("notification-service.jar")
        dependsOn("generateProto")
    }

    test {
        dependsOn("generateProto")
    }

    register<Exec>("dockerBuild") {
        group = "build"
        description = "Builds Docker Image"
        dependsOn("bootJar")
        commandLine("docker", "build", "-t", "notification-service", "--build-arg", "PROFILE=dev", ".")
    }
}