package com.Processor

import java.io.FileInputStream

import java.text.SimpleDateFormat

import scala.collection.JavaConverters._

import org.apache.poi.ss.usermodel.{Cell, Row}
import org.apache.poi.ss.usermodel.{Cell, CellType, DataFormatter, WorkbookFactory}
// import org.apache.poi.xssf.usermodel.{XSSFCell, XSSFRow, XSSFSheet, XSSFWorkbook}

import org.apache.logging.log4j.LogManager

import com.Model.PBICreditCardTransaction

import com.Processor.CategoriesUtil.{subcategoryMap,categoryMap}
import com.Processor.CategoryParser._

import java.security.MessageDigest
import java.util.Currency
import com.Model.CreditCardTransaction
import java.util.Date

object BCPParser {

    val logger = LogManager.getLogger(getClass.getName)

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

    def ParseWithKey(file:String): Map[String,PBICreditCardTransaction] = {

        val inputFileStream = new FileInputStream(file)
        val workbook = WorkbookFactory.create(inputFileStream)
        val sheet = workbook.getSheetAt(0) // assuming there is only one sheet in the workbook
        val dataFormatter = new DataFormatter()

        val pre_matrix = sheet.iterator().asScala.toSeq
                            .map (row => row.iterator().asScala.toSeq)
                            .drop(1)
                            .map(c  => c.map(cell  => dataFormatter.formatCellValue(cell).trim )  )
                            .map(row => parseRowWithKey(row))

        inputFileStream.close()    

        pre_matrix.flatten.toMap.filter(row => row._2.Amount>0)
    }

    def generateHash(Date: String, Description: String, Currency: String, Amount: String): String = {
        val concatenatedString = Date + Description + Currency + Amount
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(concatenatedString.getBytes("UTF-8"))
        val hashString = hashBytes.map("%02x".format(_)).mkString
        hashString
    }


    def loadProcessedData(processedFile:String):Map[String,PBICreditCardTransaction] = {
        //The parsing will be from a
        logger.info(s"Starting loading file ${processedFile}")

        val inputFileStream = new FileInputStream(processedFile)
        val workbook = WorkbookFactory.create(inputFileStream)
        val sheet = workbook.getSheetAt(0) // assuming there is only one sheet in the workbook
        val dataFormatter = new DataFormatter()

        val pre_matrix = sheet.iterator().asScala.toSeq
                            .map (row => row.iterator().asScala.toSeq)
                            .drop(1)
                            .map(c  => c.map(cell  => dataFormatter.formatCellValue(cell).trim )  )
                            .map(row => loadRow(row))

        inputFileStream.close()    

        pre_matrix.flatten.toMap
    }

    def parseRowDate(strDate:String):Date = {
        logger.info(s"Reading date from excel ${strDate}")
        if(strDate.split("/")(2).length()==2)
        {
            val dateFormat = new SimpleDateFormat("MM/dd/yy")
            dateFormat.parse(strDate)
        } else
        {
            val dateFormat = new SimpleDateFormat("dd/MM/yyyy")
            dateFormat.parse(strDate)
        }

    }

    def loadRow(row:Seq[String]): Map[String,PBICreditCardTransaction] = {

        
        val strDate = row(0).toString()
        logger.info(s"Reading date from excel ${strDate}")
        val strType = row(1).toString()
        // val dateFormat = new SimpleDateFormat("MM/dd/yy")
        val date = parseRowDate(strDate)
        val strCategory= row(2).toString()
        val strSubCategory= row(3).toString()
        logger.info(s"Reading sub category from excel ${strSubCategory}")
        val strDetalle= row(4).toString()
        val strCurrency = row(5).toString()
        val amount = row(6).toDouble
        logger.info(s"Reading amount from excel ${amount.toString()}")
        val exchange = row(7).toDouble

        val transaction = PBICreditCardTransaction(
                                         Date = date
                                        ,Type = strType
                                        ,Category = strCategory
                                        ,SubCategory = strSubCategory
                                        ,Detail = strDetalle
                                        ,Currency = strCurrency
                                        ,Amount = amount
                                        ,Exchange = exchange)
        
        

        val hashKey = generateHash(date.toString(),strDetalle,strCurrency,amount.toString())

        if (strSubCategory == "Sin SubCategoría"){
            val bucketedTransaction = bucketTransaction(transaction,subcategoryMap,categoryMap)
            Map(hashKey->bucketedTransaction)
        } else {
            Map(hashKey->transaction)
        }
    }

    def parseRowWithKey(row:Seq[String]): Map[String,PBICreditCardTransaction] = {

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
        val hashKey = generateHash(date.toString(),row(1),row(2),amount.toString())

        Map(hashKey->bucketedTransaction)
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
