export interface InfoResponse {
  state: number;
  info: Info;
}

export interface Info {
  studentName: string;
  sex: 'M' | 'F';
  className: string;
  phoneNumber: string;
  email: string;
  introduction: string;
  avatar?: string;
  readonly studentId?: string;
}

export interface EditParam {
  studentName: string;
  sex: 'M' | 'F';
  className: string;
  phoneNumber: string;
  email: string;
  introduction: string;
  avatar?: string;
}

export interface EditResponse {
  modified: boolean;
}
