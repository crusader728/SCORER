package scorer.datasource

import org.jsoup.Jsoup

import scala.util.{Failure, Success, Try}

trait DataSource {
  def from(source: String): Try[String]
}

object DataSource {
  class Wiki extends DataSource {
    override def from(source: String): Try[String] = {
      Try(Jsoup.connect(source).get())
        .map(_.select("firstHeading"))
        .flatMap {
          case x if x.isEmpty => Failure(new RuntimeException("empty title"))
          case y@_ => Success(y.first().text())
        }
    }
  }
}


