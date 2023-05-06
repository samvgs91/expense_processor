package Processor

import java.io.FileInputStream
import org.apache.poi.ss.usermodel.{Cell, Row}
import scala.collection.JavaConverters._
import org.apache.poi.ss.usermodel.{Cell, CellType, DataFormatter, WorkbookFactory}
import org.apache.poi.xssf.usermodel.{XSSFCell, XSSFRow, XSSFSheet, XSSFWorkbook}
import App.Main


object ExcelParser {
  def parse(file: String, parameterColumn: Int, skipTopRows: Int, skipColumns:List[Int]): Seq[Seq[String]] = {
    val workbook = WorkbookFactory.create(new FileInputStream(file))
    val sheet = workbook.getSheetAt(0) // assuming there is only one sheet in the workbook
    val dataFormatter = new DataFormatter()

   //first we drop columns list
   //then we drop top columns
   //finally we filter empty rows of parameter column

    val pre_matrix = sheet.iterator().asScala.toSeq
        .map(row => row.iterator().asScala
          .zipWithIndex
          .filterNot { case (_, index) => skipColumns.contains(index) }
          .drop(skipTopRows)
          .toSeq
          .map { case (cell, _) => dataFormatter.formatCellValue(cell).trim 
          }
        )
        .filter(row => row.lift(parameterColumn).exists(_.nonEmpty))

    var splited = splitMoneyValue(pre_matrix)
    splited.map( row => println(row.toString()))

    val matrix = removePayments(splited)

    matrix
  }

  private def splitMoneyValue(matrix: Seq[Seq[String]]): Seq[Seq[String]] = {
    matrix.zipWithIndex.map { case (row, index) =>
      if (index == 0) {
        row.dropRight(2) ++ Seq("Type", "Value")
      } else {
        val (init, last) = row.splitAt(2)
        val splitLast = last.head.split(" ")
        init ++ splitLast
      }
    }
  }

  private def removePayments(matrix: Seq[Seq[String]]): Seq[Seq[String]] = {

    val filtered = matrix.head +: matrix.tail.filter { row =>
                      val lastValue = row.last
                      if(lastValue.toDouble > 0.0) true else false
                  }

    filtered
  }
}
