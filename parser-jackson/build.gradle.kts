plugins {
    buildplugin
}

dependencies {
    implementation(project(":core"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
}
