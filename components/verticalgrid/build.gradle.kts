import com.guru.composecookbook.build.dependencies.addComposeOfficialDependencies

plugins {
    /**
     * See [common-compose-module-configs-script-plugin.gradle.kts] file
     */
    id("common-compose-module-configs-script-plugin")
}

dependencies {
    addComposeOfficialDependencies()
}
android {
    namespace = "com.guru.composecookbook.verticalgrid"
}
