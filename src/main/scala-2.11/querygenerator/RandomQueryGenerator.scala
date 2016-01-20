package querygenerator

import org.jsoup.Jsoup
import scala.collection.JavaConversions._

/**
  * Created by IanLu on 1/19/16.
  */
object RandomQueryGenerator {
  val url = "https://en.wikipedia.org/wiki/Special:Random"

  def getQueriesFromWikipedia(n: Int): Vector[String] = {
    val doc = Jsoup.connect(url).get()
    val result = doc.select("#mw-content-text a[title]").toVector.map(elem =>elem.attr("title")).filter(!_.contains(":"))
    if(result.length >= n) {
      result.take(n)
    } else {
      val newLength = n - result.length
      result ++ getQueriesFromWikipedia(newLength)
    }
  }

}
