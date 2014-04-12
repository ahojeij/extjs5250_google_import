/*
 * @Description Ext.ux.Tn5250.Field ExtJS 4.x; input field for 5250 screen
 * @author  Tomislav Milkovic
 * @license LGPLv3 http://www.opensource.org/licenses/lgpl-3.0.html
 * @version 2.0, 08.04.2014.
 * @project_url http://code.google.com/p/extjs5250/
 */

String.prototype.repeat = function(l){
	return new Array(l+1).join(this);
};

/**
 * Single screen input field, part of single view
 */
Ext.define('Ext.ux.Tn5250.Field',{
	extend : 'Ext.Component',
	requires : ['Ext.Component'],	
	
	_lastvalue:'',

        //ExtJs 4
	autoEl : {tag : 'input'},

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
        //me.addEvents('focus','blur');
        me.internalConfig();
    },
    
    //ExtJs 4
    onDestroy : function(){
      var me = this;
      me.unblink();
      me.callParent();
    },

    //ExtJs 4
    onRender : function(ct, position){
       var me = this;
       me.callParent(arguments);
       me.element = me.el;
       me.internalRender();
    },

    //Touch 2
    initialize : function(){
       var me = this;
       me.callParent(arguments);
       me.internalConfig();
       me.internalRender();

    },
    
    //Touch 2
    getElementConfig: function() {
        return Ext.apply(this.callParent(),{
          tag : 'input'
        });
    },

    //Touch 2
    remove : function(){
      this.unblink();
      this.callParent();
    },

    //@Private Shared
    internalConfig:function(){
        var me = this;
        
        me.on("focus", me.onFocus,  me);
        me.on("blur", me.onBlur,me);
        me.on("keyup", me.onKeyUp,me);
        me.on("keypress", me.onKeyPress,me);
        me.on("keydown", me.onKeyDown,me);
        me.on("dblclick", me.onDoubleClick,me);

    	if ( me.screenEl.isNumeric(me.obj)){
          me.decimal=0; //TODO does have me
          me.isDecimal = false;
          me.allowDecimal = false;
          me.allowNegative = false;
    	};
    	if (me.screenEl.isSignedNumeric(me.obj)){
          //me.decimal=2;//TODO does have me
          me.isDecimal = false;
          me.allowDecimal = true;
          me.allowNegative = true;
     	};
    },
    
    //@Private Shared
    internalRender:function(){
      var me = this;
      var dom = me.element.dom;
      me.klass=me.screenEl.getClass(me.obj);
      me.baseklass='';
      
      dom.setAttribute('name',me.id);

      if(me.screenEl.isHidden(me.obj)){
        dom.setAttribute('type','password');
        if(me.screenEl.isBypassField(me.obj)){
          me.baseklass='hidden ';
         }else {
           me.baseklass='password ';
         }
       }else{
         dom.setAttribute('type','text');
         if(me.screenEl.isBypassField(me.obj)){
           dom.setAttribute('readOnly',true);
           me.baseklass='txtBypass ';
         } else me.baseklass='textbox ';
       }
       
       dom.setAttribute('class',me.baseklass + me.klass + ' txt_' + me.klass.split('-')[0]);
       dom.setAttribute('length',me.screenEl.getMaxLength(me.obj));
       dom.setAttribute('size',me.screenEl.getLength(me.obj));
       dom.setAttribute('maxlength',me.screenEl.getMaxLength(me.obj));
       dom.setAttribute('autocomplete','off');
       dom.setAttribute('autocomplete','off');
       dom.value = me.screenEl.getValue(me.obj);


       if(me.screenEl.isBlink(me.obj)){
         me.interval=setInterval(function () { me.blink(); }, 500);
       }

       if(me.focused){
     	dom.focus();
       }
    },


    //SHARED


    unblink : function(){
    	if(this.interval) clearInterval(this.interval);
    },

    blink: function(){
    	  if(!this.interval) return;
    	  var klasses=this.element.dom.getAttribute("class");
    	  if(klasses.indexOf(this.klass+'_i') == -1){
    		this.element.dom.setAttribute("class",this.baseklass + this.klass + '_i txt_' + this.klass.split('-')[0]);
    	  } else this.element.dom.setAttribute("class",this.baseklass + this.klass+ ' txt_' + this.klass.split('-')[0]);
     },

    onDoubleClick : function(){
    	this.view.fireEvent('request', 'enter');
    },

    //for rederer to know which field is focused
    onFocus : function(){
      this.screenEl.setObject(this.obj);
      //this.fireEvent("focus", this);
    },

    onBlur : function(ev,target){
    	var sts = true, me = this;
    	if ( me.screenEl.isNumeric(me.obj) || me.screenEl.isSignedNumeric(me.obj)){
           sts = me.extractNumber(ev,target);
      	};
      	if(sts){
      		me.processFieldExit();
      	};
      	return sts;
    },

    onKeyUp : function(ev,target){
    	//3
    	var me = this;
    	if ( me.screenEl.isNumeric(me.obj) || me.screenEl.isSignedNumeric(me.obj)){
           return  me.extractNumber(ev,target);
    	};
    },

    onKeyPress : function(ev,target){
    	//2
    	var me = this;
    	if ( me.screenEl.isBypassField(me.obj)){
            return false;
     	};

    	if ( me.screenEl.isNumeric(me.obj)|| me.screenEl.isSignedNumeric(me.obj)){
           return me.blockNonNumbers(ev,target);
    	};

    	var input= me.element.dom;

    	var selection = new Selection(input);
    	var s = selection.create();
    	if(s.end>s.start) return;

    	if(input.value.length>0 ){
    		if(me.view.insmode){
        		var caretPos = doGetCaretPosition(input);

        		input.value = input.value.slice(0,caretPos) + input.value.slice(caretPos+1);
             	if (me.screenEl.isToUpper(me.obj) && ev.browserEvent.keyCode>40){
             		me.uppercase(ev,target);
             	}
        		doSetCaretPosition(input, caretPos);

    		}else{
             	if (me.screenEl.isToUpper(me.obj) && ev.browserEvent.keyCode>40){
             		me.uppercase(ev,target);
             	}

    		}
    	}
    },

    onKeyDown : function(ev,target){
    	//1
    	var me = this;
    	if(ev.browserEvent.keyCode==45){
    		this.view.switchIns();
    	}

    	var l = me.element.dom.value.length;
    	var ml = me.screenEl.getMaxLength(me.obj);
    	if(l==ml && ev.browserEvent.keyCode>40){
    		var caretPos = doGetCaretPosition(me.element.dom);
    		if(caretPos>=l)
    			me.view.doTab();
    	}
    },

    enable : function(){
       this.element.dom.removeAttribute('readonly');
    },

     disable : function(){
       this.element.dom.setAttribute('readonly','');
    },

    getSelectionStart : function(o) {
        	if (o.createTextRange) {
        		var r = document.selection.createRange().duplicate();
        		r.moveEnd('character', o.value.length);
        		if (r.text == '') return o.value.length;
        		return o.value.lastIndexOf(r.text);
        	} else return o.selectionStart;
      },


    processFieldExit : function(){
    	var me = this;
        var ml = me.screenEl.getMaxLength(me.obj);
        var l = me.element.dom.value.length;
        var sel = me.getSelectionStart(me.element.dom);
        //clear if all selected
        if ((sel==0)&&( l>0)) {me.element.dom.value=""; return;};
        if ((l-sel) >0) {
        	me.element.dom.value = me.element.dom.value.substr(0,sel);
        }

        //TODO check field type, according to type do field formatting
        //fill right/left with blank or zeroes
        //this.element.dom.dom.value = " ".repeat(ml-l) + this.element.dom.dom.value;
        //this.element.dom.dom.value =  this.element.dom.dom.value + "0".repeat(ml-l);
        if (me.screenEl.isSignedNumeric(me.obj) ||  me.screenEl.isNumeric(me.obj)){
        	if (me.screenEl.isFER(me.obj)){
        		me.element.dom.value =  me.element.dom.value + "0".repeat(ml-l);
        	}
        	return;
        }

        if (me.screenEl.isToUpper(me.obj)){me.element.dom.value =  me.element.dom.value.toUpperCase(me.obj);};
        if (me.screenEl.isFER(me.obj)){me.element.dom.value = " ".repeat(ml-l) + me.element.dom.value;};

    },

    extractNumber : function (ev,target) {
    	var me = this;
    	var temp = me.element.dom.value;
    	var decimalPlaces = me.decimal;
        var allowNegative = me.allowNegative;

          me.isDecimal = false;
          me.allowNegative = false;

    	// avoid changing things if already formatted correctly
    	var reg0Str = '[0-9]*';
    	if (decimalPlaces > 0) {
    		reg0Str += '\\.?[0-9]{0,' + decimalPlaces + '}';
    	} else if (decimalPlaces < 0) {
    		reg0Str += '\\.?[0-9]*';
    	}
    	reg0Str = allowNegative ? '^-?' + reg0Str : '^' + reg0Str;
    	reg0Str = reg0Str + '$';
    	var reg0 = new RegExp(reg0Str);
    	if (reg0.test(temp)) return true;

    	// first replace all non numbers
    	var reg1Str = '[^0-9' + (decimalPlaces != 0 ? '.' : '') + (allowNegative ? '-' : '') + ']';
    	var reg1 = new RegExp(reg1Str, 'g');
    	temp = temp.replace(reg1, '');

    	if (allowNegative) {
    		// replace extra negative
    		var hasNegative = temp.length > 0 && temp.charAt(0) == '-';
    		var reg2 = /-/g;
    		temp = temp.replace(reg2, '');
    		if (hasNegative) temp = '-' + temp;
    	}

    	if (decimalPlaces != 0) {
    		var reg3 = /\./g;
    		var reg3Array = reg3.exec(temp);
    		if (reg3Array != null) {
    			// keep only first occurrence of .
    			//  and the number of places specified by decimalPlaces or the entire string if decimalPlaces < 0
    			var reg3Right = temp.substring(reg3Array.index + reg3Array[0].length);
    			reg3Right = reg3Right.replace(reg3, '');
    			reg3Right = decimalPlaces > 0 ? reg3Right.substring(0, decimalPlaces) : reg3Right;
    			temp = temp.substring(0,reg3Array.index) + '.' + reg3Right;
    		}
    	}

    	me.element.dom.value = temp;
    },

  blockNonNumbers : function (ev,target) {
	  var me = this;
	  var allowDecimal = me.allowDecimal;
	  var allowNegative = me.allowNegative;
	  var e = ev;
	  var key=0;
	  var isCtrl = false;
	  var keychar;
	  var reg;

  	if(window.event) {
  		key = e.keyCode;
  		isCtrl = window.event.ctrlKey;
  	}
  	else if(e.which) {
  		key = e.which;
  		isCtrl = e.ctrlKey;
  	}

  	if (isNaN(key)) return true;

  	keychar = String.fromCharCode(key);

  	// check for backspace or delete, or if Ctrl was pressed
  	if (key == 8 || isCtrl)
  	{
  		return true;
  	}

  	reg = /\d/;
  	var isFirstN = allowNegative ? keychar == '-' && me.element.dom.value.indexOf('-') == -1 : false;
  	var isFirstD = allowDecimal ? keychar == '.' && me.element.dom.value.indexOf('.') == -1 : false;

  	return isFirstN || isFirstD || reg.test(keychar);
  },

  uppercase : function (ev,target) {
	  me.element.dom.value = me.element.dom.value.toUpperCase();
  }

});