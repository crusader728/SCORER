package scorer

import scorer.datasource.DataSource

import scala.util.{Failure, Success, Try}

trait RandomKeywordGenerator {
  def nextKeyword: (Try[String], RandomKeywordGenerator)
}

object RandomKeywordGenerator {

  class RandomKeywordGeneratorFromDataSource[DS <: DataSource](dataSource: DS) extends RandomKeywordGenerator {
    private val link = "https://en.wikipedia.org/wiki/Special:Random"

    override def nextKeyword: (Try[String], RandomKeywordGenerator) = {
      (dataSource.from(link), this)
    }
  }

  def keywords(n: Int)(rng: RandomKeywordGenerator): (Try[List[String]], RandomKeywordGenerator) = {
    if(n == 0) {
      (Success(List()), rng)
    } else {
      val (t1, r1) = rng.nextKeyword
      t1 match {
        case Success(k) => {
          val (ts, r2) = keywords(n - 1)(r1)
          ts match {
            case Success(ks) => (Success(k :: ks), r2)
            case Failure(e) => (Failure(e), r2)
          }
        }
        case Failure(e) => (Failure(e), r1)
      }
    }
  }
}
