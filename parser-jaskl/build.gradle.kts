plugins {
    buildplugin
}

val jasklVersion = "1.4.1"
dependencies {
    api(project(":core"))
    testImplementation(testFixtures(project(":core")))
    implementation("io.github.almighty-satan.jaskl:jaskl-core:$jasklVersion")
    testImplementation("io.github.almighty-satan.jaskl:jaskl-yaml:$jasklVersion")
    testImplementation("io.github.almighty-satan.jaskl:jaskl-hocon:$jasklVersion")
    testImplementation("io.github.almighty-satan.jaskl:jaskl-json:$jasklVersion")
}
