package org.geow.model.geometry

import org.geow.geohash.{PrecisionUltraHigh_1MM, GeoHash}

case class OsmPoint(lon: Double, lat : Double) {

  def toHash() = OsmPoint.hashCreator.encodeParallel(lon,lat)

  override def toString = StringBuilder.newBuilder.++=("[").++=("%.3f".format(lon)).++=(",").++=("%.3f".format(lat)).++=("]").toString()
}

object OsmPoint {

  lazy val hashCreator = GeoHash(PrecisionUltraHigh_1MM)

  def apply(hash:Long) = {
    val (lon, lat) = hashCreator.decodeParallel(hash)
    new OsmPoint(lon, lat)
  }

  def fromHash(hash:Long) = apply(hash)
}