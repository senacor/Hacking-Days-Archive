/**
 * Created with IntelliJ IDEA.
 * User: pkoch
 * Date: 15.06.12
 * Time: 15:36
 * To change this template use File | Settings | File Templates.
 */
var MongoBackend = require("./mongo_backend");
var http = require("http");
var backend = new MongoBackend();

var FOLLOWERS_CACHE_SUFFIX = "$$FOLLOWERS";
var FOLLOWINGS_CACHE_SUFFIX = "$$FOLLOWINGS";
var MAX_TIME_OUT = 5 * 60; // followers/followings cache must not be older than 5 minutes 

function getFollowers(user,callback) {
  
  backend.readCacheEntry(user+FOLLOWERS_CACHE_SUFFIX, MAX_TIME_OUT, function(error, dbFollowers) {
      if (!error && dbFollowers) {
        callback(dbFollowers.value);
      } else {
        // read the followers from the service and store them in the db
        var optsFollowers = {
            host : 'hd12.network.cloudfoundry.com',
            port : 80,
            path : '/rest/person/'+user+'/followers',
            method : 'GET' //
          };

          console.log("Start receiving followers for: " + user);

          var reqGet = http.get(optsFollowers,function(res){

            tempData = ''

            res.on('data',function(data){
              console.log("Data received: "+data);
              tempData += data;
            });

            res.on('end',function(){
              console.log("Ende of Request");
              
              foundUser = JSON.parse(tempData);
              
              //update backend
              backend.saveOrUpdateCache(user+FOLLOWERS_CACHE_SUFFIX, foundUser);
              
              console.log("found " + foundUser);
              callback(foundUser);
            })

          });

          reqGet.end();

          reqGet.on('error',function(error){
            console.log(error);
          });
      }
  });
}

function getFollowing(user,callback) {
  backend.readCacheEntry(user+FOLLOWINGS_CACHE_SUFFIX, MAX_TIME_OUT, function(error, dbFollowings) {
    if (!error && dbFollowings) {
      callback(dbFollowings.value);
    } else {
      var optsFollowing = {
        host : 'hd12.network.cloudfoundry.com',
        port : 80,
        path : '/rest/person/'+user+'/follows',
        method : 'GET' //
      };
    
      console.log("Start receiving Followings for: "+user);
    
      var reqGet = http.get(optsFollowing,function(res){
    
        tempData = ''
    
        res.on('data',function(data){
          console.log("Data received: "+data);
          tempData += data;
        });
    
        res.on('end',function(){
          console.log("Ende of Request");
    
          foundUser = JSON.parse(tempData);
    
          //update backend
          backend.saveOrUpdateCache(user+FOLLOWINGS_CACHE_SUFFIX, foundUser);
          
          console.log("found " + foundUser);
          callback(foundUser);
        })
      });
    
      reqGet.end();
    
      reqGet.on('error',function(error){
        console.log(error);
      });
    }
  });
}

exports.joinRoom = function(socket, userAsRoom) {
  socket.join(userAsRoom);
}

exports.leaveRoom = function(socket, userAsRoom) {
  socket.leave(userAsRoom);
}

function escapeHtml(htmlString) {
	return htmlString.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
}

function escapeTweet(tweet) {
	if (tweet) {
		if (tweet.content) {
			tweet.content = escapeHtml(tweet.content);
		}
    	if (tweet.author) {
    		tweet.author = escapeHtml(tweet.author);
    	}
    }
}

function sendTweetInternal(sockets, tweet) {
  console.log("from: " + tweet);

  escapeTweet(tweet);

  // write the tweet to the backend
  backend.writeTweet(tweet);

  // socket.broadcast.to(room).emit('new fan');
  getFollowers(tweet.author, function(followers){

    followers.push(tweet.author);
    // var newTweet = createTweet(from, msg);
    for (var i = 0; i < followers.length; i++) {
      sockets.in(followers[i]).emit('tweet', tweet);
    }
  });

}

exports.sendTweet = sendTweetInternal


function tweetHandler(sockets){
  return function(tweet) {
    sendTweetInternal(sockets, tweet);
  }
}


function publishOldTweetsFromDB(user,sockets){

  console.log("Publish old tweets for user: "+user)

  getFollowing(user,function(followings){

    console.log("Lookup old tweet from authors: "+followings);

    followings.push(user);

    var filter = {
      authors: followings,
      secOffset: 60*60*60*12, // die letzten 12 stunden
      limit:200 };

    backend.readTweets(filter,function (error, tweets) {
      if (!error) {
        console.log("oldTweets: "+tweets );

        for (var i = 0; i < tweets.length; i++) {
          sockets.in(user).emit('tweet', tweets[i]);
        }
      } else {
        console.error(error);
      }
    })

  });

}

exports.initTweetServer = function (socket, sockets, user) {


  publishOldTweetsFromDB(user,sockets);

  socket.on('newTweet', tweetHandler(sockets));

  getFollowers(user,function(followers){
    console.log('Received Followers for display: '+followers)
    socket.emit("follower",followers);
  })

  getFollowing(user,function(following){
    console.log('Received following for display: '+following)
    socket.emit("following",following);
  })

}

exports.tweetsReport = function(callback) {
  backend.countTweetsForUsersWithMapReduce(callback);
}