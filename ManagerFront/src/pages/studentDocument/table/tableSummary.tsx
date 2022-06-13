import React, { useEffect, useState } from 'react';
import { Button, ConfigProvider, Form, Input, Modal, Row } from 'antd';
import zhCN from 'antd/lib/locale/zh_CN';
import styles from '@/pages/studentDocument/table/table.scss';
import { requestPost } from '@/services/request';
import { urlDocumentDefault, urlDocumentEdit, urlStudentSaveDocument } from '@/services/url';
import { DefaultParam, FormSummary } from '@/pages/studentDocument/table/interface';
import { history } from 'umi';
import { ExclamationCircleOutlined, SaveOutlined } from '@ant-design/icons';
import { createMessageLoading } from '@/utils/message';
import { initTime } from '@/pages/studentDocument/table/table';

const { TextArea } = Input;

interface TableItem {
  summaryReport: string;
}

interface EditParam {
  category: number;
  order: number;
  formData: FormSummary;
}

export interface EditResponse {
  edited: boolean;
}

const TableSummarytPage: React.FC = () => {
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

  const save = async () => {
    const loading = createMessageLoading('save_loading');
    try {
      await loading.showLoading('保存数据中');
      const formData: FormSummary = {
        summaryReport: form.getFieldValue('summaryReport') || null
      };
      const [res, ok] = await requestPost<EditParam, EditResponse>(urlStudentSaveDocument, {
        category: 5,
        order: 1,
        formData: formData
      });
      setTime(initTime);
      if (ok.succeed && res.edited) {
        loading.success('保存成功！');
      } else {
        loading.error('保存失败！');
      }
    } catch (e) {
      loading.error('保存失败！');
    }
  };

  const onFinish = async (param: TableItem) => {
    const loading = createMessageLoading('finish_loading');
    try {
      await loading.showLoading('提交表格中');
      const formData: FormSummary = {
        summaryReport: param.summaryReport
      };
      const [res, ok] = await requestPost<EditParam, EditResponse>(urlDocumentEdit, {
        category: 5,
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

  const queryDefault = async () => {
    const loading = createMessageLoading('query_default_loading');
    try {
      await loading.showLoading('获取数据中');
      const [res, ok] = await requestPost<DefaultParam, TableItem>(urlDocumentDefault, {
        category: 5,
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
    form.setFieldsValue({ ...info });
  }, [info]);

  return (
    <div className={styles.background}>
      <div className={styles.content}>
        <div className={styles.title}>湖南大学学生校外实践总结报告</div>
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
            <Form.Item
              label={'总结报告'}
              name='summaryReport'
              className={styles.tableItem}
              rules={[{ required: true }]}
            >
              <TextArea showCount rows={4} maxLength={1500} />
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

export default TableSummarytPage;
