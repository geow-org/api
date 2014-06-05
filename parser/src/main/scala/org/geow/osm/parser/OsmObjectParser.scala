package org.geow.osm.parser

import scala.xml._
import scala.xml.pull._
import scala.io.Source
import org.geow.model.osm._
import org.joda.time.DateTimeZone
import org.joda.time.format.ISODateTimeFormat
import scala.util.Try

class OsmObjectParser(source: Source) {

  import org.geow.osm.parser.OsmObjectParser._

  val reader = new XMLEventReader(source)
  //val reader = new XMLEventReader(io.Source.fromFile("movies.xml")("UTF-8"))

  def hasNext() = reader.hasNext

  def next(): Option[OsmObject] = {

    var currentNode = ""
    var result: Option[OsmObject] = None

    while (reader.hasNext && result == None) {
      reader.next match {
        case EvElemStart(_, "relation", attr, _) =>
          print("start relation\n")
          val props = parseProperties(attr)
          print(props)
        case elem @ EvElemStart(_, "member", attr, _) =>
          val member = parseMember(attr)
          print(member)
        case elem @ EvElemStart(_, "tag", attr, _) =>
          val tag = parseTag(attr)
          print(tag)
        case EvElemStart(_, elem, _, _) => {
          print(s"start elem $elem\n")
          currentNode = elem
        }
        case EvText(text) => {
          currentNode match {
            case s => print(s"text$s")
          }
        }
        case EvElemEnd(_, "relation") => println("end relation")
        case EvElemEnd(_, _) => currentNode = ""
        case _ =>
      }
    }
    result
  }

  def parseMember(attr: MetaData): OsmMember = {
    val `type`: OsmType = attr("type").text match {
      case "relation" => OsmTypeRelation
      case "way" => OsmTypeWay
      case _ => OsmTypeNode
    }
    val ref = attr("ref").text.toInt
    val role = attr("role").text match {
      case "inner" => OsmRoleInner
      case "outer" => OsmRoleOuter
      case _ => OsmRoleEmpty
    }
    OsmMember(ref, role, `type`)
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
}

object OsmObjectParser {

  val XML_DATE_INPUT_FORMAT = ISODateTimeFormat.dateTime()
  val XML_DATE_OUTPUT_FORMAT = ISODateTimeFormat.dateTime().withZone(DateTimeZone.UTC)

  def convertXmlDateToLong(xmlTime: String): Long = {
    val millis = Try(XML_DATE_INPUT_FORMAT.parseDateTime(xmlTime).getMillis).getOrElse(System.currentTimeMillis)
    millis
  }
}