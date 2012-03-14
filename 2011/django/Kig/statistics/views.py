# Create your views here.
from datetime import timedelta, datetime
from django.http import HttpResponse
from statistics.models import MessageStats, MessageHit, UserStats
from django.shortcuts import render_to_response
from django.core.context_processors import csrf
import xml.etree.ElementTree as ET

from operator import itemgetter
from heapq import nlargest


def render_response(request, template, kwargs):
    kwargs.update(csrf(request))
    return render_to_response(template, kwargs)

def index(request):
    UserStats.update_stats()
    MessageStats.update_stats()

    now = datetime.now()
    msg_stats = []
    user_stats = []
    img_messages = []

    msg_stats_img = '"http://chart.apis.google.com/chart?chf=a,s,000000&chxr=0,0,46|1,-1.667,100&chxs=0,676767,11.5,0.167,l,676767|1,676767,11.5,0,lt,676767&chxt=y,x&chs=293x225&cht=lc&chco=224499,FF0000&chds=0,100,-1.667,100&chd=t:10,20,30,50,40|2,13,23,37,27&chdl=Messages|Blocked+messages&chg=14.3,-1,1,1&chls=1,10,5|1,5,5&chm=B,3366CC99,0,0,0|B,FF00009B,1,0,0&chtt=(Blocked)+Messages" width="293" height="225" alt="(Blocked) Messages"'

    PRE = 'http://chart.apis.google.com/chart?chf=a,s,000000&chxr=0,0,46|1,-1.667,100&chxs=0,676767,11.5,0.167,l,676767|1,676767,11.5,0,lt,676767&chxt=y,x&chs=293x225&cht=lc&chco=224499,FF0000&chds=0,100,-1.667,100&chd=t:'
    POST = '&chdl=Messages|Blocked+messages&chg=14.3,-1,1,1&chls=1,10,5|1,5,5&chm=B,3366CC99,0,0,0|B,FF00009B,1,0,0&chtt=(Blocked)+Messages'
    #width="293" height="225" alt="(Blocked) Messages"'

    for interval_length in (1,5,30,60):
        start_time = now - timedelta(minutes=6*interval_length)

        msgs = MessageStats.objects.filter(interval_start_time__gte=start_time,
                                         interval_length=interval_length)

        md_count = [ str(stats.message_count) for stats in msgs ]
        md_blocked_count = [ str(stats.blocked_messages_count) for stats in msgs ]

        # 10,20,30,50,40|2,13,23,37,27

        str_data = "|".join([
            ",".join(md_count),
            ",".join(md_blocked_count)
        ])

        img_messages.append(
            "".join([PRE, str_data, POST])
        )
        msg_stats.append(
            (interval_length, msgs))
        user_stats.append(
            (interval_length,
             UserStats.objects.filter(interval_start_time__gte=start_time,
                                      interval_length=interval_length)))

    top_words = nlargest(10, MessageHit.word_histogram().iteritems(), key=itemgetter(1))

    return render_response(request, "statistics.html", {
        "msg_interval_stats": msg_stats,
        "user_interval_stats": user_stats,
        "img_urls": img_messages,
        "dirty_list": [ (a, b, c) for ((a, b), c) in zip(msg_stats, img_messages) ],
        'top_words' : top_words,
    })

def record(request):
    if request.method == 'POST':
        print dir(request.POST)
        if request.POST:
            xml = ET.parse(request).getroot()
            msg = MessageHit._from_xml(xml)
            if msg is None:
                raise HttpResponse(status=450)

            msg.save()

            MessageStats.update_stats()
            UserStats.update_stats()

            return HttpResponse(status=201)

    return HttpResponse(status=400)