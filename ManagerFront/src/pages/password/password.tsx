import React from 'react';
import { ExclamationCircleOutlined, IdcardOutlined } from '@ant-design/icons';
import { Button, Form, Input, message, Modal } from 'antd';
import { requestPost } from '@/services/request';
import { urlChangePassword } from '@/services/url';
import { isNicePassword } from '@/utils/validator';
import TitleLine from '@/components/page-header/page-header';
import styles from './password.scss';

interface FormData {
  oldPassword: string;
  newPassword: string;
  confirmPassword: string;
}

interface SendData {
  oldPassword: string;
  newPassword: string;
}

interface GetData {
  changed: boolean;
}

const ChangePasswordPage: React.FC = () => {
  const [form] = Form.useForm<FormData>();

  const showPromiseConfirm = (value: FormData) => {
    Modal.confirm({
      title: '确认要修改密码吗？',
      icon: <ExclamationCircleOutlined />,
      onOk: async () => {
        await formSubmit(value);
      },
      onCancel: () => {
        form.resetFields();
      }
    });
  };

  const formSubmit = async (value: FormData) => {
    const [data, ok] = await requestPost<SendData, GetData>(urlChangePassword, {
      oldPassword: value.oldPassword.slice(0, 16),
      newPassword: value.newPassword
    });
    if (ok.succeed && data?.changed) {
      message.success('修改成功');
      form.resetFields();
    } else {
      ok.message && message.error(ok.message);
    }
  };

  const onFinish = (value: FormData) => {
    showPromiseConfirm(value);
  };

  const validateMessages = {
    required: '请填写 ${label} ！'
  };

  return (
    <div className={styles.outer}>
      <div className={styles.titleLine}>
        <TitleLine Icon={IdcardOutlined} headContent='修改密码' />
      </div>
      <Form
        form={form}
        onFinish={onFinish}
        name='passwordForm'
        size='middle'
        validateMessages={validateMessages}
        labelCol={{ span: 8 }}
        wrapperCol={{ span: 32 }}
        className={styles.form}
      >
        <Form.Item label='旧密码' name='oldPassword' validateFirst rules={[{ required: true }]}>
          <Input.Password />
        </Form.Item>
        <Form.Item
          name='newPassword'
          label='新密码'
          validateFirst
          rules={[
            { required: true },
            {
              type: 'string',
              max: 16,
              min: 8,
              message: '密码应为8-16位字符'
            },
            {
              validator: async (rule, value) => {
                const [isOK, errorLevel] = isNicePassword(value);
                if (isOK) {
                  return;
                } else {
                  if (errorLevel === 1) {
                    throw new Error('密码需包含至少一个大写字母、一个小写字母和一个数字');
                  } else if (errorLevel === 2) {
                    throw new Error('密码只能由字母和数字组成');
                  } else if (errorLevel === 3) {
                    throw new Error('密码应为8-16位字符');
                  }
                }
                throw new Error('两次密码输入不一致');
              }
            }
          ]}
          hasFeedback
        >
          <Input.Password />
        </Form.Item>
        <Form.Item
          name='confirmPassword'
          label='确认新密码'
          dependencies={['newPassword']}
          validateFirst
          hasFeedback
          rules={[
            {
              required: true
            },
            {
              validator: async (rule, value) => {
                if (value && form.getFieldValue('newPassword') === value) {
                  return;
                }
                throw new Error('两次密码输入不一致');
              }
            }
          ]}
        >
          <Input.Password />
        </Form.Item>
        <div className={styles.submit}>
          <Form.Item>
            <Button type='primary' htmlType='submit' className={styles.btn}>
              确认修改
            </Button>
          </Form.Item>
        </div>
      </Form>
    </div>
  );
};

export default ChangePasswordPage;
