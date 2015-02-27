package org.geow.parser.test

import org.specs2.mutable.Specification
import org.scalacheck.{ Arbitrary, Gen }
import org.scalacheck._
import org.scalacheck.Test._

import Gen._
import Arbitrary.arbitrary

import org.specs2.runner._
import org.junit.runner._

import scala.util.Try
import scala.concurrent.Await
import scala.concurrent.duration._

import org.specs2.ScalaCheck

import Prop.forAll

import org.scalacheck.Arbitrary.arbitrary

import scala.io.Source

import org.geow.parser.impl.OsmXmlParser
import org.geow.parser.impl.OsmXmlParser._
import org.geow.model._
import org.geow.model.geometry.OsmPoint

@RunWith(classOf[JUnitRunner])
class OsmObjectParserSpec extends Specification with ScalaCheck {

  val xml =
    <osm version="0.6" generator="CGImap 0.3.3 (4443 thorn-02.openstreetmap.org)" copyright="OpenStreetMap and contributors" attribution="http://www.openstreetmap.org/copyright" license="http://opendatacommons.org/licenses/odbl/1-0/">
      <node id="2465725143" visible="true" version="2" changeset="21736329" timestamp="2014-04-16T19:23:01Z" user="black_bike" uid="18130" lat="51.2378913" lon="6.7797524">
        <tag k="addr:city" v="Düsseldorf"/>
        <tag k="addr:country" v="DE"/>
        <tag k="addr:housenumber" v="53"/>
        <tag k="addr:postcode" v="40477"/>
        <tag k="addr:street" v="Nordstraße"/>
        <tag k="amenity" v="restaurant"/>
        <tag k="cuisine" v="regional"/>
        <tag k="name" v="Himmel und Ähd"/>
        <tag k="phone" v="+49 211 4981361"/>
        <tag k="website" v="http://www.himmel-aehd.de"/>
      </node>
      <way id="143653722" visible="true" version="5" changeset="19982704" timestamp="2014-01-14T00:57:49Z" user="teufli" uid="247886">
        <nd ref="203790573"/>
        <nd ref="717638289"/>
        <nd ref="73664701"/>
        <nd ref="827539061"/>
        <nd ref="717638282"/>
        <nd ref="827538924"/>
        <nd ref="73664703"/>
        <nd ref="717638284"/>
        <nd ref="717638278"/>
        <tag k="highway" v="trunk"/>
        <tag k="lanes" v="2"/>
        <tag k="layer" v="-1"/>
        <tag k="lit" v="yes"/>
        <tag k="lit_by_gaslight" v="no"/>
        <tag k="maxspeed" v="80"/>
        <tag k="maxspeed:conditional" v="60 @ (22:00-06:00)"/>
        <tag k="motorroad" v="yes"/>
        <tag k="name" v="Rheinalleetunnel"/>
        <tag k="oneway" v="yes"/>
      </way>
      <relation id="91062" visible="true" version="11" changeset="18781052" timestamp="2013-11-08T12:20:08Z" user="Gehrke" uid="14002">
        <member type="way" ref="245181859" role="outer"/>
        <member type="way" ref="245181864" role="outer"/>
        <member type="way" ref="32011174" role="outer"/>
        <member type="way" ref="32011181" role="outer"/>
        <member type="way" ref="32011176" role="outer"/>
        <member type="way" ref="31916345" role="outer"/>
        <member type="way" ref="32011190" role="outer"/>
        <member type="way" ref="32011189" role="outer"/>
        <member type="way" ref="32011184" role="outer"/>
        <tag k="admin_level" v="10"/>
        <tag k="boundary" v="administrative"/>
        <tag k="name" v="Golzheim"/>
        <tag k="type" v="boundary"/>
        <tag k="wikipedia" v="de:Golzheim_(DÃ¼sseldorf)"/>
      </relation>
    </osm>

  val nodeProps = OsmProperties(
    OsmId(2465725143L),
    Some(OsmUser("black_bike", 18130L)),
    OsmVersion(convertXmlDateToLong("2014-04-16T19:23:01Z"), 2, 21736329, true))

  val nodeTags = List(
    OsmTag("addr:city", "DÃ¼sseldorf"),
    OsmTag("addr:country", "DE"),
    OsmTag("addr:housenumber", "53"),
    OsmTag("addr:postcode", "40477"),
    OsmTag("addr:street", "NordstraÃŸe"),
    OsmTag("amenity", "restaurant"),
    OsmTag("cuisine", "regional"),
    OsmTag("name", "Himmel und Ã„hd"),
    OsmTag("phone", "+49 211 4981361"),
    OsmTag("website", "http://www.himmel-aehd.de"))

  val nodePoint = OsmPoint(6.7797524, 51.2378913)

  val node = OsmNode(nodeProps, nodeTags, nodePoint)

  val wayProps = OsmProperties(
    OsmId(143653722L),
    Some(OsmUser("teufli", 247886L)),
    OsmVersion(convertXmlDateToLong("2014-01-14T00:57:49Z"), 5, 19982704, true))

  val wayTags = List(
    OsmTag("highway", "trunk"),
    OsmTag("lanes", "2"),
    OsmTag("layer", "-1"),
    OsmTag("lit", "yes"),
    OsmTag("lit_by_gaslight", "no"),
    OsmTag("maxspeed", "80"),
    OsmTag("maxspeed:conditional", "60 @ (22:00-06:00)"),
    OsmTag("motorroad", "yes"),
    OsmTag("name", "Rheinalleetunnel"),
    OsmTag("oneway", "yes"))

  val wayNds = List(
    OsmId(203790573L),
    OsmId(717638289L),
    OsmId(73664701L),
    OsmId(827539061L),
    OsmId(717638282L),
    OsmId(827538924L),
    OsmId(73664703L),
    OsmId(717638284L),
    OsmId(717638278L))

  val way = OsmWay(wayProps, wayTags, wayNds)

  val relationProps = OsmProperties(
    OsmId(91062L),
    Some(OsmUser("Gehrke", 14002L)),
    OsmVersion(convertXmlDateToLong("2013-11-08T12:20:08Z"), 11, 18781052, true))

  val relationTags = List(
    OsmTag("admin_level", "10"),
    OsmTag("boundary", "administrative"),
    OsmTag("name", "Golzheim"),
    OsmTag("type", "boundary"),
    OsmTag("wikipedia", "de:Golzheim_(DÃ¼sseldorf)"))

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

  val relation = OsmRelation(relationProps, relationTags, relationMembers)

  val parser = new OsmXmlParser(Source.fromString(xml.toString))

  "The OsmObjectParser" should {

    "parse an Osm xml" in {
      parser.hasNext must be_==(true)
      parser.next must beSome(node)
      parser.hasNext must be_==(true)
      parser.next must beSome(way)
      parser.hasNext must be_==(true)
      parser.next must beSome(relation)
    }
  }

}