import React from 'react';
import { requestGet } from '@/services/request';
import { message, notification } from 'antd';
import { urlGetRole } from '@/services/url';
import { handleMenu } from '@/utils/convert';
import { studentRoutes, studentHomeRoute } from '@/menus/student';
import { teacherRoutes, teacherHomeRoute } from '@/menus/teacher';
import { adminRoutes, adminHomeRoute } from '@/menus/admin';
import Header from '@/components/top-right-bar/top-right-bar';
import { RoleData, InitialStateData } from './interface';

message.config({
  duration: 1.25,
  maxCount: 1,
  top: 0
});

notification.config({
  placement: 'topRight',
  duration: 3,
  rtl: true
});

const defaultData = {
  name: '默认用户名',
  avatar: 'https://xxxx.yyy/images/avatar.webp',
  role: 0
};

export async function getInitialState(): Promise<InitialStateData> {
  let result = defaultData;
  const [data, ok] = await requestGet<unknown, RoleData>(urlGetRole);
  if (ok.succeed && data) {
    result = {
      name: data.name,
      avatar: data.avatar,
      role: data.type
    };
  }
  return result;
}

export const layout = {
  rightRender: Header,
  patchMenus: (menus: any, eee: { initialState: InitialStateData }) => {
    const userInfo: InitialStateData = eee.initialState;
    if (userInfo.role === 1) {
      return handleMenu(studentRoutes, studentHomeRoute, '/student');
    } else if (userInfo.role === 2) {
      return handleMenu(teacherRoutes, teacherHomeRoute, '/teacher');
    } else if (userInfo.role === 3) {
      return handleMenu(adminRoutes, adminHomeRoute, '/admin');
    } else {
      return [];
    }
  }
};
