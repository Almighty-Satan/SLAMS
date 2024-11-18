plugins {
    buildplugin
}

dependencies {
    api(project(":core"))
    api("com.fasterxml.jackson.core:jackson-databind:2.18.1")
    testImplementation(testFixtures(project(":core")))
}
