package de.buhrwerk.spritesplitter

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.util.stream.Stream

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

    @ParameterizedTest
    @MethodSource("rowTagArguments")
    fun `should return given row tag or default`(index: Int, tagName: String) {
        val splitConfig = SplitConfig(rowTags = listOf("TagA", "TagB"))

        assertThat(splitConfig.getRowTag(index)).isEqualTo(tagName)
    }


    private fun loadFile(): File {
        val resource = this.javaClass.getResource("colors-32x32.json")
            ?: throw KotlinNullPointerException("Could not load file: colors-32x32.json")
        return File(resource.file)
    }

    companion object {
        @JvmStatic
        private fun rowTagArguments(): Stream<Arguments> = Stream.of(
            Arguments.of(0, "TagA"),
            Arguments.of(1, "TagB"),
            Arguments.of(2, "row2"),
        )
    }
}
