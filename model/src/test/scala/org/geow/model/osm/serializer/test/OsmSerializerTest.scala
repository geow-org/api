package org.geow.model.osm.serializer.test

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
import org.geow.model.osm.OsmTag
import org.geow.model.osm.OsmProperties
import org.geow.model.osm.geometry.Point
import org.geow.model.osm.OsmNode
import org.geow.model.osm.serializer.OsmSerializer


@RunWith(classOf[JUnitRunner])
class SerializerTest extends Specification with ScalaCheck {

  val tags = for {
    key <- arbitrary[String]
    value <- arbitrary[String]
  } yield OsmTag(key, value)

  def tagArb = Arbitrary { tags }

  implicit def tagsGenerator = Gen.listOfN(10, arbitrary[OsmTag](tagArb))

  "The Serializer" should {

    "serialize and deserialize an Osm object" ! Prop.forAll(tagsGenerator)({ tagList: List[OsmTag] =>
      {
        val properties = OsmProperties(
          osmId = 0L,
          user = "test",
          uid = 1L,
          timestamp = 221312312L,
          visible = true,
          version = 5,
          changeset = 10)
        val point = Point(10000000L)
        val node = OsmNode(properties, tagList, point)
        val serialized = OsmSerializer.toBinary(node)
        val deserialized = OsmSerializer.fromBinary(serialized)
        deserialized must be_==(node)
      }
    })
  }

}