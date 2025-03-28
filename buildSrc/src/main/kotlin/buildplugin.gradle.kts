plugins {
    id("java-library")
    id("checkstyle")
    id("java-test-fixtures")
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

group = "io.github.almighty-satan.slams"
version = "1.2.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:24.1.0")
    testFixturesImplementation("org.jetbrains:annotations:24.1.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testFixturesImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

val skipPublish: (String) -> Unit = { (components["java"] as AdhocComponentWithVariants).withVariantsFromConfiguration(configurations[it], ConfigurationVariantDetails::skip) }
skipPublish("testFixturesApiElements")
skipPublish("testFixturesRuntimeElements")

publishing {
    publications {
        create<MavenPublication>("release") {
            from(components["java"])
            pom {
                name.set("SLAMS")
                description.set("Simple Language And Message System")
                url.set("https://github.com/Almighty-Satan/SLAMS")
                licenses {
                    license {
                        name.set("GNU Lesser General Public License v2.1")
                        url.set("https://opensource.org/license/lgpl-2-1/")
                    }
                }
                developers {
                    developer {
                        name.set("Almighty-Satan")
                        url.set("https://github.com/Almighty-Satan")
                    }
                    developer {
                        name.set("LeStegii")
                        url.set("https://github.com/LeStegii")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/Almighty-Satan/SLAMS.git")
                    developerConnection.set("scm:git:ssh://github.com:Almighty-Satan/SLAMS.git")
                    url.set("https://github.com/Almighty-Satan/SLAMS")
                }
            }
            artifactId = "slams-${project.name}"
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
