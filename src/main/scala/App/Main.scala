package App

import Processor.ExcelParser


object Main extends App {
  val file = "resources/raw_data.xls"
  val parameterColumn = 1

  val rows = ExcelParser.parse(file, parameterColumn)
  println(rows)
}