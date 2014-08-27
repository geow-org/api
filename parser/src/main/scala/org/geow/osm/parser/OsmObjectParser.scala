package org.geow.osm.parser

import scala.xml._
import scala.xml.pull._
import scala.io.Source
import org.geow.model._
import org.joda.time.DateTimeZone
import org.joda.time.format.ISODateTimeFormat
import scala.util.Try
import scala.collection.mutable.ListBuffer
import scala.util.Success
import scala.util.Failure
import org.geow.model.geometry.OsmPoint
import org.geow.model.geometry.OsmPoint

class OsmObjectParser(source: Source) extends Iterator[Option[OsmObject]]{

  import org.geow.osm.parser.OsmObjectParser._

  val reader = new XMLEventReader(source)

  override def hasNext() = reader.hasNext

  override def next(): Option[OsmObject] = {

    var result: Option[OsmObject] = None

    var propertiesOption: Option[OsmProperties] = None
    var tagList = ListBuffer[OsmTag]()

    var memberList = ListBuffer[OsmMember]()

    var ndList = ListBuffer[OsmId]()

    var pointOption: Option[OsmPoint] = None

    def resetElements = {
      result = None
      propertiesOption = None
      pointOption = None
      tagList.clear
      memberList.clear
      ndList.clear
    }

    while (reader.hasNext && result == None) {
      reader.next match {
        case EvElemStart(_, "node", attr, _) => {
          resetElements
          propertiesOption = parseProperties(attr).toOption
          pointOption = parsePoint(attr).toOption
        }
        case EvElemStart(_, "way", attr, _) => {
          resetElements
          propertiesOption = parseProperties(attr).toOption
        }
        case EvElemStart(_, "relation", attr, _) => {
          resetElements
          propertiesOption = parseProperties(attr).toOption
        }
        case elem @ EvElemStart(_, "nd", attr, _) => {
          parseNd(attr).map(nd => ndList += nd)
        }
        case elem @ EvElemStart(_, "member", attr, _) => {
          parseMember(attr).map(member => memberList += member)
        }
        case elem @ EvElemStart(_, "tag", attr, _) => {
          parseTag(attr).map(tag => tagList += tag)
        }
        case EvElemStart(_, elem, _, _) => 
        case EvText(text) => 
        case EvElemEnd(_, "node") => {
          pointOption.map(point => {
            propertiesOption.map(properties => {
              result = Some(OsmNode(properties, tagList.toList, point))
            })
          })
        }
        case EvElemEnd(_, "way") => {
          propertiesOption.map(properties => {
            result = Some(OsmWay(properties, tagList.toList, ndList.toList))
          })
        }
        case EvElemEnd(_, "relation") => {
          propertiesOption.map(properties => {
            result = Some(OsmRelation(properties, tagList.toList, memberList.toList))
          })
        }
        case EvElemEnd(_, _) => 
        case _ =>
      }
    }
    result
  }

  def parseNd(attr: MetaData): Try[OsmId] = Try(OsmId(attr("ref").text.toLong))

  def parseMember(attr: MetaData): Try[OsmMember] = {
    Try({
      val `type`: OsmType = attr("type").text match {
        case "relation" => OsmTypeRelation
        case "way" => OsmTypeWay
        case _ => OsmTypeNode
      }
      val ref = OsmId(attr("ref").text.toLong)
      val role = attr("role").text match {
        case "inner" => OsmRoleInner
        case "outer" => OsmRoleOuter
        case _ => OsmRoleEmpty
      }
      OsmMember(`type`, ref, role)
    })
  }

  def parseTag(attr: MetaData): Try[OsmTag] = {
    val key = attr("k").text
    val value = attr("v").text
    key match {
      case s if !s.isEmpty() => Success(OsmTag(key, value))
      case _ => Failure(new Error("Key is empty"))
    }
  }

  
  def parseProperties(attr: MetaData): Try[OsmProperties] = {
    Try({
      val id = OsmId(attr("id").text.toLong)
      val user = OsmUser(attr("user").text,attr("uid").text.toLong)
      val visible = attr("visible").text match {
        case "false" => false
        case _ => true
      }
      val version = OsmVersion(
          convertXmlDateToLong(attr("timestamp").text),
              attr("version").text.toInt,
              attr("changeset").text.toInt,
              visible)
      OsmProperties(id, user, version)
    })
  }

  def parsePoint(attr: MetaData): Try[OsmPoint] = {
    Try({
      val lat = attr("lat").text.toDouble
      val lon = attr("lon").text.toDouble
      OsmPoint(lon, lat)
    })
  }
}

object OsmObjectParser {

  val XML_DATE_INPUT_FORMAT = ISODateTimeFormat.dateTimeNoMillis()
  val XML_DATE_OUTPUT_FORMAT = ISODateTimeFormat.dateTime()

  def convertXmlDateToLong(xmlTime: String): Long = {

    val parseIntent = Try(XML_DATE_INPUT_FORMAT.parseDateTime(xmlTime))
    val millis = parseIntent match {
      case Success(dateTime) => dateTime.getMillis
      case Failure(e) => {
        print(e)
        System.currentTimeMillis
      }
    }
    millis
  }
}