plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.bul"
version = "0.4"

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
    maven {
        name = "griefdefender"
        url = uri("https://repo.glaremasters.me/repository/bloodshot")
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")
    implementation("org.bstats:bstats-bukkit:3.0.2")
    compileOnly("com.github.SaberLLC:Saber-Factions:4.1.4-STABLE")
    compileOnly(group = "com.griefdefender", name = "api", version = "2.1.0-SNAPSHOT")
    compileOnly("com.github.GriefPrevention:GriefPrevention:16.18.2")
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