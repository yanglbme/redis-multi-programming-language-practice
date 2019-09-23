from redis import Redis


def get_like_key(entity_id):
    return 'like:{}'.format(entity_id)


def get_dislike_key(entity_id):
    return 'dislike:{}'.format(entity_id)


class Like:
    def __init__(self, client: Redis, entity_id: str):
        self.client = client
        self.entity_id = entity_id

    def like(self, user_id) -> int:
        """某用户点赞"""
        self.client.sadd(get_like_key(self.entity_id), user_id)
        self.client.srem(get_dislike_key(self.entity_id), user_id)
        return self.client.scard(get_like_key(self.entity_id))

    def dislike(self, user_id) -> int:
        """某用户点踩"""
        self.client.sadd(get_dislike_key(self.entity_id), user_id)
        self.client.srem(get_like_key(self.entity_id), user_id)
        return self.client.scard(get_like_key(self.entity_id))

    def get_like_status(self, user_id) -> int:
        """判断用户是否点赞或点踩了，点赞返回1，点踩返回-1，不赞不踩返回0"""
        if self.client.sismember(get_like_key(self.entity_id), user_id):
            return 1
        if self.client.sismember(get_dislike_key(self.entity_id), user_id):
            return -1
        return 0

    def get_like_count(self) -> int:
        """获取当前点赞数"""
        return self.client.scard(get_like_key(self.entity_id))
