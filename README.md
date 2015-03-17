Latest version is 2.1 Release, April, 2014.

This is ExtJS component for accessing to IBM iSeries servers (AS/400) through web interface using ExtJS framework.

Project uses ExtJS 4.x / 5.x (Sencha Touch version is comming) for front end and Java EE 1.7, WebSockets, CDI, tn5250j for backend.

Under downloads section one can find small screencast and screenshot.

Many bug fixes and optimizations are made, complete code refactoring. At the server side, websockets are used so now communication is bidirectional which brings automatic web screen updates from AS/400 server. Also, some server side threading problems are solved.

Project is written in Eclipse and will work with Java 1.7 and up. Test's are made with JBoss 8 - Wildfly.

Use enter key as field exit, CTRL as confirm, function keys as standard 5250 keys from F1..F24, page up and page down to scroll multipaged screen, CTRL+R for screen refresh.

Install instructions are here https://github.com/tommys-place/extjs5250/blob/master/extjs5250_v2.1/Instruction.txt

New download location moved to Google Drive (war file is built with Java 1.8 and tested on Jboss Wildfly 8)
https://drive.google.com/folderview?id=0B8sAkq5f-1ymS1RLRVNsa1AtSnM&usp=sharing

NOTE: Don't forget to add ExtJS libs to extjs5250_v2.1/WebContent/js/extjs4 folder.
