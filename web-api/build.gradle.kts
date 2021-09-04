import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.3"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("org.openapi.generator") version "5.1.1"
	kotlin("jvm") version "1.5.21"
	kotlin("kapt") version "1.5.21"
	kotlin("plugin.spring") version "1.5.21"
}

group = "com.exiasoft.its"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenLocal()
	mavenCentral()
}

extra["springCloudVersion"] = "2020.0.3"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-cache")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
	implementation("org.springframework.kafka:spring-kafka")

	implementation("javax.validation:validation-api:2.0.1.Final")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.12.4")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.12.4")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation("com.squareup.moshi:moshi-kotlin:1.12.0")
	implementation("com.squareup.okhttp3:okhttp:4.9.1")

	implementation("io.github.microutils:kotlin-logging-jvm:2.0.10")
	implementation("org.slf4j:slf4j-api:1.7.32")
	implementation("ch.qos.logback:logback-core:1.2.5")
	implementation("ch.qos.logback:logback-classic:1.2.5")

	implementation("com.exiasoft.its:library:0.0.1-SNAPSHOT")
	implementation("com.exiasoft.its:reference-data-restclient:0.0.1-SNAPSHOT")
	implementation("com.exiasoft.its:market-data-restclient:0.0.1-SNAPSHOT")

	implementation("org.mapstruct:mapstruct:1.4.2.Final")
	kapt("org.mapstruct:mapstruct-processor:1.4.2.Final")

	implementation("org.springdoc:springdoc-openapi-ui:1.5.10")
	implementation("org.springdoc:springdoc-openapi-kotlin:1.5.10")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.kafka:spring-kafka-test")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
	dependsOn(
		tasks.openApiGenerate
	)
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
	generatorName.set("kotlin-spring")
	inputSpec.set("$projectDir/src/main/resources/static/api-spec.yaml")
	outputDir.set("$buildDir/generated")
	apiPackage.set("com.exiasoft.its.webapi.endpoint.rest.api")
	modelPackage.set("com.exiasoft.its.webapi.endpoint.rest.model")
	invokerPackage.set("com.exiasoft.its.webapi")
	ignoreFileOverride.set("$projectDir/src/main/resources/.openapi-generator-ignore")
	modelNameSuffix.set("Dto")
	configOptions.set(
		mapOf(
			"dateLibrary" to "java8",
			"delegatePattern" to "true",
			"useTags" to "true",
			"apiDocs" to "false",
		)
	)
	typeMappings.set(
		mapOf(
			"kotlin.Float" to "java.math.BigDecimal"
		)
	)
}
