package org.geow.util.test

import org.geow.model._
import org.geow.model.geometry.{GeometryCollection, Linestring, GeometryMember, Point}
import org.geow.parser.impl.OsmXmlParser._
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

class GeoJSONSpec extends Specification with ScalaCheck {

  sequential
  val id = OsmId(123456789L)
  val user = Some(OsmUser("John_Miller", 123456L))
  val version = OsmVersion(convertXmlDateToLong("2014-04-16T19:23:01Z"), 1, 12345, true)

  val tags = List(
    OsmTag("addr:city", "Düsseldorf"),
    OsmTag("addr:country", "DE"),
    OsmTag("addr:housenumber", "1"),
    OsmTag("addr:postcode", "40200"),
    OsmTag("addr:street", "Heinrich-Heine Allee"))

  val nodePoint = Point(6.7797524, 51.2378913)

  val node = OsmDenormalizedNode(id, user, version, tags, nodePoint)

  val nodeGeoJSON =
    """{"type":"feature","geometry":{"type":"point","coordinates":[6.779752378351986,51.237891318742186]},"properties":{"addr:city":"düsseldorf","addr:country":"de","addr:housenumber":"1","addr:postcode":"40200","addr:street":"heinrich-heineallee"}}""".stripMargin


  val lineString = List(
    Point(6.7797524, 51.2378913),
    Point(6.7797525, 51.2378914),
    Point(6.7797526, 51.2378915),
    Point(6.7797527, 51.2378916),
    Point(6.7797528, 51.2378917),
    Point(6.7797529, 51.2378918)
  )

  val way = OsmDenormalizedWay(id, user, version, tags, Linestring(lineString))

  val wayGeoJSON =
    """|{"type":"feature","geometry":{"type":"linestring","coordinates":[[6.779752378351986,51.237891318742186],[6.779752462171018,51.23789140256122],[6.7797526298090816,51.23789148638025],[6.779752713628113,51.2378916121088],[6.779752797447145,51.23789169592783],[6.779752881266177,51.23789177974686]]},"properties":{"addr:city":"düsseldorf","addr:country":"de","addr:housenumber":"1","addr:postcode":"40200","addr:street":"heinrich-heineallee"}}""".stripMargin

  val relationId = OsmId(91062L)
  val relationUser = Some(OsmUser("Gehrke", 14002L))
  val relationVersion = OsmVersion(convertXmlDateToLong("2013-11-08T12:20:08Z"), 11, 18781052, true)

  val relationTags = List(
    OsmTag("admin_level", "10"),
    OsmTag("boundary", "administrative"),
    OsmTag("name", "Golzheim"),
    OsmTag("type", "boundary"))

  val relationMembers = List(
    OsmMember(OsmTypeWay, OsmId(245181859L), OsmRoleOuter),
    OsmMember(OsmTypeWay, OsmId(245181864L), OsmRoleOuter),
    OsmMember(OsmTypeWay, OsmId(32011174L), OsmRoleOuter),
    OsmMember(OsmTypeWay, OsmId(32011181L), OsmRoleOuter),
    OsmMember(OsmTypeWay, OsmId(32011176L), OsmRoleOuter),
    OsmMember(OsmTypeWay, OsmId(31916345L), OsmRoleOuter),
    OsmMember(OsmTypeWay, OsmId(32011190L), OsmRoleOuter),
    OsmMember(OsmTypeWay, OsmId(32011189L), OsmRoleOuter),
    OsmMember(OsmTypeWay, OsmId(32011184L), OsmRoleOuter))


  val member1 = GeometryMember(OsmTypeNode, OsmId(3), OsmRoleInner, Point(102.0, 0.5))
  val member2 = GeometryMember(OsmTypeWay, OsmId(2), OsmRoleOuter, Linestring(List(
    Point(102.0, 0.0),
    Point(103.0, 1.0),
    Point(104.0, 0.0),
    Point(105.0, 1.0)
  )))
  val member3 = GeometryMember(OsmTypeWay, OsmId(3), OsmRoleOuter, Linestring(List(
    Point(100.0, 0.0),
    Point(101.0, 0.0),
    Point(101.0, 1.0),
    Point(100.0, 1.0),
    Point(100.0, 0.0)
  )))

  val relation = OsmDenormalizedRelation(relationId, relationUser, relationVersion, relationTags, GeometryCollection(
    List(member1, member2, member3)
  ))


  val relationGeoJSON = """{"type":"feature","geometry":{"type":"geometrycollection","geometries":[{"type":"point","coordinates":[101.99999999720603,0.49999999115243554]},{"type":"linestring","coordinates":[[101.99999999720603,-2.0954757928848267e-8],[103.00000002142042,1.000000003259629],[103.99999996181577,-2.0954757928848267e-8],[104.99999998603016,1.000000003259629]]},{"type":"linestring","coordinates":[[100.00000003259629,-2.0954757928848267e-8],[100.99999997299165,-2.0954757928848267e-8],[100.99999997299165,1.000000003259629],[100.00000003259629,1.000000003259629],[100.00000003259629,-2.0954757928848267e-8]]}]},"properties":{"admin_level":"10","boundary":"administrative","name":"golzheim","type":"boundary"}}""".stripMargin

  "The GeoJSON" should {

    "should transform an OsmDenormalizedNode into GeoJSON" in {
      val actualGeoJSON = node.toGeoJson()
      actualGeoJSON must be_==(nodeGeoJSON).ignoreCase.ignoreSpace
    }

    "should transform an OsmDenormalizedWay into GeoJSON" in {
      val actualGeoJSON = way.toGeoJson()
      actualGeoJSON must be_==(wayGeoJSON).ignoreCase.ignoreSpace
    }

    "should transform an OsmDenormalizedRelation into GeoJSON" in {
      val actualGeoJSON = relation.toGeoJson()
      actualGeoJSON must be_==(relationGeoJSON).ignoreCase.ignoreSpace
    }
  }

}