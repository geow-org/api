package org.geow.model

case class OsmUser(username:String, uid:Long) {
  override def toString = StringBuilder.newBuilder.++=(username).++=("->").++=(uid.toString).toString()
}