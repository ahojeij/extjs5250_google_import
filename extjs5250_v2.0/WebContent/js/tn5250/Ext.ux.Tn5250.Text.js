/**
 * @Description Ext.ux.Tn5250.Text ExtJS 4.x; readOnly Text field on the screen
 * @author  Tomislav Milkovic
 * @license LGPLv3 http://www.opensource.org/licenses/lgpl-3.0.html
 * @version 2.0, 08.04.2014.
 * @project_url http://code.google.com/p/extjs5250/
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

    //Touch 2
    getElementConfig: function() {
        return Ext.apply(this.callParent(),{
          tag : 'span'
        });
    },
    
    //ExtJs 4
    initComponent : function(){
      var me = this;
      me.callParent();
      //me.addEvents('click');
    },

    //Touch 2
    initialize : function(){
    	var me = this;
    	me.callParent(arguments);
        me.internalRender();
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
        me.screenEl.setObject(me.obj);
        me.klass=me.screenEl.getClass();
        me.isBreak = me.screenEl.isBreak();
        elem.dom.setAttribute('row',me.screenEl.getRow());
        elem.on("click", this.onClick,  this);

            if (!me.screenEl.isHidden() ){
            	if(me.obj.b){
            		elem.dom.setAttribute('class','button');
            	}else
            		elem.dom.setAttribute('class',me.screenEl.getClass());
            	if(this.obj.u){
            		var url = new Ext.Element(document.createElement('a'));
            		url.dom.setAttribute('class',me.screenEl.getClass());
            		url.dom.setAttribute('href',me.screenEl.getValue());
            		url.dom.setAttribute('target','blank');
            		url.dom.innerHTML = me.screenEl.getValue();
            		elem.appendChild(url);
            	}
            }
       if(me.obj.u){}else me.setText(me.screenEl.getValue());

        
        if(me.screenEl.isBlink()){
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


    onClick : function(){
        var me = this;
    	me.screenEl.setObject(me.obj);
    	me.fireEvent('click',this);
    },

    htmlEncode : function(value){
              return Ext.util.Format.htmlEncode(value).replace(/ /g,'&nbsp;');
        },

    getText : function(text){
       if(this.isBreak)
          return '<br>' + this.htmlEncode(text);
       return this.htmlEncode(text);
    },

    setText  : function(text){
       this.element.dom.innerHTML = this.getText(text);
    }
});


