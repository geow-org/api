package org.geow.model

import org.geow.model.geometry._

sealed trait OsmDenormalizedObject {
  def properties: OsmProperties
  def tags:List[OsmTag]
}

case class OsmDenormalizedNode(properties: OsmProperties, tags : List[OsmTag], geometryNode : OsmGeometryNode) extends OsmDenormalizedObject
case class OsmDenormalizedWay(properties: OsmProperties, tags : List[OsmTag], geometryWay : OsmGeometryWay) extends OsmDenormalizedObject
case class OsmDenormalizedRelation(properties: OsmProperties, tags : List[OsmTag], geometryRelation : OsmGeometryRelation) extends OsmDenormalizedObject
