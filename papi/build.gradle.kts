plugins {
    buildplugin
}

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    api(project(":core"))
    implementation("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT") {
        exclude("commons-lang")
        exclude("com.googlecode.json-simple")
        exclude("com.google.guava")
        exclude("com.google.code.gson")
        exclude("org.avaje")
        exclude("org.yaml")
        exclude("net.md-5")
    }
    implementation("me.clip:placeholderapi:2.11.5")
}
