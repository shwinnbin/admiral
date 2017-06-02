
let configure = require('./base-proxy.conf.js');

var configs = configure({
  services: {
    ip: 'localhost',
    port: '8282'
  }
});

module.exports = configs;
