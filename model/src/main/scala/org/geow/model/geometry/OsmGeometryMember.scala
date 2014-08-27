package org.geow.model.geometry

import org.geow.model.{OsmRole,OsmType}
import org.geow.model.OsmId

case class OsmGeometryMember(`type` : OsmType, ref : OsmId, role : OsmRole, geometry : OsmGeometry)