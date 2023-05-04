package App

import Processor.ExcelParser
import Save.ExcelSave


object Main extends App {
  val file = "resources/raw_data.xls"
  val outputFile = "output/processed_data.xlsx"
  val parameterColumn = 1
  val skipTopRows = 1
  val listColumnsToSkip = List(3,4)

  val rows = ExcelParser.parse(file, parameterColumn,skipTopRows,listColumnsToSkip)
  //println(rows)

  ExcelSave.save(outputFile,rows)

}