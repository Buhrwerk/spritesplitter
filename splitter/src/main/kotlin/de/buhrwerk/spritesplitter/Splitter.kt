package de.buhrwerk.spritesplitter

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

private const val DEFAULT_FORMAT = "png"

class Splitter {

    fun split(spriteSheet: File, outDir: File, splitConfig: SplitConfig = SplitConfig.DEFAULT) {
        outDir.mkdirs()

        val images = splitImages(spriteSheet, splitConfig)
        images.forEach { imageData ->
            ImageIO.write(imageData.image, DEFAULT_FORMAT, File(outDir, imageData.name))
        }
    }

    private fun splitImages(spriteSheet: File, splitConfig: SplitConfig): List<ImageData> {
        if (splitConfig.frames.isNotEmpty()) {
            return splitFrames(spriteSheet, splitConfig.frames)
        }
        return splitBySize(spriteSheet, splitConfig)
    }

    private fun splitBySize(spriteSheet: File, splitConfig: SplitConfig): List<ImageData> {
        val baseName = spriteSheet.nameWithoutExtension
        val sourceImage = ImageIO.read(spriteSheet)
        val (width, height) = splitConfig
        val rowCount: Int = sourceImage.height / height
        if (rowCount == 1) return splitSingleRow(sourceImage, splitConfig, baseName)
        val columnCount: Int = sourceImage.width / width
        val images = mutableListOf<ImageData>()
        repeat(rowCount) { rowIndex ->
            repeat(columnCount) { columnIndex ->
                val tagName = splitConfig.getRowTag(rowIndex)
                val name = String.format("%s-%s_%03d.png", baseName, tagName, columnIndex)
                val image = sourceImage.getSubimage(columnIndex * width, rowIndex * height, width, height)
                if (isNotEmpty(image) || !splitConfig.ignoreEmpty) {
                    images.add(ImageData(name, image))
                }
            }
        }
        return images
    }

    private fun splitSingleRow(
        sourceImage: BufferedImage,
        splitConfig: SplitConfig,
        baseName: String
    ): List<ImageData> {
        val (width, height) = splitConfig
        val columnCount: Int = sourceImage.width / width
        return (0 until columnCount).map { columnIndex ->
            val image = sourceImage.getSubimage(columnIndex * width, 0, width, height)
            val name = String.format("%s_%03d.png", baseName, columnIndex)
            ImageData(name, image)
        }
    }

    private fun splitFrames(sprite: File, frames: List<Frame>): List<ImageData> {
        val sourceImage = ImageIO.read(sprite)
        val name = sprite.nameWithoutExtension
        return frames.map { frame ->
            val outImage = sourceImage.getSubimage(frame.x, frame.y, frame.width, frame.height)
            ImageData(String.format("%s-%s_%03d.png", name, frame.tagName, frame.index), outImage)
        }
    }

    private fun isNotEmpty(image: BufferedImage): Boolean {
        val rgb = image.getRGB(0, 0, image.width, image.height, null, 0, image.width)
        return rgb.any { Color(it, true).alpha != 0 }
    }
}

data class ImageData(
    val name: String,
    val image: BufferedImage
)
