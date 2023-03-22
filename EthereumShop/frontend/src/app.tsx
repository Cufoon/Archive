import React, { useState } from 'react';
import { Outlet } from 'react-router-dom';
import { Layout, Message } from '@arco-design/web-react';
import { IconMenuFold, IconMenuUnfold } from '@arco-design/web-react/icon';
import Footer from '$components/footer';
import Menus from '$components/menus';
import UserInfo from '$components/navinfo';

import styles from './app.scss';

Message.config({
  maxCount: 3,
  duration: 2000
});

const App: React.FC = () => {
  const [collapsed, setCollapsed] = useState<boolean>(false);

  const onResponseCollapsed = (isCollapsed: boolean, type: string) => {
    if (type === 'responsive') {
      setCollapsed(isCollapsed);
    }
  };

  return (
    <Layout className={styles.appLayout}>
      <Layout.Sider
        theme='light'
        className={styles.appSider}
        trigger={null}
        collapsed={collapsed}
        collapsible
        breakpoint='md'
        collapsedWidth={70}
        style={{
          overflow: 'auto',
          position: 'fixed',
          left: 0,
          top: 0
        }}
        onCollapse={onResponseCollapsed}
      >
        <div className={styles.logoContainer}>
          {(!collapsed && <div>同心链虚拟交易平台</div>) || '同心链'}
        </div>
        <Menus collapsed={collapsed} />
      </Layout.Sider>
      <Layout
        className={styles.appLayout}
        style={{
          backgroundColor: 'rgb(240, 240, 240)',
          marginLeft: collapsed ? '82px' : '212px'
        }}
      >
        <div className={styles.appHeader}>
          <div
            onClick={() => {
              setCollapsed((p) => !p);
            }}
            className={styles.appSiderCollasptor}
          >
            {collapsed ? <IconMenuUnfold /> : <IconMenuFold />}
          </div>
          <UserInfo />
        </div>
        <Layout.Content className={styles.appContent}>
          <Outlet />
        </Layout.Content>
        <Footer />
      </Layout>
    </Layout>
  );
};

export default App;
