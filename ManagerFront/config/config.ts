import { defineConfig } from 'umi';
import routes from './routes';

export default defineConfig({
  nodeModulesTransform: {
    type: 'none'
  },
  copy: [
    {
      from: 'src/assets/logo.png',
      to: 'static/logo.png'
    }
  ],
  theme: {
    'border-radius-base': '4px'
  },
  hash: true,
  locale: {
    default: 'zh-CN',
    antd: true,
    title: false,
    baseNavigator: false,
    baseSeparator: '-'
  },
  layout: {
    locale: false,
    name: '企业实习管理系统',
    logo: '/static/logo.png',
    layout: 'mix',
    splitMenus: true,
    navTheme: 'light',
    headerTheme: 'light',
    contentWidth: 'Fluid',
    fixedHeader: true,
    fixSiderbar: true,
    siderWidth: 160,
    pwa: false
  },
  ...routes,
  dva: {
    immer: true,
    hmr: true
  },
  proxy: {
    '/api': {
      target: 'http://127.0.0.1:8888/',
      changeOrigin: true,
      secure: false,
      pathRewrite: { '^/api': '' }
    }
  }
});
