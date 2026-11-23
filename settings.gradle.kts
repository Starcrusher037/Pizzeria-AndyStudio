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
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "PizzeriaApp"

// Modulo principal de la aplicacion Android
include(":app")

// Modulo de datos: clases de dominio (Cliente, Producto, Pedido) y logica de negocio
include(":data")

// Modulo de utilidades: validaciones, manejo de errores y operaciones reutilizables
include(":utilities")

// Modulo de UI: ViewModel compartido y componentes Compose reutilizables
include(":ui-module")
