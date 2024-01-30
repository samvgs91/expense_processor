import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import java.util.Date
import java.text.SimpleDateFormat

// class ParserTest extends AnyFlatSpec with Matchers {
//   "The length of a string" should "be returned correctly" in {
//     val str = "Hello, world!"
//     str.length shouldBe 13
//   }
// }

class TestparseSeqRow extends AnyFlatSpec with Matchers {
  "The method TestparseSeqRow" should "return a correct CreditCardTransaction case class" in {
    val str = "Hello, world!"
    val data = Seq("29/04/2023","(P) ECM*SPOTIFY           4899","S/","18.90")
    val result = com.Processor.ExcelParser.parseSeqRow(data)

    val dateString = "29/04/2023"
    val dateFormat = new SimpleDateFormat("dd/MM/yyyy")
    val date = dateFormat.parse(dateString)

    val compare = com.Model.CreditCardTransaction(0
                                            ,date
                                            ,2023
                                            ,"abril"
                                            ,4
                                            ,0
                                            ,""
                                            ,0
                                            ,""
                                            ,"(P) ECM*SPOTIFY           4899"
                                            ,"S/"
                                            ,18.90
                                            ,0,0,0)


    result shouldEqual compare
  }
}