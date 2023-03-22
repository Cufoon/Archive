import React, { useState, useEffect } from 'react';
import { urlInternshipInfoEdit, urlInternshipInfoQuery } from '@/services/url';
import { requestGet, requestPost } from '@/services/request';
import { createMessageLoading } from '@/utils/message';
import { getBase64 } from '@/utils/convert';
import { checkEmail, checkPhone } from '@/utils/validator';
import { findCityCodeData } from '@/utils/finder';
import { EditOutlined, PlusOutlined, ExclamationCircleOutlined } from '@ant-design/icons';
import {
  Button,
  Form,
  Input,
  Select,
  Modal,
  message as antMessage,
  Upload,
  Row,
  Col,
  Space,
  Tooltip,
  Cascader,
  message
} from 'antd';
import TitleLine from '@/components/page-header/page-header';
import StateChecker from '@/components/state-checker/state-checker';
import { cityListOption } from '@/utils/config';
import { FormData, InternPostData, InternPostReturn, InternshipGetData } from './interface';
import styles from './internship.scss';

const InternshipPage: React.FC = () => {
  const [hasEdited, setHasEdited] = useState<boolean>(true);
  const [form] = Form.useForm<FormData>();
  const [offerImg, setOfferImg] = useState<string>();
  const [originOffer, setOriginOffer] = useState<string>();
  const [loading, setLoading] = useState<boolean>(true);
  const [checkState, setCheckState] = useState<boolean>(false);
  const [cityName, setCityName] = useState<string>();

  const showPromiseConfirm = () => {
    Modal.confirm({
      title: '确认要保存实习信息吗？',
      icon: <ExclamationCircleOutlined />,
      content: '实习信息只能填写一次！！！',
      onOk: async () => {
        await submit(form.getFieldsValue());
      }
    });
  };

  const submit = async (e: FormData) => {
    if (cityName) {
      const sendData: InternPostData = {
        comCity: cityName,
        comCityCode: e.epCity[1],
        comName: e.epName,
        comAddress: e.epAddress,
        comContactName: e.eppName,
        comContactSex: e.eppSex,
        comContactEmail: e.eppEmail,
        comContactPhone: e.eppPhone,
        comInstructorName: e.eptName,
        comInstructorSex: e.eptSex,
        comInstructorEmail: e.eptEmail,
        comInstructorPhone: e.eptPhone,
        offerImg: offerImg
      };
      const [data, ok] = await requestPost<InternPostData, InternPostReturn>(urlInternshipInfoEdit, sendData);
      if (ok.succeed) {
        if (data?.edited) {
          setHasEdited(true);
          setLoading(false);
          antMessage.success('保存成功');
        } else {
          setLoading(true);
          ok.message && antMessage.error(ok.message);
        }
      } else {
        setLoading(true);
        antMessage.error('保存失败');
      }
    }
  };

  const checkOffer = () => {
    if (!originOffer && !offerImg) {
      throw new Error('请上传Offer后再提交');
    }
  };

  const onFinish = async () => {
    form
      .validateFields()
      .then(checkOffer)
      .then(showPromiseConfirm)
      .catch((e: Error) => {
        e.message && message.info(e.message);
      });
  };

  const handleChange = (info: any) => {
    if (info.file.status === 'uploading') {
      setLoading(true);
      return;
    }
    if (info.file.status === 'done') {
      getBase64(info.file.originFileObj, (imageUrl: string | undefined) => {
        setOfferImg(imageUrl);
        setLoading(false);
      });
    }
  };

  useEffect(() => {
    if (checkState) {
      (async () => {
        const loadingMessage = createMessageLoading('internship');
        loadingMessage.showLoading('获取实习信息');
        const [data, ok] = await requestGet<unknown, InternshipGetData>(urlInternshipInfoQuery);
        if (ok.succeed) {
          if (data?.state === '1') {
            form.setFieldsValue({
              epCity: findCityCodeData(data.comCity),
              epAddress: data.comAddress,
              epName: data.comName,
              eppName: data.comContact,
              eppSex: data.sex,
              eppEmail: data.comContactEmail,
              eppPhone: data.comContactPhone,
              eptName: data.comInstructorName,
              eptSex: data.comInstructorSex,
              eptEmail: data.comInstructorEmail,
              eptPhone: data.comInstructorPhone
            });
            setOfferImg(data.offerImg);
            setOriginOffer(data.offerImg);
            setHasEdited(true);
          } else {
            loadingMessage.info('无实习信息');
            setHasEdited(false);
          }
          setLoading(false);
        } else {
          loadingMessage.error('加载失败');
          setLoading(true);
        }
        loadingMessage.hideLoading();
      })();
    }
  }, [checkState]);

  return checkState ? (
    <div className={styles.outer}>
      <TitleLine Icon={EditOutlined} headContent='实习信息' />
      <div className={styles.doubleSide}>
        <div className={styles.leftSide}>
          <Upload
            disabled={loading || hasEdited}
            accept='.png,.jpg,.webp'
            showUploadList={false}
            onChange={handleChange}
            action={(file) => {
              getBase64(file, (imageUrl: string | undefined) => {
                setOfferImg(imageUrl);
                setLoading(false);
              });
              return '';
            }}
          >
            {(offerImg && (
              <div
                className={styles.pic}
                style={{
                  backgroundImage: `url(${offerImg})`,
                  backgroundColor: 'transparent',
                  border: '1px solid #bbb'
                }}
              />
            )) || (
              <>
                <div className={styles.pic}>
                  <PlusOutlined className={styles.plus} />
                </div>
                <div className={styles.plusText}>上传offer</div>
              </>
            )}
          </Upload>
        </div>
        <div className={styles.rightSide}>
          <Form
            form={form}
            name='newInternship'
            scrollToFirstError
            size='middle'
            labelAlign='left'
            labelCol={{ span: 8 }}
            wrapperCol={{ span: 16 }}
            initialValues={{
              eppSex: 'F',
              eptSex: 'M'
            }}
            onFinish={onFinish}
          >
            <Row justify='space-between'>
              <Col span={8}>
                <Form.Item
                  label='实习城市'
                  name='epCity'
                  labelCol={{ span: 12 }}
                  wrapperCol={{ span: 12 }}
                  required={!hasEdited}
                  rules={[
                    {
                      required: true,
                      message: '请选择实习所在城市！'
                    }
                  ]}
                >
                  <Cascader
                    options={cityListOption}
                    disabled={hasEdited}
                    onChange={(value, selectedOptions) => {
                      if (selectedOptions && selectedOptions.length > 1) {
                        setCityName(selectedOptions[1].label as string);
                      }
                    }}
                    displayRender={(label, selectedOptions) => {
                      return label[label.length - 1];
                    }}
                    placeholder='点击选择'
                  />
                </Form.Item>
              </Col>
              <Col span={16}>
                <Form.Item
                  label='企业名称'
                  name='epName'
                  labelAlign='right'
                  required={!hasEdited}
                  rules={[
                    {
                      required: true,
                      message: '请输入实习所在企业名称！'
                    },
                    {
                      max: 50,
                      message: '企业名称不超过50个字符'
                    }
                  ]}
                >
                  <Input disabled={hasEdited} />
                </Form.Item>
              </Col>
            </Row>
            <Row>
              <Col span={24}>
                <Form.Item
                  label='企业地址'
                  name='epAddress'
                  labelCol={{ span: 4 }}
                  wrapperCol={{ span: 20 }}
                  required={!hasEdited}
                  rules={[
                    {
                      required: true,
                      message: '请输入实习企业所在地址！'
                    },
                    {
                      max: 50,
                      message: '企业地址不超过50个字符'
                    }
                  ]}
                >
                  <Input disabled={hasEdited} />
                </Form.Item>
              </Col>
            </Row>
            <Row justify='space-between'>
              <Col span={12}>
                <Form.Item
                  label='企业联系人'
                  name='eppName'
                  labelCol={{ span: 8 }}
                  wrapperCol={{ span: 16 }}
                  required={!hasEdited}
                  rules={[
                    {
                      required: true,
                      message: '请输入实习企业联系人姓名！'
                    },
                    {
                      max: 20,
                      message: '企业联系人名称不超过20个字符'
                    }
                  ]}
                >
                  <Input disabled={hasEdited} />
                </Form.Item>
              </Col>
              <Col span={9}>
                <Form.Item
                  labelCol={{ span: 17 }}
                  labelAlign='right'
                  wrapperCol={{ span: 7 }}
                  label='联系人性别'
                  name='eppSex'
                  required={!hasEdited}
                >
                  <Select disabled={hasEdited}>
                    <Select.Option value='F'>女</Select.Option>
                    <Select.Option value='M'>男</Select.Option>
                  </Select>
                </Form.Item>
              </Col>
            </Row>
            <Row justify='space-between'>
              <Col span={12}>
                <Form.Item
                  label='联系人邮箱'
                  name='eppEmail'
                  labelCol={{ span: 8 }}
                  wrapperCol={{ span: 16 }}
                  required={!hasEdited}
                  validateFirst
                  rules={[
                    {
                      required: true,
                      message: '请输入实习企业联系人邮箱！'
                    },
                    {
                      validator: async (rule, value) => {
                        checkEmail(value);
                      }
                    },
                    {
                      max: 20,
                      message: '企业联系人邮箱不超过20个字符'
                    }
                  ]}
                >
                  <Input disabled={hasEdited} />
                </Form.Item>
              </Col>
              <Col span={10}>
                <Form.Item
                  label='联系人手机'
                  name='eppPhone'
                  labelCol={{ span: 12 }}
                  labelAlign='right'
                  wrapperCol={{ span: 12 }}
                  required={!hasEdited}
                  validateFirst
                  rules={[
                    {
                      required: true,
                      message: '请输入实习企业联系人手机号！'
                    },
                    {
                      validator: async (rule, value) => {
                        checkPhone(value);
                      }
                    }
                  ]}
                >
                  <Input disabled={hasEdited} maxLength={11} />
                </Form.Item>
              </Col>
            </Row>
            <Row justify='space-between'>
              <Col span={12}>
                <Form.Item
                  label='企业导师'
                  name='eptName'
                  labelCol={{ span: 8 }}
                  wrapperCol={{ span: 16 }}
                  required={!hasEdited}
                  rules={[
                    {
                      required: true,
                      message: '请输入企业导师姓名！'
                    },
                    {
                      max: 20,
                      message: '企业导师名称不超过20个字符'
                    }
                  ]}
                >
                  <Input disabled={hasEdited} />
                </Form.Item>
              </Col>
              <Col span={9}>
                <Form.Item
                  labelCol={{ span: 17 }}
                  wrapperCol={{ span: 7 }}
                  labelAlign='right'
                  label='导师性别'
                  name='eptSex'
                  required={!hasEdited}
                >
                  <Select disabled={hasEdited}>
                    <Select.Option value='F'>女</Select.Option>
                    <Select.Option value='M'>男</Select.Option>
                  </Select>
                </Form.Item>
              </Col>
            </Row>
            <Row justify='space-between'>
              <Col span={12}>
                <Form.Item
                  label='导师邮箱'
                  name='eptEmail'
                  labelCol={{ span: 8 }}
                  wrapperCol={{ span: 16 }}
                  required={!hasEdited}
                  validateFirst
                  rules={[
                    {
                      required: true,
                      message: '请输入企业导师邮箱！'
                    },
                    {
                      validator: async (rule, value) => {
                        checkEmail(value);
                      }
                    },
                    {
                      max: 20,
                      message: '企业导师邮箱不超过20个字符'
                    }
                  ]}
                >
                  <Input disabled={hasEdited} />
                </Form.Item>
              </Col>
              <Col span={10}>
                <Form.Item
                  labelCol={{ span: 12 }}
                  wrapperCol={{ span: 12 }}
                  labelAlign='right'
                  label='导师手机'
                  name='eptPhone'
                  required={!hasEdited}
                  validateFirst
                  rules={[
                    {
                      required: true,
                      message: '请输入企业导师手机号！'
                    },
                    {
                      validator: async (rule, value) => {
                        checkPhone(value);
                      }
                    }
                  ]}
                >
                  <Input disabled={hasEdited} maxLength={11} />
                </Form.Item>
              </Col>
            </Row>
            <Space />
            <Row justify='center'>
              <Col>
                <Form.Item>
                  {hasEdited ? (
                    <Tooltip title='实习信息只能填写一次' trigger='click' color='#ff0000'>
                      <Button className={styles.btnDisabled}>确认保存</Button>
                    </Tooltip>
                  ) : (
                    <Button type='primary' htmlType='submit'>
                      确认保存
                    </Button>
                  )}
                </Form.Item>
              </Col>
            </Row>
          </Form>
        </div>
      </div>
    </div>
  ) : (
    <StateChecker setCheckState={setCheckState} />
  );
};

export default InternshipPage;
