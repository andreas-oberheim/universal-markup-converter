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
		when (inputType) {
			InputType.MARKDOWN -> {
				val converter = MarkdownConverter(outputType, fromFile, toFile)
				converter.convert()
			}
			InputType.ASCIIDOC -> {
				val converter = AsciidocConverter(outputType, fromFile, toFile)
				converter.convert()
			}
			InputType.RESTRUCTURED_TEXT -> throw Exception("unknown input type $inputType")
			InputType.ORG_MODE -> throw Exception("unknown input type $inputType")
			InputType.MEDIAWIKI -> throw Exception("unknown input type $inputType")
			else -> throw Exception("unknown input type $inputType")
		}
	}

}

