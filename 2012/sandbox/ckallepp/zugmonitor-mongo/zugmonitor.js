var fs    = require('fs'); 
var http  = require('http');

var client = http.createClient(80, 'zugmonitor.sueddeutsche.de');
var request = client.request('GET', '/api/stations');

request.on('response', function(response) {
  console.log('http status: ' +  response.statusCode);
  var message = ''

  response.on('data', function(chunk) {
    message += chunk
  });

  response.on('end', function() {
    console.log('end of response from server...');
    var data = JSON.parse(message);
    
    var buffer = [];
    //buffer.push('{ "stations": [');
    for (var i in data) {
      if (isNaN(i))
        continue;
      
      buffer.push('{');
      var station = [];
      station.push('"id":"' + data[i]['id'] + '",');
      station.push('"name":"' + data[i]['name'] + '",');
      station.push('"location":[' + data[i]['lon'] + ', ' + data[i]['lat'] + ']'); 
      //station.push('"latitude":"' + data[i]['lat'] + '",');
      //station.push('"longitude":"' + data[i]['lon'] + '"');
      buffer.push(station.join(''));
      buffer.push('}');
      //buffer.push(',');
      buffer.push('\n');
    }
    //buffer[buffer.length-1] = '';
    //buffer.push(']}');
    var stations = buffer.join('');
    fs.writeFile('./stations.json', stations, function(err) {
      if (err) {
        console.log('An error while saving the file with the train stations!');
      } else {
        console.log('File with train stations saved!');
      }
    });
  });
});

request.end();
