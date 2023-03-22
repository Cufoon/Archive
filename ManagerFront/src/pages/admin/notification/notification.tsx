import React, { useState, useEffect } from 'react';
import dayjs from 'dayjs';
import { Table, Button, Modal, message, Row, Col } from 'antd';
import { PoweroffOutlined, ReloadOutlined } from '@ant-design/icons';
import { requestGet } from '@/services/request';
import { urlMessageListNotice } from '@/services/url';
import { OneMessageData, QueryData } from '@/pages/message/interface';
import SendMessageCard from '@/pages/admin/notification/components/send-msg/send-msg';
import styles from './notification.scss';

const NotificationPage: React.FC = () => {
  const [tableData, setTableData] = useState<OneMessageData[]>([]);
  const [isRefresh, setIsRefresh] = useState<boolean>(false);
  const [isLoadTableData, setIsLoadTableData] = useState<boolean>(true);
  const [isShowModal, setIsShowModal] = useState<boolean>(false);
  const [messageTitle, setMessageTitle] = useState<string>('');
  const [messageContent, setMessageContent] = useState<string>('');
  const [isShowSend, setIsShowSend] = useState<boolean>(false);

  const showContentModal = (title: string, content: string) => {
    setMessageTitle(title);
    setMessageContent(content);
    setIsShowModal(true);
  };

  const init = async () => {
    const [data, ok] = await requestGet<unknown, QueryData>(urlMessageListNotice);
    if (ok.succeed && data) {
      console.log(data);
      if (data.messageList?.length >= 0) {
        setTableData(
          data.messageList
            .map((value) => {
              return {
                ...value,
                sentTime: dayjs(value.sentTime).format('YYYY-MM-DD HH:mm'),
                key: `msgAt${value.msgId}`
              };
            })
            .reverse()
        );
      }
    } else {
      ok.message && message.error(ok.message);
    }
    setTimeout(() => {
      setIsLoadTableData(false);
      setIsRefresh(false);
    }, 200);
  };

  const hideContentModal = () => {
    setIsShowModal(false);
  };

  const reFresh = async () => {
    setIsRefresh(true);
    setIsLoadTableData(true);
    init();
  };

  useEffect(() => {
    init();
  }, []);

  return (
    <div className={styles.outer}>
      <Row justify='space-between'>
        <Col>
          <Button
            type='primary'
            className={styles.btn}
            onClick={() => {
              setIsShowSend(true);
            }}
          >
            添加公告
          </Button>
        </Col>
        <Col>
          <Button
            type='primary'
            className={styles.btn}
            icon={isRefresh ? <PoweroffOutlined /> : <ReloadOutlined />}
            loading={isRefresh}
            onClick={reFresh}
          >
            刷新
          </Button>
        </Col>
      </Row>
      <Table size='small' dataSource={tableData} style={{ marginTop: '20px' }} loading={isLoadTableData}>
        <Table.Column title='标题' key='message' dataIndex='msgTitle' />
        <Table.Column title='发布范围' key='receiver' dataIndex='receiver' />
        <Table.Column title='时间' key='sendTime' dataIndex='sentTime' />
        <Table.Column
          title='操作'
          key='action'
          dataIndex='msgTitle'
          render={(text: string, record: OneMessageData, index: number) => {
            return (
              <Button
                type='text'
                className={styles.readButton}
                onClick={() => {
                  let content = text;
                  if (record.msgContent && !/^ *$/.test(record.msgContent)) {
                    content = record.msgContent;
                  }
                  showContentModal(text, content);
                }}
              >
                查看详情
              </Button>
            );
          }}
        />
      </Table>
      <Modal
        visible={isShowModal}
        title={messageTitle}
        bodyStyle={{ padding: '30px 20px', fontSize: '14px' }}
        footer={
          <Button size='middle' type='primary' onClick={hideContentModal} className={styles.btn}>
            关闭
          </Button>
        }
        onCancel={hideContentModal}
      >
        <p>{messageContent}</p>
      </Modal>
      <SendMessageCard visible={isShowSend} setVisible={setIsShowSend} postHook={reFresh} />
    </div>
  );
};

export default NotificationPage;
