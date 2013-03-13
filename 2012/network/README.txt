Dokumentation der Basisschnittstelle

http://hd12.network.cloudfoundry.com/rest/person/{username} unterstützt PUT, DELTE und GET

Datenformat (username muss übereinstimmen):
{"firstname":"Oleg","lastname":"Galimov","username":"ogalimov"}


http://hd12.network.cloudfoundry.com/rest/person/{username}/followers unterstützt GET
http://hd12.network.cloudfoundry.com/rest/person/{username}/follows unterstützt GET

Datenformat:
{["ogalimov", "spahlm"]}

http://hd12.network.cloudfoundry.com/rest/person/{username}/peers/{peerUsername} unterstützt PUT(leer), DELETE

Liste der Benutzer:

GET auf http://hd12.network.cloudfoundry.com/rest/person


Benutersuche nach Nachnamen:

GET auf http://hd12.network.cloudfoundry.com/rest/person/_search?lastname.startswith=Wi

liefert alle Benutzer, deren Nachnamen mit Wi starten.



Beschreibende Liste der Verfolgten einer Person inklusive Anzahl Verfolger (Ausgangsusers ausgeschlossen) und einem iconLink:

http://hd12.network.cloudfoundry.com/rest/person/{username}/peers unterstützt GET

[
    {
        "username": "rwinzinger",
        "iconLink": "http://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Gnome-stock_person.svg/380px-Gnome-stock_person.svg.png",
        "numFollowers": 1
    },
    {
        "username": "ngeorgiev",
        "iconLink": null,
        "numFollowers": 0
    },
    {
        "username": "akeefer",
        "iconLink": "http://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Gnome-stock_person.svg/380px-Gnome-stock_person.svg.png",
        "numFollowers": 2
    },
    {
        "username": "hstamminger",
        "iconLink": "http://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Gnome-stock_person.svg/380px-Gnome-stock_person.svg.png",
        "numFollowers": 0
    },
    {
        "username": "ogalimov",
        "iconLink": "http://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Gnome-stock_person.svg/380px-Gnome-stock_person.svg.png",
        "numFollowers": 0
    }
]

Visualisierung kkann unter folgendem Link aufgerufen werden:

GET auf http://localhost:8080/rest/person/{username}/_visualisation?depth={depth}