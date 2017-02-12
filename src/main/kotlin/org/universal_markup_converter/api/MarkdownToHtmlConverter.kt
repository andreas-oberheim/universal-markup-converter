package org.universal_markup_converter.api

import laika.api.Render
import laika.api.Transform
import scala.io.Codec

class MarkdownToHtmlConverter(fromFile: String,
                              toFile: String) : AbstractMarkupConverter(fromFile, toFile) {

    override fun convert() {
        val outputType = laika.render.`HTML$`.`MODULE$`

        (Transform.from(laika.parse.markdown.`Markdown$`.`MODULE$`)
                .to(outputType)
                .fromFile(fromFile, Codec.UTF8()) as Render.BinaryTarget)
                .toFile(toFile, Codec.UTF8())
    }
}