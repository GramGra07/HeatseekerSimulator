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
    id("org.openjfx.javafxplugin") version "0.1.0" // Add JavaFX plugin
}
javafx{
    version = "23.0.2" // Or your JavaFX version
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
        packagingOptions {
            exclude("META-INF/**")
        }
}

dependencies {
//    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.4")

    if (project.hasProperty("desktop")) {
        implementation("org.openjfx:javafx-controls:23.0.2")
        implementation("org.openjfx:javafx-fxml:23.0.2")
        implementation("org.openjfx:javafx-graphics:23.0.2")
    }
    implementation(fileTree(mapOf(
        "dir" to "C:\\Users\\grade\\Documents\\openjfx-23.0.2_windows-x64_bin-sdk\\javafx-sdk-23.0.2\\lib",
        "include" to listOf("*.aar", "*.jar"),
        "exclude" to emptyList<String>()
    )))

    implementation("com.opencsv:opencsv:5.10")

    implementation ("com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava")

//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
//    implementation("androidx.core:core-ktx:1.10.1")
//    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation("com.google.android.material:material:1.9.0")
//    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
//    implementation("androidx.recyclerview:recyclerview:1.3.1")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")

    implementation("com.acmerobotics.dashboard:dashboard:0.4.16")
    implementation("com.github.GramGra07:GentrifiedAppsUtil:2.0.3-dev2")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.monitor)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
tasks.register<Delete>("deleteJar") {
    delete("libs/jars/logmanagementlib.jar")
}

tasks.register<Copy>("createJar") {
    from("build/intermediates/bundles/release/")
    into("libs/jars/")
    include("classes.jar")
    rename("classes.jar", "logmanagementlib.jar")
}

tasks.named("createJar") {
    dependsOn("deleteJar", "build")
}