var os = require ( 'os' );

module.exports = 
{
	host: '172.21.36.46',
	port: 1337,
	https: {
		use: false,
		key: 'mtibeica.com.private.key',
		cert: 'mtibeica.com.crt'
	},
	db: {
		host: 'localhost',
		port: 3306,
		user: 'root',
		password: 'root'
	},
	events: {
		onCreateNotification: {
			active: true,
			host: '172.21.36.46',
			port: 12345,
			path: '/ping'
		}
	}
}
