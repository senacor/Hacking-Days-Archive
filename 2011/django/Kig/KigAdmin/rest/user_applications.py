__author__ = 'sbehnel'

from urllib2 import urlopen
from urllib import urlencode
from httplib import HTTPConnection
import xml.etree.cElementTree as ET
import logging

from KigAdmin.rest.config import REST_HOST, REST_PORT, REST_URL, REST_BASE


log = logging.getLogger('user_applications')

post_headers = {'Content-Type' : 'application/xml'}

class User(object):
    def __init__(self, login_name, first_name, last_name, password):
        self.login_name, self.first_name, self.last_name = login_name, first_name, last_name
        self.password = password

    @classmethod
    def _from_xml(cls, xml):
        """
        >>> xml_data = '<user><firstname>John</firstname><lastname>Doe</lastname><state>PENDING</state><username>jdoe</username><password>secret</password></user>'
        >>> user_xml = ET.fromstring(xml_data)
        >>> user = User._from_xml(user_xml)
        >>> print(user.first_name)
        John
        >>> print(user.last_name)
        Doe
        >>> print(user.login_name)
        jdoe
        >>> print(user.password)
        secret
        """
        return cls(xml.findtext('username'), xml.findtext('firstname'), xml.findtext('lastname'), xml.findtext('password'))

    def _to_xml(self):
        el = ET.Element('user')
        ET.SubElement(el, 'firstname').text = self.first_name
        ET.SubElement(el, 'lastname').text = self.last_name
        ET.SubElement(el, 'username').text = self.login_name
        ET.SubElement(el, 'password').text = self.password
        return el

    def __unicode__(self):
        return u"User(%s, %s, %s, %s)" % (self.login_name, self.first_name, self.last_name, self.password)


class Application(object):
    def __init__(self, uuid, state, user):
        self.uuid = uuid
        self.state = state
        self.user = user

    @classmethod
    def _from_xml(cls, xml):
        """
        >>> xml_data = '<userApplication><state>PENDING</state><user><firstname>John</firstname><lastname>Doe</lastname><state>PENDING</state><username>jdoe</username></user></userApplication>'
        >>> application_xml = ET.fromstring(xml_data)
        >>> app = Application._from_xml(application_xml)
        >>> print(app.state)
        PENDING
        """
        user = xml.find('user')
        return cls(xml.findtext('appuuid'), xml.findtext('state'), User._from_xml(user) if user is not None else None)

    def _to_xml(self):
        el = ET.Element('userApplication')
        ET.SubElement(el, 'appuuid').text = self.uuid
        ET.SubElement(el, 'state').text = self.state
        el.append(self.user._to_xml())
        return el

    def accept(self, owner):
        xml = self._to_xml()
        xml[1].text = 'ACCEPTED' # state
        return self._update(xml, owner)

    def reject(self, owner):
        xml = self._to_xml()
        xml[1].text = 'REJECTED' # state
        return self._update(xml, owner)

    def _update(self, xml, owner):
        connection = HTTPConnection('%s:%s' % (REST_HOST, REST_PORT))
        try:
            connection.request('PUT', '%s/applications/%s?owner=%s' % (REST_BASE, self.uuid, owner),
                               ET.tostring(xml),
                               post_headers)
            result = connection.getresponse()
        finally:
            connection.close()
        if result.status < 200 or result.status >= 400:
            log.error("PUT request returned result %s: %s", result.status, result.msg)
            return False
        return True

def get_applications(**query):
    query_string = '?' + urlencode(query) if query else ''
    response = urlopen(REST_URL + 'applications' + query_string)
    xml = ET.parse(response).getroot()
    return xml

def get_pending_applications(owner):
    """
    >>> len(list(get_pending_applications())) > 0
    True
    """
    for application_xml in get_applications(owner=owner):
        app = Application._from_xml(application_xml)
        if app.uuid and app.user:
            yield app
