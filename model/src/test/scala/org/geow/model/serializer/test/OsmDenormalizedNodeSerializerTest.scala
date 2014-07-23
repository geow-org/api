package org.geow.model.serializer.test

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
import org.geow.model._
import org.geow.model.geometry._
import org.geow.model.serializer.OsmDenormalizedSerializer._

@RunWith(classOf[JUnitRunner])
class OsmDenormalizedNodeSerializerTest extends Specification with ScalaCheck {

  sequential
  
  val tags = for {
    key <- Gen.alphaStr
    value <- Gen.alphaStr
  } yield OsmTag(key, value)

  implicit def tagsArb = Arbitrary { tags }

  val properties = for {
    osmId <- arbitrary[Long]
    user <- arbitrary[String]
    uid <- arbitrary[Long]
    timestamp <- arbitrary[Long]
    visible <- arbitrary[Boolean]
    version <- arbitrary[Int]
    changeset <- arbitrary[Int]
  } yield OsmProperties(osmId, user, uid, timestamp, visible, version, changeset)

  implicit def propertiesArb = Arbitrary { properties }

  val points = for {
    hash <- arbitrary[Long]
  } yield OsmPoint(hash)

  implicit def pointsArb = Arbitrary { points }

  /*val lines = for {
    ps <- Gen.containerOf[List, OsmPoint](points)
  } yield OsmLinestring(ps)

  implicit def linesArb = Arbitrary { lines }*/

  val members = for {
    o <- arbitrary[Long]
    r <- Gen.oneOf(OsmRoleInner, OsmRoleOuter, OsmRoleEmpty)
    t <- Gen.oneOf(OsmTypeNode, OsmTypeWay, OsmTypeRelation)
  } yield OsmMember(o, r, t)

  implicit def membersArb = Arbitrary { members }

  val geometryNode = for {
    p <- arbitrary[OsmPoint](pointsArb)
  } yield GeometryNode(p)

  implicit def geoNodeArb = Arbitrary { geometryNode }

  val geometryWay = for {
    l <- Gen.containerOf[List, OsmPoint](points)
    //l <- arbitrary[OsmLinestring](linesArb)
  } yield GeometryWay(l)

  implicit def geoWayArb = Arbitrary { geometryWay }

  implicit def geoArb: Gen[Geometry] = Gen.oneOf(geometryNode, geometryWay) //, geometryRelation)

  val geometryMember = for {
    o <- arbitrary[Long]
    r <- Gen.oneOf(OsmRoleInner, OsmRoleOuter, OsmRoleEmpty)
    t <- Gen.oneOf(OsmTypeNode, OsmTypeWay, OsmTypeRelation)
    g <- geoArb
  } yield GeometryMember(o, r, t, g)

  implicit def geoMemberArb = Arbitrary { geometryMember }

  val geometryRelation = for {
    r <- Gen.containerOf[List, GeometryMember](geometryMember)
  } yield GeometryRelation(r)

  val osmDenormalizedNodes = for {
    pr <- arbitrary[OsmProperties](propertiesArb)
    t <- Gen.containerOf[List, OsmTag](tags)
    gn <- arbitrary[GeometryNode](geoNodeArb)
  } yield OsmDenormalizedNode(pr, t, gn)

  val pointList = (for {
    hash <- 10000000000000000L to 1000000000010000L
  } yield OsmPoint(hash)).toList
  
  val osmDenormalizedWays = for {
    pr <- arbitrary[OsmProperties](propertiesArb)
    t <- Gen.containerOf[List, OsmTag](tags)
  } yield OsmDenormalizedWay(pr, t, GeometryWay(pointList))

  val osmDenormalizedRelations = for {
    pr <- arbitrary[OsmProperties](propertiesArb)
    t <- Gen.containerOf[List, OsmTag](tags)
    gm <- Gen.containerOf[List, GeometryMember](geometryMember)
  } yield OsmDenormalizedRelation(pr, t, gm)

  implicit def osmDenormalizedNodesArb = Arbitrary { osmDenormalizedNodes }
  implicit def osmDenormalizedWaysArb = Arbitrary { osmDenormalizedWays }
  implicit def osmDenormalizedRelationsArb = Arbitrary { osmDenormalizedRelations }

  "The OsmDenormalizedSerializer" should {

    "serialize and deserialize an OsmDenormalizedNode object" ! check({ osmDenormalizedNode: OsmDenormalizedNode =>
      {
        val serialized = toBinary(osmDenormalizedNode)
        val deserialized = fromBinary(serialized)
        deserialized must be_==(osmDenormalizedNode)
      }
    })
  }

}