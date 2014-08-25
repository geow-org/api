# Scala OpenStreetMap API
[![Build Status](https://secure.travis-ci.org/geow-org/api.png?branch=master)](http://travis-ci.org/geow-org/api)

Geow is a lightweight API for processing [OpenStreetMap](http://wiki.openstreetmap.org/wiki/Main_Page) elements. 

**Features**:
* Lightweight domain model
* Parsing of Osm files (currently xml is supported)
* Binary roundtrip serialization
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

# Usage

## Parsing
```scala
  
  import org.geow.osm.parser.OsmObjectParser

  // use some arbitrary openstreetmap document, either as text or from an osm.xml file and create a Source object
  val xml = 
    <osm version="0.6" generator="CGImap 0.3.3 (32183 thorn-03.openstreetmap.org)" copyright="OpenStreetMap and contributors" attribution="http://www.openstreetmap.org/copyright" license="http://opendatacommons.org/licenses/odbl/1-0/">
  	<node id="240126753" visible="true" version="25" changeset="21833691" timestamp="2014-04-21T09:57:51Z" user="bilderhobbit" uid="503347" lat="51.2251964" lon="6.7737511">
  		<tag k="capital" v="4"/>
  		<tag k="name" v="Düsseldorf"/>
  		<tag k="place" v="city"/>
  		<tag k="population" v="585054"/>
  		<tag k="website" v="http://www.duesseldorf.de"/>
  		<tag k="wikipedia" v="de:Düsseldorf"/>
  	</node>
  </osm>
  val source = Source.fromString(xml.toString)
  
  // create a parser for a Source object
  val parser = new OsmObjectParser(source)
  
  // pull openstreetmap data
  while (parser.hasNext)
    println(parser.next())

```

## Serialization
```scala
  import org.geow.model.serializer.OsmSerializer._
  
  ... parse or create an osm object element
  
  val serialized = toBinary(osmObject)
  val deserialized = fromBinary(serialized)
```
