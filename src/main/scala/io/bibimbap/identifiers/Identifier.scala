package io.bibimbap
package identifiers

/** Defines a set of functions relative to some idenfier schema.
 *  E.g. DOI, ISBN, etc. */
trait Identifier {
  def extract(from : String) : Option[String]

  /** This should really be called "is not invalid", as checking validity is often
    * impossible. */
  def isValid(id : String) : Boolean
}
