package com.senacor.web.chat

import com.senacor.web.user.User
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.markup.html.panel.Panel

import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.event.Broadcast
import org.apache.wicket.event.IEventSink
import org.apache.wicket.event.IEventSource
import org.apache.wicket.model.Model
import org.apache.wicket.util.time.Duration

class UserDisplayPanel(id : String,
                       model : UserDisplayPanelModel,
                       eventSink : IEventSink) extends Panel(id, model) {
  val userRepeater = new ListView[User]("userRepeater", model) {
    override def populateItem(item : ListItem[User]) : Unit = {
      val userLink = new AjaxLink("userLink") {
        override def onClick(art: AjaxRequestTarget) {
          send(eventSink, Broadcast.EXACT, item.getModelObject());
        }
      }
      userLink.add(new Label("user", item.getModelObject().userName))
      item.add(userLink)

      //item.add(new Label("user", item.getModelObject().userName))
      //public <T extends Object> void send(IEventSink ies, Broadcast brdcst, T t);
    }
  }

  val seeAllMessagesLink = new AjaxLink("seeAllMessagesLink") {
    override def onClick(art: AjaxRequestTarget) {
      send(eventSink, Broadcast.EXACT, null);
    }
  }
  seeAllMessagesLink.add(new Label("seeAllMessages", "See all messages."))
  add(seeAllMessagesLink)

  add(new AjaxSelfUpdatingTimerBehavior(Duration.milliseconds(500)))

  add(userRepeater)

  setOutputMarkupId(true)

/*  def send(ies : IEventSink, brdcst : Broadcast , user : User) : Unit = {
    val event : UserSelectionEvent = new UserSelectionEvent(user, brdcst, this)
    ies.onEvent(event)
  } */
}
