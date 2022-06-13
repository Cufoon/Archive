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
  tName: string;
  epName: string;
  epCity: string;
  instructorName: string;
  instructorPhone: string;
  instructorEmail: string;
  startDate: string;
  endDate: string;
  projectName: string;
  attendance: string;
  eptSuggestions: string;
  evaluation: number;
  teacherSuggestion: string;
}

interface QueryParam {
  category: number;
  order: number;
}

const ViewExam: React.FC<Props> = ({ sid, order, type }) => {
  const [info, setInfo] = useState<QueryResponse>();
  const [isLoading, setIsLoading] = useState<boolean>(true);

  const queryReportStudent = async () => {
    const [res, ok] = await requestPost<QueryParam, QueryResponse>(urlStudentLookDocument, {
      category: 2,
      order: order
    });
    if (ok.succeed && res) {
      setIsLoading(false);
      setInfo(res);
    }
  };

  const queryReportTeacher = async () => {
    const [res, ok] = await requestGet<null, QueryResponse>(
      `${urlTeacherHandleDocument}?sid=${sid}&category=2&order=${order}`
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
        <Descriptions bordered size={'middle'} style={{ marginTop: '30px' }} title={'基本信息'}>
          <DescriptionsItem label={'专业班级'}>{info?.className}</DescriptionsItem>
          <DescriptionsItem label={'学生姓名'}>{info?.name}</DescriptionsItem>
          <DescriptionsItem label={'学号'} span={2}>
            {info?.sid}
          </DescriptionsItem>
          <DescriptionsItem label={'院内老师'} span={2}>
            {info?.tName}
          </DescriptionsItem>
        </Descriptions>
        <Descriptions bordered size={'middle'} style={{ marginTop: '30px' }} title={'实习信息'}>
          <DescriptionsItem label={'实习企业'} span={2}>
            {info?.epName}
          </DescriptionsItem>
          <DescriptionsItem label={'实习城市'} span={2}>
            {info?.epCity}
          </DescriptionsItem>
          <DescriptionsItem label={'企业导师'} span={2}>
            {info?.instructorName}
          </DescriptionsItem>
          <DescriptionsItem label={'企业导师联系方式'} span={2}>
            {info?.instructorPhone}
          </DescriptionsItem>
          <DescriptionsItem label={'企业导师电子邮箱'} span={2}>
            {info?.instructorEmail}
          </DescriptionsItem>
          <DescriptionsItem label={'阶段起止时间'} span={2}>
            {info ? `${info?.startDate} - ${info?.endDate}` : ''}
          </DescriptionsItem>
          <DescriptionsItem label={'参与项目名称'} span={2}>
            {info?.projectName}
          </DescriptionsItem>
        </Descriptions>
        <Descriptions bordered size={'middle'} style={{ marginTop: '30px' }} title={'阶段检查'}>
          <DescriptionsItem label={'本阶段出勤情况'}>{info?.attendance}</DescriptionsItem>
          <DescriptionsItem label={'企业导师建议'}>{info?.eptSuggestions}</DescriptionsItem>
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

export default ViewExam;
