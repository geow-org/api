package org.geow.model.geometry

import org.geow.geohash.{GeoHash, PrecisionUltraHigh_1MM}
import org.geow.model.{OsmRole, OsmId, OsmType}
import play.extras.geojson.{LatLng => JsonLatLng, Point => JsonPoint, LineString => JsonLineString, Geometry => JsonGeometry, GeometryCollection => JsonGeometryCollection}

sealed trait Geometry {

  private[model] def toGeoJson:JsonGeometry[JsonLatLng]
}

case class Linestring(linestring: List[Point]) extends Geometry {
  override def toString = linestring.mkString("[", ",", "]")


  private[model] def toGeoJson: JsonLineString[JsonLatLng] = {
    JsonLineString(for (point <- linestring) yield JsonLatLng(point.lat, point.lon))
  }
}

case class GeometryMember(typ : OsmType, ref : OsmId, role : OsmRole, geometry : Geometry)

case class GeometryCollection(members: List[GeometryMember]) extends Geometry {

  private[model] def toGeoJson:JsonGeometry[JsonLatLng] = {
    val features:List[JsonGeometry[JsonLatLng]] = for (member <- members) yield member.geometry.toGeoJson
    JsonGeometryCollection(features, None)
  }

}


case class Point(lon: Double, lat: Double) extends Geometry {

  def toHash = Point.hashCreator.encodeParallel(lon, lat)

  override def toString = StringBuilder.newBuilder.++=("[").++=("%.3f".format(lon)).++=(",").++=("%.3f".format(lat)).++=("]").toString()

  private[model] def toGeoJson: JsonPoint[JsonLatLng] = {
    JsonPoint(JsonLatLng(lat, lon))
  }
}

object Point {

  lazy val hashCreator = GeoHash(PrecisionUltraHigh_1MM)

  def apply(hash: Long) = {
    val (lon, lat) = hashCreator.decodeParallel(hash)
    new Point(lon, lat)
  }

}