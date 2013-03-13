/**
 * Created with IntelliJ IDEA.
 * User: pkoch
 * Date: 16.06.12
 * Time: 10:19
 * To change this template use File | Settings | File Templates.
 */
var express = require('express');

var app   = express.createServer();

app.configure(function() {
  app.use(express.static(__dirname + '/public'));
  app.use(express.bodyParser());

});

function getFollowers(user) {
  return ['vivo', 'pko'];
}

function getFollowing(user) {
  return ['bbat', 'ckp', 'vivo', 'pko'];
}

app.get('/followers/:id',function(req,res){
  console.log("Receive followers: "+req.params.id);

  res.send(getFollowers(req.params.id))
});


app.get('/following/:id',function(req,res){
  console.log("Receive following: "+req.params.id);

  res.send(getFollowing(req.params.id));
});


app.listen(process.env.VCAP_APP_PORT || 4000);

console.log('Server listening on port %s', app.address().port);
