/*
 *  Copyright 2011 osiefart.
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
import chatter.server._
import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.request.Request
import org.apache.wicket.request.Response

class ChatApplication extends WebApplication {
  override def getHomePage() = {
    classOf[HomePage]
  }

  override def init() = {
  }

  val server = ChatterServer

  override def newSession(req: Request, res: Response) = {
    val s = new ChatSession(req, res)
    server ! Register(s)
    s
  }

}

object ChatApplication {
  def get: ChatApplication = WebApplication.get.asInstanceOf[ChatApplication] 
}
