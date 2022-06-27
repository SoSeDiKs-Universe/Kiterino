import java.util.Locale

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://mvn.thearcanebrony.net/repository/maven-public/")
        maven("https://jitpack.io/")
        mavenCentral()
    }
}

rootProject.name = "kiterino"
for (name in listOf("Kiterino-API", "Kiterino-Server")) {
    val projName = name.toLowerCase(Locale.ENGLISH)
    include(projName)
    findProject(":$projName")!!.projectDir = file(name)
}
