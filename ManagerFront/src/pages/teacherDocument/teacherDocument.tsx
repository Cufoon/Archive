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
    '湖南大学学生校外实践情况阶段汇报表',
    '湖南大学学生校外实践阶段检查表',
    '湖南大学校外实践鉴定表',
    '湖南大学学生校外实践企业导师评价表',
    '湖南大学学生校外实践总结报告'
  ];

  const onFinish = async (param: TableData) => {
    const loading = createMessageLoading('handle_loading');
    try {
      loading.showLoading('保存数据中');
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
        loading.success('提交成功！');
        setVisible(false);
        await queryBrowse();
      } else {
        loading.error('提交失败！');
      }
    } catch (e) {
      loading.error('提交失败！');
    }
  };

  const showPromiseConfirm = () => {
    Modal.confirm({
      title: '确认要提交审核结果吗？',
      icon: <ExclamationCircleOutlined />,
      okText: '确认',
      cancelText: '取消',
      onOk: async () => {
        await form.submit();
      }
    });
  };

  const showRejectConfirm = () => {
    Modal.confirm({
      title: '确认要退回表格吗？',
      icon: <ExclamationCircleOutlined />,
      content: '退回后学生可以重新填写该表格并提交',
      okText: '确认',
      cancelText: '取消',
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
      await loading.showLoading('退回表格中');
      const [res, ok] = await requestPost<RejectParam, HandleResponse>(urlTeacherRejectDocument, {
        sid: student ? student.sid : '0',
        category: student ? student.category : 0,
        order: student ? student.order : 0
      });
      if (ok.succeed && res?.handled) {
        loading.success('退回成功！');
        setVisible(false);
        queryBrowse();
      } else {
        loading.error('退回失败！');
      }
    } catch (e) {
      loading.error('退回失败！');
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
        <Form.Item label={'评分'} name='evaluation' rules={[{ required: true }]}>
          <Select style={{ width: 120 }}>
            <Option value={5}>优秀</Option>
            <Option value={4}>良好</Option>
            <Option value={3}>中等</Option>
            <Option value={2}>合格</Option>
            <Option value={1}>不合格</Option>
          </Select>
        </Form.Item>
        <Form.Item label={'指导老师建议'} name='teaSuggestions' rules={[{ required: true }]}>
          <TextArea cols={5} />
        </Form.Item>
        <div className={styles.buttonGroup}>
          <Button
            className={styles.button}
            onClick={() => {
              setVisible(false);
            }}
          >
            取消
          </Button>
          <Button className={styles.button} danger={true} onClick={showRejectConfirm}>
            退回
          </Button>
          <Button className={styles.button} type='primary' onClick={showPromiseConfirm}>
            提交
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
        审核
      </Button>
    );
  };

  const showTag = (key: any) => {
    const current = info.find((item: Table) => item.key == key);
    const status = current ? current.status : 0;
    const due = current ? current.due : false;
    return <div>{due ? <Tag color={'red'}>过期</Tag> : <Tag color={'green'}>未过期</Tag>}</div>;
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
      message.error('获取失败');
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
        <div className={styles.title}>表格审核</div>
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
                  未审核
                </Radio.Button>
                <Radio.Button value={2} style={{ borderRadius: '0 8px 8px 0' }}>
                  已审核
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
                刷新
              </Button>
            </Col>
          </Row>
        </div>
        {tableView}
        <Table dataSource={docType === 1 ? unhandled : handled} loading={isLoadingTableData || isRefresh}>
          <Column title='姓名' dataIndex='sname' key='sname' />
          <Column title='学号' dataIndex='sid' key='sid' sorter={(a: string, b: string) => (a < b ? 1 : -1)} />
          <Column title='材料' dataIndex='name' key='name' />
          <Column title='月次' dataIndex='order' key='order' />
          {docType === 1 ? (
            <Column
              title='是否过期'
              dataIndex='key'
              key='key'
              render={(record: any) => {
                return showTag(record);
              }}
            />
          ) : null}
          <Column title='提交时间' dataIndex='submitTime' key='submitTime' />
          {docType == 2 ? <Column title='审核时间' dataIndex='checkTime' key='checkTime' /> : null}
          {docType == 2 ? (
            <Column
              title='评价'
              dataIndex='evaluation'
              key='evaluation'
              render={(record: number) => {
                switch (record) {
                  case 1:
                    return '不合格';
                  case 2:
                    return '合格';
                  case 3:
                    return '中等';
                  case 4:
                    return '良好';
                  case 5:
                    return '优秀';
                  default:
                    return '';
                }
              }}
            />
          ) : null}
          {docType == 1 ? (
            <Column title='操作' dataIndex='key' key='key' render={(record: any) => showAction(record)} />
          ) : null}
        </Table>
      </div>
    </div>
  );
};

export default TeacherDocumentPage;
