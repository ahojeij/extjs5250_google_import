/*
 * @Description Ext.ux.Tn3812.Window; main 3812 configuration panel to define printer properties 
 * @author  Tomislav Milkovic
 * @license LGPLv3 http://www.opensource.org/licenses/lgpl-3.0.html
 * @version 2.0, 08.04.2014.
 * @project_url http://code.google.com/p/extjs5250/
 */

   /**
    * Main TN5250 window. Contains connection panels
    */
	Ext.define('Ext.ux.Tn3812.Window', {
		extend : 'Ext.window.Window',
		
		requires : ['Ext.window.Window',
		            'Ext.ux.Tn3812.Form'
		           ],
		            
	    alias : 'widget.tn3812window',
	    
	    autoDestroy : true,    
	    	     
	    constructor	: function(config){
	    	var me = this;		
	    	me.callParent(arguments);
	    },
	    
	    initComponent  : function() {
			var me = this;
			
			
			function createPrinter(){
				//TODO connect ; add to store 
				me.close();
			};
			
	        me.layout = {
	            type: 'fit'
	        };
	        me.items = [{xtype: 'tn3812form', store : me.store}]
		    me.buttons = [
		              { text: 'Ok', handker : createPrinter },
		              { text: 'Cancel', handler : function(){me.close();} }
		            ];	        
			me.callParent();
	    }
	});
	