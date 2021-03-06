import React, { useState, useEffect } from 'react';
import dayjs from 'dayjs';
import { Button, Row, Col, Table, message, Modal, Radio } from 'antd';
import { urlMessageListRead, urlMessageListToRead, urlMessageListNotice, urlMessageRead } from '@/services/url';
import { QueryData, OneMessageData, ReadSend, ReadGet } from './interface';
import styles from './message.scss';
import { requestGet, requestPost } from '@/services/request';
import { PoweroffOutlined, ReloadOutlined } from '@ant-design/icons';
import { RadioChangeEvent } from 'antd/es/radio';

const DealApplyPage: React.FC = () => {
  const [tableData, setTableData] = useState<OneMessageData[]>([]);
  const [isLoadTableData, setIsLoadTableData] = useState<boolean>(true);
  const [isRefresh, setIsRefresh] = useState<boolean>(false);
  const [isShowModal, setIsShowModal] = useState<boolean>(false);
  const [messageTitle, setMessageTitle] = useState<string>('');
  const [messageContent, setMessageContent] = useState<string>('');
  const [currentURL, setCurrentURL] = useState<string>(urlMessageListNotice);
  const [currentTab, setCurrentTab] = useState<string>('1');

  const reFresh = async () => {
    setIsRefresh(true);
    setIsLoadTableData(true);
    init();
  };

  const init = async () => {
    const [data, ok] = await requestGet<unknown, QueryData>(currentURL);
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

  useEffect(() => {
    init();
  }, []);

  const showContentModal = (title: string, content: string) => {
    setMessageTitle(title);
    setMessageContent(content);
    setIsShowModal(true);
  };

  const hideContentModal = () => {
    setIsShowModal(false);
  };

  const changeMessageTab = (e: RadioChangeEvent) => {
    console.log(e);
    setCurrentTab(e.target.value);
    switch (e.target.value) {
      case '1':
        setCurrentURL(urlMessageListNotice);
        break;
      case '2':
        setCurrentURL(urlMessageListToRead);
        break;
      default:
        setCurrentURL(urlMessageListRead);
        break;
    }
  };

  const readMessage = async (mid: number) => {
    const [data, ok] = await requestPost<ReadSend, ReadGet>(urlMessageRead, { messageList: [mid] });
    if (ok.succeed && data) {
      console.log(mid, '????????????');
    } else {
      console.log(mid, '????????????');
    }
  };

  useEffect(() => {
    setIsLoadTableData(true);
    init();
  }, [currentURL]);

  return (
    <div className={styles.outer}>
      <Row justify='space-between'>
        <Col>
          <Radio.Group
            size='middle'
            defaultValue='1'
            optionType='button'
            buttonStyle='solid'
            value={currentTab}
            onChange={changeMessageTab}
            disabled={isLoadTableData}
          >
            <Radio.Button value='1' className={styles.btnLeft}>
              ????????????
            </Radio.Button>
            <Radio.Button value='2'>????????????</Radio.Button>
            <Radio.Button value='3' className={styles.btnRight}>
              ????????????
            </Radio.Button>
          </Radio.Group>
        </Col>
        <Col>
          <Button
            type='primary'
            className={styles.btn}
            icon={isRefresh ? <PoweroffOutlined /> : <ReloadOutlined />}
            loading={isRefresh}
            onClick={reFresh}
          >
            ??????
          </Button>
        </Col>
      </Row>
      <Table
        size='small'
        dataSource={tableData}
        style={{ marginTop: '20px' }}
        loading={{ spinning: isLoadTableData, delay: 100 }}
      >
        <Table.Column title='??????' key='message' dataIndex='msgTitle' />
        <Table.Column title='?????????' key='author' dataIndex='sender' />
        <Table.Column title='??????' key='sendTime' dataIndex='sentTime' />
        <Table.Column
          title='??????'
          key='action'
          dataIndex='msgTitle'
          render={(text: string, record: OneMessageData, index: number) => {
            return (
              <Button
                type='text'
                size='small'
                className={styles.readButton}
                onClick={() => {
                  let content = text;
                  if (record.msgContent && !/^ *$/.test(record.msgContent)) {
                    content = record.msgContent;
                  }
                  showContentModal(text, content);
                  readMessage(record.msgId);
                }}
              >
                ????????????
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
            ??????
          </Button>
        }
        onCancel={hideContentModal}
      >
        <p>{messageContent}</p>
      </Modal>
    </div>
  );
};

export default DealApplyPage;
