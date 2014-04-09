/**
 * @Description Ext.ux.Tn5250.Panel; main 5250 panel. Contains view and satus bar; attached key handler 
 * @author  Tomislav Milkovic
 * @license LGPLv3 http://www.opensource.org/licenses/lgpl-3.0.html
 * @version 2.0, 08.04.2014.
 * @project_url http://code.google.com/p/extjs5250/
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
			if(me.menu.items.length>0 || me.populating) return;
			me.populating = true;
			Ext.ux.Tn5250.Proxy.listHosts({
				success : function(res){
								var pnl = Ext.ComponentQuery.query('tntabpanel')[0];
								var itms = [];
								Ext.Array.each(res.data, function(host, index, sessions) {
									itms.push({ text : host, plain : true, 
										        listeners : { click : function(){pnl.createSession(this.text); } }
									          });
								});
								me.menu.add(itms);								
						  },
				failure : function(){
					        me.populating = false;
				            Ext.MessageBox.alert('Error','Can\'t load hosts list' );
						  }
			});			
		};
		me.listeners = {click : init};
		me.menu = new Ext.menu.Menu({items: []});
		
		me.callParent();
    }
});


