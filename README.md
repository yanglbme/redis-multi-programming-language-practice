# Redis 底层原理分析与多语言应用实践<sup>[©](https://github.com/yanglbme)</sup>
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
本项目所有演示代码均基于 Redis 最新稳定版本 `v5.0.3`，不同操作系统下 Redis 的安装方式请自行搜索，就不在此赘述了。

另，本项目针对不同编程语言，使用了其对应的 Redis 库，方便程序对 Redis 进行各项操作：

| lang | lib | version | usage |
|---|---|---|---|
| Python3 | [`redis`](https://pypi.org/project/redis/) | `v3.3.8` | `pip3 install redis `|
| Java8 | [`jedis`](https://mvnrepository.com/artifact/redis.clients/jedis/3.1.0) | `v3.1.0` | `compile group: 'redis.clients', name: 'jedis', version: '3.1.0'` |

## Redis 数据结构与应用

### 1. [String 字符串](/docs/redis-string-introduction.md)
- [说说如何基于 Redis 实现分布式锁？](/docs/redis-distributed-lock.md)

### 2. [List 列表](/docs/redis-list-introduction.md)
- 如何利用 Redis List 实现异步消息队列？

### 3. [Set 集合](/docs/redis-set-introduction.md)

### 4. [Sorted Sets 有序集合](/docs/redis-sorted-set-introduction.md)
- [社交网站通常会有粉丝关注的功能，用 Redis 怎么实现？](/docs/redis-sorted-set-sns-follow.md)

### 5. [Hash 哈希](/docs/redis-hash-introduction.md)
- [登录会话，用 Redis 该怎么做？](/docs/redis-hash-session-token.md)
- [如何使用 Redis 实现短网址服务？](/docs/redis-hash-shorten-url.md)

### 6. [HyperLogLog](/docs/redis-hyperLogLog-introduction.md)

### 7. [Bitmap 位图](/docs/redis-bitmap-introduction.md)