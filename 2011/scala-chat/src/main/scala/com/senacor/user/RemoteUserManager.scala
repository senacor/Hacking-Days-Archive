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

package com.senacor.user


import org.springframework.core.io.ClassPathResource
import com.senacor.hd11.model.User
import com.senacor.hd11.model.UserApplication
import org.springframework.beans.factory.xml.XmlBeanFactory
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import com.senacor.hd11.model.Users



class RemoteUserManager extends Object with IUserManager {

  val res = new ClassPathResource("config/spring/spring-context.xml")
  val beanFactory = new XmlBeanFactory(res)
  var restTemplate: RestTemplate = beanFactory.getBean("restTemplate").asInstanceOf[RestTemplate]

  val applicationPost = "http://192.168.2.100:18080/rest-1.0/rest/applications"

  val loginGet = "http://192.168.2.100:18080/rest-1.0/rest/users/{username}/authorization?pwd={pw}"

  val userListGet = "http://192.168.2.100:18080/rest-1.0/rest/users?state={state}"

  override def createUser(user: com.senacor.web.user.User) {

    val application = convertToApplication(user)

    restTemplate.postForLocation(applicationPost, application);
  }

  def convertToApplication(user: com.senacor.web.user.User) = {
    val application = new UserApplication()
    val convertedUser = new User()
    convertedUser.setUsername(user.userName)
    convertedUser.setFirstname(user.vorname)
    convertedUser.setLastname(user.name)
    convertedUser.setPassword(user.password)
    application.setUser(convertedUser)
    application
  }


  override def login(userName: String, password: String): Boolean = {
    try {
      restTemplate.getForObject(loginGet, classOf[String], userName, password);
      true
    } catch {
      case e: HttpClientErrorException => false
    }
  }

  override def getUserList(): List[com.senacor.web.user.User] = {
    try {
      var userList = restTemplate.getForObject(userListGet, classOf[Users], "ACTIVE")
      if (userList == null) {userList = new Users()}
      convertUserList(userList)
    } catch {
      case e: HttpClientErrorException => List()
    }
  }

  def convertUserList(userList: Users): List[com.senacor.web.user.User] = {
    var converted = List[com.senacor.web.user.User]()
    if (userList != null && userList.getUser != null) {
      userList.getUser.foreach(user => converted = convertUser(user) :: converted)
      converted.reverse
    }
    converted
  }

  def convertUser(user: User): com.senacor.web.user.User = {
    val converted: com.senacor.web.user.User = new com.senacor.web.user.User
    converted.vorname = user.getFirstname
    converted.name = user.getLastname
    converted.userName = user.getUsername
    converted.password = user.getPassword
    converted
  }

}
