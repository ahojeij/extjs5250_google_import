/*
 * @Description Ext.ux.Tn5250.StatusLine ExtJS 4.x; Bottom tn5250 status line
 * @author  Tomislav Milkovic
 * @license LGPLv3 http://www.opensource.org/licenses/lgpl-3.0.html
 * @version 2.0, 08.04.2014.
 * @project_url http://code.google.com/p/extjs5250/
 */

/** 
 * Bottom status line with tn5250 status indicators
 */
Ext.define('Ext.ux.Tn5250.StatusLine', {
	extend: 'Ext.Component',
	requires : ['Ext.Component'],
    alias : 'widget.tnstatusline',
    autoDestroy : true,

    region: 'south',


    config : { docked : 'bottom',
                 height:20,
                 baseCls: 'tnstatus',
                 tpl: '<span class="white {clslock}" style="float:left;width:20px;padding-left:5px;">&nbsp;</span>' +
                      '<span class="white" style="float:left;width:25%;">{msg}</span> '+
                      '<span class="red" style="float:left;width:25%;">{errmsg}</span> '+
                      '<span class="yellow {clsmsgw}"  style="float:left;width:25%;padding-left:25px;">{msgw}</span>'
      },

       params : {
                 clear  : true,
                 lock  : false,
                 msgw : false,
                 conerr : '&nbsp;'
               },

       data : {
              clslock : '',
              clsmsgw : '',
              errmsg  : '&nbsp;',
              msg     : '&nbsp;',
              msgw    : '&nbsp;'
       },

       constructor: function(config) {
      	 var me = this;
           me.callParent(arguments);
       },

       //ExtJs 4
       initComponent  : function() {
     	var me = this;
         me.callParent();
         me.connecting();
         me.refresh();
         if(Ext.getVersion().getMajor() < 5){
         	me.addEvents('5250request','5250response','5250keyboard');
         }
         me.on('added', function( me, container, pos, eOpts ){
         	var relayers =  me.relayEvents(container,['5250request', '5250response','5250keyboard']);
         	me.on('removed', function( me, container, pos, eOpts ){
         		Ext.destroy(relayers);
         	});
         });

         //me.on('5250request', me.requestHandler);
         me.on('5250response', me.responseHandler);
         me.on('5250keyboard', me.keyboardHandler);
       },

       requestHandler : function(req) {
           var me = this;
           me.setStatus(req);
           me.refresh();
       },
       
       responseHandler : function(res) {
           var me = this;
           me.setStatus(res);
           me.refresh();
       },
       
       keyboardHandler : function(evt,remote) {
           var me = this;
       	   if (me.params.lock && evt!="Reset") {    		
    			return;
    	   }
           if(remote){
        	   me.setLock(true);  
           }
           me.refresh();
       },
       
       
       
    refresh : function(obj){
    	 var data = obj || this.data;
    	 if(Ext.version) {
    		 this.updateData(data);
    	 } else {
    		 this.update(data);
    	 }
    },


   setStatus : function(data){
	  var me = this;
      me.params={clear : data.clearScr, lock  : data.locked, msgw : data.msgw, conerr : data.conerr	};
      if(me.params.clear) me.clearScreen();
      me.setLock(me.params.lock,true);
      me.setError(me.params.conerr);
      me.setMsgw(me.params.msgw);
   },

   clearScreen:function(){
	  var me = this;
      me.setLock(false,false);
      me.setMsgw(false);

      me.data.errmsg='&nbsp;';
      me.data.msgw='&nbsp;';
      me.data.msg='&nbsp;';
   },

    connecting:function(){
      var me = this;	
      me.clearScreen();
      me.setLock(true,false);
      me.data.msg='Connecting...please wait!!!';
    },

    setLock:function(sts,ret){
      var me = this;	
      if(sts){
        me.data.clslock =  ret ? 'err' : 'busy';
      }else {
        me.data.clslock= '';
        me.setError();
      }
    },

    setMsgw:function(sts){
       this.data.clsmsgw = sts ? 'msgw' : '';
    },

    setError:function(data){
      var me = this;
      if(Ext.isDefined(data)){
        me.data.errmsg = data;
      }else
        me.data.errmsg = '&nbsp;';
    }
  });