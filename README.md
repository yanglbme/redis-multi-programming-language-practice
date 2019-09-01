# Redis 底层原理分析与多语言应用实践
[![license](https://badgen.net/github/license/doocs/redis-multi-programming-language-practice?color=green)](https://github.com/doocs/redis-multi-programming-language-practice/blob/master/LICENSE)
[![PRs Welcome](https://badgen.net/badge/PRs/welcome/green)](http://makeapullrequest.com)
[![stars](https://badgen.net/github/stars/doocs/redis-multi-programming-language-practice)](https://github.com/doocs/redis-multi-programming-language-practice/stargazers)
[![contributors](https://badgen.net/github/contributors/doocs/redis-multi-programming-language-practice)](https://github.com/doocs/redis-multi-programming-language-practice/graphs/contributors)
[![help-wanted](https://badgen.net/github/label-issues/doocs/redis-multi-programming-language-practice/help%20wanted/open)](https://github.com/doocs/redis-multi-programming-language-practice/labels/help%20wanted)
[![issues](https://badgen.net/github/open-issues/doocs/redis-multi-programming-language-practice)](https://github.com/doocs/redis-multi-programming-language-practice/issues)
[![PRs Welcome](https://badgen.net/badge/PRs/welcome/green)](http://makeapullrequest.com)
[![doocs-open-source-organization](https://badgen.net/badge/organization/join%20us/cyan)](#how-to-join)
[![gitter](https://badgen.net/badge/gitter/chat/cyan)](https://gitter.im/doocs)

## 前言
本项目使用 Redis 最新稳定版本 v5.0，不同操作系统的安装方式如下：

- Windows：访问 https://www.memurai.com/get-memurai 下载后启动程序。
- Ubuntu：`apt-get install redis`
- Mac：`brew install redis`
- 其它：自行安装。

安装后，执行 `redis-cli` 命令即可启动 Redis 客户端。

```bash
C:\Users\user>redis-cli
127.0.0.1:6379> keys *
 1) "fruits"
 2) "story"
 3) "or-result"
```
