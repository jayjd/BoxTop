plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.jayjd.boxtop"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.jayjd.boxtop"
        minSdk = 23
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.utilcodex)
    implementation(libs.tv.recyclerview)
    implementation(libs.baserecyclerviewadapterhelper4)

    implementation("androidx.tvprovider:tvprovider:1.1.0")

    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.activity:activity:1.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")

    implementation("com.lzy.net:okgo:3.0.4")

    implementation("androidx.palette:palette:1.0.0")
    //noinspection UseTomlInstead
    implementation("com.google.guava:guava:33.3.1-android")
    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    //noinspection UseTomlInstead
    implementation("com.google.code.gson:gson:2.13.2")
    // https://mvnrepository.com/artifact/org.projectlombok/lombok
    //noinspection AnnotationProcessorOnCompilePath,UseTomlInstead
    implementation("org.projectlombok:lombok:1.18.42")
    //noinspection UseTomlInstead
    annotationProcessor("org.projectlombok:lombok:1.18.42")
    //noinspection UseTomlInstead 不支持7.0以下
    implementation("com.github.bumptech.glide:glide:4.16.0")
    //noinspection UseTomlInstead
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    //noinspection
    implementation("com.github.bumptech.glide:okhttp3-integration:4.16.0") {
        exclude("com.android.support")
    }

    //noinspection UseTomlInstead
    implementation("androidx.room:room-runtime:2.8.4")
    //noinspection UseTomlInstead
    annotationProcessor("androidx.room:room-compiler:2.8.4")
}