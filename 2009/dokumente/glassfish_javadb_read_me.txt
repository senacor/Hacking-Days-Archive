Datenbank starten
bin/asadmin start-database --dbhost localhost

Connection-Pool erzeugen
bin/asadmin --user admin create-jdbc-connection-pool --datasourceclassname=org.apache.derby.jdbc.ClientConnectionPoolDataSource --isconnectvalidatereq=true --validationmethod=meta-data --property user=hd09:password=hd09:databasename=hd09db:connectionattributes=\;create\\=true hd09cp

-> in der Admin-Konsole ist jetzt der Connection-Pool vorhanden und kann mit dem Button "ping" getestet werden
-> in glassfish/databases/derby.log sollten ebenfalls Einträge zu sehen sein

JDBC Resource erzeugen
bin/asadmin --user admin create-jdbc-resource --connectionpoolid=hd09cp jdbc/hd09


Mit SQL-Tool (z.B. Squirrel SQL Client) auf DB connecten
- URL : jdbc:derby://localhost:1527/hd09db
- user: hd09
- pass: hd09
