import ex from 'umi/dist';

export interface DefaultParam {
  category: number;
  order: number;
}

export interface FormReport {
  startDate: string;
  endDate: string;
  projectName: string;
  livingAddress: string;
  livingPhone: string;
  livingCondition: string;
  jobContent: string;
  requirements: string;
}

export interface FormExam {
  startDate: string;
  endDate: string;
  projectName: string;
  attendance: string;
  eptSuggestions: string;
}

export interface FormIdentify {
  startDate: string;
  endDate: string;
  projectName: string;
  personalSummary: string;
  attendance: string;
  eptSuggestions: string;
  epSuggestions: string;
}

export interface FormAppraisal {
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
}

export interface FormSummary {
  summaryReport: string;
}
