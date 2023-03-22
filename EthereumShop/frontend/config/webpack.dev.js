const common = require('./webpack');

const config = common({ isDev: true });

console.log('develop webpack config:\n', config);

module.exports = config;
