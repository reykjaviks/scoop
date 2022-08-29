import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.7"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("org.jetbrains.kotlin.plugin.allopen") version "1.6.21"
	id("org.jetbrains.kotlinx.kover") version "0.5.0"
	id("org.sonarqube") version "3.3"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"
	kotlin("kapt") version "1.7.0"
}

//======================= Project Info =============================================
group = "com.marjorie"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

allOpen {
	annotation("javax.persistence.Entity")
	annotation("javax.persistence.MappedSuperclass")
	annotation("javax.persistence.Embeddable")
}

//======================= Dependencies =============================================
dependencies {
	// Spring
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-webflux")

	// Kotlin
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")
	kapt("org.mapstruct:mapstruct-processor:1.5.2.Final")
	kapt("org.springframework.boot:spring-boot-configuration-processor")

	// JWT
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")

	// Other
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.1.Final")
	implementation("org.junit.jupiter:junit-jupiter:5.8.2")
	implementation("org.mapstruct:mapstruct:1.5.1.Final")
	implementation("junit:junit:4.13.1")
	runtimeOnly("org.postgresql:postgresql")
	implementation("org.junit.jupiter:junit-jupiter:5.8.2")

	// Test
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(module = "mockito-core")
	}
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testImplementation("com.ninja-squad:springmockk:3.1.1")
	testImplementation(kotlin("test"))
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

//======================= Tasks =============================================
tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}