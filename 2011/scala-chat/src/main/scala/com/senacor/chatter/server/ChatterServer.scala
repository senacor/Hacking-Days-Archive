package com.senacor.chatter.server

import scala.actors.Actor
import scala.actors.Actor._
import com.senacor.filter.FilterManager
import java.io._

object ChatterServer extends Actor {

  private val listeners = scala.collection.mutable.Set[MessageListener]()

  override def act() {
    loop {
      react {
        case Register(listener) =>
          listeners += listener
        case Unregister(listener) =>
          listeners -= listener
        case message: Message => 
          try {
            val filteredMessage = FilterManager.filterMessage(message)
            System.out.println("chatterserver.sendmessage: " + filteredMessage)
            filteredMessage match {
              case u: PrivateMessage =>
                println("Got a privatemessage: " + u)
                listeners.foreach(listener => if (listener.userName == u.receiver) listener ! u)
                listeners.foreach(listener => if (listener.userName == u.sender) listener ! PrivateSentMessage(u.sender, u.receiver, u.date, u.text))
              case b: Message =>
                println("Got a message: " + b)
                listeners.foreach(listener => listener ! b)
            }
          } catch {
            case ioe: IOException => println(ioe.getMessage + " The message is not delivered.")
            case e: Exception => println(e.getMessage + " The message is not delivered.")
          }
      }
    }
  }

  start()
}

case class Register(listener: MessageListener)
case class Unregister(listener: MessageListener)
