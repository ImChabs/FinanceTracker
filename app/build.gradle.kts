import org.gradle.api.GradleException

fun readReleaseSigningInput(envName: String, propertyName: String): String? =
    providers.environmentVariable(envName).orNull?.takeIf(String::isNotBlank)
        ?: providers.gradleProperty(propertyName).orNull?.takeIf(String::isNotBlank)

val releaseKeystorePath = readReleaseSigningInput(
    envName = "ANDROID_RELEASE_KEYSTORE_PATH",
    propertyName = "androidReleaseKeystorePath"
)
val releaseStorePassword = readReleaseSigningInput(
    envName = "ANDROID_RELEASE_STORE_PASSWORD",
    propertyName = "androidReleaseStorePassword"
)
val releaseKeyAlias = readReleaseSigningInput(
    envName = "ANDROID_RELEASE_KEY_ALIAS",
    propertyName = "androidReleaseKeyAlias"
)
val releaseKeyPassword = readReleaseSigningInput(
    envName = "ANDROID_RELEASE_KEY_PASSWORD",
    propertyName = "androidReleaseKeyPassword"
)

val hasAnyReleaseSigningInput = listOf(
    releaseKeystorePath,
    releaseStorePassword,
    releaseKeyAlias,
    releaseKeyPassword
).any { !it.isNullOrBlank() }

val hasCompleteReleaseSigningInput = listOf(
    releaseKeystorePath,
    releaseStorePassword,
    releaseKeyAlias,
    releaseKeyPassword
).all { !it.isNullOrBlank() }

if (hasAnyReleaseSigningInput && !hasCompleteReleaseSigningInput) {
    throw GradleException(
        "Partial release signing configuration detected. Provide all ANDROID_RELEASE_* " +
            "environment variables or all androidRelease* Gradle properties."
    )
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.example.newfinancetracker"
    compileSdk {
        version = release(36)
    }

    if (hasCompleteReleaseSigningInput) {
        signingConfigs {
            create("release") {
                storeFile = file(requireNotNull(releaseKeystorePath))
                storePassword = requireNotNull(releaseStorePassword)
                keyAlias = requireNotNull(releaseKeyAlias)
                keyPassword = requireNotNull(releaseKeyPassword)
            }
        }
    }

    defaultConfig {
        applicationId = "com.example.newfinancetracker"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            if (hasCompleteReleaseSigningInput) {
                signingConfig = signingConfigs.getByName("release")
            }
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.dagger.hilt.android)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    ksp(libs.dagger.hilt.compiler)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
