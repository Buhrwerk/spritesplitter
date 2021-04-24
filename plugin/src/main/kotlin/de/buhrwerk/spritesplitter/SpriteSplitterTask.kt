package de.buhrwerk.spritesplitter

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import java.io.File
import javax.inject.Inject

abstract class SpriteSplitterTask(
    private val splitter: Splitter,
    private val configReader: ConfigReader,
) : DefaultTask() {

    @Inject
    constructor() : this(Splitter(), ConfigReader())

    @get:InputFile
    abstract val spriteSheet: Property<File>

    @get:OutputDirectory
    abstract val outDir: DirectoryProperty

    @get:Optional
    @get:InputFile
    abstract val config: Property<File>

    @get:Optional
    @get:Input
    abstract val width: Property<Int>

    @get:Optional
    @get:Input
    abstract val height: Property<Int>

    @get:Optional
    @get:Input
    abstract val rowTags: ListProperty<String>

    @get:Optional
    @get:Input
    abstract val ignoreEmpty: Property<Boolean>

    @TaskAction
    fun split() {
        val splitterConfig = buildConfig()
        splitter.split(spriteSheet.get(), outDir.asFile.get(), splitterConfig)
    }

    private fun buildConfig(): SplitConfig {
        if (config.isPresent)
            return configReader.readJsonFile(config.get())
        return SplitConfig(
            width = width.getOrElse(SplitConfig.DEFAULT_WIDTH),
            height = height.getOrElse(SplitConfig.DEFAULT_HEIGHT),
            rowTags = rowTags.getOrElse(emptyList()),
            ignoreEmpty = ignoreEmpty.getOrElse(true)
        )
    }
}
