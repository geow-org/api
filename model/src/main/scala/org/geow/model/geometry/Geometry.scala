package org.geow.model.geometry

import org.geow.model.{OsmType,OsmRole}

sealed trait Geometry
case class GeometryNode(point : OsmPoint) extends Geometry
case class GeometryWay(linestring : List[OsmPoint]) extends Geometry
case class GeometryRelation(members : List[GeometryMember]) extends Geometry
case class GeometryMember(id : Long, role : OsmRole, `type` : OsmType, geometry : Geometry)
