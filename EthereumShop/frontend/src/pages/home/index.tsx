import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import Web3 from 'web3';
import { useSelector } from '$store/hook';
import { MyContract } from '$service/eth';
import { createMessageLoading, GlobalMessage } from '$utils/message';

import styles from './index.scss';

interface StoreInstance {
  buy: (arg0: { from: any; value: any; gas: number }) => Promise<any>;
  sell: (
    arg0: string | number | string[] | undefined,
    arg1: { from: any; gas: number }
  ) => Promise<any>;
  getBalanceInfo: { call: (arg0: any) => Promise<any> };
}

const Index: React.FC = () => {
  const location = useLocation();
  const [txb, setTxb] = useState<number>(0);
  const [eth, setEth] = useState<number>(0);
  const [myTxb, setMyTxb] = useState<number>(0);
  const [myEth, setMyEth] = useState<number>(0);
  const [rateTXB2ETH, setRateTXB2ETH] = useState<number>(0.1);
  const accountId = useSelector((store) => store.account_id);

  console.log('accountId', accountId);

  const _getBalanceInfo = () => {
    return new Promise(function (resolve) {
      MyContract.deployed().then(function (storeInstance: StoreInstance) {
        storeInstance.getBalanceInfo
          .call({
            from: accountId
          })
          .then(function (result: unknown) {
            resolve(result);
          })
          .catch(function (err: string) {
            alert('请解锁用户: ' + err);
          });
      });
    });
  };

  const getBalanceInfo = async (): Promise<void> => {
    const result: any = await _getBalanceInfo();
    console.log(result);
    setTxb(result[2].toNumber());
    setEth(Number(Number(Web3.utils.fromWei(result[3])).toFixed(3)));
    setMyTxb(result[4].toNumber());
    console.log('origin result 5', result[5].toString());
    setMyEth(Number(Number(Web3.utils.fromWei(result[5])).toFixed(3)));
    setRateTXB2ETH(Number(Web3.utils.fromWei(result[1])));
  };

  useEffect(() => {
    (async () => {
      await getBalanceInfo();
    })();
  }, []);

  const [fromEthInput, setFromEthInput] = useState<string>('');
  const [fromEth, setFromEth] = useState<number>(0);
  const check1 = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setFromEthInput(value);
    if (value !== '') {
      const num = Number(value);
      console.log('num', num);
      console.log('txb', txb);
      if (num > 0 && num <= txb) {
        setFromEth(parseFloat((num * rateTXB2ETH).toFixed(3)));
      } else {
        setFromEth(0);
      }
    }
  };

  const blur1 = (e: { target: { value: any } }) => {
    const value = e.target.value;
    if (value === '') {
      return false;
    }
    const num = Number(value);
    if (num <= 0 || num > txb) {
      GlobalMessage.error('非法输入', { duration: 3000 });
      setFromEthInput('');
      return false;
    }
    return true;
  };

  const [toEthInput, setToEthInput] = useState<string>('');
  const [toEth, setToEth] = useState<number>(0);
  const check2 = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setToEthInput(value);
    if (value !== '') {
      const num = Number(value);
      console.log('num', num);
      console.log('eth', eth);
      if (num > 0 && num <= myTxb) {
        setToEth(parseFloat((num * rateTXB2ETH).toFixed(3)));
      } else {
        setToEth(0);
      }
    }
  };

  const blur2 = (e: { target: { value: any } }) => {
    const value = e.target.value;
    if (value === '') {
      return false;
    }
    const num = Number(value);
    if (num < 0 || num > myTxb) {
      GlobalMessage.error('非法输入', { duration: 3000 });
      setToEthInput('');
      return false;
    }
    return true;
  };

  const buy = async function () {
    if (!blur1({ target: { value: fromEthInput } })) {
      return;
    }
    const msl = createMessageLoading('buy-txb');
    msl.showLoading('正在买入......');
    const num = Number(fromEthInput);
    const enable = await window.ethereum.request({ method: 'eth_requestAccounts' });
    console.log(enable, 11);
    MyContract.deployed().then(async function (storeInstance: StoreInstance) {
      storeInstance
        .buy({
          from: accountId,
          value: Web3.utils.toWei((num * rateTXB2ETH).toString(), 'ether'),
          gas: 140000
        })
        .then(function () {
          msl.success('兑换成功,等待写入区块!');
          window.location.reload();
        })
        .catch(function (err: string) {
          msl.error(`兑换失败: ${err}`);
          window.location.reload();
        });
    });
  };

  const sell = async function () {
    if (!blur2({ target: { value: toEthInput } })) {
      return;
    }
    const msl = createMessageLoading('sell-txb');
    msl.showLoading('正在卖出......');
    const num = Number(toEthInput);
    MyContract.deployed().then(async function (storeInstance: StoreInstance) {
      storeInstance
        .sell(num, {
          from: accountId,
          gas: 140000
        })
        .then(function () {
          msl.success('兑换成功,等待写入区块!');
          window.location.reload();
        })
        .catch(function (err: string) {
          msl.error(`兑换失败: ${err}`);
          window.location.reload();
        });
    });
  };

  return (
    <div className={styles.container} key={location.key}>
      <div className={styles.header}>
        <div>同心链虚拟商城，安全便捷的虚拟商品交易平台</div>
      </div>
      <div className={styles.db}>
        <h5>商城代币</h5>
        <table>
          <tbody>
            <tr>
              <td>合约同心币 (TXB)</td>
              <td>{txb}</td>
            </tr>
            <tr>
              <td>合约以太币 (ETH)</td>
              <td>{eth}</td>
            </tr>
          </tbody>
        </table>
        <h5>我的钱包</h5>
        <table>
          <tbody>
            <tr>
              <td>我的同心币 (TXB)</td>
              <td>{myTxb}</td>
            </tr>
            <tr>
              <td>我的以太币 (ETH)</td>
              <td>{myEth}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div>
        <p>兑换比例: </p>
        <p>
          <span>{'1 TXB = '}</span>
          <span style={{ color: '#f00' }}>{rateTXB2ETH}</span>
          <span>{' ETH'}</span>
        </p>
        <p>
          <span>{'1 ETH = '}</span>
          <span style={{ color: '#f00' }}>{1 / rateTXB2ETH}</span>
          <span>{' TXB'}</span>
        </p>
      </div>
      <div className={styles.exchangePart}>
        <div className={styles.row}>
          <label>买入同心币: </label>
          <input type='text' value={fromEthInput} onChange={check1} />
          <span>{'TXB = '}</span>
          <span style={{ color: '#f00' }}>{fromEth}</span>
          <span>{' ETH'}</span>
          <button
            onClick={() => {
              buy().then();
            }}
          >
            兑 换
          </button>
        </div>
        <div className={styles.row}>
          <label>卖出同心币: </label>
          <input type='text' value={toEthInput} onChange={check2} />
          <span>{'TXB = '}</span>
          <span style={{ color: '#f00' }}>{toEth}</span>
          <span>{' ETH'}</span>
          <button
            onClick={() => {
              sell().then();
            }}
          >
            兑 换
          </button>
        </div>
      </div>
    </div>
  );
};

export default Index;
