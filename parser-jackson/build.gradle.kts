plugins {
    buildplugin
}

dependencies {
    api(project(":core"))
    api("com.fasterxml.jackson.core:jackson-databind:2.20.0")
    testImplementation(testFixtures(project(":core")))
}
