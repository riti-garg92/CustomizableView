plugins {
    id("com.android.library")
    id("maven-publish")
}

android {
    namespace = "p.ritika.customizableview"

    defaultConfig {
        minSdk = 21
        compileSdk = 34
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
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
        multipleVariants {
            allVariants()
            withJavadocJar()
        }
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.riti-garg92"
            artifactId = "CustomizableView"
            version = "1.0"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}
