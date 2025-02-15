import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val githubRepo = "acmerobotics/MeepMeep"
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
        minSdk = 34
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
}

dependencies {
    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")


    implementation( "com.acmerobotics.dashboard:dashboard:0.4.16")
    implementation("com.github.GramGra07:GentrifiedAppsUtil:2.0.3-dev2")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

sourceSets["main"].java {
    srcDir("src/main/kotlin")
}

sourceSets["test"].java {
    srcDir("src/test/kotlin")
}

// Create sources Jar from main kotlin sources
val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}

publishing {
    publications {
        create<MavenPublication>("heatseekerSimulator") {
            groupId = "com.gentrifiedapps.heatseekersimulator"
            artifactId = "heatseekerSimulator"
            version = "0.0.0"

            from(components["java"])
            artifact(sourcesJar)

            pom {
                packaging = "jar"
                name.set(rootProject.name)
                description.set(pomDesc)
                url.set(pomUrl)
                scm {
                    url.set(pomScmUrl)
                }
            }
        }
    }

    repositories {
        maven {
            url = uri("$buildDir/repository")
        }
    }
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}