import React, { useState, useEffect } from 'react';
import { Button, Row, Col, Table, Space, message, Modal, Form, Input, Popconfirm } from 'antd';
import { urlRelationQuery, urlRelationAdd, urlRelationDelete } from '@/services/url';
import dayjs from 'dayjs';
import styles from './relation.scss';
import { requestGet, requestPost } from '@/services/request';
import { PoweroffOutlined, ReloadOutlined, DeleteOutlined, LoadingOutlined } from '@ant-design/icons';

export interface OneApplication {
  tname: string;
  tid: string;
  sid: string;
  sname: string;
  sendTime: string;
  dealTime: string;
}

export interface OneApplicationData extends OneApplication {
  key: string;
}

export interface QueryData {
  relationList: OneApplication[];
}

export interface HandleRelationSend {
  teacherID: string;
  studentID: string;
}

export interface HandleRelationGet {
  handle: boolean;
}

export interface FormData {
  teaID: string;
  stuID: string;
}

const DealApplyPage: React.FC = () => {
  const [tableData, setTableData] = useState<OneApplicationData[]>([]);
  const [isLoadTableData, setIsLoadTableData] = useState<boolean>(true);
  const [loadingState, setLoadingState] = useState<(1 | 2 | 3)[]>([]);
  const [isRefresh, setIsRefresh] = useState<boolean>(false);
  const [visible, setVisible] = useState<boolean>(false);
  const [form] = Form.useForm<FormData>();

  const fetchRelationDeal = async (teacherID: string, studentID: string, add: boolean) => {
    return await requestPost<HandleRelationSend, HandleRelationGet>(add ? urlRelationAdd : urlRelationDelete, {
      teacherID: teacherID,
      studentID: studentID
    });
  };

  const deleteRelation = async (teaID: string, stuID: string, index: number) => {
    try {
      const [data, ok] = await fetchRelationDeal(teaID, stuID, false);
      if (ok.succeed && data?.handle) {
        setTableData((prevState) => {
          prevState.splice(index, 1);
          return [...prevState];
        });
        message.success('删除成功');
      }
    } catch (e) {
      console.log(e);
    }
    // todo: 同步问题
    setLoadingState((prevState) => {
      prevState[index] = 1;
      return [...prevState];
    });
  };

  const addRelation = async (teaID: string, stuID: string): Promise<boolean> => {
    const [data, ok] = await fetchRelationDeal(teaID, stuID, true);
    if (ok.succeed && data?.handle) {
      message.success('添加成功');
      await init();
      return true;
    }
    return false;
  };

  const onFinish = async (e: FormData) => {
    const ok = await addRelation(e.teaID, e.stuID);
    if (ok) {
      setVisible(false);
      form.resetFields();
    } else {
      message.error('添加失败');
    }
  };

  const reFresh = async () => {
    setIsRefresh(true);
    setIsLoadTableData(true);
    await init();
  };

  const init = async () => {
    const [data, ok] = await requestGet<unknown, QueryData>(urlRelationQuery);
    if (ok.succeed && data) {
      console.log(data);
      if (data.relationList?.length > 0) {
        setTableData(
          data.relationList.map((value) => {
            return {
              ...value,
              key: `${value.tid}${value.sid}`,
              sendTime: dayjs(value.sendTime).format('YYYY-MM-DD hh:MM'),
              dealTime: dayjs(value.dealTime).format('YYYY-MM-DD hh:MM')
            };
          })
        );
        setLoadingState(Array(data.relationList.length).fill(1));
      }
    } else {
      message.error(ok.message || '未知错误');
    }
    setTimeout(() => {
      setIsLoadTableData(false);
      setIsRefresh(false);
    }, 200);
  };

  useEffect(() => {
    init();
  }, []);

  return (
    <div className={styles.outer}>
      <Row justify='space-between'>
        <Col>
          <Space>
            <Button
              type='primary'
              style={{ borderRadius: '8px' }}
              onClick={() => {
                setVisible(true);
              }}
            >
              添加关系
            </Button>
          </Space>
        </Col>
        <Col>
          <Button
            type='primary'
            style={{ borderRadius: '8px' }}
            icon={isRefresh ? <PoweroffOutlined /> : <ReloadOutlined />}
            loading={isRefresh}
            onClick={reFresh}
          >
            刷新
          </Button>
        </Col>
      </Row>
      <Table dataSource={tableData} style={{ marginTop: '20px' }} loading={isLoadTableData}>
        <Table.Column title='工号' key='tid' dataIndex='tid' />
        <Table.Column title='老师姓名' key='tname' dataIndex='tname' />
        <Table.Column title='学号' key='sid' dataIndex='sid' />
        <Table.Column title='学生姓名' key='sname' dataIndex='sname' />
        <Table.Column title='申请时间' key='sendTime' dataIndex='sendTime' />
        <Table.Column title='同意时间' key='dealTime' dataIndex='dealTime' />
        <Table.Column
          title='操作'
          key='action'
          render={(text: OneApplicationData, record: OneApplicationData, index) => {
            return (
              <Popconfirm
                title='确认删除此关系?'
                style={{ color: 'red' }}
                okText='确认'
                cancelText='取消'
                onConfirm={() => {
                  setLoadingState((prevState) => {
                    prevState[index] = 3;
                    return [...prevState];
                  });
                  return deleteRelation(record.tid, record.sid, index);
                }}
              >
                <Button
                  type='text'
                  style={{ borderRadius: '8px' }}
                  icon={loadingState[index] === 3 ? <LoadingOutlined /> : <DeleteOutlined />}
                  danger
                >
                  删除关系
                </Button>
              </Popconfirm>
            );
          }}
        />
      </Table>
      <Modal
        title='添加指导关系'
        centered
        visible={visible}
        onCancel={() => {
          setVisible(false);
        }}
        width={400}
        destroyOnClose
        footer={[]}
      >
        <Form form={form} style={styles.form} layout='horizontal' onFinish={onFinish}>
          <Form.Item label='老师工号' name='teaID' rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item label='学生学号' name='stuID' rules={[{ required: true }]}>
            <Input />
          </Form.Item>

          <div className={styles.buttonGroup}>
            <Space size='middle'>
              <Button
                type='primary'
                style={{ borderRadius: '8px' }}
                onClick={() => {
                  setVisible(false);
                }}
              >
                取消
              </Button>
              <Button type='primary' htmlType='submit' style={{ borderRadius: '8px' }}>
                添加
              </Button>
            </Space>
          </div>
        </Form>
      </Modal>
    </div>
  );
};

export default DealApplyPage;
