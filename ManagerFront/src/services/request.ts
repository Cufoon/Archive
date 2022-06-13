import { request as umiRequest } from 'umi';
import { message } from 'antd';

type Method = 'GET' | 'DELETE' | 'HEAD' | 'OPTIONS' | 'POST' | 'PUT';

interface ReturnBody<ReturnData> {
  /* 操作是否成功 */
  status: 'success' | 'error';
  /* 若错误，错误信息是啥 */
  message: string;
  /* 错误码 */
  code: number;
  /* 返回的数据 */
  data: ReturnData;
}

interface ErrorInfo {
  /* 处理是否成功 */
  succeed: boolean;
  /* 错误码 */
  code?: number;
  /* 错误信息 */
  message?: string;
}

type RequestReturn<T> = Promise<[T | null, ErrorInfo]>;

const codeMap: { [key: string]: string } = {
  '400': '请求错误',
  '500': '服务器内部错误'
};

/**
 * @function request
 * @param url 请求路径
 * @param method 请求方法
 * @param data 发送数据
 */
export async function request<SendDataType, ReturnDataType>(
  url: string,
  method: Method,
  data?: SendDataType
): RequestReturn<ReturnDataType> {
  let responseData: ReturnDataType | null = null;
  let responseError: ErrorInfo = { succeed: false };
  try {
    const result = await umiRequest<ReturnBody<ReturnDataType>>(url, {
      method: method,
      headers: {},
      data: data || {},
      credentials: 'include',
      getResponse: true,
      skipErrorHandler: true
    });

    if (process.env.NODE_ENV === 'development') {
      console.group('Request to ', url);
      console.log('send ', data);
      console.log('get  ', result);
      console.groupEnd();
    }

    const serverOK = result.response.status >= 200 && result.response.status < 300;
    const responseOK = result.data.status === 'success';
    responseData = result.data.data;
    responseError = { succeed: serverOK && responseOK, code: result.data.code, message: result.data.message };
  } catch (error) {
    if (error.response) {
      if (error.response.status !== 401) {
        message.error(codeMap[error.response.status.toString()]);
      }
    } else {
      message.error(error.message);
    }
    console.log(error.response);
  }

  return [responseData, responseError];
}

export async function requestGet<T, U>(url: string): RequestReturn<U> {
  return await request<T, U>(url, 'GET');
}

export async function requestPost<T, U>(url: string, data: T): RequestReturn<U> {
  return await request<T, U>(url, 'POST', data);
}
