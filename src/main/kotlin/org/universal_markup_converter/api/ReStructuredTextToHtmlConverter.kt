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

import laika.api.Render
import laika.api.Transform
import scala.io.Codec

class ReStructuredTextToHtmlConverter(fromFile: String,
                                      toFile: String) : AbstractMarkupConverter(fromFile, toFile) {

    override fun convert() {
        val outputType = laika.render.`HTML$`.`MODULE$`

        (Transform.from(laika.parse.rst.`ReStructuredText$`.`MODULE$`)
                .to(outputType)
                .fromFile(fromFile, Codec.UTF8()) as Render.BinaryTarget)
                .toFile(toFile, Codec.UTF8())
    }
}
