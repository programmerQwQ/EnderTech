plugins {
    alias(libs.plugins.fabric.loom)
}

group = project.property("mod_group") as String
version = project.property("mod_version") as String

dependencies {
    minecraft(libs.minecraft)
    implementation(libs.fabric.api)
    implementation(libs.fabric.loader)
}

fabricApi {
    configureDataGeneration {
        client = true
        addToResources = true
    }
}

tasks.processResources {
    filesMatching("fabric.mod.json") {
        expand(
            "mod_id" to project.property("mod_id") as String,
            "mod_name" to project.property("mod_name") as String,
            "mod_version" to project.property("mod_version") as String,
            "mod_license" to project.property("mod_license") as String,
        )
    }
}
