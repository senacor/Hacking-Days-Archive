/*
 *  Copyright 2011 calle.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package com.senacor

import com.senacor.chatter.server.ChatterServer
import java.util.Date
import org.apache.wicket.protocol.http.WebSession
import org.apache.wicket.request.Request
import org.apache.wicket.request.Response
import chatter.server._
import scala.collection.mutable.ListBuffer
import chatter.server.MessageListener

class ChatSession(req: Request, res: Response, private var _userName:String = null) extends WebSession(req) with MessageListener {

  private[this] var _chatLines = ListBuffer[Message]()

  def chatLines = _chatLines.toList

  private[this] def receive(msg: Message) {
    System.out.println("ChatSession["+userName+"].receive " + msg)
    _chatLines += msg
    import ChatSession._
    if (_chatLines.size > MAX_LINES) {
      _chatLines trimStart (_chatLines.size - MAX_LINES)
    }
  }

  def userName_=(un: String) {
    _userName = un
    ChatterServer ! JoinMessage(userName, new Date)
  }

  def userName = _userName

  override def act() {
    loop {
      react {
        case m: Message => receive(m)
      }
    }
  }

  start()
}

object ChatSession {
  def get(): ChatSession = WebSession.get.asInstanceOf[ChatSession]

  val MAX_LINES = 10
}