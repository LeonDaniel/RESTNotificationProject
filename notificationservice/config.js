var os = require ( 'os' );

module.exports = 
{
	host: 'localhost',
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
		password: 'admin'
	},
	events: {
		onCreateNotification: {
			active: false,
			host: 'localhost',
			port: 80,
			path: '/ping'
		},
		onMessage: {
			active: false,
			host: 'localhost',
			port: 80,
			path: '/newMessage'
		}
	}
}
