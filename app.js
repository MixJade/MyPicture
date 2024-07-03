const express = require('express'); // 引用express库
const app = express(); // 创建express应用
const path = require('path');

// `__dirname`变量是当前脚本所在的目录
// 访问`http://127.0.0.1:9527/pic`，就等于访问`public`文件夹
app.use('/pic', express.static(path.join(__dirname, 'public')));

app.listen(9527, function () {
    // 服务器正在监听9527端口
    console.log('Server is running at http://127.0.0.1:9527/pic');
    console.log('测试图片 at http://127.0.0.1:9527/pic/wallpaper/wp003.jpeg');
});