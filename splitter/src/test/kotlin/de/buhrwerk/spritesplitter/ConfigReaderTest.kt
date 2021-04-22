package de.buhrwerk.spritesplitter

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

internal class ConfigReaderTest {
    private val configReader = ConfigReader()

    @Test
    fun `should read json File`() {
        val configFile = loadFile()

        val splitterConfig = configReader.readJsonFile(configFile)

        assertThat(splitterConfig.frames).hasSize(6).contains(
            Frame("Tag1", 0, 0, 0, 32, 32),
            Frame("Tag1", 1, 32, 0, 32, 32),
            Frame("Tag1", 2, 64, 0, 32, 32),
            Frame("Tag2", 0, 0, 32, 32, 32),
            Frame("Tag2", 1, 32, 32, 32, 32),
            Frame("Tag2", 2, 64, 32, 32, 32),
        )
    }

    private fun loadFile(): File {
        val resource = this.javaClass.getResource("colors-32x32.json") ?: throw KotlinNullPointerException("Could not load file: colors-32x32.json")
        return File(resource.file)
    }
}
