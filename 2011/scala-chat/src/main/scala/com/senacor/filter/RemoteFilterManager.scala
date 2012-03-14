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

package com.senacor.filter

import com.senacor.chatter.server.Message
import java.io.IOException
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient

class RemoteFilterManager extends Object with IFilterManager {

  private val factory = XPathFactory.newInstance()
  private val domFactory = DocumentBuilderFactory.newInstance()
  domFactory.setNamespaceAware(true)

  def filterMessage(message: Message) = {
    val client = new DefaultHttpClient
    val post = new HttpPost("http://192.168.2.100:18080/rest-1.0/rest/messages/" + message.sender)
    post.setEntity(new StringEntity(message.text))
    val response = client.execute(post)
    val statusCode = response.getStatusLine.getStatusCode
    if (statusCode != 201) {
      throw new IOException("The filter service returned status code " + statusCode + ".")
    }

    val stream = response.getEntity.getContent

    val builder = domFactory.newDocumentBuilder()
    val doc = builder.parse(stream)

    val xpath = factory.newXPath()
    val expr = xpath.compile("/message/text")

    val filteredText = expr.evaluate(doc, XPathConstants.STRING).asInstanceOf[String]

    message.filtered(filteredText)
  }

}