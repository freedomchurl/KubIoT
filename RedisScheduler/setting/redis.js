const redis = require('redis')
const client = redis.createClient({port:6379,host:'101.101.219.90',password:'dlcjf2779!'});

module.exports = client;