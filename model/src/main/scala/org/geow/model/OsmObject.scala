package org.geow.model

import org.geow.model.geometry.OsmPoint

sealed trait OsmObject
case class OsmNode(properties: OsmProperties, tags: List[OsmTag], point : OsmPoint) extends OsmObject
case class OsmWay(properties: OsmProperties, tags : List[OsmTag], nds : List[Long]) extends OsmObject
case class OsmRelation(properties: OsmProperties, tags : List[OsmTag], refs : List[OsmMember]) extends OsmObject