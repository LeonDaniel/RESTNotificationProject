var mysql=require("./node-mysql");


var sql_client = mysql.createClient({
	host: 'localhost',
	port: 3306,
	user: 'root',
	password: '',
	schema: 'messages'
});

//client.query('CREATE TABLE USERS (user VARCHAR(255), password VARCHAR(255))');
//client.query('CREATE TABLE NOTIFRES (resource VARCHAR(255), user VARCHAR(255), connection VARCHAR(255))');
//	client_users.query('INSERT INTO USERS SET user = ?, password = ?',[username,password]);

var _exports = {}


_exports.authenticate=function(username,password,_cb/*_err,authorized,{username:string, admin:bool}*/)
{
  sql_client.query('SELECT * FROM messages.USERS where USERNAME = ? AND PASSWORD = ?',[username, password],function (err,results,fields) {
    if (err) { _cb(err); return; }
    if (typeof(results)!='undefined' && results.length!=0) {
      _cb(null,true,{username:username, admin: true});
      console.log("Authentication successfull for user ["+username+ "] with pw ["+password+"]");
    }
    else {
      _cb(null,false);
    }
  });
}

_exports.CheckUser=function(username,_cb/*_err,{username:string, admin: bool}*/)
{
  sql_client.query('SELECT * FROM messages.USERS where USERNAME = ?',[username],function (err,results,fields) {
    if (err) { _cb(err); return; }
    if (typeof(results)!='undefined' && results.length!=0) {
      _cb(null,{username:results[0].USERNAME, admin: true});
    }
    else {
      _cb(null,null);
    }
  });
}

_exports.GetTopicInfo = function(topic, _cb/*_err,topic*/) {
  // null or {insertDate: ...};
  sql_client.query('SELECT * FROM messages.TOPICS where DENUMIRE = ?',[topic],function (err,results,fields) {
    if (err) { _cb(err); return; }
    if (typeof(results)!='undefined' && results.length!=0) {
      _cb(null,{insertDate:results[0].INSERTDATE});
    }
    else {
      _cb(null,null);
    }
  });
}

_exports.GetFullTopicInfo = function(topic, _cb/*_err,topic*/) {
  // null or {insertDate: ..., resources: [ 'asd', ...] };
  _cb(null,null);
}

_exports.AddTopic = function(topic, _cb/*_err*/) {
  sql_client.query('INSERT INTO messages.TOPICS SET INSERTDATE=NOW(), DENUMIRE = ?',[topic], function(err, result) {
    if (err) 
      _cb(err);
    else
      _cb(null);
  });
}

_exports.DeleteTopic = function(topic, _cb/*_err*/) {
  sql_client.query('DELETE FROM messages.TOPICS WHERE DENUMIRE = ?', [topic], function(err, result) {
    if (err) 
      _cb(err);
    else
      _cb(null);
  });
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


module.exports = _exports;
