import { powModBig, bigToR64 } from './util';
import fs from 'fs';

const data = fs.readFileSync('./data/key_pub.txt');
const [n, e] = data
  .toString()
  .split(' ')
  .map((item) => BigInt(item));

const toEncodeBuf = fs.readFileSync('./data/plain.txt');

const maxCryptBits = BigInt(n).toString(2).length;
const maxCryptBytes = Math.floor((maxCryptBits - 1) / 8);
const splitRegx = new RegExp(`(.${maxCryptBytes * 8})|(.{4,${(maxCryptBytes - 1) * 8}})`, 'g');

let result = '';
toEncodeBuf.forEach((item) => {
  result += item.toString(2).padStart(8, '0');
});

const chunkList = result.match(splitRegx);
if (chunkList) {
  let crypt = '';
  chunkList.forEach((item) => {
    crypt += bigToR64(powModBig(BigInt(`0b${item}`), e, n)) + '+';
  });
  console.log(crypt);
  crypt && fs.writeFileSync('./data/encode.txt', crypt);
}
