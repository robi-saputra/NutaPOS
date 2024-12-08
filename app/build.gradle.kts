plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.devtools.ksp)
}

android {
    namespace = "robi.saputra.nutapos"
    compileSdk = 34

    defaultConfig {
        applicationId = "robi.saputra.nutapos"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            // Use environment variables or project properties for sensitive data
            storeFile = (System.getenv("ANDROID_SIGNING_STORE_FILE") ?: project.findProperty("android.injected.signing.store.file") as String?)?.let {
                file(
                    it
                )
            }
            storePassword = System.getenv("ANDROID_SIGNING_STORE_PASSWORD") ?: project.findProperty("android.injected.signing.store.password") as String?
            keyAlias = System.getenv("ANDROID_SIGNING_KEY_ALIAS") ?: project.findProperty("android.injected.signing.key.alias") as String?
            keyPassword = System.getenv("ANDROID_SIGNING_KEY_PASSWORD") ?: project.findProperty("android.injected.signing.key.password") as String?
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Link the signing configuration
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.collection)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.com.facebook.shimmer)
    implementation(libs.com.github.bumptech.glide)
    ksp(libs.com.github.bumptech.glide.compiler)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.android.compat)
    implementation(libs.koin.androidx.workmanager)
    implementation(libs.koin.androidx.navigation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}