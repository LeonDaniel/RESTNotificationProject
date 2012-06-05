var fs=require('fs');
var url_utils=require('url');
var querystring=require('querystring');
var connect=require('./connect');
var config=require('./config.js');
var http=require('http');
var W8ER=require('./w8.js');
var dblayer= require('./dblayer');

var serverhttp;
if (config.https.use)
{
	serverhttp = connect.createServer(
	{ 
		key: fs.readFileSync(config.https.key), 
		cert: fs.readFileSync(config.https.cert) 
	});
}
else
	serverhttp = connect.createServer();

serverhttp.use(connect.bodyParser());
serverhttp.use(
	connect.basicAuth(function(user, pass,fn){
		dblayer.authenticate(user,pass,function(_err,auth,objauth){
			if (_err)
				fn(_err,null);
			else if (!auth)
				fn(null,null);
			else
				fn(null,objauth);
		});
	})
);
serverhttp.use(connect.router( function( app ) {
	app.all( '/:service/*' , function( _req , _res ) {
		//extracting the parameters as an array
		var resource_url_params_arr=
			(function(json){ var arr=[]; for (var key in json) arr.push({k:key, o:json[key]}); return arr; })
			(querystring.parse(url_utils.parse(_req.url).query));
		//sorting the array
		resource_url_params_arr.sort(function(a,b){return ((a.k==b.k)?0:((a.k>b.k)?1:-1));});
		//regenerating the parameters in url format
		var resource_url_params_sorted=
			(function(arr) {var obj={}; for (var i=0;i<arr.length;i++) obj[arr[i].k]=arr[i].o;
				return querystring.stringify(obj); })
			(resource_url_params_arr);
		//generating the resource identifying string
		//var fullResourceStr='/'+resource_path+((resource_url_params_sorted.length==0)?'':'?'+resource_url_params_sorted);
	
		//extracting other necessary information
		var service = _req.params.service;
		var topic = '/'+_req.params[0];
		var resourceName = resource_url_params_sorted;
		var resourceContent = _req.body;
		var userInfo = _req.remoteUser;
		var method=_req.method;

		ProcessQuery(service,method,topic,resourceName,resourceContent,userInfo,function(result){
console.log(JSON.stringify(result));
			_res.writeHead(200, {'Content-type':'application/json'});
			_res.end(JSON.stringify(result));
		});
	});
}));

function ProcessQuery(service, method, topic, resourceName, resourceContent, userInfo, _cb) {
  switch (service) {
    case 'TOPIC':
      ProcessTopic(method, topic, resourceName, resourceContent, userInfo, _cb)
      break;
    case 'MESSAGE':
      ProcessMessage(method, topic, resourceName, resourceContent, userInfo, _cb)
      break;
    case 'NOTIFICATION':
      ProcessNotification(method, topic, resourceName, resourceContent, userInfo, _cb)
      break;
    default:
      _cb({ error: true, error_string: 'Unknown service' });
      break;
  }
}

function ProcessTopic(method, topic, resourceName, resourceContent, userInfo, _cb) { 
  if (typeof(resourceContent)=='string') {
    try {
      resourceContent = JSON.parse(resourceContent);
    }catch (ex){ console.log(ex.message);}
  }
  switch (method) {
    case 'POST':   //C
      if ( typeof(resourceContent)!='object' || !('topicName' in resourceContent))
        { _cb({ error: true, error_string: 'No or malformed topic name' }); return; }
      if (!IsAdmin(userInfo)) { _cb({ error: true, error_string: 'No rights' }); return; }
      dblayer.GetTopicInfo(topic,function (_err,topicInfo) {
        if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
        if (topicInfo!=null) { _cb({ error: true, error_string: 'Existing topic' }); return; }
        dblayer.AddTopic(topic, resourceContent.topicName, function(_err) {
          if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
          _cb({ error:false }); return;
        });
      });
      break;
    case 'GET':    //R
      dblayer.GetFullTopicInfo(topic,function (_err,topicInfo) {
        if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
        if (topicInfo==null) { _cb({ error: true, error_string: 'Topic does not exist' }); return; }
	_cb({ error:false, insertDate: topicInfo.insertDate, topicName: topicInfo.topicName, resources: topicInfo.resources}); return;
      });
      break;
    case 'PUT':    //U
       if ( typeof(resourceContent)!='object' || !('topicName' in resourceContent))
        { _cb({ error: true, error_string: 'No or malformed topic name' }); return; }
      if (!IsAdmin(userInfo)) { _cb({ error: true, error_string: 'No rights' }); return; }
      dblayer.GetTopicInfo(topic,function (_err,topicInfo) {
        if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
        if (topicInfo==null) { _cb({ error: true, error_string: 'Topic does not exist' }); return; }
        dblayer.UpdateTopic(topic, resourceContent.topicName, function(_err) {
          if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
          _cb({ error:false }); return;
        });
      });
      break;
    case 'DELETE': //D
      if (!IsAdmin(userInfo)) { _cb({ error: true, error_string: 'No rights' }); return; }
      dblayer.GetTopicInfo(topic,function (_err,topicInfo) {
        if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
        if (topicInfo==null) { _cb({ error: true, error_string: 'Topic does not exist' }); return; }
        dblayer.DeleteTopic(topic, function(_err) {
          if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
          _cb({ error:false }); return;
        });
      });
      break;
  }
}

function ProcessMessage(method, topic, resourceName, resourceContent, userInfo, _cb) {
  switch (method) {
    case 'POST':   //C
      if (!IsAdmin(userInfo)) { _cb({ error: true, error_string: 'No rights' }); return; }
      dblayer.GetTopicInfo(topic,function (_err,topicInfo) {
        if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
        if (topicInfo==null) { _cb({ error: true, error_string: 'Topic does not exist' }); return; }
        dblayer.GetResourceInfo(topic, resourceName, function (_err, resource) {
          if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
          if (resource != null) { _cb({ error: true, error_string: 'Existing resource' }); return; }
          dblayer.AddResource(topic, resourceName, resourceContent, function (_err) {

            dblayer.GetResourceInfo(topic, resourceName, function (_err, resource) {
              CallEvent('onMessage','idMessage='+resource.id);
            });

            if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
            _cb({ error:false }); return;
          });
        });
      });
      break;
    case 'GET':    //R
      dblayer.GetTopicInfo(topic,function (_err,topicInfo) {
        if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
        if (topicInfo==null) { _cb({ error: true, error_string: 'Topic does not exist' }); return; }
        dblayer.GetResourceInfo(topic, resourceName, function (_err, resource) {
          if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
          if (resource == null) { _cb({ error: true, error_string: 'Resource does not exist' }); return; }
          _cb({ error:false, lastModified: resource.lastModified, content: resource.content }); return;
        });
      });
      break;
    case 'PUT':    //U
      if (!IsAdmin(userInfo)) { _cb({ error: true, error_string: 'No rights' }); return; }
      dblayer.GetTopicInfo(topic,function (_err,topicInfo) {
        if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
        if (topicInfo==null) { _cb({ error: true, error_string: 'Topic does not exist' }); return; }
        dblayer.GetResourceInfo(topic, resourceName, function (_err, resource) {
          if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
          if (resource == null) { _cb({ error: true, error_string: 'Resource does not exist' }); return; }
          dblayer.UpdateResource(topic, resourceName, resourceContent, function (_err) {
            if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
            _cb({ error:false }); return;
          });
        });
      });
      break;
    case 'DELETE': //D
      if (!IsAdmin(userInfo)) { _cb({ error: true, error_string: 'No rights' }); return; }
      dblayer.GetTopicInfo(topic,function (_err,topicInfo) {
        if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
        if (topicInfo==null) { _cb({ error: true, error_string: 'Topic does not exist' }); return; }
        dblayer.GetResourceInfo(topic, resourceName, function (_err, resource) {
          if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
          if (resource == null) { _cb({ error: true, error_string: 'Resource does not exist' }); return; }
          dblayer.DeleteResource(topic, resourceName, resourceContent, function (_err) {
            if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
            _cb({ error:false }); return;
          });
        });
      });
      break;
  }
}

function ProcessNotification(method, topic, resourceName, resourceContent, userInfo, _cb) {
  var meta = querystring.parse(resourceName);
  var notificationUser=userInfo.username;
  if ('user' in meta) notificationUser=meta.user;
  var messagesNumber=5;
  if ('nr_msg' in meta) messagesNumber=meta.nr_msg;
  switch (method) {
    case 'POST':   //C
      if (!IsAdmin(userInfo)) { _cb({ error: true, error_string: 'No rights' }); return; }
      dblayer.GetTopicInfo(topic,function (_err,topicInfo) {
        if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
        if (topicInfo==null) { _cb({ error: true, error_string: 'Topic does not exist' }); return; }
        dblayer.CheckUser(notificationUser,function(_err,usrInf){
          if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
          if (usrInf==null) { _cb({ error: true, error_string: 'User does not exist' }); return; }
          dblayer.GetNotificationInfo(topic, notificationUser, function (_err,notif) {
            if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
            if (notif!=null) { _cb({ error: true, error_string: 'User is already subscribed' }); return; }
            dblayer.AddNotification(topic, notificationUser, messagesNumber, function (_err) {
              if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
              CallEvent('onCreateNotification');
              _cb({ error:false}); return;
            });
          });
        });  
      });
      break;
    case 'GET':    //R
      if ( notificationUser!=userInfo.username && !IsAdmin(userInfo)) { _cb({ error: true, error_string: 'No rights' }); return; }
      dblayer.GetTopicInfo(topic,function (_err,topicInfo) {
        if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
        if (topicInfo==null) { _cb({ error: true, error_string: 'Topic does not exist' }); return; }
        dblayer.CheckUser(notificationUser,function(_err,usrInf){
          if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
          if (usrInf==null) { _cb({ error: true, error_string: 'User does not exist' }); return; }
          dblayer.GetNotificationInfo(topic, notificationUser, function (_err,notif) {
            if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
            if (notif==null) { _cb({ error: true, error_string: 'User is not subscribed' }); return; }
            _cb({ error:false, msg_nr: notif.msg_nr, status: notif.status }); return;
          });
        });
      });
      break;
    case 'PUT':    //U
      if (!IsAdmin(userInfo)) { _cb({ error: true, error_string: 'No rights' }); return; }
      dblayer.GetTopicInfo(topic,function (_err,topicInfo) {
        if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
        if (topicInfo==null) { _cb({ error: true, error_string: 'Topic does not exist' }); return; }
        dblayer.CheckUser(notificationUser,function(_err,usrInf){
          if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
          if (usrInf==null) { _cb({ error: true, error_string: 'User does not exist' }); return; }
          dblayer.GetNotificationInfo(topic, notificationUser, function (_err,notif) {
            if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
            if (notif==null) { _cb({ error: true, error_string: 'User is not subscribed' }); return; }
            dblayer.UpdateNotification(topic, notificationUser, messagesNumber, function (_err) {
              if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
              _cb({ error:false}); return;
            });
          });
        });  
      });
      break;
    case 'DELETE': //D
      if (!IsAdmin(userInfo)) { _cb({ error: true, error_string: 'No rights' }); return; }
      dblayer.GetTopicInfo(topic,function (_err,topicInfo) {
        if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
        if (topicInfo==null) { _cb({ error: true, error_string: 'Topic does not exist' }); return; }
        dblayer.CheckUser(notificationUser,function(_err,usrInf){
          if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
          if (usrInf==null) { _cb({ error: true, error_string: 'User does not exist' }); return; }
          dblayer.GetNotificationInfo(topic, notificationUser, function (_err,notif) {
            if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
            if (notif==null) { _cb({ error: true, error_string: 'User is not subscribed' }); return; }
            dblayer.DeleteNotification(topic, notificationUser, function (_err) {
              if (_err) { _cb({ error: true, error_string: 'DB Error: '+_err }); return; }
              _cb({ error:false}); return;
            });
          });
        });  
      });
      break;
  }
}

function IsAdmin(userInfo) {
  return ('admin' in userInfo && userInfo.admin);
}

function CallEvent(evName,params) {

  if (!config.events[evName].active)
    return;
  var sendPath=config.events[evName].path;
  if (params) sendPath+='?'+params;
  var options = {
    host: config.events[evName].host,
    port: config.events[evName].port,
    path: config.events[evName].path,
    method: 'GET',
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
  //req.write(data);
  req.end();
}

serverhttp.listen( config.port , config.host ); 
console.log('Started server on '+config.host+':'+config.port);
