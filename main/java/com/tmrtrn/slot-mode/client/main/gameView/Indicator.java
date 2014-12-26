package com.tmrtrn.mrgood.client.main.gameView;

import com.sinewavemultimedia.easeljs.framework.js.display.Text;

public class Indicator {
	
	Text txt;
	public int id;
	public double dX;
	public double dY;
	public String sText;
	
	static String sFontProperty = "16px Arial Bold";
	static String sColorProperty = "#542e08";
	
	public Indicator(int id,String sText)
	{
		this.id = id;
		txt = Text.create(sText, sFontProperty, sColorProperty);
		txt.setTextAlign("center");
	}
	
	public void setX(double dX)
	{
		this.dX = dX;
		txt.setX(dX);
	}
	
	public void setY(double dY)
	{
		this.dY = dY;
		txt.setY(dY);
	}

}
