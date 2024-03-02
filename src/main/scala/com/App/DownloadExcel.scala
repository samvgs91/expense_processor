package com.App

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.util.ByteString
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import scala.util.{Success,Failure}
import java.io.File
import akka.http.scaladsl.model.headers.RawHeader


object DownloadExcel extends App {
  

      // Actor system and materializer
    implicit val system: ActorSystem = ActorSystem("excel-download-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    import system.dispatcher


  // Endpoint to download Excel file
  val route =
    path("download-excel") {
      get {
        val workbook = new XSSFWorkbook()
        val sheet = workbook.createSheet("Sample Sheet")

        // Create sample data
        val rowData = List(
          List("Name", "Age", "Country"),
          List("John Doe", "30", "USA"),
          List("Jane Doe", "28", "UK"),
          List("Alice", "25", "Canada")
        )

        // Write data to Excel sheet
        for ((rowValues, rowIndex) <- rowData.zipWithIndex) {
          val row = sheet.createRow(rowIndex)
          for ((value, cellIndex) <- rowValues.zipWithIndex) {
            row.createCell(cellIndex).setCellValue(value)
          }
        }

        val tempFile = new File("sample-excel.xlsx")
        val fileOutputStream = new java.io.FileOutputStream(tempFile)
        workbook.write(fileOutputStream)
        fileOutputStream.close()

        val fileBytes = ByteString(java.nio.file.Files.readAllBytes(tempFile.toPath))
        tempFile.delete() // Delete temp file

        // Set the filename in the response headers
        respondWithHeader(RawHeader("Content-Disposition", s"attachment; filename=sample_excel.xlsx")) {
          complete(HttpEntity(ContentTypes.`application/octet-stream`, fileBytes))
        }
      }
    }


      // Start HTTP server
  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  bindingFuture.onComplete {
    case Success(binding) =>
      println(s"Server online at http://${binding.localAddress.getHostString}:${binding.localAddress.getPort}/")
    case Failure(ex) =>
      println(s"Server could not start: ${ex.getMessage}")
      system.terminate()
  }

}
