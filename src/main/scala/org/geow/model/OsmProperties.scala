package org.geow.model

case class OsmProperties(
                          osmId: OsmId,
                          user: Option[OsmUser] = None,
                          version: OsmVersion
                          ) {
  override def toString = StringBuilder.newBuilder.++=(osmId.toString).++=(user.getOrElse("unknown").toString).++=(version.toString).toString()
}
