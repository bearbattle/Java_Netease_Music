# Java 网易云音乐客户端（2019 北航 Java 课程大作业）
## 作者
* 18373463 熊胡超（图形界面）
* 18373444 田韵豪（网易云音乐 API 适配工作；歌词动态解析）

## 功能
* 搜索、浏览歌曲
* 浏览自己的歌单（歌单与[网易云音乐](http://music.163.com/)中的数据同步）
* 播放歌曲

## 使用方法
在 [Releases](https://github.com/bearbattle/Java_Netease_Music/releases) 页面可以下载编译打包好的软件。需要安装 JDK13。

下载完成后，进入 neteasemusic\bin 文件夹，运行 neteasemusic.bat 即可打开软件。

打开软件后，输入自己的网易云音乐账号和密码（用于测试的账号密码已经包含在 release 包内），即可查看自己的歌单。
* 在“歌单”选项卡中，可以查看歌单中的歌曲，双击歌曲可以播放。
* 在“搜索”选项卡中，可以输入关键词并搜索歌曲，双击歌曲可以播放。
* 在“播放”选项卡中，可以查看歌曲的封面，调整播放进度，控制音量大小。对于有歌词的音乐，可以查看歌词。

## 实现方法
* 对接网易云音乐的 API（`com.bear.neteasemusic.api` 包），使用 okhttp 调用 API。
* 使用 JavaFX 实现 GUI（`com.bear.neteasemusic.panel` 包）。
