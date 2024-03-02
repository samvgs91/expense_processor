package com.App

import org.apache.logging.log4j.LogManager

import com.Processor.BCPParser._
import com.Save.ExcelCreditCardTransactionSave._
import com.Processor.ExcelParser
import com.Model.PBICreditCardTransaction

import java.text.SimpleDateFormat
import java.util.{Date, Calendar}
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object ReadProcessData extends App {

   import util._

   val logger = LogManager.getLogger(getClass.getName)

   logger.info("Starting Reading Processed Data")

   val path = "resources/Febrero_Gastos_Tarjetas_bcp_2024.xlsx"
   val rawPath = "resources/bcp_raw_data_20240229.xlsx"
   val whatYearMonth="2024-02"

   val (startDate, endDate) = getStartAndEndDate(whatYearMonth)

   


   val dataLoaded  = loadProcessedData(path)
   val newParsedData = ParseWithKey(rawPath)

   val maxTransaction = dataLoaded.maxByOption(data => data._2.Date)

   val calculatedStartDate =  maxTransaction match {
      case Some(tranMap) => 
            val _calculatedStartDate =  addDaysToDate(tranMap._2.Date,-1)
            logger.info(s"Max date processed ${ _calculatedStartDate}")
            _calculatedStartDate
      case _ => startDate
   }

   // logger.info(s"Reading after ${startDate.toString()} and before ${endDate.toString()}")
   logger.info(s"Reading after ${calculatedStartDate} and before ${endDate.toString()}")

   val missingValue = newParsedData.filterKeys(key => !dataLoaded.contains(key))


   val filteredNewParsedData =  newParsedData.filter { 
    case (k,_) => missingValue.contains(k)
   }

   // val dateFormat = new SimpleDateFormat("yyyy/MM/dd")
   val dateFormat = new SimpleDateFormat("yyyy/MM/dd")
   val afterDate = dateFormat.parse("2024/01/31")
   val beforeDate = dateFormat.parse("2024/03/01")


   val filteredByDates = filteredNewParsedData.filter {
      case (k,v) => v.Date.after(calculatedStartDate)
   }.filter {
      case (k,v) => v.Date.before(endDate)
   }


   val mergeData = dataLoaded ++ filteredByDates

   creditCardTransactionSave(path,mergeData.values.toSeq)

   logger.info("Finished Read Processed Data")
//    dataLoaded.foreach { r =>
//         println(r.toString())
//    }

}


object util {

   def getStartAndEndDate(yearMonth: String): (Date, Date) = {
    val yearMonthArray = yearMonth.split("-")
    val year = yearMonthArray(0).toInt
    val month = yearMonthArray(1).toInt

    val dateFormat = new SimpleDateFormat("yyyy-MM-dd")

    val startDate = LocalDate.of(year, month, 1)
    val endDate = startDate.withDayOfMonth(startDate.lengthOfMonth()).plusDays(1)

    val afterDate = dateFormat.parse(startDate.plusDays(-1).toString())
    val beforeDate = dateFormat.parse(endDate.toString())

    (afterDate, beforeDate)
  }

   def addDaysToDate(date: Date, days: Int): Date = {
   val calendar = Calendar.getInstance()
   calendar.setTime(date)
   calendar.add(Calendar.DAY_OF_YEAR, days)
   calendar.getTime // return the modified Date
   }
}