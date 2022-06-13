import React, { useEffect, useState, useCallback } from 'react';
import { useModel } from 'umi';
import { Button, Form, Input, Select, Upload, Row, Col } from 'antd';
import { EditFilled, PlusOutlined } from '@ant-design/icons';
import { requestGet, requestPost } from '@/services/request';
import { urlStudentInfoEdit, urlStudentInfoQuery } from '@/services/url';
import { getBase64 } from '@/utils/convert';
import { createMessageLoading } from '@/utils/message';
import { checkPhone, checkEmail } from '@/utils/validator';
import PageHeader from '@/components/page-header/page-header';
import { EditParam, EditResponse, Info, InfoResponse } from './interface';
import styles from './info.scss';

const Index: React.FC = () => {
  const [viewing, setViewing] = useState<boolean>(true);
  const [info, setInfo] = useState<Info>({} as Info);
  const [form] = Form.useForm<EditParam>();
  const [avatar, setAvatar] = useState<string>();
  const [originAvatar, setOriginAvatar] = useState<string>();
  const [loading, setLoading] = useState<boolean>(false);
  const { refresh, initialState, setInitialState } = useModel('@@initialState');

  const fetchData = useCallback(async () => {
    const loadingMessage = createMessageLoading('student-info');
    loadingMessage.showLoading('获取个人信息');
    const [res, ok] = await requestGet<null, InfoResponse>(urlStudentInfoQuery);
    if (ok.succeed) {
      if (res?.state) {
        setInfo({ ...res.info });
        setAvatar(res.info.avatar);
        setOriginAvatar(res.info.avatar);
        refresh();
      }
    } else {
      ok.message && loadingMessage.error(ok.message);
    }
    loadingMessage.hideLoading();
  }, []);

  useEffect(() => {
    fetchData();
  }, []);

  useEffect(() => {
    form.setFieldsValue({ ...info });
  }, [info]);

  const toEdit = () => setViewing(false);

  const onCancelEdit = () => {
    form.setFieldsValue({ ...info });
    setViewing(true);
    setAvatar(originAvatar);
  };

  const onCompleteEdit = () => {
    setViewing(true);
    fetchData();
  };

  const handleEditInfo = async (param: EditParam) => {
    const loadingMessage = createMessageLoading('student-change-info').showLoading('修改中');
    const [res, ok] = await requestPost<EditParam, EditResponse>(urlStudentInfoEdit, {
      studentName: param.studentName,
      sex: param.sex,
      className: param.className,
      phoneNumber: param.phoneNumber,
      email: param.email,
      introduction: param.introduction,
      avatar: avatar
    });
    if (ok.succeed) {
      if (res?.modified) {
        loadingMessage.success('修改成功');
        setInfo((e) => ({
          studentName: param.studentName,
          sex: param.sex,
          className: param.className,
          phoneNumber: param.phoneNumber,
          email: param.email,
          introduction: param.introduction,
          studentId: e.studentId
        }));
        if (initialState) {
          setInitialState({
            ...initialState,
            name: param.studentName,
            avatar: avatar
          });
        }

        onCompleteEdit();
      } else {
        loadingMessage.info('修改失败');
      }
    } else {
      loadingMessage.error(ok.message || '未知错误');
    }
    loadingMessage.hideLoading();
  };

  const onFinish = async () => {
    form.validateFields().then(() => handleEditInfo(form.getFieldsValue()));
  };

  const handleChange = (info: any) => {
    if (info.file.status === 'uploading') {
      setLoading(true);
      return;
    }
    if (info.file.status === 'done') {
      getBase64(info.file.originFileObj, (imageUrl: string | undefined) => {
        setAvatar(imageUrl);
        setLoading(false);
      });
    }
  };

  return (
    <div className={styles.container}>
      <PageHeader Icon={EditFilled} headContent='个人信息' />
      <div className={styles.topPart}>
        <div className={styles.leftCol}>
          <Upload
            accept='.png,.jpg,.webp'
            disabled={viewing}
            showUploadList={false}
            onChange={handleChange}
            action={(file) => {
              getBase64(file, (imageUrl: string | undefined) => {
                setAvatar(imageUrl);
                setLoading(false);
              });
              return '';
            }}
          >
            {(avatar && (
              <div
                className={styles.avatarWrapper}
                style={{
                  backgroundImage: `url(${avatar})`,
                  backgroundColor: 'transparent'
                }}
              />
            )) || (
              <>
                <div className={styles.avatarWrapper}>
                  <PlusOutlined className={styles.avatar} />
                </div>
              </>
            )}
          </Upload>
        </div>
        <div className={styles.rightCol}>
          <Form form={form} size='middle'>
            <Row>
              <Col span={12}>
                <Form.Item name='studentName' label={'姓名'} required={!viewing} rules={[{ required: true }]}>
                  <Input readOnly={viewing} />
                </Form.Item>
              </Col>
              <Col span={10} push={2}>
                <Form.Item
                  name='sex'
                  label={'性别'}
                  wrapperCol={{ span: viewing ? 8 : 7 }}
                  required={!viewing}
                  rules={[{ required: true }]}
                >
                  <Select disabled={viewing} placeholder={'?'}>
                    <Select.Option value='M'>男</Select.Option>
                    <Select.Option value='F'>女</Select.Option>
                  </Select>
                </Form.Item>
              </Col>
            </Row>
            <Row>
              <Col span={12}>
                <Form.Item name='className' label={'班级'} required={!viewing} rules={[{ required: true }]}>
                  <Input readOnly={viewing} />
                </Form.Item>
              </Col>
              <Col span={10} push={2}>
                <Form.Item name='studentId' label={'学号'} required={!viewing} rules={[{ required: true }]}>
                  <Input disabled />
                </Form.Item>
              </Col>
            </Row>
            <Row>
              <Col span={12}>
                <Form.Item
                  name='email'
                  label={'邮箱'}
                  required={!viewing}
                  validateFirst
                  rules={[
                    {
                      required: true
                    },
                    {
                      validator: async (rule, value) => {
                        checkEmail(value);
                      }
                    }
                  ]}
                >
                  <Input readOnly={viewing} />
                </Form.Item>
              </Col>
              <Col span={10} push={2}>
                <Form.Item
                  name='phoneNumber'
                  label={'手机'}
                  required={!viewing}
                  validateFirst
                  rules={[
                    {
                      required: true,
                      max: 11
                    },
                    {
                      validator: async (rule, value) => {
                        checkPhone(value);
                      }
                    }
                  ]}
                >
                  <Input readOnly={viewing} maxLength={11} />
                </Form.Item>
              </Col>
            </Row>
            <Row>
              <Col span={24}>
                <Form.Item name='introduction' label={'简介'} wrapperCol={{ span: 22 }} labelCol={{ span: 2 }}>
                  <Input.TextArea rows={5} maxLength={500} showCount placeholder='介绍自己' readOnly={viewing} />
                </Form.Item>
              </Col>
            </Row>
          </Form>
          <div className={styles.btnGroup}>
            {viewing ? (
              <>
                <Button className={styles.btn} size='middle' type='primary' onClick={toEdit}>
                  编辑
                </Button>
              </>
            ) : (
              <>
                <Button className={styles.btn} onClick={onFinish} size='middle' htmlType='submit' type='primary'>
                  确认修改
                </Button>
                <Button
                  className={`${styles.btn} ${styles.needMargin}`}
                  size='middle'
                  type='primary'
                  danger
                  onClick={onCancelEdit}
                >
                  取消修改
                </Button>
              </>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Index;
