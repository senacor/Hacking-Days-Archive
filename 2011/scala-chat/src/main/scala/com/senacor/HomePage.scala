package com.senacor

import com.senacor.web.BasePage
import com.senacor.web.chat.ChatRoomPanel


class HomePage extends BasePage {
  add(new ChatRoomPanel("chatRoomPanel"))
}
