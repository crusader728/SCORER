package page

sealed trait Page

final case class Form(get: Map[String, String]) extends Page

final case class Info(get: String) extends Page
