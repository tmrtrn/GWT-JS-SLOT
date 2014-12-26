package com.tmrtrn.mrgood.client.css;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;


public interface MainBundle extends ClientBundle{
	
	

	
	@Source("TabletStyle.css")
	TextResource cssTablet();
	
	@Source("IphoneStyle.css")
	TextResource cssIphone();
	
	@Source("DefaultStyle.css")
	TextResource cssDefault();

	public static final MainBundle INSTANCE = GWT.create(MainBundle.class);
	
	

}
