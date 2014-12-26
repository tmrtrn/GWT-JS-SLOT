package com.tmrtrn.mrgood.client.main;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Random;
import com.tmrtrn.mrgood.client.main.animationtool.AnimationSquence;
import com.tmrtrn.mrgood.client.main.gameView.GuiAnimationCallback;
import com.tmrtrn.mrgood.client.main.rpc.DataStorage;
import com.tmrtrn.mrgood.client.main.spinner.Poster;
import com.tmrtrn.mrgood.client.main.spinner.Strip;
import com.tmrtrn.mrgood.client.main.utility.GameParameters;
import com.tmrtrn.mrgood.client.main.utility.NativeHelper;

public class GameStatics {
	
	
	
	
	public static int iScoreBonus = 0;
	private static int iSpinCount = 0;
	private static GameRegistery lastSpinRegistery;
	public static boolean isEnableAnimations = false;
	
	
	
	 
	
	public static void setScoreBalance(boolean bPlayAnimation,final GuiAnimationCallback endCallback)
	{
		
		if(!bPlayAnimation)
			return;
	//		UpdateBalanceScore();
		else
		{
			if(lastSpinRegistery.bYouWonAnimationShowed || lastSpinRegistery.iWinSum <= 0)
			{
				if(endCallback != null)
					endCallback.success();
				return;
			}
				
			lastSpinRegistery.bYouWonAnimationShowed = true;
			GuiAnimationCallback callback = new GuiAnimationCallback() {
				
				@Override
				public void success() {
				//	UpdateBalanceScore();
					if(endCallback != null)
					{
						endCallback.success();
					}
				}
			};
			MrgoodController.getInstance().gameView.guiGameView.playYouWonBarAnimation(600,DataStorage.get().iWin,callback);//lastSpinRegistery.iWinSum
		}
	}
	
	/*public static void UpdateBalanceScore()
	{
		
		GWT.log("UpdateBalanceScore request "+lastSpinRegistery.iWinSum);
		if(!lastSpinRegistery.bAddedTotalScoreThisSpin)
		{
			
			lastSpinRegistery.bAddedTotalScoreThisSpin = true;
		//	DataStorage.get().dBalanceValue += lastSpinRegistery.iWinSum;
			MrgoodController.getInstance().gameView.guiGameView.textBalanceCountOnScoreTable.setText(""+((int)DataStorage.get().dBalanceValue));
			MrgoodController.getInstance().gameView.guiGameView.textBalanceCountOnBottomTextPanel.setText("\u20AC"+(int)DataStorage.get().dBalanceValue);
			GWT.log("UpdateBalanceScore updated");
		}
	} */
	
	

	
	
	public static ArrayList<AnimationSquence> FindPostersOnWinLine(ArrayList<Integer> alWinLines, Strip[] stripsTarget)
	{
		ArrayList<AnimationSquence> animsSquence = new ArrayList<AnimationSquence>();
		
		for(int iWinLineIdPointer=0; iWinLineIdPointer<alWinLines.size(); iWinLineIdPointer++)
		{
			int iWinLine = alWinLines.get(iWinLineIdPointer);
			AnimationSquence animSquenceOnWinLine = new AnimationSquence(iWinLine,iWinLineIdPointer);
			Integer[] iaCheckingLineData = GameParameters.alLineData.get(iWinLine);
			int iCheckAttributeWithOthers = stripsTarget[0].posters[iaCheckingLineData[0]].getAttributionId();
			animSquenceOnWinLine.addPoster(stripsTarget[0].posters[iaCheckingLineData[0]]);
			for(int i=1; i<iaCheckingLineData.length; i++)
			{
				if(stripsTarget[i].posters[iaCheckingLineData[i]].getAttributionId() == iCheckAttributeWithOthers)
				{
					animSquenceOnWinLine.addPoster(stripsTarget[i].posters[iaCheckingLineData[i]]);
				}
				else
					break;
			}
			
			animsSquence.add(animSquenceOnWinLine);
		}
		
		return animsSquence;
	}
	
	private  Map<Integer, ArrayList<Poster>> DetectWinLinesOnReels(Strip[] stripsTarget)
	{
		Map<Integer, ArrayList<Poster>> mapWinLine = new HashMap<Integer,ArrayList<Poster>>();
		for(Integer iLinesPoint = 0; iLinesPoint< GameParameters.alLineData.size(); iLinesPoint++)
		{
			if(iLinesPoint+1 <= DataStorage.get().lines)
			{
				Integer[] iaCheckingLineData = GameParameters.alLineData.get(iLinesPoint);
				Poster[] postersOnThisWinline = new Poster[5];
				for(int i=0; i<iaCheckingLineData.length; i++)
				{
					Poster pStatic = stripsTarget[i].posters[iaCheckingLineData[i]];
					Poster p = new Poster();
					p.dHeight = pStatic.dHeight;
					p.dWidth = pStatic.dWidth;
					p.dX = pStatic.dX;
					p.dY = pStatic.dY;
					p.iAttributionId = pStatic.iAttributionId;
					p.iColumnId = pStatic.iColumnId;
					p.iPositionIdOnSprite = pStatic.iPositionIdOnSprite;
					p.iRowId = pStatic.iRowId;
					p.isWild = pStatic.isWild;
					p.payout = pStatic.payout;
					p.containerBitmap = pStatic.containerBitmap;
					
					postersOnThisWinline[i] = p;
				}
				
				ArrayList<Poster> alMatchedPosters = new ArrayList<Poster>();
				int iFirstId = postersOnThisWinline[0].iAttributionId;
				alMatchedPosters.add(postersOnThisWinline[0]);
				
				
				if(iFirstId != 10 && iFirstId != 11)
				{
					//int iMatchedIdCounter = 0;
					for(int i=1; i<postersOnThisWinline.length; i++)
					{
						if(postersOnThisWinline[i].iAttributionId == 9)
						{
							postersOnThisWinline[i].iAttributionId = iFirstId;
						}
						if(iFirstId == postersOnThisWinline[i].iAttributionId)
						{
							alMatchedPosters.add(postersOnThisWinline[i]);
						}
						else
							break;
					}
					
					if(alMatchedPosters.size()>= postersOnThisWinline[0].getMinPayout())
					{
						mapWinLine.put(iLinesPoint, alMatchedPosters);
					}
				}
			}
			else
				break;
				
			
			
			
			
		}
		
		
		
		return mapWinLine;
	}
	
	@Deprecated
	private  Map<Integer, ArrayList<Poster>> DetectWinLines(Strip[] stripsTarget)
	{
		for(int i=0; i<stripsTarget.length; i++)
		{
			for(int j=0; j<stripsTarget[i].posters.length; j++)
			{
			//	GWT.log(""+i+" , "+j+" : "+stripsTarget[1].posters[1].iAttributionId);
			}
			
		}
		GWT.log(""+1+" , "+1+" : "+stripsTarget[1].posters[1].iAttributionId);
		
		ArrayList<Integer> alWinLinesResult = new ArrayList<Integer>();
		Map<Integer, ArrayList<Poster>> mapWinLine = new HashMap<Integer,ArrayList<Poster>>();
		
		for(Integer iLinesPoint = 0; iLinesPoint< GameParameters.alLineData.size(); iLinesPoint++)
		{
			//GWT.log("   "+" : "+stripsTarget[1].posters[1].iAttributionId);
			if(iLinesPoint == 12)
			{
				for(int i=0; i<stripsTarget[i].posters.length; i++)
				{
				//	GWT.log(" i  "+i+" : "+stripsTarget[1].posters[1].iAttributionId);
				}
				for(int i=0; i<GameParameters.alLineData.get(iLinesPoint).length; i++)
				{
				//	GWT.log(" i  "+i+" "+stripsTarget[i].posters[GameParameters.alLineData.get(iLinesPoint)[i]].iAttributionId);
				}
			}
			ArrayList<Poster> iconsOnWinline = new ArrayList<Poster>();
			Integer[] iaCheckingLineData = GameParameters.alLineData.get(iLinesPoint);
			Poster[] iconWillBeCheckingAttributionValues = new Poster[5];
			for(int i=0; i<iaCheckingLineData.length; i++)
			{
				int iCheckWinLineData = iaCheckingLineData[i];
				Poster p = stripsTarget[i].posters[iCheckWinLineData];
				iconWillBeCheckingAttributionValues[i] = p;
				
			}
			iconsOnWinline.add(iconWillBeCheckingAttributionValues[0]);
			int iTempCheckingAttributionValue = iconWillBeCheckingAttributionValues[0].getAttributionId();
			int iMinEqual = stripsTarget[0].posters[iaCheckingLineData[0]].getMinPayout();
			boolean bWinLine = false;
			int iMinEqualPoster = 1;
			
			
			for(int i=1; i<iconWillBeCheckingAttributionValues.length; i++)
			{
				int iAttId = iconWillBeCheckingAttributionValues[i].getAttributionId();
				if(iAttId == 11 || iAttId == 10)
				{
					GWT.log(" return iAttId "+iAttId);
					bWinLine = false;
					break;
				}
				GWT.log("iAttId "+iAttId);
				if(iconWillBeCheckingAttributionValues[i].getAttributionId() == 9)
					iconWillBeCheckingAttributionValues[i].iAttributionId = iTempCheckingAttributionValue;
				if(iTempCheckingAttributionValue == iconWillBeCheckingAttributionValues[i].getAttributionId() ||
						iconWillBeCheckingAttributionValues[i].getAttributionId() == 9)
				{
					
					iTempCheckingAttributionValue = iconWillBeCheckingAttributionValues[i].getAttributionId();
					iMinEqualPoster += 1;
					bWinLine = true;
					iconsOnWinline.add(iconWillBeCheckingAttributionValues[i]);
				}
				else if(iMinEqualPoster < iMinEqual)
				{
					bWinLine = false;
					break;
				}
				else
				{
					bWinLine = true;
					break;
				}
				
			}//GWT.log("iMinEqualPoster : "+iMinEqualPoster +" "+bWinLine +"id "+iLinesPoint);
			if(bWinLine)
			{
				mapWinLine.put(iLinesPoint, iconsOnWinline);
				alWinLinesResult.add(iLinesPoint);
			}
				
			
		}
		
		
		return mapWinLine;
	} 
	
	
	
	public static void setSpinResult(Strip[] stripsOnDisplaying)
	{
		MrgoodController.getInstance().addEntry("in setSpinResult");
		lastSpinRegistery = new GameRegistery(iSpinCount);
		Map<Integer, ArrayList<Poster>> mapDetectedWinLines = new GameStatics().DetectWinLinesOnReels(stripsOnDisplaying);//DetectWinLines(stripsOnDisplaying);
		
		Set<Integer> keyWinLines = mapDetectedWinLines.keySet();
		ArrayList<Integer> alDetectedWinLines = new ArrayList<Integer>();
		for(int iWinLine : keyWinLines)
		{
			alDetectedWinLines.add(iWinLine);
		}
		lastSpinRegistery.alWinLines = alDetectedWinLines;
		ArrayList<AnimationSquence> alAnimations = new ArrayList<AnimationSquence>();
		int iLineSumScore = 0;
		int iPoint = 0;
		for(int i=0; i<mapDetectedWinLines.size(); i++)
		{
			AnimationSquence animsSquence = new AnimationSquence(alDetectedWinLines.get(i), iPoint);
			ArrayList<Poster> alPostersOnWinLine = mapDetectedWinLines.get(alDetectedWinLines.get(i));
			for(Poster p : alPostersOnWinLine)
			{
				animsSquence.addPoster(p);
			}
			int iMultiplayer = alPostersOnWinLine.get(0).getWinlineMultiplayer(alPostersOnWinLine.size());
			animsSquence.iScore =  iMultiplayer * DataStorage.get().lines_bet_saved;
			alAnimations.add(animsSquence);
			iLineSumScore += animsSquence.iScore;
			iPoint +=1;
		}
//		if(MrgoodController.getInstance().getConnection().isOffline())
//			DataStorage.get().iWin = iLineSumScore;
		lastSpinRegistery.iWinSum = DataStorage.get().iWin;//iLineSumScore * DataStorage.get().iBet;
		
		lastSpinRegistery.animationsSquence = alAnimations;//FindPostersOnWinLine(alDetectedWinLines, stripsOnDisplaying);
		
		if(alDetectedWinLines.size() == 0)
		{
			
		}
		else
		{
			
		}
	}
	
	public static GameRegistery getRegisteredSpin()
	{
		return lastSpinRegistery;
	}
	
	public static int getTreasureType()
	{
		return Random.nextInt(4);
	}
	
	public static int getTreasureScore(int iTreasureType)
	{
		int iRet = 0;
		if(iTreasureType == 0)
			iRet = 150;
		else if(iTreasureType == 1)
			iRet = 100;
		else if(iTreasureType == 2)
			iRet = 250;
		else if(iTreasureType == 3)
			iRet = 50;
		return iRet;
	}
	
	public static int[][] getFreeReels()
	{
		iTotalWinSpin += 1;
		//iRet = new int[][]{{1,2,3},{3,3,3},{3,3,3},{2,5,6},{2,3,3}};
		int[][] iRet = new int[5][3];
		
		int iRand = Random.nextInt(4);
		
		if(iRand == 0)
			iRet = new int[][]{{5,10,7},{4,2,3},{10,8,6},{5,8,3},{5,2,2}};
		else if(iRand == 1)
			iRet = new int[][]{{5,6,7},{4,2,3},{10,8,6},{5,9,3},{5,2,7}};
		else if(iRand == 2)
			iRet = new int[][]{{2,6,4},{4,5,3},{7,8,6},{5,8,2},{5,2,7}};
		else if(iRand == 3)
			iRet = new int[][]{{5,4,8},{6,10,2},{3,8,6},{5,8,2},{5,2,7}};
		else if(iRand == 3)
			iRet = new int[][]{{8,4,5},{2,10,6},{6,8,3},{2,8,5},{7,4,5}};
		else if(iRand == 4)
			iRet = new int[][]{{7,8,4},{6,5,10},{8,2,3},{7,8,5},{4,7,6}};
		
		return iRet;
	}
	
	public static int[][] getRegularWin(boolean isBonus,boolean isClickme)
	{
		int[][] iRet = new int[5][3];
		
		iTotalWinSpin += 1;
		
		if(isClickme)
		{
			iTotalWinSpin +=2;
			iRet = new int[][]{{3,5,11},{8,4,4},{2,5,11},{3,9,2},{11,2,6}};
			return iRet;
		}
		
		if(isBonus)
			iTotalWinSpin = 0;
		if(iTotalWinSpin > 8)
		{
			isBonus = true;
			DataStorage.get().isBonusActive = true;
			iTotalWinSpin = 0;
		}
		
		
		if(isBonus)
		{
			iRet = new int[][]{{5,7,10},{7,10,3},{7,10,6},{5,4,3},{5,2,2}};
			return iRet;
		}
		
		int iRand = Random.nextInt(8);
		NativeHelper.console("reel rand "+iRand);
		if(iRand == 0)
			iRet = new int[][]{{5,10,7},{7,2,3},{9,8,6},{5,4,3},{5,2,2}};
		else if(iRand == 1)
			iRet = new int[][]{{3,2,4},{5,2,2},{8,2,6},{4,2,3},{5,2,7}};
		else if(iRand == 2)
			iRet = new int[][]{{7,10,2},{9,4,5},{7,2,6},{5,8,2},{5,2,3}};
		else if(iRand == 3)
			iRet = new int[][]{{3,8,5},{5,8,3},{7,2,3},{5,8,2},{5,2,3}};
		else if(iRand == 4)
			iRet = new int[][]{{6,4,11},{5,4,6},{7,10,4},{2,5,4},{4,3,11}};
		else if(iRand == 5)
			iRet = new int[][]{{11,4,6},{8,6,4},{7,10,4},{7,4,9},{2,4,3}};
		else if(iRand == 6)
			iRet = new int[][]{{7,3,5},{7,3,5},{5,10,2},{2,3,9},{2,8,6}};
		else
			iRet = new int[][]{{3,4,10},{2,9,4},{3,5,6},{9,2,3},{11,5,7}};
	//	iRet = new int[][]{{3,4,10},{2,9,4},{3,5,6},{9,2,3},{11,5,7}};
		
		return iRet;
	}
	
	public static int iChooseOptionCount = 0;
	public static int iTotalWinSpin = 0;
	public static void setOfflineBonusData()
	{
		iTotalWinSpin = 0;
		DataStorage.get().bBonusCollect = false;
		iChooseOptionCount += 1;
		
		int iRand = Random.nextInt(4);
		if(iRand == 0)
			DataStorage.get().iBonusPayout = 125;
		if(iRand == 1)
			DataStorage.get().iBonusPayout = 200;
		if(iRand == 2)
			DataStorage.get().iBonusPayout = 250;
		if(iRand == 3)
			DataStorage.get().iBonusPayout = 300;
		if(iRand == 4)
			DataStorage.get().iBonusPayout = 50;
		
		if(Random.nextInt(1) == 0 && iChooseOptionCount > 1)
			DataStorage.get().bBonusCollect = true;
		DataStorage.get().bBonusCollect = true;
	}
	
	public static int getIconPositionOnSpriteById(int iAttributionId)
	{
		String sDescription ="";
		int iPositionIdOnSprite = -1;
		
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
		return iPositionIdOnSprite;
	}

}
