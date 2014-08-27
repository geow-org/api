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
import org.geow.model.test.OsmObjectGenerator

@RunWith(classOf[JUnitRunner])
class OsmDenormalizedSerializerTest extends Specification with ScalaCheck {

  sequential
  
  val generator = OsmObjectGenerator()
  
  def denormalizedNodeGenerator = Gen.resultOf[Int,OsmDenormalizedNode](t => generator.generateDenormalizedNode)
  implicit def osmDenormalizedNodesArb = Arbitrary { denormalizedNodeGenerator }
  
  def denormalizedWayGenerator = Gen.resultOf[Int,OsmDenormalizedWay](t => generator.generateDenormalizedWay)
  implicit def osmDenormalizedWaysArb = Arbitrary { denormalizedWayGenerator }
  
  def denormalizedRelationGenerator = Gen.resultOf[Int,OsmDenormalizedRelation](t => generator.generateDenormalizedRelation)
  implicit def osmDenormalizedRelationsArb = Arbitrary { denormalizedRelationGenerator }
  
  
  "The OsmDenormalizedSerializer" should {

    "serialize and deserialize an OsmDenormalizedNode object" ! check({ osmDenormalizedNode: OsmDenormalizedNode =>
      {
        val serialized = toBinary(osmDenormalizedNode)
        val deserialized = fromBinary(serialized)
        deserialized must be_==(osmDenormalizedNode)
      }
    })
    
    "serialize and deserialize an OsmDenormalizedRelation object" ! check({ osmDenormalizedRelation: OsmDenormalizedRelation =>
      {
        val serialized = toBinary(osmDenormalizedRelation)
        val deserialized = fromBinary(serialized)
        deserialized must be_==(osmDenormalizedRelation)        
      }
    })
    
    "serialize and deserialize an OsmDenormalizedWay object" ! check({ osmDenormalizedWay:OsmDenormalizedWay =>
      {
        val serialized = toBinary(osmDenormalizedWay)
        val deserialized = fromBinary(serialized)
        deserialized must be_==(osmDenormalizedWay)
      }
    })
  }

}