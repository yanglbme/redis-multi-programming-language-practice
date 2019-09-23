from redis import Redis

from main.like_dislike import Like


def test_like_dislike():
    redis = Redis(decode_responses=True)
    redis.flushall()
    like_entity = Like(redis, 'user1')
    assert like_entity.get_like_count() == 0
    like_entity.like('user2')
    like_entity.like('user3')

    like_entity.dislike('user4')

    assert like_entity.get_like_count() == 2
    assert like_entity.get_like_status('user2') == 1
    assert like_entity.get_like_status('user4') == -1

