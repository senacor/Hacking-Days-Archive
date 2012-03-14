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

package com.senacor.web

import com.senacor.HomePage
import com.senacor.web.user.CreateUserPanel
import com.senacor.web.user.SignInPanel
import com.senacor.web.user.User
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.link.Link
import org.apache.wicket.model.Model

class BasePage extends WebPage{

  // Link to Home
  add(new Link("homeLink") {
      override def onClick() {
        setResponsePage(classOf[HomePage])
      }
    })

  // Link to open modal window
  val createUserMW = new ModalWindow("createUserMW")
  createUserMW.setInitialHeight(300)
  createUserMW.setInitialWidth(400)
  createUserMW.setContent(new CreateUserPanel(createUserMW.getContentId(), new Model[User](new User)){

      def onSendUser(target: AjaxRequestTarget) {
        createUserMW.close(target)
      }

    })
  add(createUserMW);

  add(new AjaxLink("createUser") {
      override def onClick(target:AjaxRequestTarget) {
        createUserMW.show(target)
      }
    })

  // Link to Login
  val modalWindow = new ModalWindow("modalWindow")
  modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback{
      override def onClose(target: AjaxRequestTarget){
        setResponsePage(classOf[HomePage])
      }
    })
  modalWindow.setInitialHeight(300)
  modalWindow.setInitialWidth(400)
  modalWindow.setContent(new SignInPanel(modalWindow.getContentId()){
      def loggedIn(target:AjaxRequestTarget,loginResult: Boolean){
        modalWindow.close(target)
      }
    })
  add(modalWindow)

  add(new AjaxLink("loginLink") {

      def onClick(ajaxRequestTarget: AjaxRequestTarget){
        modalWindow.show(ajaxRequestTarget);
      }

    })

}
