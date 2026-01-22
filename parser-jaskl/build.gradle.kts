plugins {
    buildplugin
}

dependencies {
    api(project(":core"))
    testImplementation(testFixtures(project(":core")))
    implementation(libs.jaskl.core)
    testImplementation(libs.jaskl.hocon)
    testImplementation(libs.jaskl.json)
    testImplementation(libs.jaskl.yaml)
}
