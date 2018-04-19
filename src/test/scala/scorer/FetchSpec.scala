package scorer

import java.net.URL

import cats.Functor
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.scalatest.{FlatSpec, Matchers}
import scorer.datasource.Fetch

class FetchSpec extends FlatSpec with Matchers {
  "A url query" should "return a Document" in {
    import scorer.datasource.FetchOps._
    val url = new URL("https://www.google.com")
    implicit val urlQuery: Fetch[URL, Document] = Fetch.create(url => Jsoup.connect(url.toString).get())
    url.fetch shouldBe a [Document]
  }

  "A wiki header fetch" should "return a header" in {
    import scorer.datasource.FetchOps._
    import cats.implicits._
    implicit val fetchUrlFunctor: Functor[Fetch[URL, ?]] = new Functor[Fetch[URL, ?]] {
      override def map[A, B](fa: Fetch[URL, A])(f: A => B): Fetch[URL, B] = Fetch.create(url => f(fa.fetch(url)))
    }
    val url = new URL("https://en.wikipedia.org/wiki/Special:Random")
    implicit val fetchHeader: Fetch[URL, String] = Fetch
      .create((url: URL) => Jsoup.connect(url.toString).get())
      .map((doc: Document) => doc.select("h1.firstHeading"))
      .map((elements: Elements) => if(elements.isEmpty) "" else elements.first().text())
    url.fetch shouldBe a [String]
  }
}
