/**
 * @Description WS4IS.direct.Provider ExtJS 4.x; custom websocket data provider
 * @author  Tomislav Milkovic
 * @license LGPLv3 http://www.opensource.org/licenses/lgpl-3.0.html
 * @version 2.0, 08.04.2014.
 * @project_url http://code.google.com/p/extjs5250/
 */


/**
 * Support provider for WS4IS backend.  Always send in JSON format, even for Ext.Forms Direct calls. 
 */
Ext.define('WS4IS.direct.Provider', {        
    extend: 'Ext.direct.RemotingProvider',
    alias: 'direct.ws4isprovider',
    
    constructor : function(config){
        var me = this;
        me.namespaceName = (Ext.isString(config.namespace)) ? config.namespace : "";
        me.callParent(arguments);
    },
    
    createHandler: function(action, method) {
        var me = this, handler = null;
        if (!method.formHandler) {
            handler = function() {            	
            	var args = Array.prototype.slice.call(arguments, 0);
            	if(args[0] instanceof  HTMLFormElement){
            		args[0] = args[2].form.getValues();
            	}
            	me.configureRequest(action, method, args);
            };
        } else { //convert to JSON and send
            handler = function(form, callback, scope) {
            	var json = scope.form.getValues(),
            	callback = Ext.Function.bind(scope.onComplete, scope);                 
                me.configureRequest(action, method, [json,callback,scope] );            	
            };
        }

        handler.directCfg = {
            action: action,
            method: method,
            namespace : me.namespaceName 
        };
        return handler;
    },
    
    getCallData: function(transaction){
        return {
        	namespace : transaction.provider.namespaceName,
            action: transaction.action,
            method: transaction.method,            
            data: transaction.data,
            type: 'rpc',
            tid: transaction.id
        };
    }   
});