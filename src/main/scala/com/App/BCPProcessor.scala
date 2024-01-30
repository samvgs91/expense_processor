package com.App

import org.apache.logging.log4j.LogManager

import com.Processor.BCPParser._
import com.Save.ExcelCreditCardTransactionSave._
import com.Processor.ExcelParser

object BCPProcessor extends App {

  val logger = LogManager.getLogger(getClass.getName)

  val file = "resources/bcp_data.xlsx"
  val outputFile = "output/bcp_processed_data.xlsx"
  val parameterColumn = 1
  
  logger.info("Starting BCP Processing")

  // val parsedTransactions =  Parse(file)
  // creditCardTransactionSave(outputFile,parsedTransactions)



  print("Ingresar la ruta de datos a procesar: ")
  val userPath = scala.io.StdIn.readLine()

  print("Ingresar la ruta donde guardar los datos procesados: ")
  val transaccionesPath = scala.io.StdIn.readLine()
  
  if (userPath.length() > 0 & transaccionesPath.length()>0){

    logger.info(s"La ruta de donde se obtendrá los datos es: ${userPath}")
    logger.info(s"La ruta de donde se guardará los datos es: ${transaccionesPath}")

    val parsedTransactions =  Parse(userPath)
    logger.info("Processing transacctions")
    creditCardTransactionSave(transaccionesPath,parsedTransactions)
    logger.info("Finished BCP Processing")
  }
  else {

    println("No se ingresaron las dos rutas. Por favor intentar otra vez.")
    logger.info("No se ingresaron las dos rutas. Por favor intentar otra vez.")
  }
  
}
