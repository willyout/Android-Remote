Object.prototype.deep_clone = function(){
	eval("var tmp = " + this.toJSON());
	return tmp;
}
Object.prototype.toJSON = function(){
	var json = [];
	for(var i in this){
		if(!this.hasOwnProperty(i)) continue;
		//if(typeof this[i] == "function") continue;
		json.push(
			i.toJSON() + " : " +
			((this[i] != null) ? this[i].toJSON() : "null")
		)
	}
	return "{\n " + json.join(",\n ") + "\n}";
}

Array.prototype.toJSON = function(){
	for(var i=0,json=[];i<this.length;i++)
		json[i] = (this[i] != null) ? this[i].toJSON() : "null";
	return "["+json.join(", ")+"]"
}

String.prototype.toJSON = function(){
	return '"' +
		this.replace(/(\\|\")/g,"\\$1")
		.replace(/\n|\r|\t/g,function(){
			var a = arguments[0];
			return  (a == '\n') ? '\\n':
					(a == '\r') ? '\\r':
					(a == '\t') ? '\\t': ""
		}) +
		'"'
}

Boolean.prototype.toJSON = function(){return this}
Function.prototype.toJSON = function(){return this}
Number.prototype.toJSON = function(){return this}
RegExp.prototype.toJSON = function(){return this}

// strict but slow
String.prototype.toJSON = function(){
	var tmp = this.split("");
	for(var i=0;i<tmp.length;i++){
		var c = tmp[i];
		(c >= ' ') ?
			(c == '\\') ? (tmp[i] = '\\\\'):
			(c == '"')  ? (tmp[i] = '\\"' ): 0 :
		(tmp[i] = 
			(c == '\n') ? '\\n' :
			(c == '\r') ? '\\r' :
			(c == '\t') ? '\\t' :
			(c == '\b') ? '\\b' :
			(c == '\f') ? '\\f' :
			(c = c.charCodeAt(),('\\u00' + ((c>15)?1:0)+(c%16)))
		)
	}
	return '"' + tmp.join("") + '"';
}


var redis = require("redis");
var http = require("http");
var exec = require('child_process').exec;
var url = require('url');
var os = require('os');

var test = "{hostname:"+os.hostname()+"}";

var info = {hostname:os.hostname(), 
	type:os.type(),platform:os.platform(), arch:os.arch(), release:os.release(), 
	uptime:os.uptime(), loadavg:os.loadavg(), totalmem:os.totalmem(), 
	freemem:os.freemem(), cpus:os.cpus(), networkInterfaces:os.networkInterfaces()};

var server = http.createServer(function(req, res)
{
    console.log(url.parse(req.url));
    if(url.parse(req.url).pathname == "/com"){
    exec(url.parse(req.url).query.replace(/%20/, ' '), function(error, stdout, stderr)
    {
        if(stdout.length != 0)
        {
            res.end(stdout)
        }
        if(stderr.length != 0)
        {
            res.end(stderr)
        }
        res.end(stdout);
        console.log('stdout: ', stdout);
        console.log('stderr: ', stderr);
        if(error != null)
        {
            console.log('exec error: ', error);
        }
    });
    }
    else if(url.parse(req.url).pathname == "/server")
    {
		res.end(info.toJSON());        
    }
});

server.listen(8888);
