package io.bibimbap
package identifiers

object DOI extends Identifier {
  private val knownPrefixes : Set[String] = Set(
    "http://dx.doi.org/",
    "dx.doi.org/",
    "http://doi.acm.org/",
    "doi.acm.org/",
    "doi:"
  )

  private val Basic = """10\.\d\d\d\d/.*""".r

  // Pass it a URL, anything, and magic !
  def extract(from : String) : Option[String] = {
    val trimmed = from.trim

    for(prefix <- knownPrefixes if trimmed.startsWith(prefix)) {
      return Some(trimmed.substring(prefix.length))
    }

    // Defaulting to good old regexp.
    Basic.findFirstIn(from)
  }

  // We could do better, I'll give you that.
  def isValid(id : String) = true
}
