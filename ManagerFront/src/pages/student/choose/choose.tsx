import React, { useEffect, useState } from 'react';
import { Button, Form, Modal, Select, Steps } from 'antd';
import { ExclamationCircleOutlined, SmileOutlined } from '@ant-design/icons';
import { requestGet, requestPost } from '@/services/request';
import { urlInstructorChoose } from '@/services/url';
import { createMessageLoading } from '@/utils/message';
import StateChecker from '@/components/state-checker/state-checker';
import TitleLine from '@/components/page-header/page-header';
import styles from './choose.scss';

interface QueryChooseResponse {
  state: number;
  teachers: Teacher[];
}

interface Teacher {
  name: string;
  tid: string;
}

interface AddChooseResponse {
  succeeded: boolean;
}

interface AddChooseParam {
  tid: string;
}

const StudentChoosePage: React.FC = () => {
  const showPromiseConfirm = () => {
    Modal.confirm({
      title: '确认要选择该教师吗？',
      icon: <ExclamationCircleOutlined />,
      onOk: async () => {
        await form.submit();
      }
    });
  };

  const onFinish = async (param: AddChooseParam) => {
    const loadingMessage = createMessageLoading('student-add-choose');
    loadingMessage.showLoading('请求发送中');
    try {
      const [res, ok] = await requestPost<AddChooseParam, AddChooseResponse>(urlInstructorChoose, { tid: param.tid });
      if (ok.succeed && res?.succeeded) {
        loadingMessage.success('发送申请成功');
        queryChoose();
      } else {
        loadingMessage.error('未知错误');
      }
    } catch (e) {
      loadingMessage.error('未知错误');
    }
  };

  const { Option } = Select;
  const { Step } = Steps;

  const [choose, setChoose] = useState(0);
  const [teachers, setTeachers] = useState<Teacher[]>([]);
  const [checkState, setCheckState] = useState<boolean>(false);
  const [form] = Form.useForm<AddChooseParam>();

  const queryChoose = async () => {
    const loadingMessage = createMessageLoading('student-query-choose');
    loadingMessage.showLoading('加载中');
    const [res, ok] = await requestGet<null, QueryChooseResponse>(urlInstructorChoose);
    if (ok.succeed && res) {
      setChoose(res.state);
      setTeachers(res.teachers);
      if (res.state !== 1) {
        form.setFieldsValue({ tid: res.teachers[0].tid });
      }
    }
    loadingMessage.hideLoading();
  };

  useEffect(() => {
    checkState && queryChoose();
  }, [checkState]);

  return checkState ? (
    <div className={styles.background}>
      <div className={styles.content}>
        <TitleLine Icon={SmileOutlined} headContent='选择导师' />
        <Form form={form} onFinish={onFinish}>
          <Form.Item name='tid' rules={[{ required: true, message: '请选择教师' }]} className={styles.select}>
            <Select
              disabled={choose != 1}
              size={'large'}
              placeholder='点击选择校内导师姓名'
              showSearch={true}
              optionFilterProp={'children'}
              filterOption={(input, option) => option?.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}
            >
              {teachers.map((value: Teacher, index) => (
                <Option key={index} value={value.tid}>
                  {value.name}
                </Option>
              ))}
            </Select>
          </Form.Item>
        </Form>
        <Button type='primary' className={styles.formSubmit} disabled={choose != 1} onClick={showPromiseConfirm}>
          发送请求
        </Button>
        <div className={styles.response}>
          <Steps current={choose - 1}>
            <Step title='选择校内导师' />
            <Step title='等待校内导师通过申请' />
            <Step title='申请通过' />
          </Steps>
        </div>
      </div>
    </div>
  ) : (
    <StateChecker setCheckState={setCheckState} />
  );
};
export default StudentChoosePage;
