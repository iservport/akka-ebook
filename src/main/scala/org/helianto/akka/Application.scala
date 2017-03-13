package org.helianto.akka

import akka.actor.{ActorSystem, Props}

/**
  * Sample actor model
  */
object Application extends App {

  val system = ActorSystem("helloakka")

  val readerActor = system.actorOf(Props[ReaderActor], "reader")

  readerActor ! "/edital0012016_agricultura_familiar_chamadapublica.pdf"

  system.terminate()

}
