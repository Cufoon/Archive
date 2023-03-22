import { createRoot } from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import { ConfigProvider } from '@arco-design/web-react';

import Routes from './route';
import GlobalStateComponent from './store/provider';

import './root.scss?raw';

const container = document.getElementById('app');
if (container) {
  const root = createRoot(container);
  root.render(
    <GlobalStateComponent>
      <ConfigProvider
        autoInsertSpaceInButton
        componentConfig={{
          Modal: {
            closable: false
          },
          Pagination: {
            hideOnSinglePage: true
          },
          'List.Item': {
            style: {
              borderRadius: '12px',
              overflow: 'hidden'
            }
          }
        }}
      >
        <BrowserRouter>
          <Routes />
        </BrowserRouter>
      </ConfigProvider>
    </GlobalStateComponent>
  );
}
