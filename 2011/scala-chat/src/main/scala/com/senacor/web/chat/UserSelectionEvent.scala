package com.senacor.web.chat

import org.apache.wicket.event.Broadcast
import org.apache.wicket.event.IEventSource
import com.senacor.web.user.User
import org.apache.wicket.event.IEvent

class UserSelectionEvent(user: User, broadcast: Broadcast, source: IEventSource) extends IEvent[User] {
  override def getPayload() : User = {
    user
  }

  override def stop() : Unit = {}
  override def dontBroadcastDeeper() : Unit = {}
  override def getType() : Broadcast = {broadcast}
  override def getSource() : IEventSource = {source}
}
