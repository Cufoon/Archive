import React, { useEffect } from 'react';
import { history, useModel } from 'umi';
import { Form, Input, Button, Checkbox } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import localforage from 'localforage';
import { request } from '@/services/request';
import { urlLogin } from '@/services/url';
import { createMessageLoading } from '@/utils/message';
import styles from './login.scss';

export interface LoginParam {
  username: string;
  password: string;
  remember?: boolean;
}

export interface UserInfo {
  username: string;
  password: string;
}

export interface LoginResponse {
  logined: boolean;
  type: number;
}

const REMEMBER_KEY = 'remember-user';

const LoginPage: React.FC = () => {
  const { initialState, refresh } = useModel('@@initialState');
  const [form] = Form.useForm<LoginParam>();

  const onFinish = async (values: LoginParam) => {
    if (values.remember) {
      localforage.setItem<UserInfo>(
        REMEMBER_KEY,
        {
          username: values.username,
          password: values.password
        },
        (err, value) => {
          console.log(err, value);
        }
      );
    } else {
      localforage.removeItem(REMEMBER_KEY, (err) => {
        console.log(err);
      });
    }
    const loading = createMessageLoading('login_loading');
    loading.showLoading('登录中');
    const [res, ok] = await request<LoginParam, LoginResponse>(urlLogin, 'POST', {
      username: values.username,
      password: values.password
    });
    if (ok.succeed && res?.logined) {
      try {
        await refresh();
      } catch (e) {
        console.log(e);
      }
      loading.success('登录成功！');
      if (res.type === 3) {
        history.push('/admin');
      } else if (res.type === 2) {
        history.push('/teacher/info');
      } else {
        history.push('/student/info');
      }
    } else {
      loading.error(ok.message || '登录失败！');
    }
  };

  useEffect(() => {
    if (initialState && initialState.role && initialState.role > 0) {
      if (initialState.role === 0) {
        history.push('/login');
      } else if (initialState.role === 1) {
        history.push('/student/message');
      } else if (initialState.role === 2) {
        history.push('/teacher/message');
      } else {
        history.push('/admin/notification');
      }
    }
  }, [initialState]);

  useEffect(() => {
    localforage.getItem<UserInfo>(REMEMBER_KEY, (err, value) => {
      if (value) {
        form.setFieldsValue({
          ...value,
          remember: true
        });
      }
    });
  }, []);

  return (
    <div className={styles.background}>
      <div className={styles.content}>
        <div className={styles.titleLine}>
          <div className={styles.icon} />
          <div className={styles.title}>企业实习管理系统</div>
        </div>
        <Form
          form={form}
          name='uni_login'
          className={styles.form}
          initialValues={{
            remember: true
          }}
          onFinish={onFinish}
        >
          <Form.Item
            name='username'
            rules={[
              {
                required: true,
                message: '请输入你的用户名!'
              }
            ]}
          >
            <Input prefix={<UserOutlined className='site-form-item-icon' />} placeholder='用户名' />
          </Form.Item>
          <Form.Item
            name='password'
            rules={[
              {
                required: true,
                message: '请输入你的密码!'
              }
            ]}
          >
            <Input prefix={<LockOutlined className='site-form-item-icon' />} type='password' placeholder='密码' />
          </Form.Item>
          <Form.Item>
            <div className={styles.formLeftAndRight}>
              <Form.Item name='remember' valuePropName='checked' noStyle>
                <Checkbox>记住密码</Checkbox>
              </Form.Item>
            </div>
          </Form.Item>
          <Form.Item>
            <Button type='primary' htmlType='submit' className={styles.formSubmit}>
              登录
            </Button>
          </Form.Item>
        </Form>
      </div>
    </div>
  );
};

export default LoginPage;
