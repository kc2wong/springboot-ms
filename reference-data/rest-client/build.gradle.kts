import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("maven-publish")
	id("org.openapi.generator") version "5.1.1"
	kotlin("jvm") version "1.5.21"
}

group = "com.exiasoft.its"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenLocal()
	mavenCentral()
}

dependencies {
	implementation("com.squareup.moshi:moshi-kotlin:1.12.0")
	implementation("com.squareup.okhttp3:okhttp:4.9.1")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

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
			artifactId = "reference-data-restclient"
			setArtifacts(archives.artifacts)
		}
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
	generatorName.set("kotlin")
	inputSpec.set("$projectDir/../spring-boot/src/main/resources/static/api-spec.yaml")
	outputDir.set("$buildDir/generated")
	apiPackage.set("com.exiasoft.its.refdata.endpoint.rest.api")
	modelPackage.set("com.exiasoft.its.refdata.endpoint.rest.model")
	invokerPackage.set("com.exiasoft.its.refdata")
	ignoreFileOverride.set("$projectDir/src/main/resources/.openapi-generator-ignore")
	configOptions.set(mapOf(
		"dateLibrary" to "java8",
		"delegatePattern" to "true",
		"useTags" to "true",
		"apiDocs" to "false",
		"serializableModel" to "true"
	))
}
