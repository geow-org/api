package org.geow.geohash

import org.geow.geohash.impl.GeoHashImpl.PRECISION
import org.geow.geohash.impl.GeoHashImpl

sealed abstract class Precision(val precision: PRECISION)
object PrecisionUltraLow extends Precision(PRECISION.ULTRA_LOW_630KM)
object PrecisionVeryLow extends Precision(PRECISION.VERY_LOW_80KM)
object PrecisionLow extends Precision(PRECISION.LOW_20KM)
object PrecisionMedium extends Precision(PRECISION.MEDIUM_5KM)
object PrecisionHigh extends Precision(PRECISION.HIGH_100M)
object PrecisionVeryHigh extends Precision(PRECISION.VERY_HIGH_1M)
object PrecisionUltra extends Precision(PRECISION.ULTRA_1CM)
object PrecisionUltraHigh extends Precision(PRECISION.ULTRA_HIGH_1MM)

class GeoHash(val precision: Precision) {

  val geohash = new GeoHashImpl(precision.precision)

  def encodeParallel(lon: Double, lat: Double): Long = geohash.encodeParallel(lon, lat)

  def decodeParallel(hash: Long): Array[Double] = geohash.decodeParallel(hash)

  def decodeSequential(hash: Long): Array[Double] = geohash.decodeSequential(hash)

  def encodeSequential(lon: Double, lat: Double): Long = geohash.encodeSequential(lon, lat)

  def reduceParallelPrecision(hash: Long, reducedPrecision: Precision): Long = geohash.reducePrecisionParallel(hash, reducedPrecision.precision)

  def neighbourHashes(hash: Long): Tuple3[Tuple3[Long, Long, Long], Tuple3[Long, Long, Long], Tuple3[Long, Long, Long]] = {
    val N = geohash.getNeighbourHashes(hash)
    ((N(0)(0), N(0)(1), N(0)(2)),
      (N(1)(0), N(1)(1), N(1)(2)),
      (N(2)(0), N(2)(1), N(2)(2)))
  }

  def encapsulatingRectangle(hashes: List[Long]): Tuple2[Long, Long] = {
    import scala.collection.JavaConverters._
    val javaHashes: java.util.Collection[java.lang.Long] = hashes.map(hash => {
      val javaHash: java.lang.Long = hash
      javaHash
    }).asJavaCollection
    val rectangle = geohash.getEncapsulatingRectangle(new java.util.ArrayList(javaHashes))
    (rectangle(0), rectangle(1))
  }
}