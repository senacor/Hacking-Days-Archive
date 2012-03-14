package com.senacor.web.chat

import scala.collection.JavaConversions._
import com.senacor.user.UserManager
import com.senacor.web.user.User
import org.apache.wicket.model.LoadableDetachableModel

class UserDisplayPanelModel extends LoadableDetachableModel[java.util.List[User]] {
  override def load() = {
    UserManager.getUserList
  }
}
