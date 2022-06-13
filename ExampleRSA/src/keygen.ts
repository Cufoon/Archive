import fs from 'fs';
import { generateRsaKey } from './generate';
import { Command } from 'commander';

const program = new Command();

program.option('-n, --length <number>', 'set the key width');

program.parse(process.argv);

const options = program.opts();
if (options.length !== undefined) {
  const datapath = './data';
  const key = generateRsaKey(parseInt(options.length));
  if (!fs.existsSync(datapath)) {
    fs.mkdirSync(datapath, { recursive: true });
  }
  fs.writeFileSync('./data/key.txt', `${key.private[0].toString()} ${key.private[1].toString()}`);
  fs.writeFileSync('./data/key_pub.txt', `${key.public[0].toString()} ${key.public[1].toString()}`);
}
