import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
    `maven-publish`
    id("io.papermc.paperweight.patcher") version "1.7.1"
}

val paperMavenPublicUrl = "https://repo.papermc.io/repository/maven-public/"

repositories {
    mavenCentral()
    maven(paperMavenPublicUrl) { content { onlyForConfigurations(configurations.paperclip.name) } }
}

dependencies {
    remapper("net.fabricmc:tiny-remapper:0.10.3:fat")
    decompiler("org.vineflower:vineflower:1.10.1")
    paperclip("io.papermc:paperclip:3.0.3")
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    java { toolchain { languageVersion.set(JavaLanguageVersion.of(21)) } }
}

subprojects {
    tasks.withType<JavaCompile>().configureEach {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }

    tasks.withType<Javadoc>().configureEach {
        options.encoding = Charsets.UTF_8.name()
    }

	tasks.withType<ProcessResources>().configureEach {
        filteringCharset = Charsets.UTF_8.name()
    }

    tasks.withType<Test> {
        testLogging {
            showStackTraces = true
            exceptionFormat = TestExceptionFormat.FULL
            events(TestLogEvent.STANDARD_OUT)
        }
    }

    repositories {
        mavenCentral()
        maven(paperMavenPublicUrl)
        maven("https://repo.codemc.io/repository/maven-public/") // NBT-API
    }
}

paperweight {
    serverProject.set(project(":kiterino-server"))

	remapRepo.set(paperMavenPublicUrl)
	decompileRepo.set(paperMavenPublicUrl)

    useStandardUpstream("Purpur") {
        url.set(github("pl3xgaming", "Purpur"))
        ref.set(providers.gradleProperty("purpurCommit"))

        withStandardPatcher {
            baseName("Purpur")

            apiPatchDir.set(layout.projectDirectory.dir("patches/api"))
            apiOutputDir.set(layout.projectDirectory.dir("Kiterino-API"))

            serverPatchDir.set(layout.projectDirectory.dir("patches/server"))
            serverOutputDir.set(layout.projectDirectory.dir("Kiterino-Server"))
        }

        patchTasks.register("generatedApi") {
            isBareDirectory = true
            upstreamDirPath = "paper-api-generator/generated"
            patchDir = layout.projectDirectory.dir("patches/generated-api")
            outputDir = layout.projectDirectory.dir("paper-api-generator/generated")
        }
    }
}

tasks.generateDevelopmentBundle {
    ignoreUnsupportedEnvironment.set(true)
    apiCoordinates.set("me.sosedik.kiterino:kiterino-api")
    libraryRepositories.set(
        listOf(
            "https://repo.maven.apache.org/maven2/",
            paperMavenPublicUrl,
//            "https://libraries.minecraft.net/",
//            "https://maven.fabricmc.net/",
//            "https://maven.quiltmc.org/repository/release/",
//            "https://repo.aikar.co/content/groups/aikar",
//            "https://ci.emc.gs/nexus/content/groups/aikar/",
//            "https://sonatype.projecteden.gg/repository/maven-public/",
            "https://nexus.velocitypowered.com/repository/velocity-artifacts-snapshots/", // Velocity stuff
            "https://repo.codemc.io/repository/maven-public/", // NBT-API
        )
    )
}

publishing {
    if (project.hasProperty("publishDevBundle")) {
        publications.create<MavenPublication>("devBundle") {
            artifact(tasks.generateDevelopmentBundle) {
                artifactId = "dev-bundle"
            }
        }
    }
}

tasks.register("printMinecraftVersion") {
    doLast {
        println(providers.gradleProperty("mcVersion").get().trim())
    }
}

tasks.register("printKiterinoVersion") {
    doLast {
        println(project.version)
    }
}

tasks.createMojmapPaperclipJar {
    outputZip.set(rootProject.layout.projectDirectory.file("kiterino.jar"))
}
