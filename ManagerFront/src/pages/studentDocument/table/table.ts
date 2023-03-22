import { Table } from '@/pages/studentDocument/studentDocument';

export const tableReport = '/student/document/tableReport';
export const tableExam = '/student/document/tableExam';
export const tableIdentify = '/student/document/tableIdentify';
export const tableAppraisal = '/student/document/tableAppraisal';
export const tableSummary = '/student/document/tableSummary';

//自动保存时间
export const initTime = 60;

//教师评价
export const level = ['不合格', '合格', '中等', '良好', '优秀'];

const tableList: Table[] = [
  {
    key: '1',
    name: '校外实践情况阶段汇报表1',
    category: 1,
    order: 1,
    deadline: '',
    due: false,
    submitTime: '',
    checkTime: '',
    status: 0
  },
  {
    key: '2',
    name: '校外实践情况阶段汇报表2',
    category: 1,
    order: 2,
    deadline: '',
    due: false,
    submitTime: '',
    checkTime: '',
    status: 0
  },
  {
    key: '3',
    name: '校外实践情况阶段汇报表3',
    category: 1,
    order: 3,
    deadline: '',
    due: false,
    submitTime: '',
    checkTime: '',
    status: 0
  },
  {
    key: '4',
    name: '校外实践阶段检查表1',
    category: 2,
    order: 1,
    deadline: '',
    due: false,
    submitTime: '',
    checkTime: '',
    status: 0
  },
  {
    key: '5',
    name: '校外实践阶段检查表2',
    category: 2,
    order: 2,
    deadline: '',
    due: false,
    submitTime: '',
    checkTime: '',
    status: 0
  },
  {
    key: '6',
    name: '校外实践阶段检查表3',
    category: 2,
    order: 3,
    deadline: '',
    due: false,
    submitTime: '',
    checkTime: '',
    status: 0
  },
  {
    key: '7',
    name: '校外实践鉴定表',
    category: 3,
    order: 1,
    deadline: '',
    due: false,
    submitTime: '',
    checkTime: '',
    status: 0
  },
  {
    key: '8',
    name: '校外实践企业导师评价表',
    category: 4,
    order: 1,
    deadline: '',
    due: false,
    submitTime: '',
    checkTime: '',
    status: 0
  },
  {
    key: '9',
    name: '校外实践总结报告',
    category: 5,
    order: 1,
    deadline: '',
    due: false,
    submitTime: '',
    checkTime: '',
    status: 0
  }
];
export default tableList;
