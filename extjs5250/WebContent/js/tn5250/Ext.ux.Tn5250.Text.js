/**
 * @Description Ext.ux.Tn5250 ExtJS 2.x and 3.x; Telnet 5250 for access to IBM iSeries (AS/400) servers (java server side using DWR library)
 * @author  Tomislav Milkovic
 * @license LGPLv3 http://www.opensource.org/licenses/lgpl-3.0.html
 * @version 1.1, 01.09.2009.
 * @project_url http://code.google.com/p/extjs5250/
 */


//assign json object, and pass Renderer.ScreenElement
Ext.ux.Tn5250.Text = Ext.extend(Ext.BoxComponent, {

    onRender : function(ct, position){

        this.screenEl.setObject(this.obj);

        if(!this.el){
            this.el = document.createElement('span');
            this.el.setAttribute('row',this.screenEl.getRow());

            if (!this.screenEl.isHidden() & !(this.hidden)){            	
            	this.el.setAttribute('class',this.screenEl.getClass());
            }
            if(this.screenEl.isBreak()){
              this.el.innerHTML = '<br>' + this.htmlEncode(Ext.util.Format.htmlEncode(this.screenEl.getValue()));
            }else
              this.el.innerHTML = this.htmlEncode(Ext.util.Format.htmlEncode(this.screenEl.getValue()));
        }
        Ext.ux.Tn5250.Text.superclass.onRender.call(this, ct, position);
    },

     htmlEncode : function(value){
              return !value ? value : String(value).replace(/ /g,'&nbsp;');
        }


});

