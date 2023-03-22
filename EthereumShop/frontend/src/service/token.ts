import storage from '$src/utils/storage';

let token = '';

export const getToken = async (): Promise<string> => {
  if (token) return token;
  const storedToken = await storage.getToken();
  if (storedToken) {
    token = storedToken;
  }
  return token;
};

export const setToken = async (v: string): Promise<boolean> => {
  token = v;
  const succeed = await storage.setToken(v);
  if (succeed) {
    return true;
  }
  console.log('token本地持久化失败');
  return false;
};

export const clearToken = async (): Promise<boolean> => {
  return await storage.removeToken();
};
