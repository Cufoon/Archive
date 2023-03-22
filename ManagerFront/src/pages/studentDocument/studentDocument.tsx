import React, { useEffect, useState } from 'react';
import { history } from 'umi';
import { Button, message, Modal, Table, Tag } from 'antd';
import tableList, {
  level,
  tableAppraisal,
  tableExam,
  tableIdentify,
  tableReport,
  tableSummary
} from '@/pages/studentDocument/table/table';
import StateChecker from '@/components/state-checker/state-checker';
import styles from '@/pages/studentDocument/studentDocument.scss';
import { requestGet } from '@/services/request';
import { urlDocument } from '@/services/url';
import { PoweroffOutlined, ReloadOutlined } from '@ant-design/icons';
import ViewReport from '@/pages/document-view/view-report';
import ViewExam from '@/pages/document-view/view-exam';
import ViewIdentify from '@/pages/document-view/view-identify';
import ViewAppraisal from '@/pages/document-view/view-appraisal';
import ViewSummary from '@/pages/document-view/view-summary';

export interface Table {
  key: any;
  name: string;
  category: number;
  order: number;
  deadline: string;
  due: boolean;
  submitTime: string;
  checkTime?: string;
  evaluation?: number;
  status: number;
}

interface QueryResponse {
  form: Table[];
}

//查看哪张表
interface TableInfo {
  category: number;
  order: number;
}

const { Column } = Table;

const StudentDocumentPage: React.FC = () => {
  const [list, setList] = useState<Table[]>(tableList);
  const [isLoadingTableData, setIsLoadingTableData] = useState<boolean>(true);
  const [isRefresh, setIsRefresh] = useState<boolean>(false);
  const [tableInfo, setTableInfo] = useState<TableInfo>();
  const [visible, setVisible] = useState<boolean>(false);
  const [checkState, setCheckState] = useState<boolean>(false);

  const tableTitle = [
    '湖南大学学生校外实践情况阶段汇报表',
    '湖南大学学生校外实践阶段检查表',
    '湖南大学校外实践鉴定表',
    '湖南大学学生校外实践企业导师评价表',
    '湖南大学学生校外实践总结报告'
  ];
  const tagColor = ['red', 'orange', 'red', 'green'];
  const tagText = ['待提交', '审核中', '审核未通过', '审核通过'];

  const deadlineColor = (day: string) => {
    const num = parseInt(day);
    if (num >= 60) {
      return 3;
    }
    if (num < 30) {
      return 2;
    }
    return 1;
  };
  const handleWrite = (key: any) => {
    const current = list.find((item: Table) => item.key == key);
    switch (current?.category) {
      case 1:
        history.push({
          pathname: tableReport,
          query: { order: current?.order.toString() }
        });
        break;
      case 2:
        history.push({
          pathname: tableExam,
          query: { order: current?.order.toString() }
        });
        break;
      case 3:
        history.push({
          pathname: tableIdentify,
          query: { order: current?.order.toString() }
        });
        break;
      case 4:
        history.push({
          pathname: tableAppraisal,
          query: { order: current?.order.toString() }
        });
        break;
      case 5:
        history.push({
          pathname: tableSummary,
          query: { order: current?.order.toString() }
        });
        break;
      default:
        break;
    }
  };

  const handleLook = async (key: any) => {
    const current = list.find((item: Table) => item.key == key);
    await setTableInfo({
      category: current ? current.category : 0,
      order: current ? current.order : 0
    });
    setVisible(true);
  };

  const viewList = (category: number, order: number) => {
    switch (category) {
      case 1:
        return <ViewReport order={order} type={'Student'} />;
      case 2:
        return <ViewExam order={order} type={'Student'} />;
      case 3:
        return <ViewIdentify order={order} type={'Student'} />;
      case 4:
        return <ViewAppraisal order={order} type={'Student'} />;
      case 5:
        return <ViewSummary order={order} type={'Student'} />;
      default:
        return <div />;
    }
  };

  const tableView = (
    <Modal
      title={tableTitle[tableInfo ? tableInfo.category - 1 : 0]}
      centered
      visible={visible}
      onOk={() => setVisible(false)}
      onCancel={() => setVisible(false)}
      width={1000}
      destroyOnClose
      footer={[]}
    >
      {viewList(tableInfo ? tableInfo.category : 0, tableInfo ? tableInfo.order : 0)}
    </Modal>
  );

  const showAction = (key: any) => {
    const current = list.find((item: Table) => item.key == key);
    return current?.status == 1 || current?.status == 3 ? (
      <Button
        data-id={key}
        type={'link'}
        style={{ paddingLeft: '0' }}
        onClick={() => {
          handleLook(key);
        }}
      >
        查看
      </Button>
    ) : (
      <Button
        data-id={key}
        type={'link'}
        style={{ paddingLeft: '0' }}
        onClick={() => {
          handleWrite(key);
        }}
      >
        填写
      </Button>
    );
  };

  const showTags = (key: any) => {
    const current = list.find((item: Table) => item.key == key);
    const day = current ? current.deadline : '';
    const tag = `剩余${day}天`;
    const status = current ? current.status : 0;
    const due = current ? current.due : false;
    return (
      <div>
        <Tag color={tagColor[current ? current.status : 0]}>{tagText[current ? current.status : 0]}</Tag>
        {status === 0 && day != '' && due === false ? <Tag color={tagColor[deadlineColor(day)]}>{tag}</Tag> : null}
        {due === true ? <Tag color={'red'}>超时</Tag> : null}
      </div>
    );
  };

  const queryDocument = async () => {
    try {
      const [res, ok] = await requestGet<null, QueryResponse>(urlDocument);
      const editItem = (item1: Table) => {
        const resItem = res?.form.find((item2) => item1.category == item2.category && item1.order == item2.order);
        return {
          ...item1,
          submitTime: resItem ? resItem.submitTime : '',
          checkTime: resItem ? resItem.checkTime : '',
          deadline: resItem ? resItem.deadline : '',
          due: resItem ? resItem.due : false,
          evaluation: resItem ? resItem.evaluation : 0,
          status: resItem ? resItem.status : 0
        };
      };

      if (ok.succeed) {
        const oldList = [...list];
        const newList = oldList.map(editItem);
        setList(newList);
        setIsLoadingTableData(false);
      }
    } catch (e) {
      message.error('获取失败');
      setIsLoadingTableData(false);
    }
  };

  const reFresh = async () => {
    setIsRefresh(true);
    await queryDocument();
    setIsRefresh(false);
  };

  useEffect(() => {
    checkState && queryDocument();
  }, [checkState]);

  return checkState ? (
    <div className={styles.background}>
      <div className={styles.content}>
        <div className={styles.title}>表格填写</div>
        <div className={styles.toolBar}>
          <Button
            type='primary'
            icon={isRefresh ? <PoweroffOutlined /> : <ReloadOutlined />}
            loading={isRefresh}
            onClick={reFresh}
            disabled={isLoadingTableData}
            style={{ borderRadius: '8px' }}
          >
            刷新
          </Button>
        </div>
        {tableView}
        <Table dataSource={list} loading={isLoadingTableData || isRefresh}>
          <Column title='材料' dataIndex='name' key='name' />
          <Column
            title='状态'
            dataIndex='key'
            key='writed'
            render={(record: any) => {
              return showTags(record);
            }}
          />
          <Column title='提交时间' dataIndex='submitTime' key='submitTime' />
          <Column title='审核时间' dataIndex='checkTime' key='checkTime' />
          <Column
            title='评价'
            dataIndex='evaluation'
            key='evaluation'
            render={(record: number) => {
              return level[record - 1];
            }}
          />
          <Column title='操作' dataIndex='key' key='key' render={(key: any) => showAction(key)} />
        </Table>
      </div>
    </div>
  ) : (
    <StateChecker setCheckState={setCheckState} highLevel />
  );
};

export default StudentDocumentPage;
