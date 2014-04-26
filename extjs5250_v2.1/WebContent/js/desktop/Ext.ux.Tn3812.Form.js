/*
 * @Description Ext.ux.Tn3812.Form; main 3812 configuration form to define printer properties 
 * @author  Tomislav Milkovic
 * @license LGPLv3 http://www.opensource.org/licenses/lgpl-3.0.html
 * @version 2.0, 08.04.2014.
 * @project_url http://code.google.com/p/extjs5250/
 */

   /**
    * Main TN3812 form. Contains connection parameters.
    * For now it is only simple config for with host and printer name,
    * in the future standard tn3812 properties can be added to be able to configure 
    * printer like in standard client access
    */
	Ext.define('Ext.ux.Tn3812.Form', {
		extend : 'Ext.form.Panel',
		
		requires : ['Ext.form.Panel'
		           ],
		            
	    alias : 'widget.tn3812form',
	    
	    autoDestroy : true,       
	     
	    constructor	: function(config){
	    	var me = this;		
	    	me.callParent(arguments);
	    },
	    
	    initComponent  : function() {
			var me = this;			
			me.bodyPadding = '5 5 0';
			me.fieldDefaults =  {
	            msgTarget: 'side',
	            labelWidth: 75
	        };			
	        me.items =[
	                   {
	                	    xtype : 'combo',
	                	    fieldLabel: 'Host name',
	                	    store: Ext.StoreManager.get(me.store),
	                	    editable  : false,
	                	    queryMode: 'local',
	                	    displayField: 'name',
	                	    valueField: 'name',
	                	    name : 'hostName'
	                   },{
                	        xtype: 'textfield',
                	        fieldLabel: 'Device name',
                	        fieldStyle : {textTransform: 'uppercase'},
                	        name: 'printerName'	                	   
	                   }];		
			me.callParent();
	    }
	});
	