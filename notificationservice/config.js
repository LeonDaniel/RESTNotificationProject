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
			host: 'localhost',
			port: 80,
			path: '/ping'
		},
		onMessage: {
			active:true,
			host: 'localhost',
			port: 80,
			path: '/newMessage'
		}
	}
}
