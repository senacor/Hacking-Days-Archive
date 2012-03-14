package com.senacor.user


import com.senacor.RunMode

object UserManager extends Object with IUserManager {

  val userManager: IUserManager =
    if(RunMode.mock) {
      new LocalUserManager()
    } else {
      new RemoteUserManager()
    }

  def getUserList = userManager.getUserList
  

  def createUser(user:com.senacor.web.user.User){
    userManager.createUser(user)
  }

  def login(userName: String, password: String) : Boolean = userManager.login(userName, password)
  
}