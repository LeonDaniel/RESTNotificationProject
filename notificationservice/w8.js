module.exports = function W8() {
        var count = 0;
        var exec = false;
	var callback = function(){};

	this.run = function( _callback ) {
		callback=_callback;
		exec = true;
		if ( count === 0 )
		{
			callback();
		}
	}

	this.queue = function() {
		count++;
		return function() {
			count--;
			if ( exec && ( count === 0 ) ) {
				callback();
			}
		}
	}

}




