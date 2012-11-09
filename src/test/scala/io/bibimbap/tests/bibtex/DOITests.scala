package io.bibimbap.tests
package bibtex

import org.scalatest.FunSuite

class DOITests extends FunSuite {
  import io.bibimbap.bibtex.DOI

  test("DOI extraction") {
    def c(src : String, doi : String) = DOI.extract(src) match {
      case None => assert(false, "The DOI [%s] should have been extracted from the string [%s].".format(doi, src))
      case Some(d) => assert(d === doi)
    }

    c("http://doi.acm.org/10.1145/1730804.1730807", "10.1145/1730804.1730807")
    c(" doi.acm.org/10.1145/1730804.1730807 ", "10.1145/1730804.1730807")
    c("doi:10.1000/189", "10.1000/189")
    c("strange.web.site/10.2456/jpkl_156.12", "10.2456/jpkl_156.12")
  }
}
