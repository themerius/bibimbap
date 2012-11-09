package io.bibimbap

case class ParseError(message : String) extends Exception(message)

trait Parser[T] {
  val typeDesc : String

  def parseOpt(input : String) : Either[String,T]

  def parse(input : String) : T = parseOpt(input) match {
    case Right(v) => v

    case Left(e) =>
      throw ParseError(typeDesc + " parsing error : " + e)
  }
}
