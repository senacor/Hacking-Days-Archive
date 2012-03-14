package com.senacor.chatter.server

import java.io.Serializable
import java.util.Date

sealed abstract class Message extends Serializable {
  def sender: String
  def date: Date
  def text: String
  def filtered(newText: String): Message
  require(sender != null, "sender must not be null!")
  require(date != null, "date must not be null!")
  require(text != null, "text must not be null!")
}

case class BroadcastMessage(sender: String, date: Date, text: String) extends Message {
  override def filtered(newText: String): BroadcastMessage = copy(text=newText)
}

case class PrivateMessage(sender: String, receiver: String, date: Date, text: String) extends Message {
  require(receiver != null, "receiver must not be null")
  override def filtered(newText: String): PrivateMessage = copy(text=newText)
}
case class PrivateSentMessage(sender: String, receiver: String, date: Date, text: String) extends Message {
  require(receiver != null, "receiver must not be null")
  override def filtered(newText: String): PrivateSentMessage = copy(text=newText)
}

case class JoinMessage(sender: String, date: Date) extends Message {
  override def text = ""
  override def filtered(newText: String) = this
}