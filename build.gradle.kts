plugins {
    id("java")
    id("application")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.diffplug.spotless") version "7.0.4"
    id("org.graalvm.buildtools.native") version "0.10.6"
}

group = "org.cli-todoer"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.googlecode.lanterna:lanterna:3.1.2")
    implementation("info.picocli:picocli:4.7.7")
    annotationProcessor("info.picocli:picocli-codegen:4.7.7")
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.19.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.19.1")
    implementation("commons-io:commons-io:2.19.0")

    testImplementation(platform("org.junit:junit-bom:5.13.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-core:5.18.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.18.0")
    testImplementation("org.assertj:assertj-core:3.27.3")
    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showStandardStreams = true
    }
}

spotless {
    java {
        googleJavaFormat("1.28.0")
        target("src/**/*.java")
    }
    kotlin {
        ktlint("1.2.1")
        target("src/**/*.kt", "*.kts")
    }
}

tasks.named("build") {
    dependsOn("spotlessApply", "shadowJar")
}
tasks.named("nativeCompile") {
    dependsOn("build")
}
application {
    mainClass.set("org.clitodoer.TodoLauncher")
}

graalvmNative {
    binaries {
        named("main") {
            imageName.set("cli-todoer")
            buildArgs.add("--no-fallback")
        }
    }
}

// Generate version.properties file
val generateVersionProperties by tasks.registering {
    val outputDir = layout.buildDirectory.dir("generated/resources")
    val versionFile = outputDir.map { it.file("version.properties") }

    outputs.file(versionFile)

    doLast {
        versionFile.get().asFile.apply {
            parentFile.mkdirs()
            writeText("version=${project.version}")
        }
    }
}

tasks.processResources {
    dependsOn(generateVersionProperties)
    from(generateVersionProperties.map { it.outputs.files }) {
        into("")
    }
}
