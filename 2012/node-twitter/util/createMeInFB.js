/**
 * Created with IntelliJ IDEA.
 * User: pkoch
 * Date: 16.06.12
 * Time: 22:04
 * To change this template use File | Settings | File Templates.
 */

itseMee = {firstname:"Philipp", lastname:"Koch", username:"pkoch"}

itseMeeString = JSON.stringify(itseMee);

var http = require(('http'));


postheader = {
  'Content-Type':'application/json',
  'Content-Length':Buffer.byteLength(itseMeeString, 'utf8')
};

optsFollowers = {
  host:'hd12.network.cloudfoundry.com',
  port:80,
  path:'/rest/person/' + itseMee.username,
  method:'PUT',
  headers: postheader
  //
};

console.info('Options prepared:');
console.info(postheader);
console.info(itseMeeString);
console.info('Do the POST call');


var optsItseMee = {
  host:'hd12.network.cloudfoundry.com',
  port:80,
  path:'/rest/person/' + itseMee.username,
  method:'PUT',
  headers:postheader
  //
};

console.log("Create Me:", optsItseMee);

var reqPut = http.request(optsItseMee, function (res) {

  console.log("statusCode: ", res.statusCode);

  res.on('data', function (data) {
    console.info('POST result:\n');
    console.log(data);
    console.info('\n\nPOST completed');
  });

  res.on('end', function () {
    console.info('POST end:\n');
  })
});

// write the json data
reqPut.write(itseMeeString);
reqPut.end();
reqPut.on('error', function (e) {
  console.error(e);
});

