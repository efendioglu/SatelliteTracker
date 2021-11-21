plugins {
    id("com.android.application")
    id("kotlin-android")
    kotlin("plugin.serialization")
    id("com.squareup.sqldelight")
}


android {
    compileSdk = 31
    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = AppConfig.androidTestInstrumentation
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                AppConfig.proguardConsumerRules
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
        viewBinding = true
    }
}

dependencies {
    implementation(Dependencies.appLibraries)
    //implementation("androidx.legacy:legacy-support-v4:1.0.0")
    testImplementation(Dependencies.testLibraries)
    androidTestImplementation(Dependencies.androidTestLibraries)
}