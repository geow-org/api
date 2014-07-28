/*******************************************************************************
 * Copyright 2013 Jan Schulte
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.geow.geohash;

import java.util.List;


/**
 * Utility class to encode/decode geohashes from/to (latitude,longitude) pairs.
 * 
 * @author JansonHanson
 * 
 */
public class GeoHashUtils {


	private static final int MAX_BITS = 64;
	private int precisionBits;
	
	/*
	 * Number of bits used for each precision. Note that the maximum is 32, since it applies twice: lon and lat
	 */
	private static final int ULTRA_LOW_PRECISION = 5;
	private static final int VERY_LOW_PRECISION = 8;
	private static final int LOW_PRECISION = 10;
	private static final int MEDIUM_PRECISION = 13;
	private static final int HIGH_PRECISION = 18;
	private static final int VERY_HIGH_PRECISION = 24;
	private static final int ULTRA_PRECISION = 30;
	
	private Long maxNumber = 0L;

	public static enum PRECISION {
		ULTRA_LOW_630KM,
		VERY_LOW_80KM,
		LOW_20KM,
		MEDIUM_5KM,
		HIGH_100M,
		VERY_HIGH_1M,
		ULTRA_1CM
	}
	
	/**
	 * Creates a KeyGenerator with default precision.
	 */
	public GeoHashUtils() {
		this(PRECISION.MEDIUM_5KM);
	}

	/**
	 * Creates a KeyGenerator with the specified precision.
	 * 
	 * @param precision
	 */
	public GeoHashUtils(PRECISION precision) {
		this.precisionBits = getNumberOfBits(precision);
			
		maxNumber = calculateMaxNumber(this.precisionBits);

	}

	public int getNumberOfBits(){
		return this.precisionBits;
	}
	
	/**
	 * Gets the number of bits for the desired precision. 
	 * @param precision
	 * @return
	 */
	public int getNumberOfBits(PRECISION precision) {
		int myPrecision = 0;
		switch(precision){
		case ULTRA_LOW_630KM:
			myPrecision = ULTRA_LOW_PRECISION;break;
		case VERY_LOW_80KM:
			myPrecision = VERY_LOW_PRECISION;break;
		case LOW_20KM: 
			myPrecision = LOW_PRECISION;break;
		case HIGH_100M:
			myPrecision = HIGH_PRECISION;break;
		case VERY_HIGH_1M:
			myPrecision = VERY_HIGH_PRECISION;break;
		case ULTRA_1CM:
			myPrecision = ULTRA_PRECISION;break;
		case MEDIUM_5KM:
		default:
			myPrecision = MEDIUM_PRECISION;break;
			
		}
		return myPrecision;
	}

	/**
	 * Calculates the maximum number of nodes that fit into a hash with the
	 * given precision.
	 */
	public long calculateMaxNumber(int workingPrecision) {
		int myPrecision = workingPrecision;
		long myMaxNumber = 0L;
		/*
		 * Calculate the maximum key that is possible
		 */
		for (int i = 0; i < MAX_BITS - (myPrecision * 2); i++) {
			myMaxNumber |= 1L;
			// if (i < 64 - precision * 2-1)

			/*
			 * Do not move the last bit since it would be filled up with a 0.
			 */
			if (i < MAX_BITS - myPrecision * 2 - 1){
				myMaxNumber <<= 1L;
			}

		}
		return myMaxNumber;
	}

	/**
	 * Returns the maximum number of nodes for a certain hash.
	 * 
	 * @return
	 */
	public Long getMaxNumber() {
		return maxNumber;
	}

	/**
	 * Encodes the given (longitude,latitude) into a sequential geohash.
	 * 
	 * @param longitude
	 *            the longitude
	 * @param latitude
	 *            the latitude
	 * @return The generated sequential geohash from the given
	 *         (longitude,latitude).
	 */
	public long encodeSequential(final double longitude, final double latitude) {

		long hash = 0;

		double[] longitudeInterval = { -180.0, 180.0 };
		double[] latitudeInterval = { -90.0, 90.0 };

		/*
		 * Calculate the longitude hash until the desired precision.
		 */
		for (int i = 0; i < precisionBits; i++) {
			double midLon = 0.0;

			midLon = (longitudeInterval[0] + longitudeInterval[1]) / 2D;
			if (longitude > midLon) {
				hash |= 1;
				longitudeInterval[0] = midLon;
			} else {
				longitudeInterval[1] = midLon;
			}

			hash <<= 1;

		}

		/*
		 * Calculate the latiude hash until the desired precision.
		 */
		for (int i = 0; i < precisionBits; i++) {
			double midLat = 0.0;
			midLat = (latitudeInterval[0] + latitudeInterval[1]) / 2D;
			if (latitude > midLat) {
				hash |= 1;
				latitudeInterval[0] = midLat;
			} else {
				latitudeInterval[1] = midLat;
			}

			if (i != precisionBits - 1) {
				hash <<= 1;
			}
		}

		/*
		 * Move the calculated hash to the left so there is space for the node
		 * id.
		 */
		hash <<= MAX_BITS - precisionBits * 2;

		return hash;

	}

	public long encodeParallel(double[] lonlat) {
		return encodeParallel(lonlat[0],lonlat[1]);		
	}
	
	/**
	 * Encodes the given (longitude,latitude) pair into a parallel geohash.
	 * 
	 * @param longitude
	 *            the longitude
	 * @param latitude
	 *            the latitude
	 * @return The generated parallel geohash from the given
	 *         (longitude,latitude).
	 */
	public long encodeParallel(final double longitude, final double latitude) {
		
		long hash = 0;

		double[] longitudeInterval = { -180.0, 180.0 };
		double[] latitudeInterval = { -90.0, 90.0 };

		/*
		 * Calculate the longitude hash until the desired precision.
		 */
		for (int i = 0; i < precisionBits; i++) {
			double midLon = 0.0;
			double midLat = 0.0;

			midLon = (longitudeInterval[0] + longitudeInterval[1]) / 2D;
			if (longitude > midLon) {
				hash |= 1;
				longitudeInterval[0] = midLon;
			} else {
				longitudeInterval[1] = midLon;
			}

			hash <<= 1;

			midLat = (latitudeInterval[0] + latitudeInterval[1]) / 2D;
			if (latitude > midLat) {
				hash |= 1;
				latitudeInterval[0] = midLat;
			} else {
				latitudeInterval[1] = midLat;
			}

			if (i != precisionBits - 1) {
				hash <<= 1;
			}
		}

		/*
		 * Move the calculated hash to the left so there is space for the node
		 * id.
		 */
		hash <<= MAX_BITS - precisionBits * 2;

		return hash;

	}

	/**
	 * Decodes the given sequential geohash into a (longitude,latitude) pair.
	 * 
	 * @param hash
	 *            The sequential hash to decode.
	 * @return Array where the first element is the longitude value and the
	 *         second element the latitude value.
	 */
	public double[] decodeSequential(long hash) {

		double longitude;
		double latitude;
		double[] longitudeInterval = { -180.0, 180.0 };
		double[] latitudeInterval = { -90.0, 90.0 };

		// hash >>>= 63 - precision * 2;
		//
		// for (int i = precision * 2 - 1; i >= precision; i--) {
		// hash >>>= 63;

		for (int i = MAX_BITS; i > MAX_BITS - precisionBits; i--) {
			long mask = 1;

			// mask <<= i;
			mask <<= i - 1;
			mask = mask & hash;
			// mask >>>= i;

			if (mask != 0) {
				longitudeInterval[0] = (longitudeInterval[0] + longitudeInterval[1]) / 2D;
			} else {
				longitudeInterval[1] = (longitudeInterval[0] + longitudeInterval[1]) / 2D;
			}
		}

		for (int i = MAX_BITS - precisionBits; i > MAX_BITS - precisionBits * 2; i--) {
			long mask = 1;
			mask <<= i - 1;
			mask = mask & hash;

			if ((mask) != 0) {
				latitudeInterval[0] = (latitudeInterval[0] + latitudeInterval[1]) / 2D;
			} else {
				latitudeInterval[1] = (latitudeInterval[0] + latitudeInterval[1]) / 2D;
			}
		}

		longitude = (longitudeInterval[0] + longitudeInterval[1]) / 2D;
		latitude = (latitudeInterval[0] + latitudeInterval[1]) / 2D;

		return new double[] { longitude, latitude };
	}

	/**
	 * Decodes the given into a [longitude,latitude] pair.
	 * 
	 * @param hash
	 *            The hash to decode.
	 * @return Array where the first element is the longitude value and the
	 *         second element the latitude value.
	 */
	public double[] decodeParallel(long hash) {

		double longitude;
		double latitude;
		double[] longitudeInterval = { -180.0, 180.0 };
		double[] latitudeInterval = { -90.0, 90.0 };

		for (int i = MAX_BITS; i > MAX_BITS - precisionBits * 2; i = i - 2) {

			long mask = 1;

			mask <<= i - 1;
			mask = mask & hash;

			if (mask != 0) {
				longitudeInterval[0] = (longitudeInterval[0] + longitudeInterval[1]) / 2D;
			} else {
				longitudeInterval[1] = (longitudeInterval[0] + longitudeInterval[1]) / 2D;
			}
			mask = 1;

			mask <<= i - 2;
			mask = mask & hash;

			if ((mask) != 0) {
				latitudeInterval[0] = (latitudeInterval[0] + latitudeInterval[1]) / 2D;
			} else {
				latitudeInterval[1] = (latitudeInterval[0] + latitudeInterval[1]) / 2D;
			}
		}

		longitude = (longitudeInterval[0] + longitudeInterval[1]) / 2D;
		latitude = (latitudeInterval[0] + latitudeInterval[1]) / 2D;

		return new double[] { longitude, latitude };
	}
	
	/**
	 * Reduces the precision of the given parallel hash. 
	 * 
	 * @param hash
	 *            The hash to reduce in precision.
	 * @param precision
	 * 			The target precision.
	 * @return The hash with the reduced precision.
	 */
	public long reducePrecisionParallel(long hash, PRECISION precision) {

		int reducedPrecisionBits = getNumberOfBits(precision);
		long reducedPrecisionHash = 0L;
		
		for (int i = MAX_BITS; i > MAX_BITS - reducedPrecisionBits * 2; i--) {

			long mask = 1;

			mask <<= i - 1;
			mask = mask & hash;
			reducedPrecisionHash |= mask;
		}
		return reducedPrecisionHash;
		
	}

	public double getLongitudeFailure(){
		double longitudeInterval = 180.0;
		double failure = longitudeInterval;
		for(int i=0;i<precisionBits;i++){
			failure = failure / 2D;
		}
		return failure;		
	}
	
	public double getLatitudeFailure(){
		double latitudeInterval = 90.0;
		double failure = latitudeInterval;
		for(int i=0;i<precisionBits;i++){
			failure = failure / 2D;
		}
		return failure;		
	}
	
		
	/**
	 * Splits the given parallel hash into a hash containing the longitude
	 * information and a second hash containing the latitude information.
	 * 
	 * @param hash
	 *            The hash to split.
	 * @return Array where element long[0] contains the longitude hash and
	 *         long[1] contains the latitude hash.
	 */
	public long[] splitHash(long hash) {

		long longitudeHash = 0;
		long latitudeHash = 0;

		for (int i = MAX_BITS; i > MAX_BITS - precisionBits * 2; i = i - 2) {

			long mask = 1;

			mask <<= i - 1;
			mask = mask & hash;

			if (mask != 0) {
				longitudeHash <<= 1;
				longitudeHash |= 1;
			} else {
				longitudeHash <<= 1;
			}
			mask = 1;

			mask <<= i - 2;
			mask = mask & hash;

			if ((mask) != 0) {
				latitudeHash <<= 1;
				latitudeHash |= 1;
			} else {
				latitudeHash <<= 1;
			}
		}

		return new long[] { longitudeHash, latitudeHash };
	}

	/**
	 * Joins the seperate latitude and longitude hash into a parallel hash.
	 * 
	 * @param longitudeHash
	 *            The longitude hash
	 * @param latitudeHash
	 *            The latitude hash
	 * @return The joined parrallel hash.
	 */
	public long joinHash(long longitudeHash, long latitudeHash) {

		long joinedHash = 0L;

		/*
		 * Calculate the longitude hash until the desired precision.
		 */
		for (int i = precisionBits; i >= 0; i--) {
			long mask = 1;
			mask <<= i;
			mask = mask & longitudeHash;

			if (mask != 0) {
				joinedHash <<= 1;
				joinedHash |= 1;
			} else {
				joinedHash <<= 1;
			}

			mask = 1;
			mask <<= i;
			mask = mask & latitudeHash;

			if (mask != 0) {
				joinedHash <<= 1;
				joinedHash |= 1;
			} else {
				joinedHash <<= 1;
			}
		}

		/*
		 * Move the calculated hash to the left so there is space for the node
		 * id.
		 */
		joinedHash <<= MAX_BITS - precisionBits * 2;

		return joinedHash;
	}

	/**
	 * Creates the geoId based on the hash and a current id.
	 * 
	 * @param hash
	 *            A sequential or parallel hash.
	 * @param id
	 *            The current node counter.
	 * @return The calculated node id.
	 * @throws IndexOutOfBoundsException
	 *             Throws an IndexOutOfBoundsException if the id is out of bounds (>maxNumber).
	 */
	public Long getGeoID(Long hash, Long id) throws IndexOutOfBoundsException {

		Long geoID = 0L;
		geoID |= hash;
		if (id > maxNumber) {

			throw new IndexOutOfBoundsException("Id " + id
					+ " out of bounds for precision " + precisionBits
					+ " (maxNumber=" + maxNumber + ").");
		}
		geoID |= hash;
		geoID |= id;

		return geoID;
	}

	/**
	 * Returns the minimum and maximum geoId for a hash.
	 * 
	 * @param hash
	 *            The sequential or parallel hash identifying longitude and latitude.
	 * @return Array where the first element identifies the minimum possible geoId and the second
	 * element identifies the maximum possible geoId.
	 */
	public Long[] getGeoIdRange(Long hash) {

		Long[] range = new Long[2];
		range[0] = getHash(hash);
		range[1] = hash | maxNumber;

		return range;
	}

	/**
	 * Returns the sequential or parallel hash for a given geoId.
	 * 
	 * @param geoId
	 * @return The plain hash.
	 */
	public Long getHash(Long geoId) {
		Long inverted = ~maxNumber;
		Long hash = inverted & geoId;
		return hash;
	}

	/**
	 * Returns the neighbouring hashes for the given hash, where the first
	 * dimension identifies the longitude values { longitude -1, longitude,
	 * longitude +1} and the second dimension identifies the latitude values{
	 * latitude +1, latitude, latitude -1 }. To put it in other words: From
	 * Top-Left rightwards & downwards to Bottom-Right.
	 * 
	 * @param hash
	 * @return
	 */
	public Long[][] getNeighbourHashes(Long hash) {
		Long[][] neighbourHashes = new Long[3][3];
		long[] separatedHash = splitHash(hash);

		neighbourHashes[0][0] = joinHash(separatedHash[0] - 1,
				separatedHash[1] + 1);
		neighbourHashes[0][1] = joinHash(separatedHash[0] - 1, separatedHash[1]);
		neighbourHashes[0][2] = joinHash(separatedHash[0] - 1,
				separatedHash[1] - 1);

		neighbourHashes[1][0] = joinHash(separatedHash[0], separatedHash[1] + 1);
		neighbourHashes[1][1] = joinHash(separatedHash[0], separatedHash[1]);
		neighbourHashes[1][2] = joinHash(separatedHash[0], separatedHash[1] - 1);

		neighbourHashes[2][0] = joinHash(separatedHash[0] + 1,
				separatedHash[1] + 1);
		neighbourHashes[2][1] = joinHash(separatedHash[0] + 1, separatedHash[1]);
		neighbourHashes[2][2] = joinHash(separatedHash[0] + 1,
				separatedHash[1] - 1);

		return neighbourHashes;
	}
	
	public Double[] getBoundingBox(Long parallelHash){
		Long boundingBox = parallelHash;
		double[] lonlat = decodeParallel(boundingBox);
		
		Long[][] neighbours = getNeighbourHashes(boundingBox);
		double[] leftNeighbour = decodeParallel(neighbours[0][1]);
		double[] topNeighbour = decodeParallel(neighbours[1][0]);
		double lonDiff = Math.abs((leftNeighbour[0] - lonlat[0])) / 2;
		double latDiff = Math.abs((topNeighbour[1] - lonlat[1])) / 2;
		return new Double[] {lonDiff, latDiff};
	}
	
	/**
	 * Returns the encapsulating rectangle for the series of hashes,identified by two hashes describing the upper left
	 * and the lower right of the rectangle. 
	 * @param hashes A list of points (hashes)
	 * @return An array containing the upper left point (hash) and the lower right point (hash)
	 */
	public long[] getEncapsulatingRectangle(List<Long> hashes){
		long[] encapsulation = new long[2];
	
		long minLon = Long.MAX_VALUE, maxLon = Long.MIN_VALUE, minLat = Long.MAX_VALUE, maxLat = Long.MIN_VALUE;
		boolean init = true;
		
		for(Long hash : hashes){
			
			long[] splitHash = splitHash(hash);
			long lon = splitHash[0];
			long lat = splitHash[1];
			
			if(init){
				minLon = lon;
				maxLon = lon;
				minLat = lat;
				maxLat = lat;
				init = false;
			}
			
			if(lon < minLon){
				minLon = lon;
			}else if(lon > maxLon){
				maxLon = lon;
			}
			
			if(lat < minLat){
				minLat = lat;
			}else if(lat > maxLat){
				maxLat = lat;
			}
		}
		
		long upperLeft = joinHash(minLon, maxLat);
		long lowerRight = joinHash(maxLon, minLat);
		
		encapsulation[0] = upperLeft;
		encapsulation[1] = lowerRight;
		
		return encapsulation;
	}
	
	/**
	 * Returns all bounding boxes of the rectangle encapsulating the list of points (hashes). The bounding boxes are returned
	 * in a 2-dimensional array starting from left to right and from top to bottom: 
	 * <ul>
	 * <li>[0][0] identifies the upper left bounding box.</li> 
	 * <li>[length-1][0] identifies upper right bounding box.</li>  
	 * <li>[0][length-1] identifies the lower left bounding box.</li>
	 * <li>[length-1][length-1] identifies lower right bounding box.</li>
	 * </ul>
	 * 
	 * This should typically be used on a lower precision key generator as shown below:
	 *  
	 * <pre>
	 * List<Long> ultraPrecisionPointList = new ArrayList<Long>();
	 * // Fill with points.
	 * 
	 * KeyGenerator keyGenLow = new KeyGenerator(KeyGenerator.PRECISION.LOW_20KM);
	 * KeyGenerator keyGenUltra = new KeyGenerator(KeyGenerator.PRECISION.ULTRA_1CM);
	 * 
	 * // Get the encapsulating rectangle on the list of ultra precision points. 
	 * long[] rectangle = keyGenUltra.getEncapsulatingRectangle(ultraPrecisionPointList);
	 * 
	 * // Convert to the target precision of your desired bounding boxes
	 * long upperLeft = keyGenUltra.reducePrecisionParallel(rectangle[0], PRECISION.LOW_20KM);
	 * long lowerRight = keyGenUltra.reducePrecisionParallel(rectangle[1], PRECISION.LOW_20KM);
	 * long[] lowPrecisionRectangle = new long[]{upperLeft, lowerRight};
	 * 
	 * // Get the bounding boxes. 
	 * long[][] encapsulatingBoundingBoxes = keyGenLow.getEncapsulatingRectangleBoundingBoxes(lowPrecisionRectangle);
	 * // Do whatever you have to do with the bounding boxes. May the force be with you! 
	 * </pre>
	 *  
	 *  
	 * @param rectangle A rectangle containing the upper left point and the lower right as encoded hashes.
	 * @return A 2-dimensional array where the first dimension identifies the x-axis and the second dimension the y-axis.
	 */
	public long[][] getEncapsulatingRectangleBoundingBoxes(long[] rectangle){
		long[][] boundingBoxes = null;
				
		long upperLeft = rectangle[0];
		long lowerRight = rectangle[1];
		
		long[] upperLeftSplit = splitHash(upperLeft);
		long[] lowerRightSplit = splitHash(lowerRight);
		
		long minLon = upperLeftSplit[0];
		long maxLon = lowerRightSplit[0];
		long maxLat = upperLeftSplit[1];
		long minLat = lowerRightSplit[1];
		
		long width = Math.abs(maxLon - minLon) + 1;
		long height = Math.abs(maxLat - minLat) + 1;
		
		if(width > Integer.MAX_VALUE || height > Integer.MAX_VALUE){
			throw new IndexOutOfBoundsException("The number of boundingboxes exceeds the limit for array creation Integer.MAX_VALUE="+Integer.MAX_VALUE+".Try using a lower resolution.");
		}
		
		boundingBoxes = new long[(int)width][(int)height];
		
		
		for(long x = minLon, i=0; x <= maxLon; x++, i++){
			for(long y = minLat, j=0; y <= maxLat; y++, j++){
				long hash = joinHash(x,y);
				boundingBoxes[(int) i][(int) j] = hash;
			}
		}
		
		return boundingBoxes;
	}
}
