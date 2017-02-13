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

import org.apache.poi.ss.usermodel.*
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.util.*

class ExcelToCsvConverter(fromFile: String,
                          toFile: String) : AbstractMarkupConverter(fromFile, toFile) {

    private var workbook: Workbook? = null
    private var maxRowWidth = 0
    private var formattingConvention = 0
    private var dataFormatter: DataFormatter? = null
    private var formulaEvaluator: FormulaEvaluator? = null
    private var separator: String? = null

    fun convert(fromFile: String, toFile: String) {

        this.convert(fromFile, toFile,
                DEFAULT_SEPARATOR, EXCEL_STYLE_ESCAPING)
    }

    override fun convert() = convert(fromFile, toFile)


    fun convert(fromFile: String, toFile: String,
                separator: String) {

        this.convert(fromFile, toFile,
                separator, EXCEL_STYLE_ESCAPING)
    }


    fun convert(fromFile: String, toFile: String,
                separator: String, formattingConvention: Int) {
        val source = File(fromFile)
        val destination = File(toFile)
        var destinationFilename: String? = null

        if (formattingConvention != EXCEL_STYLE_ESCAPING && formattingConvention != UNIX_STYLE_ESCAPING) {
            throw IllegalArgumentException("The value passed to the " + "formattingConvention parameter is out of range.")
        }

        this.separator = separator
        this.formattingConvention = formattingConvention
        this.openWorkbook(File(fromFile))
        this.saveCSVFile(convertToCSV())
    }


    private fun openWorkbook(file: File) {
        var fis: FileInputStream? = null
        try {
            fis = FileInputStream(file)

            this.workbook = WorkbookFactory.create(fis)
            this.formulaEvaluator = this.workbook!!.creationHelper.createFormulaEvaluator()
            this.dataFormatter = DataFormatter(true)
        } finally {
            if (fis != null) {
                fis.close()
            }
        }
    }

    private fun convertToCSV(): ArrayList<ArrayList<String>> {
        var sheet: Sheet? = null
        var row: Row? = null
        var lastRowNum = 0
        val csvData = ArrayList<ArrayList<String>>()

        val numSheets = this.workbook!!.numberOfSheets

        for (i in 0..numSheets - 1) {

            sheet = this.workbook!!.getSheetAt(i)
            if (sheet!!.physicalNumberOfRows > 0) {

                lastRowNum = sheet.lastRowNum
                for (j in 0..lastRowNum) {
                    row = sheet.getRow(j)
                    csvData.add(rowToCSV(row))
                }
            }
        }
        return csvData
    }


    private fun saveCSVFile(csvData: ArrayList<ArrayList<String>>) {
        var fileWriter: FileWriter? = null
        var writer: BufferedWriter? = null
        var line: ArrayList<String>? = null
        var buffer: StringBuffer? = null
        var csvLineElement: String? = null
        try {
            fileWriter = FileWriter(toFile)
            writer = BufferedWriter(fileWriter)

            for (i in csvData!!.indices) {
                buffer = StringBuffer()

                line = csvData!![i]
                for (j in 0..this.maxRowWidth - 1) {
                    if (line.size > j) {
                        csvLineElement = line[j]
                        if (csvLineElement != null) {
                            buffer.append(this.escapeEmbeddedCharacters(
                                    csvLineElement))
                        }
                    }
                    if (j < this.maxRowWidth - 1) {
                        buffer.append(this.separator)
                    }
                }

                writer.write(buffer.toString().trim { it <= ' ' })

                if (i < csvData!!.size - 1) {
                    writer.newLine()
                }
            }
        } finally {
            if (writer != null) {
                writer.flush()
                writer.close()
            }
        }
    }

    private fun rowToCSV(row: Row?): ArrayList<String> {
        var cell: Cell? = null
        var lastCellNum = 0
        val csvLine = ArrayList<String>()

        if (row != null) {

            lastCellNum = row.lastCellNum.toInt()
            for (i in 0..lastCellNum) {
                cell = row.getCell(i)
                if (cell == null) {
                    csvLine.add("")
                } else {
                    if (cell.cellTypeEnum != CellType.FORMULA) {
                        csvLine.add(this.dataFormatter!!.formatCellValue(cell))
                    } else {
                        csvLine.add(this.dataFormatter!!.formatCellValue(cell, this.formulaEvaluator))
                    }
                }
            }
            if (lastCellNum > this.maxRowWidth) {
                this.maxRowWidth = lastCellNum
            }
        }
        return csvLine
    }


    private fun escapeEmbeddedCharacters(field: String): String {
        var field = field
        var buffer: StringBuffer? = null

        if (this.formattingConvention == EXCEL_STYLE_ESCAPING) {

            if (field.contains("\"")) {
                buffer = StringBuffer(field.replace("\"".toRegex(), "\\\"\\\""))
                buffer.insert(0, "\"")
                buffer.append("\"")
            } else {
                buffer = StringBuffer(field)
                if (buffer.indexOf(this.separator) > -1 || buffer.indexOf("\n") > -1) {
                    buffer.insert(0, "\"")
                    buffer.append("\"")
                }
            }
            return buffer.toString().trim { it <= ' ' }
        } else {
            if (field.contains(this.separator!!)) {
                field = field.replace(this.separator!!.toRegex(), "\\\\" + this.separator!!)
            }
            if (field.contains("\n")) {
                field = field.replace("\n".toRegex(), "\\\\\n")
            }
            return field
        }
    }

    companion object {
        const val CSV_FILE_EXTENSION = ".csv"
        const val DEFAULT_SEPARATOR = ","
        const val EXCEL_STYLE_ESCAPING = 0
        const val UNIX_STYLE_ESCAPING = 1
    }
}
