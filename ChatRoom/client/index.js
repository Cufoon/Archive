let ws = null; // ws连接
let wsUserList = null; // 聊天室用户列表
let userMode = false; // 私聊还是聊天室
let myUid = null; // 我的uid
let toUser = null; // 当前私信对象
let messageList = new Map(); // 私信消息存储

const scrollToBottom = (element, duration) => {
  if (element.scrollTop === element.scrollHeight - element.clientHeight) return;

  const cosParameter = element.scrollTop;
  const leftParameter = element.scrollHeight - element.clientHeight;
  let scrollCount = 0,
    oldTimestamp = null;

  function step(newTimestamp) {
    if (oldTimestamp !== null) {
      scrollCount += (Math.PI * (newTimestamp - oldTimestamp)) / duration;
      if (scrollCount >= Math.PI)
        return (element.scrollTop = element.scrollHeight);
      element.scrollTop =
        cosParameter + leftParameter * Math.sin(scrollCount / 2);
    }
    oldTimestamp = newTimestamp;
    window.requestAnimationFrame(step);
  }
  window.requestAnimationFrame(step);
};

const login = () => {
  const loginInput = document.getElementById('userName');
  const name = loginInput.value;
  if (name) {
    ws = new WebSocket('ws://localhost:5000');

    ws.onopen = () => {
      const data = JSON.stringify({
        type: 'login',
        name
      });
      ws.send(data);
    };

    ws.onmessage = (e) => {
      const data = JSON.parse(e.data);
      console.log(data);

      if (data.type === 'system-chater-list') {
        let list = data.list;
        wsUserList = list;
        let length = list.length;
        const out = document.getElementById('userListOuter');
        let userList = document.getElementById('userList');
        out.removeChild(userList);
        userList = document.createElement('div');
        userList.setAttribute('class', 'user-list');
        userList.setAttribute('id', 'userList');
        out.appendChild(userList);
        document.getElementById('online').innerText = `在线人数${length}人`;
        for (let i = 0; i < list.length; i++) {
          let p_user = document.createElement('p');
          p_user.setAttribute('class', 'user');
          if (list[i].uid === myUid) {
            p_user.setAttribute('class', 'user user-me');
          }
          p_user.innerText = list[i].name;
          const p_btn = document.createElement('div');
          p_btn.setAttribute('class', 'message-btn');
          p_btn.innerText = '私聊';
          p_btn.onclick = () => {
            const online = document.getElementById('online');
            online.innerText = `<与${list[i].name}私聊`;
            online.onclick = () => {
              userMode = false;
              document.getElementById(
                'online'
              ).innerText = `在线人数${wsUserList.length}人`;
              document.getElementById('contentUser').style.display = 'none';
              document.getElementById('content').style.display = 'block';
              online.onclick = () => {};
            };
            userMode = true;
            toUser = list[i].uid;
            document.getElementById('content').style.display = 'none';
            let cndiv = document.getElementById('contentUser');
            const outer = document.getElementById('contentOuter');
            outer.removeChild(cndiv);
            cndiv = document.createElement('div');
            cndiv.setAttribute('class', 'content');
            cndiv.setAttribute('id', 'contentUser');
            cndiv.style.display = 'block';
            const lll = messageList.get(list[i].uid) || [];
            lll.map((item) => {
              cndiv.appendChild(
                createChatDiv({
                  type: 'user-message-text',
                  ...item
                })
              );
            });
            cndiv.scrollTop = cndiv.scrollHeight;
            outer.appendChild(cndiv);
          };
          p_user.appendChild(p_btn);
          userList.appendChild(p_user);
        }
        return;
      }

      if (data.type === 'user-message-text') {
        let oldContent = document.getElementById('content');
        if (userMode) {
          oldContent = document.getElementById('contentUser');
          const lll = messageList.get(toUser) || [];
          lll.push({
            name: data.name,
            content: data.content
          });
          messageList.set(toUser, lll);
        }
        oldContent.appendChild(createChatDiv(data));
        scrollToBottom(oldContent, 2000);
        return;
      }

      if (data.type === 'system-login-success') {
        document.getElementById('login').style.display = 'none';
        myUid = data.uid;
        const contentDiv = document.getElementById('content');
        data.history.forEach((item) => {
          contentDiv.appendChild(createChatDiv(item));
        });
        contentDiv.scrollTop = contentDiv.scrollHeight;
        return;
      }

      if (data.type === 'user-message') {
        if (myUid !== data.uid) {
          const msglist = messageList.get(data.uid) || [];
          msglist.push({
            name: data.from,
            content: data.content,
            time: data.time
          });
          messageList.set(data.uid, msglist);
        }
        if (userMode) {
          if (myUid !== data.uid) {
            document.getElementById('contentUser').appendChild(
              createChatDiv({
                type: 'user-message-text',
                name: data.from,
                content: data.content,
                time: data.time
              })
            );
          }
        }

        return;
      }
    };
  }
};

document.getElementById('comein').onclick = login;

const createChatDiv = (data) => {
  let div = document.createElement('div');
  let p_time = document.createElement('p');
  const p_user = document.createElement('div');
  let p_content = document.createElement('p');
  const div_outer = document.createElement('div');
  div_outer.setAttribute('class', 'chat-card');
  div_outer.appendChild(p_user);
  div_outer.appendChild(p_content);
  switch (data.type) {
    case 'system-logout':
    case 'system-login':
      p_time.innerHTML = data.time;
      p_content.innerHTML = data.name;
      break;
    case 'user-message-text':
      p_time.innerHTML = data.time;
      p_user.innerHTML = data.name;
      p_content.innerHTML = data.content;
      break;
    default:
      break;
  }

  p_time.setAttribute('class', 'time');
  p_user.setAttribute('class', 'user');
  p_content.setAttribute('class', 'message');

  div.appendChild(p_time);
  div.appendChild(div_outer);

  return div;
};

const send = () => {
  let message = document.getElementById('message');

  if (!message.value) {
    return;
  }
  let data = {
    type: 'text',
    text: message.value
  };
  if (userMode) {
    data = {
      type: 'user-text',
      uid: toUser,
      content: message.value
    };
  }
  ws.send(JSON.stringify(data));
  message.value = '';
};

document.getElementById('sendMessage').onclick = send;
