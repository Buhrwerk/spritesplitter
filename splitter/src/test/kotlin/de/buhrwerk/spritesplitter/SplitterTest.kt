package de.buhrwerk.spritesplitter

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import javax.imageio.ImageIO

internal class SplitterTest {

    private val splitter: Splitter = Splitter()

    @TempDir
    lateinit var tmpDir: File

    @Test
    fun `split single row of images with default size 32x32 px`() {
        val file = loadImage("test-row-32x32-noPadding.png")
        val outDir = File(tmpDir, "out")

        splitter.split(file, outDir)

        assertThat(outDir).exists()
        val imageFiles = outDir.listFiles()
        requireNotNull(imageFiles) { "no Images found!" }
        assertThat(imageFiles).extracting("name").contains(
            "test-row-32x32-noPadding_000.png",
            "test-row-32x32-noPadding_001.png",
            "test-row-32x32-noPadding_002.png",
            "test-row-32x32-noPadding_003.png",
            "test-row-32x32-noPadding_004.png",
        )

        assertImageSizes(imageFiles, 32)
    }

    @Test
    fun `split single row of images with size 16x16 px`() {
        val file = loadImage("test-row-16x16-noPadding.png")
        val outDir = File(tmpDir, "out")

        splitter.split(file, outDir, SplitConfig(16, 16))

        assertThat(outDir).exists()
        val imageFiles = outDir.listFiles()
        requireNotNull(imageFiles) { "no Images found!" }
        assertThat(imageFiles).extracting("name").contains(
            "test-row-16x16-noPadding_000.png",
            "test-row-16x16-noPadding_001.png",
            "test-row-16x16-noPadding_002.png",
            "test-row-16x16-noPadding_003.png",
            "test-row-16x16-noPadding_004.png",
        )

        assertImageSizes(imageFiles, 16)
    }

    @Test
    fun `splits by configured frames`() {
        val file = loadImage("colors-32x32.png")
        val outDir = File(tmpDir, "out")

        splitter.split(
            file, outDir, SplitConfig(
                999, 999, frames = listOf(
                    Frame("Tag1", 0, 0, 0, 32, 32),
                    Frame("Tag1", 1, 32, 0, 32, 32),
                    Frame("Tag1", 2, 64, 0, 32, 32),
                    Frame("Tag2", 0, 0, 32, 32, 32),
                    Frame("Tag2", 1, 32, 32, 32, 32),
                    Frame("Tag2", 2, 64, 32, 32, 32),
                )
            )
        )

        val imageFiles = outDir.listFiles()
        requireNotNull(imageFiles) { "no Images found!" }
        assertThat(imageFiles).extracting("name").containsExactlyInAnyOrder(
            "colors-32x32-Tag1_000.png",
            "colors-32x32-Tag1_001.png",
            "colors-32x32-Tag1_002.png",
            "colors-32x32-Tag2_000.png",
            "colors-32x32-Tag2_001.png",
            "colors-32x32-Tag2_002.png",
        )

        assertImageSizes(imageFiles, 32)
    }

    @Test
    fun `split by rowTags and size`() {
        val file = loadImage("colors-32x32.png")
        val outDir = File(tmpDir, "out")

        splitter.split(file, outDir, SplitConfig(32, 32, rowTags = listOf("TagA", "TagB", "TagC")))

        val imageFiles = outDir.listFiles()
        requireNotNull(imageFiles) { "no Images found!" }
        assertThat(imageFiles).extracting("name").containsExactlyInAnyOrder(
            "colors-32x32-TagA_000.png",
            "colors-32x32-TagA_001.png",
            "colors-32x32-TagA_002.png",
            "colors-32x32-TagB_000.png",
            "colors-32x32-TagB_001.png",
            "colors-32x32-TagB_002.png",
            "colors-32x32-TagC_000.png",
            "colors-32x32-TagC_001.png",
        )

        assertImageSizes(imageFiles, 32)
    }

    @Test
    fun `split by rowTags and size and writes empty images`() {
        val file = loadImage("colors-32x32.png")
        val outDir = File(tmpDir, "out")

        splitter.split(
            file, outDir, SplitConfig(
                32,
                32,
                rowTags = listOf("TagA", "TagB", "TagC"),
                ignoreEmpty = false
            )
        )

        val imageFiles = outDir.listFiles()
        requireNotNull(imageFiles) { "no Images found!" }
        assertThat(imageFiles).extracting("name").containsExactlyInAnyOrder(
            "colors-32x32-TagA_000.png",
            "colors-32x32-TagA_001.png",
            "colors-32x32-TagA_002.png",
            "colors-32x32-TagB_000.png",
            "colors-32x32-TagB_001.png",
            "colors-32x32-TagB_002.png",
            "colors-32x32-TagC_000.png",
            "colors-32x32-TagC_001.png",
            "colors-32x32-TagC_002.png",
        )

        assertImageSizes(imageFiles, 32)
    }

    private fun assertImageSizes(imageFiles: Array<out File>, width: Int, height: Int = width) {
        imageFiles.forEach {
            val image = ImageIO.read(it)
            assertThat(image.width).isEqualTo(width)
            assertThat(image.height).isEqualTo(height)
        }
    }

    private fun loadImage(name: String): File {
        val path =
            this.javaClass.getResource(name)?.file ?: throw KotlinNullPointerException("Resource not found: $name")
        return File(path)
    }
}
