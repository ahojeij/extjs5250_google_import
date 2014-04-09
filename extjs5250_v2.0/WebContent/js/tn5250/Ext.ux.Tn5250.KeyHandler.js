/**
 * @Description Ext.ux.Tn5250.KeyHandler ExtJS 4.x; Keyboard handler 
 * @author  Tomislav Milkovic
 * @license LGPLv3 http://www.opensource.org/licenses/lgpl-3.0.html
 * @version 2.0, 08.04.2014.
 * @project_url http://code.google.com/p/extjs5250/
 */

//handle active 5250 session , send keystrokes to renderer, overrides standard browser keys while active


Ext.define('Ext.ux.Tn5250.KeyHandler',{
  extend : 'Ext.util.Observable',

  displayID : '',

  constructor : function(config){
    var me = this;
    me.callParent(config);
    me.initialize();
  },

  initialize : function(){
	var me = this;
        Ext.ux.Tn5250.KeyManager.register( me );
        
        if(Ext.getVersion().getMajor() < 5){
          me.addEvents('documentKeypress');
        }
        me.on( 'documentKeypress', me._keypressEvent );

        //used to activate/deactivate key handler
        //handler is overriding every keyboard actions so it is needed to clear handler on exit
        me.on('show', me._activateEvent,me);
        me.on('tabchange', me._activateEvent,me);
        me.on('hide', me._deactivateEvent,me);
        me.on('close', me._closeEvent,me);
  },

  _activateEvent : function(){
    Ext.ux.Tn5250.KeyManager.windowActivate(this);
  },

  _deactivateEvent : function(){
    Ext.ux.Tn5250.KeyManager.windowDeactivate(this);
  },

  _closeEvent : function(p,o){
    Ext.ux.Tn5250.KeyManager.windowClose(this);
  },

  _keypressEvent: function(ev) {

          if (!ev) ev = window.event;
          var me = this, bubble = true, evt=null, remote = false;

           //do process
           //  alert('keypress:'+ev.keyCode+' '+ev.which);
           //  alert('modifiers:'+   ev.shiftKey  +' '+  ev.ctrlKey  +' '+  ev.altKey  +' '+ ev.metaKey);
           //Ext.EventObject.F1

           if ((ev.keyCode >= Ext.EventObject.F1) && ((ev.keyCode <= Ext.EventObject.F12))){
                  remote = true;
                  var i = ev.shiftKey ? 99 : 111;
                  evt = 'PF' + (ev.keyCode-i);
                  bubble = false;
           } else if(ev.keyCode == Ext.EventObject.ESC){
                  remote = true;
                  evt = ev.shiftKey ? 'Attn' : 'SysReq'
                  bubble = false;
           }  else if(ev.keyCode == Ext.EventObject.CTRL){
                  remote = true;
                  evt = ev.shiftKey ? 'Reset' : 'Enter'
                  bubble = false;
           } else if(ev.keyCode == Ext.EventObject.R){
                //Reload screen from cache (clear inputs)
                if(ev.ctrlKey) {
                  evt = ev;
                  bubble = false;
                }
           } else if(ev.keyCode == Ext.EventObject.ENTER){
                  //replace with tab -> go to next field
                  evt = ev;
                  bubble = false;
           } else if(ev.keyCode == Ext.EventObject.UP){
                  //replace with tab -> go to next field
                  evt = ev;
                  bubble = false;
           } else if(ev.keyCode == Ext.EventObject.DOWN){
                  //replace with tab -> go to next field
                  evt = ev;
                  bubble = false;
           } else if(ev.keyCode == Ext.EventObject.PAGEUP){
                  evt = 'PGUP';
                  bubble = false;
           } else if(ev.keyCode == Ext.EventObject.PAGEDOWN){
                  evt = 'PGDOWN';
                  bubble = false;
           };

           if(evt){
              me.fireEvent('5250keyboard',evt,remote);
           };

           if(!bubble) {
        	   ev.cancelBubble=true;
        	   if (ev.stopPropagation) ev.stopPropagation();
        	   if (ev.preventDefault)  ev.preventDefault();
        	   return false;
           }
           return true;
  }

});

