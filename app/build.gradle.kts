plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.sleetworks.serenity.android.newone"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.sleetworks.serenity.android.newone"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            // Note the escaped quotes \" within the string, and the new String[] { ... } syntax
            buildConfigField(
                "String[]",
                "URL_ARRAY",
                "new String[]{\"https://app.pinpointworks.com/\", \"https://dev.pinpointworks.com/\", \"https://pinpoint.asm-yachts.com/\", \"https://release.pinpointworks.com/\"}"
            )
            // Other debug configurations...
        }
        release {
            // For ndk.debugSymbolLevel, it's typically set within android.buildTypes.release.ndk
            // However, the property is debuggable. If you intend to include debug symbols for release NDK builds,
            // this might be what you intend, but often it's 'SYMBOL_TABLE' or 'NONE' for release.
            // ndk {
            //     debugSymbolLevel = "FULL" // Or "SYMBOL_TABLE" or "NONE"
            // }

            buildConfigField(
                "String[]",
                "URL_ARRAY",
                "new String[]{\"https://app.pinpointworks.com/\", \"https://pinpoint.asm-yachts.com/\", \"https://release.pinpointworks.com/\"}"
            )

            isMinifyEnabled = false // In Kotlin DSL, it's a property assignment
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), // Use optimize for release
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
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material.icons.extended)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    //room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    //datastore
    implementation(libs.androidx.datastore.preferences)

    //splash
    implementation(libs.androidx.core.splashscreen) // Use the latest stable version

    //core splashscreen
    implementation(libs.androidx.core.splashscreen)

    //datastore
    implementation(libs.androidx.datastore.preferences)

    //lottie
    implementation(libs.lottie.compose)


}


