package org.geow.model

import org.geow.model.geometry._
import play.api.libs.json.{Json, JsValue, JsString, JsObject}
import play.extras.geojson.{Feature => JsonFeature, Geometry => JsonGeometry, GeometryCollection => JsonGeometryCollection, LatLng => JsonLatLng, Point => JsonPoint, LineString => JsonLineString}

sealed trait OsmDenormalizedObject{

  def id: OsmId
  def user: Option[OsmUser]
  def version: OsmVersion

  def tags:List[OsmTag]

  type T  <: Geometry

  def geometry:T

  def toGeoJson(withTags:Boolean):String


  protected def props(withTags: Boolean): Option[JsObject] = {
    val properties = if (withTags) {
      Some(JsObject(for (tag <- tags) yield tag.key -> JsString(tag.value)))
    } else {
      None
    }
    properties
  }


}

case class OsmDenormalizedNode(id: OsmId, user: Option[OsmUser] = None, version:OsmVersion = OsmVersion(), tags : List[OsmTag], geometry : Point) extends OsmDenormalizedObject{
  type T = Point

  override def toString = toGeoJson()

  def toGeoJson(withTags : Boolean = true):String = {
    val point = geometry.toGeoJson
    val properties: Option[JsObject] = props(withTags)

    val feature = JsonFeature(point,
      properties = properties)
    Json.toJson(feature).toString
  }

}
case class OsmDenormalizedWay(id: OsmId, user: Option[OsmUser] = None, version:OsmVersion = OsmVersion(), tags : List[OsmTag], geometry : Linestring) extends OsmDenormalizedObject{
  type T = Linestring

  def toGeoJson(withTags : Boolean = true):String = {
    val line = geometry.toGeoJson
    val properties: Option[JsObject] = props(withTags)
    val feature = JsonFeature(line,
      properties = properties)
    Json.toJson(feature).toString
  }

}
case class OsmDenormalizedRelation(id: OsmId, user: Option[OsmUser] = None, version:OsmVersion = OsmVersion(), tags : List[OsmTag], geometry : GeometryCollection) extends OsmDenormalizedObject{
  type T = GeometryCollection

  def toGeoJson(withTags : Boolean = true):String = {
    val geometryCollection = geometry.toGeoJson
    val feature = JsonFeature(geometryCollection,
      properties = props(withTags))
    Json.toJson(feature).toString
  }


}