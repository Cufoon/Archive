const common = require('./webpack');

const config = common({ isDev: false });

console.log('production webpack config:\n', config);

module.exports = config;
