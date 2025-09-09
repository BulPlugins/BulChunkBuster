plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.bul"
version = "0.3"

repositories {
    mavenCentral()
    maven {
        name = "aikar"
        url = uri("https://repo.aikar.co/content/groups/aikar/")
    }
    maven { url = uri("https://jitpack.io") }
    maven {
        name = "CodeMC"
        url = uri("https://repo.codemc.io/repository/maven-public/")
    }
    maven { url = uri("https://libraries.minecraft.net/") }
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")
    implementation("org.bstats:bstats-bukkit:3.0.2")
    compileOnly("com.github.SaberLLC:Saber-Factions:4.1.4-STABLE")
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