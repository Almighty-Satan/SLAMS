plugins {
    buildplugin
}

java {
    disableAutoTargetJvm()
}

dependencies {
    api(project(":core"))
    implementation(libs.adventure.text.minimessage)
    testImplementation(testFixtures(project(":core")))
}
