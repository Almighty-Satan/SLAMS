plugins {
    id("java-library")
    id("checkstyle")
    id("maven-publish")
    id("signing")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    withSourcesJar()
    withJavadocJar()
}

checkstyle {
    configDirectory.set(File("../checkstyle"))
    toolVersion = "9.3"
}

group = "io.github.almighty-satan.language"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("release") {
            from(components["java"])
            pom {
                name.set("TODO")
                description.set("TODO")
                url.set("https://github.com/Almighty-Satan/Language")
                licenses {
                    license {
                        name.set("TODO")
                        url.set("TODO")
                    }
                }
                developers {
                    developer {
                        name.set("Almighty-Satan")
                        url.set("https://github.com/Almighty-Satan")
                    }
                    developer {
                        name.set("UeberallGebannt")
                        url.set("https://github.com/UeberallGebannt")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/Almighty-Satan/Language.git")
                    developerConnection.set("scm:git:ssh://github.com:Almighty-Satan/Language.git")
                    url.set("https://github.com/Almighty-Satan/Language")
                }
            }
            artifactId = "language-${project.name}"
        }
        repositories {
            maven {
                setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = System.getenv("OSSRH_USER")
                    password = System.getenv("OSSRH_PASSWORD")
                }
            }
        }
    }
}

signing {
    val signingKey = System.getenv("SIGNING_KEY")
    val signingPassword = System.getenv("SIGNING_PASSWORD")
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications.getByName("release"))
}
