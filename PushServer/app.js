var express = require('express');
var app = express();
var cors = require('cors');

port = 7878;
// port num = 7878


app.use(cors());
//var DeviceRoutes = require('./routes/device/device_router.js');
//var ProjectRoutes = require('./routes/project/project_router.js');
var PushRoutes = reuiqre('./routes/push/push_router.js');


app.use(express.json());
//app.use('/device',DeviceRoutes);
//app.use('/project',ProjectRoutes);
app.use('/push',PushRoutes);

app.listen(port,function(){
	console.log('connected');
});
