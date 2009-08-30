
Ext.BLANK_IMAGE_URL = '../../extjs3/resources/images/default/s.gif';

Ext.onReady(function (){

	var telnet = new Ext.ux.Tn5250.Renderer({autoconnect : false, closable: true, disableSelect: true, debug :false});
	
	var win = new Ext.Window( {
		title : 'Telnet 5250 for ExtJs - http://code.google.com/p/extjs5250/',
		iconCls : 'silk-grid',
		renderTo : Ext.getBody(),
		layout : 'fit',
		 width : 700,
		height : 440,
		plain : true,
		tbar:[{text:'Connect',
			 handler : function(){
					telnet.initConnection();
				}
			}], 
		items : telnet
	});
	
	win.show();
	
});
