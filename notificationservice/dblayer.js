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
	_cb(null,true,{username:username});
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
