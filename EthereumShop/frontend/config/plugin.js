const CopyPlugin = require('copy-webpack-plugin');
const ForkTsCheckerWebpackPlugin = require('fork-ts-checker-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const TerserPlugin = require('terser-webpack-plugin');
const webpack = require('webpack');
const env = require('./env');

module.exports = () => {
  console.log('plugin.js', 'isdev', env.isDev());
  const result = [
    new CopyPlugin({
      patterns: [
        {
          from: env.PUBLIC_DIR(),
          to: env.BUILD_DIR(),
          context: env.PUBLIC_DIR(),
          globOptions: { dot: true, gitignore: true, ignore: ['**/template.html'] },
          noErrorOnMissing: true
        }
      ]
    }),
    new MiniCssExtractPlugin(),
    new HtmlWebpackPlugin({
      filename: '[name].html',
      template: env.PUBLIC_DIR('template.html')
    })
  ];
  if (env.isDev()) {
    return [
      ...result,
      new webpack.ProvidePlugin({
        process: 'process/browser',
        Buffer: ['buffer', 'Buffer']
      })
    ];
  }
  return [
    ...result,
    new ForkTsCheckerWebpackPlugin({
      typescript: {
        configFile: env.PROJECT_DIR('tsconfig.json')
      }
    }),
    new TerserPlugin({
      terserOptions: {
        format: {
          comments: false
        }
      },
      extractComments: false
    }),
    new webpack.BannerPlugin({
      banner: 'copyright (c) Cufoon'
    }),
    new webpack.ProvidePlugin({
      process: 'process/browser',
      Buffer: ['buffer', 'Buffer']
    })
  ];
};
