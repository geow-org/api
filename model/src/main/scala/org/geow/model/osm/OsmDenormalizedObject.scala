package org.geow.model.osm

import org.geow.model.osm.geometry.{GeometryNode, GeometryWay, GeometryRelation}

sealed trait OsmDenormalizedObject
case class OsmDenormalizedNode(properties: OsmProperties, tags : List[OsmTag], geometryNode : GeometryNode) extends OsmDenormalizedObject
case class OsmDenormalizedWay(properties: OsmProperties, tags : List[OsmTag], geometryWay : GeometryWay) extends OsmDenormalizedObject
case class OsmDenormalizedRelation(properties: OsmProperties, tags : List[OsmTag], geometryRelation : GeometryRelation) extends OsmDenormalizedObject
