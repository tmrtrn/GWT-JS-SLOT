package com.tmrtrn.mrgood.client.main;





import java.io.Console;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LinkElement;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;
import com.tmrtrn.mrgood.client.css.MainBundle;
import com.tmrtrn.mrgood.client.main.MessageView.MessageType;
import com.tmrtrn.mrgood.client.main.rpc.CallbackAction;
import com.tmrtrn.mrgood.client.main.rpc.CallbackJSONRequest;
import com.tmrtrn.mrgood.client.main.rpc.Connection;
import com.tmrtrn.mrgood.client.main.rpc.DataStorage;
import com.tmrtrn.mrgood.client.main.sounds.AudioSprite;
import com.tmrtrn.mrgood.client.main.tools.CallbackButton;
import com.tmrtrn.mrgood.client.main.utility.NativeHelper;

public class Activity implements EntryPoint{

	
	static final String upgradeMessage = "Your browser does not support the HTML5 Canvas. Please upgrade your browser to view this game";
	static FlexTable flextable;
	
	
	@Override
	public void onModuleLoad() {
		
		flextable = new FlexTable();
		flextable.addStyleName("LogPanel");
		RootPanel.get().add(flextable);
		
		//StyleInjector.inject(MainBundle.INSTANCE.cssDefault().getText());
		String baseUrl = Window.Location.getParameter("media");
		if (baseUrl == null)
			baseUrl = "";
		loadCss(baseUrl);
		
		String sLocale = findLocaleName();
		DataStorage.get().locale = sLocale;
		loadLang(this,sLocale);
		
		GWT.log("locale : "+DataStorage.get().locale);
	}
	
	/** Load CSS file from url */
	public static void loadCss(String url){
	    LinkElement link = Document.get().createLinkElement();
	    link.setRel("stylesheet");
	    link.setHref(url+"/DefaultStyle.css");
	    nativeAttachToHead(link);
	}

	/**
	 * Attach element to head
	 */
	protected static native void nativeAttachToHead(JavaScriptObject scriptElement) /*-{
	    $doc.getElementsByTagName("head")[0].appendChild(scriptElement);
	}-*/;
	
	private void init(JavaScriptObject jsoLocale)
	{

		DataStorage.get().jsoLocale = jsoLocale;
		
		
		if(!checkBrowser(this))
			return;
		
		final Connection conn = MrgoodController.getInstance().getConnection();
		boolean bSuccess = conn.authorize();
		
		if(!bSuccess)
		{
			return;
		}
		
		String errorXmlUri = conn.isOffline() ? "http://127.0.0.1:8888/Default/Slots/xml/" : conn.rpc.url +"Default/Slots/xml/";
		
		if(!conn.isOffline())
		{
			loadErrorXML(errorXmlUri,DataStorage.get().locale, new CallbackJSONRequest() {
				
				@Override
				public void success(JavaScriptObject jsoJSON) {
					DataStorage.get().errorXml = jsoJSON;
				//	MessageView mw = new MessageView("", "SessionExpired", null, null, MessageType.Error);
				//	mw.render(NativeHelper.getMessageElement());
				}
			});
		}
				
		
		
		conn.call("gameinit", new CallbackAction() {
			
			@Override
			public void success() {
				
				conn.call("startup", new CallbackAction() {
					
					@Override
					public void success() {
						//Show Preloader
						GWT.log("startup ok");
						
						String url = conn.rpc.url +"js/messaging.js";
						String hash = conn.rpc.getParameter("hash");
						boolean startMessagingListener  = Window.Location.getQueryString().indexOf("poll_message_interval") >= 0;
						if(startMessagingListener)
							startReality_check(url,conn.rpc.url,hash,Double.parseDouble(Window.Location.getParameter("poll_message_interval")));
						MrgoodController.getInstance().showLoadingView();
					}
				});
			}
		});
		
	}
	
	private native void startReality_check(String sUrl,String baseUrl, String sHash, double pool_message_interval)
	/*-{
		if(pool_message_interval <= 0)
			return;
//http://'+location.hostname + '/js/messaging.js?a=42
		$wnd.require([location.protocol+'//'+location.hostname + '/js/messaging.js?a=42'], function(data){
			$wnd.Messaging.init({hash: sHash,poll_interval: pool_message_interval, host:baseUrl ,debug:false});
			$wnd.Messaging.startListening();
		});

//		$wnd.jQuery.getScript(sUrl, function(data, textStatus, jqxhr) {
//			$wnd.alert(data);
//   				$wnd.Messaging.init({hash: sHash,poll_interval: pool_message_interval, host:baseUrl ,debug:true});
//   				$wnd.Messaging.startListening();
//		});

	}-*/;
	
	private native String findLocaleName()
	/*-{
		var locale = $wnd.location.search.match(/locale=([^&]*)/);
		var name = locale ? locale[1] : 'en_US';
		
		return name;
		
	}-*/;
	
	private native void loadErrorXML(String url,String langName,CallbackJSONRequest cb)
	/*-{


		$wnd.jQuery.ajax({
			type     : "GET",
			url      : url + langName+".xml",
			dataType : "xml",
			success  : function(xmlData){
			var totalNodes = $wnd.jQuery('*',xmlData).length; // count XML nodes
			//$wnd.alert("This XML file has " + totalNodes );
			
		//	$wnd.alert("This XML " + $wnd.jQuery(xmlData).find("data").find("errors").find("SessionExpired").text() );
			cb.@com.tmrtrn.mrgood.client.main.rpc.CallbackJSONRequest::success(Lcom/google/gwt/core/client/JavaScriptObject;)(xmlData);
			},
			error    : function(){
     		//	$wnd.alert("Could not retrieve XML file.");
			}
 		});
	}-*/;
	
	private native void loadLang(Activity x,String name)
	/*-{
	    var locale;
	    
		$wnd.require(['text!resources/lang/' + name + '.json'], function(data){				
			 locale = eval('(' + data + ')');				
				$wnd.require(['text!resources/lang/custom/' + name + '.json?a=42'], function(data){		
					data = eval('(' + data + ')');
					for(var p in data)
					{
						locale[p] = data[p];
						//$wnd.alert(locale[p]);
					} 
						
					
					//$wnd.alert(locale['BRW_CHECK_COMMON']);
					x.@com.tmrtrn.mrgood.client.main.Activity::init(Lcom/google/gwt/core/client/JavaScriptObject;)(locale);
				});
		});
		
		
	}-*/;
	
	private final native boolean checkBrowser(Activity x)
	/*-{ 
			var error = null;
			if (navigator.userAgent.search("Android") !== -1) {
				
				if (navigator.userAgent.search("Android 4") === -1) { //only android 4
					
					x.@com.tmrtrn.mrgood.client.main.Activity::showCheckBrowserMessage(Ljava/lang/String;)("BRW_CHECK_ANDROID_OLD");
					return false;
				}
				else if(navigator.userAgent.search("GT-I9100") !== -1) { //s2 - native
					if(navigator.userAgent.search("Chrome") !== -1)
						x.@com.tmrtrn.mrgood.client.main.Activity::showCheckBrowserMessage(Ljava/lang/String;)("BRW_CHECK_ANDROID_S2");
						return false;
				}
				else if (navigator.userAgent.search("Chrome") === -1) { //and still disable for other than chrome (and not s2)
					x.@com.tmrtrn.mrgood.client.main.Activity::showCheckBrowserMessageWithInstallChrome(Ljava/lang/String;)("BRW_CHECK_ANDROID_NSAFARI");
					return false;
				}
			
				return true;	
			}
			else if(navigator.userAgent.search("iPad") !== -1 || navigator.userAgent.search("iPod") !== -1 || navigator.userAgent.search("iPhone") !== -1){
				if((navigator.userAgent.search("OS 5") === -1) && (navigator.userAgent.search("OS 6") === -1) && (navigator.userAgent.search("OS 7") === -1))
				{
					x.@com.tmrtrn.mrgood.client.main.Activity::showCheckBrowserMessage(Ljava/lang/String;)("BRW_CHECK_IOS_OLD");
					return false;
				}			
				else if(navigator.userAgent.match('CriOS') != null) //chrome
				{
					x.@com.tmrtrn.mrgood.client.main.Activity::showCheckBrowserMessage(Ljava/lang/String;)("BRW_CHECK_IOS_NSAFARI");
					return false;
				}
					
				return true;	
			}
			
			else { //desktop
					
				x.@com.tmrtrn.mrgood.client.main.Activity::showCheckBrowserMessage(Ljava/lang/String;)("BRW_CHECK_COMMON");
				return false;
			//	return true;
			}
	}-*/; 
	
	private void showCheckBrowserMessage(String statusmessage)
	{
		MessageView mw = new MessageView("", statusmessage, new CallbackButton() {
			
			@Override
			public void pressed() {
				
				historyback();
			}
		}, null, MessageType.Error);
		mw.setFirstButtonName("BACK");
		mw.render(NativeHelper.getMessageElement());
	}
	
	private void showCheckBrowserMessageWithInstallChrome(String statusmessage)
	{
		MessageView mw = new MessageView("", statusmessage, new CallbackButton() {
			
			@Override
			public void pressed() {
				
				goGooglePlay();
			}
		}, new CallbackButton() {
			
			@Override
			public void pressed() {
				
				historyback();
			}
		}, MessageType.Error);
		mw.setFirstButtonName("INSTALL");
		mw.setSecondButtonName("BACK");
		mw.render(NativeHelper.getMessageElement());
	}
	
	
	private final native boolean historyback()
	/*-{ 
		history.back();
	}-*/; 
	
	private final native boolean goGooglePlay()
	/*-{ 
		$wnd.location.href = 'https://play.google.com/store/apps/details?id=com.android.chrome';
	}-*/; 
	
	public static  void AddLogMessage(String sMessage)
	{
		
	/*	if(flextable.getRowCount() > 100)
		{
			flextable.removeRow(10);
		}
		
		for(int i=flextable.getRowCount()-1; i>-1; i--)
		{
			flextable.setWidget(i+1,0, flextable.getWidget(i, 0));
		}
		flextable.setWidget(0, 0, new Label(sMessage));   
		     */                      
	}
	
	
	
}


