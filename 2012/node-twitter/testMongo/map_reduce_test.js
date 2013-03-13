var MongoBackend = require("../mongo_backend");
var backend = new MongoBackend();

console.log("TESTING MAP AND REDUCE");

var callback = function(error, collection, status) {
  console.log(status); // status is optional, you can provide a function with only 2 args
  if (error) {
    console.log(error);
  } else {
    console.log(collection);
  }
}

backend.countTweetsForUsersWithMapReduce(callback);

// should print something like this:

//[ { _id: 'mbrunner', value: { tweet_count: 47, tweet_size: 1081, img_count: 0, img_size: 0 } },
//  { _id: 'ngeorgiev', value: { tweet_count: 4, tweet_size: 112, img_count: 1, img_size: 16538 } },
//  { _id: 'spalm', value: { tweet_count: 3, tweet_size: 50, img_count: 0, img_size: 0 } } ]

// thus we can see the total count of tweets, total size of the tweets, etc. per user
