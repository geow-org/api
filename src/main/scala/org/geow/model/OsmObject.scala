package org.geow.model

import org.geow.model.geometry.Point

sealed trait OsmObject{

  def id: OsmId
  def user: Option[OsmUser]
  def version: OsmVersion

  def tags:List[OsmTag]

  def tagsToString = tags.mkString("[",",","]")

}
case class OsmNode(id: OsmId, user: Option[OsmUser] = None, version:OsmVersion = OsmVersion(), tags: List[OsmTag], point : Point) extends OsmObject{
  override def toString() = {
    StringBuilder.newBuilder.++=(id.toString).++=(",").++=(tagsToString).++=(",").++=(point.toString).toString()
  }
}
case class OsmWay(id: OsmId, user: Option[OsmUser] = None, version:OsmVersion = OsmVersion(), tags : List[OsmTag], nds : List[OsmId]) extends OsmObject{
  override def toString() = {
    StringBuilder.newBuilder.++=(id.toString).++=(",").++=(tagsToString).++=(",").++=(nds.mkString("[",",","]")).toString()
  }
}
case class OsmRelation(id: OsmId, user: Option[OsmUser] = None, version:OsmVersion = OsmVersion(), tags : List[OsmTag], refs : List[OsmMember]) extends OsmObject {
 override def toString() = {
   StringBuilder.newBuilder.++=(id.toString).++=(",").++=(tagsToString).++=(",").++=(refs.mkString("[",",","]")).toString()
 }
}