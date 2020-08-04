var express = require('express');
var app = express();


port = 7676;
// port num = 7676


var DeviceRoutes = require('./routes/device/device_router.js');


app.use(express.json());
app.use('/device',DeviceRoutes);

app.listen(port,function(){
	console.log('connected');
});
