import org.gradle.jvm.tasks.Jar

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.openjfx.javafxplugin") version "0.1.0" // Add JavaFX plugin
}
javafx {
    version = "23.0.2" // Or your JavaFX version
    modules("javafx.controls", "javafx.fxml", "javafx.graphics")
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

    kotlinOptions {
        jvmTarget = "17"
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

kotlin {
    sourceSets {
        val main by getting
        val test by getting
    }
}
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.10")
//    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.4")

//        implementation("org.openjfx:javafx-controls:23.0.2")
//        implementation("org.openjfx:javafx-fxml:23.0.2")
//        implementation("org.openjfx:javafx-graphics:23.0.2")
//
//    implementation(
//        fileTree(
//            mapOf(
//                "dir" to "C:\\Users\\grade\\Documents\\openjfx-23.0.2_windows-x64_bin-sdk\\javafx-sdk-23.0.2\\lib",
//                "include" to listOf("*.aar", "*.jar"),
//                "exclude" to emptyList<String>()
//            )
//        )
//    )

    implementation("com.opencsv:opencsv:5.10")

    implementation("com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava")

//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
//    implementation("androidx.core:core-ktx:1.10.1")
//    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation("com.google.android.material:material:1.9.0")
//    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
//    implementation("androidx.recyclerview:recyclerview:1.3.1")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")


    implementation("com.acmerobotics.dashboard:core:0.4.16")
    implementation("com.github.GramGra07:GentrifiedAppsUtil:2.0.3-dev2")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.monitor)
    implementation(files("src/main/java/com/gentrifiedapps/heatseekersimulator/javafx.base.jar"))
    implementation(files("src/main/java/com/gentrifiedapps/heatseekersimulator/javafx.controls.jar"))
    implementation(files("src/main/java/com/gentrifiedapps/heatseekersimulator/javafx.fxml.jar"))
    implementation(files("src/main/java/com/gentrifiedapps/heatseekersimulator/javafx.graphics.jar"))
    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "17" // Adjust to your installed version
    targetCompatibility = "17" // Adjust to your installed version
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17" // Adjust to match your Java version
    }
}
tasks.register<Delete>("deleteJar") {
    delete("libs/jars/heatseekersimulator.jar")
}
tasks.register<Jar>("createJar") {
    dependsOn("deleteJar", "assembleDebug")

    archiveBaseName.set("heatseekersimulator")
    archiveVersion.set("1.0.0")

    destinationDirectory.set(file("libs/jars"))

    from("build/intermediates/javac/debug/classes")
    from("build/tmp/kotlin-classes/debug")

    // Include JavaFX JARs manually
    val javafxJars = configurations.getByName("debugRuntimeClasspath").filter {
        it.name.contains("javafx")
    }

    from(javafxJars.map { zipTree(it) }) {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    manifest {
        attributes(
            "Main-Class" to "com.gentrifiedapps.heatseekersimulator.RoboticsSimulatorKt"
        )
    }
}

tasks.register<Copy>("moveJar") {
    dependsOn("createJar")
    from("build/libs/")
    into("libs/jars/")
}