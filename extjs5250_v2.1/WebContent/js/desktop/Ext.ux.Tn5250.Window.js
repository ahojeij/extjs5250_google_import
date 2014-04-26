/*
 * @Description Ext.ux.Tn5250.Window; main 5250 window. Contains 5250 panel & action buttons 
 * @author  Tomislav Milkovic
 * @license LGPLv3 http://www.opensource.org/licenses/lgpl-3.0.html
 * @version 2.0, 08.04.2014.
 * @project_url http://code.google.com/p/extjs5250/
 */

(function(){
  
    var css_themes = ['as400','as400_gray','as400_inverse','as400_blue'];

    function switchTheme() {
      var css = Ext.getHead().first('[id=tntheme]');
      if (!css) return;
      
      var id = css.dom.getAttribute('tid');
      if (id) {
        css_themes.length - 1 == id * 1 ? id = 0 : id = id * 1 + 1;
      } else {  
        id = 1;
      }
      css.dom.setAttribute('tid', id);
      css.dom.setAttribute('href', 'css/' + css_themes[id] + '.css');
   };


   function startPrinter(){
	   var win = this.up('window');
	   Ext.create('Ext.ux.Tn3812.Window', {
			   title : 'New Printer',
			   width : 300,
			   height : 200,
			   store : win.store,
			   printerStore : win.printerStore,
			   modal  :true
		   }).show();
   };
   

   var td = 0;
   function switchDemo(){
	   var pnl = Ext.ComponentQuery.query('tnpanel[title="demo"]')[0];
	   if(!pnl){
		td = 0;
	    pnl = Ext.ComponentQuery.query('tntabpanel')[0].createPanel('demo');
	   };

	   var v = pnl.getView();
	   v.render5250(testdata[td]);
	   td++; td > 2 ? td = 0 : td;
   };
	   

   /**
    * Main TN5250 window. Contains connection panels
    */
	Ext.define('Ext.ux.Tn5250.Window', {
		extend : 'Ext.window.Window',
		
		requires : ['Ext.window.Window',
		            'Ext.ux.Tn5250.HostsMenu',
		            'Ext.ux.Tn5250.TabPanel'
		           ],
		            
	    alias : 'widget.tn5250window',
	    
	    autoDestroy : true,       
	     
	    constructor	: function(config){
	    	var me = this;		
	    	me.callParent(arguments);
	    },
	    
	    initComponent  : function() {
			var me = this;
			
			var printerStore = Ext.StoreManager.get(me.printerStore);
	        me.layout = 'border';
	        me.maximized = false;
	        me.defaults =  {	            
	            split: true
	        };	        
	        me.items =[{xtype: 'tntabpanel', 
	        	        autoReload:true, 
	        	        region: 'center'
	        	       }, {      	    	   
	        	    	 xtype: 'grid', 
	        	    	 region: 'east', 
	        	    	 title : 'Printers',
	        	    	 store : printerStore,
	        	    	 collapsible: true, 
	        	    	 collapsed : true,
	        	    	 minWidth : 180,
	        	    	 width : 180,
	        	    	 columns: [
	        	    		        { text: 'Name',  dataIndex: 'name' },
	        	    		        { text: '...',  
	        	    		          xtype:'actioncolumn',
	        	    		          width:50,
	        	    		          items: [{
	        	    		        	  icon: 'img/close.png',  // Use a URL in the icon config
	        	    		                tooltip: 'Close printer',
	        	    		                handler: function(grid, rowIndex, colIndex) {
	        	    		                    var rec = grid.getStore().getAt(rowIndex);
	        	    		                    Ext.ux.Tn5250.Proxy.ClosePrinter(rec.get('name'), function(){
	        	    		                    	printerStore.load();
	        	    		                    });
	        	    		                }	        	    		        	  
	        	    		          }]
	        	    		        }
	        	    		    ]	        	    	 
	        	       }
	        		  ];
	        me.tbar = [{ xtype: 'tnmenu', 
	        			text: 'Connect', 
	        			tooltip:'Start telnet session', 
	        			store : 'tnhostsstore', 
	        			iconCls: 'img_terminal'
	        		  }, { 
	        			  xtype: 'button', 
	        			  text: 'Printer', 
	        			  tooltip : 'Start printer session', 
	        			  store : 'tnhostsstore', 
	        			  iconCls: 'img_printer', 
	        			  handler : startPrinter 
	        		  }, {
	        			  xtype: 'button', 
	        			  text: 'Switch Theme',
	        			  tooltip : 'Switch display screen theme',
	        			  iconCls: 'img_theme', 
	        			  handler : switchTheme 
	        		  }, {
	        			  xtype: 'button', 
	        			  text: 'Demo',
	        			  tooltip : 'Switch demo screens',
	        			  iconCls: 'img_demo', 
	        			  handler : switchDemo
	        		  }];		
	        me.tools =[{
	            type:'refresh',
	            tooltip: 'Refresh list of Hosts',
	            // hidden:true,
	            handler: function(event, toolEl, panelHeader) {
	            	Ext.StoreManager.get(me.store).load();
	            	Ext.StoreManager.get(me.printerStore).load();
	            }
	        }];
	        
			me.callParent();
	    }
	});
	
})();

