package org.geow.model.geometry

import org.geow.model.{OsmRole,OsmType}

case class OsmGeometryMember(id : Long, role : OsmRole, `type` : OsmType, geometry : OsmGeometry)