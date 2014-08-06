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
import org.geow.model.OsmDenormalizedRelation
import org.geow.model.geometry._
import org.geow.model.serializer.OsmDenormalizedSerializer._
import org.geow.model.test.OsmGenerators._
import org.geow.model.OsmDenormalizedRelation
@RunWith(classOf[JUnitRunner])
class OsmDenormalizedRelationSerializerTest extends Specification with ScalaCheck {

  sequential
  
  "The OsmDenormalizedSerializer" should {

    "serialize and deserialize an OsmDenormalizedRelation object" ! check({ osmDenormalizedRelation: OsmDenormalizedRelation =>
      {
        val serialized = toBinary(osmDenormalizedRelation)
        val deserialized : OsmDenormalizedRelation = fromBinary(serialized).asInstanceOf[OsmDenormalizedRelation]
        deserialized.properties must be_==(osmDenormalizedRelation.properties)
        deserialized.tags must be_==(osmDenormalizedRelation.tags)
        deserialized.geometryMembers must be_==(osmDenormalizedRelation.geometryMembers)
      }
    })
  }
  

}