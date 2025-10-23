import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.10"
	id("io.ktor.plugin") version "2.3.5"
	kotlin("plugin.serialization") version "1.9.10"
    id("application")
}

repositories {
    mavenCentral()
}

val ktor_version = "2.3.4"
val exposed_version = "0.41.1"
val logback_version = "1.2.11"

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
	implementation("io.ktor:ktor-server-call-logging-jvm:${ktor_version}")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktor_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.xerial:sqlite-jdbc:3.41.2.1")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.mindrot:jbcrypt:0.4")
    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.9.10")
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "19"
}
