package org.geow.model.test

import org.geow.model._
import org.geow.model.geometry._
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalacheck.Gen._
import org.geow.model.geometry.OsmGeometryMember

object OsmGenerators {
  
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
    lon <- arbitrary[Double] 
    lonNormalized = lon % 180
    lat <- arbitrary[Double] 
    latNormalized = lat % 90
  } yield OsmPoint(lonNormalized, latNormalized)

  implicit def pointsArb = Arbitrary { points }

  val members = for {
    typ <- Gen.oneOf(OsmTypeNode, OsmTypeWay, OsmTypeRelation)
    id <- arbitrary[Long]
    role <- Gen.oneOf(OsmRoleInner, OsmRoleOuter, OsmRoleEmpty)
  } yield OsmMember(typ, id, role)

  implicit def membersArb = Arbitrary { members }

   val osmNodes = for {
    pr <- arbitrary[OsmProperties](propertiesArb)
    t <- Gen.listOfN(5, tags)
    po <- arbitrary[OsmPoint](pointsArb)
  } yield OsmNode(pr, t, po)

  implicit def osmNodesArb = Arbitrary { osmNodes }
  
  val osmWays = for {    
    pr <- arbitrary[OsmProperties](propertiesArb)
    t <- Gen.listOfN(5, tags)
    nds <- Gen.listOfN(50,arbitrary[Long])
  } yield OsmWay(pr, t, nds)
  
  implicit def osmWaysArb = Arbitrary { osmWays }
  
  val osmRelations = for {    
    pr <- arbitrary[OsmProperties](propertiesArb)
    t <- Gen.listOfN(5, tags)
    mems <- Gen.listOfN(30, members)
  } yield OsmRelation(pr, t, mems)
  
  implicit def osmRelationsArb = Arbitrary { osmRelations }
  
  val geometryNode = for {
    p <- arbitrary[OsmPoint](pointsArb)
  } yield OsmGeometryNode(p)

  implicit def geoNodeArb = Arbitrary { geometryNode }

  val geometryWay = for {
    l <- Gen.listOfN(50, points)
    w = OsmLinestring(l)
  } yield OsmGeometryWay(w)

  implicit def geoWayArb = Arbitrary { geometryWay }

  implicit def geoArb: Gen[OsmGeometry] = Gen.oneOf(geometryNode, geometryWay) 

  val geometryMember = for {
    t <- Gen.oneOf(OsmTypeNode, OsmTypeWay, OsmTypeRelation)
    i <- arbitrary[Long]
    r <- Gen.oneOf(OsmRoleInner, OsmRoleOuter, OsmRoleEmpty)
    g <- geoArb
  } yield OsmGeometryMember(t, i, r, g)

  implicit def geoMemberArb = Arbitrary { geometryMember }

  val geometryRelation = for {
    r <- Gen.listOfN(20,geometryMember)
  } yield OsmGeometryRelation(r)

  val osmDenormalizedNodes = for {
    pr <- arbitrary[OsmProperties](propertiesArb)
    t <- Gen.listOfN(5, tags)
    gn <- arbitrary[OsmGeometryNode](geoNodeArb)
  } yield OsmDenormalizedNode(pr, t, gn)

  implicit def osmDenormalizedNodesArb = Arbitrary { osmDenormalizedNodes }

  val osmDenormalizedWays = for {
    pr <- arbitrary[OsmProperties](propertiesArb)
    t <- Gen.listOfN(5, tags)
    pointList <- Gen.listOfN(50,points)
    linestring = OsmLinestring(pointList)
    gw = OsmGeometryWay(linestring)
  } yield OsmDenormalizedWay(pr, t, gw)

  implicit def osmDenormalizedWaysArb = Arbitrary { osmDenormalizedWays }
    
  val osmDenormalizedRelations = for {
    pr <- arbitrary[OsmProperties](propertiesArb)
    t <- Gen.listOfN(5, tags)
    gm <- Gen.listOfN(5,geometryMember)
  } yield OsmDenormalizedRelation(pr, t, gm)

  implicit def osmDenormalizedRelationsArb = Arbitrary { osmDenormalizedRelations }

}