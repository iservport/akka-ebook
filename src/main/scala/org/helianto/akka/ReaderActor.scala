package org.helianto.akka

import akka.actor.{Actor, ActorLogging, Props}
import org.apache.pdfbox.pdmodel.PDDocument
import technology.tabula.ObjectExtractor
import technology.tabula.extractors.BasicExtractionAlgorithm

/**
  * Reader actor
  */
class ReaderActor extends Actor with ActorLogging {

  import collection.JavaConverters._

  val extractorActor = context.actorOf(Props[ExtractorActor], "extractor")

  override def receive = {
    case fileName: String =>
      try {
        val stream = getClass.getResourceAsStream(fileName)
        val document = PDDocument.load(stream)
        log.debug(s"Opening $document")
        try {
          val oe = new ObjectExtractor(document)
          val extractor = new BasicExtractionAlgorithm
          for {
            page <- oe.extract.asScala
            table <- extractor.extract(page).asScala
          } extractorActor ! table
        }
        finally {
          document.close()
          stream.close()
        }
      }
      catch {
        case e: Throwable =>
          log.error(s"Unable to read $fileName.", e)
      }
  }

}
