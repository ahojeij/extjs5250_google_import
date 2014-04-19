/*
 * @Description Ext.ux.Tn5250.Text ExtJS 4.x; readOnly Text field on the screen
 * @author  Tomislav Milkovic
 * @license LGPLv3 http://www.opensource.org/licenses/lgpl-3.0.html
 * @version 2.0, 08.04.2014.
 * @project_url http://code.google.com/p/extjs5250/
 */

/**
 * Represents non editable region on screen view
 */
Ext.define('Ext.ux.Tn5250.Text', {
    extend : 'Ext.Component',
    requires : ['Ext.Component'],

    //ExtJs 4
    autoEl : {tag:'span'},

     constructor: function(config) {
         //if Touch, remove deprecated
         if(Ext.version) {
           this.initComponent=Ext.emptyFn;
           this.onRender=Ext.emptyFn;
         };
         this.callParent(arguments);
     },

    
    //ExtJs 4
    initComponent : function(){
      var me = this;
      me.callParent();
      //me.addEvents('click');
    },


    //ExtJs 4
    onRender : function(ct, position){
    	var me = this;
    	me.callParent();
    	me.element = me.el;
        me.internalRender();
    },

    //shared
    getElem : function(){
      if('ext'===this.env) return this.el;
      return this.element;
    },

    internalRender : function(){
        var me = this;
        var elem = me.element;

        me.klass=me.screenEl.getClass();
        me.isBreak = me.screenEl.isBreak();
        elem.dom.setAttribute('row',me.screenEl.getRow(me.obj));
        elem.on("click", me.onClick,  me);

            if (!me.screenEl.isHidden(me.obj) ){
            	if(me.obj.b){
            		elem.dom.setAttribute('class','button');
            	}else
            		elem.dom.setAttribute('class',me.screenEl.getClass(me.obj));
            	if(this.obj.u){
            		var url = new Ext.Element(document.createElement('a'));
            		url.dom.setAttribute('class',me.screenEl.getClass(me.obj));
            		url.dom.setAttribute('href',me.screenEl.getValue(me.obj));
            		url.dom.setAttribute('target','blank');
            		url.dom.innerHTML = me.screenEl.getValue(me.obj);
            		elem.appendChild(url);
            	}
            }
       if(me.obj.u){}else me.setText(me.screenEl.getValue(me.obj));

        
        if(me.screenEl.isBlink(me.obj)){
        	me.interval=setInterval(function () { me.blink(); }, 500);
        }
    },

    //Touch
    destroy: function() {
        this.unblink();
        this.callParent();
    },

    //ExtJs
    onDestroy : function(){
      this.unblink();
      this.callParent();
    },

    unblink : function(){
    	if(this.interval) clearInterval(this.interval);
    },

    blink: function(){
      var me = this;
      if(!me.interval) return;
  	  if(me.klass == me.element.dom.getAttribute("class")){
  		me.element.dom.setAttribute("class",me.klass + '_i');
  	  } else me.element.dom.setAttribute("class",me.klass);
   },

    htmlEncode : function(value){
       return Ext.util.Format.htmlEncode(value).replace(/ /g,'&nbsp;');
    },

    getText : function(text){
       var me = this;
       if(me.isBreak)
          return '<br>' + me.htmlEncode(text);
       return me.htmlEncode(text);
    },

    setText  : function(text){
       this.element.dom.innerHTML = this.getText(text);
    }
});


