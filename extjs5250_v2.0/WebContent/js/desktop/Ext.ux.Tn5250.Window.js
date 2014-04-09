/**
 * @Description Ext.ux.Tn5250.Panel; main 5250 panel. Contains view and satus bar; attached key handler 
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

	Ext.define('Ext.ux.Tn5250.Window', {
		extend : 'Ext.window.Window',
		requires : ['Ext.window.Window',
		            'Ext.ux.Tn5250.HostsMenu',
		            'Ext.ux.Tn5250.TabPanel'
		           ],
		            
	    alias : 'widget.tnwindow',
	    autoDestroy : true,       
	     
	    constructor	: function(config){
	    	var me = this;		
	    	me.callParent(arguments);
	    },
	    
	    initComponent  : function() {
			var me = this;
	        me.layout = {
	            type: 'fit'
	        };
	        me.items =[{xtype: 'tntabpanel', autoReload:false}],
	        me.tbar = [{ xtype: 'tnmenu', text: 'Connect'},
	                   {xtype: 'button', text: 'Switch Theme', handler : switchTheme }
	                  ];			
			me.callParent();
	    }
	});
	
})();

