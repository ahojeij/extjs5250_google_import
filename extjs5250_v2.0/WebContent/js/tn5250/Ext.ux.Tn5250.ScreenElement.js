/**
 * @Description Ext.ux.Tn5250.ScreenElemen ExtJS 4.x; Screen data decoder
 * @author  Tomislav Milkovic
 * @license LGPLv3 http://www.opensource.org/licenses/lgpl-3.0.html
 * @version 2.0, 08.04.2014.
 * @project_url http://code.google.com/p/extjs5250/
 */

Ext.define('Ext.ux.Tn5250.ScreenElement', {
  
  setObject :  function(o) {
    ((o==undefined) || (o==null)) ?  this.o=null : this.o = o;
  },

  isBreak :  function() {
    return (this.o && this.o.br) ? this.o.br : false;
  },

  isField :  function() {
     return (this.o  && this.o.d) ? this.o.d[2]>0 : false;
  },

  isHidden :  function() {
    return (this.o  && this.o.d) ? this.o.d[0] : -1;
  },

  getFieldType :  function() {
    return (this.o  && this.o.d) ? this.o.d[1] : -1;
  },

  getFieldId :  function() {
    return (this.o  && this.o.d) ? this.o.d[2] : -1;
    },

  getAttributeId :  function() {
    return (this.o  && this.o.d) ? this.o.d[3] : -1;
    },


  getLength :  function() {
    return (this.o  && this.o.d) ? this.o.d[4] : -1;
    },

  getMaxLength :  function() {
    return (this.o  && this.o.d) ? this.o.d[5] : -1;
    },

  getRow :  function() {
    return (this.o && this.o.d) ? this.o.d[6] : -1;
  },


  getValue :  function() {
    return (this.o && this.o.t) ? this.o.t : '';
  },

  isBlink : function(){
	 var id=this.getAttributeId();
	 return id==42 || id==43 || id==46;
  },

  getClass : function(){

      switch(this.getAttributeId()){
         case 32: return 'green';
         case 33: return 'green-rv';
         case 34: return 'white';
         case 35: return 'white-rv';
         case 36: return 'green-ul';
         case 37: return 'green-rv-ul';
         case 38: return 'white-ul';
         case 39: return 'nondisp';

         case 40: return 'red';
         case 41: return 'red-rv';
         case 42: return 'red-hi';
         case 43: return 'red-hi-rv';
         case 44: return 'red-ul';
         case 45: return 'red-ul-hi-rv';
         case 46: return 'red-ul-hi';

         case 47: return 'non-disp';
         case 48: return 'turq-cs';
         case 49: return 'turq-rv';
         case 50: return 'yellow';
         case 51: return 'yellow-rv';
         case 52: return 'turq-ul';
         case 53: return 'turq-rv-ul';
         case 54: return 'yellow-ul';
         case 55: return 'nondisp';

         case 56: return 'pink';
         case 57: return 'pink-rv';
         case 58: return 'blue';
         case 59: return 'blue-rv';
         case 60: return 'pink-ul';
         case 61: return 'pink-rv-ul';
         case 62: return 'blue-ul';

         case 63: return 'nondisp';
         default : return 'green';
       }

   },

   isFocused : function(){
	   if(this.getFieldId()>1000) return false;
	   var i = this.getFieldType();
	   return (i >>13)&1;
   },

    isAutoEnter : function(){
	   var i = this.getFieldType();
	   return (i >>12)&1;
   },

    isBypassField : function(){
	   var i = this.getFieldType();
	   return (i >>11)&1;
   },

    isContinued : function(){
	   var i = this.getFieldType();
	   return (i >>10)&1;
   },

    isContinuedFirst : function(){
	   var i = this.getFieldType();
	   return (i >>9)&1;
   },

    isContinuedLast : function(){
	   var i = this.getFieldType();
	   return (i >>8)&1;
   },

    isContinuedMiddle : function(){
	   var i = this.getFieldType();
	   return (i >>7)&1;
   },

    isDupEnabled : function(){
	   var i = this.getFieldType();
	   return (i >>6)&1;
   },

    isFER : function(){
	   var i = this.getFieldType();
	   return (i >>5)&1;
   },

    isHiglightedEntry : function(){
	   var i = this.getFieldType();
	   return (i >>4)&1;
   },

    isMandatoryEnter : function(){
	   var i = this.getFieldType();
	   return (i >>3)&1;
   },

    isNumeric : function(){
	   var i = this.getFieldType();
	   return (i >>2)&1;
   },

    isSignedNumeric : function(){
	   var i = this.getFieldType();
	   return (i>>1)&1;
   },

    isToUpper : function(){
	   var i = this.getFieldType();
	   return (i&1);
   }

});

