package com.Processor

import java.io.FileInputStream
import scala.collection.JavaConverters._
import org.apache.poi.ss.usermodel.{Cell, Row}
import org.apache.poi.ss.usermodel.{Cell, CellType, DataFormatter, WorkbookFactory}
// import org.apache.poi.xssf.usermodel.{XSSFCell, XSSFRow, XSSFSheet, XSSFWorkbook}

object DinersParser {



    def Parse(file:String) = {
        val workbook = WorkbookFactory.create(new FileInputStream(file))
        val sheet = workbook.getSheetAt(0) // assuming there is only one sheet in the workbook
        val dataFormatter = new DataFormatter()

        val pre_matrix = sheet.iterator().asScala.toSeq
                            .map (
                                    row => row.iterator().asScala
                                    .filterNot { case c:Cell => 
                                        
                                        val index = c.getRowIndex()
                                        if (index == 0) true else false
                                    }.drop(10)
                                    .toSeq
                            )

        pre_matrix.map( row => 
                        print(row.toString())
                     )
  
    }
}
