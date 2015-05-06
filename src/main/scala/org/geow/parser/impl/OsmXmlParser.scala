package org.geow.parser.impl

import scala.xml._
import scala.xml.pull._
import scala.io.Source
import org.geow.model._
import org.joda.time.format.ISODateTimeFormat
import scala.util.{Failure, Try, Success}
import scala.collection.mutable.ListBuffer
import org.geow.model.geometry.Point
import scala.io.Codec
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
import java.io.FileInputStream
import scala.xml.NodeSeq.seqToNodeSeq
import org.geow.parser.OsmParser

case class OsmXmlParser(source: Source) extends OsmParser {

  import OsmXmlParser._

  val reader = new XMLEventReader(source)

  override def hasNext() = reader.hasNext

  override def next(): Option[OsmObject] = {

    var result: Option[OsmObject] = None

    var idOption: Option[OsmId] = None
    var userOption: Option[OsmUser] = None
    var version: OsmVersion = OsmVersion()

    var tagList = ListBuffer[OsmTag]()

    var memberList = ListBuffer[OsmMember]()

    var ndList = ListBuffer[OsmId]()

    var pointOption: Option[Point] = None

    def resetElements = {
      result = None
      idOption = None
      userOption = None
      version = OsmVersion()
      pointOption = None
      tagList.clear
      memberList.clear
      ndList.clear
    }

    def parseProperties(attr: MetaData): Unit = {
      idOption = parseId(attr).toOption
      version = parseVersion(attr)
      userOption = parseUser(attr).toOption
    }

    while (reader.hasNext && result == None) {
      reader.next match {
        case EvElemStart(pre, "node", attr, _) => {
          resetElements
          parseProperties(attr)

          pointOption = parsePoint(attr).toOption
        }
        case EvElemStart(pre, "way", attr, _) => {
          resetElements
          parseProperties(attr)
        }
        case EvElemStart(pre, "relation", attr, _) => {
          resetElements
          parseProperties(attr)
        }
        case elem@EvElemStart(pre, "nd", attr, _) => {
          parseNd(attr).map(nd => ndList += nd)
        }
        case elem@EvElemStart(pre, "member", attr, _) => {
          parseMember(attr).map(member => memberList += member)
        }
        case elem@EvElemStart(pre, "tag", attr, _) => {
          parseTag(attr).map(tag => tagList += tag)
        }
        case EvElemStart(pre, elem, _, _) =>
        case EvText(text) =>
        case EvElemEnd(pre, "node") => {
          (idOption,userOption,version,pointOption) match {
            case (Some(id), user, version, Some(point)) => result = Some(OsmNode(id,user,version,tagList.toList, point))
            case _ => result = None
          }
        }
        case EvElemEnd(pre, "way") => {
          (idOption,userOption,version, ndList.toList) match {
            case (Some(id), user, version, first :: second :: rest) => result = Some(OsmWay(id,user,version,tagList.toList,ndList.toList))
            case _ => result = None
          }
        }
        case EvElemEnd(pre, "relation") => {
          (idOption,userOption,version, memberList.toList) match {
            case (Some(id), user, version, first :: rest) => result = Some(OsmRelation(id,user,version,tagList.toList,memberList.toList))
            case _ => result = None
          }
        }
        case EvElemEnd(pre, elem) =>
        case _ =>
      }
    }
    result
  }


}

object OsmXmlParser {

  val XML_DATE_INPUT_FORMAT = ISODateTimeFormat.dateTimeNoMillis()
  val XML_DATE_OUTPUT_FORMAT = ISODateTimeFormat.dateTime()

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
        case s: String if !s.isEmpty => new OsmRoleOther(s)
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

  def parseVersion(attr: MetaData): OsmVersion = {

    val visibleOptStr = attr.get("visible")
    val visible: Boolean = visibleOptStr match {
      case None => true
      case Some(seq: Seq[Node]) => seq.text match {
        case "false" => false
        case _ => true
      }
    }

    val timestampOptStr = attr.get("timestamp")
    val timestamp: Long = timestampOptStr match {
      case None => System.currentTimeMillis()
      case Some(seq: Seq[Node]) => convertXmlDateToLong(seq.text)
    }

    val versionOptStr = attr.get("version")
    val version: Int = versionOptStr match {
      case None => 1
      case Some(seq: Seq[Node]) => Try(seq.toString.toInt).getOrElse(1)
    }

    val changesetOptStr = attr.get("changeset")
    val changeset: Int = changesetOptStr match {
      case None => 1
      case Some(seq: Seq[Node]) => Try(seq.toString.toInt).getOrElse(1)
    }

    OsmVersion(timestamp, version, changeset, visible)
  }

  def parseUser(attr: MetaData): Try[OsmUser] = {
    Try(OsmUser(attr("user").text, attr("uid").text.toLong))
  }

  def parseId(attr: MetaData): Try[OsmId] = {
    val idOptStr = attr.get("id")
    val idTry = idOptStr match {
      case None => Failure(new scala.Error("Element does not have an id"))
      case Some(seq: Seq[Node]) => Try(OsmId(seq.text.toLong))
    }
    idTry
  }

  def parsePoint(attr: MetaData): Try[Point] = {
    Try({
      val lat = attr("lat").text.toDouble
      val lon = attr("lon").text.toDouble
      Point(lon, lat)
    })
  }

  def convertXmlDateToLong(xmlTime: String): Long = {

    val parseIntent = Try(XML_DATE_INPUT_FORMAT.parseDateTime(xmlTime).getMillis)

    val millis = parseIntent.toOption.getOrElse(System.currentTimeMillis)
    millis
  }

}