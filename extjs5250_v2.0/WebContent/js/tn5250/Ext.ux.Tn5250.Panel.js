/**
 * @Description Ext.ux.Tn5250.Panel; main 5250 panel. Contains view and satus bar; attached key handler 
 * @author  Tomislav Milkovic
 * @license LGPLv3 http://www.opensource.org/licenses/lgpl-3.0.html
 * @version 2.0, 08.04.2014.
 * @project_url http://code.google.com/p/extjs5250/
 */

Ext.define('Ext.ux.Tn5250.Panel', {
	extend : 'Ext.container.Container',
	requires : ['Ext.container.Container',
	            'Ext.ux.Tn5250.Proxy',
	            'Ext.ux.Tn5250.KeyManager',
	            'Ext.ux.Tn5250.KeyHandler',
	            'Ext.ux.Tn5250.View',
	            'Ext.ux.Tn5250.StatusLine'],
	            
    alias : 'widget.tnpanel',
    autoDestroy : true,       
    mixins : {
    	keyhandler : 'Ext.ux.Tn5250.KeyHandler'
    },
     
    constructor	: function(config){
    	var me = this;		
    	me.callParent(arguments);
    	me.mixins.keyhandler.constructor.call(me, config);
    },
    
    initComponent  : function() {
		var me = this;
		me.layout = {
	          type: 'vbox',
	          align : 'stretch',
	          pack  : 'start'
	        };
		
	        me.items = [{xtype: 'tnview', devName : me.devName},
	                    {xtype:'tnstatusline'}
	                   ];
		me.callParent();
		me.view = this.down('tnview');
		me.statusBar = this.down('tnstatusline');
		
		if(Ext.getVersion().getMajor() < 5){
			me.addEvents('5250request','5250response','5250keyboard');
		}
		me.on('5250request', me.requestHandler);
		Ext.ux.Tn5250.KeyManager.register(me);
    },

    getView : function(){
    	return this.view;
    },

    getStatusBar : function(){
    	return this.statusBar;
   },

    requestHandler : function(flds,req) {
        var me = this;
        me.getStatusBar().fireEvent('5250request',req);
        Ext.ux.Tn5250.Proxy.processRequest(flds,req);
    }
});


