# 使用node搭建服务器

> 2024年7月3日19:59:51

创建一个本地服务器并在 Node.js 中提供静态文件的一个常见方式是使用 express.js 框架和 middleware。下面是一种可能的做法：

1.首先，确保您的设备上已经安装了 node.js 和 npm 。若还未安装，可从此处下载：https://nodejs.org/

2.创建一个新的文件夹来存放你的服务器代码，这个例子中我们将它叫做 "myServer"

3.在 "myServer" 文件夹内，打开命令行，输入以下命令创建一个新的node.js项目:

```shell
npm init -y
```
这将创建一个名为 `package.json`的新文件。

4.然后，使用 npm 安装 express：

```shell
npm install express
```
5.在 "myServer" 文件夹下创建一个新文件，比如叫做 `app.js` ，然后在这个文件内输入以下代码：

```javascript
const express = require('express'); // 引用express库
const app = express(); // 创建express应用
const path = require('path');

// `__dirname`变量是当前脚本所在的目录
// 访问`http://127.0.0.1:9527/pic`，就等于访问`public`文件夹
app.use('/pic', express.static(path.join(__dirname, 'public')));

app.listen(9527, function () {
    // 服务器正在监听9527端口
    console.log('Server is running at http://127.0.0.1:9527/pic');
});
```
这就会创建一个新的 Express 应用，并且还配置了一个 middleware 来在 "/myPic" URL 下提供在你的 "c:/mycod" 文件夹中的静态文件。

6.保存这个文件。

7.返回到命令行，启动你的服务器：

```shell
node app.js
```

现在，你就可以在浏览器中访问 `http://127.0.0.1:9527/myPic/xxx.jpg` 来查看你在 "c:/mycod" 下的 "xxx.jpg" 文件了。