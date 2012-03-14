package com.senacor.user

import com.senacor.web.user._
import java.util.ArrayList

trait IUserManager {
	
	def getUserList() : List[User]
	
	def createUser(user:com.senacor.web.user.User)
	
	def login(userName: String, password: String): Boolean
	
}