package org.geow.model.osm

import org.geow.model.osm.geometry.Point

sealed trait OsmObject
case class OsmNode(properties: OsmProperties, tags: List[OsmTag], point : Point) extends OsmObject
case class OsmWay(properties: OsmProperties, tags : List[OsmTag], nds : List[Long]) extends OsmObject
case class OsmRelation(properties: OsmProperties, tags : List[OsmTag], refs : List[OsmMember]) extends OsmObject