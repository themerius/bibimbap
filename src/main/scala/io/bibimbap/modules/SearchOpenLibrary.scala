package io.bibimbap
package modules

import akka.actor._

import bibtex._
import strings._
import identifiers.ISBN
import json._

/** Currently, this provider can only lookup ISBN numbers. */
class SearchOpenLibrary(val repl : ActorRef, val console : ActorRef, val settings : Settings) extends SearchProvider with WebProvider {
  val name   = "Search Open Library"
  val source = "openlibrary"

  private val searchURL = "http://openlibrary.org/api/books?bibkeys=ISBN:%s&format=json&jscmd=data"

  override def search(terms : List[String], limit : Int) : SearchResults = {
    ISBN.extract(terms.mkString("")) match {
      case Some(isbn) =>
        HTTPQueryAsString(searchURL.format(isbn)) match {
          case Some(text) => SearchResults(extractJSONRecords(text))
          case None => SearchResults(Nil)
        }

      case None => SearchResults(Nil)
    }
  }

  private def extractJSONRecords(text : String) : List[SearchResult] = {
    new JSONParser().parseOpt(text) match {
      case Left(error) =>
        console ! Warning("Open Library returned malformed JSON data.")
        console ! Warning(error)
        Nil

      case Right(JObject(fs)) if(fs.size == 1) =>
        extractFromJSON(fs.head._2).map(_ :: Nil).getOrElse(Nil)

      case _ => Nil
    }
  }

  private def extractFromJSON(jvalue : JValue) : Option[SearchResult] = {
    val title : Option[String] = (jvalue \ "title") match {
      case JString(str) => Some(str)
      case _            => None
    }

    val authors : Seq[String] = (jvalue \ "authors") match {
      case JArray(elems) => elems.flatMap(_ match {
        case JObject(es) => es.get("name") match {
          case Some(JString(nm)) => Some(nm)
          case _ => None
        }
        case _ => None
      })
      case _ => Seq.empty
    }
    val authorsConcat : Option[String] = if(authors.isEmpty) None else Some(authors.mkString(" and "))

    val publisher : Option[String] = (jvalue \ "publishers") match {
      case JArray(elems) => elems.flatMap(_ match {
        case JObject(es) => es.get("name") match {
          case Some(JString(nm)) => Some(nm)
          case _ => None
        }
        case _ => None
      }).headOption
      case _ => None
    }

    val year : Option[String] = (jvalue \ "publish_date") match {
      case JString(d) => Some(d)
      case _ => None
    }

    val address : Option[String] = (jvalue \ "publish_places") match {
      case JArray(elems) => elems.flatMap(_ match {
        case JObject(es) => es.get("name") match {
          case Some(JString(nm)) => Some(nm)
          case _ => None
        }
        case _ => None
      }).headOption
      case _ => None
    }

    // TODO: keep ISBN around, look for potential editors, month, etc.

    val emap : Map[String,String] = Map[String,Option[String]](
      "title" -> title,
      "author" -> authorsConcat,
      "publisher" -> publisher,
      "address" -> address,
      "year" -> year
    ).filterNot(_._2.isEmpty).mapValues(_.get)

    val entry = BibTeXEntry.fromEntryMap(
      Some(BibTeXEntryTypes.Book), None, emap.mapValues(MString.fromJava),
      console ! Error(_))

    entry.map(SearchResult(_, Set(source), 1.0))
  }
}
