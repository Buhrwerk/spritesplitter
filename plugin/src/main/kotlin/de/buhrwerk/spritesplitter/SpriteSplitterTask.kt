package de.buhrwerk.spritesplitter

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import javax.inject.Inject

abstract class SpriteSplitterTask(
    private val splitter: Splitter,
    private val configReader: ConfigReader,
) : DefaultTask() {

    @Inject
    constructor(): this(Splitter(), ConfigReader())

    @get:InputFile
    abstract val sprite: Property<File>

    @get:InputFile
    abstract val config: Property<File>

    @get:OutputDirectory
    abstract val outDir: DirectoryProperty

    @TaskAction
    fun split() {
        val splitterConfig = configReader.readJsonFile(config.get())
        splitter.split(sprite.get(), outDir.asFile.get(), splitterConfig)
    }
}
