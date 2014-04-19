<!DOCTYPE html>
<%@ page session="true" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=us-ascii" />
  <title>Telnet-5250</title>

        <link rel="stylesheet" href="js/extjs5/resources/ext-theme-gray-all.css" type="text/css" />
        <link id="tntheme" rel="stylesheet" href="css/as400.css" type="text/css" />

       <script type="text/javascript" src="js/extjs4/ext-all-debug.js"></script>
       <script type="text/javascript" src="js/ws4is/WS4IS.WebSocket.js"></script>
       <script type="text/javascript" src="js/ws4is/WS4IS.direct.Provider.js"></script>
       <script type="text/javascript" src="js/ws4is/WS4IS.direct.WebSocketProvider.js"></script>
  
       <script type="text/javascript" src="js/tn5250/Ext.ux.Tn5250.KeyHandler.js"></script>
       <script type="text/javascript" src="js/tn5250/Ext.ux.Tn5250.KeyManager.js"></script>
       <script type="text/javascript" src="js/tn5250/Ext.ux.Tn5250.Proxy.js"></script>
         
       <script type="text/javascript" src="js/tn5250/Selection.js"></script>
       <script type="text/javascript" src="js/tn5250/Ext.ux.Tn5250.ScreenElement.js"></script>
       <script type="text/javascript" src="js/tn5250/Ext.ux.Tn5250.Text.js"></script>
       <script type="text/javascript" src="js/tn5250/Ext.ux.Tn5250.Field.js"></script>
       <script type="text/javascript" src="js/tn5250/Ext.ux.Tn5250.StatusLine.js"></script>
       <script type="text/javascript" src="js/tn5250/Ext.ux.Tn5250.View.js"></script>
       <script type="text/javascript" src="js/tn5250/Ext.ux.Tn5250.Panel.js"></script>
       <script type="text/javascript" src="js/tn5250/Ext.ux.Tn5250.TabPanel.js"></script>

       <script type="text/javascript" src="js/desktop/Ext.ux.Tn5250.HostsMenu.js"></script>
       <script type="text/javascript" src="js/desktop/Ext.ux.Tn5250.Window.js"></script>
              
       <script type="text/javascript" src="js/init.js"></script>
       <script type="text/javascript" src="js/desktop/index.js"></script>

	   <script type="text/javascript">
	    function beep(){
	     var sound = document.getElementById('beep');	     
	     sound.load();
	     sound.play();
	     }
	   </script>

</head>

<body>
<pre>Ctrl+SHIFT = Reset
Ctrl = Return
Enter = Field Exit
F1..12 = PF1..12
F1..12 + SHIFT = PF12..24
</pre>
<audio src="sounds/beep.mp3" id="beep" style="visibility:hidden;" type="audio/mpeg" ></audio>
</body>
</html>
