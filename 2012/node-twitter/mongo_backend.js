var mongoose = require('mongoose'),
    mongo;

if (process.env.VCAP_SERVICES) {
    var env = JSON.parse(process.env.VCAP_SERVICES);
    mongo = env['mongodb-1.8'][0]['credentials'];
} else {
    mongo = { 
        "hostname" : "localhost", 
        "port" : "27017", 
        "username" : "", 
        "password" : "", 
        "name" : "", 
        "db" : "db"
    }
}

console.log("mongo configuration: " + mongo);

var generateMongoUrl = function() {
    var url;
    if (mongo.username && mongo.password) {
        url = "mongodb://" + mongo.username + ":" + mongo.password + "@" + mongo.hostname + ":" + mongo.port + "/" + mongo.db;
    } else {
        url = "mongodb://" + mongo.hostname + ":" + mongo.port + "/" + mongo.db;
    }
    console.log("Mongodb URL: " + url);
    return url;
};

var db = mongoose.connect(generateMongoUrl(mongo) ,function(err) {
    if (err) {
    	console.log("MongoDB database error!");
    	throw err;
    }
});
var Schema = mongoose.Schema;

TweetSchema = new Schema({
	'author': {type: String, index: true },
	'timestamp':{type:Date, default:Date.now},
	'content': String
});

CacheSchema = new Schema({
	'key': {type: String, unique: true, index: true},
	'value': {type: {}},
	'timestamp': {type: Date, default:Date.now}
});


mongoose.model('Tweet', TweetSchema);
mongoose.model('Cache', CacheSchema);

var MongoBackend = function() {
	
	this.writeTweet = function(tweet) {
		var Tweet = db.model('Tweet');
		var dbTweet = new Tweet(tweet);
		dbTweet.save(function (err) {
		  if (err) {
		    console.log(err)
		  }
		});
	}
	
	this.readTweets = function(filters, callback) {
		var Tweet = db.model('Tweet');
		var where = {};
		
		if (filters && filters.authors){
			where.author = {$in: filters.authors};
		} else {
			return {} // no followers no tweets :)
		}
		
		if (filters && filters.secOffset){
			var end = new Date();
			var start = new Date(end.getTime() - filters.secOffset * 1000); //minutes to milliseconds
			where.timestamp = {$gte: start, $lte: end}
		}
		
		console.log("Where statement:" + JSON.stringify(where));
		
		var query = Tweet.find(where);
		if (filters.limit) {
			 query.limit(filters.limit);
		}
		
		query.sort('timestamp',1)
		query.exec(callback);
	}
	
	this.readCacheEntry = function(key_, secOffset, callback) {
		var Cache = db.model('Cache');
		if (!key_) {
			return {}
		}
		var end = new Date();
		var start = new Date(end.getTime() - secOffset * 1000); //minutes to milliseconds
		return Cache.findOne({key: key_, timestamp: {$gte: start, $lte: end}}, callback);
	}
	
	this.saveOrUpdateCache = function(key_, val) {
		var Cache = db.model('Cache');
		if (key_ && val) {
			Cache.update({key: key_}, {$set: {value: val, timestamp: Date()}}, {upsert: true}, function(err) {
			    if (err) {
			    	console.log(err);
			    }
			});
		}
	}
	
	
	this.countTweetsForUsersWithMapReduce = function(callback) {
	  var Tweet = db.model('Tweet');
	  
  	var mapfunc = function() { 
  	  var lengthInUtf8Bytes = function (str) {
  	    if (str) {
  	      // Matches only the 10.. bytes that are non-initial characters in a multi-byte sequence.
          var m = encodeURIComponent(str).match(/%[89ABab]/g);
          return str.length + (m ? m.length : 0);
  	    } else {
  	      return 0;
  	    }
      }
  	  emit(this.author, {
  	    tweet_count: 1, 
  	    tweet_size: lengthInUtf8Bytes(this.content), 
  	    img_count: (this.image ? 1 : 0), // the image is not always present 
  	    img_size: (this.image ? this.image.length : 0)
  	  }); 
  	};
  	
  	var reducefunc = function(key, values) { 
  	  var result = {tweet_count: 0, tweet_size: 0, img_count: 0, img_size: 0};
      values.forEach(function(value) {
        result.tweet_count += value.tweet_count;
        result.tweet_size += value.tweet_size;
        result.img_count += value.img_count;
        result.img_size += value.img_size;
      });
      return result;
  	};
	  
  	Tweet.collection.mapReduce(
  	  mapfunc.toString() ,
  	  reducefunc.toString() ,
	    {out: { inline: 1 }},
	    callback
	  );
	}
}

module.exports = MongoBackend;
