package org.geow.model.serializer

import org.geow.model._
import org.geow.model.geometry._

object OsmDenormalizedSerializer {

  import scala.pickling._
  import binary._

  def fromBinary(encoded: Array[Byte]): OsmDenormalizedObject = encoded.unpickle[OsmDenormalizedObject]

  def toBinary(decoded: OsmDenormalizedObject): Array[Byte] = decoded.pickle.value

}