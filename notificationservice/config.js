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
			active: true,
			host: 'localhost',
			port: 8080,
			path: '/jmsServer/subscribe'
		},
		onMessage: {
			active: true,
			host: 'localhost',
			port: 8080,
			path: '/jmsServer/newMessage'
		}
	}
}
