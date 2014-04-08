/**
 * @Description Ext.ux.Tn5250.Tn5250Proxy ExtJS 4.x; Communication handler , sigleton 
 * @author  Tomislav Milkovic
 * @license LGPLv3 http://www.opensource.org/licenses/lgpl-3.0.html
 * @version 2.0, 08.04.2014.
 * @project_url http://code.google.com/p/extjs5250/
 */

/*
 remotingAPI : {
    provider : '5250provider',
    namespace : 'hr.ws4is',
    action : 'Controller',
    methods : {      
      open    : 'open5250Session',
      close   : 'close5250Session',
      request : 'request5250Session',
      refresh : 'refresh5250Session',
      sessions : '',
      hosts : ''
    }
 } 
*/

(function(){

    var API = null;
    
    var buildApiCall = function (name){
    	return API.namespace + '.' + API.action + '.' + API.methods[name];
    };
    
    
    var getDisplay = function (devName){
    	var panels = Ext.ComponentQuery.query('tnpanel[devName="'+ devName +'"]')
		 if(panels.length==1){
			 return panels[0];
		 }
    }

    
    Ext.define('Ext.ux.Tn5250.Proxy', {
    	requires : ['Ext.direct.Manager'],
    	singleton : true,
    	
    	getProvider : function(){
		  	 return Ext.direct.Manager.getProvider(API.provider);
		},
		
		RegisterAPI : function(remotingApi) {
			API = remotingApi;
            var prov = Ext.direct.Manager.getProvider(API.provider);
            prov.addListener('data',function(prov,resp,act){
            	console.log(resp);
                if(resp.devName){
                	var panel = getDisplay(resp.devName);
                    if(panel){
                    	panel.fireEvent('5250response',resp);
                     }                        		  
                }
            });

		},

		RefreshSession : function (devName, cb){
			eval(buildApiCall('refresh'))(devName, cb);
		},
        		
        CloseSession : function (devName, cb){
         	eval(buildApiCall('close'))(devName, cb);
        },


        CreateSession : function (hostName, cb){
        	eval(buildApiCall('open'))(hostName,cb);
		},

        processRequest: function(fields, request, cb){                          
		    eval(buildApiCall('request'))(request,fields, cb);
		},
				
		listSessions : function(cb){
			eval(buildApiCall('sessions'))(cb);
		},
				
		listHosts : function(cb){
			eval(buildApiCall('hosts'))(cb);
		}
	
	});

})();