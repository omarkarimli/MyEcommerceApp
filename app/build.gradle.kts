plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    id("androidx.navigation.safeargs")
    id("kotlin-parcelize")
    id("kotlin-kapt")

    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")

    // Dagger Hilt
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.omarkarimli.myecommerceapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.omarkarimli.myecommerceapp"
        minSdk = 26
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
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Views/Fragments integration
    val navVersion = "2.8.3"
    implementation("androidx.navigation:navigation-fragment:$navVersion")
    implementation("androidx.navigation:navigation-ui:$navVersion")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.7.2")
    implementation ("com.squareup.retrofit2:converter-gson:2.7.2")

    // Okhttp
    implementation("com.squareup.okhttp3:okhttp:4.9.1")

    // Picasso
    implementation ("com.squareup.picasso:picasso:2.8")

    // DotsIndicator
    implementation("com.tbuonomo:dotsindicator:4.3")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}