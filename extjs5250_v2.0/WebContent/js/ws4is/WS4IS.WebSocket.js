/**
 * @Description WS4IS.WebSocket ExtJS 4.x; custom websocket data provider
 * @author  Tomislav Milkovic
 * @license LGPLv3 http://www.opensource.org/licenses/lgpl-3.0.html
 * @version 2.0, 08.04.2014.
 * @project_url http://code.google.com/p/extjs5250/
 */


Ext.define('WS4IS.WebSocket', {
	config : {
		url : '',
		type : 'ws4is'
	},	
    mixins: {
        observable: 'Ext.util.Observable'
    },

    statics : {
    	$instances : {},
    	register : function(ws){
            WS4IS.WebSocket.$instances[ws.id] = ws;    		
    	},
    	unregister : function(ws){
    		WS4IS.WebSocket.$instances[ws.id]= null;
    		delete WS4IS.WebSocket.$instances[ws.id];
    	},    	
    	getInstance : function(name){
    		return WS4IS.WebSocket.$instances[name];
    	}
    },
    /*
    * config = {url , type }
    */
    constructor : function(config){
        var me = this;
        me.mixins.observable.constructor.call(me, config);
        me.initConfig(config);
        me.callParent(arguments);
        Ext.applyIf(me, {
            id: Ext.id(null, 'wsocket-')
        });        
        WS4IS.WebSocket.register(me);
        if(Ext.getVersion().getMajor() == 4){
          me.addEvents(
            'connect',
            'disconnect',
            'exception',
            'data'
          );
        };
    },
    
    open : function(){
        var me = this;
   	    if(me.isConnected()) return;
        try {
            me.wsocket = new WebSocket(me.getUrl(),me.getType());
        } catch(ex) {
            console.log(ex);
            me._onSockError();
            throw ex;
        }
        
        if(me.wsocket == null) return;
        
        me.wsocket.onopen = Ext.Function.bind(me._onSockOpen, me);
        me.wsocket.onmessage = Ext.Function.bind(me._onSockMessage, me);
        me.wsocket.onclose = Ext.Function.bind(me._onSockClose, me);
        me.wsocket.onerror = Ext.Function.bind(me._onSockError, me);
     },

     isConnected : function(){
        var me = this;
        return (me.wsocket && me.wsocket.readyState === 1);
     },
     
     send : function(data){
    	 var me = this;
    	 if(me.isConnected()){
    		 me.wsocket.send(data);
    	 }    	 
     },

     close : function(data){
    	 var me = this;
    	 if(me.isConnected()){
    		 me.wsocket.close();
    	 }
     },
     
     destroy: function() {
    	 var me = this;
         me.destroy = Ext.emptyFn;
         me.close();
         WS4IS.WebSocket.unregister(me);
     },
     
     _onSockOpen: function(evt) {
    	 var me = this, _evt=evt;
    	 var recursion = function(){
             if (me.wsocket.readyState === 1) {
            	  WS4IS.WebSocket.register(me);
                  me.fireEvent('connect', _evt);
                 return;
             } else {
                 me._onSockOpen(_evt);
             }
    	 };
    	 Ext.Function.defer(recursion,50,me);
     },

     _onSockMessage: function(obj) {
         var me = this;
         me.fireEvent('data', obj);
     },

     _onSockClose: function(obj) {
    	 var me = this;
         me.wsocket.onopen = null;
         me.wsocket.onmessage = null;
         me.wsocket.onclose = null;
         me.wsocket.onerror = null;
         me.wsocket = null;
         me.fireEvent('disconnect', obj);
      },

     _onSockError: function(obj) {
    	 var me = this;
    	 me.fireEvent('exception', obj);
      },

});

// Ext.create('WS4IS.WebSocket',{url :'ws://localhost:8080/ws/socket' , type : 'ws4isWebSocket'})
