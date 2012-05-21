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
	app.all( '/*' , function( _req , _res ) {
		//extracting the path
		var resource_path=_req.params[0]
	
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
		var resourceStr='/'+resource_path+((resource_url_params_sorted.length==0)?'':'?'+resource_url_params_sorted);

		var method=_req.method;
		switch (method)
		{
			case 'GET':
			{
				dblayer.getResourceInformation(_req.remoteUser,resourceStr,function(response){
					_res.writeHead(200, {'Content-type':'application/json'});
					_res.end(JSON.stringify(response));
				});
				break;
			}
			case 'POST':
			{
				dblayer.createResource(_req.remoteUser,resourceStr,function(response){
					_res.writeHead(200, {'Content-type':'application/json'});
					_res.end(JSON.stringify(response));
				});
				break;
			}
			case 'DELETE':
			{
				dblayer.deleteResource(_req.remoteUser,resourceStr,function(response){
					_res.writeHead(200, {'Content-type':'application/json'});
					_res.end(JSON.stringify(response));
				});
				break;
			}
			default:
			_res.writeHead(200);
			_res.end('Unimplemented');
		}
	});
}));

serverhttp.listen( config.port , config.host ); 
console.log('Started server on '+config.host+':'+config.port);
