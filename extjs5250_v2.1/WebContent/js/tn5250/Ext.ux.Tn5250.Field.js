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
	alias : 'widget.tnfield',
	
	_lastvalue:'',
	
     //ExtJs 4
	autoEl : {tag : 'input'},

     constructor: function(config) {
         this.callParent(arguments);
     },

    //ExtJs 4
    initComponent : function(){
        var me = this;
        me.internalConfig();
	    me.callParent();
        
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
       me.internalRender();
    },

    //@Private Shared
    internalConfig:function(){
        var me = this;
       
		if(Ext.getVersion().getMajor() < 5){
			me.addEvents('5250lock','5250unlock');
		}        
        me.on('added', function( me, container, pos, eOpts ){
        	var relayers =  me.relayEvents(container.view,['5250lock','5250unlock']);
        	me.on('removed', function( me, container, pos, eOpts ){
        		Ext.destroy(relayers);
        	});
        });
        
        me.on("5250unlock", me.onUnlock,  me);
        me.on("5250lock", me.onLock,  me);
        
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
    
    applyRenderSelectors: function() {
        var me = this;

        me.callParent();
        var el = me.getEl();
        el.on("focus", me.onFocusClick,  me);
        el.on("blur", me.onBlur,me);
        el.on("keyup", me.onKeyUp,me);
        el.on("keypress", me.onKeyPress,me);
        el.on("keydown", me.onKeyDown,me);
        el.on("click", me.onFocusClick,me);
        el.on("dblclick", me.onDoubleClick,me);        
        
    },
    
    //@Private Shared
    internalRender:function(){
      var me = this;
      var dom = me.getEl().dom;
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

       if(me.readOnly){
    	   dom.setAttribute('readOnly',true);
       } else  if(me.focused){
     	dom.focus();
       }
    },


    //SHARED


    unblink : function(){
    	if(this.interval) clearInterval(this.interval);
    },

    blink: function(){
    	  if(!this.interval) return;
    	  var klasses=this.getEl().dom.getAttribute("class");
    	  if(klasses.indexOf(this.klass+'_i') == -1){
    		this.getEl().dom.setAttribute("class",this.baseklass + this.klass + '_i txt_' + this.klass.split('-')[0]);
    	  } else this.getEl().dom.setAttribute("class",this.baseklass + this.klass+ ' txt_' + this.klass.split('-')[0]);
     },

    onDoubleClick : function(){
    	this.view.fireEvent('request', 'enter');
    },

    //for rederer to know which field is focused
    onFocusClick : function(){
      this.view.activeField = this;
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

    	var input= me.getEl().dom;

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

    	var l = me.getEl().dom.value.length;
    	var ml = me.screenEl.getMaxLength(me.obj);
    	if(l==ml && ev.browserEvent.keyCode>40){
    		var caretPos = doGetCaretPosition(me.getEl().dom);
    		if(caretPos>=l)
    			me.view.doTab();
    	}
    },

    onUnlock : function(){
       var me = this;
       me.readOnly = false;
       me.getEl().dom.removeAttribute('readonly');
    },

    onLock : function(){
       var me = this;
       me.readOnly = true;
       me.getEl().dom.setAttribute('readonly','');
    },

    getSelectionStart : function(o) {
        	if (o.createTextRange) {        		
        		var r = o.createTextRange().duplicate();
        		r.moveEnd('character', o.value.length);
        		if (r.text == '') return o.value.length;
        		return o.value.lastIndexOf(r.text);
        	} else return o.selectionStart;
      },


    processFieldExit : function(){
    	var me = this;
        var ml = me.screenEl.getMaxLength(me.obj);
        var l = me.getEl().dom.value.length;
        var sel = me.getSelectionStart(me.getEl().dom);
        //clear if all selected
        if ((sel==0)&&( l>0)) {me.getEl().dom.value=""; return;};
        if ((l-sel) >0) {
        	me.getEl().dom.value = me.getEl().dom.value.substr(0,sel);
        }

        //TODO check field type, according to type do field formatting
        //fill right/left with blank or zeroes
        //this.getEl().dom.dom.value = " ".repeat(ml-l) + this.getEl().dom.dom.value;
        //this.getEl().dom.dom.value =  this.getEl().dom.dom.value + "0".repeat(ml-l);
        if (me.screenEl.isSignedNumeric(me.obj) ||  me.screenEl.isNumeric(me.obj)){
        	if (me.screenEl.isFER(me.obj)){
        		me.getEl().dom.value =  me.getEl().dom.value + "0".repeat(ml-l);
        	}
        	return;
        }

        if (me.screenEl.isToUpper(me.obj)){me.getEl().dom.value =  me.getEl().dom.value.toUpperCase(me.obj);};
        if (me.screenEl.isFER(me.obj)){me.getEl().dom.value = " ".repeat(ml-l) + me.getEl().dom.value;};

    },

    extractNumber : function (ev,target) {
    	var me = this;
    	var temp = me.getEl().dom.value;
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

    	me.getEl().dom.value = temp;
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
  	var isFirstN = allowNegative ? keychar == '-' && me.getEl().dom.value.indexOf('-') == -1 : false;
  	var isFirstD = allowDecimal ? keychar == '.' && me.getEl().dom.value.indexOf('.') == -1 : false;

  	return isFirstN || isFirstD || reg.test(keychar);
  },

  uppercase : function (ev,target) {
	  var me = this;
	  me.getEl().dom.value = me.getEl().dom.value.toUpperCase();
  }

});