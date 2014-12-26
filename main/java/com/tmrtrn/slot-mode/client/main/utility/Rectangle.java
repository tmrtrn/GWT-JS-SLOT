package com.tmrtrn.mrgood.client.main.utility;

import com.google.gwt.core.client.JavaScriptObject;

/*A class the represents Rectangle.js
* author: James Tyner 
*/
public class Rectangle extends JavaScriptObject {
	
	protected Rectangle(){}
	
	public final native static Rectangle create(double x,double y,double width,double height)/*-{
		return new $wnd.createjs.Rectangle(x,y,width,height);
	}-*/;
	
	public final native Rectangle clone()/*-{
		return this.clone();
	}-*/;
	
	public final native void setY(int y)/*-{
		this.y = y;
	}-*/;	

	/** 
	 * X position. 
	 * @property x
	 * @type Number
	 **/	
	public final native void setX(int x)/*-{
		this.x = x;
	}-*/;
	
	public final native int getX()/*-{
		return this.x;
	}-*/;
	
	/** 
	 * Y position. 
	 * @property y
	 * @type Number
	 **/	
	public final native int getY()/*-{
		return this.y;
	}-*/;	

	/** 
	 * Width.
	 * @property width
	 * @type Number
	 **/	
	public final native void setWidth(int width)/*-{
		this.width = width;
	}-*/;
	
	public final native int getWidth()/*-{
		return this.width;
	}-*/;

	/** 
	 * Height.
	 * @property height
	 * @type Number
	 **/	
	public final native void setHeight(int height)/*-{
		this.height = height;
	}-*/;
	
	public final native int getHeight()/*-{
		return this.height;
	}-*/;
	
}
