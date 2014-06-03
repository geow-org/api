package org.geow.model.osm.geometry

import org.geow.model.osm.{OsmRole,OsmType}



sealed trait Geometry

case class GeometryNode(point : Point) extends Geometry
case class GeometryWay(linestring : Linestring) extends Geometry
case class GeometryRelation(members : List[GeometryMember]) extends Geometry
case class GeometryMember(id : Long, role : OsmRole, `type` : OsmType, geometry : Geometry)
