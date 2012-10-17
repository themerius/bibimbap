package io.bibimbap

import akka.actor._
import com.typesafe.config.ConfigFactory

object Main {
  def main(args : Array[String]) : Unit = run(args, None)

  def boot(args : Array[String], cl : ClassLoader) : Unit = run(args, Some(cl))

  private def run(args : Array[String], cl : Option[ClassLoader]) : Unit = {
    val homeDir = System.getProperty("user.home") + System.getProperty("file.separator")

    val configFileName = homeDir + ".bibimbapconfig"
    val historyFileName = homeDir + ".bibimbaphistory"

    val system  = cl.map { c =>
      ActorSystem("bibimbap", ConfigFactory.load(c), c)
    } getOrElse {
      ActorSystem("bibimbap")
    }

    val settings = (new ConfigFileParser(configFileName)).parse.getOrElse(DefaultSettings)

    val repl = system.actorOf(Props(new Repl(homeDir, configFileName, historyFileName)), name = "repl")

    repl ! Start
  }
}
