import {
  IconArchive,
  IconHistory,
  IconHome,
  IconSettings,
  IconShareExternal,
  IconTrophy,
  IconUser
} from '@arco-design/web-react/icon';

export interface Menu {
  name: string;
  key: string;
  path?: string;
  access?: number[];
  icon?: JSX.Element;
  children?: Menu[];
}

const menus: [Menu, ...Menu[]] = [
  {
    name: '主页',
    key: 'home',
    path: '/',
    access: [0],
    icon: <IconUser />
  },
  {
    name: '管理',
    key: 'manage',
    path: '/',
    access: [0],
    icon: <IconSettings />,
    children: [
      {
        name: '我的商品',
        key: 'manage-my-product',
        path: 'product-mine',
        access: [0],
        icon: <IconHistory />
      },
      {
        name: '发布商品',
        key: 'manage-product-publish',
        path: 'product-publish',
        access: [0],
        icon: <IconShareExternal />
      }
    ]
  },
  {
    name: '商店',
    key: 'shop',
    path: '/',
    access: [0],
    icon: <IconArchive />,
    children: [
      {
        name: '商品主页',
        key: 'shop-home',
        path: 'shop-home',
        access: [0],
        icon: <IconHome />
      },
      {
        name: '商品排行',
        key: 'shop-rank',
        path: 'shop-rank',
        access: [0],
        icon: <IconTrophy />
      }
    ]
  }
];

export default menus;
