plugins {
    //alias(libs.plugins.android.application)
    //id("org.jetbrains.kotlin.android")

    id("com.android.application")
    id("org.jetbrains.kotlin.android") version "1.9.0" // specify a version here
}

android {
    namespace = "com.guyi.class25b_and_1"
    compileSdk = 35

    kotlinOptions {
        jvmTarget = "11"
    }

    defaultConfig {
        applicationId = "com.guyi.class25b_and_1"
        minSdk = 29
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("androidx.core:core-ktx:1.12.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.google.android.gms:play-services-maps:18.2.0")

    implementation ("androidx.fragment:fragment-ktx:1.6.2")


    // Image Loader
    implementation(libs.glide)

}