package org.geow.model

sealed trait OsmRole
case object OsmRoleInner extends OsmRole
case object OsmRoleOuter extends OsmRole
case object OsmRoleEmpty extends OsmRole
case class OsmRoleOther(value:String) extends OsmRole