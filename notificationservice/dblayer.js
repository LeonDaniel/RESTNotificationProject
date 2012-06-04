var mysql=require("./node-mysql");


var sql_client = mysql.createClient({
	host: 'localhost',
	port: 3306,
	user: 'root',
	password: '',
	schema: 'messages'
});

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
  // null or {insertDate: ..., id:...};
  sql_client.query('SELECT * FROM messages.TOPICS where DENUMIRE = ?',[topic],function (err,results,fields) {
    if (err) { _cb(err); return; }
    if (typeof(results)!='undefined' && results.length!=0) {
      _cb(null,{insertDate:results[0].INSERTDATE, id: results[0].idTOPICS});
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
  //null or { lastModified: ..., content: ..., id: }
  _exports.GetTopicInfo(topic,function(_err,topicInfo) {
    if (_err) { _cb(_err); return; }
    if (topicInfo==null) { _cb('no topic'); return; }
    sql_client.query('SELECT * FROM messages.MESSAGES where FK_ID_TOPIC = ? AND DENUMIRE = ?',[topicInfo.id, resource],function (err,results,fields) {
      if (err) { _cb(err); return; }
      if (typeof(results)!='undefined' && results.length!=0) {
        _cb(null,{lastModified:results[0].LASTMODIFIED, content: results[0].DESCRIERE, id: results[0].idNOTIFICATIONS});
      }
      else {
        _cb(null,null);
      }
    });
  });
}

_exports.AddResource = function(topic, resource, content, _cb/*_err*/) {
  _exports.GetTopicInfo(topic,function(_err,topicInfo) {
    if (_err) { _cb(_err); return; }
    if (topicInfo==null) { _cb('no topic'); return; }
    sql_client.query('INSERT INTO messages.MESSAGES SET DENUMIRE = ?, DESCRIERE = ?, FK_ID_TOPIC = ?, LASTMODIFIED = NOW()',[resource, content, topicInfo.id], function(err, result) {
      if (err) 
        _cb(err);
      else
        _cb(null);
    });
  });;
}

_exports.UpdateResource = function(topic, resource, content, _cb/*_err*/) {
  _exports.GetTopicInfo(topic,function(_err,topicInfo) {
    if (_err) { _cb(_err); return; }
    if (topicInfo==null) { _cb('no topic'); return; }
    sql_client.query('UPDATE messages.MESSAGES SET DESCRIERE = ?, LASTMODIFIED = NOW() WHERE FK_ID_TOPIC = ? AND DENUMIRE = ?',[content, topicInfo.id, resource], function(err, result) {
      if (err) 
        _cb(err);
      else
        _cb(null);
    });
  });;
}

_exports.DeleteResource = function(topic, resource, content, _cb/*_err*/) {
  _exports.GetTopicInfo(topic,function(_err,topicInfo) {
    if (_err) { _cb(_err); return; }
    if (topicInfo==null) { _cb('no topic'); return; }
    sql_client.query('DELETE FROM messages.MESSAGES WHERE FK_ID_TOPIC = ? AND DENUMIRE = ?', [topicInfo.id, resource], function(err, result) {
      if (err) 
        _cb(err);
      else
        _cb(null);
    });
  });
}

_exports.GetNotificationInfo = function (topic, user, _cb/*_err, notif*/) {
  _cb('not implemented');
}

_exports.AddNotification = function (topic, user, _cb/*_err*/) {
  _cb('not implemented');
}

_exports.DeleteNotification = function (topic, user, _cb/*_err*/) {
  _cb('not implemented');
}


module.exports = _exports;
