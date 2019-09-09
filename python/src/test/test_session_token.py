from redis import Redis

from main.session_token import LoginSession


def test_session_token():
    redis = Redis(decode_responses=True)
    redis.flushall()
    session = LoginSession(redis, 'bingo')

    user_token = session.create()

    res = session.validate('this is a wrong token')
    assert res == 'SESSION_TOKEN_INCORRECT'

    res = session.validate(user_token)
    assert res == 'SESSION_TOKEN_CORRECT'

    session.destroy()
    res = session.validate(user_token)
    assert res == 'SESSION_NOT_LOGIN'
