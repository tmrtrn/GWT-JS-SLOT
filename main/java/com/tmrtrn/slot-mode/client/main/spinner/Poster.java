package com.tmrtrn.mrgood.client.main.spinner;

import com.google.gwt.core.shared.GWT;
import com.sinewavemultimedia.easeljs.framework.js.display.Container;
import com.tmrtrn.mrgood.client.main.Activity;
import com.tmrtrn.mrgood.client.main.animationtool.AnimationComponent;
import com.tmrtrn.mrgood.client.main.rpc.DataStorage;
import com.tmrtrn.mrgood.client.main.rpc.Payout;

public class Poster {

	public int iAttributionId;
	
	public int iColumnId, iRowId;
	public double dX;
	public double dY;
	public double dWidth;
	public double dHeight;
	public AnimationComponent animComponent;
	public int iPositionIdOnSprite = 0;
	public String sDescription;
	public Payout payout = null;
	public boolean isWild = false;
	public Container containerBitmap;
	
	public Poster()
	{
		
	}
	
	public Poster(int iRowId,int iColumnId)
	{
		this.iRowId = iRowId;
		this.iColumnId = iColumnId;
	}
	public void setAttribution(int iAttributionId)
	{
		
		this.iAttributionId = iAttributionId;
		setIconPositionIdOnSpriteSheet();
		if(iAttributionId == 9)
		{
			isWild = true;
			return;
		}
		if(iAttributionId == 10 || iAttributionId == 11)
		{
			return;
		}
			
		
		for(Payout p : DataStorage.get().alPayouts)
		{
			if(Integer.parseInt(p.sKey) == iAttributionId)
			{
				
				payout = p;
				break;
			}
		}
	}
	
	public int getAttributionId()
	{
		return iAttributionId;
	}
	
	public void setIconPositionIdOnSpriteSheet()
	{
		if(iAttributionId == -1)
			return;
		if(iAttributionId == 2)
		{
			sDescription = "Shield";
			iPositionIdOnSprite = 1;
		}
		if(iAttributionId == 3)
		{
			sDescription = "Arrow in target";
			iPositionIdOnSprite = 8;
		}
		if(iAttributionId == 4)
		{
			sDescription = "Bag of money";
			iPositionIdOnSprite = 5;
		}
		if(iAttributionId == 5)
		{
			sDescription = "Wine";
			iPositionIdOnSprite = 0;
		}
		if(iAttributionId == 6)
		{
			sDescription = "Arrow";
			iPositionIdOnSprite = 9;
		}
		if(iAttributionId == 7)
		{
			sDescription = "Castle";
			iPositionIdOnSprite = 7;
		}
		if(iAttributionId == 8)
		{
			sDescription = "Guard";
			iPositionIdOnSprite = 2;
		}
		if(iAttributionId == 9)
		{
			sDescription = "King";
			iPositionIdOnSprite = 6;
		}
		if(iAttributionId == 10)
		{
			sDescription = "Mr Good";
			iPositionIdOnSprite = 4;
		}
		if(iAttributionId == 11)
		{
			// Click Me
			sDescription = "Wanted poster";
			iPositionIdOnSprite = 3;
		}
	}
	
	public static int getSpritePositionByAttributionId(int iAttributionId)
	{
		int iPositionIdOnSprite = 0;
		
		if(iAttributionId == -1)
			return 0;
		if(iAttributionId == 2)
		{
			iPositionIdOnSprite = 1;
		}
		if(iAttributionId == 3)
		{
			iPositionIdOnSprite = 8;
		}
		if(iAttributionId == 4)
		{
			iPositionIdOnSprite = 5;
		}
		if(iAttributionId == 5)
		{
			iPositionIdOnSprite = 0;
		}
		if(iAttributionId == 6)
		{
			iPositionIdOnSprite = 9;
		}
		if(iAttributionId == 7)
		{
			iPositionIdOnSprite = 7;
		}
		if(iAttributionId == 8)
		{
			iPositionIdOnSprite = 2;
		}
		if(iAttributionId == 9)
		{
			iPositionIdOnSprite = 6;
		}
		if(iAttributionId == 10)
		{
			iPositionIdOnSprite = 4;
		}
		if(iAttributionId == 11)
		{
		
			iPositionIdOnSprite = 3;
		}
		return iPositionIdOnSprite;
	}
	
	
	public int getMinPayout()
	{
		int iMin = -1;
		if(payout != null)
		{
			iMin = payout.alKeyValues.get(0);
		}
		else
		{
			
		}
		
		return iMin;
	}
	
	public int getWinlineMultiplayer(int iTotalIconOnWinline)
	{
		int iWinlineMultplayer = -1;
		int iMinEqual = getMinPayout();
		if(iTotalIconOnWinline < iMinEqual || iMinEqual == -1)
			return iWinlineMultplayer;
		
		int i=0;
		for(int iKey :payout.alKeyValues)
		{
			if(iKey == iTotalIconOnWinline)
			{
				iWinlineMultplayer = payout.alDataValues.get(i);
				break;
			}
			i += 1;
		}
		
		return iWinlineMultplayer;
	}
}
