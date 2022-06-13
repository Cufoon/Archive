export interface OneMessage {
  msgId: number;
  type: string;
  msgTitle: string;
  msgContent: string;
  receiver: string;
  isRead: boolean;
  summary: string;
  sender: string;
  sentTime: string;
}

export interface OneMessageData extends OneMessage {
  key: string;
}

export interface QueryData {
  messageList: OneMessage[];
}

export interface ReadSend {
  messageList: number[];
}

export interface ReadGet {
  read: boolean;
}
