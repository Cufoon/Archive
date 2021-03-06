import React, { useEffect, useState } from 'react';
import { Button, Col, Form, message, Modal, Radio, Row, Select, Table, Tag } from 'antd';
import styles from '@/pages/teacherDocument/teacherDocument.scss';
import { requestGet, requestPost } from '@/services/request';
import { urlTeacherBrowseDocument, urlTeacherHandleDocument, urlTeacherRejectDocument } from '@/services/url';
import Column from 'antd/es/table/Column';
import ViewReport from '@/pages/document-view/view-report';
import TextArea from 'antd/es/input/TextArea';
import ViewExam from '@/pages/document-view/view-exam';
import ViewIdentify from '@/pages/document-view/view-identify';
import ViewSummary from '@/pages/document-view/view-summary';
import ViewAppraisal from '@/pages/document-view/view-appraisal';
import { ExclamationCircleOutlined, PoweroffOutlined, ReloadOutlined } from '@ant-design/icons';
import { createMessageLoading } from '@/utils/message';

interface BrowseResponse {
  form: Table[];
}

interface Student {
  sid: string;
  category: number;
  order: number;
}

interface Table {
  key: number;
  sid: string;
  name: string;
  sname: string;
  category: number;
  order: number;
  status: number;
  evaluation: number;
  due: boolean;
  submitTime: string;
  checkTime: string;
}

interface TableData {
  evaluation: number;
  teaSuggestions: string;
}

interface RejectParam {
  sid: string;
  category: number;
  order: number;
}

interface HandleParam {
  sid: string;
  category: number;
  order: number;
  tableData: TableData;
}

interface HandleResponse {
  handled: boolean;
}

const TeacherDocumentPage: React.FC = () => {
  const [info, setInfo] = useState<Table[]>([]);
  const [unhandled, setUnhandled] = useState<Table[]>([]);
  const [handled, setHandled] = useState<Table[]>([]);
  const [visible, setVisible] = useState<boolean>(false);
  const [student, setStudent] = useState<Student>();
  const [isLoadingTableData, setIsLoadingTableData] = useState<boolean>(true);
  const [isRefresh, setIsRefresh] = useState<boolean>(false);
  const [docType, setDocType] = useState<number>(1);
  const [form] = Form.useForm<TableData>();
  const tableTitle = [
    '???????????????????????????????????????????????????',
    '?????????????????????????????????????????????',
    '?????????????????????????????????',
    '???????????????????????????????????????????????????',
    '??????????????????????????????????????????'
  ];

  const onFinish = async (param: TableData) => {
    const loading = createMessageLoading('handle_loading');
    try {
      loading.showLoading('???????????????');
      const [res, ok] = await requestPost<HandleParam, HandleResponse>(urlTeacherHandleDocument, {
        sid: student ? student.sid : '0',
        category: student ? student.category : 0,
        order: student ? student.order : 0,
        tableData: {
          evaluation: param.evaluation,
          teaSuggestions: param.teaSuggestions
        }
      });
      if (ok.succeed && res?.handled) {
        loading.success('???????????????');
        setVisible(false);
        await queryBrowse();
      } else {
        loading.error('???????????????');
      }
    } catch (e) {
      loading.error('???????????????');
    }
  };

  const showPromiseConfirm = () => {
    Modal.confirm({
      title: '?????????????????????????????????',
      icon: <ExclamationCircleOutlined />,
      okText: '??????',
      cancelText: '??????',
      onOk: async () => {
        await form.submit();
      }
    });
  };

  const showRejectConfirm = () => {
    Modal.confirm({
      title: '???????????????????????????',
      icon: <ExclamationCircleOutlined />,
      content: '???????????????????????????????????????????????????',
      okText: '??????',
      cancelText: '??????',
      onOk: async () => {
        await rejectTable();
      }
    });
  };

  const viewList = (sid: string, category: number, order: number) => {
    switch (category) {
      case 1:
        return <ViewReport sid={sid} order={order} type={'Teacher'} />;
      case 2:
        return <ViewExam sid={sid} order={order} type={'Teacher'} />;
      case 3:
        return <ViewIdentify sid={sid} order={order} type={'Teacher'} />;
      case 4:
        return <ViewAppraisal sid={sid} order={order} type={'Teacher'} />;
      case 5:
        return <ViewSummary sid={sid} order={order} type={'Teacher'} />;
      default:
        return <div />;
    }
  };

  const rejectTable = async () => {
    const loading = createMessageLoading('reject_loading');
    try {
      await loading.showLoading('???????????????');
      const [res, ok] = await requestPost<RejectParam, HandleResponse>(urlTeacherRejectDocument, {
        sid: student ? student.sid : '0',
        category: student ? student.category : 0,
        order: student ? student.order : 0
      });
      if (ok.succeed && res?.handled) {
        loading.success('???????????????');
        setVisible(false);
        queryBrowse();
      } else {
        loading.error('???????????????');
      }
    } catch (e) {
      loading.error('???????????????');
    }
  };

  const { Option } = Select;
  const tableView = (
    <Modal
      title={tableTitle[student ? student.category - 1 : 0]}
      centered
      visible={visible}
      onOk={() => setVisible(false)}
      onCancel={() => setVisible(false)}
      width={1000}
      destroyOnClose
      footer={[]}
    >
      {viewList(student ? student.sid : '0', student ? student.category : 0, student ? student.order : 0)}
      <Form className={styles.form} layout={'horizontal'} onFinish={onFinish} form={form}>
        <Form.Item label={'??????'} name='evaluation' rules={[{ required: true }]}>
          <Select style={{ width: 120 }}>
            <Option value={5}>??????</Option>
            <Option value={4}>??????</Option>
            <Option value={3}>??????</Option>
            <Option value={2}>??????</Option>
            <Option value={1}>?????????</Option>
          </Select>
        </Form.Item>
        <Form.Item label={'??????????????????'} name='teaSuggestions' rules={[{ required: true }]}>
          <TextArea cols={5} />
        </Form.Item>
        <div className={styles.buttonGroup}>
          <Button
            className={styles.button}
            onClick={() => {
              setVisible(false);
            }}
          >
            ??????
          </Button>
          <Button className={styles.button} danger={true} onClick={showRejectConfirm}>
            ??????
          </Button>
          <Button className={styles.button} type='primary' onClick={showPromiseConfirm}>
            ??????
          </Button>
        </div>
      </Form>
    </Modal>
  );

  const handleClick = async (sid: string, category: number, order: number) => {
    await setStudent({ sid, category, order });
    setVisible(true);
  };

  const showAction = (key: any) => {
    const current = info.find((item: Table) => item.key == key);
    return (
      <Button
        data-id={key}
        type={'link'}
        style={{ paddingLeft: '0' }}
        disabled={current?.status === 3}
        onClick={() => {
          handleClick(current ? current.sid : '0', current ? current.category : 1, current ? current.order : 1);
        }}
      >
        ??????
      </Button>
    );
  };

  const showTag = (key: any) => {
    const current = info.find((item: Table) => item.key == key);
    const status = current ? current.status : 0;
    const due = current ? current.due : false;
    return <div>{due ? <Tag color={'red'}>??????</Tag> : <Tag color={'green'}>?????????</Tag>}</div>;
  };

  const queryBrowse = async () => {
    await setIsLoadingTableData(true);
    try {
      const [res, ok] = await requestGet<null, BrowseResponse>(urlTeacherBrowseDocument);
      if (ok.succeed && res) {
        res.form.map((item, index) => {
          item.key = index;
        });
        console.log(res.form);
        setInfo(res.form);
        setUnhandled(res.form.filter((item) => item.status === 1));
        setHandled(res.form.filter((item) => item.status === 3));
        setIsLoadingTableData(false);
      }
    } catch (e) {
      message.error('????????????');
      setIsLoadingTableData(false);
    }
  };

  const reFresh = async () => {
    setIsRefresh(true);
    await queryBrowse();
    setIsRefresh(false);
  };

  useEffect(() => {
    queryBrowse();
  }, []);

  return (
    <div className={styles.background}>
      <div className={styles.content}>
        <div className={styles.title}>????????????</div>
        <div className={styles.toolBar}>
          <Row justify='space-between'>
            <Col>
              <Radio.Group
                value={docType}
                onChange={(e) => {
                  setDocType(e.target.value);
                }}
                disabled={isLoadingTableData}
              >
                <Radio.Button value={1} style={{ borderRadius: '8px 0 0 8px' }}>
                  ?????????
                </Radio.Button>
                <Radio.Button value={2} style={{ borderRadius: '0 8px 8px 0' }}>
                  ?????????
                </Radio.Button>
              </Radio.Group>
            </Col>
            <Col>
              <Button
                type='primary'
                style={{ borderRadius: '8px' }}
                icon={isRefresh ? <PoweroffOutlined /> : <ReloadOutlined />}
                loading={isRefresh}
                onClick={reFresh}
                disabled={isLoadingTableData}
              >
                ??????
              </Button>
            </Col>
          </Row>
        </div>
        {tableView}
        <Table dataSource={docType === 1 ? unhandled : handled} loading={isLoadingTableData || isRefresh}>
          <Column title='??????' dataIndex='sname' key='sname' />
          <Column title='??????' dataIndex='sid' key='sid' sorter={(a: string, b: string) => (a < b ? 1 : -1)} />
          <Column title='??????' dataIndex='name' key='name' />
          <Column title='??????' dataIndex='order' key='order' />
          {docType === 1 ? (
            <Column
              title='????????????'
              dataIndex='key'
              key='key'
              render={(record: any) => {
                return showTag(record);
              }}
            />
          ) : null}
          <Column title='????????????' dataIndex='submitTime' key='submitTime' />
          {docType == 2 ? <Column title='????????????' dataIndex='checkTime' key='checkTime' /> : null}
          {docType == 2 ? (
            <Column
              title='??????'
              dataIndex='evaluation'
              key='evaluation'
              render={(record: number) => {
                switch (record) {
                  case 1:
                    return '?????????';
                  case 2:
                    return '??????';
                  case 3:
                    return '??????';
                  case 4:
                    return '??????';
                  case 5:
                    return '??????';
                  default:
                    return '';
                }
              }}
            />
          ) : null}
          {docType == 1 ? (
            <Column title='??????' dataIndex='key' key='key' render={(record: any) => showAction(record)} />
          ) : null}
        </Table>
      </div>
    </div>
  );
};

export default TeacherDocumentPage;
