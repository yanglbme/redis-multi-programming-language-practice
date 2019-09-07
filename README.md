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

本项目主要讲解 Redis 的底层原理以及在各种场景下的应用。所有演示代码均基于 Redis 最新稳定版本 `v5.0.3`，不同操作系统下 Redis 的安装方式请自行搜索，就不在此赘述了。

另，本项目针对不同编程语言，使用了其对应的 Redis 库，方便程序对 Redis 进行各项操作：

| # | 语言 | 库 | 版本 | 使用 |
|---|---|---|---|---|
| 1 | Python3 | [`redis`](https://pypi.org/project/redis/) | `v3.3.8` | `pip3 install redis `|
| 2 | Java8 | [`jedis`](https://mvnrepository.com/artifact/redis.clients/jedis/3.1.0) | `v3.1.0` | `compile group: 'redis.clients', name: 'jedis', version: '3.1.0'` |

欢迎各位开发朋友补充更多的实际应用场景，让项目内容更加完善。

书籍参考：

| # | 书名 | 作者 | 推荐指数 |
|---|---|---|---|
| 1 | Redis 深度历险：核心原理和应用实践[2018] | 钱文品 | ⭐⭐⭐⭐⭐ |
| 2 | Redis 使用手册[2019] | 黄健宏 | ⭐⭐⭐⭐⭐ |


学习下面知识之前，先来[感受一波 Redis 面试连环炮](/docs/redis-interview.md)。

## Redis 数据结构与应用

### [String 字符串](/docs/redis-string-introduction.md)
- [说说如何基于 Redis 实现分布式锁？](/docs/redis-distributed-lock.md)

### [List 列表](/docs/redis-list-introduction.md)
- 如何利用 Redis List 实现异步消息队列？
- [用 Redis 如何实现页面数据分页的效果？](/docs/redis-list-paginate.md)

### [Set 集合](/docs/redis-set-introduction.md)

### [Sorted Sets 有序集合](/docs/redis-sorted-set-introduction.md)
- [社交网站通常会有粉丝关注的功能，用 Redis 怎么实现？](/docs/redis-sorted-set-sns-follow.md)

### [Hash 哈希](/docs/redis-hash-introduction.md)
- [登录会话，用 Redis 该怎么做？](/docs/redis-hash-session-token.md)
- [如何使用 Redis 实现短网址服务？](/docs/redis-hash-shorten-url.md)

### [HyperLogLog](/docs/redis-hyperLogLog-introduction.md)

### [Bitmap 位图](/docs/redis-bitmap-introduction.md)