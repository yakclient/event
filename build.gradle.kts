plugins {
    kotlin("jvm") version "1.6.0"

    id("io.gitlab.arturbosch.detekt") version "1.18.1"
    id("maven-publish")
    id("org.jetbrains.dokka") version "1.4.32"
}

group = "net.yakclient"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.wrapper {
    gradleVersion = "7.3.1"
}

tasks.wrapper {
    gradleVersion = "7.3.1"

}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")


    publishing {
        repositories {
            if (!project.hasProperty("maven-user") || !project.hasProperty("maven-pass")) return@repositories

            maven {
                val repo = if (project.findProperty("isSnapshot") == "true") "snapshots" else "releases"

                isAllowInsecureProtocol = true

                url = uri("http://repo.yakclient.net/$repo")

                credentials {
                    username = project.findProperty("maven-user") as String
                    password = project.findProperty("maven-pass") as String
                }
                authentication {
                    create<BasicAuthentication>("basic")
                }
            }
        }
    }


    repositories {
        mavenCentral()
    }

    kotlin {
        explicitApi()
    }

    dependencies {
        implementation(kotlin("stdlib"))
    }
}