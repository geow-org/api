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
import org.geow.model.serializer.OsmSerializer._

@RunWith(classOf[JUnitRunner])
class OsmDenormalizedRelationSerializerTest extends Specification with ScalaCheck {

  sequential
  val tags = for {
    key <- arbitrary[String]
    value <- arbitrary[String]
  } yield OsmTag(key, value)

  def tagsArb = Arbitrary { tags }

  val properties = for {
    osmId <- arbitrary[Long]
    user <- arbitrary[String]
    uid <- arbitrary[Long]
    timestamp <- arbitrary[Long]
    visible <- arbitrary[Boolean]
    version <- arbitrary[Int]
    changeset <- arbitrary[Int]
  } yield OsmProperties(osmId, user, uid, timestamp, visible, version, changeset)

  def propertiesArb = Arbitrary { properties }

  val points = for {
    hash <- arbitrary[Long]
  } yield OsmPoint(hash)

  def pointsArb = Arbitrary { points }

   val members = for {
    o <- arbitrary[Long]
    r <- Gen.oneOf(OsmRoleInner, OsmRoleOuter, OsmRoleEmpty)
    t <- Gen.oneOf(OsmTypeNode, OsmTypeWay, OsmTypeRelation)
  } yield OsmMember(o, r, t)

  def membersArb = Arbitrary { members }
  
  val osmNodes = for {
    pr <- arbitrary[OsmProperties](propertiesArb)
    t <- Gen.containerOf[List,OsmTag](tags)
    po <- arbitrary[OsmPoint](pointsArb)
  } yield OsmNode(pr, t, po)

  val osmWays = for {    
    pr <- arbitrary[OsmProperties](propertiesArb)
    t <- Gen.containerOf[List,OsmTag](tags)
    nds <- Gen.containerOf[List,Long](arbitrary[Long])
  } yield OsmWay(pr, t, nds)
  
  val osmRelations = for {    
    pr <- arbitrary[OsmProperties](propertiesArb)
    t <- Gen.containerOf[List,OsmTag](tags)
    mems <- Gen.containerOf[List,OsmMember](members)
  } yield OsmRelation(pr, t, mems)
  
  implicit def osmNodesArb = Arbitrary { osmNodes }
  implicit def osmWaysArb = Arbitrary { osmWays }
  implicit def osmRelationsArb = Arbitrary { osmRelations }
  
  "The OsmSerializer" should {

    "serialize and deserialize an OsmRelation object" ! check({ osmRelation: OsmRelation =>
      {
        val serialized = toBinary(osmRelation)
        val deserialized = fromBinary(serialized)
        deserialized must be_==(osmRelation)
      }
    })
  }
  

}