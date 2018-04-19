package scorer.datasource

trait Fetch[-A, +B] {
  def fetch(source: A): B
}

object Fetch {
  def create[A, B](f: A => B): Fetch[A, B] = (source: A) => f(source)
}

class FetchOps[-A, +B](a: A)(implicit ev: Fetch[A, B]) {
  def fetch: B = ev.fetch(a)
}

object FetchOps {
  implicit def toFetchOps[A, B](a: A)(implicit ev: Fetch[A,B]): FetchOps[A, B] = new FetchOps[A,B](a)
}


