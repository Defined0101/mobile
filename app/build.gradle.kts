plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.defined.mobile"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.defined.mobile"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // Core AndroidX Dependencies
    implementation(libs.androidx.core.ktx) // Kotlin extensions for basic Android APIs
    implementation(libs.androidx.lifecycle.runtime.ktx) // Lifecycle management with Kotlin extensions
    implementation(libs.androidx.activity.compose) // Integrates Activity management with Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Jetpack Compose Dependencies
    implementation(platform(libs.androidx.compose.bom)) // BOM (Bill of Materials) for consistent Compose versions
    implementation(libs.androidx.ui) // Core UI components for building UI
    implementation(libs.androidx.ui.graphics) // Graphics and color support for UI components
    implementation(libs.androidx.ui.tooling.preview) // Preview support for UI components in Compose
    implementation(libs.androidx.material3) // Material Design 3 components and themes

    // Navigation Dependencies
    implementation(libs.androidx.navigation.compose) // Navigation library for Compose
    implementation(libs.androidx.navigation.fragment) // Fragment-based navigation management
    implementation(libs.androidx.navigation.ui) // UI-based navigation helpers
    implementation(libs.androidx.navigation.dynamic.features.fragment) // Dynamic features support for fragment-based navigation

    // Support and Compatibility Dependency
    implementation(libs.androidx.appcompat)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.auth.ktx) // Compatibility library for legacy APIs and devices

    // Testing Dependencies
    testImplementation(libs.junit) // JUnit testing framework for unit tests
    androidTestImplementation(libs.androidx.junit) // JUnit for Android-specific tests
    androidTestImplementation(libs.androidx.espresso.core) // UI testing with Espresso
    androidTestImplementation(platform(libs.androidx.compose.bom)) // BOM for Compose in Android tests
    androidTestImplementation(libs.androidx.ui.test.junit4) // JUnit4 support for UI testing

    // Debug Dependencies
    debugImplementation(libs.androidx.ui.tooling) // Debugging support for UI components in Compose
    debugImplementation(libs.androidx.ui.test.manifest) // Manifest support for debugging tests

    // Import the BoM for the Firebase platform
    implementation(platform(libs.firebase.bom))

    // When using the BoM, don't specify versions in Firebase dependencies
    implementation(libs.firebase.analytics)

    // Also add the dependency for the Google Play services library and specify its version
    implementation(libs.play.services.auth)

    // Backend Connection Dependencies
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
}
