from unittest import TestCase

from redis import Redis

from main.social_relationship import SocialRelationship

test_case = TestCase()


def test_sns():
    redis = Redis(decode_responses=True)
    bingo = SocialRelationship(redis, 'Bingo')
    iris = SocialRelationship(redis, 'Iris')
    bingo.follow('Iris')
    bingo.follow('GitHub')
    bingo.follow('Apple')
    iris.follow('Bingo')
    iris.follow('GitHub')

    assert bingo.is_following('Iris') is True
    assert iris.is_following('Bingo') is True
    assert bingo.count_following() == 3
    test_case.assertListEqual(['GitHub'], bingo.get_common_following('Iris'))
