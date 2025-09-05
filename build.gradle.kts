plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.example"
version = "0.1"

repositories {
    mavenCentral()
    maven {
        name = "aikar"
        url = uri("https://repo.aikar.co/content/groups/aikar/")
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")
    implementation("org.bstats:bstats-bukkit:3.0.2")
}

//Need to include and relocate bStats
tasks {
    //Replace the version in plugin.yml
    processResources {
        filesMatching("plugin.yml") {
            expand(project.properties)
        }
    }
    withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
        archiveClassifier.set("")
        minimize()
        relocate("org.bstats", "com.alihaine.bulchunkbuster.libs.bstats")
    }
    build {
        dependsOn(shadowJar)
    }
}