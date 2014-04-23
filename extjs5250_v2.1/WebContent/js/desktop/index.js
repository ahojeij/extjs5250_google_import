 Ext.onReady(function(){

	 Ext.tip.QuickTipManager.init();
	 
	 Ext.define('WS4IS.model.Hosts', {
		    extend: 'Ext.data.Model',
		    fields: ['id', 'name']
		});

		Ext.define('WS4IS.data.HostsStore', {
		        extend: 'Ext.data.Store',
		        model: 'WS4IS.model.Hosts',
		        alias :'store.hoststore',
		        autoLoad : false
		    });


	var store = Ext.create('WS4IS.data.HostsStore',{
		        storeId : 'tnhostsstore',
		        proxy: {
		            type: 'direct',
		            directFn: 'hr.ws4is.HostsController.listDefinitions',
		            reader: {
		                type: 'json',
		                root: 'data'
		            }
		        }
		});
	
		
   Ext.create('Ext.ux.Tn5250.Window', {
          title: 'Telnet 5250 - JsonRPC Services Framework - WebSocket',
          maximizable : true,
          width: 1150,
          height: 800,
          name : 'main',
          store : 'tnhostsstore'
   }).show();
   
 });

