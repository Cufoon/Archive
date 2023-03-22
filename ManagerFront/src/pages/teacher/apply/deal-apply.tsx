import React, { ReactText, useState, useEffect } from 'react';
import { Button, Row, Col, Table, Space, message, Popconfirm, Radio, Modal } from 'antd';
import { urlStudentApplyQuery, urlStudentApplyAccept, urlStudentApplyReject } from '@/services/url';
import dayjs from 'dayjs';
import lodash from 'lodash';
import { QueryData, OneApplicationData, ApplyAcceptSend, ApplyAcceptGet } from './interface';
import styles from './deal-apply.scss';
import { requestGet, requestPost } from '@/services/request';
import { ExclamationCircleOutlined, PoweroffOutlined, ReloadOutlined } from '@ant-design/icons';

const DealApplyPage: React.FC = () => {
  const [selectedRowKeys, setSelectedRowKeys] = useState<ReactText[]>([]);
  const [multiSelect, setMultiSelect] = useState<boolean>(false);
  const [selectedItems, setSelectedItems] = useState<OneApplicationData[]>([]);
  const [tableData, setTableData] = useState<OneApplicationData[]>([]);
  const [isLoadTableData, setIsLoadTableData] = useState<boolean>(true);
  const [loadingState, setLoadingState] = useState<(1 | 2 | 3)[]>([]);
  const [isRefresh, setIsRefresh] = useState<boolean>(false);

  const onSelectRowKeyChange = (sRowKeys: ReactText[], sItem: OneApplicationData[]) => {
    console.log(sItem);
    setMultiSelect(sItem.length > 0);
    setSelectedRowKeys(sRowKeys);
    setSelectedItems(sItem);
  };

  const rowSelection = {
    selectedRowKeys,
    onChange: onSelectRowKeyChange
  };

  const fetchApplicationDeal = async (stuList: string[], accept: boolean) => {
    return await requestPost<ApplyAcceptSend, ApplyAcceptGet>(accept ? urlStudentApplyAccept : urlStudentApplyReject, {
      handleApplyList: stuList
    });
  };

  const handleOneApplication = async (stuID: string, index: number, accept: boolean) => {
    const [data, ok] = await fetchApplicationDeal([stuID], accept);
    if (ok.succeed && data?.handle) {
      setTableData((prevState) => {
        prevState.splice(index, 1);
        return [...prevState];
      });
      message.success(`${accept ? '同意' : '拒绝'}成功`);
    }

    setLoadingState((prevState) => {
      prevState[index] = 1;
      return [...prevState];
    });
  };

  const getAllSelectedStudent = (): [string[], OneApplicationData[]] => {
    const copy = [...selectedItems];
    const stuList = copy.map((item) => {
      return item.id;
    });
    return [stuList, copy];
  };

  const handleApplications = async (accept: boolean) => {
    const [stuList, selectedList] = getAllSelectedStudent();
    const [data, ok] = await fetchApplicationDeal(stuList, accept);
    if (ok.succeed && data?.handle) {
      message.success(`${accept ? '同意' : '拒绝'}成功`);
      const leftItems = lodash.differenceWith(tableData, selectedList, lodash.isEqual);
      setTableData(leftItems);
      setSelectedItems([]);
      setMultiSelect(false);
    }
  };

  const reFresh = async () => {
    setIsRefresh(true);
    setIsLoadTableData(true);
    init();
  };

  const showPromiseConfirm = (type: boolean) => {
    Modal.confirm({
      title: type ? '确认要批量同意吗？' : '确定要批量拒绝吗？',
      icon: <ExclamationCircleOutlined />,
      onOk: async () => {
        await handleApplications(type);
      }
    });
  };

  const init = async () => {
    const [data, ok] = await requestGet<unknown, QueryData>(urlStudentApplyQuery);
    if (ok.succeed && data) {
      console.log(data);
      if (data.studentApplyList?.length > 0) {
        setTableData(
          data.studentApplyList.map((value) => {
            return {
              ...value,
              key: `${value.id}${value.applyTime}`,
              applyTime: dayjs(value.applyTime).format('YYYY-MM-DD hh:MM')
            };
          })
        );
        setLoadingState(Array(data.studentApplyList.length).fill(1));
        setSelectedItems([]);
        setSelectedRowKeys([]);
        setMultiSelect(false);
      }
    } else {
      message.error(ok.message || '未知错误');
    }
    setTimeout(() => {
      setIsLoadTableData(false);
      setIsRefresh(false);
    }, 200);
  };

  useEffect(() => {
    init();
  }, []);

  return (
    <div className={styles.outer}>
      <Row justify='space-between'>
        <Col>
          <Space>
            <Button
              type='primary'
              className={styles.btn}
              disabled={!multiSelect || isLoadTableData}
              onClick={() => {
                showPromiseConfirm(true);
              }}
            >
              同意
            </Button>
            <Button
              type='primary'
              className={styles.btn}
              disabled={!multiSelect || isLoadTableData}
              danger
              onClick={() => {
                showPromiseConfirm(false);
              }}
            >
              拒绝
            </Button>
          </Space>
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
      <Table rowSelection={rowSelection} dataSource={tableData} style={{ marginTop: '20px' }} loading={isLoadTableData}>
        <Table.Column title='姓名' key='name' dataIndex='name' />
        <Table.Column title='学号' key='id' dataIndex='id' />
        <Table.Column title='专业班级' key='class' dataIndex='class' />
        <Table.Column title='申请时间' key='applyTime' dataIndex='applyTime' />
        <Table.Column
          title='操作'
          key='action'
          render={(text: OneApplicationData, record: OneApplicationData, index) => {
            return (
              <Space>
                <Popconfirm
                  title='确认要同意?'
                  okText='确认'
                  style={{ color: '#1890ff' }}
                  cancelText='取消'
                  onConfirm={() => {
                    setLoadingState((prevState) => {
                      prevState[index] = 2;
                      return [...prevState];
                    });
                    return handleOneApplication(record.id, index, true);
                  }}
                >
                  <Button
                    disabled={multiSelect}
                    style={{ color: multiSelect ? '' : '#1890ff' }}
                    type='text'
                    loading={loadingState[index] === 2}
                  >
                    同意
                  </Button>
                </Popconfirm>

                <Popconfirm
                  title='确认要拒绝?'
                  style={{ color: '#ff0000' }}
                  okText='确认'
                  cancelText='取消'
                  onConfirm={() => {
                    setLoadingState((prevState) => {
                      prevState[index] = 3;
                      return [...prevState];
                    });
                    return handleOneApplication(record.id, index, false);
                  }}
                >
                  <Button disabled={multiSelect} type='text' loading={loadingState[index] === 3} danger>
                    拒绝
                  </Button>
                </Popconfirm>
              </Space>
            );
          }}
        />
      </Table>
    </div>
  );
};

export default DealApplyPage;
