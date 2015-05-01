package org.geow.geohash.test

import org.geow.geohash._
import org.geow.generator.OsmObjectGenerator
import org.specs2.ScalaCheck
import org.specs2.execute.Result
import org.specs2.mutable.Specification

/**
 * Created by janschulte on 12/02/15.
 */
class GeoHashSpec extends Specification with ScalaCheck {

  sequential

  val generator = OsmObjectGenerator()


  private val hashUltraLow = new GeoHash(PrecisionUltraLow_630KM)
  private val hashVeryLow = new GeoHash(PrecisionVeryLow_80KM)
  private val hashLow = new GeoHash(PrecisionLow_20KM)
  private val hashMedium = new GeoHash(PrecisionMedium_5KM)
  private val hashHigh = new GeoHash(PrecisionHigh_100M)
  private val hashVeryHigh = new GeoHash(PrecisionVeryHigh_1M)
  private val hashUltra = new GeoHash(PrecisionUltra_1CM)
  private val hashUltraHigh = new GeoHash(PrecisionUltraHigh_1MM)

  val testCases = 100000

  "The GeoHash" should {

    s"encode/decode $testCases points at ultra low precision" in {

      Result.unit {
        (1 to testCases) foreach {
          i =>

            val expectedPoint = generator.generatePoint

            val hash = hashUltraLow.encodeParallel(expectedPoint.lon, expectedPoint.lat)
            val (lon, lat) = hashUltraLow.decodeParallel(hash)

            lon must beCloseTo(expectedPoint.lon, 180 / Math.pow(2, 5))
            lat must beCloseTo(expectedPoint.lat, 90 / Math.pow(2, 5))
        }
      }
    }
    s"encode/decode $testCases points at very low precision" in {

      Result.unit {
        (1 to testCases) foreach {
          i =>

            val expectedPoint = generator.generatePoint

            val hash = hashVeryLow.encodeParallel(expectedPoint.lon, expectedPoint.lat)
            val (lon, lat) = hashVeryLow.decodeParallel(hash)

            lon must beCloseTo(expectedPoint.lon, 180 / Math.pow(2, 8))
            lat must beCloseTo(expectedPoint.lat, 90 / Math.pow(2, 8))
        }
      }
    }
    s"encode/decode $testCases points at low precision" in {

      Result.unit {
        (1 to testCases) foreach {
          i =>

            val expectedPoint = generator.generatePoint

            val hash = hashLow.encodeParallel(expectedPoint.lon, expectedPoint.lat)
            val (lon, lat) = hashLow.decodeParallel(hash)

            lon must beCloseTo(expectedPoint.lon, 180 / Math.pow(2, 10))
            lat must beCloseTo(expectedPoint.lat, 90 / Math.pow(2, 10))
        }
      }
    }
    s"encode/decode $testCases points at medium precision" in {

      Result.unit {
        (1 to testCases) foreach {
          i =>

            val expectedPoint = generator.generatePoint

            val hash = hashMedium.encodeParallel(expectedPoint.lon, expectedPoint.lat)
            val (lon, lat) = hashMedium.decodeParallel(hash)

            lon must beCloseTo(expectedPoint.lon, 180 / Math.pow(2, 13))
            lat must beCloseTo(expectedPoint.lat, 90 / Math.pow(2, 13))
        }
      }
    }
    s"encode/decode $testCases points at high precision" in {

      Result.unit {
        (1 to testCases) foreach {
          i =>

            val expectedPoint = generator.generatePoint

            val hash = hashHigh.encodeParallel(expectedPoint.lon, expectedPoint.lat)
            val (lon, lat) = hashHigh.decodeParallel(hash)

            lon must beCloseTo(expectedPoint.lon, 180 / Math.pow(2, 18))
            lat must beCloseTo(expectedPoint.lat, 90 / Math.pow(2, 18))
        }
      }
    }
    s"encode/decode $testCases points at very high precision" in {

      Result.unit {
        (1 to testCases) foreach {
          i =>

            val expectedPoint = generator.generatePoint

            val hash = hashVeryHigh.encodeParallel(expectedPoint.lon, expectedPoint.lat)
            val (lon, lat) = hashVeryHigh.decodeParallel(hash)

            lon must beCloseTo(expectedPoint.lon, 180 / Math.pow(2, 24))
            lat must beCloseTo(expectedPoint.lat, 90 / Math.pow(2, 24))
        }
      }
    }
    s"encode/decode $testCases points at ultra precision" in {

      Result.unit {
        (1 to testCases) foreach {
          i =>

            val expectedPoint = generator.generatePoint

            val hash = hashUltra.encodeParallel(expectedPoint.lon, expectedPoint.lat)
            val (lon, lat) = hashUltra.decodeParallel(hash)

            lon must beCloseTo(expectedPoint.lon, 180 / Math.pow(2, 30))
            lat must beCloseTo(expectedPoint.lat, 90 / Math.pow(2, 30))
        }
      }
    }
    s"encode/decode $testCases points at ultra high precision" in {

      Result.unit {
        (1 to testCases) foreach {
          i =>

            val expectedPoint = generator.generatePoint

            val hash = hashUltraHigh.encodeParallel(expectedPoint.lon, expectedPoint.lat)
            val (lon, lat) = hashUltraHigh.decodeParallel(hash)

            lon must beCloseTo(expectedPoint.lon, 180 / Math.pow(2, 32))
            lat must beCloseTo(expectedPoint.lat, 90 / Math.pow(2, 32))
        }
      }
    }
  }

}