import React, { useEffect, useState } from 'react';
import { EditFilled, PlusOutlined } from '@ant-design/icons';
import { getBase64 } from '@/utils/convert';
import PageHeader from '@/components/page-header/page-header';
import StateChecker from '@/components/state-checker/state-checker';
import { Button, Col, message, Row, Space, Upload } from 'antd';
import { createMessageLoading } from '@/utils/message';
import { requestGet, requestPost } from '@/services/request';
import { urlStudentExportPng, urlStudentUploadPng, urlStudentUploadQuery } from '@/services/url';
import styles from './archive.scss';

interface SendData {
  archive: string;
}

interface GetData {
  uploaded: boolean;
}

interface ArchiveData {
  archivePng: string;
}

const Index: React.FC = () => {
  const [tablePng, setTablePng] = useState<string>();
  const [load, setLoad] = useState<boolean>(false);
  const [canUpload, setCanUpload] = useState<boolean>(true);
  const [checkState, setCheckState] = useState<boolean>(false);

  const handleChange = (info: any) => {
    if (info.file.status === 'done') {
      getBase64(info.file.originFileObj, (imageUrl: string | undefined) => {
        setTablePng(imageUrl);
        setLoad(true);
      });
    }
  };

  const uploadPng = async () => {
    if (canUpload) {
      if (tablePng) {
        setCanUpload(false);
        const loadingMessage = createMessageLoading('student-upload-archive').showLoading('上传中');
        const [data, ok] = await requestPost<SendData, GetData>(urlStudentUploadPng, {
          archive: tablePng || ''
        });
        if (ok.succeed && data) {
          if (data.uploaded) {
            loadingMessage.success('上传成功');
          } else {
            loadingMessage.error('上传失败');
          }
        } else {
          loadingMessage.hideLoading();
        }
        setCanUpload(true);
      } else {
        message.info('请先选择图片再上传');
      }
    }
  };

  const fetchData = async () => {
    const loadingMessage = createMessageLoading('student-archive-query').showLoading('加载中');
    const [data, ok] = await requestGet<unknown, ArchiveData>(urlStudentUploadQuery);
    if (ok.succeed && data) {
      setLoad(true);
      setTablePng(data.archivePng);
    }
    loadingMessage.hideLoading();
  };

  useEffect(() => {
    checkState && fetchData();
  }, [checkState]);

  return checkState ? (
    <div className={styles.outer}>
      <PageHeader Icon={EditFilled} headContent='鉴定表存档' />
      <div className={styles.main}>
        <Upload
          className={styles.upload}
          accept='.png,.jpg,.webp'
          disabled={!load}
          showUploadList={false}
          onChange={handleChange}
          action={(file) => {
            getBase64(file, (imageUrl: string | undefined) => {
              setTablePng(imageUrl);
              setLoad(true);
            });
            return '';
          }}
        >
          {(tablePng && (
            <div
              className={styles.avatarWrapper}
              style={{
                backgroundImage: `url(${tablePng})`,
                backgroundColor: 'transparent'
              }}
            />
          )) || (
            <div className={styles.avatarWrapper}>
              <PlusOutlined className={styles.avatar} />
            </div>
          )}
        </Upload>
        <Row>
          <Space size='large'>
            <Col>
              <Button style={{ borderRadius: '8px' }} href={urlStudentExportPng}>
                导出鉴定表
              </Button>
            </Col>
            <Col>
              <Button disabled={!load} onClick={uploadPng} style={{ borderRadius: '8px' }}>
                上传鉴定表
              </Button>
            </Col>
          </Space>
        </Row>
      </div>
    </div>
  ) : (
    <StateChecker setCheckState={setCheckState} highLevel />
  );
};

export default Index;
