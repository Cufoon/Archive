import React, { useEffect, useState } from 'react';
import {
  Button,
  Col,
  Collapse,
  Form,
  Input,
  message,
  Modal,
  Radio,
  Row,
  Select,
  Space,
  Table,
  Tag,
  Upload
} from 'antd';
import { requestGet, requestPost } from '@/services/request';
import { urlAdminAdd, urlAdminBan, urlAdminExcel, urlAdminQuery, urlAdminStart } from '@/services/url';
import TextArea from 'antd/es/input/TextArea';
import { ExclamationCircleOutlined, UploadOutlined } from '@ant-design/icons';
import { request } from 'umi';
import { createMessageLoading } from '@/utils/message';
import styles from './user.scss';
import { checkEmail, checkPhone } from '@/utils/validator';

interface User {
  key?: number;
  id: string;
  type: number;
  state: number;
  name: string;
  avatar: string;
  sex: string;
  className: string;
  phone: string;
  email: string;
}

interface NewUser {
  userId: string;
  password: string;
  type: number;
  state: number;
  name: string;
  sex: string;
  className: string;
  phone: string;
  email: string;
  introduction: string;
}

interface QueryResponse {
  formData: User[];
}

interface AddResponse {
  increased: boolean;
}

interface UploadUser {
  id: string;
  name: string;
  succeed: string;
}

interface HandleStateResponse {
  ban: boolean;
}

const UserManagePage: React.FC = () => {
  const [user, setUser] = useState<User[]>();
  const [type, setType] = useState<number>(1);
  const [addVisible, setAddVisible] = useState<boolean>(false);
  const [importVisible, setImportVisible] = useState<boolean>(false);
  const [isLoadingTableData, setIsLoadingTableData] = useState<boolean>(true);
  const [isLoadingUpload, setIsLoadingUpload] = useState<boolean>(false);
  const [file, setFile] = useState<any>();
  const [createType, setCreateType] = useState<string>('Student');
  const [form] = Form.useForm<NewUser>();
  const [importUser, setImportUser] = useState<UploadUser[]>([]);

  const showPromiseConfirm = () => {
    Modal.confirm({
      title: '确认要新增用户吗？',
      icon: <ExclamationCircleOutlined />,
      onOk: async () => {
        await form.submit();
      }
    });
  };

  const queryUser = async (type: number) => {
    setIsLoadingTableData(true);
    try {
      const [res, ok] = await requestGet<null, QueryResponse>(`${urlAdminQuery}?type=${type}`);
      if (ok.succeed) {
        res?.formData.map((value, index) => {
          value.key = index;
        });
        setUser(res?.formData);
        setIsLoadingTableData(false);
      }
    } catch (e) {
      message.error('获取失败');
    }
  };

  const { Option } = Select;
  const { Panel } = Collapse;

  const handleAddClick = () => {
    setAddVisible(true);
  };

  const handleImportClick = () => {
    setImportVisible(true);
  };

  const handleTypeChange = async (e: any) => {
    await queryUser(e.target.value);
    setType(e.target.value);
  };

  const onFinish = async (param: NewUser) => {
    try {
      const [res, ok] = await requestPost<NewUser, AddResponse>(urlAdminAdd, {
        ...param
      });
      if (ok.succeed && res?.increased) {
        setAddVisible(false);
        message.success('添加成功');
        form.resetFields();
        queryUser(type);
      } else {
        message.error('添加失败');
      }
    } catch (e) {
      message.error('添加失败');
    }
  };

  const handleChange = (info: any) => {
    setImportUser([]);
    if (info.file.status !== 'uploading') {
      console.log(info.file, info.fileList);
    }
    if (info.file.status === 'done') {
      message.success(`${info.file.name} file uploaded successfully`);
    } else if (info.file.status === 'error') {
      message.error(`${info.file.name} file upload failed.`);
    }
  };

  const handleUpload = async () => {
    setIsLoadingUpload(true);
    const formData = new FormData();
    formData.append('file', file);
    const loading = createMessageLoading('uploading_loading');
    await loading.showLoading('上传文件中');
    await request(urlAdminExcel, {
      method: 'POST',
      credentials: 'include',
      data: formData
    })
      .then((res) => {
        setImportUser(res.data.userVOList);
        loading.success('上传成功');
        setIsLoadingUpload(false);
      })
      .catch((e) => {
        loading.error('上传失败');
        setIsLoadingUpload(false);
        console.log(e);
      });
  };

  const handleState = async (record: string, state: number) => {
    try {
      const [res, ok] = await requestPost<any, HandleStateResponse>(state === 1 ? urlAdminBan : urlAdminStart, {
        id: record
      });
      if (ok.succeed && res?.ban) {
        message.success('修改成功');
      } else {
        message.error('修改失败');
      }
    } catch (e) {
      message.error('修改失败');
    }
  };

  const renderAction = (record: string) => {
    const current = user?.find((e: User) => e.id === record);
    const state = current ? current.state : 0;
    const name = current ? current.name : '';
    return (
      <div>
        <Button
          type={'link'}
          onClick={() => {
            Modal.confirm({
              title: state === 1 ? `确认要禁用 ${name} 吗？` : `确认要启用 ${name} 吗？`,
              icon: <ExclamationCircleOutlined />,
              onOk: async () => {
                await handleState(record, state);
                queryUser(type);
              }
            });
          }}
          style={{ paddingLeft: '0px' }}
        >
          {current?.state === 1 ? '禁用' : '启用'}
        </Button>
      </div>
    );
  };

  useEffect(() => {
    queryUser(1);
  }, []);

  return (
    <div className={styles.background}>
      <div className={styles.content}>
        <div className={styles.title}>用户查看</div>
        <div className={styles.toolBar}>
          <Row justify='space-between'>
            <Col>
              <Radio.Group value={type} onChange={handleTypeChange} disabled={isLoadingTableData}>
                <Radio.Button value={1} style={{ borderRadius: '8px 0 0 8px' }}>
                  学生
                </Radio.Button>
                <Radio.Button value={2} style={{ borderRadius: '0 8px 8px 0' }}>
                  教师
                </Radio.Button>
              </Radio.Group>
            </Col>
            <Col>
              <Button
                type='primary'
                onClick={handleAddClick}
                disabled={isLoadingTableData}
                style={{ marginRight: '20px', borderRadius: '8px' }}
              >
                添加用户
              </Button>
              <Button
                type='primary'
                onClick={handleImportClick}
                disabled={isLoadingTableData}
                style={{ borderRadius: '8px' }}
              >
                批量导入
              </Button>
            </Col>
          </Row>
        </div>
        <Table dataSource={user} style={{ marginTop: '20px' }} className={styles.table} loading={isLoadingTableData}>
          <Table.Column title='账号id' dataIndex='id' key='id' />
          <Table.Column
            title='状态'
            dataIndex='state'
            key='id'
            render={(record: number) => {
              return record ? <Tag color={'green'}>启用</Tag> : <Tag color={'red'}>禁用</Tag>;
            }}
          />
          <Table.Column title='姓名' dataIndex='name' key='id' />
          <Table.Column
            title='性别'
            dataIndex='sex'
            key='id'
            render={(record: string) => {
              switch (record) {
                case 'M':
                  return '男';
                case 'F':
                  return '女';
                default:
                  return '';
              }
            }}
          />
          <Table.Column title={type == 1 ? '班级' : '学院'} dataIndex='className' key='id' />
          <Table.Column title='手机' dataIndex='phone' key='id' />
          <Table.Column title='邮箱' dataIndex='email' key='id' />
          <Table.Column
            title='操作'
            dataIndex='id'
            key='id'
            render={(record: string) => <Space>{renderAction(record)}</Space>}
          />
        </Table>

        <Modal
          title={'批量导入'}
          centered
          visible={importVisible}
          onCancel={() => {
            setFile(null);
            setImportVisible(false);
            setImportUser([]);
          }}
          width={700}
          destroyOnClose
          footer={[]}
        >
          <Upload
            onChange={handleChange}
            showUploadList={false}
            accept={'.xls,.xlsx'}
            beforeUpload={(file) => {
              setFile(file);
              return false;
            }}
          >
            <Button icon={<UploadOutlined />}>{file ? file.name : '上传EXCEL文件'}</Button>
          </Upload>
          <Button type='primary' onClick={handleUpload} style={{ marginLeft: '10px' }}>
            导入
          </Button>
          <Table dataSource={importUser} style={{ marginTop: '20px' }} loading={isLoadingUpload}>
            <Table.Column dataIndex='id' title='账号id' />
            <Table.Column dataIndex='name' title='姓名' />
            <Table.Column
              dataIndex='succeed'
              title='导入状态'
              render={(record: boolean) => {
                if (record) {
                  return '成功';
                } else {
                  return '失败';
                }
              }}
            />
          </Table>
        </Modal>
        <Modal
          title={'添加新用户'}
          centered
          visible={addVisible}
          onCancel={() => {
            setAddVisible(false);
          }}
          width={700}
          destroyOnClose
          footer={[]}
        >
          <Form style={styles.form} layout={'horizontal'} onFinish={onFinish} form={form}>
            <Row gutter={[16, 8]}>
              <Col span={12}>
                <Form.Item label={'账号id'} name='userId' rules={[{ required: true }]}>
                  <Input />
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item label={'姓名'} name='name' rules={[{ required: true }]}>
                  <Input />
                </Form.Item>
              </Col>
            </Row>
            <Row gutter={[16, 8]}>
              <Col span={12}>
                <Form.Item label={'账号类型'} name='type' rules={[{ required: true }]}>
                  <Select
                    style={{ width: 120 }}
                    onChange={(value) => {
                      if (value == 1) {
                        setCreateType('Student');
                      } else {
                        setCreateType('Teacher');
                      }
                    }}
                  >
                    <Option value={1}>学生</Option>
                    <Option value={2}>教师</Option>
                  </Select>
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item label={'账号状态'} name='state' rules={[{ required: true }]}>
                  <Select style={{ width: 120 }}>
                    <Option value={1}>启用</Option>
                    <Option value={0}>禁用</Option>
                  </Select>
                </Form.Item>
              </Col>
            </Row>
            <Collapse accordion>
              <Panel header={'额外信息'} key={'1'}>
                <Form.Item label={'密码'} name='password'>
                  <Input placeholder={'默认为账号id'} />
                </Form.Item>
                <Row gutter={[16, 8]}>
                  <Col span={8}>
                    <Form.Item label={'性别'} name='sex'>
                      <Select style={{ width: 120 }}>
                        <Option value='M'>男</Option>
                        <Option value='F'>女</Option>
                      </Select>
                    </Form.Item>
                  </Col>
                  <Col span={16}>
                    <Form.Item label={createType == 'Student' ? '班级' : '学院'} name='className'>
                      <Input />
                    </Form.Item>
                  </Col>
                </Row>
                <Row gutter={[16, 8]}>
                  <Col span={12}>
                    <Form.Item
                      label={'电话'}
                      name='phone'
                      rules={[
                        {
                          max: 11
                        },
                        {
                          validator: async (rule, value) => {
                            if (value != null) {
                              checkPhone(value);
                            }
                          }
                        }
                      ]}
                    >
                      <Input />
                    </Form.Item>
                  </Col>
                  <Col span={12}>
                    <Form.Item
                      label={'邮箱'}
                      name='email'
                      rules={[
                        {
                          validator: async (rule, value) => {
                            if (value != null) {
                              checkEmail(value);
                            }
                          }
                        }
                      ]}
                    >
                      <Input />
                    </Form.Item>
                  </Col>
                </Row>
                <Form.Item label={'个人介绍'} name='introduction'>
                  <TextArea showCount rows={4} maxLength={500} />
                </Form.Item>
              </Panel>
            </Collapse>
            <div className={styles.buttonGroup}>
              <Button className={styles.button} type='primary' onClick={showPromiseConfirm}>
                提交
              </Button>
              <Button
                className={styles.button}
                onClick={() => {
                  setAddVisible(false);
                }}
              >
                取消
              </Button>
            </div>
          </Form>
        </Modal>
      </div>
    </div>
  );
};

export default UserManagePage;
