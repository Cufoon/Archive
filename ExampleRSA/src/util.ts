import { primes, r64char } from './const';

export const powMod = (base: number, exponent: number, mod: number): number => {
  let result = 1 % mod;
  while (exponent) {
    if (exponent & 1) {
      result = (result * base) % mod;
    }
    base = (base * base) % mod;
    exponent >>= 1;
  }
  return result;
};

export const powModBig = (base: bigint, exponent: bigint, mod: bigint): bigint => {
  let result = 1n % mod;
  while (exponent) {
    if (exponent & 1n) {
      result = (result * base) % mod;
    }
    base = (base * base) % mod;
    exponent >>= 1n;
  }
  return result;
};

export const isPrime = (num: bigint): boolean => {
  for (let i = 0; i < primes.length; i++) {
    if (primes[i] == num) return true;
    if (primes[i] > num) return false;
    let t = powModBig(primes[i], num - 1n, num);
    let x = num - 1n;
    if (t != 1n) return false;
    while (t == 1n && x % 2n == 0n) {
      x = x / 2n;
      t = powModBig(primes[i], x, num);
      if (t != 1n && t != num - 1n) return false;
    }
  }
  return true;
};

export const inverseGcd = (origin: bigint, end: bigint): bigint => {
  let [u1, u2, u3] = [1n, 0n, origin];
  let [v1, v2, v3] = [0n, 1n, end];
  while (v3 != 0n) {
    const q = u3 / v3;
    [v1, v2, v3, u1, u2, u3] = [u1 - q * v1, u2 - q * v2, u3 - q * v3, v1, v2, v3];
  }
  return ((u1 % end) + end) % end;
};

export const bigToR64 = (num: bigint): string => {
  let binStr = num.toString(8);
  const rest = binStr.length & 1;
  if (rest) {
    binStr = '0' + binStr;
  }
  const result = binStr
    .match(/.{2}/g)
    ?.map((item) => r64char[parseInt(item, 8)])
    .join('');
  return result || '';
};

export const r64ToBig = (src: string): bigint => {
  let result = '0b';
  for (const c of src) {
    const ascii = c.charCodeAt(0);
    let offset = 0;
    if (ascii > 96 && ascii < 123) {
      offset = -87;
    } else if (ascii > 64 && ascii < 91) {
      offset = -29;
    } else if (ascii === 61) {
      offset = 1;
    } else if (ascii > 47 && ascii < 58) {
      offset = -48;
    } else {
      offset = 28;
    }
    result += (ascii + offset).toString(2).padStart(6, '0');
  }
  return BigInt(result);
};
