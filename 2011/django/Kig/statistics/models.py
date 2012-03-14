from datetime import datetime, timedelta
from django.db import models
from threading import Lock

one_minute = timedelta(minutes=1)

def read_last_interval(model):
    last_update = model.objects.aggregate(start_time=models.Max('interval_start_time'))['start_time']
    if last_update:
        # start at end of first minute interval
        return last_update + one_minute
    last_update = MessageHit.objects.aggregate(start_time=models.Min('timestamp'))['start_time']
    if last_update:
        return last_update
    return None

def update_stats(model, update_func):
    last_update = read_last_interval(model)
    if last_update is None:
        return

    now = datetime.now().replace(second=0, microsecond=0)
    last_update = last_update.replace(second=0, microsecond=0)

    while last_update < now:
        interval_start = last_update
        for interval_length in (1,5,30,60):
            if not last_update.minute % interval_length and interval_start < now - interval_length * one_minute:
                update_func(interval_start, interval_length)
            else:
                # 1*5 = 5, *6 = 30, *2 = 60  ==> if we can't divide, we're done
                break
        last_update += one_minute


class MessageHit(models.Model):
    timestamp = models.DateTimeField("timestamp", auto_now_add=True)
    message = models.TextField("message")
    blocking_status = models.BooleanField("blocking status")
    author = models.CharField("author", max_length=50)

    @classmethod
    def _from_xml(cls, xml):
        if xml.tag != 'adminMsg':
            return None
        
        msg = MessageHit(
            message = xml.findtext('content'),
            blocking_status = xml.findtext('blocked')=='true',
            author = xml.findtext('author'))

        return msg

    @classmethod
    def word_histogram(cls):
        word_counter = {}
        for message in cls.objects.values('message'):
            for word in message['message'].lower().split():
                try:
                    word_counter[word] += 1
                except KeyError:
                    word_counter[word] = 1
        return word_counter


class MessageStats(models.Model):
    interval_start_time = models.DateTimeField("interval start time")
    interval_length = models.IntegerField("interval length in minutes")
    message_count = models.IntegerField("message count")
    blocked_messages_count = models.IntegerField("number of blocked messages")
    authors_count = models.IntegerField("number of distinct authors")

    _lock = Lock()
    
    @classmethod
    def update_stats(cls):
        """
        >>> MessageStats.objects.aggregate(models.Max('interval_start_time'))
        """
        with cls._lock:
            update_stats(MessageStats, cls.update_minute_stats)

    @classmethod
    def update_minute_stats(cls, interval_start, interval_length):
        interval_end = interval_start + one_minute * interval_length
        messages = MessageHit.objects.filter(timestamp__gte=interval_start,
                                             timestamp__lt=interval_end)
        num_messages = messages.count()
        num_blocked_messages = messages.filter(blocking_status=True).count()
        num_authors = messages.aggregate(num_authors=models.Count('author', distinct=True))['num_authors']

        ms = MessageStats(interval_start_time=interval_start, interval_length=interval_length,
                          message_count=num_messages, blocked_messages_count=num_blocked_messages,
                          authors_count=num_authors)
        ms.save()

class UserStats(models.Model):
    interval_start_time = models.DateTimeField("interval start time")
    interval_length = models.IntegerField("interval length in minutes")
    author_login_name = models.CharField("login name of user", max_length=50)
    messages = models.IntegerField("number of messages")

    _lock = Lock()

    @classmethod
    def update_stats(cls):
        with cls._lock:
            update_stats(UserStats, cls.update_minute_stats)

    @classmethod
    def update_minute_stats(cls, interval_start, interval_length):
        interval_end = interval_start + one_minute * interval_length
        messages = MessageHit.objects.filter(timestamp__gte=interval_start,
                                             timestamp__lt=interval_end)
        messages_per_author = messages.values('author').annotate(num_messages=models.Count('message'))

        for msg_stats in messages_per_author:
            ms = UserStats(interval_start_time=interval_start, interval_length=interval_length,
                           author_login_name=msg_stats['author'],
                           messages=msg_stats['num_messages'])
            ms.save()
