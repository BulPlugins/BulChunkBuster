plugins {
    id("java")
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
}

tasks.test {
    useJUnitPlatform()
}