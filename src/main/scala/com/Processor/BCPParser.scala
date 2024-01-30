package com.Processor

import java.io.FileInputStream

import java.text.SimpleDateFormat

import scala.collection.JavaConverters._

import org.apache.poi.ss.usermodel.{Cell, Row}
import org.apache.poi.ss.usermodel.{Cell, CellType, DataFormatter, WorkbookFactory}
// import org.apache.poi.xssf.usermodel.{XSSFCell, XSSFRow, XSSFSheet, XSSFWorkbook}

import com.Model.PBICreditCardTransaction

import com.Processor.CategoriesUtil.{subcategoryMap,categoryMap}
import com.Processor.CategoryParser._


object BCPParser {

    def Parse(file:String) = {

        val inputFileStream = new FileInputStream(file)
        val workbook = WorkbookFactory.create(inputFileStream)
        val sheet = workbook.getSheetAt(0) // assuming there is only one sheet in the workbook
        val dataFormatter = new DataFormatter()

        val pre_matrix = sheet.iterator().asScala.toSeq
                            .map (row => row.iterator().asScala.toSeq)
                            .drop(1)
                            .map(c  => c.map(cell  => dataFormatter.formatCellValue(cell).trim )  )
                            .map(row => parseRow(row))
                            .filter(row => row.Amount>0)

        inputFileStream.close()    

        pre_matrix
  
    }

    def parseRow(row:Seq[String]): PBICreditCardTransaction = {

        val strDate = row(0).toString()
        val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
        val date = dateFormat.parse(strDate)
        val rawAmount=row(3).toDouble
        val amount = -rawAmount
        val exchange = "3.75".toDouble

        val transaction = PBICreditCardTransaction(
                                         Date = date
                                        ,Type = "VISA BCP"
                                        ,Category = "Sin Categoría"
                                        ,SubCategory = "Sin SubCategoría"
                                        ,Detail = row(1)
                                        ,Currency = row(2)
                                        ,Amount = amount
                                        ,Exchange = exchange)

        val bucketedTransaction = bucketTransaction(transaction,subcategoryMap,categoryMap)
        
        bucketedTransaction
    }
}
