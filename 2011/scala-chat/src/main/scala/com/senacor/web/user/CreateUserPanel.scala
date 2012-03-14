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

import com.senacor.user.UserManager
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.form.AjaxButton
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.PasswordTextField
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model

abstract class CreateUserPanel(id: String, model: IModel[User]) extends Panel(id, model){
  val form = new Form[User]("createUserForm", new CompoundPropertyModel[User](model))
  form.add(new TextField("vorname"))
  form.add(new TextField("name"))
  form.add(new TextField("userName"))
  val pw1Field = new PasswordTextField("password1", new Model[String]())
  form.add(pw1Field)

  val pw2Field = new PasswordTextField("password2", new Model[String]())
  form.add(pw2Field)

  form.add(new EqualPasswordInputValidator(pw1Field,pw2Field))


  form.add(new AjaxButton("button"){
      def onSubmit(target:AjaxRequestTarget, form: Form[_]){
        model.getObject.setPassword(pw1Field.getModelObject)
        UserManager.createUser(model.getObject)
        CreateUserPanel.this.setDefaultModelObject(new User())
        onSendUser(target)
      }

      def onError(target:AjaxRequestTarget, form: Form[_]){}

    })

  add(form)

  def onSendUser(target: AjaxRequestTarget)
  
}
