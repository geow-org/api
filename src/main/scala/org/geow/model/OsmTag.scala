package org.geow.model

case class OsmTag(key: String, value: String) {
  def this(tuple:(String,String)) = this(tuple._1,tuple._2)
}

object OsmTag {

  implicit def tagToTuple(tag:OsmTag):(String,String) = (tag.key,tag.value)

  implicit def tupleToTag(tuple:(String,String)) = new OsmTag(tuple)
}