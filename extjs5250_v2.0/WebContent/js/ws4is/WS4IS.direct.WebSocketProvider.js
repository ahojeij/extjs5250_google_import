/*
 * @Description ExtJS data provider for WebSocket protocol 
 * @author  Tomislav Milkovic
 * @license LGPLv3 http://www.opensource.org/licenses/lgpl-3.0.html
 * @version 2.0, 08.04.2014.
 * @project_url http://code.google.com/p/extjs5250/
 */

/**
 * ExtJS data provider for WebSocket protocol.  
 */
Ext.define('WS4IS.direct.WebSocketProvider', {
      extend: 'WS4IS.direct.Provider',
      alias : 'direct.ws4isWebSocketprovider',

      /**
       * @property {String} wsocket
       * This property contains a name of registered WebSocket provider. 
       * This is used to retrieve provider when sending data voer registered WebSocket
       * @private
       */
      wsocket: null,
      
      
      constructor : function(config){
          var me = this;
          
          if(Ext.getVersion().getMajor() == 4){
          	me.addEvents('echo','bye');
          }
          
          me.callParent(arguments);
      },      

      parseResponse: function(xhr){
               //console.log(xhr);
               if(!Ext.isEmpty(xhr.data)){
                   if(typeof xhr.data == 'object'){
                       return xhr.data;
                   }
                  var me = this, result =  Ext.decode(xhr.data);
                  if(Ext.isObject(result)){
                   if(result.type=='ws'){
                 	  if(result.cmd=='bye'){
                          var ws = WS4IS.WebSocket.getInstance(this.wsocket);
                          if(ws) ws.close();
                          me.fireEvent('bye', me, result);
                          return null;
                 	  } else if(result.cmd=='err'){
                 		 me.fireEvent('exception', me, result);
                 		return null;
                 	  } else if(result.cmd=='echo'){
                 		 me.fireEvent('echo', me, result);
                 		return null;
                 	  } else{
                          result = result.data;
              	      }                	   
                   }
                   
                   if(!Ext.isString(result.type)){
                       result.type = 'rpc';
                   }
                  }
                  return result;
               }
               return null;
            },

     connect : function() {
         var me = this;
         var ws = WS4IS.WebSocket.getInstance(me.wsocket);
         if(!ws){
           var cfg = {
               url:me.url, 
               type:'ws4is',
               listeners : {
                 connect : function(evt){
                	 		 me.initAPI();
                	 		 me.fireEvent('connect', me, evt);
                	 		},
                 disconnect : function(evt){
                	 		 me.fireEvent('disconnect', me, evt);
                	 		  },
                 data : function(evt){
                	 		 me.onData(null,true, evt);
                	 	},
                 error : function(evt){
                	 		me.fireEvent('exception', me, evt);
                	 	}
               }
           };
           ws = Ext.create('WS4IS.WebSocket', cfg);
           me.wsocket = ws.id;
           ws.open();
         } else {
           if(ws.isConnected()){
             me.fireEvent('connect');
           } else {
             ws.initialize();
           }
         }
     },
     
     /**
      * Method to verify if connection is open and active
      * @private
      */
     checkSocket : function(){
    	 var me = this, ws = WS4IS.WebSocket.getInstance(me.wsocket); 
         if (!ws) {
        	 Ext.Error.raise('Socket not initialized!');
          };    	 
          if (!ws.isConnected()) {
         	 Ext.Error.raise('Socket not connected!');
           }; 
           return ws;
     },

     /**
      * Method to check server side socket status. WebSocket simple echo. 
      */
     echo : function(data){
    	 var me = this, ws = me.checkSocket();
         var data = {type :'ws' , cmd:'echo'};          
         ws.send(Ext.encode(data));     	 
     },
     
     
     sendRequest : function(data){
    	 var me = this, ws = me.checkSocket();

          var request = {
                        url: me.url,
                        callback: me.onData,
                        scope: me,
                        transaction: data,
                        timeout: me.timeout
                    },           
          enableUrlEncode = me.enableUrlEncode,
          params;
          
          var callData = me._prepareData(data);
          
          if (enableUrlEncode) {
            params = {};
            params[Ext.isString(enableUrlEncode) ? enableUrlEncode : 'data'] = Ext.encode(callData);
            request.params = params;
          } else {
            request.jsonData = callData;
          }

          var data = {type :'ws' , cmd:'data' , 'data': request.jsonData};          
          ws.send(Ext.encode(data));          
     },

     _prepareData : function(data){
    	 var me = this;
    	 var _data = [];
    	 if(Ext.isArray(data)){
           Ext.Array.each(data, function(el, index, els) {
             _data.push(me.getCallData(el));
	   });
    	 } else {
    	   _data.push(me.getCallData(data));
    	 }
	 return _data;
     }, 


     configureFormRequest : function(controller, method, form, callback, scope){
          var me = this,
              transaction = Ext.create('Ext.direct.Transaction', {
                  provider: me,
                  controller: controller,
                  method: method.name,
                  args: [form, callback, scope],
                  callback: scope && Ext.isFunction(callback) ? Ext.Function.bind(callback, scope) : callback,
                  isForm: true
              }),
              isUpload,
              params;

          if (me.fireEvent('beforecall', me, transaction, method) !== false) {
              Ext.direct.Manager.addTransaction(transaction);
              isUpload = String(form.getAttribute("enctype")).toLowerCase() == 'multipart/form-data';

              params = {
                  extTID: transaction.id,
                  extController: transaction.controller,
                  extMethod: transaction.method,
                  extType: 'rpc',
                  extUpload: String(isUpload)
              };

              // change made from typeof callback check to callback.params
              // to support addl param passing in DirectSubmit EAC 6/2
              Ext.apply(transaction, {
                  form: Ext.getDom(form),
                  isUpload: isUpload,
                  params: callback && Ext.isObject(callback.params) ? Ext.apply(params, callback.params) : params
              });
              me.fireEvent('call', me, transaction, method);
              me.sendFormRequest(transaction);
          }
     },

     processForm: function(t){
              var me = this, 
              ws = WS4IS.WebSocket.getInstance(me.wsocket),
              o = {
                    url: me.url,
                    params: t.params,
                    callback: me.onData,
                    scope: me,
                    form: t.form,
                    isUpload: t.isUpload,
                    ts: t
                };
              console.log('Submitting form');

              var form = Ext.getDom(o.form);
              var serForm = Ext.lib.Ajax.serializeForm(form);
              var fields = serForm.split("&");
              var formFields = {};

              //@todo review this code
              for(var key = 0, l = fields.length; key < l; key++) {
                 var field = fields[key];
                 var fld    = field.split("=");
                 var fName  = fld[0];
                 var fValue = fld[1];
                 formFields[fName] = fValue;
              }
              formData = Ext.apply(o.params, formFields);
              reqData = {
                    "action" : o.ts.controller,
                    "method" : o.ts.method,
                    "tid" : o.ts.id,
                    "type": o.params.extType,
                    "data": formData

              };
              console.log(reqData, o);
              var data = {type :'ws' , cmd:'data' , 'data': reqData};
              ws.send(Ext.encode(data));
           }

});

