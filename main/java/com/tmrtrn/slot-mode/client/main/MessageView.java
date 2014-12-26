package com.tmrtrn.mrgood.client.main;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.typedarrays.server.Int8ArrayImpl;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.tmrtrn.mrgood.client.css.MainBundle;
import com.tmrtrn.mrgood.client.main.rpc.DataStorage;
import com.tmrtrn.mrgood.client.main.rpc.JsonAnalyzer;
import com.tmrtrn.mrgood.client.main.tools.CallbackButton;
import com.tmrtrn.mrgood.client.main.tools.GameButton;
import com.tmrtrn.mrgood.client.main.utility.CallbackUtilityTimer;
import com.tmrtrn.mrgood.client.main.utility.NativeHelper;
import com.tmrtrn.mrgood.client.main.utility.Utility;

public class MessageView {
	
	String sTitle;
	String sBody;
	CallbackButton btn1;
	CallbackButton btn2;
	MessageType type;
	String sButton_1_name = "OK";
	String sButton_2_name = "Cancel";
	
	JavaScriptObject clickedHandler_1;
	JavaScriptObject clickedHandler_2;
	
	public enum MessageType
	{
		Error,
		Warning,
		Info,
		Bank
	}
	
	public MessageView(String sTitle, String sBody, CallbackButton btn1, CallbackButton btn2,
			MessageType type)
	{
		
		if(writeErrorFromXML(sBody))
		{
			
		}
		else
		{
			this.sTitle = translate(sTitle,DataStorage.get().jsoLocale);
			this.sBody = translate(sBody,DataStorage.get().jsoLocale);
		}
		
		
		
		this.btn1 = btn1;
		this.btn2 = btn2;
		this.type = type;
	}
	private native String translate(String sKey,JavaScriptObject jsoLang)
	/*-{
		
			var translated = jsoLang[sKey] ? jsoLang[sKey] : sKey;
			
			return translated;
	}-*/;
	
	public void setFirstButtonName(String sButton_1_name)
	{
		this.sButton_1_name = translate(sButton_1_name,DataStorage.get().jsoLocale);
	}
	
	public void setSecondButtonName(String sButton_2_name)
	{
		this.sButton_2_name = translate(sButton_2_name,DataStorage.get().jsoLocale);
	}
	
	
	Element el_bgWrapper;
	Element el_serverMsgCnt;
	
	public void render(final Element elRootMessage)
	{
		

		if(elRootMessage.getClassName().contains(MessageType.Error.toString()))
		{
			return;
		}
		
		GWT.log("message render");
		elRootMessage.addClassName(type.toString());
	//	elRootMessage.setInnerHTML("<div class=\"serverMsgCnt\"><div class=\"title bold\"></div><div class=\"body\"></div></div><div class=\"bgWrapper\"><div class=\"bgTop\"></div><div class=\"bgMiddle\"></div><div class=\"bgBottom\"></div></div>");
		el_serverMsgCnt = Document.get().createDivElement();
		el_serverMsgCnt.setClassName("serverMsgCnt");
		elRootMessage.appendChild(el_serverMsgCnt);

		
		Element el_title = Document.get().createDivElement();
		el_title.addClassName("title bold");
		el_serverMsgCnt.appendChild(el_title);
		Element el_body = Document.get().createDivElement();
		el_body.addClassName("body");
		el_serverMsgCnt.appendChild(el_body);
		
		
		el_bgWrapper = Document.get().createDivElement();
		el_bgWrapper.addClassName("bgWrapper");
		elRootMessage.appendChild(el_bgWrapper);
		
		Element el_bgTop = Document.get().createDivElement();
		el_bgTop.addClassName("bgTop");
		el_bgWrapper.appendChild(el_bgTop);
		Element el_bgMiddle = Document.get().createDivElement();
		el_bgMiddle.addClassName("bgMiddle");
		el_bgWrapper.appendChild(el_bgMiddle);
		Element el_bgBottom = Document.get().createDivElement();
		el_bgBottom.addClassName("bgBottom");
		el_bgWrapper.appendChild(el_bgBottom);
		
	
		el_title.setInnerHTML(sTitle);
		el_body.setInnerHTML(sBody);
		
		
		
		if(btn1!= null)
		{
			insertButton(this,elRootMessage, sButton_1_name,true,type.toString());
			
		}
		if(btn2 != null)
		{
			insertButton(this,elRootMessage, sButton_2_name,false,type.toString());
		}
		

		Utility.WaitForSeconds(50, new CallbackUtilityTimer() {
			
			@Override
			public void onCompletedWaiting() {
				
				resizeView(elRootMessage);
			}
		});
		
	
	
	}
	
	public void destroy()
	{
		el_bgWrapper.removeFromParent();
		el_serverMsgCnt.removeFromParent();
	}
	
	@Deprecated
	public void removeClickListener(boolean isFirstButton)
	{
		if(clickedHandler_1 != null && isFirstButton)
		{
			removeClickListener(true,clickedHandler_1);
			clickedHandler_1 = null;
		}
		else if(clickedHandler_2 != null && !isFirstButton)
		{
			removeClickListener(false,clickedHandler_2);
			clickedHandler_2 = null;
		}
			
	}
	
	
	private native Element insertButton(MessageView x,Element el,String btn,boolean isFirst,String sType)/*-{
	
		$wnd.jQuery(el).find('.serverMsgCnt').append('<a class="messageBtn"><span>' + btn + '</span></a>');
		$wnd.jQuery($wnd.jQuery(el).find('.messageBtn')[0]).addClass(sType);
		@com.tmrtrn.mrgood.client.main.utility.NativeHelper::textfill(IILcom/google/gwt/dom/client/Element;IIZ)(30,10,$wnd.jQuery(el).find('.serverMsgCnt .messageBtn').find("span"),120,40,false);
		
		var $btn;
		if(isFirst)
		{
			$btn = $wnd.jQuery($wnd.jQuery(el).find('.messageBtn')[0]);
			
			var clicked = function(event)
			{
				x.@com.tmrtrn.mrgood.client.main.MessageView::receivedClick_button_1()();
			};
	    
		 	$wnd.jQuery($wnd.jQuery(el).find('.messageBtn')[0]).bind("click", clicked);
		 	$wnd.jQuery($wnd.jQuery(el).find('.messageBtn')[0]).bind("touchstart", clicked);
		 	 x.@com.tmrtrn.mrgood.client.main.MessageView::clickedHandler_1 = clicked;
		}
		else
		{
			$btn = $wnd.jQuery($wnd.jQuery(el).find('.messageBtn')[1]);
			$wnd.jQuery(el).find('.serverMsgCnt .messageBtn').attr('style', 'display:inline-block;');
			$wnd.jQuery($wnd.jQuery(el).find('.messageBtn')[1]).addClass(sType+" cancel");
			
			var clicked = function(event)
			{
				x.@com.tmrtrn.mrgood.client.main.MessageView::receivedClick_button_2()();
			};
	    
		 	$wnd.jQuery($wnd.jQuery(el).find('.messageBtn')[1]).bind("click", clicked);
		 	$wnd.jQuery($wnd.jQuery(el).find('.messageBtn')[1]).bind("touchstart", clicked);
		 	 x.@com.tmrtrn.mrgood.client.main.MessageView::clickedHandler_2 = clicked;
		}
		return $btn;
		
	}-*/;
	
	private final void receivedClick_button_1()
	{
		if(btn1 != null)
			btn1.pressed();
	}
	private final void receivedClick_button_2()
	{
		if(btn2 != null)
			btn2.pressed();
	}
	
	private final native void removeClickListener(boolean isFirstButton, JavaScriptObject clickedHandler) 
	/*-{ 
		if(isFirstButton)	
		{
			$wnd.jQuery($wnd.jQuery(el).find('.messageBtn')[0]).unbind('click');
		 	$wnd.jQuery($wnd.jQuery(el).find('.messageBtn')[0]).unbind('touchstart');
		}
		else
		{
			$wnd.jQuery($wnd.jQuery(el).find('.messageBtn')[1]).unbind("click", clickedHandler);
		 	$wnd.jQuery($wnd.jQuery(el).find('.messageBtn')[1]).unbind("touchstart", clickedHandler);
		}
		 
	}-*/; 
	
	private boolean writeErrorFromXML(String key)
	{
		
		if(DataStorage.get().errorXml == null)
			return false;
		try{
			if(NativeHelper.isErrorInXML(DataStorage.get().errorXml, key))
			{
				this.sBody = NativeHelper.getErrorFromXML(DataStorage.get().errorXml, key, false);
				this.sTitle = NativeHelper.getErrorFromXML(DataStorage.get().errorXml, "", true);
				return true;
			}
			return false; 
			
			/*				Object dataJson = NativeHelper.getErrorJsonFromXML(DataStorage.get().errorXml, "data");
			
			Object errorHeaderJson = NativeHelper.getErrorJsonFromXML(dataJson, "errorHeader");
			if(JsonAnalyzer.getJsonDataTypeofInteger((JavaScriptObject) errorHeaderJson, "length") > 0)
				header = JsonAnalyzer.getJsonDataTypeofString((JavaScriptObject) errorHeaderJson, "message");
			Object errorsJson = NativeHelper.getErrorJsonFromXML(dataJson, "errors");
			Object errorJson = NativeHelper.getErrorJsonFromXML(errorsJson, key);
			if(JsonAnalyzer.getJsonDataTypeofInteger((JavaScriptObject) errorJson, "length") > 0)
			{
				message = JsonAnalyzer.getJsonDataTypeofString((JavaScriptObject) errorJson, "message");
				return true;
			}
			return false;
			*/
			
		}
		catch(Exception ex){
			return false;
		}
	}
	
	
	public native Element findTitleElement(Element el)/*-{
		
		return $wnd.jQuery($wnd.jQuery(el).find('.title'));
	}-*/;
	
	public native Element findBodylement(Element el)/*-{
		return  $wnd.jQuery(el).find('.body');
	}-*/;
	
	private native void showElement(Element el)/*-{
		$wnd.jQuery(el).show();
	}-*/;
	
	private native void resizeView(Element el)/*-{
		
		$wnd.jQuery(el).find('.bgMiddle').css('height',parseInt($wnd.jQuery(el).find('.bgMiddle').css('height')) + parseInt($wnd.jQuery(el).find('.body').css('height')));
	 
	}-*/;
	
	// $wnd.alert($wnd.jQuery(el).find('.body'));
}


