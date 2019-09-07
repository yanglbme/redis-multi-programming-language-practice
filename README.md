# Redis 底层原理分析与多语言应用实践<sup>[©](https://github.com/yanglbme)</sup>
[![Languages](https://badgen.net/badge/langs/Python,Java,.../green?list=1)](https://github.com/yanglbme/redis-multi-programming-language-practice)
[![PRs Welcome](https://badgen.net/badge/PRs/welcome/green)](http://makeapullrequest.com)
[![doocs-open-source-organization](https://badgen.net/badge/organization/join%20us/cyan)](#how-to-join)
[![gitter](https://badgen.net/badge/gitter/chat/cyan)](https://gitter.im/doocs)

本项目主要讲解 Redis 的底层原理以及在各种场景下的应用。所有演示代码均基于 Redis 最新稳定版本 `v5.0.3`，不同操作系统下 Redis 的安装方式请自行搜索，就不在此赘述了。

另，本项目针对不同编程语言，使用了其对应的 Redis 库，方便程序对 Redis 进行各项操作：

| # | Lang | Lib | Version | Usage |
|---|---|---|---|---|
| 1 | Python3 | [`redis`](https://pypi.org/project/redis/) | `v3.3.8` | `pip3 install redis `|
| 2 | Java8 | [`jedis`](https://mvnrepository.com/artifact/redis.clients/jedis/3.1.0) | `v3.1.0` | `compile group: 'redis.clients', name: 'jedis', version: '3.1.0'` |

欢迎补充更多的实际应用场景，让项目内容更加完善。如果你认为演示代码有待改进，可以在 Issues 区反馈，当然，你也可以直接发起 Pull Request。

注，本项目部分知识点参考以下两本书籍，内容很好，都值得一读，所以我个人推荐你购买学习，相信一定会有不少收获。

| # | Title | Author | Summary |
|---|---|---|---|
| 1 | Redis 深度历险：核心原理和应用实践[2018] | 钱文品 | 从 Redis 的基础使用出发，结合实际项目中遇到的诸多应用场景，最后详细讲解集群环境，图文并茂地对 Redis 的特性做了全面解析。 |
| 2 | Redis 使用手册[2019] | 黄健宏 | 帮助读者学习如何将 Redis 应用到实际开发中，内容涵盖最新的 Redis 5。 |


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
- [每日、每周、每月积分排行榜功能该怎么实现？](/docs/redis-sorted-set-ranking-or-trending-list.md)

### [Hash 哈希](/docs/redis-hash-introduction.md)
- [登录会话，用 Redis 该怎么做？](/docs/redis-hash-session-token.md)
- [如何使用 Redis 实现短网址服务？](/docs/redis-hash-shorten-url.md)

### [HyperLogLog](/docs/redis-hyperLogLog-introduction.md)

### [Bitmap 位图](/docs/redis-bitmap-introduction.md)