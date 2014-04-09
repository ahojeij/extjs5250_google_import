/**
 * @Description Ext.ux.Tn5250.TabPanel; main tab for 5250 panels.  
 * @author  Tomislav Milkovic
 * @license LGPLv3 http://www.opensource.org/licenses/lgpl-3.0.html
 * @version 2.0, 08.04.2014.
 * @project_url http://code.google.com/p/extjs5250/
 */

Ext.define('Ext.ux.Tn5250.TabPanel', {
	extend : 'Ext.tab.Panel',
    requires : ['Ext.tab.Panel', 
                'Ext.ux.Tn5250.Panel',
                'Ext.ux.Tn5250.Proxy'],
    
    alias : 'widget.tntabpanel',
    autoDestroy : true,
    
    listeners : {
              tabchange : function(tabPanel, newCard, oldCard, eOpts){
    		 	    newCard.fireEvent('tabchange');
    	 	          },
	         remove : function(t,c,o){
    			    Ext.ux.Tn5250.Proxy.CloseSession(c.displayID);
	 		  }
    },
    autoReload : true,

    initComponent  : function() {
    	var me = this;
    	
    	me.callParent();
    	if(me.autoReload){
    	 var provider = Ext.ux.Tn5250.Proxy.getProvider(); 
    	 if(!provider.isConnected()){
    	   provider.on('connect', me._reload, me);
    	 }
    	}
    },
        
    _reload : function(){
      var me = this;
      Ext.ux.Tn5250.Proxy.listSessions({success : function(res){
			Ext.Array.each(res.data, function(session, index, sessions) {
			    var pnl = me.createPanel(session);
			    Ext.ux.Tn5250.Proxy.RefreshSession(session,{success : function(res){
			      pnl.fireEvent('5250response',res);   	
			    }});    
			});      
      }});
    },
    
   createPanel: function(dev){
       var me = this,
       cfg = {title : dev, displayID : dev, closable : true, active : true, autodestroy :true};
       var pnl = Ext.create('Ext.ux.Tn5250.Panel', cfg);
       me.add(pnl);
       me.setActiveTab(pnl);
       return pnl;
  },
  
  createSession: function(name){
      var me = this;
      Ext.ux.Tn5250.Proxy.CreateSession(name, {success : function(res){
      	me.createPanel(res.displayID);
      }});
  }
      
});
  