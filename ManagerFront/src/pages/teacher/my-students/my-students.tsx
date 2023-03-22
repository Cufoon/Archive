import PageHeader from '@/components/page-header/page-header';
import { requestGet, requestPost } from '@/services/request';
import { urlMessageSend, urlQueryAllStudents } from '@/services/url';
import { SmileFilled } from '@ant-design/icons';
import { Button, Col, Form, Input, message, Modal, Row, Select, Space, Table } from 'antd';
import React, { useEffect, useState } from 'react';
import { createMessageLoading } from '@/utils/message';
import styles from './my-students.scss';
import StudentModal from '@/components/student-modal/student-modal';

interface QueryResponse {
  studentList: StudentInfo[];
}

interface StudentInfo {
  key?: any;
  id: string;
  name: string;
  class: string;
  epName: string;
  phone: string;
}

interface FormData {
  msgTitle: string;
  msgContent: string;
  msgReceiver: string[];
}

interface SendData {
  msgTitle: string;
  msgContent: string;
  msgType: '3';
  msgRecvIds: string[];
}

interface GetData {
  sent: boolean;
}

const Index: React.FC = () => {
  const [students, setStudents] = useState<StudentInfo[]>([]);
  const [isLoadingData, setIsLoadingData] = useState<boolean>(true);
  const [modalVisible, setModalVisible] = useState<boolean>(false);
  const [form] = Form.useForm<FormData>();

  const [current, setCurrent] = useState<string>('');

  const handleChange = (value: string[]): void => {
    console.log(value);
  };

  const sendMessage = async (param: FormData) => {
    const loadingMessage = createMessageLoading('teacher-send-message').showLoading('发送中');
    const [data, ok] = await requestPost<SendData, GetData>(urlMessageSend, {
      msgTitle: param.msgTitle,
      msgContent: param.msgContent,
      msgType: '3',
      msgRecvIds: param.msgReceiver
    });
    if (ok.succeed && data?.sent) {
      loadingMessage.success('发送成功');
      setModalVisible(false);
      form.resetFields();
    }
    loadingMessage.hideLoading();
  };

  const onFinish = (param: FormData) => {
    sendMessage(param);
  };

  useEffect(() => {
    let living = true;
    const fetchData = async () => {
      const [res, ok] = await requestGet<null, QueryResponse>(urlQueryAllStudents);
      if (ok.succeed && res) {
        console.log(res.studentList);
        if (living) {
          setStudents(res.studentList);
          setIsLoadingData(false);
        }
      } else {
        console.error(ok.message);
        message.error('未知错误');
      }
    };
    fetchData();
    return () => {
      living = false;
    };
  }, []);

  const renderAction = (record: string) => {
    return (
      <>
        <Button type='link' onClick={() => setCurrent(record)}>
          详情
        </Button>
        <Button type='link' onClick={() => setModalVisible(true)}>
          发送消息
        </Button>
        <StudentModal studentId={record} visible={current == record} closeModal={() => setCurrent('')} />
      </>
    );
  };

  return (
    <div className={styles.container}>
      <PageHeader Icon={SmileFilled} headContent='我的学生' />
      <div className={styles.choices} />
      <Table dataSource={students} className={styles.table} loading={isLoadingData}>
        <Table.Column title='学号' dataIndex='id' key='id' />
        <Table.Column title='姓名' dataIndex='name' key='name' />
        <Table.Column title='专业班级' dataIndex='class' key='class' />
        <Table.Column title='实习企业' dataIndex='epName' key='epName' />
        <Table.Column title='手机号' dataIndex='phone' key='phone' />
        <Table.Column
          title='操作'
          dataIndex='id'
          key='key'
          render={(record: string) => <Space size='middle'>{renderAction(record)}</Space>}
        />
      </Table>
      <Modal
        title='发送消息'
        visible={modalVisible}
        footer={null}
        onCancel={() => setModalVisible(false)}
        className={styles.modal}
      >
        <Form form={form} onFinish={onFinish}>
          <Form.Item label='消息标题' name='msgTitle' required rules={[{ required: true, message: '请输入标题' }]}>
            <Input />
          </Form.Item>
          <Form.Item label='消息内容' name='msgContent' required rules={[{ required: true, message: '请输入内容' }]}>
            <Input.TextArea showCount rows={4} maxLength={500} />
          </Form.Item>
          <Form.Item label='接收学生' name='msgReceiver' required rules={[{ required: true, message: '请输入内容' }]}>
            <Select
              mode='multiple'
              style={{ width: '100%' }}
              placeholder='点击选择接收者'
              onChange={handleChange}
              optionLabelProp='label'
            >
              {students?.length > 0 &&
                students.map((item) => {
                  return (
                    <Select.Option value={item.id} label={item.name} key={item.id}>
                      <div className='demo-option-label-item'>
                        <span role='img' aria-label='China'>
                          {item.id}
                        </span>
                        <span>{item.name}</span>
                      </div>
                    </Select.Option>
                  );
                })}
            </Select>
          </Form.Item>
          <Row justify='center' align='middle'>
            <Col>
              <Button type='primary' danger size='middle' onClick={() => setModalVisible(false)} className={styles.btn}>
                取消
              </Button>
            </Col>
            <Col>
              <Button type='primary' size='middle' htmlType='submit' className={`${styles.btn} ${styles.leftSpace}`}>
                发送
              </Button>
            </Col>
          </Row>
        </Form>
      </Modal>
    </div>
  );
};

export default Index;
