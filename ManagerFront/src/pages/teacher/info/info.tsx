import React, { useEffect, useState } from 'react';
import { useModel } from 'umi';
import { Button, Form, Input, Upload, Col, Row } from 'antd';
import { EditFilled, PlusOutlined } from '@ant-design/icons';
import { requestGet, requestPost } from '@/services/request';
import { urlTeacherInfoEdit, urlTeacherInfoQuery } from '@/services/url';
import { getBase64 } from '@/utils/convert';
import { createMessageLoading } from '@/utils/message';
import { checkEmail, checkPhone } from '@/utils/validator';
import PageHeader from '@/components/page-header/page-header';
import styles from './info.scss';

interface EditParam {
  name: string;
  email: string;
  phone: string;
  introduction: string;
  avatar?: string;
}

interface QueryResponse {
  id: string;
  name: string;
  email: string;
  phone: string;
  introduction: string;
  avatar?: string;
}

interface EditResponse {
  edited: boolean;
}

const Index: React.FC = () => {
  const [viewing, setViewing] = useState<boolean>(true);
  const [form] = Form.useForm<EditParam>();
  const [info, setInfo] = useState<EditParam>({} as EditParam);
  const [avatar, setAvatar] = useState<string>();
  const [originAvatar, setOriginAvatar] = useState<string>();
  const [loading, setLoading] = useState<boolean>(false);
  const { refresh, initialState, setInitialState } = useModel('@@initialState');

  const fetchData = async () => {
    const loadingMessage = createMessageLoading('teacher-info-get');
    loadingMessage.showLoading('获取个人信息');
    const [res, ok] = await requestGet<null, QueryResponse>(urlTeacherInfoQuery);
    if (ok.succeed && res) {
      setInfo({ ...res });
      setAvatar(res.avatar);
      setOriginAvatar(res.avatar);
      loadingMessage.hideLoading();
    } else {
      loadingMessage.error('获取失败');
    }
    // loadingMessage.hideLoading();
  };

  useEffect(() => {
    fetchData();
  }, []);

  useEffect(() => {
    form.setFieldsValue({ ...info });
  }, [info]);

  const toEdit = () => setViewing(false);

  const onCancelEdit = () => {
    setInfo({ ...info });
    setAvatar(originAvatar);
    setViewing(true);
  };

  const onFinish = async (param: EditParam) => {
    const loadingMessage = createMessageLoading('teacher-info-edit');
    loadingMessage.showLoading('修改中');
    const [res, ok] = await requestPost<EditParam, EditResponse>(urlTeacherInfoEdit, { ...param, avatar: avatar });
    if (ok.succeed && res) {
      if (res.edited) {
        loadingMessage.success('修改成功');
        setInfo({ ...param });
        setAvatar(avatar);
        setOriginAvatar(avatar);
        setViewing(true);
        if (initialState) {
          setInitialState({
            ...initialState,
            name: param.name,
            avatar: avatar
          });
        }
        try {
          await refresh();
        } catch (e) {
          console.log(e);
        }
      } else {
        loadingMessage.error('修改失败');
      }
    } else {
      loadingMessage.error(ok.message || '未知错误');
    }
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
      <Form
        form={form}
        size='middle'
        labelCol={{ span: 6 }}
        wrapperCol={{ span: 18 }}
        className={styles.form}
        onFinish={onFinish}
      >
        <div className={styles.formContent}>
          <div className={styles.topContent}>
            <div className={styles.leftPart}>
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
            <div className={styles.rightPart}>
              <Row justify='space-between'>
                <Col span={12}>
                  <Form.Item name='name' label='姓名' required={!viewing} rules={[{ required: true }]}>
                    <Input readOnly={viewing} />
                  </Form.Item>
                </Col>
                <Col span={9}>
                  <Form.Item name='id' label='工号' required={!viewing} rules={[{ required: true }]}>
                    <Input readOnly={viewing} />
                  </Form.Item>
                </Col>
              </Row>
              <Row justify='space-between'>
                <Col span={12}>
                  <Form.Item
                    name='email'
                    label='邮箱'
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
                <Col span={9}>
                  <Form.Item
                    name='phone'
                    label='电话'
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
                    <Input readOnly={viewing} />
                  </Form.Item>
                </Col>
              </Row>
              <Row>
                <Col span={24}>
                  <Form.Item name='introduction' label='简介' labelCol={{ span: 3 }} wrapperCol={{ span: 21 }}>
                    <Input.TextArea readOnly={viewing} maxLength={500} showCount rows={7} />
                  </Form.Item>
                </Col>
              </Row>
            </div>
          </div>
        </div>
        <div className={styles.btnGroup}>
          {viewing ? (
            <>
              <Button className={styles.button} size='middle' type='primary' onClick={toEdit}>
                编辑
              </Button>
            </>
          ) : (
            <>
              <Form.Item>
                <Button className={styles.button} size='middle' htmlType='submit' type='primary'>
                  确认修改
                </Button>
              </Form.Item>
              <Button
                className={`${styles.button} ${styles.needMargin}`}
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
      </Form>
    </div>
  );
};

export default Index;
