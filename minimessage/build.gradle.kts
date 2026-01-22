plugins {
    buildplugin
}

dependencies {
    api(project(":core"))
    implementation(libs.adventure.text.minimessage)
    testImplementation(testFixtures(project(":core")))
}
