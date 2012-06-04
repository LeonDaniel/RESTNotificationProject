var mysql=require("./node-mysql");

var users_table='USERS';

var client_users = mysql.createClient({
	host: 'localhost',
	port: 3306,
	user: 'root',
	password: 'root',
	database: 'pos'
});

//client.query('CREATE TABLE USERS (user VARCHAR(255), password VARCHAR(255))');
//client.query('CREATE TABLE NOTIFRES (resource VARCHAR(255), user VARCHAR(255), connection VARCHAR(255))');



var _exports = {}


_exports.authenticate=function(username,password,_cb/*_err,authorized,{username:string}*/)
{
	console.log("Authentication successfull for user ["+username+ "] with pw ["+password+"]");
//	client_users.query('INSERT INTO USERS SET user = ?, password = ?',[username,password]);
	client_users.query('SELECT * FROM USERS where user = ?',[username],function (err,results,fields) {
		if (typeof(results)!='undefined')
		{
			
		}
	});
	_cb(null,true,{username:username, admin: true});
}

_exports.CheckUser=function(username,_cb/*_err,{username:string, admin: bool}*/)
{
  _cb(null,null);
}

_exports.GetTopicInfo = function(topic, _cb/*_err,topic*/) {
  // null or {insertDate: ...};
  _cb(null,null);
}

_exports.GetFullTopicInfo = function(topic, _cb/*_err,topic*/) {
  // null or {insertDate: ..., resources: [ 'asd', ...] };
  _cb(null,null);
}

_exports.AddTopic = function(topic, _cb/*_err*/) {
  _cb(null);
}

_exports.DeleteTopic = function(topic, _cb/*_err*/) {
  _cb(null);
}

_exports.GetResourceInfo = function(topic, resource, _cb/*_err,resource*/) {
  //null or { lastModified: ..., content: ... }
  _cb(null,null);
}

_exports.AddResource = function(topic, resource, content, _cb/*_err*/) {
  _cb(null);
}

_exports.UpdateResource = function(topic, resource, content, _cb/*_err*/) {
  _cb(null);
}

_exports.DeleteResource = function(topic, resource, content, _cb/*_err*/) {
  _cb(null);
}

_exports.GetNotificationInfo = function (topic, user, _cb/*_err, notif*/) {
  //null or {  status: ..., msg_nr: ...  }
  _cb(null,null);
}

_exports.AddNotification = function (topic, user, _cb/*_err*/) {
  _cb(null);
}

_exports.DeleteNotification = function (topic, user, _cb/*_err*/) {
  _cb(null);
}







_exports.getResourceInformation=function(objauth,resourceStr,_cb/*response*/)
{
	console.log("User ["+objauth.username+"] requesting "+resourceStr);

	_cb({error:false, ready:false});
}

_exports.createResource=function(objauth,resourceStr,_cb/*response*/)
{
	console.log("User ["+objauth.username+"] creating "+resourceStr);

	_cb({error:false});
}

_exports.deleteResource=function(objauth,resourceStr,_cb/*response*/)
{
	console.log("User ["+objauth.username+"] deleting "+resourceStr);

	_cb({error:false});
}


module.exports = _exports;
