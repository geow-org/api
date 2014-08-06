package org.geow.model

import org.geow.model.geometry._

sealed trait OsmDenormalizedObject
case class OsmDenormalizedNode(properties: OsmProperties, tags : List[OsmTag], geometryNode : OsmGeometryNode) extends OsmDenormalizedObject
case class OsmDenormalizedWay(properties: OsmProperties, tags : List[OsmTag], geometryWay : OsmGeometryWay) extends OsmDenormalizedObject
case class OsmDenormalizedRelation(properties: OsmProperties, tags : List[OsmTag], geometryMembers : List[OsmGeometryMember]) extends OsmDenormalizedObject
