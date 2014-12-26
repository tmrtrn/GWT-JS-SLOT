package com.tmrtrn.mrgood.client.main;

import java.util.ArrayList;

import com.tmrtrn.mrgood.client.main.animationtool.AnimationSquence;

public class GameRegistery {
	
	int iRegisterId;
	boolean bAddedTotalScoreThisSpin = false;
	boolean bYouWonAnimationShowed = false;
	
	public int iWonTotalScoreThisSpin = 0;
	
	public ArrayList<Integer> alWinLines;
	public ArrayList<AnimationSquence> animationsSquence;
	int iNextAnimationPointerByWinlineId;
	public int iWinSum;
	
	
	public GameRegistery(int iRegisterId)
	{
		this.iRegisterId = iRegisterId;
		bAddedTotalScoreThisSpin = false;
		animationsSquence = new ArrayList<AnimationSquence>();
		alWinLines = new ArrayList<Integer>();
		iNextAnimationPointerByWinlineId = 0;
	}
	
	public AnimationSquence getNextAnimationSquence()
	{
		AnimationSquence squenceRet = null;
		for (AnimationSquence squenceNext : animationsSquence) {
			if(squenceNext.getWinLineId() == alWinLines.get(iNextAnimationPointerByWinlineId))
			{
				if(iNextAnimationPointerByWinlineId == 0)
					squenceNext.isFirstAnimationByOthers = true;
					
				iNextAnimationPointerByWinlineId += 1;
				
				if(iNextAnimationPointerByWinlineId== alWinLines.size())
				{
					squenceNext.isLastAnimationSquenceByOthers = true;
					iNextAnimationPointerByWinlineId = 0;
				}
				
				squenceRet = squenceNext;
				
				break;
			}
		}
		return squenceRet;
	}

}
