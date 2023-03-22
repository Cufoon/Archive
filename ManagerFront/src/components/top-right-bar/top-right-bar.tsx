import React, { useEffect, useState } from 'react';
import { requestGet } from '@/services/request';
import localforage from 'localforage';
import dayjs from 'dayjs';
import { urlAutoMessageList, urlLogout } from '@/services/url';
import { Avatar, Button, Col, message, Popconfirm, Row, Space, notification } from 'antd';
import { history, useModel } from 'umi';
import { InfoCircleFilled, LoadingOutlined, LogoutOutlined, NotificationOutlined } from '@ant-design/icons';
import { AutoMessage, AutoMessageList } from './interface';
import styles from './top-right-bar.scss';

const STORAGE_KEY = 'last-notice-date';

const URL_FOR_MESSAGE_LIST = ['', '/student/message', '/teacher/message'];

const Header: React.FC = () => {
  const { initialState, refresh } = useModel('@@initialState');
  const [init, setInit] = useState<boolean>(true);
  const [hadNoticed, setHadNoticed] = useState<boolean>(true);
  const [confirmLoading, setConfirmLoading] = useState<boolean>(false);
  const [autoMessageList, setAutoMessageList] = useState<AutoMessage[]>([]);

  const logout = async () => {
    const [data, ok] = await requestGet<unknown, { logout: boolean }>(urlLogout);
    if (ok.succeed && data?.logout) {
      try {
        await refresh();
      } catch (e) {
        console.log('refresh initialState 失败');
      }
      message.success('成功退出登录');
      history.push('/login');
      console.log('退出登录成功');
    } else {
      history.push('/login');
    }
  };

  const confirm = () => {
    setConfirmLoading(true);
    logout();
  };

  const cancel = () => {};

  const onClickNotificationIcon = () => {
    history.push(URL_FOR_MESSAGE_LIST[initialState?.role || 0]);
  };

  const getAutoMesage = async () => {
    if (!hadNoticed) {
      const [data, ok] = await requestGet<unknown, AutoMessageList>(urlAutoMessageList);
      if (data) {
        if (ok.succeed) {
          !init && !hadNoticed && setAutoMessageList(data.autoList);
        } else {
          console.log(ok.message || '获取自动通知失败');
        }
      } else {
        console.log('未知错误');
      }
    }
  };

  useEffect(() => {
    if (initialState?.role !== 0) {
      if (initialState?.role === 1 && !hadNoticed && !init) {
        getAutoMesage();
      }
    }
  }, [initialState, hadNoticed, init]);

  useEffect(() => {
    if (autoMessageList?.length > 0 && !init && !hadNoticed) {
      autoMessageList.map((item) => {
        notification.open({
          message: item.title,
          description: item.content,
          onClick: () => {
            setHadNoticed(true);
          },
          onClose: () => {
            setHadNoticed(true);
          }
        });
      });
      setHadNoticed(true);
      localforage.setItem(STORAGE_KEY, dayjs().format('YYYY-MM-DD'), (err, value) => {
        console.log(err, value);
      });
    }
  }, [autoMessageList]);

  useEffect(() => {
    let living = true;
    localforage.getItem<string>(STORAGE_KEY, (err, value) => {
      if (err) {
        console.log(err);
      } else {
        console.log(value);
        if (living) {
          if (value) {
            const isBefore = dayjs(dayjs().format('YYYY-MM-DD')).isBefore(dayjs(value).add(1, 'day'), 'day');
            setHadNoticed(isBefore);
          } else {
            setHadNoticed(false);
          }
          setInit(false);
        }
      }
    });
    return () => {
      living = false;
    };
  }, []);

  return (
    <Row justify='start' align='middle' className={styles.outer}>
      <Space size={15}>
        <Col>
          <NotificationOutlined className={styles.notice} onClick={onClickNotificationIcon} />
        </Col>
        <Col push={1}>
          <Row>
            <Space size={7}>
              <Col>
                <Avatar className={styles.avatar} size='small' src={initialState?.avatar} />
              </Col>
              <Col>{initialState?.name || '默认用户名'}</Col>
            </Space>
          </Row>
        </Col>
        <Col>
          <Popconfirm
            icon={<InfoCircleFilled className={styles.confirm} />}
            arrowPointAtCenter
            placement='bottomLeft'
            title='确定要退出吗？'
            onConfirm={confirm}
            onCancel={cancel}
            okText='确定'
            cancelText='取消'
            okButtonProps={{ shape: 'round' }}
            cancelButtonProps={{ shape: 'round' }}
          >
            <Button
              icon={confirmLoading ? <LoadingOutlined /> : <LogoutOutlined />}
              loading={confirmLoading}
              danger
              shape='round'
              size='small'
              type='text'
              style={{ padding: 0 }}
            >
              退出登录
            </Button>
          </Popconfirm>
        </Col>
      </Space>
    </Row>
  );
};

export default Header;
