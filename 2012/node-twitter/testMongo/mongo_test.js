var MongoBackend = require("../mongo_backend");
var backend = new MongoBackend();

console.log("START TWEETS TEST");

var tweet1 = ({
  author : "ngeorgiev",
  content : "this is a json tweet231"
});

var tweet2 = ({
  author : "spalm",
  content : "this is a json tweet231"
});

// Write test
backend.writeTweet(tweet1);
backend.writeTweet(tweet2);

// Read test

var callback = function(error, objects) {
  if (!error && objects) {
    console.log(objects);
  } else {
    console.error(error);
  }
}

var filters = ({
  authors : [ 'pko', 'asdf12313123123123' ],
  secOffset : 100000000, // tweets only from the past 10 seconds
  limit : 100
});

backend.readTweets(filters, callback);

console.log("FINISH TWEETS TEST");
console.log("START FOLLOWERS TEST");

user = "ngv123";
followers = [ 'pko', '12312431231' ]

backend.saveOrUpdateCache(user, followers);
backend.readCacheEntry(user, 100, callback);

console.log("FINISH FOLLOWERS TEST");
