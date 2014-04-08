 Ext.onReady(function(){
  
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



   var win = Ext.create('Ext.window.Window', {
          title: 'Telnet 5250 - JsonRPC Services Framework - WebSocket',
          width: 800,
          height: 600,
          name : 'main',
          layout: {
              type: 'fit'
          },
          items: [{xtype: 'tntabpanel'}],
          tbar : [{ xtype: 'button', text: 'Connect', handler : function(){
                      win.down('tntabpanel').createSession('dev');
          			} },
                  {xtype: 'button', text: 'Switch Theme', handler : switchTheme }]	                   
   });
   win.show();
   
 });

