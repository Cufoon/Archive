import { Message, MessageProps } from '@arco-design/web-react';
import type { MessageType as CloserType } from '@arco-design/web-react/es/Message';
import { getRandomMsgId } from '$utils/util';

class MessageLoading {
  closeHandler: CloserType | null = null;
  preContent = '加载中';
  canShowLoading = true;
  canShowDelayLoading = true;
  delay = 200;
  id: string;
  lastUpdateHolder: NodeJS.Timeout | null = null;

  constructor(id: string, delay: number) {
    this.id = id;
    this.delay = delay;
  }

  showLoading(content?: string) {
    this.preContent = content || '加载中';
    if (this.canShowLoading) {
      this.canShowDelayLoading = true;
      setTimeout(() => {
        if (this.canShowDelayLoading) {
          this.closeHandler = GlobalMessage.loading(content || '加载中', {
            id: this.id,
            duration: 60000
          });
        }
      }, this.delay);
    }
  }

  updateLoading(content: string) {
    this.preContent = content;
    if (this.canShowLoading) {
      this.canShowDelayLoading = false;
      this.closeHandler = GlobalMessage.loading(content, {
        id: this.id,
        duration: 60000
      });
    }
  }

  updateSuccess(content: string, long = 1000) {
    if (long < 200) {
      long = 200;
    }
    if (this.canShowLoading) {
      this.canShowDelayLoading = false;
      this.lastUpdateHolder && clearTimeout(this.lastUpdateHolder);
      this.lastUpdateHolder = setTimeout(() => {
        this.lastUpdateHolder = null;
        this.updateLoading(this.preContent);
      }, long);
      this.closeHandler = GlobalMessage.success(content, {
        id: this.id,
        duration: long + 500
      });
    }
  }

  updateError(content: string, long = 1000) {
    if (long < 200) {
      long = 200;
    }
    if (this.canShowLoading) {
      this.canShowDelayLoading = false;
      this.lastUpdateHolder && clearTimeout(this.lastUpdateHolder);
      this.lastUpdateHolder = setTimeout(() => {
        this.lastUpdateHolder = null;
        this.updateLoading(this.preContent);
      }, long);
      this.closeHandler = GlobalMessage.error(content, {
        id: this.id,
        duration: long + 500
      });
    }
  }

  updateInfo(content: string, long = 1000) {
    if (long < 200) {
      long = 200;
    }
    if (this.canShowLoading) {
      this.canShowDelayLoading = false;
      this.lastUpdateHolder && clearTimeout(this.lastUpdateHolder);
      this.lastUpdateHolder = setTimeout(() => {
        this.lastUpdateHolder = null;
        this.updateLoading(this.preContent);
      }, long);
      this.closeHandler = GlobalMessage.info(content, {
        id: this.id,
        duration: long + 500
      });
    }
  }

  hideLoading() {
    this.lastUpdateHolder && clearTimeout(this.lastUpdateHolder);
    this.lastUpdateHolder = null;
    this.canShowDelayLoading = false;
    this.canShowLoading = false;
    this.closeHandler?.();
    this.closeHandler = null;
  }

  success(content: string) {
    this.lastUpdateHolder && clearTimeout(this.lastUpdateHolder);
    this.lastUpdateHolder = null;
    if (this.canShowLoading) {
      this.canShowLoading = false;
      this.canShowDelayLoading = false;
      GlobalMessage.success(content, {
        id: this.id,
        duration: 2000
      });
    }
  }

  error(content: string) {
    this.lastUpdateHolder && clearTimeout(this.lastUpdateHolder);
    this.lastUpdateHolder = null;
    if (this.canShowLoading) {
      this.canShowLoading = false;
      this.canShowDelayLoading = false;
      GlobalMessage.error(content, {
        id: this.id,
        duration: 2000
      });
    }
  }

  info(content: string) {
    this.lastUpdateHolder && clearTimeout(this.lastUpdateHolder);
    this.lastUpdateHolder = null;
    if (this.canShowLoading) {
      this.canShowLoading = false;
      this.canShowDelayLoading = false;
      GlobalMessage.error(content, {
        id: this.id,
        duration: 2000
      });
    }
  }
}

export const createMessageLoading = (id?: string, delay?: number) => {
  if (!id) {
    id = getRandomMsgId();
  }
  if (delay === undefined) {
    delay = 200;
  } else {
    if (delay < 20) {
      delay = 20;
    }
  }
  return new MessageLoading(id, delay);
};

type GlobalMessageProps = Omit<MessageProps, 'content'>;
const convertMessageFunc = (fn: (config: string | MessageProps) => CloserType) => {
  return (content: string | JSX.Element, props?: GlobalMessageProps, id?: string) => {
    if (!id) {
      id = getRandomMsgId();
    }
    return fn({
      content,
      className: 'custom-antd-message-style',
      id: id,
      ...props
    });
  };
};

export const GlobalMessage = {
  success: convertMessageFunc(Message.success),
  error: convertMessageFunc(Message.error),
  info: convertMessageFunc(Message.info),
  warning: convertMessageFunc(Message.warning),
  loading: convertMessageFunc(Message.loading)
};
