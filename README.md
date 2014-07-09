# bibimbap-bibtex

This is a stripped down version of `bibimbap`, which only includes bibtex and latex
parsers provied as library.
This library is compatible with Scala 2.11.1 and uses as only dependency Apache Commons Lang.

## bibimbap

`bibimbap` is a tool to import BibTeX entries from various sources and manage
BibTex files. It runs in the console and is designed to be simple and fast. It
follows the philosophy that you should be able to start and stop using it
whenever you want without affecting your work.

The official website is [http://bibimbap.io](http://bibimbap.io). It includes
pre-compiled versions and documentation.

### Getting bibimbap

The simplest way of running bibimbap is to download and run the
[bootstrapper](http://bibimbap.io/downloads). It will ensure that you are
always running the latest version.

## library usage example

Parse and work with BibTeX entries within Scala, e.g. using as library
for processing BibTeX-Files.

    import io.bibimbap.bibtex._

    import scala.io.Source

    val bibStr = """
      @BOOK{dragonbook,
        AUTHOR = {Aho, Alfred V. and Lam, Monica S. and Sethi, Ravi and Ullman, Jeffrey D.},
        YEAR = {2007},
        TITLE = {Compilers. Principles, Techniques, and Tools},
        EDITION = {2. Aufl.},
        ISBN = {978-0-321-48681-3},
        PUBLISHER = {Pearson/Addison Wesley},
        ADDRESS = {Boston},
        totalpages={1009}
    }"""

    def errorHandler(s : String) : Unit = { println("error", s) }

    val parser = new BibTeXParser(Source.fromString(bibStr), errorHandler)

    val entries = parser.entries.toList

    entries(0).key.get
    entries(0).title.get.toJava  // or toASCII, toLaTeX
    entries(0).authors.map(_.toJava)
    entries(0).year.get.toJava

    entries(0).inlineString
