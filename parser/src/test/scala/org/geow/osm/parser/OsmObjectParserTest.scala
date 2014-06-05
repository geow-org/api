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
import scala.io.Source
import org.geow.osm.parser.OsmObjectParser

@RunWith(classOf[JUnitRunner])
class OsmObjectParserTest extends Specification with ScalaCheck {

  val src = Source.fromString("""<osm version="0.6" generator="CGImap 0.3.3 (4443 thorn-02.openstreetmap.org)" copyright="OpenStreetMap and contributors" attribution="http://www.openstreetmap.org/copyright" license="http://opendatacommons.org/licenses/odbl/1-0/">
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
<tag k="wikipedia" v="de:Golzheim_(Düsseldorf)"/>
</relation>
</osm>""")

  "The OsmObjectParser" should {

    "parse an OsmRelation" ! check {
      val parser = new OsmObjectParser(src)
      parser.hasNext must be_==(true)
      parser.next must be_==(None)
    }

  }

}