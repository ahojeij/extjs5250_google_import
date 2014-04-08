/**
 * @Description Ext.ux.Tn5250.KeyManager; KeyManager singleton for paner key observer registration 
 * @author  Tomislav Milkovic
 * @license LGPLv3 http://www.opensource.org/licenses/lgpl-3.0.html
 * @version 2.0, 08.04.2014.
 * @project_url http://code.google.com/p/extjs5250/
 */

Ext.define( 'Ext.ux.Tn5250.KeyManager', {
	singleton: true,
	bypass : false,
		    
	constructor: function(config) {
		this.initConfig(config);
	},
	
	initConfig: function( config ) {

        this.terms = [];
        this.active = false;
        document.onhelp=function (){return false;};

        var kup = Ext.Function.bind(this._keyUp,this),
            kdw = Ext.Function.bind(this._keyDown, this );
        if (Ext.isIE){
            document.onkeyup   = kup;
            document.onkeydown = kdw;
        } else {
            document.addEventListener('keyup', kup, true);
            document.addEventListener('keydown', kdw, true);
        };
        this.callParent(config);
    },

    _keyDown: function( ev ) {
        var me = this;
        if ( !me.active ) return;

        if (Ext.isIE){
              if( Math.abs((window.event.keyCode-18))<3) return false;
        }else{
              if( Math.abs((ev.keyCode-18))<3) return false;
        }

        //return this.activeWin.fireEvent( 'documentKeypress', ev);
        if (me.activeWin.fireEvent( 'documentKeypress', ev)) {
            return true;
        }
        
        if (Ext.isIE){
	    window.event.returnValue=false;
            if (window.event.keyCode>18) {
            	window.event.keyCode=0;
            }
	    return false;
	}

    },

    _keyUp: function( ev ) {
      var me = this;
      if ( !me.active ) return;
        
      var event = window.event ? window.event : ev;

      if ((me.bypass)& (event.keyCode==17)){
            event.cancelBubble=true;
            if (event.stopPropagation) event.stopPropagation();
            if (event.preventDefault)  event.preventDefault();
            me.bypass=false;
    		return false;
      }
      
      this.bypass = ((event.ctrlKey) & (event.keyCode!=17));

      if( event.keyCode != 17) return true ;
      if(event.altKey) return ;
      
      me.activeWin.fireEvent( 'documentKeypress', ev);

    },

    register: function( win ) {
        // where is [].add()?
        var me = this;
    	if(Ext.Array.contains(me.terms, win)) return;
    	Ext.Array.push(me.terms, win);
        win.on('close',me.windowClose, me );
        win.on('activate',me.windowActivate, me );
        win.on('deactivate',me.windowDeactivate, me );
    },

    windowActivate: function( win ) {
        this.active = true;
        this.activeWin = win;
    },

    windowDeactivate: function( win ) {
        this.active = false;
        this.activeWin = null;
    },

    windowClose: function( win ) {
        this.unregister( win );
    },

    unregister: function( win ) {
        var me = this;
        if (( me.activeWin === win )) {
            me.active = false;
            me.activeWin = null;
        }
        Ext.Array.remove(me.terms, win);
    }

});