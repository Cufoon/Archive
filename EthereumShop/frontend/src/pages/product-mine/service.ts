import { useEffect, useMemo, useState } from 'react';
import { useSelector } from '$store/hook';
import { MyContract } from '$service/eth';
import useDelayChange from '$hooks/useDelayChange';
import { createMessageLoading, GlobalMessage } from '$utils/message';
import { delay } from '$utils/util';

interface StoreInstance {
  getProductFile: any;
  getProductInfo: any;
  buy: (arg0: { from: any; value: any; gas: number }) => Promise<any>;
  sell: (
    arg0: string | number | string[] | undefined,
    arg1: { from: any; gas: number }
  ) => Promise<any>;
  getBalanceInfo: { call: () => Promise<any> };
  publish: (
    arg0: any,
    arg1: any,
    arg2: any,
    arg3: any,
    arg4: any,
    arg5: any,
    arg6: any,
    arg7: { from: string | undefined }
  ) => Promise<any>;
  getPublishedProducts(): any;
  getPurchasedProducts(): any;
}

interface ProductInfo {
  commentNum: number;
  evaluated: boolean;
  id: string;
  owner: string;
  name: string;
  type: string;
  introduction: string;
  usedRules: string;
  price: string;
  sales: string;
  score: string;
  publishedTime: string;
  cover: string;
  file: string;
}

export const useAPI = () => {
  const [loading, setLoadingDelay, setLoading] = useDelayChange();
  const [purchasedProductList, setPurchasedProductList] = useState<ProductInfo[]>([]);
  const [publishedProductList, setPublishedProductList] = useState<ProductInfo[]>([]);
  const [searchInput, setSearchInput] = useState<string>('');
  const [search, setSearch] = useState<string>('');
  const [selectType, setSelectType] = useState<string>('所有');
  const accountId = useSelector((store) => store.account_id);

  const s = {
    getPublishedProduct: async () => {
      const store = await MyContract.deployed().then((v: StoreInstance) => v);
      const r1 = await store.getPublishedProducts({ from: accountId });
      const r2 = await Promise.all(
        r1.map(async (item: { toString: () => any }) => {
          return await s.getProductInfo(item.toString());
        })
      );
      console.log('getPublishedProduct result', r2);
      return r2;
    },
    getPurchasedProduct: async () => {
      const store = await MyContract.deployed().then((v: any) => v);
      const r1 = await store.getPurchasedProducts({ from: accountId });
      const r2 = await Promise.all(
        r1.map(async (item: { toString: () => any }) => {
          return await s.getProductInfo(item.toString());
        })
      );
      console.log('getPurchasedProduct result', r2);
      return r2;
    },
    getProductInfo: async (id: any) => {
      const store = await MyContract.deployed().then((v: any) => v);
      const result = await store.getProductInfo.call(id.toString(), { from: accountId });
      const isEvaluated = await s.isEvaluated(id);
      const commentNum = await s.getCommentsLength(id);
      const date = new Date();
      date.setTime(parseInt(result[8].toString()) * 1000);
      return {
        commentNum,
        evaluated: isEvaluated,
        id: id,
        owner: result[0],
        name: result[1],
        type: result[2],
        introduction: result[3],
        usedRules: result[4],
        price: result[5].toString(),
        sales: result[6].toString(),
        score: (parseInt(result[7].toString()) / (commentNum || 1)).toFixed(2),
        publishedTime: date.toString(),
        cover: result[9],
        file: result[10]
      };
    },
    getProductFile: async (id: any) => {
      const store = await MyContract.deployed().then((v: any) => v);
      const result = await store.getProductFile.call(id, { from: accountId });
      console.log('getProductFile result', result);
      return result;
    },
    isEvaluated: async (id: any) => {
      const store = await MyContract.deployed().then((v: any) => v);
      const result = await store.isEvaluated.call(id, { from: accountId });
      console.log('isEvaluated result', result);
      return result;
    },
    evaluate: async (id: any, evaluateScore: number, comment: string) => {
      const msl = createMessageLoading();
      msl.showLoading('提交评价中...');
      const store = await MyContract.deployed().then((v: any) => v);
      if (comment === '') {
        comment = '该用户很懒，没有留下任何有用的东西。';
      }
      const result = await store.evaluate(id, evaluateScore, comment, {
        from: accountId
      });
      const newInfo = await s.getProductInfo(id);
      setPurchasedProductList((v) => {
        const idx = v.findIndex((item) => item.id === id);
        if (idx > -1) {
          v[idx] = newInfo;
          return [...v];
        }
        return v;
      });
      msl.success('评价成功!');
      console.log('evaluate result', result);
    },
    getCommentsLength: async (id: any) => {
      const store = await MyContract.deployed().then((v: any) => v);
      const result = await store.getCommentLength(id, { from: accountId });
      return parseInt(result);
    },
    getCommentsInfo: async (id: any, cid: any) => {
      const store = await MyContract.deployed().then((v: any) => v);
      const result = await store.getCommentInfo(id, cid, { from: accountId });
      return {
        buyer: result[0],
        date: result[1].toString(),
        score: result[2].toString(),
        content: result[3]
      };
    }
  };

  const resetSearch = () => {
    setSearch('');
    setSearchInput('');
    setSelectType('所有');
  };

  const pcpList = useMemo(() => {
    const result: ProductInfo[] = [];
    purchasedProductList.forEach((item) => {
      if (search === '') {
        if (selectType === '所有' || selectType === '') {
          result.push(item);
        } else {
          if (item.type === selectType) {
            result.push(item);
          }
        }
      } else {
        if (item.name.indexOf(search) > -1) {
          if (selectType === '所有' || selectType === '') {
            result.push(item);
          } else {
            if (item.type === selectType) {
              result.push(item);
            }
          }
        }
      }
    });
    return result;
  }, [purchasedProductList, search, selectType]);

  const pbpList = useMemo(() => {
    const result: ProductInfo[] = [];
    publishedProductList.forEach((item) => {
      if (search === '') {
        if (selectType === '所有') {
          result.push(item);
        } else {
          if (item.type === selectType) {
            result.push(item);
          }
        }
      } else {
        if (item.name.indexOf(search) > -1) {
          if (selectType === '所有') {
            result.push(item);
          } else {
            if (item.type === selectType) {
              result.push(item);
            }
          }
        }
      }
    });
    return result;
  }, [publishedProductList, search, selectType]);

  const seeProductFile = async (productId: string) => {
    const store = await MyContract.deployed().then((v: any) => v);
    const result = await store.getProductFile(productId, { from: accountId });
    window.open(result);
  };

  const getCommentListPcp = async (id: any) => {
    const commentNum = (await s.getCommentsLength(id)) || 0;
    const commentPromiseArr = [];
    if (commentNum > 0) {
      for (let i = 0; i < commentNum; i++) {
        commentPromiseArr.push(s.getCommentsInfo(id, i));
      }
      return await Promise.all(commentPromiseArr);
    }
    return [];
  };

  const getCommentListPbp = async (id: any) => {
    const commentNum = (await s.getCommentsLength(id)) || 0;
    const commentPromiseArr = [];
    if (commentNum > 0) {
      for (let i = 0; i < commentNum; i++) {
        commentPromiseArr.push(s.getCommentsInfo(id, i));
      }
      return await Promise.all(commentPromiseArr);
    }
    return [];
  };

  const evaluate = async (id: any, evaluateScore: number, comment: string) => {
    try {
      await s.evaluate(id, evaluateScore, comment);
      return true;
    } catch (e) {
      console.log('evaluate err', e);
      return false;
    }
  };

  useEffect(() => {
    (async () => {
      const msl = createMessageLoading('product-mine-loading');
      msl.showLoading('加载已发布商品');
      setLoadingDelay(true);
      const arr2 = await s.getPublishedProduct();
      setPublishedProductList(arr2);
      if (arr2.length === 0) {
        GlobalMessage.info('没有发布任何商品', { duration: 1500 });
        setLoading(false);
        await delay(30);
      }
      msl.updateLoading('加载已购买商品');
      const arr1 = await s.getPurchasedProduct();
      setPurchasedProductList(arr1);
      msl.success('加载完毕！');
      setLoading(false);
    })();
  }, []);

  return {
    accountId,
    evaluate,
    getCommentListPbp,
    getCommentListPcp,
    loading,
    pbpList,
    pcpList,
    resetSearch,
    searchInput,
    seeProductFile,
    selectType,
    setSearch,
    setSearchInput,
    setSelectType
  };
};
