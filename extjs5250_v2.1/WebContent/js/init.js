/*********************************************************
  Initializer for websocket remote calls
**********************************************************/
Ext.onReady(function(){

	var tn5250API = {
			provider : '5250provider',
			namespace : 'hr.ws4is',
		    action : 'Controller5250',
		    methods : {      
		      open    : 'open5250Session',
		      close   : 'close5250Session',
		      request : 'request5250Session',
		      refresh : 'refresh5250Session',
		      sessions : 'list5250Sessions',
		      hosts    : 'list5250Definitions'		      
		    }
	}; 
		 
	var ws_REMOTING_API = {"id" : "5250provider",
                           "url": "ws://localhost:8080/extjs5250/socket",
			               "type":"ws4isWebSocket", 
			               "namespace" : "hr.ws4is", 
			               "actions":{   "Controller5250": [{"name":"reload5250Definitions","len":0},
			                                                {"name":"list5250Definitions","len":0},
			                                                {"name":"list5250Sessions","len":0},
			                                                {"name":"open5250Session","len":1},
			                                                {"name":"close5250Session","len":1},
			                                                {"name":"closeSessions","len":0},
			                                                {"name":"refresh5250Session","len":1},
			                                                {"name":"request5250Session","len":2}
			                                                ] 	
									 }
                          };
    
	Ext.direct.Manager.addProvider(ws_REMOTING_API);
	Ext.ux.Tn5250.Proxy.RegisterAPI(tn5250API);
	
});
