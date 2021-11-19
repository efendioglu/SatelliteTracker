import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {

    //android ui
    private val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    private val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    private val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    private val material = "com.google.android.material:material:${Versions.material}"
    private val lifecycleLivedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleKtx}"
    private val lifecycleViewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleKtx}"
    private val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
    private val swiperefreshlayout = "androidx.recyclerview:recyclerview:${Versions.swiperefreshlayout}"
    private val cardview = "androidx.recyclerview:recyclerview:${Versions.cardview}"

    //Koin
    private val koinAndroid = "io.insert-koin:koin-android:${Versions.koin}"
    private val koinJunit = "io.insert-koin:koin-test-junit4:${Versions.koin}"

    //Ktor
    private val ktorAndroid = "io.ktor:ktor-client-android:${Versions.ktor}"
    private val ktorSerialization = "io.ktor:ktor-client-serialization:${Versions.ktor}"
    private val ktorLogging = "io.ktor:ktor-client-logging:${Versions.ktor}"

    private val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serialization}"

    //test libs
    private val junit = "junit:junit:${Versions.junit}"
    private val extJUnit = "androidx.test.ext:junit:${Versions.extJunit}"
    private val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    private val mockito = "org.mockito:mockito-core:${Versions.mockito}"



    val appLibraries = arrayListOf<String>().apply {
        add(coreKtx)
        add(appcompat)
        add(constraintLayout)
        add(material)
        add(lifecycleLivedata)
        add(lifecycleViewmodel)
        add(recyclerview)
        add(swiperefreshlayout)
        add(cardview)
        add(koinAndroid)
        add(koinJunit)
        add(ktorAndroid)
        add(ktorSerialization)
        add(ktorLogging)
        add(serialization)
    }

    val androidTestLibraries = arrayListOf<String>().apply {
        add(extJUnit)
        add(espressoCore)
    }

    val testLibraries = arrayListOf<String>().apply {
        add(junit)
        add(mockito)
    }
}

//util functions for adding the different type dependencies from build.gradle file
fun DependencyHandler.kapt(list: List<String>) {
    list.forEach { dependency ->
        add("kapt", dependency)
    }
}

fun DependencyHandler.implementation(list: List<String>) {
    list.forEach { dependency ->
        add("implementation", dependency)
    }
}

fun DependencyHandler.androidTestImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
}

fun DependencyHandler.testImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("testImplementation", dependency)
    }
}