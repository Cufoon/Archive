import React, { useEffect, useRef, useState } from 'react';
import { Button, Card, Input, List, Modal, Rate, Select, Space } from '@arco-design/web-react';
import {
  IconCompass,
  IconHighlight,
  IconLock,
  IconThunderbolt,
  IconUnlock
} from '@arco-design/web-react/icon';
import { GlobalMessage } from '$utils/message';
import { ProductComment, useAPI } from './service';

import styles from './index.scss';

const InputSearch = Input.Search;
const Index: React.FC = () => {
  const [commentVisible, setCommentVisible] = useState<boolean>(false);
  const [confirmBuyModalVisible, setConfirmBuyModalVisible] = useState<boolean>(false);
  const purchaseIdRef = useRef<string | null>(null);
  const [toLookCommentId, setToLookCommentId] = useState(-1);
  const [commentLoading, setCommentLoading] = useState(false);
  const {
    productList,
    seeProductFile,
    setSearch,
    resetSearch,
    setSelectType,
    searchInput,
    setSearchInput,
    selectType,
    buy,
    loading,
    accountId,
    getCommentList
  } = useAPI();

  const [commentList, setCommentList] = useState<ProductComment[]>([]);

  useEffect(() => {
    (async () => {
      if (toLookCommentId > -1) {
        setCommentLoading(true);
        const result = await getCommentList(toLookCommentId);
        console.log('comments', result);
        setCommentList(result);
        setCommentLoading(false);
      }
    })();
  }, [toLookCommentId]);

  return (
    <div>
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
        loading={loading}
        bordered
        grid={{
          column: 1
        }}
        className={styles.listDemoActionLayout}
        style={{ padding: '10px 10px 0 10px' }}
        pagination={{ pageSize: 8 }}
        dataSource={productList}
        render={(item) => {
          const isOwner = item.owner === accountId;
          const canSee = isOwner || item.purchased;
          return (
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
                <span
                  key='op1'
                  onClick={() => {
                    if (canSee) {
                      seeProductFile(item.id).then();
                      return;
                    }
                    GlobalMessage.info('购买后才能查看！', { duration: 1000 });
                  }}
                  style={{
                    userSelect: 'none',
                    display: 'flex',
                    justifyContent: 'flex-start',
                    alignItems: 'center',
                    color: canSee ? '#165DFF' : '#808080'
                  }}
                >
                  {(canSee && <IconUnlock style={{ color: '#165DFF' }} />) || (
                    <IconLock style={{ color: '#808080' }} />
                  )}
                  {'查看商品'}
                </span>,
                <span
                  key='op2'
                  onClick={() => {
                    if (item.commentNum <= 0) {
                      GlobalMessage.info('暂无评论！', {
                        duration: 1000,
                        id: 'shop-home-no-comment'
                      });
                      return;
                    }
                    const nextId = parseInt(item.id);
                    console.log('id to look comments', nextId);
                    if (toLookCommentId !== nextId) {
                      setCommentList([]);
                    }
                    setToLookCommentId(nextId);
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
                </span>,
                <span
                  key='op3'
                  style={{
                    userSelect: 'none',
                    display: 'flex',
                    justifyContent: 'flex-start',
                    alignItems: 'center',
                    color: (canSee && '#808080') || '#165DFF'
                  }}
                  onClick={() => {
                    if (isOwner) {
                      GlobalMessage.info('不能购买自己发布的商品！', { duration: 1000 });
                      return;
                    }
                    if (item.purchased) {
                      GlobalMessage.info('已经购买过了！', { duration: 1000 });
                      return;
                    }
                    purchaseIdRef.current = item.id;
                    setConfirmBuyModalVisible(true);
                  }}
                >
                  <IconThunderbolt style={{ color: (canSee && '#808080') || '#165DFF' }} />
                  {isOwner
                    ? '这个商品是你发布的'
                    : (item.purchased && '已经购买') || `购买${item.price}TXB`}
                </span>
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
          );
        }}
      />
      <Modal
        title='购买提示'
        visible={confirmBuyModalVisible}
        onOk={async () => {
          setConfirmBuyModalVisible(false);
          purchaseIdRef.current !== null && (await buy(purchaseIdRef.current));
        }}
        onCancel={() => setConfirmBuyModalVisible(false)}
        autoFocus={false}
        focusLock={true}
      >
        <p>确认要购买吗？购买成功后不支持退款</p>
      </Modal>
      <Modal
        style={{ borderRadius: '12px' }}
        title='评论查看'
        visible={commentVisible}
        hideCancel
        onOk={() => setCommentVisible(false)}
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
