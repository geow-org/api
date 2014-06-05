package org.geow.model

sealed trait OsmType
object OsmTypeNode extends OsmType
object OsmTypeWay extends OsmType
object OsmTypeRelation extends OsmType