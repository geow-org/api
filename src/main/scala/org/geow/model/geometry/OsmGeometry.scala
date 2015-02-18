package org.geow.model.geometry

import org.geow.model.{OsmType,OsmRole}

sealed trait OsmGeometry
case class OsmGeometryNode(point : OsmPoint) extends OsmGeometry {
  override def toString = point.toString
}
case class OsmGeometryWay(linestring : List[OsmPoint]) extends OsmGeometry {
  override def toString = linestring.mkString("[",",","]")
}
case class OsmGeometryRelation(members : List[OsmGeometryMember]) extends OsmGeometry

