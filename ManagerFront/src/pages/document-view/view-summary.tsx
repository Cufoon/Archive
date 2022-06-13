import React, { useEffect, useState } from 'react';
import { requestGet, requestPost } from '@/services/request';
import { urlStudentLookDocument, urlTeacherHandleDocument } from '@/services/url';
import { Descriptions, Spin } from 'antd';
import DescriptionsItem from 'antd/es/descriptions/Item';
import { level } from '@/pages/studentDocument/table/table';

interface Props {
  sid?: string;
  order: number;
  type: 'Student' | 'Teacher';
}

interface QueryResponse {
  className: string;
  name: string;
  sid: string;
  phone: string;
  summaryReport: string;
  evaluation: number;
  teacherSuggestion: string;
}

interface QueryParam {
  category: number;
  order: number;
}

const ViewSummary: React.FC<Props> = ({ sid, order, type }) => {
  const [info, setInfo] = useState<QueryResponse>();
  const [isLoading, setIsLoading] = useState<boolean>(true);

  const queryReportStudent = async () => {
    const [res, ok] = await requestPost<QueryParam, QueryResponse>(urlStudentLookDocument, {
      category: 5,
      order: order
    });
    if (ok.succeed && res) {
      setIsLoading(false);
      setInfo(res);
    }
  };
  const queryReportTeacher = async () => {
    const [res, ok] = await requestGet<null, QueryResponse>(
      `${urlTeacherHandleDocument}?sid=${sid}&category=5&order=${order}`
    );
    if (ok.succeed && res) {
      setIsLoading(false);
      setInfo(res);
    }
  };

  useEffect(() => {
    if (type == 'Teacher') {
      queryReportTeacher();
    } else {
      queryReportStudent();
    }
  }, []);

  return (
    <div>
      <Spin spinning={isLoading}>
        <Descriptions style={{ marginTop: '30px' }} title={'基础信息'} bordered size={'middle'}>
          <DescriptionsItem label={'专业班级'} span={2}>
            {info?.className}
          </DescriptionsItem>
          <DescriptionsItem label={'学生姓名'} span={2}>
            {info?.name}
          </DescriptionsItem>
          <DescriptionsItem label={'学号'} span={2}>
            {info?.sid}
          </DescriptionsItem>
          <DescriptionsItem label={'电话'} span={2}>
            {info?.phone}
          </DescriptionsItem>
        </Descriptions>
        <Descriptions style={{ marginTop: '30px' }} title={'实习总结'} bordered size={'middle'}>
          <DescriptionsItem label={'总结报告'} span={4}>
            {info?.summaryReport}
          </DescriptionsItem>
        </Descriptions>
        {type === 'Student' ? (
          <div>
            <Descriptions size={'middle'} style={{ marginTop: '30px' }} title={'校内导师评价'}>
              <DescriptionsItem span={3}>
                {info && info.evaluation ? level[info.evaluation - 1] : '暂无'}
              </DescriptionsItem>
            </Descriptions>
            <Descriptions size={'middle'} style={{ marginTop: '30px' }} title={'校内导师建议'}>
              <DescriptionsItem span={3}>{info?.teacherSuggestion || '暂无'}</DescriptionsItem>
            </Descriptions>
          </div>
        ) : null}
      </Spin>
    </div>
  );
};

export default ViewSummary;
