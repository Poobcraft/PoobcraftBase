plugins {
    id("java")
}

group = "com.poobcraft"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.7-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

sourceSets {
    main {
        resources.srcDir("src/main/resources")
    }
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
val pluginJarName = "PoobcraftBase-${version}.jar"

tasks.register<Copy>("copyJarToPlugins") {
    dependsOn("build")

    from("build/libs/$pluginJarName")
    into("C:/Users/Justi/OneDrive/Documents/Plugin Dev/Test Server/plugins")

    doLast {
        println("✅ Plugin copied to server plugins folder.")
    }
}
