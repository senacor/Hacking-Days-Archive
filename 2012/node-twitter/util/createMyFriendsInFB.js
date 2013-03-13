/**
 * Created with IntelliJ IDEA.
 * User: pkoch
 * Date: 16.06.12
 * Time: 22:04
 * To change this template use File | Settings | File Templates.
 */

itseMee = {firstname:"Philipp", lastname:"Koch", username:"pkoch"}

var http = require(('http'));

friends = ["mbrunner","spalm","akeefer","hstamminger","rwinzinger","vvolle","ngeorgiev","mwimmer","ctk","ogalimov"]

for (var i = 0; i < friends.length; i++) {

  postheader = {
    'Content-Type':'application/json',
    'Content-Length':0
  };

  var optsFollowers = {
    host:'hd12.network.cloudfoundry.com',
    port:80,
    path:'/rest/person/' + itseMee.username + '/peers/' + friends[i],
    method:'PUT',
    headers: postheader
  }



  console.log("Create My Friends:", optsFollowers);

  var reqPut = http.request(optsFollowers, function (res) {

    console.log("\nstatusCode: ", res.statusCode);

    res.on('data', function (data) {
      console.info('POST result:\n');
      console.log(data);
      console.info('\n\nPOST completed');
    });

    res.on('end', function () {

    })
  });

  reqPut.end();
  reqPut.on('error', function (e) {
    console.error(e);
  });

}
