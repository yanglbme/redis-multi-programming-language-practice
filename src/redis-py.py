from redis import Redis

client = Redis(decode_responses=True)

client.set('author', 'yanglbme')
client.set('wechat_id', 'YLB0109')
client.set('link', 'https://github.com/yanglbme')
client.set('email', 'contact@yanglibin.info')

print('Hello, Redis')

