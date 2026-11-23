// Modulo: utilities
// Contiene funciones generales reutilizables: validaciones, manejo de errores y operaciones comunes.
plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.gourmet.pizzeriaapp.utilities"
    compileSdk = 37

    defaultConfig {
        minSdk = 23
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.junit)
}
