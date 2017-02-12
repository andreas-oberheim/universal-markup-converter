package org.universal_markup_converter.api

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.profiles.pegdown.Extensions
import com.vladsch.flexmark.profiles.pegdown.PegdownOptionsAdapter
import java.io.File

class FlexmarkMarkdownToHtmlConverter(fromFile: String,
                                      toFile: String) : AbstractMarkupConverter(fromFile, toFile) {

    override fun convert() {
        val options = PegdownOptionsAdapter.flexmarkOptions(Extensions.ALL)
        val parser = Parser.builder(options).build()
        val document = parser.parse(File(fromFile).readText())
        val renderer = HtmlRenderer.builder(options).build()
        val html = renderer.render(document)
        File(toFile).writeText(html)
    }
}