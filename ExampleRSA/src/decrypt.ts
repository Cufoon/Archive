import { powModBig, r64ToBig } from './util';
import fs from 'fs';
const data = fs.readFileSync('./data/key.txt');
const [n, d] = data
  .toString()
  .split(' ')
  .map((item) => BigInt(item));

const decodeList = fs.readFileSync('./data/encode.txt').toString().slice(0, -1).split('+');

let decode: number[] = [];
decodeList.forEach((item) => {
  let rStr = powModBig(r64ToBig(item), d, n).toString(16);

  if (rStr.length & 1) {
    rStr = '0' + rStr;
  }
  const rList = rStr.match(/.{2}/g);
  if (rList) {
    decode.push(...rList.map((item) => parseInt(item, 16)));
  }
});

const buf = Buffer.from(decode);

console.log(buf.toString());
fs.writeFileSync('./data/decode.txt', buf.toString());
