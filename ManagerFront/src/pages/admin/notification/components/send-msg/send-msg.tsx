import React, { useEffect, useState } from 'react';
import { requestGet, requestPost } from '@/services/request';
import { urlMessageSend, urlRecvsQuery } from '@/services/url';
import { Button, Col, Form, Input, message, Row, Select, Spin, Modal } from 'antd';
import { createMessageLoading } from '@/utils/message';
import styles from './send-msg.scss';

interface Props {
  visible: boolean;
  setVisible: React.Dispatch<React.SetStateAction<boolean>>;
  postHook?: () => void;
}

interface SendParam {
  msgTitle: string;
  msgContent: string;
  msgType: string;
  msgRecvIds: string[];
}

interface SendResponse {
  sent: boolean;
}

interface Recv {
  id: string;
  name: string;
}

interface RecvResponse {
  recvs: Recv[];
}

const SPECIFIC = '3';

const SendMsgModal: React.FC<Props> = ({ visible, setVisible, postHook }) => {
  const [form] = Form.useForm<SendParam>();
  const [specific, setSpecific] = useState<boolean>(false);
  const [recvList, setRecvList] = useState<Recv[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [queryingRecvs, setQueryingRecvs] = useState<boolean>(true);

  const sendMessage = async (param: SendParam) => {
    const loadingMessage = createMessageLoading('admin-send-message').showLoading('发送中');
    const [res, ok] = await requestPost<SendParam, SendResponse>(urlMessageSend, param);
    if (ok.succeed && res?.sent) {
      setVisible(false);
      postHook && postHook();
      message.success('发送成功');
    } else {
      message.error(ok.message);
    }
    loadingMessage.hideLoading();
    setLoading(false);
  };

  const onFinish = async (param: SendParam) => {
    setLoading(true);
    sendMessage(param);
  };

  const fetchRecvs = async () => {
    const [res, ok] = await requestGet<null, RecvResponse>(urlRecvsQuery);
    if (ok.succeed && res) {
      setRecvList([...res.recvs]);
    } else {
      message.error(ok.message);
    }
    setQueryingRecvs(false);
  };

  const onSelect = (v: string) => {
    if (v === SPECIFIC) setSpecific(true);
    else setSpecific(false);
  };

  useEffect(() => {
    fetchRecvs();
  }, []);

  return (
    <Modal visible={visible} footer={null} closable={false} title='发送通知'>
      <Spin spinning={loading}>
        <Form form={form} onFinish={onFinish}>
          <Form.Item label='标题' name='msgTitle' required rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item label='内容' name='msgContent' required rules={[{ required: true }]}>
            <Input.TextArea showCount rows={4} maxLength={500} />
          </Form.Item>
          <Form.Item label='类型' name='msgType' required rules={[{ required: true }]}>
            <Select placeholder='选择一种类型' onChange={onSelect}>
              <Select.Option value={'0'}>全体成员</Select.Option>
              <Select.Option value={'1'}>全体学生</Select.Option>
              <Select.Option value={'2'}>全体教师</Select.Option>
              <Select.Option value={'3'}>指定用户</Select.Option>
            </Select>
          </Form.Item>
          {specific && (
            <Form.Item label='接收者' name='msgRecvIds' required rules={[{ required: true }]}>
              <Select
                showSearch
                mode='multiple'
                optionLabelProp='label'
                placeholder='搜索或选择用户'
                optionFilterProp='children'
                filterOption={(input: string, option) => {
                  const inputLow = input.toLowerCase();
                  return (
                    option?.value.toLowerCase().indexOf(inputLow) > -1 ||
                    // @ts-ignore
                    option?.label?.toLowerCase().indexOf(inputLow) > -1
                  );
                }}
                loading={queryingRecvs}
              >
                {recvList.map((e) => (
                  <Select.Option value={e.id} label={e.name} key={e.id}>
                    {e.name + ' ' + e.id}
                  </Select.Option>
                ))}
              </Select>
            </Form.Item>
          )}
          <Row justify='center' align='middle'>
            <Col span={6}>
              <Button type='primary' size='middle' loading={loading} htmlType='submit' className={styles.btn}>
                发送
              </Button>
            </Col>
            <Col>
              <Button type='primary' danger size='middle' onClick={() => setVisible(false)} className={styles.btn}>
                取消
              </Button>
            </Col>
          </Row>
        </Form>
      </Spin>
    </Modal>
  );
};

export default SendMsgModal;
