package org.geow.model

import org.geow.model.geometry._

sealed trait OsmDenormalizedObject{
  def properties: OsmProperties
  def tags:List[OsmTag]

  type T  <: OsmGeometry

  def geometry:T
}

case class OsmDenormalizedNode(properties: OsmProperties, tags : List[OsmTag], geometry : OsmGeometryNode) extends OsmDenormalizedObject{
  type T = OsmGeometryNode
}
case class OsmDenormalizedWay(properties: OsmProperties, tags : List[OsmTag], geometry : OsmGeometryWay) extends OsmDenormalizedObject{
  type T = OsmGeometryWay
}
case class OsmDenormalizedRelation(properties: OsmProperties, tags : List[OsmTag], geometry : OsmGeometryRelation) extends OsmDenormalizedObject{
  type T = OsmGeometryRelation
}