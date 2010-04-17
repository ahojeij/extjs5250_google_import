/**
 * @Description Ext.ux.Tn5250 ExtJS 2.x and 3.x; Telnet 5250 for access to IBM iSeries (AS/400) servers (java server side using DWR library)
 * @author  Tomislav Milkovic
 * @license LGPLv3 http://www.opensource.org/licenses/lgpl-3.0.html
 * @version 1.1, 01.09.2009.
 * @project_url http://code.google.com/p/extjs5250/
 */

	
Tn5250Proxy = function() {	
	
	var callAjax = function (options){
	        Ext.Ajax.request( {
	            url : '@TODO',
	            params : '',
	            method : 'GET',
	            callback : options.callback,
	            scope : options.scope
	    });
	};
	
	var inner = {
		DeleteSession : function (devName){callAjax(devName);},
		
		//Tn5250Proxy.CreateSession(render.devName,{exceptionHandler : this.exceptionHandler,callback : render.initFirstScreen, scope:render});
		CreateSession : function (devName, options){callAjax(devName,options);},
		
		//Tn5250Proxy.processRequest(flds, req, { exceptionHandler : this.exceptionHandler, callback:this.render5250, scope:this});
		processRequest: function(fields, request, options){callAjax(devName,options);}
	};
	return inner;
}();

