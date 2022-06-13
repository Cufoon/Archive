import React, { useEffect, useState } from 'react';
import { Button, Col, Descriptions, Form, Input, InputNumber, message, Modal, Row, Space, Spin, Table } from 'antd';
import { requestGet, requestPost } from '@/services/request';
import { urlAdminQuery, urlAdminScore, urlScoreEnter } from '@/services/url';
import DescriptionsItem from 'antd/lib/descriptions/Item';
import { level } from '@/pages/studentDocument/table/table';
import styles from './score.scss';

//
interface User {
  key?: any;
  name: string;
  id: string;
}

interface Score {
  score1: number;
  score2: number;
  score3: number;
  score4: number;
  score5: number;
  score6: number;
  score7: number;
  score8: number;
  score9: number;
  score10: number | null;
  sum: number | null;
}

interface QueryResponse {
  formData: User[];
}

interface EditParam {
  studentId: string;
  groupScore: number;
}

interface EditResponse {
  entered: boolean;
}

interface ValidateScore {
  value: number;
  validateStatus: any;
  errMsg: string;
}

const AdminScorePage: React.FC = () => {
  const [groupCurrent, setGroupCurrent] = useState<string>('');
  const [scoreCurrent, setScoreCurrent] = useState<string>('');
  const [isLoadingTableData, setIsLoadingTableData] = useState<boolean>(true);
  const [isLoadingScore, setIsLoadingScore] = useState<boolean>(true);
  const [info, setInfo] = useState<User[]>();
  const [validateScore, setValidateScore] = useState<ValidateScore>();
  const [score, setScore] = useState<Score>();

  const editHandle = (param: EditParam, record: string) => {
    if (param.groupScore < 0 || param.groupScore > 100) return;
    const enterScore = async () => {
      try {
        const [res, ok] = await requestPost<EditParam, EditResponse>(urlScoreEnter, {
          studentId: record,
          groupScore: param.groupScore
        });
        if (ok.succeed && res?.entered) {
          message.success('录入答辩成绩成功');
          setGroupCurrent('');
        } else {
          message.error('录入答辩成绩失败');
        }
      } catch (e) {
        message.error('录入答辩成绩失败');
      }
    };
    enterScore();
  };

  const queryScore = async (sid: string) => {
    try {
      setIsLoadingScore(true);
      const [res, ok] = await requestPost<any, Score>(urlAdminScore, { studentId: sid });
      if (ok.succeed && res) {
        console.log(res);
        setScore(res);
        setIsLoadingScore(false);
      }
    } catch (e) {
      message.error('获取成绩失败');
      setIsLoadingScore(false);
    }
  };

  const onChange = (value: any) => {
    setValidateScore({
      value,
      ...validate(value)
    });
  };

  const validate = (score: number) => {
    if (score < 0 || score > 100) {
      return {
        validateStatus: 'error',
        errMsg: '分数超出范围'
      };
    } else {
      return {
        validateStatus: 'success',
        errMsg: ''
      };
    }
  };

  const initScore = () => {
    setScore({
      score1: 0,
      score10: null,
      score2: 0,
      score3: 0,
      score4: 0,
      score5: 0,
      score6: 0,
      score7: 0,
      score8: 0,
      score9: 0,
      sum: null
    });
  };

  const tips = '分数范围为0~100';

  const renderAction = (record: string) => {
    const current = info?.find((e: User) => e.id === record);
    return (
      <div>
        <Button
          data-id={record}
          type={'link'}
          onClick={() => {
            setGroupCurrent(record);
          }}
          style={{ paddingLeft: '0' }}
        >
          录入答辩成绩
        </Button>
        <Modal
          visible={groupCurrent == record}
          onCancel={() => {
            setGroupCurrent('');
          }}
          footer={null}
          centered
          destroyOnClose
          title={'答辩成绩录入'}
        >
          <Form
            onFinish={(param: EditParam) => {
              editHandle(param, record);
            }}
            layout={'inline'}
            id={'score'}
          >
            <Form.Item label='姓名'>
              <Input disabled={true} value={current ? current.name : ''} />
            </Form.Item>
            <Form.Item
              label='成绩'
              name='groupScore'
              rules={[{ required: true }]}
              validateStatus={validateScore?.validateStatus}
              help={validateScore?.errMsg || tips}
            >
              <InputNumber onChange={onChange} />
            </Form.Item>
          </Form>
          <div className={styles.buttonGroup}>
            <Row>
              <Col span={12} style={{ textAlign: 'center' }}>
                <Button
                  className={styles.button}
                  onClick={() => {
                    setGroupCurrent('');
                  }}
                >
                  取消
                </Button>
              </Col>
              <Col span={12} style={{ textAlign: 'center' }}>
                <Button className={styles.button} type='primary' htmlType='submit' form='score'>
                  提交
                </Button>
              </Col>
            </Row>
          </div>
        </Modal>
        <Button
          data-id={record}
          type={'link'}
          style={{ paddingLeft: '0' }}
          onClick={() => {
            setScoreCurrent(record);
            queryScore(record);
          }}
        >
          查看过程评分
        </Button>
        <Modal
          visible={scoreCurrent == record}
          onCancel={() => {
            setScoreCurrent('');
            initScore();
          }}
          footer={null}
          centered
          destroyOnClose
          title={'过程评分'}
        >
          <Spin spinning={isLoadingScore}>
            <Descriptions bordered size={'middle'} title={current?.name} column={1}>
              <DescriptionsItem label={'校外实践情况阶段汇报表1'}>
                {score && score.score1 != 0 ? level[score.score1 - 1] : ''}
              </DescriptionsItem>
              <DescriptionsItem label={'校外实践情况阶段汇报表2'}>
                {score && score.score2 != 0 ? level[score.score2 - 1] : ''}
              </DescriptionsItem>
              <DescriptionsItem label={'校外实践情况阶段汇报表3'}>
                {score && score.score3 != 0 ? level[score.score3 - 1] : ''}
              </DescriptionsItem>
              <DescriptionsItem label={'校外实践阶段检查表1'}>
                {score && score.score4 != 0 ? level[score.score4 - 1] : ''}
              </DescriptionsItem>
              <DescriptionsItem label={'校外实践阶段检查表2'}>
                {score && score.score5 != 0 ? level[score.score5 - 1] : ''}
              </DescriptionsItem>
              <DescriptionsItem label={'校外实践阶段检查表3'}>
                {score && score.score6 != 0 ? level[score.score6 - 1] : ''}
              </DescriptionsItem>
              <DescriptionsItem label={'校外实践鉴定表'}>
                {score && score.score7 != 0 ? level[score.score7 - 1] : ''}
              </DescriptionsItem>
              <DescriptionsItem label={'校外实践企业导师评价表'}>
                {score && score.score8 != 0 ? level[score.score8 - 1] : ''}
              </DescriptionsItem>
              <DescriptionsItem label={'校外实践总结报告'}>
                {score && score.score9 != 0 ? level[score.score9 - 1] : ''}
              </DescriptionsItem>
              <DescriptionsItem label={'小组答辩成绩'}>
                {score && score.score10 != null ? score.score10.toString() : ''}
              </DescriptionsItem>
              <DescriptionsItem label={'总分'}>
                {score && score.sum != null ? score.sum.toString() : ''}
              </DescriptionsItem>
            </Descriptions>
          </Spin>
        </Modal>
      </div>
    );
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [res, ok] = await requestGet<null, QueryResponse>(`${urlAdminQuery}?type=1`);
        if (ok.succeed && res) {
          setInfo(res.formData);
          setIsLoadingTableData(false);
        }
      } catch (e) {
        message.error('获取失败');
      }
    };
    fetchData();
  }, []);

  return (
    <div className={styles.background}>
      <div className={styles.content}>
        <div className={styles.title}>成绩管理</div>
        {/*<div className={styles.toolBar}></div>*/}
        <Table
          dataSource={info}
          style={{ marginTop: '20px' }}
          className={styles.table}
          tableLayout={'auto'}
          loading={isLoadingTableData}
        >
          <Table.Column title='姓名' dataIndex='name' key='name' />
          <Table.Column title='学号' dataIndex='id' key='id' />
          <Table.Column
            title='操作'
            dataIndex='id'
            key='id'
            render={(record: string) => <Space>{renderAction(record)}</Space>}
          />
        </Table>
      </div>
    </div>
  );
};

export default AdminScorePage;
