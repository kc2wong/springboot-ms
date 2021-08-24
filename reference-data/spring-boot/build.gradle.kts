import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.3"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("org.openapi.generator") version "5.1.1"
	kotlin("jvm") version "1.5.21"
	kotlin("kapt") version "1.5.21"
	kotlin("plugin.spring") version "1.5.21"
}

group = "com.exiasoft.its.reference-data"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenLocal()
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.kafka:spring-kafka")

	implementation("javax.validation:validation-api:2.0.1.Final")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("mysql:mysql-connector-java:8.0.26")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation("io.github.microutils:kotlin-logging-jvm:2.0.10")
	implementation("org.slf4j:slf4j-api:1.7.32")
	implementation("ch.qos.logback:logback-core:1.2.5")
	implementation("ch.qos.logback:logback-classic:1.2.5")

	implementation("com.exiasoft.its:library:0.0.1-SNAPSHOT")

	implementation("org.mapstruct:mapstruct:1.4.2.Final")
	kapt("org.mapstruct:mapstruct-processor:1.4.2.Final")

	implementation("org.flywaydb:flyway-core:7.12.0")

	implementation("org.springdoc:springdoc-openapi-ui:1.5.10")
	implementation("org.springdoc:springdoc-openapi-kotlin:1.5.10")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

sourceSets {
	main {
		java.srcDirs("$buildDir/generated/src/main/kotlin", "$buildDir/generated/source/kapt/main")
	}
}

openApiGenerate {
	auth.set("http://exiasoft.byethost7.com")
	generatorName.set("kotlin-spring")
	inputSpec.set("$projectDir/src/main/resources/static/api-spec.yaml")
	outputDir.set("$buildDir/generated")
	apiPackage.set("com.exiasoft.its.refdata.endpoint.rest.api")
	modelPackage.set("com.exiasoft.its.refdata.endpoint.rest.model")
	invokerPackage.set("com.exiasoft.its.refdata")
	ignoreFileOverride.set("$projectDir/src/main/resources/.openapi-generator-ignore")
	modelNameSuffix.set("Dto")
	configOptions.set(mapOf(
		"dateLibrary" to "java8",
		"delegatePattern" to "true",
		"useTags" to "true",
		"apiDocs" to "false"
	))
}
