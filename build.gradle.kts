plugins {
    id("java")
    id ("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.cli-todoer"
version = "1.0.0-SNAPSHOT"

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
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")
}

tasks.test {
    useJUnitPlatform()
}

//compileJava {
//    options.compilerArgs += ["-Aproject=${project.group}/${project.name}"]
//}

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
