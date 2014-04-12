/*
 * @Description Ext.ux.Tn5250.View ExtJS 4.x; Main 5250 panel , holder of scren data
 * @author  Tomislav Milkovic
 * @license LGPLv3 http://www.opensource.org/licenses/lgpl-3.0.html
 * @version 2.0, 08.04.2014.
 * @project_url http://code.google.com/p/extjs5250/
 */

/**
 * Scren veiw - used to rendr received data onto 
 * and to process keyboard requests from screen and fields
 * Contains field navigation and field exit behaviour
 */
Ext.define('Ext.ux.Tn5250.View', {
	extend : 'Ext.container.Container',
	requires : ['Ext.container.Container',
	            'Ext.ux.Tn5250.ScreenElement',
	            'Ext.ux.Tn5250.Field',
	            'Ext.ux.Tn5250.Text' ],

    alias : 'widget.tnview',
    autoDestroy : true,
    autoEl :{
              tag: 'form'
          },
    config : {
              scrollable : false,
              baseCls : 'tnmain tncontainer',
              flex : 1
        },

     constructor: function(config) {
    	 var me = this;
         me.callParent(arguments);
     },

      //ExtJs 4
      initComponent  : function() {
    	  
    	var me = this;
        me.callParent();
        me.initInternal();
        if(Ext.getVersion().getMajor() < 5){
        	me.addEvents('5250request','5250response','5250keyboard');
        }
        
        me.on('added', function( me, container, pos, eOpts ){
        	var relayers =  me.relayEvents(container,['5250response','5250keyboard']);
        	var prelayers =  container.relayEvents(me,['5250request']);
        	me.on('removed', function( me, container, pos, eOpts ){
        		Ext.destroy(relayers);
        		Ext.destroy(prelayers);
        	});
        });

        me.on('afterRender', me.calcFont);
        me.on('resize', me.calcFont);
        me.on('5250response', me.responseHandler);
        me.on('5250keyboard', me.keyboardHandler);
      },

      initInternal  : function(config) {
    	var me = this;
        me.screenEl = Ext.ux.Tn5250.ScreenElement;
        me.clear = true;
        me.lock = false;
        me.wsize = 80;
        me.hsize = 24;
        me.msgw = false;
        me.conerr = null;
        me.resize = false;
        me.insmode =true;
        me.connected= false;
        me.fields=[];
        me.btnmatch =/F([0-9]|[0-9][0-9])=/;
        me.urlmatch = x = /((http|https|ftp|ftps|mailto)\:\/\/)/;
      },

      //ExtJs 4
      onRender : function(ct, position){
      	var me = this;
      	me.callParent();
      	me.element = me.el;
      },
      
      switchIns:function(){
    	  this.insmode = !this.insmode;
      },
      
      getElSize : function (el) {
            var myWidth = 0, myHeight = 0;
            if( typeof( el.innerWidth ) == 'number' ) {
              myWidth = el.innerWidth;
              myHeight = el.innerHeight;
            } else if(el.clientWidth || el.clientHeight ) {
              myWidth = el.clientWidth;
              myHeight = el.clientHeight;
            } else if(el.offsetWidth || el.offsetHeight ) {
              myWidth = el.offsetWidth;
              myHeight = el.offsetHeight;
            }
            return {width : myWidth , height : myHeight};
      },

      getWindowSize : function () {
        var myWidth = 0, myHeight = 0;
        if( typeof( window.innerWidth ) == 'number' ) {
          //Non-IE
          myWidth = window.innerWidth;
          myHeight = window.innerHeight;
        } else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
          //IE 6+ in 'standards compliant mode'
          myWidth = document.documentElement.clientWidth;
          myHeight = document.documentElement.clientHeight;
        } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
          //IE 4 compatible
          myWidth = document.body.clientWidth;
          myHeight = document.body.clientHeight;
        }
        return {width : myWidth , height : myHeight};
      },

      calcFont : function(){
          var me = this;
          var cel = me.element.dom;
          var screen = me.getWindowSize();
          var elsize = me.getElSize(cel);
          var visina = screen.height - cel.offsetTop;
          var sirina = elsize.width;
          var fontsize = Math.floor((sirina/me.wsize)/0.63);
          var fsize2= visina/(me.hsize+2)/1.2;
          if(fsize2<fontsize) fontsize = Math.floor(fsize2);
          cel.style.fontSize = fontsize + 'px';
   	},

      exceptionHandler : function(){
		var sc = arguments[1].stackTrace[0];
		var msg = 'Server call error in <br> File : ' + sc.fileName + ' <br> Class : ' + sc.className + ' <br> Method : ' + sc.methodName + ' <br> Line : ' + sc.lineNumber;
		Ext.Msg.alert( 'Error!',  msg );
	},


	//disable/enable must be overriden as we are working with custom html elements not belonging to ext
      disable : function(){
		var me = this;
		var collection = me.getFields();
		Ext.each(collection, function(item, index, length) {
			item.disable();
		});
	},

      enable : function(){
		var collection = this.getFields();
		Ext.each(collection, function(item, index, length) {
			item.enable();
		});
	},
        
      getFields : function(){
         return this.fields;
        },

      // create list of fields and its data , used for sending to server as part of telnet request
      formatJsonReq : function() {
		var me = this;
		var collection = me.getFields();
		var fields = [];
		var i=0;
		Ext.each(collection, function(item, index, length) {
			var id = me.screenEl.getFieldId(item.obj); 
			if(id>1000){
				fields[i].t=fields[i].t +item.el.dom.value;
			}else{
				fields.push({
					d :  [0,0,id] ,
					t :item.el.dom.value
				});
				i = index;
			}
		});
		return fields;
	},

	getRequest : function(key,data){
		var me = this;
	    var req = {
	    	 displayID :me.displayID,
			 keyRequest :key,
			 data:data ,
			 cursorField :me.screenEl.getFieldId(me.activeField.obj),
			 // or
			 cursorRow : me.screenEl.getRow(me.activeField.obj)
			 // TODO add function to Text renderer, on click
		     };
	    return req;
	},

	// handle 5250 keys, iterates through fields and creates json object then calls 5250request function
	requestHandler : function(key,data) {
		var me = this;
		if (me.activeField==null) return;
		if (me.disabled) return;
		me.disable();
		me.lock=true;
		var req = me.getRequest(key,Ext.isString(data) ? data : null);
		var flds = me.formatJsonReq();
		me.fireEvent('5250request',flds, req);
	},
	
	//event on server reponse
	responseHandler : function(obj) {
		var me = this;
		me.preRender5250(obj);
		me.render5250(obj.data);
    },

	// keyboard commands - field exit, up,down...
	keyboardHandler : function(event,remote) {
		var me = this;
        if (remote){
        	 me.requestHandler(event);
            return;
        };
		
		switch (event.keyCode) {
		case 13:
			return me.doTab();
		case 38:
			return me.prevField();
		case 40:
			return me.nextField();
		case 82:
			return me.render5250(me.objs);
		}
	},

	prevField : function() {
		var comp = this;
		//var collection = comp.items.filter('id', 'FLD');
		var collection = this.getFields();
		Ext.each(collection, function(item, index, items) {
			if (item.id == comp.activeField.id) {
				if (index > 0) {
					comp.activeField = collection[index - 1];
					collection[index - 1].el.dom.focus();
				} else {
					comp.activeField = collection[length - 1];
					collection[length - 1].el.dom.focus();
				}
				return false;
			}

		});
	},

	nextField : function() {
		var comp = this;
		var collection = this.getFields();

		Ext.each(collection, function(item, index, items) {
			if (item.id == comp.activeField.id) {
				if (index < collection.length - 1) {
					comp.activeField = collection[index + 1];
					collection[index + 1].el.dom.focus();
				} else {
					comp.activeField = collection[0];
					collection[0].el.dom.focus();
				}
				return false;
			}

		});
	},

	// proces tab key, find nex and focuse it, if last go to first
	doTab : function() {
		var comp = this;
		var collection = this.getFields();

		Ext.each(collection, function(item, index, items) {
			if (item.id == comp.activeField.id) {
				item.processFieldExit();
				if (index < collection.length - 1) {
					comp.activeField = collection[index + 1];
					collection[index + 1].el.dom.focus();
					collection[index + 1].el.dom.select();
				} else {
					comp.activeField = collection[0];
					collection[0].el.dom.focus();
					collection[0].el.dom.select();
				}
				return false;
			}

		});
	},


	// clear 5250 screen data
	clearScreen : function() {
		var me = this;
		me.removeAll(true);
		me.elements=[];
		me.fields=[];
	},

    getLine : function(row){
          var el = Ext.create('Ext.Container' , {cls : 'tnline', row : row});
          return el;
    },

    addToLine : function(line,elm){
            line.add(elm);
            this.elements.push(elm);
    },

	//draws initial screen while connecting to host
	preRender5250 : function(data) {
		var me = this;
		me.resize = me.wsize != data.size;
		me.clear  = data.clearScr;
		me.lock  = data.locked;
		me.wsize = data.size;
		me.hsize = me.wsize==80 ? 24 : 27;
		me.msgw = data.msgw;
		me.conerr = data.conerr;
		if(me.resize) me.calcFont();
	},
	
	// parse received json stream and create 5250 screen
	render5250 : function(data) {
	    	var me = this;
            if(me.clear) me.clearScreen();

            if (data==undefined) return;
      	    if (!Ext.isArray(data)) return;

      		me.objs = data;
      		var jsonobj = null;
      		var row = 0;
      		var elm = null;
      		var prevIsField = false;
      		var line = me.getLine(row);
      		var lines = [];
      		lines.push(line);

      		for ( var i = 0, l = me.objs.length; i < l; i++) {
      			jsonobj = me.objs[i];
      			if (me.screenEl.getRow(jsonobj) > row) {
      				row = me.screenEl.getRow(jsonobj);
      				//jsonobj.br = true;
      				line = me.getLine(row);
      				lines.push(line);
      			}

      			if (me.screenEl.isField(jsonobj)) {
      				prevIsField = true;
      				elm = Ext.create('Ext.ux.Tn5250.Field',{
      					obj :jsonobj,
      					screenEl :me.screenEl,
      					name :'FLD' + me.screenEl.getFieldId(jsonobj),
      					view :me
      				});
      				me.fields.push(elm);
      			} else {
      				var pos = jsonobj.t.search(me.btnmatch);
      				if(pos>-1){
      					me.renderClickable(line,jsonobj,me.btnmatch,true);
      					continue;
      				}
      				var pos = jsonobj.t.search(me.urlmatch);
      				if(pos>-1){
      					me.renderClickable(line,jsonobj,me.urlmatch,false);
      					continue;
      				}

      				elm = Ext.create('Ext.ux.Tn5250.Text',{
      					obj :jsonobj,
      					screenEl :me.screenEl
      				});
      				prevIsField = false;
      			}
      			// add focus listener to element
      			elm.on('focus', function(o) {me.activeField = o;}, me);

      			// TODO add listener for text also
      			 elm.on('click',function(o){me.activeField = o;}, me);
      			// add element to the screen container (form) and focus it if needed
      			me.addToLine(line,elm);
      			if (me.screenEl.isFocused(jsonobj)) {
      				me.activeField = elm;
      				elm.focused = true;
      			}

		} //end for

        me.add(lines);
        /*
		if(this.lock){
			this.disable();
		} else 	this.enable();
		*/
	},


    newLine : function(jsonobj){
        var elm = null;
		if(jsonobj.br==true){
			elm = me.getPart(jsonobj, '', false, true);
			me.addToLine(line,elm);
		}
	},
		
	renderClickable : function(line,jsonobj, text, isButton ){
		var me = this;
	    var elm = null;
		var tx = jsonobj.t;
		var poseq=-1;
		var pos=-1;
		var sig = isButton ? '=' : ' ';
		
		me.newLine(jsonobj);
		
		while(true){
			pos = tx.search(text);
			if(pos==-1) break;
			poseq=tx.indexOf(sig,pos);
			if(poseq==-1) break;
			
			//render url part
			if(!isButton){
					if(poseq==-1) {
						elm = me.getPart(jsonobj, tx.slice(0,pos), false,false);
						me.addToLine(line,elm);
						elm = me.getUrlPart(jsonobj, tx.slice(pos));
						me.addToLine(line,elm);
						tx='';
						break;
					}
		    }
		
			elm = me.getPart(jsonobj, tx.slice(0,pos), false,false);
			me.addToLine(line,elm);
			if(isButton){
		      elm = me.getPart(jsonobj, tx.slice(pos,poseq), true,false);
		    } else {
		      elm = me.getUrlPart(jsonobj, tx.slice(pos,poseq), true,false);
		    }
			me.addToLine(line,elm);
			tx=tx.slice(tx.indexOf(sig,pos));
		}
		
		if(tx.length>0){
			elm = me.getPart(jsonobj, tx, false,false);
			me.addToLine(line,elm);
		}
	},
		
	getPart : function(jsonobj,txt,btn,brk){

		var me = this,
        obj = Object.create(jsonobj);
		obj.br=brk;
		obj.t= txt;
		obj.b= btn;
		var elm = new Ext.ux.Tn5250.Text( {
			obj :obj,
			screenEl :me.screenEl
		});
		if(btn){
			elm.on('click',function(o){
				this.fireEvent('request', 'p' + o.obj.t.toLowerCase());
			}, me);
		}
		return elm;
	},

	getUrlPart : function(jsonobj,txt){

		var obj = Object.create(jsonobj);
		obj.br=false;
		obj.t= txt;
		obj.u=true;
		var elm = new Ext.ux.Tn5250.Text( {
			obj :obj,
			screenEl :this.screenEl
		});
		return elm;
	},

	// used to disable selection (events : onselectstart - ie, onmousedown -
	// mozilla)
	dummyEvent : function() {
		return false;
	}
	
});


