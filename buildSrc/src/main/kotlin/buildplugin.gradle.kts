plugins {
    id("java-library")
    id("checkstyle")
    id("java-test-fixtures")
    id("maven-publish")
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

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:26.0.2")
    testFixturesImplementation("org.jetbrains:annotations:26.0.2")

    testImplementation("org.junit.jupiter:junit-jupiter:5.12.2")
    testFixturesImplementation("org.junit.jupiter:junit-jupiter:5.12.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
                setUrl(rootProject.layout.buildDirectory.dir("staging-deploy"))
            }
        }
    }
}
