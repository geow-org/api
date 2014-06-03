package org.geow.model.osm

sealed trait OsmType
object OsmTypeNode extends OsmType
object OsmTypeWay extends OsmType
object OsmTypeRelation extends OsmType