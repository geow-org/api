package org.geow.generator

import org.geow.model._
import org.geow.model.geometry._

import scala.util.Random

/**
 * Utility class to generate osm objects
 *
 * @author Jan Schulte <jan@plasmap.io>
 */
case class OsmObjectGenerator() {

  val random = new Random()

  def generateOsmId = OsmId(random.nextLong)

  def generateUser: Option[OsmUser] = {
    Some(OsmUser(random.nextString(10), random.nextLong))
  }

  def generateVersion: OsmVersion = {
    val num = random.nextInt(10)
    val cs = random.nextInt(100000)
    OsmVersion(versionNumber = num, changeset = cs, visible = true)
  }

  def generateTag: OsmTag = {
    val key = random.nextString(10)
    val value = random.nextString(10)
    OsmTag(key, value)
  }

  def generateTags(n: Int = 10): List[OsmTag] = {
    Seq.fill(n)(generateTag).toList
  }

  def generateNds(n: Int = 20): List[OsmId] = {
    Seq.fill(n)(generateOsmId).toList
  }

  def generateMember: OsmMember = {
    val `type` = generateOsmType
    val ref = generateOsmId
    val role = generateOsmRole
    OsmMember(`type`, ref, role)
  }

  def generateMembers(n: Int = 10): List[OsmMember] = {
    Seq.fill(n)(generateMember).toList
  }

  def generatePoint: Point = {
    val lon = random.nextDouble * 360 - 180
    val lat = random.nextDouble * 180 - 90
    Point(lon, lat)
  }

  def generatePointList(n: Int = 20): List[Point] = {
    Seq.fill(n)(generatePoint).toList
  }

  def generateOsmType: OsmType = {
    oneOf(OsmTypeNode, OsmTypeWay, OsmTypeRelation)
  }

  def oneOf[T](params: T*): T = {
    val list = Random.shuffle(params)
    list(0)
  }

  def generateOsmRole: OsmRole = {
    oneOf(OsmRoleEmpty, OsmRoleInner, OsmRoleOuter)
  }

  def generateLinestring: Linestring = {
    Linestring(generatePointList())
  }

  def generateGeometryMember: GeometryMember = {
    val typ = generateOsmType
    val ref = generateOsmId
    val role = generateOsmRole
    val geoemtry = generateLinestring
    GeometryMember(typ, ref, role, geoemtry)
  }

  def generateGeometryCollection: GeometryCollection = {
    val members = Seq.fill(10)(generateGeometryMember).toList
    GeometryCollection(members)
  }

  def generateNode: OsmNode = {
    val id = generateOsmId
    val user = generateUser
    val version = generateVersion
    val tags = generateTags()
    val point = generatePoint
    OsmNode(id, user, version, tags, point)
  }

  def generateWay: OsmWay = {
    val id = generateOsmId
    val user = generateUser
    val version = generateVersion
    val tags = generateTags()
    val nds = generateNds()
    OsmWay(id, user, version, tags, nds)
  }

  def generateRelation: OsmRelation = {
    val id = generateOsmId
    val user = generateUser
    val version = generateVersion
    val tags = generateTags()
    val members = generateMembers()
    OsmRelation(id, user, version, tags, members)
  }

  def generateDenormalizedNode: OsmDenormalizedNode = {
    val id = generateOsmId
    val user = generateUser
    val version = generateVersion
    val tags = generateTags()
    val point = generatePoint
    OsmDenormalizedNode(id, user, version, tags, point)
  }

  def generateDenormalizedWay: OsmDenormalizedWay = {
    val id = generateOsmId
    val user = generateUser
    val version = generateVersion
    val tags = generateTags()
    val geometryWay = generateLinestring
    OsmDenormalizedWay(id, user, version, tags, geometryWay)
  }

  def generateDenormalizedRelation: OsmDenormalizedRelation = {
    val id = generateOsmId
    val user = generateUser
    val version = generateVersion
    val tags = generateTags()
    val geometryRelation = generateGeometryCollection
    OsmDenormalizedRelation(id, user, version, tags, geometryRelation)
  }

}