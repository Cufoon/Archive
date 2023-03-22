export interface OneApplication {
  name: string;
  id: string;
  class: string;
  applyTime: string;
}

export interface OneApplicationData extends OneApplication {
  key: string;
}

export interface QueryData {
  studentApplyList: OneApplication[];
}

export interface ApplyAcceptSend {
  handleApplyList: string[];
}

export interface ApplyAcceptGet {
  handle: boolean;
}
