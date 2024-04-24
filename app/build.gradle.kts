import java.util.Locale

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs")
    jacoco
}

android {
    namespace = "com.example.pokeapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pokeapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            // enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    implementation("androidx.compose.material3:material3-window-size-class:1.2.1")
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Retrofit for API requests
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // ViewModel and LiveData for MVVM architecture
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.livedata)

    // Dependency injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Unit testing
    testImplementation(libs.junit)
    testImplementation(libs.androidx.core)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)

    // Instrumented testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    //Jacoco
    implementation(libs.org.jacoco.core)
}

kapt {
    correctErrorTypes = true
}

tasks.withType(Test::class) {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

val exclusions = listOf(
    // data binding
    "**/databinding/*",
    "android/databinding/**/*.class",
    "**/android/databinding/*Binding.class",
    "**/android/databinding/*",
    "**/androidx/databinding/*",
    "**/BR.*",

    // android
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*",
    "android/**/*.*",

    // kotlin
    "**/*MapperImpl*.*",
    "**/BuildConfig.*",
    "**/*Component*.*",
    "**/*BR*.*",
    "**/Manifest*.*",
    "**/*Companion*.*",
    "**/*Module*.*",
    "**/*Dagger*.*",
    "**/*Hilt*.*",
    "**/*MembersInjector*.*",
    "**/*_MembersInjector.class",
    "**/*_Factory*.*",
    "**/*_Provide*Factory*.*",
    "**/*Extensions*.*",

    // sealed and data classes
    "**/*$Result.*",
    "**/*$Result$*.*",

    // adapters generated by moshi
    "**/*JsonAdapter.*",

    // Hilt
    "**/*Module.kt",
    "**/di/**",
    "dagger.hilt.internal/*",
    "**/hilt_aggregated_deps/*",

    // Views and data clases
    "**/*Card*.*",
    "**/*Dialog*.*",
    "**/*Resources*.*",
    "**/theme/**",
    "**/constant/**/*",
    "**/entity/**/*",
    "**/*Application*.*",
    "**/*Retrofit*.*",
    "**/compose/**/*",

    // Uncomment this for instrumented testing
    "**/*Activity*.*",
    "**/navigation/**/*",
    "**/*UiState*.*",
    "**/*ScreenKt*.*",

    "**/*Args*.*", /* filtering Navigation Component generated classes */
    "**/*Directions*.*", /* filtering Navigation Component generated classes */
    "**/*inlined*.class", /* filtering inlined classes */
    "**/composables/**"
)

android {
    applicationVariants.all {
        val variantName = this.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }

        // Define task names for unit tests and Android tests
        val unitTests = "test${variantName}UnitTest"
        // val androidTests = "connected${variantName}AndroidTest"    --->> Uncomment to have instrumented tests

        tasks.register<JacocoReport>("Jacoco${variantName}CodeCoverage") {
            // Depend on unit tests and Android tests tasks
            dependsOn(listOf(
                unitTests,
                //androidTests   --->> Uncomment to have instrumented tests
                )
            )

            group = "Reporting"
            description = "Execute UI and unit tests, generate and combine Jacoco coverage report"
            
            reports {
                xml.required.set(true)
                html.required.set(true)
            }

            // Set source directories to the main source directory
            sourceDirectories.setFrom(layout.projectDirectory.dir("src/main"))
            // Set class directories to compiled Java and Kotlin classes, excluding specified exclusions
            classDirectories.setFrom(files(
                fileTree(layout.buildDirectory.dir("intermediates/javac/")) {
                    exclude(exclusions)
                },
                fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/")) {
                    exclude(exclusions)
                }
            ))
            // Collect execution data from .exec and .ec files generated during test execution
            executionData.setFrom(files(
                fileTree(layout.buildDirectory) { include(listOf("**/*.exec", "**/*.ec")) }
            ))
        }
    }
}