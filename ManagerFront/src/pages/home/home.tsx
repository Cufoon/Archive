import React, { useEffect } from 'react';
import { history, useModel } from 'umi';

const Home: React.FC = () => {
  const { initialState, refresh } = useModel('@@initialState');

  const gotoPage = (role: any) => {
    if (role === 0) {
      history.push('/login');
    } else if (role === 1) {
      history.push('/student/message');
    } else if (role === 2) {
      history.push('/teacher/message');
    } else {
      history.push('/admin/notification');
    }
  };

  useEffect(() => {
    if (initialState?.role === undefined) {
      refresh();
    }
  }, []);

  useEffect(() => {
    if (initialState?.role !== undefined) {
      gotoPage(initialState.role);
    }
  }, [initialState]);

  return null;
};

export default Home;
