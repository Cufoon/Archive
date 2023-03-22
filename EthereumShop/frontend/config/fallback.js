module.exports = () => ({
  crypto: require.resolve('crypto-browserify'),
  os: require.resolve('os-browserify/browser'),
  path: require.resolve('path-browserify'),
  stream: require.resolve('stream-browserify'),
  url: require.resolve('url/'),
  assert: require.resolve('assert/'),
  http: require.resolve('stream-http'),
  https: require.resolve('https-browserify')
});
