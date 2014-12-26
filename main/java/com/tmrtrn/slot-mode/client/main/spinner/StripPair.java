package com.tmrtrn.mrgood.client.main.spinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.Window;
import com.sinewavemultimedia.easeljs.framework.js.CallBackTween;
import com.sinewavemultimedia.easeljs.framework.js.display.Container;
import com.sinewavemultimedia.easeljs.framework.js.display.Graphics;
import com.sinewavemultimedia.easeljs.framework.js.display.Shape;
import com.sinewavemultimedia.easeljs.framework.js.tween.Ease;
import com.sinewavemultimedia.easeljs.framework.js.tween.EaseFunction;
import com.sinewavemultimedia.easeljs.framework.js.tween.Property;
import com.sinewavemultimedia.easeljs.framework.js.tween.Tween;
import com.tmrtrn.mrgood.client.main.MrgoodController;
import com.tmrtrn.mrgood.client.main.rpc.DataStorage;
import com.tmrtrn.mrgood.client.main.sounds.SoundEffects;
import com.tmrtrn.mrgood.client.main.utility.CallbackTimerEnded;
import com.tmrtrn.mrgood.client.main.utility.CallbackTweenHelper;
import com.tmrtrn.mrgood.client.main.utility.CallbackUtilityTimer;
import com.tmrtrn.mrgood.client.main.utility.GameParameters;
import com.tmrtrn.mrgood.client.main.utility.NativeHelper;
import com.tmrtrn.mrgood.client.main.utility.Utility;

public class StripPair {
	
	int iColumnId;
	public Strip stripTarget;
	public Strip stripExtra;
	
	Container containerTempTarget;
	Container containerTempExtra;
	Container containerPair;
	
	
	JavaScriptObject jsoHelper;
	
	public StripPair(final int iColumnId)
	{
		this.iColumnId = iColumnId;
		
		
		int[] iaDefaultReelIcons = DataStorage.get().getReel(iColumnId);
		stripTarget = new Strip(iColumnId,iaDefaultReelIcons);
		containerPair = Container.createContainer();
		containerPair.setMask(createMask());
		
		containerTempTarget = Container.createContainer();
		containerPair.addChild(containerTempTarget);
		
		containerPair.setY(GameParameters.dStripStartPositionIfMiddleStrip);
		containerPair.setX(0 +
		iColumnId*(GameParameters.dStripBipMapWidth + GameParameters.dStripWidthOffsetToRightStrip)); 
		containerTempTarget.setX(0);
		containerTempTarget.setY(0);
		
		MrgoodController.getInstance().gameView.containerSpinPanel.addChild(containerPair);
		
		containerTempTarget = stripTarget.CreateStripContainerByPosterAttributionId(containerTempTarget);
		
		NativeHelper.createTweenHelper(containerPair,containerTempTarget,
				MrgoodController.getInstance().utility.GetGuiImageAsImage("imgMrgood_Icons_Sheet", false), new CallbackTweenHelper() {
			
			@Override
			public void success(JavaScriptObject jsoTweenHelper) {
				jsoHelper = jsoTweenHelper;
				
				
				
			}
		}, iColumnId);
		
		
	}
	
	
	
	Shape createMask()
	{
		Graphics graphicsMask = Graphics.createGraphics();
		graphicsMask.beginFill("#000");
	//	graphicsMask.rect((int)(1 +
	//			iColumnId*(GameParameters.dStripBipMapWidth + GameParameters.dStripWidthOffsetToRightStrip)), (int)GameParameters.dStripStartPositionIfMiddleStrip, (int)GameParameters.dStripBipMapWidth, 469);
		graphicsMask.rect((int)(
				iColumnId*(GameParameters.dStripBipMapWidth + GameParameters.dStripWidthOffsetToRightStrip))
				, (int)GameParameters.dStripStartPositionIfMiddleStrip, (int)GameParameters.dStripBipMapWidth, 522);
		
		return Shape.createShape(graphicsMask);
	}
	
	
	
	
	boolean bRewindTarget = false;
	
	CallbackStopSpin cbStop;
	
	public void SpinRequest(CallbackStopSpin cbStop)
	{
		this.cbStop = cbStop;

		MrgoodController.getInstance().gameView.containerSpinPanel.addChild(containerPair);
		NativeHelper.spin(jsoHelper, 
				Poster.getSpritePositionByAttributionId(DataStorage.get().getReel(iColumnId)[0]),
         		Poster.getSpritePositionByAttributionId(DataStorage.get().getReel(iColumnId)[1]),
         		Poster.getSpritePositionByAttributionId(DataStorage.get().getReel(iColumnId)[2]), iColumnId);
		
	}
	
	
	

	public void stopRequest()
	{
		/*
		if(iColumnId == 0)
		{
			double delay = 350;
			
			try{
				if(Window.Location.getQueryString().indexOf("play") >= 0)
					delay = Double.parseDouble(Window.Location.getParameter("play"));
			}
			catch(Exception ex)
			{
				delay = 350;
			}
			
			Utility.WaitForSeconds(delay, new CallbackUtilityTimer() {
				
				@Override
				public void onCompletedWaiting() {
					SoundEffects.get().playSound("reels_lock");
				}
			});
			
		}
		*/
		MrgoodController.getInstance().addEntry(" call stop request "+iColumnId);
		stripTarget.UpdatePosterAttributions(iColumnId);
		
		NativeHelper.delay(700 + iColumnId * 270, new CallbackTimerEnded() {
			
			@Override
			public void success() {
				 NativeHelper.stopSpin(jsoHelper,
						 	stripTarget.posters[0].iPositionIdOnSprite,
						 	stripTarget.posters[1].iPositionIdOnSprite,
						 	stripTarget.posters[2].iPositionIdOnSprite
			 				,new CallbackTweenHelper() {
							
							@Override
							public void success(JavaScriptObject jsoHelper) {
								MrgoodController.getInstance().addEntry(" stop success "+iColumnId);
								cbStop.success(iColumnId);
							}
						});
			}
		});
		
		
		
		
			
		
		
	}
	
	
	 private native String getTime()
	 /*-{
	 	var currentTime = +new Date();
		  
		  return   currentTime;
	 }-*/;
	
	
	
	
	
	

	
	
	
	public native final void setDuration(Tween t,double duration)/*-{
	t.duration = duration;
}-*/;

	public native final void setLoop(Tween t,boolean loop)/*-{
	t.loop = loop;
}-*/;
	
	
	
	
	
	
	private Property getMiddlePositionProperty()
	{
		Property propTargetMiddle = Property.createProperty();
		//propTargetMiddle.addDoubleProperty("y", StripManager.dStripTargetDisplayerMiddle);
		propTargetMiddle.addDoubleProperty("y", GameParameters.dStripTargetDisplayerMiddle);
		
		return propTargetMiddle;
	}
	
	
	
	
}












