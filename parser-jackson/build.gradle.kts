plugins {
    buildplugin
}

dependencies {
    api(project(":core"))
    api(libs.jackson.databind)
    testImplementation(testFixtures(project(":core")))
}
