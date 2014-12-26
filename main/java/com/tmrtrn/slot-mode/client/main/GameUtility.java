package com.tmrtrn.mrgood.client.main;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.user.client.Window;
import com.sinewavemultimedia.easeljs.framework.js.CallBack;
import com.sinewavemultimedia.easeljs.framework.js.CallBackEvent;
import com.sinewavemultimedia.easeljs.framework.js.Event;
import com.sinewavemultimedia.easeljs.framework.js.Image;
import com.sinewavemultimedia.easeljs.framework.js.Manifest;
import com.sinewavemultimedia.easeljs.framework.js.display.Bitmap;
import com.sinewavemultimedia.easeljs.framework.js.display.Container;
import com.sinewavemultimedia.easeljs.framework.js.display.DisplayObject;
import com.sinewavemultimedia.easeljs.framework.js.preload.PreloadJS;
import com.sinewavemultimedia.easeljs.framework.js.tween.Property;
import com.sinewavemultimedia.easeljs.framework.js.tween.Tween;
import com.tmrtrn.mrgood.client.main.Preloader.LoadingGameComponents;
import com.tmrtrn.mrgood.client.main.Preloader.LoadingScreen;
import com.tmrtrn.mrgood.client.main.rpc.Rpc;
import com.tmrtrn.mrgood.client.main.settings.SettingsView.SettingsTab;
import com.tmrtrn.mrgood.client.main.spinner.Poster;
import com.tmrtrn.mrgood.client.main.utility.CallbackUpdateTween;
import com.tmrtrn.mrgood.client.main.utility.Rectangle;


public class GameUtility {
	
	Map<String, JavaScriptObject> loadedComponents;
	Map<String, JavaScriptObject> loadedBackgroundComponents;
	
	PreloadJS preloadBackground;
	
	public GameUtility()
	{
		
	}
	
	public void initialize(Map<String, JavaScriptObject> loadedComponents)
	{
		this.loadedComponents = loadedComponents;
		loadBackgroundAssets();
	}
	
	/*
	private void loadDoubleUpAssets()
	{
		MrgoodController.getInstance().bBackgroundDoubleUpAssetsLoaded = false;
		LoadingGameComponents.prepareDoubleUpBackgroundAssets();
		JsArray<Manifest> manifest = (JsArray<Manifest>)JavaScriptObject.createArray();
		for(int i=0; i<LoadingGameComponents.imagesBackground.size(); i++)
		{
			
			Manifest imageManifest = Manifest.create();
			imageManifest.addStringProperty("id", LoadingGameComponents.imagesBackground.get(i).sStyleName);
			imageManifest.addStringProperty("src", LoadingGameComponents.imagesBackground.get(i).sUrl);
			manifest.push(imageManifest);
			
		} 
		
		CallBackEvent handleFileLoad = new CallBackEvent() {
			@Override
			public void onSuccess(Event event) {
				
				loadedBackgroundComponents.put(event.getId(), event.getResult());
			}
		};
		
		CallBackEvent handleComplete = new CallBackEvent() {
			
			@Override
			public void onSuccess(Event event) {
				GWT.log("background assets loaded");
				MrgoodController.getInstance().bBackgroundAssetsLoaded = true;
				MrgoodController.getInstance().bBackgroundDoubleUpAssetsLoaded = true;
			}
		};
		
		preloadBackground.onComplete(handleComplete);
		preloadBackground.onFileLoad(handleFileLoad); 
		preloadBackground.loadManifest(manifest);
	}
	*/
	
	private void loadBackgroundAssets()
	{
		String baseUrl = Window.Location.getParameter("media");
		if (baseUrl == null)
			baseUrl = "";
		MrgoodController.getInstance().bBackgroundAssetsLoaded = false;
		LoadingGameComponents.prepareBackgroundLoadingImages();
		loadedBackgroundComponents  = new HashMap<String, JavaScriptObject>();
		preloadBackground = PreloadJS.createPreloadJs();
		JsArray<Manifest> manifest = (JsArray<Manifest>)JavaScriptObject.createArray();
		for(int i=0; i<LoadingGameComponents.imagesBackground.size(); i++)
		{
			
			Manifest imageManifest = Manifest.create();
			imageManifest.addStringProperty("id", LoadingGameComponents.imagesBackground.get(i).sStyleName);
			imageManifest.addStringProperty("src", baseUrl+LoadingGameComponents.imagesBackground.get(i).sUrl);
			manifest.push(imageManifest);
			
		} 
		GWT.log(" manifest "+manifest.length());
		CallBackEvent handleFileLoad = new CallBackEvent() {
			@Override
			public void onSuccess(Event event) {
				
				loadedBackgroundComponents.put(event.getId(), event.getResult());
			}
		};
		
		CallBackEvent handleComplete = new CallBackEvent() {
			
			@Override
			public void onSuccess(Event event) {
				GWT.log("background assets loaded");
				MrgoodController.getInstance().bBackgroundAssetsLoaded = true;
			}
		};
		
		preloadBackground.onComplete(handleComplete);
		preloadBackground.onFileLoad(handleFileLoad); 
		preloadBackground.loadManifest(manifest);
	}
	
	
	public Bitmap GetGuiImage(String sStyleId,boolean bBackgroundAsset)
	{
		if(!bBackgroundAsset)
			return Bitmap.create(loadedComponents.get(sStyleId));
		return Bitmap.create(loadedBackgroundComponents.get(sStyleId));
	}
	
	
	public Image GetGuiImageAsImage(String sStyleId,boolean bBackgroundAssets)
	{
		if(!bBackgroundAssets)
			return (Image) loadedComponents.get(sStyleId);
		else
			return (Image) loadedBackgroundComponents.get(sStyleId);
	}
	
	
	
	public Bitmap createPosterBitmap(Poster poster,boolean isBlur)
	{
		
		Bitmap bmpReel = GetGuiImage("imgMrgood_Icons_Sheet",false);
		Rectangle rect_bitmap = null;
		if(!isBlur)
			rect_bitmap = Rectangle.create(poster.iPositionIdOnSprite*170, 0, 170, 168);
		else
			rect_bitmap = Rectangle.create(poster.iPositionIdOnSprite*170, 170, 170, 168);
		
		setSourceRect(bmpReel,rect_bitmap);
		
		return bmpReel;
	}
	
	public  Bitmap createLargePosterBitmap(Poster poster)
	{
		Bitmap bmpReel = GetGuiImage("imgMrgood_Icons_Sheet_Large",false);
		Rectangle rect_bitmap = null;
		rect_bitmap = Rectangle.create(poster.iPositionIdOnSprite*300, 0, 300, 300);

		setSourceRect(bmpReel,rect_bitmap);
		return bmpReel;
	}
	
	public Bitmap getBonusGameDoor(int iPosition)
	{
		Bitmap bmpDoors = GetGuiImage("imgBonus_Doors_spritemap",false);
		setSourceRect(bmpDoors,Rectangle.create(iPosition*225, 0, 225, 431));
		return bmpDoors;
	}
	
	public Bitmap getBonusGameOpenedDoor(int iPosition)
	{
		Bitmap bmpDoors = GetGuiImage("imgBonus_DoorsOpened_spritemap",false);
		if(iPosition == 0)
			setSourceRect(bmpDoors,Rectangle.create(0, 0, 126, 519));
		else if(iPosition == 1)
			setSourceRect(bmpDoors,Rectangle.create(126, 0, 76, 519));
		else if(iPosition == 2)
			setSourceRect(bmpDoors,Rectangle.create(208, 0, 37, 519));
		
		return bmpDoors;
	}
	
	public Bitmap getBonusGameTreasure(int iType)
	{
		Bitmap bmpTreasure = GetGuiImage("imgTreasuresSpritemap",false);
		setSourceRect(bmpTreasure,Rectangle.create(222*iType, 0, 222, 423));
		return bmpTreasure;
	}
	
	public Bitmap getBonusGameEmptyDoor(int iPosition)
	{
		Bitmap bmpDoors = GetGuiImage("imgBonus_DoorsEmpty_spritemap",false);
		if(iPosition == 0)
			setSourceRect(bmpDoors,Rectangle.create(0, 0, 237, 452));
		else if(iPosition == 1)
			setSourceRect(bmpDoors,Rectangle.create(237, 0, 259, 452));
		else if(iPosition == 2)
			setSourceRect(bmpDoors,Rectangle.create(450, 0, 240, 442));
		
		return bmpDoors;
	}
	
	/*
	public Bitmap getPaytableButton(int iButtonId)
	{
		Bitmap bmpButton = GetGuiImage("imgPaytableButtonsSpritemap");
		if(iButtonId == 0)
			setSourceRect(bmpButton,Rectangle.create(0, 0, 228, 59)); //Previous
		else if(iButtonId == 1)
			setSourceRect(bmpButton,Rectangle.create(228, 0, 164, 59)); // Next
		else if(iButtonId == 2)
			setSourceRect(bmpButton,Rectangle.create(392, 0, 93, 59)); // Exit
		
		return bmpButton;
	}
	*/
	public Bitmap getSettingsTabIcon(SettingsTab tab, int iGetDefault)
	{
		Bitmap bmpButton = GetGuiImage("imgSettingsTabsIconMap",true);
		if(tab == SettingsTab.BetSettings)
			setSourceRect(bmpButton, Rectangle.create(85*iGetDefault, 0, 77, 63));
		else if(tab == SettingsTab.SpinSettings)
			setSourceRect(bmpButton, Rectangle.create(85*iGetDefault, 63, 77, 57));
		else if(tab == SettingsTab.SoundSettings)
			setSourceRect(bmpButton, Rectangle.create(85*iGetDefault, 121, 77, 61));
		else if(tab == SettingsTab.PaytableSettings)
			setSourceRect(bmpButton, Rectangle.create(85*iGetDefault, 183, 77, 58));
		else if(tab == SettingsTab.RulesSettings)
			setSourceRect(bmpButton, Rectangle.create(85*iGetDefault, 242, 77, 57));
		else if(tab == SettingsTab.Banking)
			setSourceRect(bmpButton, Rectangle.create(80*iGetDefault, 300, 61, 60));
		
		return bmpButton;
	}
	public Bitmap getSettingsTabIconBackground(int iDefault)
	{
		Bitmap bmpButton = GetGuiImage("imgSettingsTabsBackground",true);
		setSourceRect(bmpButton, Rectangle.create(0, 80*iDefault , 130, 80));
		
		return bmpButton;
	}
	
	public Bitmap getSettingsBetText(int iPosition)
	{
		Bitmap bmpButton = GetGuiImage("imgSettingsBetText",true);
		setSourceRect(bmpButton, Rectangle.create(0, 33*iPosition , 200, 33));
		
		return bmpButton;
	}
	public Bitmap getSettingsBetsDisplayer(int iPosition)
	{
		Bitmap bmpButton = GetGuiImage("imgSettingsBetsDisplayer",true);
		setSourceRect(bmpButton, Rectangle.create(iPosition*290, 0 , 290, 116));
		
		return bmpButton;
	}
	
	public Bitmap getSettingsGlowNumber(int iNumber)
	{
		Bitmap bmp = GetGuiImage("imgSettingsGlowNumbers",true);
		if(iNumber == -1)
			setSourceRect(bmp, Rectangle.create(0, 0 , 20, 50));
		else
			setSourceRect(bmp, Rectangle.create(25+(iNumber*40), 0 , 40, 50));
		
		return bmp;
	}
	
	public Bitmap getCounterNumber(int iNumber,boolean bWhite)
	{
		
		Bitmap bmp = null;
		if(!bWhite)
			bmp = GetGuiImage("imgConterNumberSheet",false);
		else
			bmp = GetGuiImage("imgConterNumberSheet_White",false);
		
		int iTopOffset = iNumber / 5;
		setSourceRect(bmp, Rectangle.create(41*(iNumber%5), 59*iTopOffset , 41, 59));
		
		return bmp;
	}
	
	public Bitmap getSettingsToggleButton(int iOpen)
	{
		Bitmap bmp = GetGuiImage("imgSettingsToggle",true);
		if(iOpen == 1)
			setSourceRect(bmp, Rectangle.create(0, 0 , 197, 67));
		else
			setSourceRect(bmp, Rectangle.create(0, 67 , 197, 67));
		
		return bmp;
	}
	
	public Bitmap getSettingsGambleOption(boolean bLeft)
	{
		Bitmap bmp = GetGuiImage("imgSettingsGambleIcons",true);
		if(bLeft)
			setSourceRect(bmp, Rectangle.create(0, 0 , 95, 68));
		else
			setSourceRect(bmp, Rectangle.create(95, 0 , 75, 68));
		
		return bmp;
	}
	
	private int iUpdatePointCounter = 0;
	
	public void UpdatePoint(final int iTargetPoint,int iDuration,final CallbackUpdateTween callback)
	{
		iUpdatePointCounter = 0;
		final Container contTemp = Container.createContainer();
		Property propStart = Property.createProperty();
		Property propTarget = Property.createProperty();
		propStart.addIntProperty("x", 0);
		propTarget.addIntProperty("x", iTargetPoint);
		contTemp.setX(0);
		Tween.get(contTemp, propStart).to(propTarget,iDuration).onChange(new CallBack() {
			
			@Override
			public void onSuccess() {
				
				iUpdatePointCounter = (int) contTemp.getX();
				callback.update(iUpdatePointCounter);
				if(iUpdatePointCounter >= iTargetPoint)
					callback.end();
			}
		});
	}
	

	
	public native void setHidden(Element elm)/*-{
    	elm.style.opacity = 0; // Gecko/Opera
	}-*/;
	public native void setVisable(Element elm)/*-{
		elm.style.opacity = 1; // Gecko/Opera
	}-*/;
	
	public final native void removeChild(Container x,DisplayObject object)/*-{
		x.removeChild(object);
	}-*/;	
	
	public final native void setSourceRect(Bitmap bmp,Rectangle sourceRect)/*-{
	bmp.sourceRect = sourceRect;
}-*/;
	
}
