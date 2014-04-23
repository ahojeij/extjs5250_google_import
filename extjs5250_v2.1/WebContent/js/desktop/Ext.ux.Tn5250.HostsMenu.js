/*
 * @Description Ext.ux.Tn5250.Panel; main 5250 panel. Contains view and satus bar; attached key handler 
 * @author  Tomislav Milkovic
 * @license LGPLv3 http://www.opensource.org/licenses/lgpl-3.0.html
 * @version 2.0, 08.04.2014.
 * @project_url http://code.google.com/p/extjs5250/
 */


/**
 * Dynamic menu, shows list of available hosts to which user can connect
 */
Ext.define('Ext.ux.Tn5250.HostsMenu', {
	extend : 'Ext.button.Button',
	
	requires : ['Ext.button.Button',
	            'Ext.menu.Item',
	            'Ext.ux.Tn5250.Proxy',
	           ],
	            
    alias : 'widget.tnmenu',
    autoDestroy : true,      
     
    constructor	: function(config){
    	var me = this;		
    	me.callParent(arguments);
    },
    
    initComponent  : function() {
		var me = this;
		
		var init = function (item,evt,opt){
			var me = item;
			if(me.populating) return;			
			me.populating = true;
			me.menu.removeAll(true);
			var pnl = Ext.ComponentQuery.query('tntabpanel')[0];
			var store = Ext.StoreManager.get(me.store);
			var itms = store.getRange().map(function(r){
					return { text : r.get('name'), 
					    plain : true, 
			            listeners : { click : function(){pnl.createSession(this.text); } }
		          };								 
				})								
			me.menu.add(itms);		
			me.menu.showBy(me);
			me.populating = false;
					
		};
		me.listeners = {click : init};
		me.menu = new Ext.menu.Menu();		
		me.callParent();

    }
});


