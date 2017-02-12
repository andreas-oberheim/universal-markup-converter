/*
 * MIT License
 * Copyright (c) 2016 Andreas M. Oberheim
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.universal_markup_converter.api

class Convert(private val inputType: InputType,
              private val outputType: OutputType,
              private val fromFile: String,
              private val toFile: String) {

    companion object {
        fun from(from: InputType) = Convert(from, OutputType.NONE, "", "")
    }

    fun to(to: OutputType) = Convert(inputType, to, fromFile, toFile)
    fun fromFile(file: String) = Convert(inputType, outputType, file, toFile)
    fun toFile(file: String) = Convert(inputType, outputType, fromFile, file).convert()

    private fun convert() {
        val converter: AbstractMarkupConverter
        when (inputType) {
            InputType.MARKDOWN -> {
                when (outputType) {
                    OutputType.HTML -> converter = FlexmarkMarkdownToHtmlConverter(fromFile, toFile)
                    else -> throw Exception("unknown input type $inputType")
                }
            }
            InputType.ASCIIDOC -> {
                when (outputType) {
                    OutputType.HTML -> converter = AsciidocToHtmlConverter(fromFile, toFile)
                    OutputType.XHTML -> converter = AsciidocToXhtmlConverter(fromFile, toFile)
                    OutputType.DOCBOOK -> converter = AsciidocToDocBookConverter(fromFile, toFile)
                    OutputType.DOCBOOK45 -> converter = AsciidocToDocBook45Converter(fromFile, toFile)
                    OutputType.MANPAGE -> converter = AsciidocToManPageConverter(fromFile, toFile)
                    else -> throw Exception("unknown input type $inputType")
                }
            }
            InputType.RST -> {
                when (outputType) {
                    OutputType.HTML -> converter = ReStructuredTextToHtmlConverter(fromFile, toFile)
                    OutputType.PDF -> converter = ReStructuredTextToPdfConverter(fromFile, toFile)
                    else -> throw Exception("unknown input type $inputType")
                }
            }
            InputType.RESTRUCTURED_TEXT -> {
                when (outputType) {
                    OutputType.HTML -> converter = ReStructuredTextToHtmlConverter(fromFile, toFile)
                    OutputType.PDF -> converter = ReStructuredTextToPdfConverter(fromFile, toFile)
                    else -> throw Exception("unknown input type $inputType")
                }
            }
            InputType.EXCEL -> {
                when (outputType) {
                    OutputType.CSV -> converter = ExcelToCsvConverter(fromFile, toFile)
                    else -> throw Exception("unknown input type $inputType")
                }
            }
            InputType.RESTRUCTURED_TEXT -> throw Exception("unknown input type $inputType")
            InputType.ORG_MODE -> throw Exception("unknown input type $inputType")
            InputType.MEDIAWIKI -> throw Exception("unknown input type $inputType")
            else -> throw Exception("unknown input type $inputType")
        }
        converter.convert()
    }

}

