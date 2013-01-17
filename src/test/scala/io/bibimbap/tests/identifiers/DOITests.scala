package io.bibimbap.tests
package identifiers

import io.bibimbap.identifiers._

import org.scalatest.FunSuite

class IdentifierTests extends FunSuite {

  test("DOI extraction") {
    def e(src : String, doi : String) = DOI.extract(src) match {
      case None => assert(false, "The DOI [%s] should have been extracted from the string [%s].".format(doi, src))
      case Some(d) => assert(d === doi)
    }

    e("http://doi.acm.org/10.1145/1730804.1730807", "10.1145/1730804.1730807")
    e(" doi.acm.org/10.1145/1730804.1730807 ", "10.1145/1730804.1730807")
    e("doi:10.1000/189", "10.1000/189")
    e("strange.web.site/10.2456/jpkl_156.12", "10.2456/jpkl_156.12")
  }

  test("ISBN validity") {
    def e(src : String, isbn : String) = ISBN.extract(src) match {
      case None => assert(false, "The ISBN [%s] should have been extracted from the string [%s].".format(isbn, src))
      case Some(d) => assert(d === isbn)
    }

    def v(src : String) =
      assert(ISBN.isValid(src),
             "The ISBN [%s] should have been recognized as valid.".format(src))

    def i(src : String) =
      assert(!ISBN.isValid(src),
             "The ISBN [%s] should have been recognized as invalid.".format(src))

    e("ISBN 978-0-521-63124-2", "9780521631242")
    e("ISBN 0-7923-9532-8",     "0792395328")
    e("9 780792 395324",        "9780792395324")

    // Some ISBN-10...
    v("0865591725")
    v("2852265478") 
    v("0894682415")
    v("2850880701")
    v("0894682105")
    v("0810966379")
    v("912115628X")
    v("912115628x")
    i("9121156280")

    // ...and some ISBN-13
    v("9780306406157")
    v("9781401302375") 
    v("9780321205681")
    v("9783468490439")
    v("9788535911039")
    v("9780316346627")
    v("9780470375044")
    v("9780596529321")
    v("9780131479418")
    v("9780201485677")
    v("9781934356050")
    i("9780231479418")
    i("9780201485767")
    i("9781924356050")
  }
}
