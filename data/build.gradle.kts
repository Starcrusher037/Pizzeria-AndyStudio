// Modulo: data
// Contiene las clases de dominio (Cliente, Producto, Pedido) y la logica de negocio asociada.
plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.gourmet.pizzeriaapp.data"
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
    implementation(project(":utilities"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.junit)
}
