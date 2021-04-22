package de.buhrwerk.spritesplitter

import java.io.File
import javax.imageio.ImageIO

private const val DEFAULT_FORMAT = "png"

class Splitter {

    fun split(sprite: File, outDir: File, splitConfig: SplitConfig = SplitConfig.DEFAULT) {
        outDir.mkdirs()

        if (splitConfig.frames.isNotEmpty()) {
            splitFrames(sprite, outDir, splitConfig.frames)
            return
        }

        val width = splitConfig.width
        val height = splitConfig.height

        val name = sprite.nameWithoutExtension
        val sourceImage = ImageIO.read(sprite)
        var x = 0
        var imageIndex = 0
        while (x < sourceImage.width) {
            val outImg = sourceImage.getSubimage(x, 0, width, height)
            ImageIO.write(outImg, DEFAULT_FORMAT, File(outDir, String.format("%s_%03d.png", name, imageIndex)))

            imageIndex++
            x += width
        }
    }

    private fun splitFrames(sprite: File, outDir: File, frames: List<Frame>) {
        val sourceImage = ImageIO.read(sprite)
        val name = sprite.nameWithoutExtension
        frames.forEach { frame ->
            val outImage = sourceImage.getSubimage(frame.x, frame.y, frame.width, frame.height)
            ImageIO.write(
                outImage,
                DEFAULT_FORMAT,
                File(outDir, String.format("%s-%s_%03d.png", name, frame.tagName, frame.index))
            )
        }
    }
}
