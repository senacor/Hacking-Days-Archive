package com.senacor.web.chat

import com.senacor.ChatSession
import com.senacor.chatter.server._

import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.markup.html.panel.Panel
import com.senacor.web.user.User
import java.text.SimpleDateFormat
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior
import org.apache.wicket.event.IEvent
import org.apache.wicket.event.IEventSink
import org.apache.wicket.behavior.AttributeAppender
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.util.time.Duration

class ChatDisplayPanel(id : String, model : IModel[java.util.List[Message]]) extends Panel(id, model) {
  var selectedUser : User = null

  val messageRepeater = new ListView[Message]("messageRepeater", model) {
    override def populateItem(item : ListItem[Message]) : Unit = {
      val chatMessage = item.getModelObject()

      val simpleDateFormat = new SimpleDateFormat("hh:mm:ss")
      val date = "[" + simpleDateFormat.format(chatMessage.date) + "]"
      val user = "<" + chatMessage.sender + ">"



      val displayBroadcastMessage = (selectedUser == null
                            || chatMessage.sender == selectedUser.userName)
      val displayPrivateMessage = (selectedUser == null
                                   || ((chatMessage.isInstanceOf[PrivateSentMessage] && chatMessage.asInstanceOf[PrivateSentMessage].receiver == selectedUser.userName)
                                        || (chatMessage.isInstanceOf[PrivateMessage] && chatMessage.asInstanceOf[PrivateMessage].receiver == selectedUser.userName)))

      if(displayBroadcastMessage || displayPrivateMessage) {
        item.add(new Label("date", date))
        val msgModel = Model.of(chatMessage.text)
        val msgLabel = new Label("message", msgModel)
        val msgClassModel = Model.of("")
        msgLabel.add(new AttributeAppender("class", true, msgClassModel, " "))
        item.add(msgLabel)
        val userModel = Model.of(user)
        item.add(new Label("user", userModel))

        chatMessage match {
        case PrivateSentMessage(_, receiver, _, _) =>
          userModel.setObject(user + " => " + receiver)
          msgClassModel.setObject("private-sent")
        case p: PrivateMessage =>
          msgClassModel.setObject("private-received")
        case JoinMessage(joiner, _) =>
          userModel.setObject("")
          msgModel.setObject("*** " + joiner + " has joined.")
        case _ => // NOP
        }
      } else {
        item.add(new Label("date", ""))
        item.add(new Label("user", ""))
        item.add(new Label("message", ""))
      }
    }
  }
  
  add(new AjaxSelfUpdatingTimerBehavior(Duration.milliseconds(500)))

  add(messageRepeater)

  setOutputMarkupId(true)

  override def onEvent(event: IEvent[_]) = {
    if (event.getPayload.isInstanceOf[User]) {
      println("Setting User: " + event.getPayload().asInstanceOf[User].userName)
      selectedUser = event.getPayload().asInstanceOf[User]
    } else if (event.getPayload() == null) {
      selectedUser = null
    }
  }
}
