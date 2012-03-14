REST

dummyapp
Services und Objektmodell für die Beispielanwendungen jerseytest und springmvctest. Das Objektmodell besitzt 
rudimentäre JAXB-Annotations.

jerseytest
Zeigt eine RESTful Anwendung basierend auf dem Jersey-Framework. Die Resource /customers/changes wird in text/html 
und bei als application/xml+atom geliefert. Die Resource /customer/{id} liefert text/html und application/json. Zur
Erzeugung von atom wird Rome verwendet.
Außerdem ist eine JSP enthalten, welche über AJAX (jquery) den JSON-Service aufruft und die übertragenen Daten in der 
HTML-Seite darstellt (ohne Page Reload).
Bauen (mvn package) und im Server installieren.

springmvctest
Eine RESTful Anwendung basierend auf Spring-Web bzw. Spring-MVC. Verschiedene Content-Types werden mit Hilfe des
ContentNegotiatingViewResolvers zurückgegeben. 
(OSGi) Ferner wird über Spring-OSGi ein OSGI-Service als Spring-Bean angebunden. Hieruzu werden der "spring-glassfish-hook"
und die Greeter-Services verwendet. Die Anbindung des OSGi-Service via Spring sorgt dafür, dass alle OSGi Spezifika
transparent bleiben. Die Services können zur Laufzeit ausgetausch und aktualisiert werden, ohne dass die Anwendung
darunter leidet.

GoogleTwitterAmazon
Exemplarische Anbindung von Flickr, Google-API und Twitter. Zur Erzeugung der benötigten Requests wird rome verwendet.


OSGi

spring-dm-extender
Enthält die Bundles, mit denen der Spring-DM-Extender in einer beliebigen OSGi-Runtime gestartet werden kann. Das 
Bundle org.springframework.osgi.extender-1.2.0 muss gestartet werden - am besten zusammen mit der Runtime (Glassfish: 
<domain>/autostart/bundles). Die Bundles unter "dependency" müssen nur aufgelöst werden können (Glassfish: 
glassfish/modules - hier kann dann auch ein Verzeichnis angelegt werden, bspw. "spring"). 

GreeterInterface/GreeterImplEnglish/GreeterImplGerman
"Hello World" OSGi-Bundles. Aufgeteilt in ein Interface-Bundle und zwei unterschiedliche Implementierungen. Wird
von der Anwendung "springmvctest" verwendet. Zur Laufzeit wird automatisch das Bundle mit der höheren Priorität 
gewählt. Wird dieses entfernt, so erfolgt sofort (und transparent) ein Fallback auf das nächste Bundle, welches 
das Interface implemntiert. Die Bundles sind Spring-DM-Bundles - also vorher den Extender installieren.
