package com.Save

import java.io.{FileOutputStream,File}
import org.apache.poi.ss.usermodel.{Cell, Row, WorkbookFactory}
import org.apache.poi.xssf.usermodel.XSSFWorkbook
// import org.apache.poi.xssf.usermodel.XSSFWorkbook
import javax.security.auth.login.CredentialException
import com.Model.PBICreditCardTransaction

import java.text.SimpleDateFormat

object ExcelCreditCardTransactionSave {
  def save(fileName: String, rows: Seq[PBICreditCardTransaction]): Unit = ???

  def creditCardTransactionSave(fileName: String, rows: Seq[PBICreditCardTransaction]):Unit = {
    val workbook = new XSSFWorkbook()
    val sheet = workbook.createSheet("banco")

    val headerRow = sheet.createRow(0)

    headerRow.createCell(0).setCellValue("Fecha")
    headerRow.createCell(1).setCellValue("Tipo de Registro")
    headerRow.createCell(2).setCellValue("Categoria")
    headerRow.createCell(3).setCellValue("SubCategoria")
    headerRow.createCell(4).setCellValue("Detalle")
    headerRow.createCell(5).setCellValue("Tipo")
    headerRow.createCell(6).setCellValue("MontoOriginal")
    headerRow.createCell(7).setCellValue("TipoCambio")

    for (i <- 0 until rows.length) {
      val row = sheet.createRow(i + 1)
      // val dateFormat = new SimpleDateFormat("yyyy/MM/dd")
      val dateFormat = new SimpleDateFormat("dd/MM/yyyy")
      val formatedDate = dateFormat.format(rows(i).Date)

      row.createCell(0).setCellValue(formatedDate)
      row.createCell(1).setCellValue(rows(i).Type)
      row.createCell(2).setCellValue(rows(i).Category)
      row.createCell(3).setCellValue(rows(i).SubCategory)
      row.createCell(4).setCellValue(rows(i).Detail)
      row.createCell(5).setCellValue(rows(i).Currency)
      row.createCell(6).setCellValue(rows(i).Amount)
      row.createCell(7).setCellValue(rows(i).Exchange)
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
