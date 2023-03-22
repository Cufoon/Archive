import { requestPost } from '@/services/request';
import { urlQueryStudent } from '@/services/url';
import { SmileOutlined } from '@ant-design/icons';
import { Button, message, Modal, Spin } from 'antd';
import React, { useEffect, useState } from 'react';
import styles from './student-modal.scss';

interface ModalProps {
  studentId: string;
  visible: boolean;
  closeModal: () => void;
}

interface QueryResponse {
  name: string;
  id: string;
  class: string;
  sex: 'M' | 'F';
  phone: string;
  email: string;
  epName: string;
  epCity: string;
  eptName: string;
  eptEmail: string;
  eptPhone: string;
  avatar: string;
}

const StudentModal: React.FC<ModalProps> = (param: ModalProps) => {
  const [info, setInfo] = useState<QueryResponse>({} as QueryResponse);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const { visible, closeModal, studentId } = param;
  console.log(studentId);

  useEffect(() => {
    (async () => {
      const [res, ok] = await requestPost<ModalProps, QueryResponse>(urlQueryStudent, {
        studentId: studentId
      } as ModalProps);
      if (ok.succeed && res) {
        setInfo(res);
      } else {
        console.log(ok.message);
        message.error('未知错误');
      }
      setTimeout(() => {
        setIsLoading(false);
      }, 200);
    })();
  }, []);

  const convertSex = (sex: 'M' | 'F') => {
    return sex === 'M' ? '男' : '女';
  };

  return (
    <Modal title='学生信息' visible={visible} onCancel={closeModal} footer={null} centered>
      <Spin spinning={isLoading}>
        <div className={styles.container}>
          <div className={styles.top}>
            <div
              className={styles.leftPart}
              style={{
                backgroundImage: `url(${info.avatar})`,
                backgroundColor: 'transparent'
              }}
            >
              {!info?.avatar && <SmileOutlined />}
            </div>
            <div className={styles.rightPart}>
              <div className={styles.line}>
                <div className={styles.textBox}>姓名：{info.name}</div>
                <div className={styles.textBox}>学号：{info.id}</div>
              </div>
              <div className={styles.line}>
                <div className={styles.textBox}>班级：{info.class}</div>
                <div className={styles.textBox}>性别：{convertSex(info.sex)}</div>
              </div>
              <div className={styles.line}>手机：{info.phone}</div>
              <div className={styles.line}>邮箱：{info.email}</div>
              <div className={styles.line}>实习企业：{info.epName}</div>
              <div className={styles.line}>实习城市：{info.epCity}</div>
              <div className={styles.line}>企业导师姓名：{info.eptName}</div>
              <div className={styles.line}>企业导师手机：{info.eptPhone}</div>
              <div className={styles.line}>企业导师邮箱：{info.eptEmail}</div>
            </div>
          </div>
          <div className={styles.btnGroup}>
            <Button type='primary' size='middle' onClick={closeModal} className={styles.btn}>
              关闭
            </Button>
          </div>
        </div>
      </Spin>
    </Modal>
  );
};

export default StudentModal;
