const addPrint = (content) => {
  const parent = document.getElementById('app');
  const newChild = document.createElement('p');
  newChild.innerText = content;
  parent.appendChild(newChild);
};

const deleteDeployment = (url, id, index, token) => {
  axios({
    headers: {
      Accept: 'application/vnd.github.v3+json',
      Authorization: `token ${token}`
    },
    method: 'DELETE',
    url: url
  })
    .then((response) => {
      addPrint(`删除第 ${index} 个 成功√`);
      console.log('删除第 ', index, ' 个 ', id, ' 成功 ', response);
    })
    .catch((error) => {
      addPrint(`删除第 ${index} 个 失败×`);
      console.log('删除第 ', index, ' 个 ', id, ' 失败 ', error);
    });
};

const makeInactiveAndDelete = (req, token) => {
  req.map((item, index) => {
    axios({
      headers: {
        Accept: 'application/vnd.github.ant-man-preview+json',
        Authorization: `token ${token}`
      },
      method: 'POST',
      url: item.statuses_url,
      data: {
        state: 'inactive'
      }
    })
      .then((response) => {
        addPrint(`修改第 ${index + 1} 个 status 为 inactive 成功√`);
        console.log('修改第 ', index + 1, ' 个 ', item.id, ' status 为 inactive  ', response);
        deleteDeployment(item.url, item.id, index + 1, token);
      })
      .catch((error) => {
        addPrint(`修改第 ${index + 1} 个 status 为 inactive 失败×`);
        console.log('修改第 ', index + 1, ' 个 ', item.id, ' status 失败  ', error);
      });
  });
};

const runDelete = (username, reponame, environment, token, keep = 'none') => {
  if (username && reponame && environment && token) {
    axios({
      url: `https://api.github.com/repos/${String(username)}/${String(reponame)}/deployments`,
      method: 'GET',
      params: {
        environment: String(environment)
      },
      headers: {
        Accept: 'application/vnd.github.v3+json',
        Authorization: `token ${token}`
      }
    })
      .then((response) => {
        const req = response.data;
        if (keep === 'keep') {
          const [_, ...rest] = req;
          console.log('获取的 Deployment 列表', rest);
          makeInactiveAndDelete(rest, token);
        } else if (keep === 'none') {
          console.log('获取的 Deployment 列表', req);
          makeInactiveAndDelete(req, token);
        }
      })
      .catch((error) => {
        console.log('获取 Deployment 列表 失败  ', error);
      });
  }
};

window.CufoonAPP = {
  deleteDeployment: runDelete
};
