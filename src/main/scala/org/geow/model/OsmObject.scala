package org.geow.model

import org.geow.model.geometry.OsmPoint

sealed trait OsmObject{
  def properties: OsmProperties
  def tags:List[OsmTag]

  def tagsToString = tags.mkString("[",",","]")

}
case class OsmNode(properties: OsmProperties, tags: List[OsmTag], point : OsmPoint) extends OsmObject{
  override def toString() = {
    StringBuilder.newBuilder.++=(properties.toString).++=(",").++=(tagsToString).++=(",").++=(point.toString).toString()
  }
}
case class OsmWay(properties: OsmProperties, tags : List[OsmTag], nds : List[OsmId]) extends OsmObject{
  override def toString() = {
    StringBuilder.newBuilder.++=(properties.toString).++=(",").++=(tagsToString).++=(",").++=(nds.mkString("[",",","]")).toString()
  }
}
case class OsmRelation(properties: OsmProperties, tags : List[OsmTag], refs : List[OsmMember]) extends OsmObject {
 override def toString() = {
   StringBuilder.newBuilder.++=(properties.toString).++=(",").++=(tagsToString).++=(",").++=(refs.mkString("[",",","]")).toString()
 }
}