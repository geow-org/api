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
import org.geow.geohash.GeoHashUtils

class OsmObjectParser(source: Source) {

  import org.geow.osm.parser.OsmObjectParser._

  val reader = new XMLEventReader(source)

  def hasNext() = reader.hasNext

  def next(): Option[OsmObject] = {

    var currentNode = ""
    var result: Option[OsmObject] = None

    var props: Option[OsmProperties] = None
    var tagList = ListBuffer[OsmTag]()

    var memberList = ListBuffer[OsmMember]()

    var ndList = ListBuffer[Long]()

    var point : Option[OsmPoint] = None
    
    def resetElements = {
      result = None
      tagList.clear
      memberList.clear
      ndList.clear
    }

    while (reader.hasNext && result == None) {
      reader.next match {
        case EvElemStart(_, "node", attr, _) => {
          resetElements
          props = Some(parseProperties(attr))
          point = Some(parsePoint(attr))
        }
        case EvElemStart(_, "way", attr, _) => {
          resetElements
          props = Some(parseProperties(attr))
        }
        case EvElemStart(_, "relation", attr, _) => {
          resetElements
          props = Some(parseProperties(attr))
        }
        case elem @ EvElemStart(_, "nd", attr, _) => {
          ndList += parseNd(attr)
        }
        case elem @ EvElemStart(_, "member", attr, _) => {
          memberList += parseMember(attr)
        }
        case elem @ EvElemStart(_, "tag", attr, _) => {
          tagList += parseTag(attr)
        }
        case EvElemStart(_, elem, _, _) => {
          currentNode = elem
        }
        case EvText(text) => {
          currentNode match {
            case s =>
          }
        }
        case EvElemEnd(_, "way") => {
          props.map(p => {
            result = Some(OsmWay(p, tagList.toList, ndList.toList))
          })
        }
        case EvElemEnd(_, "relation") => {
          props.map(p => {
            result = Some(OsmRelation(p, tagList.toList, memberList.toList))
          })
        }
        case EvElemEnd(_, _) => currentNode = ""
        case _ =>
      }
    }
    result
  }

  def parseNd(attr: MetaData): Long = {
    attr("ref").text.toLong
  }

  def parseMember(attr: MetaData): OsmMember = {
    val `type`: OsmType = attr("type").text match {
      case "relation" => OsmTypeRelation
      case "way" => OsmTypeWay
      case _ => OsmTypeNode
    }
    val ref = attr("ref").text.toLong
    val role = attr("role").text match {
      case "inner" => OsmRoleInner
      case "outer" => OsmRoleOuter
      case _ => OsmRoleEmpty
    }
    OsmMember(`type`, ref, role)
  }

  def parseTag(attr: MetaData): OsmTag = OsmTag(attr("k").text, attr("v").text)

  def parseProperties(attr: MetaData): OsmProperties = {

    val id = attr("id").text.toLong
    val user = attr("user").text
    val uid = attr("uid").text.toLong
    val timestamp = convertXmlDateToLong(attr("timestamp").text)
    val visible = attr("visible").text match {
      case "false" => false
      case _ => true
    }
    val version = attr("version").text.toInt
    val changeset = attr("changeset").text.toInt
    OsmProperties(
      id,
      user,
      uid,
      timestamp,
      visible,
      version,
      changeset)
  }
  
  def parsePoint(attr: MetaData): OsmPoint = {    
    val lat = attr("lat").text.toLong
    val lon = attr("lon").text.toLong
    OsmPoint(lon,lat)
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