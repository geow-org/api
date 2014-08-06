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

import org.geow.model.test.OsmGenerators._

@RunWith(classOf[JUnitRunner])
class OsmWaySerializerTest extends Specification with ScalaCheck {

  sequential
  
  "The OsmSerializer" should {

    "serialize and deserialize an OsmWay object" ! check({ osmWay: OsmWay =>
      {
        val serialized = toBinary(osmWay)
        val deserialized = fromBinary(serialized)
        deserialized must be_==(osmWay)
      }
    })
  }
  

}