plugins {
    buildplugin
}

dependencies {
    api(project(":core"))
    testImplementation(testFixtures(project(":core")))
}
