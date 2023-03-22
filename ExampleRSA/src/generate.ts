import { isPrime, inverseGcd } from './util';

export const generatePrime = (keyWidth: number): bigint => {
  keyWidth < 8 && (keyWidth = 8);
  const gap = 2n ** BigInt(keyWidth - 1);
  while (true) {
    const tmpNum = (BigInt(Math.floor(Math.random() * 10000)) * gap) / 10000n + gap;
    if (isPrime(tmpNum)) {
      return tmpNum;
    }
  }
};

interface RsaKey {
  public: [bigint, bigint];
  private: [bigint, bigint];
}

export const generateRsaKey = (keyWidth: number): RsaKey => {
  const p = generatePrime(keyWidth);
  let q = generatePrime(keyWidth);
  while (p === q) {
    q = generatePrime(keyWidth);
  }
  const n = p * q;
  const fin = (p - 1n) * (q - 1n);
  let e = generatePrime(keyWidth);
  while (e === p || e === q || e >= fin) {
    e = generatePrime(keyWidth);
  }
  const d = inverseGcd(e, fin);
  return {
    public: [n, e],
    private: [n, d]
  };
};
