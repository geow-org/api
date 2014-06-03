package org.geow.model.osm.serializer


import scala.pickling._
import binary._
import org.geow.model.osm._


object OsmDenormalizedSerializer {
	
  def fromBinary(encoded: Array[Byte]): OsmDenormalizedObject = encoded.unpickle[OsmDenormalizedObject]
  
  def toBinary(decoded : OsmDenormalizedObject): Array[Byte] = decoded.pickle.value

}