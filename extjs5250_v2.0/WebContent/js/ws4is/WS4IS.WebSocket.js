/*
 * @Description ExtJS wrapper for WebSocket object adding Observable linked to WebSocket events
 * @author  Tomislav Milkovic
 * @license LGPLv3 http://www.opensource.org/licenses/lgpl-3.0.html
 * @version 2.0, 08.04.2014.
 * @project_url http://code.google.com/p/extjs5250/
 * @example
 *  Ext.create('WS4IS.WebSocket',{url :'ws://localhost:8080/ws/socket' , type : 'ws4isWebSocket'})  
 */


//fix for Mozilla WebSocket
if(typeof MozWebSocket !== 'undefined' && typeof WebSocket === 'undefined'){WebSocket = MozWebSocket;}


/**
 * ExtJS wrapper for WebSocket object adding Observable linked to WebSocket events
 */
Ext.define('WS4IS.WebSocket', {

	/**
	 * @cfg {object} parametres of WebSocket conenction
	 * @param {String} [config.url=''] url address of remote socket
	 * @return {String} [config.url='ws4is'] subprotocol name 
	 */	
	config : {
		url : '',
		type : 'ws4is'
	},	
	
    mixins: {
        observable: 'Ext.util.Observable'
    },

    statics : {
    	$instances : {},
    	
    	/**
    	* Register WS4IS.WebSocket object to instance list
    	* @param {object} WS4IS.WebSocket
    	* @static
    	*/    	
    	register : function(ws){
            WS4IS.WebSocket.$instances[ws.id] = ws;    		
    	},
    	
    	/**
    	* Unregister WS4IS.WebSocket object from instance list
    	* @param {object} WS4IS.WebSocket
    	* @static
    	*/      	
    	unregister : function(ws){
    		WS4IS.WebSocket.$instances[ws.id]= null;
    		delete WS4IS.WebSocket.$instances[ws.id];
    	},
    	
    	/**
    	* Retrieves WS4IS.WebSocket object from instance list
    	* @param {String} name of WS4IS.WebSocket instance
    	* @static
    	*/      	
    	getInstance : function(name){
    		return WS4IS.WebSocket.$instances[name];
    	}
    },
    
    
   /**
    * Create WS4IS.WebSocket object
    * @param {object} {url , type }
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
    
    /**
     * Opens WebSocket connection and bind events to internal methods. 
     * Throws an error if already opened.
     */    
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

     /**
      * Checks for connection status of WebSocket 
      * @return {Boolean} status
      */
     isConnected : function(){
        var me = this;
        return (me.wsocket && me.wsocket.readyState === 1);
     },
     
     /**
      * Sends data over WebSocket 
      * @param {object} json object
      */     
     send : function(data){
    	 var me = this;
    	 if(me.isConnected()){
    		 me.wsocket.send(data);
    	 }    	 
     },

     /**
      * Closes WebSocket connection 
      */       
     close : function(){
    	 var me = this;
    	 if(me.isConnected()){
    		 me.wsocket.close();
    	 }
     },

     /**
      * Destroys WS4IS.WebSocket object 
      */              
     destroy: function() {
    	 var me = this;
         me.destroy = Ext.emptyFn;
         me.close();
         WS4IS.WebSocket.unregister(me);
     },
     
     
     /**
      * Method executed from WebSocket callback on connection open
      * @private
      */
     _onSockOpen: function(evt) {
    	 var me = this, _evt=evt;
    	 var recursion = function(){
             if (me.wsocket.readyState === 1) {
                  me.fireEvent('connect', _evt);
                 return;
             } else {
                 me._onSockOpen(_evt);
             }
    	 };
    	 Ext.Function.defer(recursion,50,me);
     },

     /**
      * Method executed from WebSocket callback when message arrives
      * @private
      */     
     _onSockMessage: function(obj) {
         var me = this;
         me.fireEvent('data', obj);
     },

     /**
      * Method executed from WebSocket callback on connection close
      * @private
      */     
     _onSockClose: function(obj) {
    	 var me = this;
         me.wsocket.onopen = null;
         me.wsocket.onmessage = null;
         me.wsocket.onclose = null;
         me.wsocket.onerror = null;
         me.wsocket = null;
         me.fireEvent('disconnect', obj);
      },

      /**
       * Method executed from WebSocket callback when connection error ocures
       * @private
       */      
     _onSockError: function(obj) {
    	 var me = this;
    	 me.fireEvent('exception', obj);
      },

});

