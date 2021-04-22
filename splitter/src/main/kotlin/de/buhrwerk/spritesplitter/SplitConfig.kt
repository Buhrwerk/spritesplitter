package de.buhrwerk.spritesplitter

import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon
import java.io.File

data class SplitConfig(
    val width: Int = 32,
    val height: Int = 32,
    val frames: List<Frame> = emptyList()
) {

    companion object {

        val DEFAULT = SplitConfig()
    }
}

data class Frame(
    val tagName: String,
    val index: Int,
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
)

class ConfigReader(
    private val marshaller: Klaxon = Klaxon()
) {

    fun readJsonFile(jsonFile: File): SplitConfig {
        val json = marshaller.parse<AsepriteJson>(jsonFile)
            ?: throw AbortSplitterException("Specified Config file could not be parsed: ${jsonFile.absolutePath}")

        val frames = json.meta.frameTags.flatMap { frameTag ->
            val asepriteFrames = (frameTag.from .. frameTag.to).map { index -> json.frames[index] }
            asepriteFrames.mapIndexed { index, frameMeta ->
                Frame(
                    tagName = frameTag.name,
                    index = index,
                    x = frameMeta.frame.x,
                    y = frameMeta.frame.y,
                    width = frameMeta.frame.width,
                    height = frameMeta.frame.height,
                )
            }

        }
        return SplitConfig(frames = frames)
    }
}


data class AsepriteJson(
    val frames: List<AsepriteFrameMeta>,
    val meta: AsepriteMeta
)

data class AsepriteFrameMeta(
    val frame: AsepriteFrame
)

data class AsepriteFrame(
    val x: Int,
    val y: Int,
    @Json(name = "w") val width: Int,
    @Json(name = "h") val height: Int,
)

data class AsepriteMeta(
    val image: String,
    val frameTags: List<AsepriteFrameTag>
)

data class AsepriteFrameTag(
    val name: String,
    val from: Int,
    val to: Int,
)
