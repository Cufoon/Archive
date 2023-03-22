import menus, { type Menu as MenuType } from '$src/menus';

interface pathKeyMapData {
  group: string[];
  key: string;
  access: number[];
}

let pathKeyMapOne: Map<string, pathKeyMapData> | null = null;

export const getPathToMenuKeyMap = () => {
  if (pathKeyMapOne) {
    return pathKeyMapOne;
  }
  const pathKeyMap = new Map<string, pathKeyMapData>();
  const fmt = (m: MenuType[], group: string[]) => {
    m.map((item) => {
      if (item.children) {
        fmt(item.children, [...group, item.key]);
        return;
      }
      if (item.path) {
        pathKeyMap.set(item.path, { group, key: item.key, access: item.access || [] });
      }
    });
  };
  fmt(menus, []);
  pathKeyMapOne = pathKeyMap;
  return pathKeyMap;
};

export const getRouteList = (path: string) => {
  if (path === '/' || path === '//') {
    return ['person-info'];
  }
  if (path[0] === '/') {
    path = path.substring(1);
  }
  if (path[path.length - 1] === '/') {
    path = path.substring(0, path.length - 1);
  }
  return path.split('/');
};

const charList = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
export const getRandomStr = (len = 64) => {
  let result = '';
  for (let i = 0; i < len; i++) {
    const randomIndex = Math.floor(62 * Math.random());
    result += charList[randomIndex];
  }
  return result;
};

export const getRandomMsgId = () => `${Date.now().toString()}-${getRandomStr(12)}`;

export const delay = (d = 100) => {
  if (d < 20) {
    d = 20;
  }
  return new Promise((resolve) => {
    setTimeout(resolve, d);
  });
};
