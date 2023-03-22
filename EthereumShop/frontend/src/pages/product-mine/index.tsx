import React, { useRef, useState } from 'react';
import {
  List,
  Radio,
  Input,
  Select,
  Card,
  Space,
  Button,
  Modal,
  Rate
} from '@arco-design/web-react';
import { IconCompass, IconEye, IconHighlight, IconStar } from '@arco-design/web-react/icon';
import { GlobalMessage } from '$utils/message';
import { useAPI } from './service';

import styles from './index.scss';

const InputSearch = Input.Search;

const Index: React.FC = () => {
  const [position, setPosition] = useState(1);
  const [visible, setVisible] = useState(false);
  const [commentVisible, setCommentVisible] = useState<boolean>(false);
  const evaluateIdRef = useRef('');
  const preCommentLookIdRef = useRef(-1);
  const [rate, setRate] = useState(0);
  const [comment, setComment] = useState('');
  const [commentLoading, setCommentLoading] = useState(false);

  const [commentList, setCommentList] = useState<any[]>([]);

  const {
    accountId,
    evaluate,
    getCommentListPbp,
    getCommentListPcp,
    loading,
    pbpList,
    pcpList,
    resetSearch,
    searchInput,
    seeProductFile,
    selectType,
    setSearch,
    setSearchInput,
    setSelectType
  } = useAPI();

  return (
    <div>
      <Radio.Group
        size='large'
        type='button'
        name='position'
        value={position}
        onChange={setPosition}
        style={{ marginBottom: 20 }}
        options={[
          {
            label: '已购买的商品',
            value: 0
          },
          {
            label: '发布的商品',
            value: 1
          }
        ]}
      />
      <Card title='筛选' style={{ marginBottom: 20 }}>
        <List
          grid={{
            sm: 24,
            md: 24,
            lg: 12,
            xl: 12
          }}
          bordered={false}
          split={false}
          style={{ padding: '0' }}
        >
          <List.Item style={{ marginBottom: '15px', padding: '0' }}>
            <Space align='center'>
              <div>商品名称</div>
              <InputSearch
                value={searchInput}
                onChange={(value) => {
                  setSearchInput(value);
                }}
                searchButton
                onSearch={(value) => {
                  setSearch(value);
                }}
                placeholder='输入关键字进行搜索'
                style={{ width: 271 }}
              />
            </Space>
          </List.Item>
          <List.Item style={{ padding: '0' }}>
            <Space align='center'>
              <div>商品类型</div>
              <Select
                style={{ width: 200 }}
                onChange={(value) => {
                  setSelectType(value);
                }}
                defaultValue='所有'
                value={selectType}
                options={[
                  {
                    label: '所有',
                    value: '所有'
                  },
                  {
                    label: '绘画',
                    value: '绘画'
                  },
                  {
                    label: '摄影',
                    value: '摄影'
                  },
                  {
                    label: '音乐',
                    value: '音乐'
                  },
                  {
                    label: '视频',
                    value: '视频'
                  },
                  {
                    label: '文章',
                    value: '文章'
                  },
                  {
                    label: '设计',
                    value: '设计'
                  }
                ]}
              />
              <Button type='primary' onClick={resetSearch}>
                重 置
              </Button>
            </Space>
          </List.Item>
        </List>
      </Card>
      <List
        bordered
        loading={loading}
        grid={{
          sm: 24,
          md: 24,
          lg: 12,
          xl: 12
        }}
        className={styles.listDemoActionLayout}
        style={{ padding: '10px 10px 0 10px' }}
        pagination={{ pageSize: 20 }}
        dataSource={position == 0 ? pcpList : pbpList}
        render={(item) => (
          <List.Item
            key={item.id}
            style={{
              marginRight: '10px',
              marginBottom: '10px',
              padding: '20px',
              borderRadius: '12px',
              border: '1px solid #e9e9e9'
            }}
            actionLayout='vertical'
            actions={[
              <div key={1}>
                <span
                  style={{
                    display: 'flex',
                    justifyContent: 'flex-start',
                    alignItems: 'center',
                    color: '#165DFF'
                  }}
                  onClick={() => {
                    seeProductFile(item.id).then();
                  }}
                >
                  <IconEye style={{ color: '#165DFF' }} />
                  {'查看商品'}
                </span>
                <span
                  key='op2'
                  onClick={async () => {
                    if (item.commentNum <= 0) {
                      GlobalMessage.info('暂无评论！', {
                        duration: 1000,
                        id: 'shop-home-no-comment'
                      });
                      return;
                    }
                    const nextId = parseInt(item.id);
                    console.log('id to look comments', nextId);
                    if (nextId !== preCommentLookIdRef.current) {
                      setCommentList([]);
                      setCommentLoading(true);
                      setCommentVisible(true);
                      try {
                        const result =
                          position === 0
                            ? await getCommentListPcp(nextId)
                            : await getCommentListPbp(nextId);
                        setCommentList(result);
                        preCommentLookIdRef.current = nextId;
                      } catch (e) {
                        console.log('load comments error:', e);
                        GlobalMessage.error('评论加载失败');
                      }
                      setCommentLoading(false);
                    }
                    setCommentVisible(true);
                  }}
                  style={{
                    userSelect: 'none',
                    display: 'flex',
                    justifyContent: 'flex-start',
                    alignItems: 'center',
                    color: item.commentNum <= 0 ? '#808080' : '#165DFF'
                  }}
                >
                  <IconCompass style={{ color: item.commentNum <= 0 ? '#808080' : '#165DFF' }} />
                  {'查看评论'}
                </span>
                {position === 0 &&
                  ((item.evaluated && (
                    <span
                      onClick={() => {
                        GlobalMessage.info('你已经评价过了哦', {
                          id: 'product-mine-evaluated-tip'
                        });
                      }}
                    >
                      <Rate
                        allowHalf
                        value={parseFloat(((item.score as unknown as number) / 2).toFixed(1))}
                        count={5}
                        readonly
                      />
                    </span>
                  )) || (
                    <span
                      onClick={() => {
                        evaluateIdRef.current = item.id;
                        setVisible(true);
                      }}
                      style={{
                        userSelect: 'none',
                        display: 'flex',
                        justifyContent: 'flex-start',
                        alignItems: 'center',
                        color: '#165DFF'
                      }}
                    >
                      <IconStar />
                      {'评价商品'}
                    </span>
                  ))}
              </div>
            ]}
            extra={
              <div className='image-area'>
                <img alt='intro' src={item.cover} />
              </div>
            }
          >
            <List.Item.Meta
              title={`商品名称：${item.name}`}
              description={`商品介绍：${item.introduction}`}
            />
            <div>{`销量：${item.sales}`}</div>
            <div>{`评分：${item.score}`}</div>
            <div>{`类型：${item.type}`}</div>
            <div>{`使用规则：${item.usedRules}`}</div>
          </List.Item>
        )}
      />
      <Modal
        title='商品评价'
        visible={visible}
        onOk={async () => {
          if (rate === 0) {
            GlobalMessage.info('最低只能给半颗星的评价！', {
              id: 'wrong-when-submit-score',
              duration: 1000
            });
            return;
          }
          setVisible(false);
          let evaluateSuccess = false;
          evaluateIdRef.current !== null &&
            (evaluateSuccess = await evaluate(
              evaluateIdRef.current,
              Math.floor(rate * 2),
              comment
            ));
          if (evaluateSuccess) {
            setRate(0);
            setComment('');
            preCommentLookIdRef.current = -1;
          }
        }}
        onCancel={() => setVisible(false)}
        autoFocus={false}
        focusLock={true}
      >
        <p>给这个商品打个分吧！</p>
        <Rate allowHalf count={5} allowClear value={rate} onChange={(value) => setRate(value)} />
        <p>要给商品添加一个评论吗？</p>
        <Input.TextArea value={comment} onChange={(value) => setComment(value)} />
      </Modal>
      <Modal
        style={{ borderRadius: '12px' }}
        title='评论查看'
        visible={commentVisible}
        hideCancel
        onOk={() => setCommentVisible(false)}
        onCancel={() => setCommentVisible(false)}
        okButtonProps={{
          style: { borderRadius: '12px' }
        }}
        okText='关闭'
        closable={false}
        autoFocus={false}
        focusLock={true}
      >
        <List
          loading={commentLoading}
          bordered
          grid={{
            column: 1,
            align: 'stretch'
          }}
          style={{ borderRadius: '12px', maxHeight: '65vh' }}
          className={styles.listDemoActionLayout}
          dataSource={commentList}
          render={(item) => {
            return (
              <List.Item
                key={`${item.buyer}${item.date}`}
                style={{ padding: '0', border: '1px solid #e9e9e9', borderRadius: '12px' }}
              >
                <Card
                  style={{ margin: '0', borderRadius: '12px' }}
                  title={
                    <div
                      style={{
                        display: 'flex',
                        justifyContent: 'space-between',
                        alignItems: 'center'
                      }}
                    >
                      <div>{item.buyer}</div>
                      <div>
                        {item.buyer === accountId && <IconHighlight style={{ color: '#165DFF' }} />}
                      </div>
                    </div>
                  }
                  actions={[
                    <span key={1}>{new Date(parseInt(item.date) * 1000).toLocaleString()}</span>
                  ]}
                >
                  <Space style={{ width: '100%' }}>
                    <span>评分：</span>
                    <span>
                      <Rate
                        allowHalf
                        value={Math.floor(item.score as unknown as number) / 2}
                        count={5}
                        readonly
                      />
                    </span>
                  </Space>
                  <Space style={{ width: '100%' }}>
                    <span>评价：</span>
                    <span>{item.content}</span>
                  </Space>
                </Card>
              </List.Item>
            );
          }}
        />
      </Modal>
    </div>
  );
};

export default Index;
