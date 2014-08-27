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
import org.geow.model.test.OsmObjectGenerator

@RunWith(classOf[JUnitRunner])
class OsmSerializerTest extends Specification with ScalaCheck{

  sequential
  
  val generator = OsmObjectGenerator()
  
  def nodeGenerator = Gen.resultOf[Int,OsmNode](t => generator.generateNode)
  implicit def osmNodesArb = Arbitrary { nodeGenerator }
  
  def wayGenerator = Gen.resultOf[Int,OsmWay](t => generator.generateWay)
  implicit def osmWaysArb = Arbitrary { wayGenerator }
  
  def relationGenerator = Gen.resultOf[Int,OsmRelation](t => generator.generateRelation)
  implicit def osmRelationsArb = Arbitrary { relationGenerator }
  
  "The OsmSerializer" should {

    "serialize and deserialize an OsmNode object" ! check({ osmNode: OsmNode =>
      {
        val serialized = toBinary(osmNode)
        val deserialized = fromBinary(serialized)
        deserialized must be_==(osmNode)
      }
    })
    
    "serialize and deserialize an OsmWay object" ! check({ osmWay: OsmWay =>
      {
        val serialized = toBinary(osmWay)
        val deserialized = fromBinary(serialized)
        deserialized must be_==(osmWay)
      }
    })
    
    "serialize and deserialize an OsmRelation object" ! check({ osmRelation: OsmRelation =>
      {
        val serialized = toBinary(osmRelation)
        val deserialized = fromBinary(serialized)
        deserialized must be_==(osmRelation)
      }
    })
  }
  

}