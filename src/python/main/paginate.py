from redis import Redis


class Paginate:
    def __init__(self, client: Redis, key: str):
        self.client = client
        self.key = key

    def add(self, item):
        """添加元素到分页列表中"""
        self.client.lpush(self.key, item)

    def get_page(self, page: int, per_page: int):
        """根据页码取出指定数量的元素"""
        start = (page - 1) * per_page
        end = page * per_page - 1
        return self.client.lrange(self.key, start, end)

    def get_size(self):
        """获取列表的元素数量"""
        return self.client.llen(self.key)
