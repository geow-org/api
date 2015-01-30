package org.geow.model

case class OsmProperties(
                          osmId: OsmId,
                          user: Option[OsmUser] = None,
                          version: OsmVersion
                          )
