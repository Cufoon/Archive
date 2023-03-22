import { icons } from '@/menus/icon';

export const getBase64 = (img: Blob, callback: any) => {
  const reader = new FileReader();
  reader.addEventListener('load', () => callback(reader.result));
  reader.readAsDataURL(img);
};

export const handleMenu = (e: { [x: string]: any; menu: any }[], home: string, root: string) => {
  const rList = e.map(({ menu, ...item }) => ({
    ...item,
    menu: {
      ...menu,
      icon: menu?.icon !== undefined && icons[menu.icon]
    }
  }));

  return [
    {
      path: root,
      menu: {
        hideInMenu: true,
        hideChildrenInMenu: false
      },
      children: [...rList]
    },
    {
      redirect: home
    }
  ];
};
