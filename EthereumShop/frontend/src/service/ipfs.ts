import * as ipfs from 'ipfs-http-client';

export const IPFSClient = ipfs.create({
  url: 'http://localhost:5001/api/v0'
});
