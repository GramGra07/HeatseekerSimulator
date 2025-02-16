import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val githubRepo = "GramGra07/HeatseekerSimulator"
val githubReadme = "README.md"

val pomUrl = "https://github.com/$githubRepo"
val pomScmUrl = "https://github.com/$githubRepo"
val pomIssueUrl = "https://github.com/$githubRepo/issues"
val pomDesc = "https://github.com/$githubRepo"

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

android {
    namespace = "com.gentrifiedapps.heatseekersimulator"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.gentrifiedapps.heatseekersimulator"
        targetSdk = 35
        minSdk = 30
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    sourceSets {
        getByName("main") {
            java.srcDirs("src/main/java")
            resources.srcDirs("src/main/res")
        }
    }

    // Configure publishing inside android block
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(libs.javafx.controls)
    implementation(libs.javafx.fxml)
    implementation(libs.javafx.graphics)

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")

    implementation("com.acmerobotics.dashboard:dashboard:0.4.16")
    implementation("com.github.GramGra07:GentrifiedAppsUtil:2.0.3-dev2")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(fileTree(mapOf(
        "dir" to "C:\\Users\\grade\\Documents\\openjfx-23.0.2_windows-x64_bin-sdk\\javafx-sdk-23.0.2\\lib",
        "include" to listOf("*.aar", "*.jar"),
        "exclude" to emptyList<String>()
    )))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
// Define resources JAR
val resourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("resources") // This sets the classifier (like 'resources.jar')
    from(android.sourceSets["main"].resources.srcDirs) // Includes all the resources from the main source set
}

// Define sources JAR
val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets["main"].java.srcDirs)
}

// Publishing block outside android
afterEvaluate {
    extensions.configure<PublishingExtension>("publishing") {
        publications {
            create<MavenPublication>("heatseekerSimulator") {
                from(components["release"]) // Ensure "release" variant exists in the android.publishing block
                artifact(sourcesJar)

                pom {
                    description.set(pomDesc)
                    url.set(pomUrl)
                    scm {
                        url.set(pomScmUrl)
                    }
                }
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
