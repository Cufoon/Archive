import React, { useState, useEffect } from 'react';
import { Result } from 'antd';
import dayjs from 'dayjs';
import { createMessageLoading } from '@/utils/message';
import { requestGet } from '@/services/request';
import { urlStudentCheckState } from '@/services/url';
import styles from './state-checker.scss';

interface Props {
  setCheckState: React.Dispatch<React.SetStateAction<boolean>>;
  highLevel?: boolean;
}

interface GetData {
  flag: number;
  date: string;
}

const Index: React.FC<Props> = ({ setCheckState, highLevel = false }) => {
  const [init, setInit] = useState<boolean>(false);
  const [title, setTitle] = useState<string>();
  const [content, setContent] = useState<string>();

  useEffect(() => {
    let living = true;
    (async () => {
      const loadingMessage = createMessageLoading('student-check-state').showLoading('加载中');
      const [data, ok] = await requestGet<unknown, GetData>(urlStudentCheckState);
      if (ok.succeed && data && living) {
        switch (data.flag) {
          case 0:
            setTitle('管理员未开启本次实习');
            setContent('请等待管理员开启此次实习');
            setInit(true);
            break;
          case 1:
            const startDate = dayjs(data.date);
            setTitle('还未到实习开始的时间');
            setContent(`实习开始时间为: ${startDate.year()}年${startDate.format('MM')}月${startDate.date()}日`);
            setInit(true);
            break;
          // case 2:
          //   const endDate = dayjs(data.date);
          //   setTitle('本次实习已经结束');
          //   setContent(`本次实习结束于: ${endDate.year()}年${endDate.format('MM')}月${endDate.date()}日`);
          //   setInit(true);
          //   break;
          default:
            if (highLevel) {
              switch (data.flag) {
                case 4:
                  setTitle('未填写实习信息');
                  setContent('完成实习信息且确定校内导师后才能填写实习材料');
                  setInit(true);
                  break;
                case 5:
                  setTitle('未确定校内导师');
                  setContent('完成实习信息且选择校内导师后才能填写实习材料');
                  setInit(true);
                  break;
                case 6:
                  setTitle('未填写实习信息且未确定校内导师');
                  setContent('完成实习信息且确定校内导师后才能填写实习材料');
                  setInit(true);
                  break;
                default:
                  setCheckState(true);
              }
            } else {
              setCheckState(true);
            }
        }
      }
      loadingMessage.hideLoading();
    })();
    return () => {
      living = false;
    };
  }, []);
  return init ? (
    <div className={styles.outer}>
      <Result status='info' title={title} subTitle={content} />
    </div>
  ) : null;
};

export default Index;
