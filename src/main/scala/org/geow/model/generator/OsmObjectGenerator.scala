package org.geow.model.generator

import org.geow.model._
import scala.util.Random
import org.geow.model.geometry._
import org.geow.model._

case class OsmObjectGenerator() {

  val random = new Random()

  def generateOsmId = OsmId(random.nextLong)

  def generateProperties: OsmProperties = {
    val osmId = generateOsmId
    val user = OsmUser(random.nextString(10), random.nextLong)
    val version = OsmVersion(random.nextLong, random.nextInt, random.nextInt, random.nextBoolean)
    OsmProperties(osmId, Some(user), version)
  }

  def generateTag: OsmTag = {
    val key = random.nextString(10)
    val value = random.nextString(10)
    OsmTag(key, value)
  }

  def generateTags(n: Int = 10): List[OsmTag] = {
    Seq.fill(n)(generateTag).toList
  }

  def generateNds(n: Int = 100): List[OsmId] = {
    Seq.fill(n)(generateOsmId).toList
  }

  def generateMember: OsmMember = {
    val `type` = generateOsmType
    val ref = generateOsmId
    val role = generateOsmRole
    OsmMember(`type`, ref, role)
  }

  def generateMembers(n: Int = 100): List[OsmMember] = {
    Seq.fill(n)(generateMember).toList
  }

  def generatePoint: OsmPoint = {
    val lon = random.nextDouble * 360 - 180
    val lat = random.nextDouble * 180 - 90
    OsmPoint(lon, lat)
  }

  def generateLinestring(n: Int = 100): List[OsmPoint] = {
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

  def generateGeometryNode: OsmGeometryNode = {
    val point = generatePoint
    OsmGeometryNode(point)
  }

  def generateGeometryWay: OsmGeometryWay = {
    val linestring = generateLinestring()
    OsmGeometryWay(linestring)
  }

  def generateGeometryMember: OsmGeometryMember = {
    val `type` = generateOsmType
    val ref = generateOsmId
    val role = generateOsmRole
    val geoemtry = generateGeometryWay
    OsmGeometryMember(`type`, ref, role, geoemtry)
  }

  def generateGeometryRelation: OsmGeometryRelation = {
    val members = Seq.fill(30)(generateGeometryMember).toList
    OsmGeometryRelation(members)
  }

  def generateNode: OsmNode = {
    val properties = generateProperties
    val tags = generateTags()
    val point = generatePoint
    OsmNode(properties, tags, point)
  }

  def generateWay: OsmWay = {
    val properties = generateProperties
    val tags = generateTags()
    val nds = generateNds()
    OsmWay(properties, tags, nds)
  }

  def generateRelation: OsmRelation = {
    val properties = generateProperties
    val tags = generateTags()
    val members = generateMembers()
    OsmRelation(properties, tags, members)
  }

  def generateDenormalizedNode: OsmDenormalizedNode = {
    val properties = generateProperties
    val tags = generateTags()
    val geometryNode = generateGeometryNode
    OsmDenormalizedNode(properties, tags, geometryNode)
  }

  def generateDenormalizedWay: OsmDenormalizedWay = {
    val properties = generateProperties
    val tags = generateTags()
    val geometryWay = generateGeometryWay
    OsmDenormalizedWay(properties, tags, geometryWay)
  }

  def generateDenormalizedRelation: OsmDenormalizedRelation = {
    val properties = generateProperties
    val tags = generateTags()
    val geometryRelation = generateGeometryRelation
    OsmDenormalizedRelation(properties, tags, geometryRelation)
  }

}