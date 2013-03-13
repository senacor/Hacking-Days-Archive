var http = require('http');

http.createServer(function(req, res) {
    res.writeHead(200, {'Content-Type': 'text/html'});
    res.write('<table>');
    for (var i = 0; i < mock['tweets'].length; i++) {
        res.write('<tr><td>' + mock.tweets[i].user + '</td>' +
        '<td>' + mock.tweets[i].msg + '</td></tr>');
    }
    res.write('</table>');
    res.end();
}).listen(8181, '127.0.0.1');

var mock = {
    'tweets' : [
        {
            'user' : 'tweet-user1',
            'msg'  : 'This is a tweet from user1'
        },
        {
            'user' : 'user2',
            'msg'  : 'Another message; this time from user2'
        }
    ]
}

console.log('Server listening on http://127.0.0.1:8181 ...');