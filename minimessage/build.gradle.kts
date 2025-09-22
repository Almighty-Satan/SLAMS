plugins {
    buildplugin
}

dependencies {
    api(project(":core"))
    implementation("net.kyori:adventure-text-minimessage:4.24.0")
    testImplementation(testFixtures(project(":core")))
}
