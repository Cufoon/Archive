import { MyContract } from '$service/eth';
import { useEffect, useMemo, useState } from 'react';
import { useSelector } from '$store/hook';
import { createMessageLoading } from '$utils/message';

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
  const [loading, setLoading] = useState(true);
  const [originProductList, setOriginProductList] = useState<ProductInfo[]>([]);
  const [searchInput, setSearchInput] = useState<string>('');
  const [search, setSearch] = useState<string>('');
  const [selectType, setSelectType] = useState<string>('所有');
  const [position, setPosition] = useState(0);
  const accountId = useSelector((store) => store.account_id);

  const s = {
    purchase: async (id: any) => {
      const store = await MyContract.deployed().then((v: StoreInstance) => v);
      const isPurchased = await store.isPurchased.call(id, { from: accountId });
      if (isPurchased) {
        console.log('已经购买过');
      } else {
        const purchaseResult = await store.purchase(id, {
          from: accountId,
          gas: 140000
        });
        if (purchaseResult) {
          console.log('购买成功', purchaseResult);
        }
      }
    },
    getProductNums: async () => {
      const store = await MyContract.deployed().then((v: StoreInstance) => v);
      const nums = await store.getProductLength.call();
      if (nums) {
        return nums;
      }
      return 0;
    },
    getProductInfo: async (id: any) => {
      const store = await MyContract.deployed().then((v: any) => v);
      const result = await store.getProductInfo.call(id.toString(), { from: accountId });
      const commentNum = await s.getCommentsLength(id);
      const date = new Date();
      date.setTime(parseInt(result[8].toString()) * 1000);
      return {
        commentNum: commentNum,
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
      const result = await store.getProductFile.call(id);
      console.log('getProductFile result', result);
      return result;
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

  const buy = async (id: any) => {
    const msl = createMessageLoading('shop-buy');
    msl.showLoading('购买中...');
    try {
      await s.purchase(id);
      msl.success('购买成功！');
    } catch (e) {
      msl.error('购买失败！');
    }
  };

  const getCommentList = async (id: any) => {
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

  const productList = useMemo(() => {
    const result: ProductInfo[] = [];
    originProductList.forEach((item) => {
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
    if (position === 0) {
      result.sort((a, b) => parseInt(b.sales) - parseInt(a.sales));
    } else if (position === 1) {
      result.sort((a, b) => parseInt(b.score) - parseInt(a.score));
    } else if (position === 2) {
      result.sort((a, b) => parseInt(b.price) - parseInt(a.price));
    } else {
      result.sort((a, b) => b.commentNum - a.commentNum);
    }
    return result;
  }, [originProductList, search, selectType, position]);

  const seeProductFile = async (productId: string) => {
    const store = await MyContract.deployed().then((v: any) => v);
    const result = await store.getProductFile(productId);
    window.open(result);
  };

  useEffect(() => {
    (async () => {
      const msl = createMessageLoading('shop-rank-loading');
      msl.showLoading('加载所有商品信息中...');
      const num = await s.getProductNums();
      const promiseArr = [];
      for (let i = 0; i < num; i++) {
        promiseArr.push(s.getProductInfo(i));
      }
      const result = await Promise.all(promiseArr);
      setOriginProductList(result);
      setLoading(false);
      msl.hideLoading();
    })();
  }, []);

  return {
    productList,
    seeProductFile,
    resetSearch,
    setSearch,
    setSelectType,
    searchInput,
    setSearchInput,
    selectType,
    buy,
    position,
    setPosition,
    loading,
    accountId,
    getCommentList
  };
};
