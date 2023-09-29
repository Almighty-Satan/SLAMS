plugins {
    buildplugin
}

dependencies {
    api(project(":core"))
    testImplementation(testFixtures(project(":core")))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
}
