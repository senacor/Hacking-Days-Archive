var mongoose = require('mongoose');
require('./station_model');

var dbUrl = 'mongodb://localhost/db';
var db = mongoose.connect(dbUrl);
var station = mongoose.model('Station');
var s = new station();

s.id = 1;
s.name = 'Musterstadt';
s.location.longitude = 12;
s.location.latitude = 32;

s.save(function(err) {
  if (!err) {
    console.log('save station ' + s.name);
    
    // find station by name
    station.find({name: 'Musterstadt'}, function(err, s1) {
      if (!err) {
        console.log(JSON.stringify(s1));
      } else {
        console.log('error retrieving station');
        console.log(err);
      }
    });
  } else {
    console.log('error while saving ' + s.name);
    console.log(err);
  }
});

var findStations = station.find({name: 'Musterstadt'}, function(err, s1) {
  if (!err) {
    console.log(JSON.stringify(s1));
  } else {
    console.log('error retrieving station');
    console.log(err);
  }
});
