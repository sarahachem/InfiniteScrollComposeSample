buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = uri("https://www.jitpack.io"))
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0-alpha14")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.30")
    }
}

plugins {
    `kotlin-dsl`
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = uri("https://www.jitpack.io")) }
}

subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            // Enable Kotlin backend compilation
            useIR = true
            // Treat all Kotlin warnings as errors
            allWarningsAsErrors = false
            freeCompilerArgs += "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
            freeCompilerArgs += "-Xopt-in=kotlin.Experimental"

            freeCompilerArgs += "-Xjvm-default=all"

            jvmTarget = "11"
        }
    }
}