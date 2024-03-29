const path = require('path');

const createPath = (p) => (child) => {
  if (child) {
    return path.resolve(p, child);
  }
  return p;
};

const MODE = {
  DEV: 'development',
  PROD: 'production'
};

let mode = MODE.PROD;

const setMode = (m) => (mode = m);
const isDev = () => mode === MODE.DEV;

const isPROD = () => mode === MODE.PROD;

module.exports = {
  PROJECT_DIR: createPath(path.resolve(__dirname, '..')),
  SOURCE_DIR: createPath(path.resolve(__dirname, '../src')),
  PUBLIC_DIR: createPath(path.resolve(__dirname, '../public')),
  BUILD_DIR: createPath(path.resolve(__dirname, '../build')),
  MODE,
  setMode,
  isDev,
  isPROD
};
