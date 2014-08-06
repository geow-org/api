package org.geow.model.geometry

import org.geow.model.{OsmRole,OsmType}

case class OsmGeometryMember(`type` : OsmType, id : Long, role : OsmRole, geometry : OsmGeometry)