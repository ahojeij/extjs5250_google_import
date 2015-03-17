This is ExtJS component for accessing to IBM iSeries servers (AS/400) through web interface using ExtJS framework.

Project uses DWR, SpringFramework, Java and ExtJS 2.0 / 3.0

Original post for Ext 2.0 is here http://extjs.com/forum/showthread.php?t=58164

I have made some bug fixes and tested it for Ext 3.0.

Main idea is based on opensource projects at http://tn5250j.sourceforge.net/ and http://www.web5250.com/.

I have used idea from this projects to write my own java class (DWR5250.java) to handle connection to as/400.

This class is exposed through DWR framework and is used to send 5250 screen description in JSON format.

Keyboard emulation of 5250 is quite hard to do, so I have used Ext.ux.rTerm.KeyManager
from http://svn.xantus.org/ext-ux/lib/rterm/rterm.js as a base idea how to do it.

All java demo application can be found under downloads as also small screencast and screenshot.

Project is written in Eclipse and will work with Java 1.5 and up.

You can run Tomcat on any Java enabled machine, and if you have AS/400 with installed JAVA 1.5 you can run it on As/400 also.

I have made tests with Tomcat 5.5 and Tomcat 6

Use enter key as field exit, CTRL as confirm, function keys as standard 5250 keys from F1..F24, page up and page down to scroll multipaged screen.

  * INSTALL INSTRUCTIONS

> Put WAR file into Tomcat Server under it's webapps folder.
> After starting Tomcat go to http://localhost:8080/web5250.

<font color='red'>
NOTE: Don't forget to create subfolder <i>extjs3</i> under webapps of Tomcat server and unzip downloaded ExtJs library inside.<br>
</font>

> NOTE: To select connecting AS/400, edit file WebContent\WEB-INF\spring.xml

```
      <!-- Web5250DWR Proxy class -->
      <b:bean id="tn5250" class="ws4is.tn5250.Dwr5250">
      <b:constructor-arg value="10.10.10.1"/> -----> CHANGE TO YOUR AS00 SERVER
      <dwr:remote javascript="Tn5250Proxy">
      <dwr:include method="CreateSession" />
      <dwr:include method="DeleteSession" />
      <dwr:include method="processRequest" />
      </dwr:remote>
      </b:bean>
```