package io.bibimbap
package modules

import io.bibimbap.bibtex._
import io.bibimbap.strings._

import akka.actor._

import java.io.File

import org.apache.pdfbox._
import org.apache.pdfbox.pdmodel._
import org.apache.pdfbox.util._

class PDF(val repl: ActorRef, val console: ActorRef, val settings: Settings) extends Module {
  val name = "pdf"

  override val dependsOn = Set("search")

  lazy val searchModule = modules("search")

  override def receive : Receive = {
    case Command2("scrap", path) => {
      scrap(path)
    }

    case x => super.receive(x)
  }

  override def complete(buffer: String, pos: Int): (List[String], Int) = {
    val Scrap = FileCompletor("scrap ")

    (buffer, pos) match {
      case Scrap(alts, pos) => (alts, pos)
      case _ => (Nil, 0)
    }
  }

  val helpItems = Map(
    "scrap" -> HelpEntry("scrap <path>", "Extract information and references from a PDF file.")
  )

  private def scrap(path : String) : Unit = {
    val file = new File(path)
    val document = PDDocument.load(file)
    val nfo = document.getDocumentInformation()

    println("Title  : " + nfo.getTitle())
    println("Author : " + nfo.getAuthor())

    val stripper = new PDFTextStripper()
    val text = stripper.getText(document)

    println(text) 
  }
}
