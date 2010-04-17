/**
 * @Description Ext.ux.Tn5250 ExtJS 2.x and 3.x; Telnet 5250 for access to IBM iSeries (AS/400) servers (java server side using DWR library)
 * @author  Tomislav Milkovic
 * @license LGPLv3 http://www.opensource.org/licenses/lgpl-3.0.html
 * @version 1.1, 01.09.2009.
 * @project_url http://code.google.com/p/extjs5250/
 */


/**
 * @todo autoresize fonts on telnet screen size change 24x80 / 27x132
 * @todo implement field formating functions 
**/

Ext.ux.Tn5250.Renderer = Ext.extend(Ext.form.FormPanel, {

	initComponent : function() {

	   var screl = new Ext.ux.Tn5250.ScreenElement();
	   
		Ext.apply(this, {
			activeField : null,
			objs : null,
			screenEl : screl,
			debug : this.debug == undefined ? false : this.debug,
			items : null
		});
		
		Ext.ux.Tn5250.Renderer.superclass.initComponent.call(this);
		
		this.addEvents(
				/**
				 * @event action Fires when new 5250 keys are pressed, used for sending
				 *        requests to the server
				 * @param {key ,
				 *            event, owner}
				 */
				'request',
				/**
				 * @event action Fires when key remap needed
				 * @param {key ,
				 *            event owner}
				 */
				'remap');
		
				
		// for catching keyboard handler events (remapped keys to commands or remote requests....)
		this.on('request', this.requestHandler);
		this.on('remap', this.remapHandler);
	
		// set black background
		this.on('render', function() {
			this.body.addClass('container');			
		});
	
		if (this.autoconnect) this.initConnection();
		
		if (!this.debug ) return;
		
		this.render5250(Ext.ux.Tn5250.testdata);
	},

	exceptionHandler : function(){
		var sc = arguments[1].stackTrace[0];
		var msg = 'Server call error in <br> File : ' + sc.fileName + ' <br> Class : ' + sc.className + ' <br> Method : ' + sc.methodName + ' <br> Line : ' + sc.lineNumber;
		Ext.Msg.alert( 'Error!',  msg );
	},

	initFirstScreen : function (data){
		if (this.debug ) return;
		if (data) 
		Tn5250Proxy.processRequest([],{devName:this.devName},{ exceptionHandler : this.exceptionHandler, callback : this.render5250, scope:this}) ;	 
		
	},
	
	promptDevName : function(){
		
		var render = this;	
		Ext.Msg.prompt('Select device name', 'Device Name:', function(btn, text){
		    if (btn == 'ok'){
		    	this.devName=text;
		    	this.setTitle(text);
		    	this.handler = new Ext.ux.Tn5250.KeyHandler( {
					renderer :this
				});

				// call initial connection
		    	if (this.debug ) return;
		    	Tn5250Proxy.CreateSession(render.devName,{exceptionHandler : this.exceptionHandler,callback : render.initFirstScreen, scope:render});

		    }
		},this);
		
	},
	
	
	// called on create object, if devName not set, cretewindow to ask for it	
	initConnection : function() {
		if (this.devName != undefined) {
			this.setTitle(this.devName);
			this.handler = new Ext.ux.Tn5250.KeyHandler( {
				renderer :this
			});
			
			// call initial connection
			if (this.debug ) return;
			Tn5250Proxy.CreateSession(this.devName,{exceptionHandler : this.exceptionHandler, callback : this.initFirstScreen, scope:this});		
			return;
		} 
		
		   var item = new Ext.ux.Tn5250.Text( {
				obj :{ea :"Connecting..."},
				screenEl : this.screenEl
			});
		   
		   this.clearScreen();
		   this.add(item);
		   this.doLayout();
		   
		this.promptDevName();

	},

	disable : function(){
		//Ext.ux.Tn5250.Renderer.superclass.disable.call(this);
		var collection = this.items.filter('id', 'FLD');
		collection.each( function(item, index, length) {
			item.disable();
		});
	},
	
	enable : function(){
		//Ext.ux.Tn5250.Renderer.superclass.enable.call(this);
		var collection = this.items.filter('id', 'FLD');
		collection.each( function(item, index, length) {
			item.enable();
		});
	},	

	// create list of fields and its data , used for sending to server as part of telnet request
	formatJsonReq : function() {
		var collection = this.items.filter('id', 'FLD');
		var fields = new Array(collection.getCount());
		var i=0;	
		collection.each( function(item, index, length) {
			fields[i]= {
				e4 :item.fieldId,
				ea :item.el.dom.value
			};
			i++;
		});
		return fields;
	},

	// handle 5250 keys, iterates through fields and creates json object then calls dwr 5250request function
	requestHandler : function(key) {
		if (this.activeField==null) return;
		if (this.disabled) return;
		this.disable();
		var req = {
				devName :this.devName,
				keyRequest :key,
				
				cursorField :this.activeField.screenEl.getFieldId(),
				// or
				cursorRow : this.activeField.screenEl.getRow()
			// TODO add function to Text renderer, on click
					};
			var flds =  this.formatJsonReq();
			if (this.debug ) return;
		Tn5250Proxy.processRequest(flds, req, { exceptionHandler : this.exceptionHandler, callback:this.render5250, scope:this});
	},

	// keyboard commands - field exit, up,down...
	remapHandler : function(event) {

		switch (event.keyCode) {
		case 13:
			return this.doTab();
		case 38:
			return this.prevField();
		case 40:
			return this.nextField();
		case 82:
			return this.render5250(this.objs);
		}
	},

	prevField : function() {
		var comp = this;
		var collection = comp.items.filter('id', 'FLD');
		collection.each( function(item, index, length) {
			if (item.id == comp.activeField.id) {
				if (index > 0) {
					comp.activeField = collection.items[index - 1];
					collection.items[index - 1].focus();
				} else {
					comp.activeField = collection.items[length - 1];
					collection.items[length - 1].focus();
				}
				return false;
			}

		});
	},

	nextField : function() {
		var comp = this;
		var collection = comp.items.filter('id', 'FLD');

		collection.each( function(item, index, length) {
			if (item.id == comp.activeField.id) {
				if (index < length - 1) {
					comp.activeField = collection.items[index + 1];
					collection.items[index + 1].focus();
				} else {
					comp.activeField = collection.items[0];
					collection.items[0].focus();
				}
				return false;
			}

		});
	},

	// proces tab key, find nex and focuse it, if last go to first
	doTab : function() {
		var comp = this;
		var collection = comp.items.filter('id', 'FLD');

		collection.each( function(item, index, length) {
			if (item.id == comp.activeField.id) {
				item.processFieldExit();
				if (index < length - 1) {
					comp.activeField = collection.items[index + 1];
					collection.items[index + 1].focus();
					collection.items[index + 1].el.dom.select();
				} else {
					comp.activeField = collection.items[0];
					collection.items[0].focus();
					collection.items[0].el.dom.select();
				}
				return false;
			}

		});
	},

	// clear 5250 screen data
	clearScreen : function() {
		
		if (Ext.type(this.items) != 'nodelist') return;
		var comp = this;
		this.items.each( function(item, index, length) {
			comp.remove(item);
		});

		this.items.clear();
		// this.comp.doLayout();
	},

	// parse received json stream and create 5250 screen
	render5250 : function(data) {
		
		if (data==undefined) return;
	    if (!Ext.isArray(data)) return;
	    
		this.objs = data;
		this.clearScreen();

		var jsonobj = null;
		var row = 0;
		var elm = null;
		var prevIsField = false;
		for ( var i = 0, l = this.objs.length; i < l; i++) {
			jsonobj = this.objs[i];
			this.screenEl.setObject(jsonobj);
			if (this.screenEl.getRow() > row) {
				row = this.screenEl.getRow();
				jsonobj.br = true;
			}

			if (this.screenEl.isField()) {
				prevIsField = true;
				elm = new Ext.ux.Tn5250.Field( {
					obj :jsonobj,
					screenEl :this.screenEl,
					id :'FLD' + this.screenEl.getFieldId()
				});
			} else {
				
				elm = new Ext.ux.Tn5250.Text( {
					obj :jsonobj,
					hidden : prevIsField,
					screenEl :this.screenEl
				});
				prevIsField = false;
			}
			// add focus listener to element
			elm.on('focus', function(o) {this.activeField = o;}, this);
			
			// TODO add listener for text also
			 elm.on('click',function(o){this.activeField = o;}, this);
		
			// add element to the screen container (form) and focus it if needed
			this.add(elm);
			if (this.screenEl.isFocused()) {
				this.activeField = elm;
				elm.focus(false, 300);
			}
		
			}
			
			this.enable();
			this.doLayout();

	},

	// used to disable selection (events : onselectstart - ie, onmousedown -
	// mozilla)
		dummyEvent : function() {
			return false;
		}
});


Ext.reg('ext5250', Ext.ux.Tn5250.Renderer);	


