import localforage from 'localforage';

enum KEY {
  TOKEN = 'token'
}

interface Storage {
  [KEY.TOKEN]: string;
}

type AsyncGet<T extends KEY> = Promise<Storage[T] | null>;

async function getItem(key: `${KEY.TOKEN}`): AsyncGet<KEY.TOKEN>;
async function getItem(key: `${KEY}`): AsyncGet<KEY> {
  try {
    return await localforage.getItem<Storage[typeof key]>(key);
  } catch (error) {
    console.log(error);
  }
  return null;
}

async function setItem(key: `${KEY.TOKEN}`, value: Storage[KEY.TOKEN]): Promise<boolean>;
async function setItem(key: `${KEY}`, value: Storage[KEY]): Promise<boolean> {
  try {
    await localforage.setItem(key, value);
    return true;
  } catch (error) {
    console.log(error);
  }
  return false;
}

async function removeItem(key: KEY) {
  try {
    await localforage.removeItem(key);
    return true;
  } catch (error) {
    console.log(error);
  }
  return false;
}

const getToken = () => getItem(`${KEY.TOKEN}`);
const setToken = (value: Storage[KEY.TOKEN]) => setItem(`${KEY.TOKEN}`, value);
const removeToken = () => removeItem(KEY.TOKEN);

const clear = async () => {
  try {
    await localforage.clear();
    return true;
  } catch (error) {
    console.log(error);
  }
  return false;
};

export default {
  getToken,
  setToken,
  removeToken,
  clear
};
