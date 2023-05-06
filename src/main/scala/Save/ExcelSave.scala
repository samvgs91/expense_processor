package Save

import java.io.{FileOutputStream,File}
// import org.apache.poi.ss.usermodel.{Cell, Row, WorkbookFactory}
import org.apache.poi.xssf.usermodel.XSSFWorkbook

object ExcelSave {
  def save(fileName: String, rows: Seq[Seq[Any]]): Unit = {
    val workbook = new XSSFWorkbook()
    val sheet = workbook.createSheet()
    for ((row, rowIndex) <- rows.zipWithIndex) {
      val sheetRow = sheet.createRow(rowIndex)
      for ((cellValue, columnIndex) <- row.zipWithIndex) {
        val cell = sheetRow.createCell(columnIndex)
        cell.setCellValue(cellValue.toString)
      }
    }
    val file = new File(fileName)
    val parent = file.getParentFile
    if (parent != null && !parent.exists()) {
      parent.mkdirs()
    }

    if (file.exists()) {
      file.delete()
    }

    val outputStream = new FileOutputStream(file)
    workbook.write(outputStream)
    outputStream.close()
  }
}
