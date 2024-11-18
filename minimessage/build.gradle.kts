plugins {
    buildplugin
}

dependencies {
    api(project(":core"))
    implementation("net.kyori:adventure-text-minimessage:4.17.0")
    testImplementation(testFixtures(project(":core")))
}
