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

  if (typeof(data) == 'object')
  {
    options.headers['Content-type']='application/json';
    data=JSON.stringify(data);
  }

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

Request('POST','/TOPIC/general/interest/topic','daniel','bad pwd',{topicName: "just a name" },function(resp){ console.log('Creating a topic with wrong password: '+resp);
Request('POST','/TOPIC/general/interest/topic','daniel','daniel',{topicName: "just a name" },function(resp){ console.log('Creating a topic: '+resp);
Request('GET','/TOPIC/general/interest/topic','daniel','daniel',null,function(resp){ console.log('Reading a topic: '+resp);
Request('PUT','/TOPIC/general/interest/topic','daniel','daniel',{topicName: "just a fancy name" },function(resp){ console.log('Updating a topic: '+resp);
Request('GET','/TOPIC/general/interest/topic','daniel','daniel',null,function(resp){ console.log('Reading an updated topic: '+resp);
Request('POST','/MESSAGE/general/interest/topic?data=1','daniel','daniel','firstmessage',function(resp){ console.log('Creating a message: '+resp);
Request('GET','/MESSAGE/general/interest/topic?data=1','daniel','daniel',null,function(resp){ console.log('Reading a message: '+resp);
Request('PUT','/MESSAGE/general/interest/topic?data=1','daniel','daniel','firstmessage modified',function(resp){ console.log('Modifying a message: '+resp);
Request('GET','/MESSAGE/general/interest/topic?data=1','daniel','daniel',null,function(resp){ console.log('Reading a modified message: '+resp);
Request('GET','/TOPIC/general/interest/topic','daniel','daniel',null,function(resp){ console.log('Reading topic with message: '+resp);
Request('POST','/NOTIFICATION/general/interest/topic?user=paul','daniel','daniel','',function(resp){ console.log('Subscribing paul: '+resp);
Request('GET','/NOTIFICATION/general/interest/topic','paul','paul',null,function(resp){ console.log('Checking pauls notification info: '+resp);
Request('PUT','/NOTIFICATION/general/interest/topic?user=paul&nr_msg=10','daniel','daniel','',function(resp){ console.log('Modifying pauls notification: '+resp);
Request('DELETE','/NOTIFICATION/general/interest/topic','paul','paul',null,function(resp){ console.log('Deleting pauls notification: '+resp);
Request('GET','/NOTIFICATION/general/interest/topic','paul','paul',null,function(resp){ console.log('Checking pauls notification info: '+resp);
Request('DELETE','/TOPIC/general/interest/topic','daniel','daniel','',function(resp){ console.log('Removing topic: '+resp);
});});});});});});});});});});});});});});});});
