import React, { PropsWithChildren, useCallback, useEffect, useMemo, useRef, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Menu } from '@arco-design/web-react';
import { useSelector } from '$store/hook';
import { getPathToMenuKeyMap, getRouteList } from '$src/utils/util';
import menus, { type Menu as MenuType } from '$src/menus';

import styles from './index.scss';

interface Props {
  collapsed: boolean;
}

const Menus: React.FC<PropsWithChildren<Props>> = ({ collapsed }) => {
  const rootSubmenuKeysRef = useRef<string[]>([]);
  const menuPathMap = useRef<Map<string, string>>(new Map<string, string>());
  const navigate = useNavigate();
  const location = useLocation();
  const authority = useSelector((state) => state.auth);

  const [initialed, setInitialed] = useState<boolean>(false);
  const [openKeys, setOpenKeys] = useState<string[]>([]);
  const [selectedKeys, setSelectedKeys] = useState<string[]>(['home']);

  useEffect(() => {
    console.log(location.pathname);
    if (!initialed) {
      const routes = getRouteList(location.pathname);
      if (routes.length === 0) {
        setInitialed(true);
        return;
      }
      const pathKeyMap = getPathToMenuKeyMap();
      const key = pathKeyMap.get(String(routes[0]));
      setSelectedKeys((prev) => {
        key && (prev[0] = key.key);
        return prev;
      });
      key && setOpenKeys(key.group);
      setInitialed(true);
    }
  }, [location, initialed]);

  const onOpenChange = (_key: string, keys: string[]) => {
    setOpenKeys((prev) => {
      const latestOpenKey = keys.find((key) => prev.indexOf(key) === -1);
      if (
        (latestOpenKey && rootSubmenuKeysRef.current.indexOf(latestOpenKey) === -1) ||
        keys.length === 1
      ) {
        return keys;
      } else {
        return latestOpenKey ? [latestOpenKey] : [];
      }
    });
  };

  const onSelect = (key: string) => {
    setSelectedKeys((prev) => {
      prev[0] = key;
      return prev;
    });
    const path = menuPathMap.current.get(key);
    path && navigate(path);
  };

  const generateMenu = useCallback(
    (m: MenuType[], root: boolean, auth: number, collapsed1: boolean, child = false) => {
      return m.map((item) => {
        if (item.children) {
          if (item.access?.includes(auth)) {
            if (root) {
              rootSubmenuKeysRef.current.push(item.key);
            }
            return (
              <Menu.SubMenu
                key={item.key}
                className={!collapsed1 && styles.fixArcoMenuIconSuffix}
                style={{ overflow: 'hidden' }}
                title={
                  <div>
                    <span
                      style={{
                        position: 'relative',
                        left: '-4px',
                        top: '5px'
                      }}
                    >
                      {item.icon}
                    </span>
                    {!collapsed1 && <span>{item.name}</span>}
                  </div>
                }
              >
                {generateMenu(item.children, false, auth, collapsed1, true)}
              </Menu.SubMenu>
            );
          }
          return;
        }
        if (item.access?.includes(auth)) {
          menuPathMap.current.set(item.key, item.path || '404');
          return (
            <Menu.Item
              key={item.key}
              className={styles.fixArcoMenuItemInner}
              style={{
                overflow: 'hidden',
                display: 'flex',
                justifyContent: 'flex-start',
                alignItems: 'center'
              }}
            >
              <div
                style={{
                  marginLeft: '-4px',
                  display: 'flex',
                  justifyContent: 'center',
                  alignItems: 'center',
                  height: '40px',
                  marginRight: ((child || !collapsed1) && '5px') || '0'
                }}
              >
                {item.icon}
              </div>
              {(child || !collapsed1) && item.name}
            </Menu.Item>
          );
        }
        return;
      });
    },
    []
  );

  const MenuItems = useMemo(
    () => generateMenu(menus, true, authority, collapsed),
    [authority, collapsed]
  );

  return initialed ? (
    <Menu
      accordion
      autoOpen
      theme='light'
      mode='vertical'
      levelIndent={12}
      selectedKeys={selectedKeys}
      openKeys={openKeys}
      onClickSubMenu={onOpenChange}
      onClickMenuItem={onSelect}
      style={{ userSelect: 'none', padding: '0', margin: '0' }}
    >
      {MenuItems}
    </Menu>
  ) : null;
};

export default Menus;
