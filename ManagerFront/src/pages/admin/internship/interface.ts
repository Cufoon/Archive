export interface PeriodData {
  name: string;
  startDate: string;
  endDate: string;
}

export interface GetPeriodData {
  currentCreated: boolean;
  periodList: PeriodData[];
}

export interface EditSendData {
  fromDate: string;
  endDate: string;
  periodName: string;
  isUpdate: boolean;
}

export interface EditGetData {
  edited: boolean;
}

export interface FormData {
  periodName: string;
  periodRange: [string, string];
}
