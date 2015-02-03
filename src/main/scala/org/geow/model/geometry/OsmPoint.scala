package org.geow.model.geometry

import org.geow.geohash.{PrecisionUltraHigh, GeoHash}

case class OsmPoint(lon: Double, lat : Double) {

  def toHash() = OsmPoint.hashCreator.encodeParallel(lon,lat)

}

object OsmPoint {

  lazy val hashCreator = GeoHash(PrecisionUltraHigh)

  def apply(hash:Long) = {
    val lonlat = hashCreator.decodeParallel(hash)
    new OsmPoint(lonlat._1,lonlat._2)
  }

  def fromHash(hash:Long) = apply(hash)
}