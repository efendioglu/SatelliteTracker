import org.gradle.api.artifacts.dsl.DependencyHandler

object Classpaths {
    private val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.gradle}"
    private val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    private val serializationGradlePlugin = "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"
    private val sqlDelightGradlePlugin = "com.squareup.sqldelight:gradle-plugin:${Versions.sqlDelight}"

    val list = arrayListOf<String>().apply {
        add(androidGradlePlugin)
        add(kotlinGradlePlugin)
        add(serializationGradlePlugin)
        add(sqlDelightGradlePlugin)
    }
}


fun DependencyHandler.classpaths(list: List<String>) = list.forEach { path ->
    add("classpath", path)
}