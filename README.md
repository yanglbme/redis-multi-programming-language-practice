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

- Mac：`brew install redis`
- Ubuntu：`apt-get install redis`
- Windows：访问 https://www.memurai.com/get-memurai 下载后安装程序。
- 其它：请自行搜索安装方式，就不在此赘述了。

安装后，执行 `redis-cli` 命令即可启动 Redis 客户端。

```bash
C:\Users\user>redis-cli
127.0.0.1:6379> keys *
 1) "fruits"
 2) "story"
 3) "or-result"
```

## Redis 数据结构与应用
Redis 基础的数据结构有 5 种，分别是：

- string（字符串）
- list（列表）
- hash（哈希）
- set（集合）
- zset（有序集合）

每一种数据结构都有它对应的实践场景，下面几个小节会就这几种数据结构展开讨论，也欢迎各位开发朋友来分享更多的应用案例。

### 字符串

### 列表
- 如何利用 Redis List 实现异步消息队列？

### 哈希

### 集合

### 有序集合