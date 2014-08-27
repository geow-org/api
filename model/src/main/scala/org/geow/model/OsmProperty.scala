package org.geow.model

sealed trait OsmProperty
case class OsmId(osmId:Long) extends OsmProperty
case class OsmUser(username:String, uid:Long) extends OsmProperty
case class OsmVersion(timestamp:Long,versionNumber:Int, changeset:Int, visible:Boolean) extends OsmProperty