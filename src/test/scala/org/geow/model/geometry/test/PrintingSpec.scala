package org.geow.model.geometry.test

import org.geow.model.geometry.{OsmGeometryWay, OsmPoint}
import org.geow.model.{OsmDenormalizedRelation, OsmDenormalizedWay, OsmDenormalizedNode}
import org.geow.model.generator.OsmObjectGenerator
import org.scalacheck.{Arbitrary, Gen}
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

/**
 * Created by janschulte on 12/02/15.
 */
class PrintingSpec extends Specification with ScalaCheck {

  sequential

  val generator = OsmObjectGenerator()



  "The GeoHash" should {

    "pretty print an OsmPoint" in {
      val p = OsmPoint(34.4344583453, 43.3405834580345)
      val pretty = p.toString
      pretty must be_==("[34.434,43.341]")
    }
    "pretty print a list of OsmPoints" in {
      val p1 = OsmPoint(34.4344583453, 43.3405834580345)
      val p2 = OsmPoint(34.34083453, 43.330345)
      val p3 = OsmPoint(35.5458567567, 45.34534580345)
      val w = OsmGeometryWay(List(p1,p2,p3))
      val pretty = w.toString
      pretty must be_==("[[34.434,43.341],[34.341,43.330],[35.546,45.345]]")
    }
  }

}