from redis import Redis


class AutoComplete:
    def __init__(self, client: Redis):
        self.client = client

    def feed(self, keyword: str, weight):
        for i in range(1, len(keyword)):
            key = 'auto_complete:' + keyword[:i]
            self.client.zincrby(key, weight, keyword)

    def hint(self, prefix: str, count=10):
        key = 'auto_complete:' + prefix
        return self.client.zrevrange(key, 0, count - 1)
