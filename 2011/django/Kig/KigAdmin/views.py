from pprint import pprint, pformat
from django.http import Http404, HttpResponse
from django.shortcuts import render_to_response, redirect
from django.core.context_processors import csrf
from django.views.decorators.csrf import csrf_protect
from django.contrib.auth.decorators import login_required
from django.template import RequestContext

from .rest.user_applications import get_pending_applications

import logging

logger = logging.getLogger("KigAdmin.views")

def _render_response(request, template, kwargs):
    return render_to_response(template, kwargs, context_instance=RequestContext(request))

def user_name(request):
    return request.user.username

@csrf_protect
@login_required
def home(request):
    """
    Holt eine Liste aller noch nicht freigeschalteter Nutzer per REST und stellt
    diese als Liste dar; jeder Nutzer kann freigeschaltet oder abgelehnt werden.
    """
    log = []
    applications = list(get_pending_applications(user_name(request)))
    user_list = [ application.user for application in applications ]
    request.session['applications'] = applications
    for user in user_list:
        log.append(unicode(user))

    return _render_response(request, 'applications.html', {'application_list': applications, 'log': log })

@csrf_protect
@login_required
def update(request):
    """
    """
    if request.method == 'POST':
        logger.debug(pformat(request.POST))
        applications = request.session['applications']

        appmap = dict( (application.uuid, application) for application in applications )

        for uuid, state in request.POST.iteritems():
            if not uuid.startswith('ID:'):
                continue
            uuid = uuid[3:]
            if uuid not in appmap:
                logger.error("unexpected application ID '%s' returned from form" % uuid)
                continue
            application = appmap[uuid]
            if(application is not None and state != 'postpone'):
                logger.info ("Application %s: %r -> %s", application.uuid, application.user, state)
                if state == 'accept':
                    application.accept(user_name(request))
                elif state == 'reject':
                    application.reject(user_name(request))

        return redirect('home')
    else:
        raise Http404
