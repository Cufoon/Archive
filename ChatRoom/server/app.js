const WebSocket = require('ws');

const formatTime = (date, fmt) => {
  var o = {
    'M+': date.getMonth() + 1, //月份
    'd+': date.getDate(), //日
    'h+': date.getHours(), //小时
    'm+': date.getMinutes(), //分
    's+': date.getSeconds(), //秒
    'q+': Math.floor((date.getMonth() + 3) / 3), //季度
    S: date.getMilliseconds() //毫秒
  };
  if (/(y+)/.test(fmt))
    fmt = fmt.replace(
      RegExp.$1,
      (date.getFullYear() + '').substr(4 - RegExp.$1.length)
    );
  for (var k in o)
    if (new RegExp('(' + k + ')').test(fmt))
      fmt = fmt.replace(
        RegExp.$1,
        RegExp.$1.length == 1 ? o[k] : ('00' + o[k]).substr(('' + o[k]).length)
      );
  return fmt;
};

let pointH = 0;
const messageHistory = [];
const saveHistory = (data) => {
  if (pointH > 499) {
    pointH = 0;
  }
  messageHistory[pointH] = data;
  pointH++;
};

const getHistory = () => [
  ...messageHistory.slice(pointH, 500),
  ...messageHistory.slice(0, pointH)
];

const wss = new WebSocket.Server({
  port: 5000,
  perMessageDeflate: {
    zlibDeflateOptions: {
      // See zlib defaults.
      chunkSize: 1024,
      memLevel: 7,
      level: 3
    },
    zlibInflateOptions: {
      chunkSize: 10 * 1024
    },
    // Other options settable:
    clientTracking: true,
    clientNoContextTakeover: true, // Defaults to negotiated value.
    serverNoContextTakeover: true, // Defaults to negotiated value.
    serverMaxWindowBits: 10, // Defaults to negotiated value.
    // Below options specified as default values.
    concurrencyLimit: 10, // Limits zlib concurrency for perf.
    threshold: 1024 // Size (in bytes) below which messages
    // should not be compressed.
  }
});

const broadcast = (message) => {
  console.log(message);
  const data = JSON.stringify(message);
  wss.clients.forEach((client) => {
    if (client.readyState === WebSocket.OPEN) {
      client.send(data);
    }
  });
};

let uid = 0n;
const genUid = () => `ws-uid-${uid++}`;
const userList = new Map();

const getUserList = () => {
  const result = [];
  userList.forEach((item) => {
    result.push({
      name: item.name,
      uid: item.uid
    });
  });
  return result.sort((a, b) => a.time > b.time);
};

wss.on('connection', (ws, req) => {
  // 获取连接者的 id
  const ip = req.socket.remoteAddress;
  // 当接收到客户端发过来的消息的时候
  ws.on('message', (content) => {
    const data = JSON.parse(content);
    const type = data.type || 'none';
    // 进入聊天室的情况
    if (type === 'login') {
      const uid = genUid();
      userList.set(uid, {
        name: data.name || '无名',
        ip,
        time: Date.now(),
        uid,
        ws
      });
      ws.uid = uid;
      ws.send(
        JSON.stringify({
          type: 'system-login-success',
          uid,
          history: getHistory()
        })
      );
      broadcast({
        type: 'system-login',
        content: `--> ${data.name || '无名'} <-- 加入聊天！`
      });
      broadcast({ type: 'system-chater-list', list: getUserList() });
      return;
    }
    // 聊天室消息
    if (type === 'text') {
      const msg = {
        type: 'user-message-text',
        name: userList.get(ws.uid).name,
        content: data.text,
        time: formatTime(new Date(), 'yyyy-MM-dd hh:mm:ss')
      };
      broadcast(msg);
      saveHistory(msg);
      return;
    }
    // 私信消息
    if (type === 'user-text') {
      const timeStr = formatTime(new Date(), 'yyyy-MM-dd hh:mm:ss');
      // 发给私信接收者
      userList.get(data.uid).ws.send(
        JSON.stringify({
          type: 'user-message',
          from: userList.get(ws.uid).name,
          uid: ws.uid,
          content: data.content,
          time: timeStr
        })
      );
      // 回馈给私信的发送者
      ws.send(
        JSON.stringify({
          type: 'user-message-text',
          name: userList.get(ws.uid).name,
          content: data.content,
          time: timeStr
        })
      );
      return;
    }
  });

  // 连接断开的时候
  ws.on('close', () => {
    broadcast({
      type: 'system-logout',
      content: `--> ${userList.get(ws.uid).name || '无名'} <-- 离开聊天。`
    });
    userList.delete(ws.uid);
    broadcast({ type: 'system-chater-list', list: getUserList() });
  });
});
