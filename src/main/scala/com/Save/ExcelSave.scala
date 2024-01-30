package com.Save

import java.io.{FileOutputStream,File}
// import org.apache.poi.ss.usermodel.{Cell, Row, WorkbookFactory}
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import javax.security.auth.login.CredentialException
import com.Model.CreditCardTransaction

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

  def creditCardTransactionSave(fileName: String, rows: Seq[CreditCardTransaction]):Unit = {
    val workbook = new XSSFWorkbook()
    val sheet = workbook.createSheet()

    val headerRow = sheet.createRow(0)

    headerRow.createCell(0).setCellValue("PK")
    headerRow.createCell(1).setCellValue("Fecha")
    headerRow.createCell(2).setCellValue("Año")
    headerRow.createCell(3).setCellValue("Mes")
    headerRow.createCell(4).setCellValue("nMes")
    headerRow.createCell(4).setCellValue("CodCategoria")
    headerRow.createCell(4).setCellValue("Categoria")
    headerRow.createCell(4).setCellValue("CodSubCategoria")
    headerRow.createCell(4).setCellValue("SubCategoria")
    headerRow.createCell(4).setCellValue("Detalle")
    headerRow.createCell(4).setCellValue("Tipo")
    headerRow.createCell(4).setCellValue("MontoOriginal")
    headerRow.createCell(4).setCellValue("TipoCambio")
    headerRow.createCell(4).setCellValue("MontoSoles")
    headerRow.createCell(4).setCellValue("MontoUSD")

    for (i <- 0 until rows.length) {
      val row = sheet.createRow(i + 1)
      row.createCell(0).setCellValue(rows(i).Pk.toDouble)
      row.createCell(0).setCellValue(rows(i).Date)
      row.createCell(0).setCellValue(rows(i).Year.toString())
      row.createCell(0).setCellValue(rows(i).Month)
      row.createCell(0).setCellValue(rows(i).NMonth.toDouble)
      row.createCell(0).setCellValue(rows(i).CodCategory.toDouble)
      row.createCell(0).setCellValue(rows(i).Category)
      row.createCell(0).setCellValue(rows(i).CodSubCategory.toDouble)
      row.createCell(0).setCellValue(rows(i).SubCategory)
      row.createCell(0).setCellValue(rows(i).Detail)
      row.createCell(0).setCellValue(rows(i).Currency)
      row.createCell(0).setCellValue(rows(i).OriginalAmount)
      row.createCell(0).setCellValue(rows(i).Exchange)
      row.createCell(0).setCellValue(rows(i).SolesAmount)
      row.createCell(0).setCellValue(rows(i).USDAmount)
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
