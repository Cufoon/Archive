const tsLoader = require('./loader.ts');
const scssLoader = require('./loader.scss');
const assetLoader = require('./loader.asset');
const env = require('./env');
const plugin = require('./plugin');
const pathAlias = require('./alias');
const optimization = require('./optimization');
const fallback = require('./fallback');

module.exports = ({ isDev }) => {
  if (isDev) {
    env.setMode(env.MODE.DEV);
  }
  return {
    mode: isDev ? env.MODE.DEV : env.MODE.PROD,
    entry: { index: env.SOURCE_DIR('root.tsx') },
    output: {
      clean: true,
      chunkLoadingGlobal: 'CufoonChunkLoader',
      iife: true,
      hashDigestLength: 24,
      hashFunction: 'sha512',
      hashSalt: 'cufoon-hash',
      filename: '[name].[contenthash].js',
      chunkFilename: 'chunk.[name].[contenthash].js',
      path: env.BUILD_DIR(),
      publicPath: '/'
    },
    module: {
      rules: [...tsLoader(), ...scssLoader(), ...assetLoader()]
    },
    resolve: {
      extensions: ['.tsx', '.ts', '.jsx', '.js', '...'],
      alias: { ...pathAlias() },
      fallback: fallback()
    },
    optimization: { ...optimization() },
    plugins: [...plugin()],
    devServer: {
      client: {
        overlay: {
          errors: true,
          warnings: false
        },
        progress: true
      },
      headers: [
        {
          key: 'CUFOON-DEVELOP',
          value: 'cufoon.com'
        }
      ],
      host: '0.0.0.0',
      hot: true,
      liveReload: true,
      port: 2233,
      proxy: {
        '/api': {
          target: 'http://127.0.0.1:61001/v2',
          secure: false,
          changeOrigin: true,
          pathRewrite: { '^/api': '' }
        }
      },
      historyApiFallback: {
        index: '/index.html'
      },
      static: env.BUILD_DIR()
    },
    devtool: isDev ? 'eval-source-map' : false
  };
};
