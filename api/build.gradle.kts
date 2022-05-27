plugins {
    id("org.javamodularity.moduleplugin") version "1.8.10"
    id("org.jetbrains.dokka")
}

group = "net.yakclient"
version = "1.0-SNAPSHOT"

repositories {
    maven {
        isAllowInsecureProtocol = true
        url = uri("http://repo.yakclient.net/snapshots")
    }
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    implementation("net.yakclient:common-util:1.0-SNAPSHOT") {
        isChanging = true
    }
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.compileTestKotlin {
    destinationDirectory.set(tasks.compileTestJava.get().destinationDirectory.get().asFile)
}
tasks.jar {
    archiveBaseName.set("event-api")
    archiveClassifier.set("")
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = "17"
    }
}

tasks.compileJava {
    targetCompatibility = "17"
    sourceCompatibility = "17"
}

tasks.compileTestKotlin {
    kotlinOptions {
        jvmTarget = "17"
    }
}

task<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

task<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
    from(tasks.dokkaJavadoc)
}

publishing {
    publications {
        create<MavenPublication>("api-maven") {
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            artifactId = "event-api"

            pom {
                name.set("Event API")
                description.set("A Kotlin Event API")
                url.set("https://github.com/yakclient/event")

                packaging = "jar"

                developers {
                    developer {
                        id.set("Chestly")
                        name.set("Durgan McBroom")
                    }
                }

                licenses {
                    license {
                        name.set("GNU General Public License")
                        url.set("https://opensource.org/licenses/gpl-license")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/yakclient/event")
                    developerConnection.set("scm:git:ssh://github.com:yakclient/event.git")
                    url.set("https://github.com/yakclient/event")
                }
            }
        }
    }
}
