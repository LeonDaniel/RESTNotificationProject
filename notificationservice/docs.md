Deploy
========
```
Download and unzip node 0.4 ( http://nodejs.org/dist/node-v0.4.9.tar.gz )
Build and install it:
 - ./configure
 - make
 - make install
 
Update config.js with the machine and database information.

Run it: node main.js

It should print: Running server on ip:port

```

Usage
========
authentication = basic auth  
path  = topic, url parameters = resource

Topics
--------
/TOPIC/path/path2
* conditions:
    * CUD - admin rights
* DB: TOPICS
    * DENUMIRE = /path/path2
    * INSERTDATE = NOW()
    * TOPICNAME = from post body
* Create: { topicName: ... }, content-type: application/json
    * { error: true, error_string: “no topic name” }
    * { error: true, error_string: “no rights”}
    * { error: true, error_string: “existing topic”}
    * { error: false }
* Read:
    * { error: true, error_string: “topic does not exist”}
    * { error: false, insertDate: “...”, topicName: “tname”, resources: [ “param=1”, ... ] }
* Update: { topicName: ‘tname’ }, content-type: application/json
    * { error: true, error_string: “no topic name” }
    * { error: true, error_string: “no rights”}
    * { error: true, error_string: “topic does not exist”}
    * { error: false }
* Delete:
    * { error: true, error_string: “no rights”}
    * { error: true, error_string: “topic does not exist”}
    * { error: false } 

Messages
--------
/MESSAGE/path/path2?param=1
* conditii: 
    * CRUD - sa existe topicul
    * CUD - trebuie admin 
* DB: MESSAGES
    * DENUMIRE = ”param=1”
    * FK_ID_TOPIC = id of /path/path2
    * DESCRIERE = post_put_content 
    * LASTMODIFIED = NOW()
* Create:
    * { error: true, error_string: “no rights”}
    * { error: true, error_string: “topic does not exist”}
    * { error: true, error_string: “existing resource”}
    * { error: false }
* Read:
    * { error: true, error_string: “topic does not exist”}
    * { error: true, error_string: “resource does not exist”}
    * { error: false, lastModified: ..., content: DESCRIERE_din_db }
* Update:
    * { error: true, error_string: “no rights”}
    * { error: true, error_string: “topic does not exist”}
    * { error: true, error_string: “resource does not exist”}
    * { error: false }
* Delete:
    * { error: true, error_string: “no rights”}
    * { error: true, error_string: “topic does not exist”}
    * { error: true, error_string: “resource does not exist”}
    * { error: false }

Notifications
--------
/NOTIFICATION/path/path?user=un&nr_msg=nrm
* conditii:
    * CRUD - sa existe topicul
    * CUD - trebuie admin. R doar pt alt user
* DB: NOTIFICATIONS
    * FK_ID_TOPIC = id of /path/path2
    * MSG_NR = nrm
    * FK_ID_USER = id of un
    * STATUS = (PENDING/DELETING/connection_set_by_lucian)
* Create:
    * { error: true, error_string: “no rights”}
    * { error: true, error_string: “topic does not exist”}
    * { error: true, error_string: “user does not exist”}
    * { error: true, error_string: “existing notification”}
    * { error: false } + apelare url lucian
* Read:
    * { error: true, error_string: “no rights”}
    * { error: true, error_string: “topic does not exist”}
    * { error: true, error_string: “user does not exist”}
    * { error: true, error_string: “user is not subscribed”}
    * { error: false, connection: STATUS_din_dd, msg_nr:  }
* Update: 
    * { error: true, error_string: “no rights”}
    * { error: true, error_string: “topic does not exist”}
    * { error: true, error_string: “user does not exist”}
    * { error: true, error_string: “notification does not exist”}
    * { error: false } + apelare url lucian
* Delete:
    * { error: true, error_string: “no rights”}
    * { error: true, error_string: “topic does not exist”}
    * { error: true, error_string: “user does not exist”}
    * { error: true, error_string: “user is not subscribed”}
    * { error: false }