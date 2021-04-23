/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package de.buhrwerk.spritesplitter

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
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
        val splitterTasks = mutableListOf<Task>()
        splitterConfig.all { config ->
            val task = project.tasks.register("spriteSplitter-${config.name}", SpriteSplitterTask::class.java) { task ->
                task.spriteSheet.set(config.spriteSheet)
                task.config.set(config.config)
                task.outDir.set(config.outDir)
                task.width.set(config.width)
                task.height.set(config.height)
                task.rowTags.set(config.rowTags)
            }
            splitterTasks.add(task.get())
        }
        project.tasks.register("spriteSplitter") { allTask ->
            splitterTasks.forEach { task ->
                allTask.dependsOn(task)
            }
        }
    }
}

abstract class SplitterConfigExtension @Inject constructor(val name: String) {

    abstract val spriteSheet: Property<File>
    abstract val config: Property<File>
    abstract val outDir: DirectoryProperty
    abstract val width: Property<Int>
    abstract val height: Property<Int>
    abstract val rowTags: ListProperty<String>
}
