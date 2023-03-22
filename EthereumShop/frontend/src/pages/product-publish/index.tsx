import React from 'react';
import { Button, Form, Input, Modal, Select, Upload } from '@arco-design/web-react';
import { useSelector } from '$store/hook';
import { MyContract } from '$service/eth';
import { IPFSClient } from '$service/ipfs';
import { createMessageLoading, GlobalMessage } from '$utils/message';
import type { ImportCandidate } from 'ipfs-core-types/dist/src/utils';

import styles from './index.scss';

const FormItem = Form.Item;

interface StoreInstance {
  buy: (arg0: { from: any; value: any; gas: number }) => Promise<any>;
  sell: (
    arg0: string | number | string[] | undefined,
    arg1: { from: any; gas: number }
  ) => Promise<any>;
  getBalanceInfo: { call: () => Promise<any> };
  publish: (
    arg0: any,
    arg1: any,
    arg2: any,
    arg3: any,
    arg4: any,
    arg5: any,
    arg6: any,
    arg7: { from: string | undefined }
  ) => Promise<any>;
}

interface PushProductFormData {
  productName: string;
  productType: string;
  price: number;
  introduction: string;
  usedRule: string;
  cover: any[];
  file: any[];
}

const Index: React.FC = () => {
  const accountId = useSelector((store) => store.account_id);

  const upload = async (file: ImportCandidate) => {
    try {
      const added = await IPFSClient.add(file, {
        progress: (p) => console.log(`received: ${p}`)
      });
      const hash = added.cid.toString();
      console.log('hash', hash);
      return `http://localhost:8080/ipfs/${hash}`;
    } catch (error) {
      console.log(error);
      return '';
    }
  };

  const handlePublish = async function (
    name: any,
    style: any,
    intro: any,
    rules: any,
    price: any,
    cover: any,
    file: any
  ) {
    const store = await MyContract.deployed().then((v: StoreInstance) => v);
    try {
      await store.publish(name, style, intro, rules, price, cover, file, {
        from: accountId
      });
      return true;
    } catch (err) {
      console.error(err);
      return false;
    }
  };

  const publish = async function (values: PushProductFormData) {
    const msl = createMessageLoading('product-publish', 20);
    msl.showLoading('正在发布...');

    const name = values.productName;
    const style = values.productType;
    const intro = values.introduction;
    const rules = values.usedRule;
    const price = values.price;
    const cover = (values.cover[0].response as any).data as Blob;
    const file = (values.file[0].response as any).data as Blob;

    msl.updateLoading('上传封面...');
    const coverURL = await upload(cover);
    console.log('coverURL', coverURL);
    msl.updateLoading('上传文件...');
    const fileURL = await upload(file);
    console.log('fileURL', fileURL);
    msl.updateLoading('发布中...');
    const isSuccess = await handlePublish(name, style, intro, rules, price, coverURL, fileURL);
    if (isSuccess) {
      msl.success('发布成功,等待写入区块!');
      window.location.reload();
    } else {
      msl.error('发布失败');
    }
  };

  const isAcceptFile = (file: File | undefined, accept: string) => {
    if (accept && file) {
      const accepts = Array.isArray(accept)
        ? accept
        : accept
            .split(',')
            .map((x: string) => x.trim())
            .filter((x: any) => x);
      const fileExtension = file.name.indexOf('.') > -1 ? file.name.split('.').pop() : '';
      return accepts.some((type: string) => {
        const text = type && type.toLowerCase();
        const fileType = (file.type || '').toLowerCase();
        if (text === fileType) {
          return true;
        }
        if (/\/*/.test(text)) {
          return fileType.replace(/\/.*$/, '') === text.replace(/\/.*$/, '');
        }
        if (/.+/.test(text)) {
          // .jpg 等后缀名
          return text === `.${fileExtension && fileExtension.toLowerCase()}`;
        }
        return false;
      });
    }
    return !!file;
  };

  const [form] = Form.useForm<PushProductFormData>();

  return (
    <div className={styles.container}>
      <Form<PushProductFormData>
        form={form}
        style={{ width: 600 }}
        autoComplete='off'
        onSubmit={publish}
      >
        <FormItem label='商品名称' field='productName' rules={[{ required: true }]}>
          <Input />
        </FormItem>
        <FormItem label='类型' field='productType' initialValue='绘画' rules={[{ required: true }]}>
          <Select
            options={[
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
            allowClear
          />
        </FormItem>
        <FormItem label='价格' field='price' required>
          <Input />
        </FormItem>
        <FormItem label='简介' field='introduction' required>
          <Input.TextArea />
        </FormItem>
        <FormItem label='使用限制' field='usedRule' required>
          <Input.TextArea />
        </FormItem>
        <Form.Item required label='封面' field='cover' triggerPropName='fileList'>
          <Upload
            name='files'
            accept='image/*'
            limit={1}
            listType={'picture-card'}
            showUploadList
            customRequest={async (option) => {
              const { onProgress, onSuccess, onError, file } = option;
              if (!isAcceptFile(file, 'image/*')) {
                onError({ error: '文件类型错误' });
                GlobalMessage.warning('封面只能是图片！');
                return;
              }
              onProgress(0);
              const buffer = await file.arrayBuffer();
              onProgress(100);
              onSuccess({
                data: new Blob([buffer], { type: file.type })
              });
            }}
            onPreview={async (file) => {
              if (file.response === undefined) return;
              const blob = (file.response as any).data as Blob;
              Modal.info({
                title: '封面预览',
                content: (
                  <img
                    src={URL.createObjectURL(blob)}
                    style={{
                      maxWidth: '100%'
                    }}
                    alt='preview'
                  />
                )
              });
            }}
          />
        </Form.Item>
        <Form.Item required label='文件' field='file' triggerPropName='fileList'>
          <Upload
            drag
            onDrop={(e) => {
              console.log('拖拽上传', e.dataTransfer.files);
            }}
            customRequest={async (option) => {
              const { onProgress, onSuccess, file } = option;
              onProgress(0);
              const buffer = await file.arrayBuffer();
              onProgress(100);
              onSuccess({
                data: new Blob([buffer], { type: file.type })
              });
            }}
          />
        </Form.Item>
        <FormItem wrapperCol={{ offset: 5 }}>
          <Button type='primary' htmlType='submit' style={{ marginRight: 24 }}>
            发布
          </Button>
        </FormItem>
      </Form>
    </div>
  );
};

export default Index;
