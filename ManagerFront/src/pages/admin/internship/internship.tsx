import React, { useEffect, useState } from 'react';
import { DatePicker, Card, Empty, Button, Space, Form, Input, List, Row, Col, Spin } from 'antd';
import { urlAdminPeriodList, urlInternShipChange } from '@/services/url';
import { requestGet, requestPost } from '@/services/request';
import styles from './internship.scss';
import { EditSendData, EditGetData, PeriodData, GetPeriodData, FormData } from './interface';
import { createMessageLoading } from '@/utils/message';
import moment from 'moment';

const InternshipPage: React.FC = () => {
  const [form] = Form.useForm<FormData>();
  const [nowPeriod, setNowPeriod] = useState<PeriodData>();
  const [oldPeriodList, setOldPeriodList] = useState<PeriodData[]>();
  const [isShowCreateCard, setIsShowCreateCard] = useState<boolean>(false);
  const [isEdit, setIsEdit] = useState<boolean>(false);
  const [init, setInit] = useState<boolean>(true);

  const queryPeriodList = async () => {
    const loadingMessage = createMessageLoading('admin-intern-query').showLoading('获取实习周期信息');
    const [data, ok] = await requestGet<unknown, GetPeriodData>(urlAdminPeriodList);
    if (ok.succeed && data) {
      if (data.currentCreated) {
        setNowPeriod(data.periodList[0]);
        const [_, ...old] = data.periodList;
        setOldPeriodList(old);
        form.setFieldsValue({
          periodName: data.periodList[0].name,
          periodRange: [
            moment(data.periodList[0].startDate, 'YYYY-MM-DD'),
            moment(data.periodList[0].endDate, 'YYYY-MM-DD')
          ]
        });
      } else {
        setOldPeriodList(data.periodList);
      }
      setInit(false);
    }
    loadingMessage.hideLoading();
  };

  const onCreateNewPeriod = async () => {
    try {
      await form.validateFields();
      const formdData = form.getFieldsValue();
      const isEditOp = isEdit;
      const loadingMessage = createMessageLoading('create-period');
      loadingMessage.showLoading(isEditOp ? '修改实习阶段信息' : '新建实习阶段');
      const [data, ok] = await requestPost<EditSendData, EditGetData>(urlInternShipChange, {
        fromDate: formdData.periodRange[0],
        endDate: formdData.periodRange[1],
        periodName: formdData.periodName,
        isUpdate: false
      });
      if (ok.succeed && data) {
        loadingMessage.success(isEditOp ? '修改成功' : '新建成功');
      }
      loadingMessage.hideLoading();
      onCancelEdit();
      queryPeriodList();
    } catch (e) {
      console.log(e);
    }
  };

  const onCancelEdit = () => {
    if (nowPeriod) {
      form.setFieldsValue({
        periodName: nowPeriod.name,
        periodRange: [moment(nowPeriod.startDate, 'YYYY-MM-DD'), moment(nowPeriod.endDate, 'YYYY-MM-DD')]
      });
    }
    setIsEdit(false);
  };

  useEffect(() => {
    (async () => {
      await queryPeriodList();
    })();
  }, []);

  useEffect(() => {
    console.log(nowPeriod, oldPeriodList);
  }, [nowPeriod, oldPeriodList]);

  return (
    <div className={styles.outer}>
      <div className={styles.placeHolder} />
      <Space style={{ width: '100%' }} direction='vertical' size='middle'>
        <Spin delay={150} spinning={init}>
          <Card title='当前实习阶段' style={{ height: '300px' }}>
            {isShowCreateCard || nowPeriod ? (
              <div className={styles.form}>
                <Form form={form}>
                  <Form.Item
                    name='periodName'
                    label='实习阶段名称'
                    labelCol={{ span: 8 }}
                    wrapperCol={{ span: 16 }}
                    required={isEdit}
                    rules={[
                      {
                        required: true,
                        message: '请输入实习阶段名称'
                      }
                    ]}
                  >
                    <Input className={styles.inputWidth} readOnly={!!nowPeriod && !isEdit} />
                  </Form.Item>
                  <Form.Item
                    name='periodRange'
                    label='阶段时间范围'
                    labelCol={{ span: 8 }}
                    wrapperCol={{ span: 16 }}
                    required={isEdit}
                    rules={[
                      {
                        type: 'array',
                        required: true,
                        message: '请选择实习阶段时间范围'
                      }
                    ]}
                  >
                    <DatePicker.RangePicker className={styles.inputWidth} disabled={nowPeriod && !isEdit} />
                  </Form.Item>
                  <Form.Item>
                    <Row justify='center'>
                      <Col>
                        <Space size='large'>
                          {isEdit && (
                            <Button type='primary' danger className={styles.btn} onClick={onCancelEdit}>
                              取消
                            </Button>
                          )}
                          <Button
                            type='primary'
                            className={styles.btn}
                            onClick={nowPeriod && !isEdit ? () => setIsEdit(true) : onCreateNewPeriod}
                          >
                            {nowPeriod ? (isEdit ? '保存' : '编辑') : '新建实习'}
                          </Button>
                        </Space>
                      </Col>
                    </Row>
                  </Form.Item>
                </Form>
              </div>
            ) : (
              <Empty description='当前暂未开始实习'>
                <Button type='primary' className={styles.btn} onClick={() => setIsShowCreateCard(true)}>
                  新建实习
                </Button>
              </Empty>
            )}
          </Card>
        </Spin>
        <Spin delay={150} spinning={init}>
          <Card title='历史实习阶段'>
            {oldPeriodList && oldPeriodList.length > 0 ? (
              <List>
                <List.Item className={styles.widthAll}>
                  <Row justify='space-between' className={styles.widthAll}>
                    <Col span={14}>实习阶段</Col>
                    <Col span={4}>开始阶段</Col>
                    <Col span={4}>结束阶段</Col>
                  </Row>
                </List.Item>
                {oldPeriodList.map((item) => {
                  return (
                    <List.Item className={styles.widthAll} key={`${item.name}${item.startDate}`}>
                      <Row justify='space-between' className={styles.widthAll}>
                        <Col span={14}>{item.name}</Col>
                        <Col span={4}>{item.startDate}</Col>
                        <Col span={4}>{item.endDate}</Col>
                      </Row>
                    </List.Item>
                  );
                })}
              </List>
            ) : (
              <Empty description='暂无历史实习阶段' />
            )}
          </Card>
        </Spin>
      </Space>
    </div>
  );
};

export default InternshipPage;
