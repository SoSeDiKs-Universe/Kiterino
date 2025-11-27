version = "1.0.0-SNAPSHOT"

plugins {
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19"
}

repositories {
    mavenLocal()
}

dependencies {
    compileOnly(project(":kiterino-api"))
    paperweight.paperDevBundle("1.21.10-R0.1-SNAPSHOT", "me.sosedik.kiterino")
}

tasks.processResources {
    val apiVersion = rootProject.providers.gradleProperty("mcVersion").get()
    val props = mapOf(
        "version" to project.version,
        "apiversion" to "\"$apiVersion\"",
    )
    inputs.properties(props)
    filesMatching("paper-plugin.yml") {
        expand(props)
    }
}
