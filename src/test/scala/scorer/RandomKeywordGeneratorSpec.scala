package scorer

import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.Inside._
import scorer.RandomKeywordGenerator.RandomKeywordGeneratorFromDataSource
import scorer.datasource.DataSource.Wiki

import scala.util.Success

class RandomKeywordGeneratorSpec extends FlatSpec with Matchers {
  "A wiki random keyword generator" should "generate a random search key word" in {
    val wikiRKG = new RandomKeywordGeneratorFromDataSource(Wiki)
    wikiRKG.nextKeyword should matchPattern { case (Success(_), _) => }
  }

  "keywords" should "return a proper list" in {
    import RandomKeywordGenerator._

    val wikiRKG = new RandomKeywordGeneratorFromDataSource(Wiki)
    val (r, _) = keywords(10)(wikiRKG)
    r should matchPattern { case Success(_) => }
    inside(r) { case Success(x) =>
        x.length shouldBe 10
    }
  }
}
