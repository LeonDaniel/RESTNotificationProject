var config=require('./config.js');
var http=require('http');


function Request(method, path, user, password, data, _cb)
{

  var auth = 'Basic ' + new Buffer(user + ':' + password).toString('base64');

  var options = {
    host: config.host,
    port: config.port,
    path: path,
    method: method,
    headers:{
      "Authorization": auth,
    } 
  };

  var req = http.request(options, function(res) {
    var response_str="";
    res.on('data', function (chunk) {
      response_str += chunk;
    });
    res.on('end', function() {
      _cb(response_str);
    })
  });

  if (method!='GET')
    req.write(data);
  req.end();
}

Request('POST','/TOPIC/general/interest/topic','marius','bad pwd','',function(resp){ console.log('Creating a topic with wrong password: '+resp);
Request('POST','/TOPIC/general/interest/topic','marius','marius','',function(resp){ console.log('Creating a topic: '+resp);
Request('POST','/MESSAGE/general/interest/topic?data=1','marius','marius','firstmessage',function(resp){ console.log('Creating a message: '+resp);
Request('PUT','/MESSAGE/general/interest/topic?data=1','marius','marius','firstmessage modified',function(resp){ console.log('Modifying a message: '+resp);
Request('POST','/NOTIFICATION/general/interest/topic?user=daniel','marius','marius','',function(resp){ console.log('Subscribing daniel: '+resp);
Request('GET','/NOTIFICATION/general/interest/topic','daniel','daniel',null,function(resp){ console.log('Checking daniels notification info: '+resp);
Request('DELETE','/TOPIC/general/interest/topic','marius','marius','',function(resp){ console.log('Removing topic: '+resp);
});});});});});});});
