import React, { useEffect } from 'react';
import { Form, Input } from 'antd';
import { SkinFilled } from '@ant-design/icons';
import { createMessageLoading } from '@/utils/message';
import { requestGet } from '@/services/request';
import { urlStudentQueryTeacher } from '@/services/url';
import PageHeader from '@/components/page-header/page-header';
import styles from './teacher.module.scss';

interface TQueryResponse {
  succeed: boolean;
  name: string;
  sex: 'M' | 'F' | '男' | '女';
  phone: string;
  email: string;
}

const Index: React.FC = () => {
  const [form] = Form.useForm<TQueryResponse>();

  useEffect(() => {
    (async () => {
      const loadingMessage = createMessageLoading('student-teacher-query');
      loadingMessage.showLoading('获取校内导师信息');
      const [res, ok] = await requestGet<null, TQueryResponse>(urlStudentQueryTeacher);
      if (ok.succeed) {
        if (res?.succeed) {
          form.setFieldsValue({
            ...res,
            sex: res.sex === 'F' ? '女' : '男'
          });
        } else {
          loadingMessage.info('尚未添加导师');
        }
      } else {
        loadingMessage.error(ok.message || '未知错误');
      }
      loadingMessage.hideLoading();
    })();
  }, []);

  return (
    <div className={styles.container}>
      <PageHeader Icon={SkinFilled} headContent='校内导师信息' />
      <Form form={form} size='middle'>
        <div className={styles.line}>
          <Form.Item name='name' label='姓名'>
            <Input readOnly />
          </Form.Item>
          <Form.Item name='sex' label='性别' className={styles.needMargin}>
            <Input readOnly />
          </Form.Item>
        </div>
        <Form.Item name='phone' label='手机'>
          <Input readOnly />
        </Form.Item>
        <Form.Item name='email' label='邮箱'>
          <Input readOnly />
        </Form.Item>
      </Form>
    </div>
  );
};

export default Index;
