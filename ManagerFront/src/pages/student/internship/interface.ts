export interface FormData {
  epCity: [number, number];
  epName: string;
  epAddress: string;
  eppName: string;
  eppSex: string;
  eppEmail: string;
  eppPhone: string;
  eptName: string;
  eptSex: string;
  eptEmail: string;
  eptPhone: string;
}

export interface InternshipGetData {
  state: string;
  comCity: string;
  comName: string;
  comAddress: string;
  comContact: string;
  sex: 'M' | 'F';
  comContactEmail: string;
  comContactPhone: string;
  comInstructorName: string;
  comInstructorSex: string;
  comInstructorEmail: string;
  comInstructorPhone: string;
  offerImg: string;
}

export interface InternPostData {
  comCity: string;
  comCityCode: number;
  comName: string;
  comAddress: string;
  comContactName: string;
  comContactSex: string;
  comContactEmail: string;
  comContactPhone: string;
  comInstructorName: string;
  comInstructorSex: string;
  comInstructorEmail: string;
  comInstructorPhone: string;
  offerImg: string | undefined;
}

export interface InternPostReturn {
  edited: boolean;
}
