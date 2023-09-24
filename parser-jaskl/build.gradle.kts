plugins {
    buildplugin
}

dependencies {
    api(project(":core"))
    testImplementation(testFixtures(project(":core")))
    implementation("io.github.almighty-satan.jaskl:jaskl-core:1.4.0")
    testImplementation("io.github.almighty-satan.jaskl:jaskl-yaml:1.4.0")
    testImplementation("io.github.almighty-satan.jaskl:jaskl-hocon:1.4.0")
    testImplementation("io.github.almighty-satan.jaskl:jaskl-json:1.4.0")
}
