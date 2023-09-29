plugins {
    buildplugin
}

dependencies {
    api(project(":core"))
    api("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    testImplementation(testFixtures(project(":core")))
}
