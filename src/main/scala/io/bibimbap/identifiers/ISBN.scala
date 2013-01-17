package io.bibimbap
package identifiers

object ISBN extends Identifier {
  private val nonDigits = """[^0123456789xX]""".r
  private val isbn10re = """^\d{9}[\dX]$""".r
  private val isbn13re = """^\d{13}$""".r

  def extract(from : String) : Option[String] = {
    val digitsOnly = nonDigits.replaceAllIn(from, "").toUpperCase
    if(digitsOnly.length < 10) return None
    
    isbn13re.findFirstIn(digitsOnly).filter(isValidISBN13) orElse
    isbn10re.findFirstIn(digitsOnly).filter(isValidISBN10)
  }

  def isValid(id : String) : Boolean = {
    (id.length == 10 && isValidISBN10(id)) ||
    (id.length == 13 && isValidISBN13(id))
  }

  private def isValidISBN10(in : String) : Boolean = try {
    val ints = in.take(9).map(_.toString.toInt).toIndexedSeq
    val last = in(9) match {
      case 'x' | 'X' => 10
      case n => n.toString.toInt
    }
    var s = 0; var m = 10; var i = 0
    while(i < 9) {
      s = s + m * ints(i)
      m = m - 1
      i = i + 1
    }
    ((s + last) % 11) == 0
  } catch {
    case nfe : NumberFormatException => false
  }

  private def isValidISBN13(in : String) : Boolean = try {
    val ints = in.map(_.toString.toInt).toIndexedSeq
    var s = 0; var m = 1; var i = 0
    while(i < 12) {
      s = s + m * ints(i)
      m = 4 - m
      i = i + 1
    }
    val check = (10 - (s % 10)) % 10
    ints.last == check
  } catch {
    case nfe : NumberFormatException => false
  }
}
