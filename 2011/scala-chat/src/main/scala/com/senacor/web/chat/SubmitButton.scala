package com.senacor.web.chat;

import com.senacor.ChatSession
import com.senacor.chatter.server.ChatterServer
import com.senacor.chatter.server._

import java.util.Date
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField

class SubmitButton(id : String, inputField : TextField[String]) extends AjaxButton(id) {

  override def onError(arg0 : AjaxRequestTarget , arg1 : Form[_]) : Unit = {
  }

  override def onSubmit(ajaxRequestTarget : AjaxRequestTarget, arg1 : Form[_]) : Unit = {
    val line = inputField.getModelObject
    val currentUser = ChatSession.get.userName
    System.out.println("submit by "+currentUser+": " + line)
    val message: Message =
      if (line startsWith "/msg ") {
        val (receiver, msg) = (line drop 5) span (_ != ' ')
        PrivateMessage(currentUser, receiver, new Date, msg)
      } else BroadcastMessage(currentUser, new Date, line)
    ChatterServer ! message
    inputField.setModelObject("")
    ajaxRequestTarget add inputField
  }
}
