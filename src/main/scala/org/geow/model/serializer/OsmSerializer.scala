package org.geow.model.serializer

import org.geow.model._
import org.geow.model.geometry._

object OsmSerializer {

  import scala.pickling._
  import binary._

  def fromBinary(encoded: Array[Byte]): OsmObject = encoded.unpickle[OsmObject]

  def toBinary(decoded: OsmObject): Array[Byte] = decoded.pickle.value

}