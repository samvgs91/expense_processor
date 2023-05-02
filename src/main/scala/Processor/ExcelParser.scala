package Processor

import java.io.FileInputStream
import scala.collection.JavaConverters._
import org.apache.poi.ss.usermodel.{Cell, CellType, DataFormatter, WorkbookFactory}

object ExcelParser {
  def parse(file: String, parameterColumn: Int): Seq[Seq[String]] = {
    val workbook = WorkbookFactory.create(new FileInputStream(file))
    val sheet = workbook.getSheetAt(0) // assuming there is only one sheet in the workbook
    val dataFormatter = new DataFormatter()

    sheet.iterator().asScala.toSeq
      .map(row => row.iterator().asScala.toSeq.map(cell => dataFormatter.formatCellValue(cell).trim))
      .filter(row => row.lift(parameterColumn).exists(_.nonEmpty))
  }
}
