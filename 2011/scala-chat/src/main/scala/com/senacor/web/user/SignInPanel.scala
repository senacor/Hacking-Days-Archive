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

package com.senacor.web.user

import com.senacor.ChatSession
import com.senacor.chatter.server.BroadcastMessage
import com.senacor.chatter.server.ChatterServer
import com.senacor.user.UserManager
import com.senacor.chatter.server._
import com.senacor.web.BasePage
import java.util.Date
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.form.AjaxButton
import org.apache.wicket.markup.html.form.Button
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.PasswordTextField
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.PropertyModel

abstract class SignInPanel(id: String, var userName: String = null, var password: String = null) extends Panel(id){

  val form = new Form("loginForm")
  val textField = new TextField("username", new PropertyModel[String](this,"userName"))
  textField.setRequired(true)
  form.add(textField)
  
  val passwordTF = new PasswordTextField("password", new PropertyModel[String](this,"password"))
  form.add(passwordTF)

  form.add(new AjaxButton("button"){
      def onSubmit(target:AjaxRequestTarget, form: Form[_]){
        val res = UserManager.login(textField.getModelObject, passwordTF.getModelObject)
        if (res){
          ChatSession.get.userName = textField.getInput
        } else {
          // TODO Feedback an den Benutzer
        }
          loggedIn(target, res)

      }

      def onError(target:AjaxRequestTarget, form: Form[_]){}
      
    })
  add(form)

  def loggedIn(target:AjaxRequestTarget, loginResult: Boolean)
  
}
