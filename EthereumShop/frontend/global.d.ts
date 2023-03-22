import type { MetaMaskInpageProvider } from '@metamask/providers';

declare global {
  interface Window {
    ethereum: MetaMaskInpageProvider;
  }

  declare module '*.png';
  declare module '*.gif';
  declare module '*.jpg';
  declare module '*.jpeg';
  declare module '*.svg';
  declare module '*.css';
  declare module '*.less';
  declare module '*.scss';
  declare module '*.sass';
  declare module '*.styl';
}
