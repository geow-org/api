package org.geow.model

import org.geow.model.geometry._
import play.api.libs.json.{JsObject, JsString, Json}
import play.extras.geojson.{Feature => JsonFeature, FeatureCollection => JsonFeatureCollection, Geometry => JsonGeometry, GeometryCollection => JsonGeometryCollection, LatLng => JsonLatLng, LineString => JsonLineString, Point => JsonPoint}

sealed trait OsmDenormalizedObject{

  def id: OsmId
  def user: Option[OsmUser]
  def version: OsmVersion

  def tags:List[OsmTag]

  type T  <: Geometry

  def geometry:T

  def toGeoJson(withTags:Boolean):String

  private[model] def toGeoJsonImpl(withTags:Boolean):JsonFeature[JsonLatLng]

  protected def props(withTags: Boolean): Option[JsObject] = {
    val properties = if (withTags) {
      Some(JsObject(for (tag <- tags) yield tag.key -> JsString(tag.value)))
    } else {
      None
    }
    properties
  }


}

object OsmDenormalizedObject{

  def toGeoJson(osmObjects: List[OsmDenormalizedObject]):String = {

    val feature = JsonFeatureCollection(osmObjects.map(obj => obj.toGeoJsonImpl(true)))
    Json.toJson(feature).toString()

  }

}

case class OsmDenormalizedNode(id: OsmId, user: Option[OsmUser] = None, version:OsmVersion = OsmVersion(), tags : List[OsmTag], geometry : Point) extends OsmDenormalizedObject{

  type T = Point

  override def toString = toGeoJson()

  override def toGeoJson(withTags : Boolean = true):String = {
    Json.toJson(toGeoJsonImpl(withTags)).toString
  }

  override def toGeoJsonImpl(withTags:Boolean):JsonFeature[JsonLatLng] = {
    val point = geometry.toGeoJsonImpl
    val properties: Option[JsObject] = props(withTags)

    JsonFeature(point,
      properties = properties)
  }

}
case class OsmDenormalizedWay(id: OsmId, user: Option[OsmUser] = None, version:OsmVersion = OsmVersion(), tags : List[OsmTag], geometry : Linestring) extends OsmDenormalizedObject{

  type T = Linestring

  override def toString= toGeoJson()

  override def toGeoJson(withTags : Boolean = true):String = {
    Json.toJson(toGeoJsonImpl(withTags)).toString
  }

  override def toGeoJsonImpl(withTags:Boolean):JsonFeature[JsonLatLng] = {
    val line = geometry.toGeoJsonImpl
    val properties: Option[JsObject] = props(withTags)
    JsonFeature(line,
      properties = properties)
  }

}
case class OsmDenormalizedRelation(id: OsmId, user: Option[OsmUser] = None, version:OsmVersion = OsmVersion(), tags : List[OsmTag], geometry : GeometryCollection) extends OsmDenormalizedObject{

  type T = GeometryCollection

  override def toString = toGeoJson()

  override def toGeoJson(withTags : Boolean = true):String = {
    Json.toJson(toGeoJsonImpl(withTags)).toString
  }

  override def toGeoJsonImpl(withTags: Boolean): JsonFeature[JsonLatLng] = {
    val geometryCollection = geometry.toGeoJsonImpl
    JsonFeature(geometryCollection,
      properties = props(withTags))
  }
}