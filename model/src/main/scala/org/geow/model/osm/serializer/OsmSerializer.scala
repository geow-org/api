package org.geow.model.osm.serializer


import scala.pickling._
import binary._
import org.geow.model.osm._


object OsmSerializer {
	
  def fromBinary(encoded: Array[Byte]): OsmObject = encoded.unpickle[OsmObject]
  
  def toBinary(decoded : OsmObject): Array[Byte] = decoded.pickle.value

}