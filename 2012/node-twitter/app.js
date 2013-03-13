var express = require('express');

var tweetHandler  = require('./server');

var app   = express.createServer();
var io    = require('socket.io').listen(app);
var store = new express.session.MemoryStore;

// Cloudfoundry does not support websockets... fallback transports has to be defined!
if (process.env.VCAP_APP_PORT) {
  console.log('Cloud Foundry. No WebSockets');
  io.set('transports', ['flashsocket', 'htmlfile', 'xhr-polling', 'jsonp-polling']);
} else {
  console.log('WebSockets. Yihaa!');
}


app.configure(function() {
  app.use(express.static(__dirname + '/public'));
  app.use(express.bodyParser());
  app.use(express.cookieParser());
  // secret is used to compute the hash
  app.use(express.session({
        secret: "hd2012secret",
        store: store,
        key: 'hd2012client.sid'
      }
  ));
});

app.post('/login', function(req, res) {
  console.log(req.body);
  if (req.body) {
    req.session.user = req.body.username;
  }
  res.sendfile(__dirname + '/public/main.html');
});

app.get('/logout', function(req, res) {
  req.session.destroy(function(err) {
    console.log('Error destroying session: ' + err);
    return;
  });
  res.sendfile(__dirname + '/public/index.html');
});


app.get('/restTweetWithGet',function(req,res){
  tweetHandler.sendTweet(io.sockets, { 'author' : req.param('author'), 'content': req.param('content'), 'timestamp': new Date() });
  res.send(req.param('content', 'default message (/writer)') + "<br/>");
} );


app.get('/restTweetReport', function(req, res) {
  tweetHandler.tweetsReport(function(error, collection, status) {
    console.log(status); // status is optional, you can provide a function with only 2 args
    if (error) {
      console.log(error);
      res.send(error);
    } else {
      console.log(JSON.stringify(collection));
      res.send(collection);
    }
  });
});


var parseCookie = require('connect').utils.parseCookie;
io.set('authorization', function (data, accept) {
  if (data.headers.cookie) {
    data.cookie = parseCookie(data.headers.cookie);
    data.sessionID = data.cookie['hd2012client.sid'];
    // (literally) get the session data from the session store
    store.get(data.sessionID, function (err, session) {
      if (err || !session) {
        // if we cannot grab a session, turn down the connection
        accept('Error', false);
      } else {
        // save the session data and accept the connection
        data.session = session;
        accept(null, true);
      }
    });
  } else {
    return accept('No cookie transmitted.', false);
  }
});

io.sockets.on('connection', function(socket) {
  console.log("Client connected...");

  // get session from socket.io handshakee
  var session = socket.handshake.session;

  socket.emit('user', { user: session.user });

  tweetHandler.joinRoom(socket, session.user);

  tweetHandler.initTweetServer(socket, io.sockets, session.user);

  socket.on('disconnect', function () {
    console.log('Client disconnected...');
    tweetHandler.leaveRoom(socket, session.user);
  });
});

app.listen(process.env.VCAP_APP_PORT || 3000);
console.log('Server listening on port %s', app.address().port);
