var mysql=require("./node-mysql");
var config = require("./config");

var sql_client = mysql.createClient(config.db);

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

_exports.CheckUser=function(username,_cb/*_err,userInfo*/)
{
  //null or {username:string, admin: bool, id:...}
  sql_client.query('SELECT * FROM messages.USERS where USERNAME = ?',[username],function (err,results,fields) {
    if (err) { _cb(err); return; }
    if (typeof(results)!='undefined' && results.length!=0) {
      var admin=false;
      if (results[0].ROLE=='admin') admin=true;
      _cb(null,{username:results[0].USERNAME, admin: admin, id: results[0].idUSERS});
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
      _cb(null,{insertDate:results[0].INSERTDATE, id: results[0].idTOPICS, topicName: results[0].TOPICNAME});
    }
    else {
      _cb(null,null);
    }
  });
}

_exports.GetFullTopicInfo = function(topic, _cb/*_err,topic*/) {
  // null or {insertDate: ..., resources: [ 'asd', ...] };
  sql_client.query('SELECT * FROM messages.TOPICS where DENUMIRE = ?',[topic],function (err,results,fields) {
    if (err) { _cb(err); return; }
    if (typeof(results)!='undefined' && results.length!=0) {
      var returnVal={insertDate:results[0].INSERTDATE, id: results[0].idTOPICS, topicName: results[0].TOPICNAME, resources: [] };
      sql_client.query('SELECT * FROM messages.MESSAGES where FK_ID_TOPIC = ?',[returnVal.id],function (err,results,fields) {
        if (err) { _cb(err); return; }  
        if (typeof(results)!='undefined' && results.length!=0)
          for (var i=0;i<results.length;i++)
            returnVal.resources.push(results[i].DENUMIRE);
        _cb(null,returnVal);
      });
    }
    else {
      _cb(null,null);
    }
  });
}

_exports.AddTopic = function(topic, topicName, _cb/*_err*/) {
  sql_client.query('INSERT INTO messages.TOPICS SET INSERTDATE=NOW(), DENUMIRE = ?, TOPICNAME = ?',[topic, topicName], function(err, result) {
    if (err) 
      _cb(err);
    else
      _cb(null);
  });
}

_exports.UpdateTopic = function(topic, topicName, _cb/*_err*/) {
    sql_client.query('UPDATE messages.TOPICS SET TOPICNAME = ? WHERE DENUMIRE = ?',[topicName, topic], function(err, result) {
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
        _cb(null,{lastModified:results[0].LASTMODIFIED, content: results[0].DESCRIERE, id: results[0].idMESSAGES});
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
  // null or { msg_nr:, status: }

  _exports.GetTopicInfo(topic,function(_err,topicInfo) {
    if (_err) { _cb(_err); return; }
    if (topicInfo==null) { _cb('no topic'); return; }

    _exports.CheckUser(user,function(_err,userInfo){
      if (_err) { _cb(_err); return; }
      if (userInfo==null) { _cb('no user'); return; }

      sql_client.query('SELECT * FROM messages.NOTIFICATIONS where FK_ID_TOPIC = ? AND FK_ID_USER = ?',[topicInfo.id, userInfo.id],function (err,results,fields) {
        if (err) { _cb(err); return; }
        if (typeof(results)!='undefined' && results.length!=0) {
          _cb(null,{msg_nr: results[0].MSG_NR, status: results[0].STATUS });
        }
        else {
          _cb(null,null);
        }
      });
    }) ;
  });;
}

_exports.AddNotification = function (topic, user, msgnr, _cb/*_err*/) {
  _exports.GetTopicInfo(topic,function(_err,topicInfo) {
    if (_err) { _cb(_err); return; }
    if (topicInfo==null) { _cb('no topic'); return; }

    _exports.CheckUser(user,function(_err,userInfo){
      if (_err) { _cb(_err); return; }
      if (userInfo==null) { _cb('no user'); return; }

      sql_client.query('INSERT INTO messages.NOTIFICATIONS SET FK_ID_USER = ?, FK_ID_TOPIC = ?, MSG_NR = ?, STATUS = ?',[userInfo.id, topicInfo.id, msgnr, 'PENDING'], function(err, result) {
        if (err) 
          _cb(err);
        else
          _cb(null);
      });
    }) ;
  });;
}

_exports.UpdateNotification = function (topic, user, msgnr, _cb/*_err*/) {
  _exports.GetTopicInfo(topic,function(_err,topicInfo) {
    if (_err) { _cb(_err); return; }
    if (topicInfo==null) { _cb('no topic'); return; }

    _exports.CheckUser(user,function(_err,userInfo){
      if (_err) { _cb(_err); return; }
      if (userInfo==null) { _cb('no user'); return; }

      sql_client.query('UPDATE messages.NOTIFICATIONS SET MSG_NR = ? WHERE FK_ID_TOPIC = ? AND FK_ID_USER = ?',[msgnr, topicInfo.id, userInfo.id], function(err, result) {
        if (err) 
          _cb(err);
        else
          _cb(null);
      });
    }) ;
  });;
}

_exports.DeleteNotification = function (topic, user, _cb/*_err*/) {
  _exports.GetTopicInfo(topic,function(_err,topicInfo) {
    if (_err) { _cb(_err); return; }
    if (topicInfo==null) { _cb('no topic'); return; }

    _exports.CheckUser(user,function(_err,userInfo){
      if (_err) { _cb(_err); return; }
      if (userInfo==null) { _cb('no user'); return; }

      sql_client.query('UPDATE messages.NOTIFICATIONS SET STATUS = "DELETING" WHERE FK_ID_TOPIC = ? AND FK_ID_USER = ?',[topicInfo.id, userInfo.id], function(err, result) {
        if (err) 
          _cb(err);
        else
          _cb(null);
      });
    }) ;
  });;
}


module.exports = _exports;
