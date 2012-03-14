package com.senacor.web.chat

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.model.IModel
import org.apache.wicket.model.LoadableDetachableModel
import org.apache.wicket.model.Model
import com.senacor.ChatSession
import com.senacor.chatter.server.Message
import scala.collection.JavaConversions._

class ChatRoomPanel(id : String) extends Panel(id) {
  private[this] val chatDisplayPanel = new ChatDisplayPanel("chatDisplayPanel", new LoadableDetachableModel[java.util.List[Message]](){
      override def load() = {
//        System.out.println("load Messages for " + ChatSession.get.userName)
        ChatSession.get.chatLines
      }
    }).setOutputMarkupId(true)
  add(chatDisplayPanel)
  add(new UserDisplayPanel("userDisplayPanel", new UserDisplayPanelModel(), chatDisplayPanel))

  // Eingabe neue Nachricht
  val chatForm = new Form("chatForm"){
    override def onConfigure(){
      setVisible(ChatSession.get.userName ne null)
    }
  }
  
  val inputField = new TextField[String]("inputField", new Model[String]())
  inputField.setOutputMarkupId(true)
  inputField setRequired(true)
  chatForm.add(inputField)
  chatForm.add(new SubmitButton("submitButton", inputField))
  add(chatForm)

}
