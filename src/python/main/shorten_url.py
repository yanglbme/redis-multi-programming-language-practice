from redis import Redis


# 10进制数转换为36进制字符串
def base10_to_base36(number: int) -> str:
    alphabets = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ'
    result = ''
    while number != 0:
        number, i = divmod(number, 36)
        result = alphabets[i] + result

    return result or alphabets[0]


URL_HASH_SHORT_SOURCE_KEY = 'url_hash:short_source'
ID_COUNTER = 'short_url:id_counter'


class URLShorten:

    def __init__(self, client: Redis):
        self.client = client
        # 设置初始ID值，保留1-5位的短码，从6位的短码开始生成
        self.client.set(ID_COUNTER, 36 ** 5 - 1)

    def shorten(self, source_url: str) -> str:
        """对源网址进行缩短，返回短网址ID"""
        new_id = self.client.incr(ID_COUNTER)
        short_id = base10_to_base36(new_id)
        self.client.hset(URL_HASH_SHORT_SOURCE_KEY, short_id, source_url)
        return short_id

    def restore(self, short_id: str) -> str:
        """根据短网址ID，返回对应的源网址"""
        return self.client.hget(URL_HASH_SHORT_SOURCE_KEY, short_id)
