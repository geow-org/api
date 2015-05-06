package org.geow.parser

import java.io.FileInputStream
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
import org.geow.model.OsmObject
import scala.io.Codec
import scala.io.Source
import impl._

trait OsmParser extends Iterator[Option[OsmObject]]

object OsmParser {

  implicit val codec = Codec.UTF8

  def apply(fileName: String)(implicit codec: Codec): OsmParser = {
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

  def apply(source:Source): OsmParser = new OsmXmlParser(source)

}