/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package de.buhrwerk.spritesplitter

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.domainObjectContainer
import org.gradle.kotlin.dsl.newInstance
import java.io.File
import javax.inject.Inject

/**
 * A simple 'hello world' plugin.
 */
class SpriteSplitterPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val objects = project.objects

        val splitterConfig = objects.domainObjectContainer(SplitterConfigExtension::class) { name ->
            objects.newInstance<SplitterConfigExtension>(name)
        }
        project.extensions.add("spriteSplitter", splitterConfig)
        // Register a task
        splitterConfig.all { config ->
            project.tasks.register("spriteSplitter-${config.name}", SpriteSplitterTask::class.java) { task ->
                task.sprite.set(config.sprite)
                task.config.set(config.config)
                task.outDir.set(config.outDir)
            }
        }

        project.tasks.register("greeting") {
            it.doLast {
                println("hello World")
                println(project.extensions.getByName("spriteSplitter"))
            }

        }
    }
}

abstract class SplitterConfigExtension @Inject constructor(val name: String) {

    abstract val sprite: Property<File>
    abstract val config: Property<File>
    abstract val outDir: DirectoryProperty
}