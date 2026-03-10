plugins {
    buildplugin
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/repository/public/")
}

dependencies {
    api(project(":core"))
    api(project(":standalone"))

    compileOnly(libs.spigot) {
        exclude("commons-lang")
        exclude("com.googlecode.json-simple")
        exclude("com.google.guava")
        exclude("com.google.code.gson")
        exclude("org.avaje")
        exclude("org.yaml")
    }

    testImplementation(testFixtures(project(":core")))
    testImplementation(testFixtures(project(":standalone")))
    testImplementation(libs.spigot) {
        exclude("commons-lang")
        exclude("com.googlecode.json-simple")
        exclude("com.google.guava")
        exclude("com.google.code.gson")
        exclude("org.avaje")
        exclude("org.yaml")
    }
}
