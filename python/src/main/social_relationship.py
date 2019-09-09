from time import time

from redis import Redis


def following_key(user):
    return 'following:' + user


def followers_key(user):
    return 'followers:' + user


def common_following_key(user1, user2):
    return 'common:following:{}:{}'.format(user1, user2)


class SocialRelationship:
    def __init__(self, client: Redis, user):
        self.client = client
        self.user = user

    def follow(self, target):
        """关注目标用户"""
        self.client.zadd(following_key(self.user), {target: time()})
        self.client.zadd(followers_key(target), {self.user: time()})

    def unfollow(self, target):
        """取关目标用户"""
        self.client.zrem(following_key(self.user), target)
        self.client.zrem(followers_key(target), self.user)

    def is_following(self, target):
        """判断是否关注着目标用户"""
        return self.client.zrank(following_key(self.user), target) is not None

    def get_all_following(self):
        """获取当前用户关注的所有人，并按最近关注时间倒序排列"""
        return self.client.zrevrange(following_key(self.user), 0, -1)

    def get_all_followers(self):
        """获取当前用户的所有粉丝，并按最近关注时间倒序排列"""
        return self.client.zrevrange(followers_key(self.user), 0, -1)

    def count_following(self):
        """获取当前用户关注的人数"""
        return self.client.zcard(following_key(self.user))

    def count_followers(self):
        """获取当前用户的粉丝数"""
        return self.client.zcard(followers_key(self.user))

    def get_common_following(self, one):
        """获取与某用户的共同关注"""
        common_key = common_following_key(self.user, one)
        self.client.zinterstore(
            common_key, (following_key(self.user), following_key(one)))
        return self.client.zrevrange(common_key, 0, -1)
