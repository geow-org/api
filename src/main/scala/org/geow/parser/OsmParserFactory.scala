package org.geow.parser

import java.io.FileInputStream
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
import scala.io.Codec
import scala.io.Source
import impl._

object OsmParserFactory {

  implicit val codec = Codec.UTF8

  def createParser(fileName: String)(implicit codec: Codec): OsmParser = {
    fileName match {
      case bz2 if fileName.endsWith(".bz2") => {
        val source = Source.fromInputStream(new BZip2CompressorInputStream(new FileInputStream(fileName)))
        new OsmXmlParser(source)
      }
      case osm if fileName.endsWith(".osm") => {
        val source = Source.fromFile(fileName)(Codec.UTF8)
        new OsmXmlParser(source)
      }
      case pbf if fileName.endsWith(".pbf") => {
        throw new Error(".pbf files are not yet supported. Please try .osm or .osm.bz2")
      }
      case _ => {
        throw new Error("Unknown file type.")
      }
    }
  }

  def createParser(source:Source): OsmParser = new OsmXmlParser(source)

}