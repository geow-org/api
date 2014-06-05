package org.geow.model

case class OsmProperties(
	osmId : Long,
	user : String,
	uid : Long,
	timestamp : Long,
	visible : Boolean,
	version : Int,
	changeset : Int
)