package org.geow.model.geometry

import org.geow.geohash.{GeoHash, PrecisionUltraHigh_1MM}
import org.geow.model.{OsmId, OsmRole, OsmType}
import play.api.libs.json.Json
import play.extras.geojson.{Geometry => JsonGeometry, GeometryCollection => JsonGeometryCollection, LatLng => JsonLatLng, LineString => JsonLineString, Point => JsonPoint}

sealed trait Geometry {

  def toGeoJson(): String

  private[model] def toGeoJsonImpl:JsonGeometry[JsonLatLng]
}

case class Point(hash:Long) extends Geometry {

  import Point._

  def lon = hashCreator.decodeParallel(hash)._1

  def lat = hashCreator.decodeParallel(hash)._2

  override def toString = toGeoJson()

  override def toGeoJson():String = {
    Json.toJson(toGeoJsonImpl).toString
  }

  override def toGeoJsonImpl: JsonPoint[JsonLatLng] = {
    val (lon,lat) = hashCreator.decodeParallel(hash)
    JsonPoint(JsonLatLng(lat, lon))
  }
}

object Point {

  lazy val hashCreator = GeoHash(PrecisionUltraHigh_1MM)

  def apply(lon: Double, lat: Double) = new Point(hashCreator.encodeParallel(lon,lat))

}

case class Linestring(points: List[Point]) extends Geometry {

  override def toString = toGeoJson()

  override def toGeoJson():String = {
    Json.toJson(toGeoJsonImpl).toString
  }

  override def toGeoJsonImpl: JsonLineString[JsonLatLng] = {
    JsonLineString(for (point <- points) yield JsonLatLng(point.lat, point.lon))
  }
}

case class GeometryMember(typ: OsmType, ref: OsmId, role: OsmRole, geometry: Geometry)

case class GeometryCollection(members: List[GeometryMember]) extends Geometry {

  override def toString = toGeoJson()

  override def toGeoJson(): String= {
    Json.toJson(toGeoJsonImpl).toString
  }

  override def toGeoJsonImpl: JsonGeometry[JsonLatLng] = {
    val features: List[JsonGeometry[JsonLatLng]] = for (member <- members) yield member.geometry.toGeoJsonImpl
    JsonGeometryCollection(features, None)
  }
}
