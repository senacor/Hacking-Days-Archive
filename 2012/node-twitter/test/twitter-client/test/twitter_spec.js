var mocha = require('mocha');
var should = require('should');
var io = require('socket.io-client');
var express = require('express');
var querystring = require('querystring');

var http = require("http");

var userName = 'vivo';

var post_data = querystring.stringify({
  'user' : userName
});

var optionsBase = {
  host : 'localhost',
  port : 3000,
  path : '/',
  method : 'GET',
};

var optionsLogin = {
  host : optionsBase.host,
  port : optionsBase.port,
  path : '/login',
  method : 'POST',
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded',
    'Content-Length': post_data.length
  }
};

var reqInit = http.get(optionsLogin,function(res){

  res.on('end',function(){
    console.log("End of Init");

    optionsLogin.headers['cookie'] = res.headers['set-cookie'];

    console.log(optionsLogin.headers['cookie']);

    var reqLogin = http.get(optionsLogin,function(res){

      res.on('end',function(){
        console.log("End of Login");

        // optionsLogin.headers['cookie'] = res.headers['set-cookie'];
        console.log(res.headers);

        var socketURL = 'http://' + optionsLogin.host + ":" + optionsLogin.port;

        var optionsSocket ={
          'cookie': optionsLogin.headers['cookie']
        };


        var socket = io.connect(socketURL, optionsSocket);
        // console.log(socket);


        socket.on('connect', function () {
          console.log(socket);
          socket.emit('newTweet', {author : userName, timestamp : new Date(), content : "from test client"});
        });

      })

    });

    reqLogin.end();

    reqLogin.on('error',function(error){
      console.log(error);
    });
  })

});

reqInit.end();

reqInit.on('error',function(error){
  console.log(error);
});


var app   = express.createServer();
app.listen(5000);
console.log('Server listening on port %s', app.address().port);


//describe("Twitter Server",function(){
//  it('Should do something', function(done){
//    var socket = io.connect(socketURL, options);
//
//    socket.emit('newTweet', {author : user, timestamp : new Date(), content : "from test client"});
//
//  });
//});



