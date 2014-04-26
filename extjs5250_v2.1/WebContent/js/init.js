/*********************************************************
  Initializer for websocket remote calls
**********************************************************/
Ext.onReady(function(){

	
	var ws_REMOTING_API = {"id" : "5250provider",
                           "url": "ws://localhost:8080/extjs5250/socket",
			               "type":"ws4isWebSocket", 
			               "namespace" : "hr.ws4is", 
			               "actions":{   "Tn5250Controller": [{"name":"reloadDefinitions","len":0},
			                                                {"name":"listDefinitions","len":0},
			                                                {"name":"listSessions","len":0},
			                                                {"name":"openSession","len":1},
			                                                {"name":"closeSession","len":1},
			                                                {"name":"closeSessions","len":0},
			                                                {"name":"refreshSession","len":1},
			                                                {"name":"requestSession","len":2}
			                                                ],
			                                                
			                              "Tn3812Controller": [
			                                                   {"name":"openSession","len":2},  
			                                                   {"name":"closeSession","len":1},
			                                                   {"name":"refreshSession","len":1},
			                                                   {"name":"listSessions","len":0},
			                                                   {"name":"closeSessions","len":0}
			                                                  ],
			                             "HostsController"  : [
			                                                   {"name":"listDefinitions","len":0},
			                                                   {"name":"reloadDefinitions","len":0}
			                                                   ]
									 }
                          };
    
	Ext.direct.Manager.addProvider(ws_REMOTING_API);
	
	
	var tn5250API = {
			provider : '5250provider',
		    methods : {      
		      open    : 'hr.ws4is.Tn5250Controller.openSession',
		      close   : 'hr.ws4is.Tn5250Controller.closeSession',
		      request : 'hr.ws4is.Tn5250Controller.requestSession',
		      refresh : 'hr.ws4is.Tn5250Controller.refreshSession',
		      sessions: 'hr.ws4is.Tn5250Controller.listSessions',	
		      hosts   : 'hr.ws4is.HostsController.listDefinitions' ,
		      printerSessions: 'hr.ws4is.Tn3812Controller.listSessions',
		      printerOpen: 'hr.ws4is.Tn3812Controller.openSession',
		      printerClose: 'hr.ws4is.Tn3812Controller.closeSession'
		    }
	}; 	
	Ext.ux.Tn5250.Proxy.RegisterAPI(tn5250API);
	
});
