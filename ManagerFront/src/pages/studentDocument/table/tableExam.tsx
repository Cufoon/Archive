import React, { useEffect, useState } from 'react';
import { Button, Col, ConfigProvider, DatePicker, Form, Input, Modal, Row } from 'antd';
import zhCN from 'antd/lib/locale/zh_CN';
import styles from '@/pages/studentDocument/table/table.scss';
import { requestPost } from '@/services/request';
import { urlDocumentDefault, urlDocumentEdit, urlStudentSaveDocument } from '@/services/url';
import { DefaultParam, FormExam } from '@/pages/studentDocument/table/interface';
import { history } from 'umi';
import moment from 'moment';
import { ExclamationCircleOutlined, SaveOutlined } from '@ant-design/icons';
import { createMessageLoading } from '@/utils/message';
import { initTime } from '@/pages/studentDocument/table/table';

const { RangePicker } = DatePicker;
const { TextArea } = Input;

interface TableItem {
  className: string;
  name: string;
  sid: string;
  tName: string;
  instructorName: string;
  instructorPhone: string;
  instructorEmail: string;
  epName: string;
  epCity: string;
  date: any;
  startDate: string;
  endDate: string;
  projectName: string;
  attendance: string;
  eptSuggestions: string;
}

interface EditParam {
  category: number;
  order: number;
  formData: FormExam;
}

interface EditResponse {
  edited: boolean;
}

const getQueryString = (name: string) => {
  const query = window.location.href.split('?')[1];
  const vars = query.split('&');
  for (let i = 0; i < vars.length; i++) {
    let pair = vars[i].split('=');
    if (pair[0] === name) {
      return parseInt(pair[1]);
    }
  }
  return 1;
};

const TableExamPage: React.FC = () => {
  const [info, setInfo] = useState<TableItem>();
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [form] = Form.useForm<TableItem>();
  const [time, setTime] = useState<number>(initTime);

  const showPromiseConfirm = () => {
    Modal.confirm({
      title: '确认要提交表格吗？',
      icon: <ExclamationCircleOutlined />,
      content: '提交后无法修改',
      onOk: async () => {
        await form.submit();
      }
    });
  };

  const onFinish = async (param: TableItem) => {
    const loading = createMessageLoading('finish_loading');
    try {
      await loading.showLoading('提交表格中');
      const formData: FormExam = {
        startDate: param.date[0].format('YYYY-MM-DD').toString(),
        endDate: param.date[1].format('YYYY-MM-DD').toString(),
        projectName: param.projectName,
        attendance: param.attendance,
        eptSuggestions: param.eptSuggestions
      };
      const [res, ok] = await requestPost<EditParam, EditResponse>(urlDocumentEdit, {
        category: 2,
        order: getQueryString('order'),
        formData: formData
      });
      if (ok.succeed && res.edited) {
        loading.success('提交成功！');
        history.goBack();
      } else {
        loading.error('提交失败！');
      }
    } catch (e) {
      loading.error('提交失败！');
    }
  };

  const queryDefault = async () => {
    const loading = createMessageLoading('query_default_loading');
    try {
      await loading.showLoading('获取数据中');
      const [res, ok] = await requestPost<DefaultParam, TableItem>(urlDocumentDefault, {
        category: 2,
        order: getQueryString('order')
      });
      if (ok.succeed) {
        loading.success('获取数据成功');
        setIsLoading(false);
        setInfo({ ...res });
      } else {
        loading.error('获取数据失败');
        history.goBack();
      }
    } catch (e) {
      loading.error('获取数据失败');
      history.goBack();
    }
  };

  const save = async () => {
    const loading = createMessageLoading('save_loading');
    try {
      await loading.showLoading('保存数据中');
      const date = form.getFieldValue('date');
      const formData: FormExam = {
        startDate: date && date[0] ? date[0].format('YYYY-MM-DD').toString() : null,
        endDate: date && date[1] ? date[1].format('YYYY-MM-DD').toString() : null,
        projectName: form.getFieldValue('projectName') || null,
        attendance: form.getFieldValue('attendance') || null,
        eptSuggestions: form.getFieldValue('eptSuggestions') || null
      };
      const [res, ok] = await requestPost<EditParam, EditResponse>(urlStudentSaveDocument, {
        category: 2,
        order: getQueryString('order'),
        formData: formData
      });
      setTime(initTime);
      if (ok.succeed && res.edited) {
        loading.success('保存成功！');
      } else {
        loading.success('保存失败！');
      }
    } catch (e) {
      loading.success('保存失败！');
    }
  };

  useEffect(() => {
    queryDefault();
  }, []);

  useEffect(() => {
    const id = setInterval(() => {
      if (time > 0 && !isLoading) {
        setTime(time - 1);
      }
      if (time === 0) {
        save();
        setTime(initTime);
      }
    }, 1000);
    return () => clearInterval(id);
  }, [time, isLoading]);

  useEffect(() => {
    form.setFieldsValue({
      ...info,
      date: info
        ? [
            info.startDate ? moment(info.startDate, 'YYYY-MM-DD') : null,
            info.endDate ? moment(info.endDate, 'YYYY-MM-DD') : null
          ]
        : null
    });
  }, [info]);

  return (
    <div className={styles.background}>
      <div className={styles.content}>
        <div className={styles.title}>湖南大学学生校外实践阶段检查表</div>
        <div className={styles.toolBar}>
          <Row>
            <Button
              type={'primary'}
              className={styles.button}
              onClick={save}
              icon={<SaveOutlined />}
              disabled={isLoading}
            >
              {isLoading ? '保存' : `${time} S后自动保存`}
            </Button>
          </Row>
        </div>
        <ConfigProvider locale={zhCN}>
          <Form form={form} className={styles.table} size={'large'} onFinish={onFinish} id={'content'}>
            <Row gutter={[16, 8]}>
              <Col span={6}>
                <Form.Item label={'专业班级'} name='className' className={styles.tableItem}>
                  <Input disabled />
                </Form.Item>
              </Col>
              <Col span={6}>
                <Form.Item label={'学生姓名'} name='name' className={styles.tableItem}>
                  <Input disabled />
                </Form.Item>
              </Col>
              <Col span={6}>
                <Form.Item label={'学号'} name='sid' className={styles.tableItem}>
                  <Input disabled />
                </Form.Item>
              </Col>
              <Col span={6}>
                <Form.Item label={'院内老师'} name='tName' className={styles.tableItem}>
                  <Input disabled />
                </Form.Item>
              </Col>
            </Row>
            <Row gutter={[16, 8]}>
              <Col span={12}>
                <Form.Item label={'实习企业'} name='epName' className={styles.tableItem}>
                  <Input disabled />
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item label={'实习城市'} name='epCity' className={styles.tableItem}>
                  <Input disabled />
                </Form.Item>
              </Col>
            </Row>
            <Row gutter={[16, 8]}>
              <Col span={8}>
                <Form.Item label={'企业导师'} name='instructorName' className={styles.tableItem}>
                  <Input disabled />
                </Form.Item>
              </Col>
              <Col span={8}>
                <Form.Item label={'企业导师联系方式'} name='instructorPhone' className={styles.tableItem}>
                  <Input disabled />
                </Form.Item>
              </Col>
              <Col span={8}>
                <Form.Item label={'企业导师电子邮箱'} name='instructorEmail' className={styles.tableItem}>
                  <Input disabled />
                </Form.Item>
              </Col>
            </Row>
            <Row gutter={[16, 8]}>
              <Col span={12}>
                <Form.Item label={'阶段起止时间'} name='date' rules={[{ required: true }]} className={styles.tableItem}>
                  <RangePicker />
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item
                  label={'参与项目名称'}
                  name='projectName'
                  rules={[{ required: true }]}
                  className={styles.tableItem}
                >
                  <Input maxLength={20} />
                </Form.Item>
              </Col>
            </Row>
            <Row gutter={[16, 8]}>
              <Col span={24}>
                <Form.Item
                  label={'本阶段出勤情况'}
                  name='attendance'
                  rules={[{ required: true }]}
                  className={styles.tableItem}
                >
                  <TextArea showCount rows={4} maxLength={500} />
                </Form.Item>
              </Col>
            </Row>
            <Row gutter={[16, 8]}>
              <Col span={24}>
                <Form.Item
                  label={'本阶段企业导师意见'}
                  name='eptSuggestions'
                  rules={[{ required: true }]}
                  className={styles.tableItem}
                >
                  <TextArea showCount rows={4} maxLength={255} />
                </Form.Item>
              </Col>
            </Row>
          </Form>
          <div className={styles.buttonGroup}>
            <Button
              className={styles.button}
              onClick={() => {
                history.goBack();
              }}
            >
              取消
            </Button>
            <Button className={styles.button} type='primary' form={'content'} onClick={showPromiseConfirm}>
              提交
            </Button>
          </div>
        </ConfigProvider>
      </div>
    </div>
  );
};

export default TableExamPage;
