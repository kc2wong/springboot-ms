import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("maven-publish")
	kotlin("jvm") version "1.5.21"
	kotlin("plugin.spring") version "1.5.21"
}

group = "com.exiasoft.its"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenLocal()
	mavenCentral()
}

dependencies {
	compileOnly("javax.persistence:javax.persistence-api:2.2")
	compileOnly("org.springframework.data:spring-data-jpa:2.5.3")
	compileOnly("org.springframework:spring-webmvc:5.3.9")
	compileOnly("org.hibernate:hibernate-core:5.5.5.Final")
	compileOnly("com.fasterxml.jackson.core:jackson-databind:2.12.4")
	compileOnly("org.springframework.kafka:spring-kafka:2.7.4")
	compileOnly("io.github.microutils:kotlin-logging-jvm:2.0.10")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

val archives by configurations
configure<PublishingExtension> {
	publications {
		create<MavenPublication>("maven") {
			setArtifacts(archives.artifacts)
		}
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
