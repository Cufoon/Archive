import React, { useEffect, useState } from 'react';
import { Button, Card, Col, ConfigProvider, DatePicker, Form, Input, Modal, Radio, Row } from 'antd';
import zhCN from 'antd/lib/locale/zh_CN';
import styles from '@/pages/studentDocument/table/table.scss';
import { requestPost } from '@/services/request';
import { urlDocumentDefault, urlDocumentEdit, urlStudentSaveDocument } from '@/services/url';
import { DefaultParam, FormAppraisal } from '@/pages/studentDocument/table/interface';
import { history } from 'umi';
import { ExclamationCircleOutlined, SaveOutlined } from '@ant-design/icons';
import { createMessageLoading } from '@/utils/message';
import moment from 'moment';
import { initTime } from '@/pages/studentDocument/table/table';

const { RangePicker } = DatePicker;
const { TextArea } = Input;

interface TableItem {
  name: string;
  sid: string;
  epName: string;
  epCity: string;
  instructorName: string;
  instructorPhone: string;
  date: any;
  startDate: string;
  endDate: string;
  projectName: string;
  eptEvaluation1: number;
  eptEvaluation2: number;
  eptEvaluation3: number;
  eptEvaluation4: number;
  eptEvaluation5: number;
  eptEvaluation6: number;
  eptEvaluation7: number;
  eptEvaluation8: number;
  eptEvaluation9: number;
  eptSuggestions: string;
}

interface EditParam {
  category: number;
  order: number;
  formData: FormAppraisal;
}

interface EditResponse {
  edited: boolean;
}

const TableAppraisalPage: React.FC = (props) => {
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
      const formData: FormAppraisal = {
        startDate: param.date[0].format('YYYY-MM-DD').toString(),
        endDate: param.date[1].format('YYYY-MM-DD').toString(),
        projectName: param.projectName,
        eptEvaluation1: param.eptEvaluation1,
        eptEvaluation2: param.eptEvaluation2,
        eptEvaluation3: param.eptEvaluation3,
        eptEvaluation4: param.eptEvaluation4,
        eptEvaluation5: param.eptEvaluation5,
        eptEvaluation6: param.eptEvaluation6,
        eptEvaluation7: param.eptEvaluation7,
        eptEvaluation8: param.eptEvaluation8,
        eptEvaluation9: param.eptEvaluation9,
        eptSuggestions: param.eptSuggestions
      };
      const [res, ok] = await requestPost<EditParam, EditResponse>(urlDocumentEdit, {
        category: 4,
        order: 1,
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

  const radioGroup = (
    <Radio.Group className={styles.radioGroup}>
      <Radio value={5}>非常满意</Radio>
      <Radio value={4}>满意</Radio>
      <Radio value={3}>普通</Radio>
      <Radio value={2}>不满意</Radio>
      <Radio value={1}>非常不满意</Radio>
    </Radio.Group>
  );

  const queryDefault = async () => {
    const loading = createMessageLoading('query_default_loading');
    try {
      await loading.showLoading('获取数据中');
      const [res, ok] = await requestPost<DefaultParam, TableItem>(urlDocumentDefault, {
        category: 4,
        order: 1
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
    await loading.showLoading('保存数据中');
    const date = form.getFieldValue('date');
    const formData: FormAppraisal = {
      startDate: date && date[0] ? date[0].format('YYYY-MM-DD').toString() : null,
      endDate: date && date[1] ? date[1].format('YYYY-MM-DD').toString() : null,
      projectName: form.getFieldValue('projectName') || null,
      eptEvaluation1: form.getFieldValue('eptEvaluation1') || null,
      eptEvaluation2: form.getFieldValue('eptEvaluation2') || null,
      eptEvaluation3: form.getFieldValue('eptEvaluation3') || null,
      eptEvaluation4: form.getFieldValue('eptEvaluation4') || null,
      eptEvaluation5: form.getFieldValue('eptEvaluation5') || null,
      eptEvaluation6: form.getFieldValue('eptEvaluation6') || null,
      eptEvaluation7: form.getFieldValue('eptEvaluation7') || null,
      eptEvaluation8: form.getFieldValue('eptEvaluation8') || null,
      eptEvaluation9: form.getFieldValue('eptEvaluation9') || null,
      eptSuggestions: form.getFieldValue('eptSuggestions') || null
    };
    const [res, ok] = await requestPost<EditParam, EditResponse>(urlStudentSaveDocument, {
      category: 4,
      order: 1,
      formData: formData
    });
    setTime(initTime);
    if (ok.succeed) {
      if (res.edited) {
        loading.success('保存成功！');
      } else {
        loading.error('保存失败！');
      }
    } else {
      loading.error('保存失败！');
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
        <div className={styles.title}>湖南大学学生校外实践企业导师评价表</div>
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
              <Col span={12}>
                <Form.Item label={'学生姓名'} name='name' className={styles.tableItem}>
                  <Input disabled />
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item label={'学号'} name='sid' className={styles.tableItem}>
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
              <Col span={12}>
                <Form.Item label={'企业导师'} name='instructorName' className={styles.tableItem}>
                  <Input disabled />
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item label={'企业导师联系方式'} name='instructorPhone' className={styles.tableItem}>
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
            <Form.Item label={'企业导师对该生本阶段工作评价'} className={styles.tableItem}>
              <Card className={styles.radioGroup}>
                <Form.Item
                  label={'遵守公司管理制度'}
                  name='eptEvaluation1'
                  rules={[{ required: true, message: '请选择一项' }]}
                  className={styles.tableItem}
                >
                  {radioGroup}
                </Form.Item>
                <Form.Item
                  label={'工作态度与责任感'}
                  name='eptEvaluation2'
                  rules={[{ required: true, message: '请选择一项' }]}
                  className={styles.tableItem}
                >
                  {radioGroup}
                </Form.Item>
                <Form.Item
                  label={'沟通与团队合作能力'}
                  name='eptEvaluation3'
                  rules={[{ required: true, message: '请选择一项' }]}
                  className={styles.tableItem}
                >
                  {radioGroup}
                </Form.Item>
                <Form.Item
                  label={'专业知识与工作能力'}
                  name='eptEvaluation4'
                  rules={[{ required: true, message: '请选择一项' }]}
                  className={styles.tableItem}
                >
                  {radioGroup}
                </Form.Item>
                <Form.Item
                  label={'自信与自主学习能力'}
                  name='eptEvaluation5'
                  rules={[{ required: true, message: '请选择一项' }]}
                  className={styles.tableItem}
                >
                  {radioGroup}
                </Form.Item>
                <Form.Item
                  label={'情绪表现与抗压能力'}
                  name='eptEvaluation6'
                  rules={[{ required: true, message: '请选择一项' }]}
                  className={styles.tableItem}
                >
                  {radioGroup}
                </Form.Item>
                <Form.Item
                  label={'解决问题能力'}
                  name='eptEvaluation7'
                  rules={[{ required: true, message: '请选择一项' }]}
                  className={styles.tableItem}
                >
                  {radioGroup}
                </Form.Item>
                <Form.Item
                  label={'创新思维能力'}
                  name='eptEvaluation8'
                  rules={[{ required: true, message: '请选择一项' }]}
                  className={styles.tableItem}
                >
                  {radioGroup}
                </Form.Item>
                <Form.Item
                  label={'整体工作的表现'}
                  name='eptEvaluation9'
                  rules={[{ required: true, message: '请选择一项' }]}
                  className={styles.tableItem}
                >
                  {radioGroup}
                </Form.Item>
              </Card>
            </Form.Item>
            <Form.Item
              label={'企业导师建议'}
              name='eptSuggestions'
              rules={[{ required: true }]}
              className={styles.tableItem}
            >
              <TextArea showCount rows={4} maxLength={500} />
            </Form.Item>
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

export default TableAppraisalPage;
