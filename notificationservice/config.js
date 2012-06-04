var os = require ( 'os' );

module.exports = 
{
	host: '192.168.1.102',
	port: 80,
	https: 
	{
		use: false,
		key: 'mtibeica.com.private.key',
		cert: 'mtibeica.com.crt'
	},
	db: {
		host: 'localhost',
		port: 3306,
		user: 'root',
		password: ''
	}
}
