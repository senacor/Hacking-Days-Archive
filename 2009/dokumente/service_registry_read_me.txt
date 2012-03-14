curl -i -X POST -H "Content-Type: application/json" -d '{"name":"test","uri":"http://test.de/doit"}' http://podcast.senacor.com/ServiceRegistry/registry/services

curl -i -X DELETE http://podcast.senacor.com/ServiceRegistry/registry/services/test

http://podcast.senacor.com/ServiceRegistry