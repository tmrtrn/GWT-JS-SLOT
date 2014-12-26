package com.tmrtrn.mrgood.client.main.gameView;

import java.util.ArrayList;

import com.sinewavemultimedia.easeljs.framework.js.display.Text;
import com.tmrtrn.mrgood.client.main.MrgoodController;
import com.tmrtrn.mrgood.client.main.rpc.DataStorage;

public class LinebetIndicatorControl {
	
	ArrayList<Indicator> indicators;
	
	
	public LinebetIndicatorControl()
	{
		
		indicators = new ArrayList<Indicator>();
	}
	
	String sFontProperty = "16px Arial Bold";
	String sColorProperty = "#542e08";
	
	public void createIndicatorElements()
	{
		
		int start_left = 45;
		int start_right = 983;
		int x_offset_left = 0;
		int y_offset_left = 0;
		
		int x_offset_right = 0;
		int y_offset_right = 0;
		int iLine = DataStorage.get().lines_bet;
		
		Indicator indicator_1 = new Indicator(1,"22"); //16.
		indicator_1.setX(start_left - x_offset_left);
		indicator_1.setY(101 - y_offset_left);
		indicators.add(indicator_1);

		
		Indicator indicator_2 = new Indicator(2,"4"); //17.
		indicator_2.setX(start_left - x_offset_left-1);
		indicator_2.setY(137 - y_offset_left);
		indicators.add(indicator_2);

		Indicator indicator_3 = new Indicator(3,"2"); //18.
		indicator_3.setX(start_left - x_offset_left - 0.5);
		indicator_3.setY(172 - y_offset_left);
		indicators.add(indicator_3);
		
		Indicator indicator_4 = new Indicator(4,"12"); //19.
		indicator_4.setX(start_left - x_offset_left -1);
		indicator_4.setY(207.5 - y_offset_left);
		indicators.add(indicator_4);
		
		Indicator indicator_5 = new Indicator(5,"24"); 
		indicator_5.setX(start_left - x_offset_left-0.5 );
		indicator_5.setY(243 - y_offset_left);
		indicators.add(indicator_5);

		Indicator indicator_6 = new Indicator(6,"14"); 
		indicator_6.setX(start_left - x_offset_left -1 );
		indicator_6.setY(279 - y_offset_left);
		indicators.add(indicator_6);
		
		Indicator indicator_7 = new Indicator(7,"11");  //22.
		indicator_7.setX(start_left - x_offset_left);
		indicator_7.setY(311.5 - y_offset_left);
		indicators.add(indicator_7);
		
		Indicator indicator_8 = new Indicator(8,"1");  //23.
		indicator_8.setX(start_left - x_offset_left -1 );
		indicator_8.setY(345.8 - y_offset_left);
		indicators.add(indicator_8);
		
		Indicator indicator_9 = new Indicator(9,"15");  //24.
		indicator_9.setX(start_left - x_offset_left -1 );
		indicator_9.setY(381 - y_offset_left);
		indicators.add(indicator_9);

		Indicator indicator_10 = new Indicator(10,"10");  //25.
		indicator_10.setX(start_left - x_offset_left -1 );
		indicator_10.setY(416 - y_offset_left);
		indicators.add(indicator_10);
		
		Indicator indicator_11 = new Indicator(11,"23");  //26.
		indicator_11.setX(start_left - x_offset_left );
		indicator_11.setY(453 - y_offset_left);
		indicators.add(indicator_11);
		
		Indicator indicator_12 = new Indicator(12,"5");  //27.
		indicator_12.setX(start_left - x_offset_left );
		indicator_12.setY(488.5 - y_offset_left);
		indicators.add(indicator_12);
		
		Indicator indicator_13 = new Indicator(13,"3");  //28.
		indicator_13.setX(start_left - x_offset_left );
		indicator_13.setY(524 - y_offset_left);
		indicators.add(indicator_13);
		
		Indicator indicator_14 = new Indicator(14,"13");  //29.
		indicator_14.setX(start_left - x_offset_left );
		indicator_14.setY(560.5 - y_offset_left);
		indicators.add(indicator_14);
		
		Indicator indicator_15 = new Indicator(15,"25"); 
		indicator_15.setX(start_left - x_offset_left);
		indicator_15.setY(596 - y_offset_left);
		indicators.add(indicator_15);
	
		
		
		
		
		Indicator indicator_16 = new Indicator(16,"26"); //1
		indicator_16.setX(start_right - x_offset_right);
		indicator_16.setY(92.5 - y_offset_right);
		indicators.add(indicator_16);
		
		Indicator indicator_17 = new Indicator(17,"9"); //2
		indicator_17.setX(start_right - x_offset_right);
		indicator_17.setY(125 - y_offset_right);
		indicators.add(indicator_17);
		
		Indicator indicator_18 = new Indicator(18,"6"); //3
		indicator_18.setX(start_right - x_offset_right);
		indicator_18.setY(165 - y_offset_right);
		indicators.add(indicator_18);
		
		Indicator indicator_19 = new Indicator(19,"18"); //4
		indicator_19.setX(start_right - x_offset_right);
		indicator_19.setY(199 - y_offset_right);
		indicators.add(indicator_19);
		
		Indicator indicator_20 = new Indicator(20,"29"); 
		indicator_20.setX(start_right - x_offset_right+0.5);
		indicator_20.setY(233.5 - y_offset_right);
		indicators.add(indicator_20);		
		
		Indicator indicator_21 = new Indicator(21,"20"); //6
		indicator_21.setX(start_right - x_offset_right+0.5);
		indicator_21.setY(269.5 - y_offset_right);
		indicators.add(indicator_21);
		
		Indicator indicator_22 = new Indicator(22,"16"); //7
		indicator_22.setX(start_right - x_offset_right-1);
		indicator_22.setY(307.5 - y_offset_right);
		indicators.add(indicator_22);
		
		Indicator indicator_23 = new Indicator(23,"30"); //8
		indicator_23.setX(start_right - x_offset_right);
		indicator_23.setY(347 - y_offset_right);
		indicators.add(indicator_23);
	
		Indicator indicator_24 = new Indicator(24,"17"); //9
		indicator_24.setX(start_right - x_offset_right);
		indicator_24.setY(385 - y_offset_right);
		indicators.add(indicator_24);
		
		Indicator indicator_25 = new Indicator(25,"21"); //10
		indicator_25.setX(start_right - x_offset_right+1);
		indicator_25.setY(422 - y_offset_right);
		indicators.add(indicator_25);
	
		Indicator indicator_26 = new Indicator(26,"27"); //11
		indicator_26.setX(start_right - x_offset_right +0.5);
		indicator_26.setY(457.5 - y_offset_right);
		indicators.add(indicator_26);
		
		Indicator indicator_27 = new Indicator(27,"8"); //12
		indicator_27.setX(start_right - x_offset_right);
		indicator_27.setY(492 - y_offset_right);
		indicators.add(indicator_27);
		
		Indicator indicator_28 = new Indicator(28,"7"); //13
		indicator_28.setX(start_right - x_offset_right);
		indicator_28.setY(525.5 - y_offset_right);
		indicators.add(indicator_28);
		
		Indicator indicator_29 = new Indicator(29,"19"); //14
		indicator_29.setX(start_right - x_offset_right);
		indicator_29.setY(560.5 - y_offset_right);
		indicators.add(indicator_29);
		
		
		Indicator indicator_30 = new Indicator(30,"28"); 
		indicator_30.setX(start_right - x_offset_right);
		indicator_30.setY(598.5 - y_offset_right);
		indicators.add(indicator_30);
		
	
		
		
		
		
		
	
		
		for(int i=0; i <indicators.size(); i++)
		{
			MrgoodController.getInstance().gameView.stage.addChild(indicators.get(i).txt);
		}
		
	}

	
	public void updateIndicatorTexts()
	{
		/*
		int iLine = 1;//DataStorage.get().lines_bet;
		for(int i=0; i<txtarrays.size(); i++)
		{
			Text txt = txtarrays.get(i);
			txt.setText(""+iLine);
		}
		*/
	}
}
