import com.github.megatronking.stringfog.plugin.StringFogExtension
plugins {
    alias(libs.plugins.android.application)
    id("stringfog")
}
apply(plugin = "stringfog")

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
            isMinifyEnabled = true
            isShrinkResources = true
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

configure<StringFogExtension> {
    // 必要：加解密库的实现类路径，需和上面配置的加解密算法库一致。
    implementation = "com.github.megatronking.stringfog.xor.StringFogImpl"
    // 可选：加密开关，默认开启。
    enable = true
    // 可选：指定需加密的代码包路径，可配置多个，未指定将默认全部加密。
    fogPackages = arrayOf("com.jayjd.boxtop")
    kg = com.github.megatronking.stringfog.plugin.kg.RandomKeyGenerator()
    // base64或者bytes
    mode = com.github.megatronking.stringfog.plugin.StringFogMode.bytes
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
    implementation("org.nanohttpd:nanohttpd:2.3.1")
//局域网http服务器搭建库
    implementation("org.nanohttpd:nanohttpd-websocket:2.3.1")
//局域网http服务器搭建库
    implementation("com.google.zxing:core:3.5.4")
//字符串生成二维码库
    //noinspection UseTomlInstead
    implementation("androidx.room:room-runtime:2.8.4")
    //noinspection UseTomlInstead
    annotationProcessor("androidx.room:room-compiler:2.8.4")

    implementation("com.github.megatronking.stringfog:xor:5.0.0")
}