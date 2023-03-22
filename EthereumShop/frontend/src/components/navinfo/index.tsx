import React, { useEffect, useMemo, useRef, useState } from 'react';
import { Select, Modal, Avatar } from '@arco-design/web-react';
import { createMessageLoading } from '$utils/message';
import { useSelector, useDispatch, Action } from '$store/hook';
import { web3 } from '$service/eth';
import { setToken } from '$service/token';
import styles from './index.scss';

const UserInfo: React.FC = () => {
  const [visible, setVisible] = useState(false);
  const nextAccountRef = useRef('');
  const { account_id } = useSelector((p) => p);
  const dispatch = useDispatch();
  const [nextAccount, setNextAccount] = useState<string>(account_id);
  const [accountList, setAccountList] = useState<string[]>([]);
  const nowIndex = useMemo(() => {
    return accountList.findIndex((value) => {
      return value === account_id;
    });
  }, [accountList, account_id]);

  const options = useMemo(() => {
    return accountList.map((item, index) => {
      return {
        label: index,
        value: item
      };
    });
  }, [accountList]);

  useEffect(() => {
    (async () => {
      const ids = await web3.eth.getAccounts();
      // setNextAccount(ids[0] || '');
      setAccountList(ids);
    })();
  }, []);

  useEffect(() => {
    setNextAccount(account_id);
  }, [account_id]);

  const onChangeAccount = async () => {
    const msl = createMessageLoading();
    msl.showLoading('正在切换');
    await new Promise((resolve) => {
      setTimeout(resolve, 1000);
    });
    setVisible(false);
    console.log('nextAccount', nextAccountRef.current);
    await setToken(nextAccountRef.current);
    dispatch({ type: Action.CHANGE_ACCOUNT_ID, payload: nextAccountRef.current });
    msl.success('成功切换账户');
    setTimeout(() => {
      location.reload();
    }, 100);
  };

  return (
    <>
      <div
        className={styles.container}
        onClick={() => {
          nextAccountRef.current = nextAccount;
          setVisible(true);
        }}
      >
        <Avatar style={{ backgroundColor: '#165DFF', marginRight: '5px' }}>
          {nowIndex > -1 ? nowIndex : '-'}
        </Avatar>
        <div className={styles.name}>{account_id.substring(2, 10)}</div>
      </div>
      <Modal
        title='切换用户'
        visible={visible}
        onOk={onChangeAccount}
        onCancel={() => setVisible(false)}
        okText='切换'
        cancelText='取消'
        autoFocus={false}
        focusLock={true}
      >
        <p>选择要切换到用户的id</p>
        <p>之前的用户id:{nowIndex}</p>
        <Select
          onChange={(value) => {
            setNextAccount(value);
            nextAccountRef.current = value;
          }}
          value={nextAccount}
          options={options}
        />
      </Modal>
    </>
  );
};

export default UserInfo;
