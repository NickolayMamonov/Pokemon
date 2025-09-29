plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.koin.core)
    
    // Testing dependencies (только JVM совместимые)
    testImplementation(libs.junit)
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    // Убираем Android-специфичные зависимости:
    // testImplementation("androidx.arch.core:core-testing:2.2.0") - только для Android модулей
    // testImplementation("app.cash.turbine:turbine:1.0.0") - больше подходит для Flow тестирования в Android
}
