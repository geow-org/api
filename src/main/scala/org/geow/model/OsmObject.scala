package org.geow.model

import org.geow.model.geometry.OsmPoint
import org.geow.model._

sealed trait OsmObject{
  def properties: OsmProperties
  def tags:List[OsmTag]
}
case class OsmNode(properties: OsmProperties, tags: List[OsmTag], point : OsmPoint) extends OsmObject
case class OsmWay(properties: OsmProperties, tags : List[OsmTag], nds : List[OsmId]) extends OsmObject
case class OsmRelation(properties: OsmProperties, tags : List[OsmTag], refs : List[OsmMember]) extends OsmObject