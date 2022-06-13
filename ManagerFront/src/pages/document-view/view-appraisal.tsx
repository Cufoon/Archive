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
  name: string;
  sid: string;
  instructorName: string;
  instructorPhone: string;
  epName: string;
  epCity: string;
  startDate: string;
  endDate: string;
  projectName: string;
  eptEvaluation1: number;
  eptEvaluation2: number;
  eptEvaluation3: number;
  eptEvaluation4: number;
  eptEvaluation5: number;
  eptEvaluation6: number;
  eptEvaluation7: number;
  eptEvaluation8: number;
  eptEvaluation9: number;
  eptSuggestions: string;
  evaluation: number;
  teacherSuggestion: string;
}

interface QueryParam {
  category: number;
  order: number;
}

const ViewAppraisal: React.FC<Props> = ({ sid, order, type }) => {
  const [info, setInfo] = useState<QueryResponse>();
  const [isLoading, setIsLoading] = useState<boolean>(true);

  const queryReportStudent = async () => {
    const [res, ok] = await requestPost<QueryParam, QueryResponse>(urlStudentLookDocument, {
      category: 4,
      order: order
    });
    if (ok.succeed && res) {
      setIsLoading(false);
      setInfo(res);
    }
  };
  const queryReportTeacher = async () => {
    const [res, ok] = await requestGet<null, QueryResponse>(
      `${urlTeacherHandleDocument}?sid=${sid}&category=4&order=${order}`
    );
    if (ok.succeed && res) {
      setIsLoading(false);
      setInfo(res);
    }
  };

  const appraisalList = ['非常满意', '满意', '普通', '不满意', '非常不满意'];

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
          <DescriptionsItem label={'学生姓名'}>{info?.name}</DescriptionsItem>
          <DescriptionsItem label={'学号'}>{info?.sid}</DescriptionsItem>
        </Descriptions>
        <Descriptions style={{ marginTop: '30px' }} title={'实习信息'} bordered size={'middle'}>
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
          <DescriptionsItem label={'阶段起止时间'} span={2}>
            {info ? `${info?.startDate} - ${info?.endDate}` : ''}
          </DescriptionsItem>
          <DescriptionsItem label={'参与项目名称'} span={2}>
            {info?.projectName}
          </DescriptionsItem>
        </Descriptions>
        <Descriptions style={{ marginTop: '30px' }} title={'企业导师对该生的工作评价'} bordered size={'middle'}>
          <DescriptionsItem label={'遵守公司管理制度'}>
            {info ? appraisalList[info.eptEvaluation1 - 1] : ''}
          </DescriptionsItem>
          <DescriptionsItem label={'工作态度与责任感'}>
            {info ? appraisalList[info.eptEvaluation2 - 1] : ''}
          </DescriptionsItem>
          <DescriptionsItem label={'沟通与团队合作能力'}>
            {info ? appraisalList[info.eptEvaluation3 - 1] : ''}
          </DescriptionsItem>
          <DescriptionsItem label={'专业知识与工作能力'}>
            {info ? appraisalList[info.eptEvaluation4 - 1] : ''}
          </DescriptionsItem>
          <DescriptionsItem label={'自信与自主学习能力'}>
            {info ? appraisalList[info.eptEvaluation5 - 1] : ''}
          </DescriptionsItem>
          <DescriptionsItem label={'情绪表现与抗压能力'}>
            {info ? appraisalList[info.eptEvaluation6 - 1] : ''}
          </DescriptionsItem>
          <DescriptionsItem label={'解决问题能力'}>
            {info ? appraisalList[info.eptEvaluation7 - 1] : ''}
          </DescriptionsItem>
          <DescriptionsItem label={'创新思维能力'}>
            {info ? appraisalList[info.eptEvaluation8 - 1] : ''}
          </DescriptionsItem>
          <DescriptionsItem label={'整体工作的表现'}>
            {info ? appraisalList[info.eptEvaluation9 - 1] : ''}
          </DescriptionsItem>
          <DescriptionsItem label={'企业导师建议'} span={3}>
            {info?.eptSuggestions}
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

export default ViewAppraisal;
