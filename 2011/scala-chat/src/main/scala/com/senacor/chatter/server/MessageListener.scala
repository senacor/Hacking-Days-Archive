package com.senacor.chatter.server

import scala.actors.Actor

trait MessageListener extends Actor {
  def userName: String
} 