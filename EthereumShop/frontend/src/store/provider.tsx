import React, { useEffect, useState, type PropsWithChildren } from 'react';
import { getGlobalAccount } from '$service/eth';
import { getToken, setToken } from '$service/token';
import { Action } from './action';
import { useStore } from './store';
import { GlobalStateContext, GlobalDispatchContext } from './context';
import type { DispatchType } from './interface';

const GlobalStateComponent: React.FC<PropsWithChildren> = ({ children }) => {
  const [store, dispatch] = useStore();
  const [init, setInit] = useState(false);

  useEffect(() => {
    (async () => {
      let savedId = await getToken();
      if (savedId === '') {
        savedId = await getGlobalAccount();
      }
      await setToken(savedId);
      dispatch({
        type: Action.CHANGE_ACCOUNT_ID,
        payload: savedId
      });
      setInit(true);
    })();
  }, []);

  useEffect(() => {
    console.log('store', store);
  }, [store]);

  return (
    (init && (
      <GlobalDispatchContext.Provider value={dispatch as DispatchType}>
        <GlobalStateContext.Provider value={store}>{children}</GlobalStateContext.Provider>
      </GlobalDispatchContext.Provider>
    )) ||
    null
  );
};

export default GlobalStateComponent;
