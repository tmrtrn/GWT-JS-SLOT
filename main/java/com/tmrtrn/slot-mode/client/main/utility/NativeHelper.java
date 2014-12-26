package com.tmrtrn.mrgood.client.main.utility;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.sinewavemultimedia.easeljs.framework.js.display.Container;


public class NativeHelper {
	
	public static native Element getMainElement()/*-{
		return $wnd.jQuery('.main')[0];
	}-*/;
	
	public static native Element getMessageElement()/*-{
		return $wnd.jQuery('.serverMessage')[0];
	}-*/;
	
	public static native void showElement(Element el)/*-{
		$wnd.jQuery(el).show();
	}-*/;
	
	public static native void setInnerHTML(Element el,String html)/*-{
	  $wnd.jQuery(el).html('<span class="txtWrapper"><span class="txt">BALANCE</span>:</span>');
	}-*/;
	
	public static native void addClass(Element el,String classname)/*-{
		
	 	$wnd.jQuery(el).addClass(classname);
	}-*/;

	public static native double getElementWidth(Element el)
	/*-{
		return $wnd.jQuery(el).width();
		
	}-*/;
	
	public static native double getElementHeight(Element el)
	/*-{
		return $wnd.jQuery(el).height();
		
	}-*/;
	
	public static native double getElementPositionLeft(Element el)
	/*-{
		return parseFloat($wnd.jQuery(el).css('left').split("px")[0]);
	}-*/;
	
	public static native void setElementPositionLeft(Element el,double left)
	/*-{
		$wnd.jQuery(el).css('left',left);
	}-*/;
	
	public static native void setFontSize(Element el, String size)
	/*-{
		$wnd.jQuery(el).css('font-size',size);
	}-*/;
	
	public static native double getFontSize(Element el)
	/*-{
		return parseFloat($wnd.jQuery(el).css('font-size').split("px")[0]);
	}-*/;
	

	
//	public static native JavaScriptObject getErrorJsonFromXML(Object xmlData,String key)
//	/*-{$wnd.alert($wnd.jQuery(xmlData).find(key).text());
//		return '{"length" :' +$wnd.jQuery(xmlData).find(key).length+',"message" :' +$wnd.jQuery(xmlData).find(key).text()+ '}';
		//$wnd.jQuery.parseJSON
//	}-*/;
	
	
	public static native String getErrorFromXML(Object xmlData,String key,boolean isHeader)
	/*-{
		if(!isHeader)
			return $wnd.jQuery(xmlData).find("data").find("errors").find(key).text();
		else
			return $wnd.jQuery(xmlData).find("data").find("errorHeader").text();
			
		}-*/;
	public static native boolean isErrorInXML(Object xmlData,String key)
	/*-{
		return $wnd.jQuery(xmlData).find("data").find("errors").find(key).length > 0;
	}-*/;
	
	public static native String getProtocol()
	/*-{
		return $wnd.protocol;
	}-*/;
	
	public static native void console(String text)
	/*-{
		console.log(text);
	}-*/;
	
	public static native void textfillByParentProp(int maxFontSizePx,int minFontSizePx,Element el,boolean bVerticalAlign)/*-{
	
 		maxFontSizePx = parseInt(maxFontSizePx, 10);
        minFontSizePx = parseInt(minFontSizePx, 10);
        if (maxFontSizePx > 0 && minFontSizePx > maxFontSizePx) {
            minFontSizePx = maxFontSizePx;
        }
       
       var parent = $wnd.jQuery(el).parent();
       var maxHeight = parent.height();
       var maxWidth = parent.width();

        var fontSize = maxFontSizePx;
        var textHeight;
        var textWidth;
            do {
                 $wnd.jQuery(el).css("fontSize",fontSize);  
                textHeight =  $wnd.jQuery(el).height();
                textWidth =  $wnd.jQuery(el).width();
                fontSize = fontSize - 1;
                
            } while ((textHeight > maxHeight || textWidth > maxWidth) && fontSize > minFontSizePx);
            
 		if(bVerticalAlign)
 		{
 			var offsetHeight = maxHeight - textHeight;
 			var position = $wnd.jQuery(el).position();
 			var newPosTop = position.top+(offsetHeight/2);
 			$wnd.jQuery(el).css({position: 'relative'});
 			$wnd.jQuery(el).css('top', newPosTop + 'px');
 			
 		}
 		
 		
	}-*/;
	
	public static native String textfill(int maxFontSizePx,int minFontSizePx,Element el,int iWidth,int iHeight,boolean bVerticalAlign)
	/*-{
		
		maxFontSizePx = parseInt(maxFontSizePx, 10);
        minFontSizePx = parseInt(minFontSizePx, 10);
        if (maxFontSizePx > 0 && minFontSizePx > maxFontSizePx) {
            minFontSizePx = maxFontSizePx;
        }
       
      
       var maxHeight = iHeight;
       var maxWidth = iWidth;

        var fontSize = maxFontSizePx;
        var textHeight;
        var textWidth;
            do {
             	$wnd.jQuery(el).css("fontSize",fontSize);
                textHeight =  $wnd.jQuery(el).height();
                textWidth =  $wnd.jQuery(el).width();
                fontSize = fontSize - 1;
                
            } while ((textHeight > maxHeight || textWidth > maxWidth) && fontSize > minFontSizePx);
            
        if(bVerticalAlign)
 		{
 			var offsetHeight = maxHeight - textHeight;
 			var position = $wnd.jQuery(el).position();
 			var newPosTop = position.top+(offsetHeight/2);
 			$wnd.jQuery(el).css({position: 'relative'});
 			$wnd.jQuery(el).css('top', newPosTop + 'px');
 			
 		}
        return fontSize.toString() ;
		
	}-*/;
	
	 public static Element getChild(Element parent, String name) {
		    for (Node child = (Node) parent.getFirstChild(); child != null; child = child.getNextSibling()) {
		      if (child instanceof Element && name.equals(child.getNodeName())) {
		        return (Element) child;
		      }
		    }
		    return null;
		  }
	 
	public static native void href(String url)
		/*-{
		 	$wnd.location.href = url;
		}-*/;
	public native static void open_new_blank(String url)
	/*-{
		
 		$wnd.open(url, "_blank");
	}-*/;
	
	public static native void createTweenHelper(Container container,Container current_strip,JavaScriptObject backgroundImage, CallbackTweenHelper cbHelper, int strip_id)
	/*-{
		$wnd.require(["jquery","underscore","TweenHelper"], function($, _,TweenHelper ){
			this.tweenHelper = new TweenHelper({
				container : container,
				current_strip : current_strip,
				backgroundImage : backgroundImage,
				strip_id : strip_id
			});
			
			cbHelper.@com.tmrtrn.mrgood.client.main.utility.CallbackTweenHelper::success(Lcom/google/gwt/core/client/JavaScriptObject;)(this.tweenHelper);
		});
	}-*/;
	

	
	public static native void spin(JavaScriptObject jsoTweenHelper,int sprite_pos_x, int sprite_pos_y, int sprite_pos_z,int strip_id)
	/*-{
		jsoTweenHelper.options.sprite_pos_x = sprite_pos_x;
		jsoTweenHelper.options.sprite_pos_y = sprite_pos_y;
		jsoTweenHelper.options.sprite_pos_z = sprite_pos_z;
		jsoTweenHelper.spin();
		
	}-*/;
	
	public static native void stopSpin(JavaScriptObject jsoHelper,int sprite_pos_x, int sprite_pos_y, int sprite_pos_z, CallbackTweenHelper cbEnd)
	/*-{
		var callbackfunc = function(tween){
			cbEnd.@com.tmrtrn.mrgood.client.main.utility.CallbackTweenHelper::success(Lcom/google/gwt/core/client/JavaScriptObject;)(this.tweenHelper);
		}	
		
		jsoHelper.options.sprite_pos_x = sprite_pos_x;
		jsoHelper.options.sprite_pos_y = sprite_pos_y;
		jsoHelper.options.sprite_pos_z = sprite_pos_z;
		jsoHelper.stopSpin(callbackfunc);
		
	}-*/;
	
	public static native int delay(int idelay,CallbackTimerEnded endedTimer)
	/*-{
		var timeoutId = $wnd.setTimeout(function(){
			endedTimer.@com.tmrtrn.mrgood.client.main.utility.CallbackTimerEnded::success()();
		},idelay);
		return timeoutId;
		
	}-*/;
	public static native void clearTimeout(int id)
	/*-{
		$wnd.clearTimeout(id);
	}-*/;
	public static native Element getLogElement()/*-{
	return $wnd.jQuery('.logWindow')[0];
}-*/;
	
	public static native void UpdateJS(int count,int duration, CallbackUpdateTween callback)
	/*-{
		
		var callbackUpdatefunc = function(counter){
			callback.@com.tmrtrn.mrgood.client.main.utility.CallbackUpdateTween::update(I)(counter);
		}	
		var callbackEndfunc = function(){
			callback.@com.tmrtrn.mrgood.client.main.utility.CallbackUpdateTween::end()();
		}	
		$wnd.require(["jquery","underscore","TweenHelper"], function($, _,TweenHelper ){
			new TweenHelper().counterTween(count, duration, callbackUpdatefunc,callbackEndfunc);
		});
		
		
	}-*/;
	
	
}
