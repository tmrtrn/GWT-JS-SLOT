package com.tmrtrn.mrgood.client.main.spinner;

import com.google.gwt.core.shared.GWT;
import com.sinewavemultimedia.easeljs.framework.js.display.Bitmap;
import com.sinewavemultimedia.easeljs.framework.js.display.Container;
import com.sinewavemultimedia.easeljs.framework.js.display.DisplayObject;
import com.sinewavemultimedia.easeljs.framework.js.tween.Tween;
import com.tmrtrn.mrgood.client.main.MrgoodController;
import com.tmrtrn.mrgood.client.main.rpc.DataStorage;
import com.tmrtrn.mrgood.client.main.utility.GameParameters;
import com.tmrtrn.mrgood.client.main.utility.Utility;




public class Strip {
	
	public Poster[] posters;
	

	
	public Strip(int iColumnId, int[] iaAttributions)
	{
		posters = new Poster[3];
		for(int i=0; i<posters.length; i++)
		{
			posters[i] = new Poster(i,iColumnId);
			posters[i].setAttribution(iaAttributions[i]);
			
		}
		
		
	}
	
	public void UpdatePosterAttributions(int iColumnId)
	{
		int[] iaDefaultReelIcons = DataStorage.get().getReel(iColumnId);
		for(int i=0; i<posters.length; i++)
		{
			posters[i].setAttribution(iaDefaultReelIcons[i]);
			
		
		}
	//	if(posters[StripController.iTestRow].iColumnId != 4)
	//		posters[StripController.iTestRow].setAttribution( StripController.iTestIconId);
		
	}
	
	
	
	public native static final Tween setChain(Tween t,Tween tBack)/*-{
	return t.chain(tBack);
	
}-*/;
	
	public native final Tween get(DisplayObject target)/*-{
	return new $wnd.createjs.Tween(target); //get(target,property);
	
}-*/;
	
	public native final Tween get(DisplayObject target,boolean b)/*-{
	return new $wnd.createjs.Tween(target,b); //get(target,property);
	
}-*/;
	
	


	
	
	
	
	public Container CreateBlurStripContainerByPosterAttribution()
	{
		
		Container containerBlur = Container.createContainer();
		for(int i=0; i<3; i++)
		{
			Bitmap displayObj = MrgoodController.getInstance().utility.createPosterBitmap(posters[i], true);//(Bitmap) GUIController.GetGuiImage("BlurIcon"+Integer.toString(posters[i].iAttributionId)).clone();
			Container containerPoster = Container.createContainer();
			containerPoster.addChild(displayObj);
			containerPoster.setTransform(0, (2-i)*(GameParameters.dStripBipMapHeight+GameParameters.dStripHeightOffsetEachBitMapOnStrip));
			containerBlur.addChild(containerPoster);
		}
		return containerBlur;
	}
	
	public Container CreateStripContainerByPosterAttributionId(Container containerNormal)
	{
		containerNormal.removeAllChildren();
		
		for(int i=0; i<3; i++)
		{
			
			Bitmap displayObj = MrgoodController.getInstance().utility.createPosterBitmap(posters[i], false);//(Bitmap) GUIController.GetGuiImage(Integer.toString(posters[i].iAttributionId)).clone();
			posters[i].containerBitmap = Container.createContainer();
			posters[i].containerBitmap.addChild(displayObj);
			posters[i].containerBitmap.setTransform(0, (2-i)*(GameParameters.dStripBipMapHeight+GameParameters.dStripHeightOffsetEachBitMapOnStrip));
			containerNormal.addChild(posters[i].containerBitmap);
			
			
			
			posters[i].dX = 0 +posters[i].iColumnId*(GameParameters.dStripBipMapWidth + GameParameters.dStripWidthOffsetToRightStrip);
			posters[i].dY = (2-i)*(GameParameters.dStripBipMapHeight + GameParameters.dStripHeightOffsetEachBitMapOnStrip);
			posters[i].dWidth = GameParameters.dStripBipMapWidth;
			posters[i].dHeight = GameParameters.dStripBipMapHeight;
			
		}
		return containerNormal;
	}
	
	

	public Container CreateBlurStripContainer(Container containerBlur, int iColumnId)
	{
		containerBlur.removeAllChildren();
		
		for(int i=0; i<3; i++)
		{
			Poster p = new Poster(-1, -1);
			int iRand = GameParameters.GetRandomNumber(1, 10);
			while((iColumnId == 0 || iColumnId == 4) && iRand == 9)
				iRand = GameParameters.GetRandomNumber(1, 10);
			p.setAttribution(iRand);
			Bitmap displayObj = MrgoodController.getInstance().utility.createPosterBitmap(p, true);//(Bitmap) GUIController.GetGuiImage("BlurIcon"+Integer.toString(posters[i].iAttributionId)).clone();
			displayObj.setTransform(0, (2-i)*(GameParameters.dStripBipMapHeight+GameParameters.dStripHeightOffsetEachBitMapOnStrip));
			containerBlur.addChild(displayObj);
		}
		return containerBlur;
	}
	
	
}
