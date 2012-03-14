noname:glassfishv3 rwinzing$ curl -i -X POST -H "Content-Type: application/json" -d '{"vorname":"michael","nachname":"mustermann"}' http://localhost:8080/jerseytest/rest/customers
HTTP/1.1 201 Created
X-Powered-By: Servlet/3.0
Server: GlassFish v3
Location: http://localhost:8080/jerseytest/rest/customers/1
Content-Length: 0
Date: Wed, 25 Nov 2009 13:52:23 GMT

noname:glassfishv3 rwinzing$ curl -i -X POST -H "Content-Type: application/json" -d '' http://localhost:8080/jerseytest/rest/customer/1/orders
HTTP/1.1 201 Created
X-Powered-By: Servlet/3.0
Server: GlassFish v3
Location: http://localhost:8080/jerseytest/rest/customer/1/orders/1
Content-Length: 0
Date: Wed, 25 Nov 2009 13:52:26 GMT

noname:glassfishv3 rwinzing$ curl -i -X PUT -H "Content-Type: application/json" -d '{"orderState":"INACTIVE"}' http://localhost:8080/jerseytest/rest/customer/1/order/2
HTTP/1.1 202 Accepted
X-Powered-By: Servlet/3.0
Server: GlassFish v3
Content-Length: 0
Date: Wed, 25 Nov 2009 14:15:08 GMT

noname:glassfishv3 rwinzing$ curl -i -X GET http://localhost:8080/jerseytest/rest/customer/1/orders?state=ACTIVE
HTTP/1.1 200 OK
X-Powered-By: Servlet/3.0
Server: GlassFish v3
Content-Type: application/json
Transfer-Encoding: chunked
Date: Wed, 25 Nov 2009 14:30:01 GMT
{"order":{"eingang":"2009-11-25T00:00:00+01:00","id":"2","orderNo":"1","orderState":"ACTIVE"}}

