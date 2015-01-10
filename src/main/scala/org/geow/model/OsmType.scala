package org.geow.model

sealed trait OsmType
case object OsmTypeNode extends OsmType
case object OsmTypeWay extends OsmType
case object OsmTypeRelation extends OsmType