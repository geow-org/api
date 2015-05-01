package org.geow.util

import org.geow.model._
import org.geow.model.geometry._

/**
 * Utility class to denormalize osm elements.
 * @author Jan Schulte <jan@plasmap.io>
 */
object Denormalizer {

  def denormalizeNode(node: OsmNode): OsmDenormalizedNode = {
    OsmDenormalizedNode(node.id,node.user,node.version, node.tags, node.point)
  }

  def denormalizeWay(way: OsmWay, mappings: Map[OsmId, Point]): OsmDenormalizedWay = {
    val wayPoints = for {
      nd: OsmId <- way.nds
      point <- mappings.get(nd)
    } yield point

    val geometry = Linestring(wayPoints)
    OsmDenormalizedWay(way.id,way.user,way.version, way.tags, geometry)
  }

  def denormalizeRelation(relation: OsmRelation, mappings: Map[(OsmId,OsmType), Geometry]) = {
    val members = for {
      member: OsmMember <- relation.refs
      tuple = member.ref -> member.typ
      geometry <- mappings.get(tuple)
    } yield GeometryMember(member.typ, member.ref, member.role, geometry)
    val geometry = GeometryCollection(members)
    OsmDenormalizedRelation(relation.id, relation.user, relation.version, relation.tags, geometry)
  }
}
