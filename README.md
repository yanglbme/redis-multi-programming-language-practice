# Redis 底层原理分析与多语言应用实践<sup>[©](https://github.com/yanglbme)</sup>

[![prs-welcome](https://badgen.net/badge/PRs/welcome/green)](http://makeapullrequest.com)
[![doocs-open-source-organization](https://badgen.net/badge/organization/join%20us/cyan)](https://doocs.github.io/#/?id=how-to-join)
[![gitter](https://badgen.net/badge/gitter/chat/cyan)](https://gitter.im/doocs)

本项目主要讲解 Redis 的底层原理以及在各种场景下的应用。所有演示代码均基于 Redis 最新稳定版本 `v6`，不同操作系统下 Redis 的安装方式请自行搜索，就不在此赘述了。

另，本项目针对不同编程语言，使用了其对应的 Redis 库，方便程序对 Redis 进行各项操作：

- Python: 使用 pip 安装 redis 库，[`pip install redis`](https://pypi.org/project/redis/)
- Java: 使用 gradle 导入 jedis 库，[`implementation group: 'redis.clients', name: 'jedis', version: '3.7.0'`](https://mvnrepository.com/artifact/redis.clients/jedis/3.7.0)

欢迎补充更多的实际应用场景，让项目内容更加完善。如果你认为演示代码有待改进，可以在 Issues 区反馈，当然，你也可以直接发起 Pull Request。

## Redis 数据结构与应用

### [String 字符串](/docs/redis-string-introduction.md)

- [说说如何基于 Redis 实现分布式锁？](/docs/redis-distributed-lock.md)

### [List 列表](/docs/redis-list-introduction.md)

- 如何利用 Redis List 实现异步消息队列？
- [用 Redis 如何实现页面数据分页的效果？](/docs/redis-list-paginate.md)

### [Set 集合](/docs/redis-set-introduction.md)

- [如何用 Redis 实现论坛帖子的点赞点踩功能？](/docs/redis-set-like-and-dislike.md)

### [Sorted Sets 有序集合](/docs/redis-sorted-set-introduction.md)

- [社交网站通常会有粉丝关注的功能，用 Redis 怎么实现？](/docs/redis-sorted-set-sns-follow.md)
- [每日、每周、每月积分排行榜功能该怎么实现？](/docs/redis-sorted-set-ranking-or-trending-list.md)
- [关键词搜索，如何用 Redis 实现自动补全？](/docs/redis-sorted-set-auto-complete.md)

### [Hash 哈希](/docs/redis-hash-introduction.md)

- [登录会话，用 Redis 该怎么做？](/docs/redis-hash-session-token.md)
- [如何使用 Redis 实现短网址服务？](/docs/redis-hash-shorten-url.md)

### [HyperLogLog](/docs/redis-hyperLogLog-introduction.md)

### [Bitmap 位图](/docs/redis-bitmap-introduction.md)
