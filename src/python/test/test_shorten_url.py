from redis import Redis

from main.shorten_url import URLShorten


def test_shorten_url():
    redis = Redis(decode_responses=True)
    redis.flushall()
    url_shorten = URLShorten(redis)

    short_id = url_shorten.shorten('https://github.com/yanglbme')
    assert short_id == '100000'

    source_url = url_shorten.restore(short_id)
    assert source_url == 'https://github.com/yanglbme'

    assert url_shorten.shorten('https://doocs.github.io') == '100001'
