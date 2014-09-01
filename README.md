# Scala OpenStreetMap API
[![Build Status](https://secure.travis-ci.org/geow-org/api.png?branch=master)](http://travis-ci.org/geow-org/api)

Geow is a lightweight API for processing [OpenStreetMap](http://wiki.openstreetmap.org/wiki/Main_Page) elements. 

**Features**:
* Lightweight domain model
* Parsing of Osm files (currently xml is supported)
* High-performance binary serialization
* Support for geometric denormalization (i.e. Osm objects contain the full geometry)
* Efficient and flexible geo-hashing utilities

**Planned:**
* Pbf support

# Installation
SonaType deployment is under way. In the meantime you can use Geow by cloning the repo and building from source:
```
git clone https://github.com/geow-org/api.git
sbt update
sbt publishLocal
```
And then add the dependency to your build.sbt:
```scala
libraryDependencies ++= Seq(
  "org.geow" %% "geohash" % "0.1-SNAPSHOT",
  "org.geow" %% "model" % "0.1-SNAPSHOT",
  "org.geow" %% "parser" % "0.1-SNAPSHOT"
)
```


# Usage

## Parsing
Stream osm file to retrieve Osm objects. Currently, .osm files and .osm.bz2 files are supported. 
```scala
  
  import org.geow.parser.OsmParserFactory._

  // create a parser from a file
  val parser = createParser(fileName)
  
  // pull openstreetmap data
  for (elem <- parser) println(elem)

```

## Serialization
Serialize and deserialize Osm objects for network transfer. 
```scala
  import org.geow.model.serializer.OsmSerializer._
  
  ... parse or create an osm object element
  
  val serialized = toBinary(osmObject)
  val deserialized = fromBinary(serialized)
```
