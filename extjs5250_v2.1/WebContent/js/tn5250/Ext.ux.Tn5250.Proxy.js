/*
 * @Description Ext.ux.Tn5250.Tn5250Proxy ExtJS 4.x; Communication handler , sigleton 
 * @author  Tomislav Milkovic
 * @license LGPLv3 http://www.opensource.org/licenses/lgpl-3.0.html
 * @version 2.0, 08.04.2014.
 * @project_url http://code.google.com/p/extjs5250/
 */

/*
	var tn5250API = {
			provider : '5250provider',
		    methods : {      
		      open    : 'hr.ws4is.Tn5250Controller.openSession',
		      close   : 'hr.ws4is.Tn5250Controller.closeSession',
		      request : 'hr.ws4is.Tn5250Controller.requestSession',
		      refresh : 'hr.ws4is.Tn5250Controller.refreshSession',
		      sessions: 'hr.ws4is.Tn5250Controller.listSessions',	
		      hosts   : 'hr.ws4is.HostsController.listDefinitions' 
		    }
	}; 
 } 
*/

(function(){

    var API = null;
    
    var buildApiCall = function (name){
    	return API.methods[name];
    };
    
    var openInNewTab = function (url) {
      var win=window.open(url, '_blank');
      win.focus();
    }
    
    var getDisplay = function (displayID){
    	var panels = Ext.ComponentQuery.query('tnpanel[displayID="'+ displayID +'"]')
		 if(panels.length==1){
			 return panels[0];
		 }
    }

    /**
     * General tn5250 connection proxy used all over the Ext.ux.Tn5250.* classes.
     * Provides remote API abstraction for TN5250. One must register standard Ext.provider.Remoting or it's child component
     * and then to register remoting provider to this proxy so ap will be able to communicate with the server.
     * 
     * 	
     * Here is an example of registerint WebSocket service remoting provider 
     * 
     *  	var ws_REMOTING_API = {"id" : "5250provider",
	 *	                           "url": "ws://localhost:8080/extjs5250/socket",
	 *				               "type":"ws4isWebSocket", 
	 *				               "namespace" : "hr.ws4is", 
	 *				               "actions":{   "Controller": [{"name":"reload5250Definitions","len":0},
	 *				                                                {"name":"list5250Definitions","len":0},
	 *				                                                {"name":"list5250Sessions","len":0},
	 *				                                                {"name":"open5250Session","len":1},
	 *				                                                {"name":"close5250Session","len":1},
	 *				                                                {"name":"closeSessions","len":0},
	 *				                                                {"name":"refresh5250Session","len":1},
	 *				                                                {"name":"request5250Session","len":2}
	 *				                                                ] 	
	 *										 }
	 *	                          };
	 *	    
	 *		Ext.direct.Manager.addProvider(ws_REMOTING_API);
	 *
	 * To register remoting provider to proxy, we must define registered controller and it's methods 
	 * to be attached to specific tn5250 request    
	 *  		
	 *		var tn5250API = {
	 *				provider : '5250provider',
	 *			    methods : {      
	 *              open    : 'hr.ws4is.Tn5250Controller.openSession',
	 *              close   : 'hr.ws4is.Tn5250Controller.closeSession',
	 *              request : 'hr.ws4is.Tn5250Controller.requestSession',
	 *              refresh : 'hr.ws4is.Tn5250Controller.refreshSession',
	 *              sessions: 'hr.ws4is.Tn5250Controller.listSessions',
	 *              hosts   : 'hr.ws4is.HostsController.listDefinitions' 	      
	 *			    }
	 *		}; 
	 *		Ext.ux.Tn5250.Proxy.RegisterAPI(tn5250API);
     */
    Ext.define('Ext.ux.Tn5250.Proxy', {
    	requires : ['Ext.direct.Manager'],
    	singleton : true,
    	
    	getProvider : function(){
		  	 return Ext.direct.Manager.getProvider(API.provider);
		},
		
		/**
		 * Registers active remoting provider 
		 */
		RegisterAPI : function(remotingApi) {
			API = remotingApi;
            var prov = Ext.direct.Manager.getProvider(API.provider);
            prov.addListener('data',function(prov,resp,act){
            	//debug
            	/*
            	window.resp = resp;
            	console.log(resp);
            	*/
                if(resp.displayID){
                	var panel = getDisplay(resp.displayID);
                    if(panel){
                    	panel.fireEvent('5250response',resp);
                     }                        		  
                } else if(resp.reportName){
                	var p = window.location.href.lastIndexOf('/');
                	var url =window.location.href.substr(0,p) + '/reports?r=' + resp.reportName; 
                	openInNewTab(url);
                }
            });

		},

		/**
		 * Uses to refresh current screen in server cache
		 * @aram {String} displayID
		 *  ID of current display, it is retrieved from server on connection open and registered on display panel as displayID property
		 * @aram {function} callback  
		 *  Standard Ext.remoting callback method 
		 */
		RefreshSession : function (displayID, cb){
			eval(buildApiCall('refresh'))(displayID, cb);
		},
        		
		/**
		 * Closes current tn 5250 session
		 * 
		 * @aram {String} displayID
		 *  ID of current display, it is retrieved from server on connection open and registered on display panel as displayID property
		 *  
		 * @aram {function} callback  
		 *  Standard Ext.remoting callback method
		 */
        CloseSession : function (displayID, cb){
         	eval(buildApiCall('close'))(displayID, cb);
        },


		/**
		 * Closes current tn 5250 session
		 * 
		 * @aram {String} hostName
		 *  virtual name of configured host at the server side. Name is used from server to find AS/400 server config.
		 *  
		 * @aram {function} callback  
		 *  Standard Ext.remoting callback method. Callback returns displayID
		 */
        CreateSession : function (hostName, cb){
        	eval(buildApiCall('open'))(hostName,cb);
		},

		
		/**
		 * Sends telnet request informtions to the server - it's for a key request processing
		 * 
		 * @aram {Array} fields
		 *  this is a Array of screen fields with it's data send to be populated on 5250 screen
		 *  
		 * @param {String/Object} 
		 *  name of a keyboard key pressed, if it is special key like Fn or Reset, SysReq, parameter is special key name used by underlying tn5250j lib
		 * 
		 * @aram {function} callback  
		 *  Standard Ext.remoting callback method.
		 */		
        processRequest: function(fields, request, cb){                          
		    eval(buildApiCall('request'))(request,fields, cb);
		},
				
		/**
		 * List of currently opened tn5250 sessions for curent web user/session
		 *  
		 * @aram {function} callback
		 *  Standard Ext.remoting callback method. 
		 *  Returns a list of 5250 session display id's
		 */		
		listSessions : function(cb){
			eval(buildApiCall('sessions'))(cb);
		},
				
		/**
		 * List of virtual AS/400 hosts names of currently configured hosts
		 *  
		 * @aram {function} callback
		 *  Standard Ext.remoting callback method. 
		 *  Returns a list of virtual names mapped to AS/400 host servers
		 */			
		listHosts : function(cb){
			eval(buildApiCall('hosts'))(cb);
		}
	
	});

})();