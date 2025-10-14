pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        maven (url="https://jitpack.io" )
        maven ( url = "https://api.mapbox.com/downloads/v2/releases/maven")

        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven (url="https://jitpack.io" )
        maven (  url ="https://api.mapbox.com/downloads/v2/releases/maven")

        google()
        mavenCentral()
    }
}

rootProject.name = "Parvaznama"
include(":app")
 