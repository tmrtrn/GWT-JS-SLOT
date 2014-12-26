package com.tmrtrn.mrgood.client.main.utility;

import java.util.ArrayList;

import com.google.gwt.resources.client.ImageResource;
import com.sinewavemultimedia.easeljs.framework.js.display.Graphics;
import com.sinewavemultimedia.easeljs.framework.js.display.Shape;
import com.tmrtrn.mrgood.client.css.MainBundle;

public class GameParameters {
	

	// Strip positions
	public static double dStripStartPositionIfTopStrip = -430;//-342;
	public static double dStripStartPositionIfMiddleStrip = 97;//118;
	public static double dStripBipMapHeight = 170;//151;
	public static double dStripBipMapWidth = 170;//151;
	public static double dStripWidthOffsetToRightStrip = 12;
	public static double dStripHeightOffsetEachBitMapOnStrip = 6;
	public static double dStripTargetDisplayerBottom = 500;//556;
	public static double dStripTargetDisplayerMiddle = 102;//130;
	public static double dStripLeftPosition = 65;//132;
	
	//Animator positions
	public static double dBorderStartLeftPosition = 154;
	public static double dBorderStartTopPosition = 117;
	public static double dBorderWidthCell = 151;
	public static double dBorderHeightCell = 151;
	public static double dBorderHeightOffset = 5;
	public static double dBorderWidthOffset = 9;
	public static double dBorderSize = 12;
	
	
	
	public static int GetRandomAttribution()
	{
		int iRetValue = 0;
		while(iRetValue == 0)
		{
			iRetValue = com.google.gwt.user.client.Random.nextInt(10);
		}
			
		
		
		return iRetValue;
	}
	
	public static int[] GetDefaultAttributions()
	{
		int[] iaRet = new int[3];
		for(int i=0; i<iaRet.length; i++)
		{
			iaRet[i] = GetRandomAttribution();
		}
		return iaRet;
	}
	
	public static int GetRandomNumber(int iMin, int iMax)
	{
		int iRetValue = -1;
		while(iRetValue < iMin )
		{
			iRetValue = com.google.gwt.user.client.Random.nextInt(iMax);
		}
		
		return iRetValue;
	}
	
	
	public static ArrayList<Integer[]> alLineData;
	
	public static void PrepareLineData()
	{
		alLineData = new ArrayList<Integer[]>();
//0 bottom 2 top
		alLineData.add( new Integer[]{1, 1, 1, 1, 1}); // Line 1
		alLineData.add( new Integer[]{2, 2, 2, 2, 2}); // Line 2
		alLineData.add( new Integer[]{0, 0, 0, 0, 0}); // Line 3
		alLineData.add( new Integer[]{2, 1, 0, 1, 2}); // Line 4
		alLineData.add( new Integer[]{0, 1, 2, 1, 0}); // Line 5
		alLineData.add( new Integer[]{2, 2, 1, 2, 2}); // Line 6
		alLineData.add( new Integer[]{0, 0, 1, 0, 0}); // Line 7
		alLineData.add( new Integer[]{0, 1, 1, 1, 0}); // Line 8
		alLineData.add( new Integer[]{2, 1, 1, 1, 2}); // Line 9
		alLineData.add( new Integer[]{1, 0, 0, 0, 1}); // Line 10
		alLineData.add( new Integer[]{1, 2, 2, 2, 1}); // Line 11
		alLineData.add( new Integer[]{2, 1, 2, 1, 2}); // Line 12
		alLineData.add( new Integer[]{0, 1, 0, 1, 0}); // Line 13
		alLineData.add( new Integer[]{1, 2, 1, 2, 1}); // Line 14
		alLineData.add( new Integer[]{1, 0, 1, 0, 1}); // Line 15
		alLineData.add( new Integer[]{1, 1, 2, 1, 1}); // Line 16
		alLineData.add( new Integer[]{1, 1, 0, 1, 1}); // Line 17
		alLineData.add( new Integer[]{2, 0, 2, 0, 2}); // Line 18
		alLineData.add( new Integer[]{0, 2, 0, 2, 0}); // Line 19
		alLineData.add( new Integer[]{1, 2, 0, 2, 1}); // Line 20
		alLineData.add( new Integer[]{1, 0, 2, 0, 1}); // Line 21
		alLineData.add( new Integer[]{2, 0, 1, 0, 2}); // Line 22
		alLineData.add( new Integer[]{0, 2, 1, 2, 0}); // Line 23
		alLineData.add( new Integer[]{2, 0, 0, 0, 2}); // Line 24
		alLineData.add( new Integer[]{0, 2, 2, 2, 0}); // Line 25
		alLineData.add( new Integer[]{2, 2, 0, 2, 2}); // Line 26
		alLineData.add( new Integer[]{0, 0, 2, 0, 0}); // Line 27
		alLineData.add( new Integer[]{2, 1, 0, 0, 0}); // Line 28
		alLineData.add( new Integer[]{0, 1, 2, 2, 2}); // Line 29
		alLineData.add( new Integer[]{1, 1, 0, 0, 1}); // Line 30
	}
	
	
}

