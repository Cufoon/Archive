import Web3 from 'web3';
import detectEthereumProvider from '@metamask/detect-provider';
import storeJSON from '$src/../../truffle/build/contracts/Store.json';

// @ts-ignore
import contract from '@truffle/contract';

(async () => {
  const mmProvider = await detectEthereumProvider();
  if (mmProvider) {
    console.log('MetaMask installed!');
  } else {
    console.log('Please install MetaMask!');
  }
})();

const provider = new Web3.providers.HttpProvider('http://localhost:7545');
export const web3 = new Web3(provider);

export const MyContract = contract(storeJSON);
MyContract.setProvider(provider);

let globalAccount = '';
const id = 0;
export const getGlobalAccount = async () => {
  if (globalAccount === '') {
    globalAccount = (await web3.eth.getAccounts())[id] || '';
  }
  return globalAccount;
};

// era purse case grocery material sentence depend bachelor autumn company van umbrella
