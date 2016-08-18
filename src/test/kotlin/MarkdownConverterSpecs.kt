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
import com.google.common.io.Resources.*
import org.jetbrains.spek.api.Spek
import org.universal_markup_converter.api.Convert
import org.universal_markup_converter.api.InputType
import org.universal_markup_converter.api.OutputType
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MarkdownConverterSpecs : Spek({

    given("sample markdown file") {
        val inputFile = File(getResource("sample.md").toURI())
        val outputFile = File(inputFile.toString() + ".html")

        on("converting to html") {
            Convert.from(InputType.MARKDOWN)
                    .to(OutputType.HTML)
                    .fromFile(inputFile.toString())
                    .toFile(outputFile.toString())
            it("should exist $outputFile") {
                assertTrue(outputFile.exists(), "$outputFile doesn't exist")
            }
        }
    }
})

