pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Pokemon"
include(":app")
include(":domain")
include("core:core-network")
include(":core:core-common")
include(":core:core-database")
include(":core:core-uikit")
include(":features:feature-pokemon-list")
include(":features:feature-pokemon-details")
include(":features:feature-favorites")
include(":core:core-data")
