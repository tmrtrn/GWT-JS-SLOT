package com.tmrtrn.mrgood.client.main.spinner;

import java.util.ArrayList;
import java.util.Calendar;

import sun.util.calendar.LocalGregorianCalendar.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.AudioElement;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;

import com.tmrtrn.mrgood.client.main.Activity;
import com.tmrtrn.mrgood.client.main.MrgoodController;
import com.tmrtrn.mrgood.client.main.rpc.DataStorage;
import com.tmrtrn.mrgood.client.main.sounds.SoundEffects;
import com.tmrtrn.mrgood.client.main.utility.CallbackTimerEnded;
import com.tmrtrn.mrgood.client.main.utility.CallbackUtilityTimer;
import com.tmrtrn.mrgood.client.main.utility.NativeHelper;
import com.tmrtrn.mrgood.client.main.utility.Utility;



public class StripController {
	
	public static boolean bSpinRunning = false;
	public ArrayList<StripPair> alStripsPair;
	
	
	public StripController()
	{
	
		alStripsPair = new ArrayList<StripPair>();
		for(int i=0; i<5; i++)
		{
			StripPair stripPair = new StripPair(i);
			alStripsPair.add(stripPair);
		}
		retStrips = new Strip[alStripsPair.size()];

		for(int i=0; i<alStripsPair.size(); i++)
		{
			retStrips[i] = alStripsPair.get(i).stripTarget;
		}
	}
	
	
	public void startSpin(CallbackStopSpin cbEnd)
	{
		for(int i=0; i<alStripsPair.size(); i++)
		{
			alStripsPair.get(i).SpinRequest(cbEnd);
		}
	}
	
	public void startSpinRequest()
	{
		
		MrgoodController.getInstance().triggerOnSpinRequested();
		MrgoodController.getInstance().gameView.containerSpinPanel.removeAllChildren();
		
		SoundEffects.get().playSound("reels_start");
		for(int i=0; i<alStripsPair.size(); i++)
		{
			alStripsPair.get(i).SpinRequest(new onSpinEnd());
		}
	}
	
	
	public void stopSpin()
	{
		endedReelSpinCount = 0;
		
		for(int i=0; i<alStripsPair.size(); i++)
		{
			alStripsPair.get(i).stopRequest();
		}
	}
	
	int endedReelSpinCount = 0;
	class onSpinEnd implements CallbackStopSpin
	{

		@Override
		public void success(int iReelId) {
			endedReelSpinCount += 1;
			if(endedReelSpinCount == 5)
			{
				MrgoodController.getInstance().addEntry(" ended reel spin count "+endedReelSpinCount);
				endedReelSpinCount = 0;
				Utility.WaitForSeconds(500, new CallbackUtilityTimer() {
					
					@Override
					public void onCompletedWaiting() {
						endSpinCallback();
					}
				});
				
			}
		}
		
	}
	
	public Strip[] retStrips;
	
	public void endSpinCallback()
	{
		MrgoodController.getInstance().addEntry(" endSpinCallback");
		retStrips = new Strip[alStripsPair.size()];

		for(int i=0; i<alStripsPair.size(); i++)
		{
			retStrips[i] = alStripsPair.get(i).stripTarget;
		}
		MrgoodController.getInstance().triggerOnSpinEnded();
		
	}
	
	public Poster[][] getPostersOnReels()
	{
		Poster[][] postersOnReels = new Poster[5][3];
		
		for(int i=0; i<alStripsPair.size(); i++)
		{
			postersOnReels[i] = alStripsPair.get(i).stripTarget.posters;
		}
		return postersOnReels;
	}

}
