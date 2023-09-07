plugins {
    buildplugin
}

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    api(project(":core"))
    implementation("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    implementation("me.clip:placeholderapi:2.11.3")
}
