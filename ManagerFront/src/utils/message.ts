import { message } from 'antd';
import { MessageType } from 'antd/lib/message';

type controlFuncType = (content: string, key: string) => MessageType;

const messageShowLoading: controlFuncType = (content, key) => {
  return message.loading({
    content: content,
    key: key,
    duration: 0
  });
};

const messageSucceedLoaded: controlFuncType = (content, key) => {
  return message.success({
    content: content,
    key: key,
    duration: 1.25
  });
};

const messageFailedLoaded: controlFuncType = (content, key) => {
  return message.error({
    content: content,
    key: key,
    duration: 1.25
  });
};

const messageCompleteLoaded: controlFuncType = (content, key) => {
  return message.info({
    content: content,
    key: key,
    duration: 1.25
  });
};

const voidFunc = () => {};

export const createMessageLoading = (key: string) => {
  let loading = false;
  let delay = true;

  const startLoading = (callback: controlFuncType) => (content: string) => {
    delay = true;
    setTimeout(() => {
      if (delay) {
        callback(content, key);
        loading = true;
      }
    }, 410);

    return controller;
  };

  const stopLoading = (callback: controlFuncType) => (content: string) => {
    delay = false;
    callback(content, key);
    loading = false;
    return controller;
  };

  const hideLoading = () => {
    delay = false;
    if (loading) {
      message.destroy(key);
    }
  };

  const controller = {
    showLoading: startLoading(messageShowLoading),
    hideLoading: hideLoading,
    success: stopLoading(messageSucceedLoaded),
    error: stopLoading(messageFailedLoaded),
    info: stopLoading(messageCompleteLoaded)
  };

  return controller;
};
