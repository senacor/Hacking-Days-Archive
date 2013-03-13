var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var Station = new Schema ({
  id : String,
  name : String,
  location : {
    longitude : Number,
    latitude : Number
  } 
});

mongoose.model('Station', Station);
