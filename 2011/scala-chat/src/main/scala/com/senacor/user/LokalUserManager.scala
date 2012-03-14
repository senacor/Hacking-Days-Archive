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

import com.senacor.web.user.User


class LocalUserManager extends Object with IUserManager {

  var users = List[User]()

  def getUserList = {
    users
  }

  def createUser(user: User) {
     users = user :: users
  }

  def login(userName: String, password: String): Boolean ={
    users.exists(user => user.userName == userName && user.password == password)
  }

}
