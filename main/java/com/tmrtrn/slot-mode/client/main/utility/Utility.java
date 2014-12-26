package com.tmrtrn.mrgood.client.main.utility;

import java.util.ArrayList;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayMixed;
import com.google.gwt.core.client.JsArrayString;
import com.sinewavemultimedia.easeljs.framework.js.Animation;
import com.sinewavemultimedia.easeljs.framework.js.CallBack;
import com.sinewavemultimedia.easeljs.framework.js.CallBackTween;
import com.sinewavemultimedia.easeljs.framework.js.Image;
import com.sinewavemultimedia.easeljs.framework.js.SpriteFrame;
import com.sinewavemultimedia.easeljs.framework.js.SpriteSheetObject;
import com.sinewavemultimedia.easeljs.framework.js.display.BitmapAnimation;
import com.sinewavemultimedia.easeljs.framework.js.display.Container;
import com.sinewavemultimedia.easeljs.framework.js.display.SpriteSheet;
import com.sinewavemultimedia.easeljs.framework.js.tween.Property;
import com.sinewavemultimedia.easeljs.framework.js.tween.Tween;
import com.tmrtrn.mrgood.client.main.MrgoodController;


public class Utility {
	
	
	
	
	private static int iUpdatePointCounter = 0;
	
	public static void UpdatePoint(final int iTargetPoint,int iDuration,final CallbackUpdateTween callback)
	{
		NativeHelper.UpdateJS(iTargetPoint, iDuration, callback);
		/*
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
		*/
	}
	
	
	public static void WaitForSeconds(double dDuration, final CallbackUtilityTimer callback)
	{
		NativeHelper.delay((int)dDuration, new CallbackTimerEnded() {
			
			@Override
			public void success() {
				callback.onCompletedWaiting();
			}
		});
	/*	
		Utility util = new Utility();
		util.contTemp = Container.createContainer();
		util.callback = callback;
		alUtilsRunning.add(util);
		util.Wait(dDuration, callback);
		*/
	}
	
	
	
	
	Container contTemp;
	CallbackUtilityTimer callback;
	static ArrayList<Utility> alUtilsRunning = new ArrayList<Utility>();
	
	private void Wait(double dDuration, final CallbackUtilityTimer callback)
	{
		Tween.get(contTemp).waitDuration(dDuration).call(new CallBackTween() {
			
			@Override
			public void onSuccess(Tween tween) {
				
				callback.onCompletedWaiting();
			}
		});
		
	}
	
	
	public static BitmapAnimation getBorderBitmapanimation()
	{
		BitmapAnimation bmpAnim = create(createBorderSprite(MrgoodController.getInstance().utility.GetGuiImageAsImage("imgBorderSprites", false))); //getBorderSpriteSheet()
		return bmpAnim;
	}
	
	public final native static BitmapAnimation create(Object sheet)/*-{
		return new $wnd.createjs.BitmapAnimation(sheet);
	}-*/;
	
	/*
	private static TodoSpriteSheet createBorderSpriteSheet()
	{
		TodoSpriteSheet spriteSheet = TodoSpriteSheet.create(createBorderSpriteObject());
		return spriteSheet;
	}
	*/
	
	private static native Object createBorderSprite(Image img)
	/*-{
		var spriteSheet = new $wnd.createjs.SpriteSheet({
                images: [ img ],
                frames: { width: 142, height: 140, count: 41 },
                animations: {
                    run:  [0,40],//$wnd._.range(0, 33, -1),
                    backward: { frames: [34,40], next:"endRunFast"},
                    runFast:{frames :[0,8]},
                    jump: { frames: [34,40] },
                    runBorder : { frames: [0,40]},
                    endRunFast : {frames: [8,0,-1], next:"stop" },
                    stop: [0,0],
                    pauseRunFast: {frames:[8,8]},
                    endScale: {frames: [40,40]}
                }
            });
            
            return spriteSheet;
	}-*/;
	
//	 static SpriteSheet spriteSheetBorder;
	
	public static SpriteSheet getBorderSpriteSheet()
	{
	/*	if(spriteSheetBorder != null)
		{
			return spriteSheetBorder;
		} */
		SpriteSheetObject object = SpriteSheetObject.create();
		
		JsArrayMixed arrayForwardBorderFrame = (JsArrayMixed)JavaScriptObject.createArray();
		arrayForwardBorderFrame.push(0);//0
		arrayForwardBorderFrame.push(33); //33
		Animation animation = Animation.create();
		
		animation.run(arrayForwardBorderFrame);
	
		
		JsArrayMixed arrayBackwardBorderFrame = (JsArrayMixed)JavaScriptObject.createArray();
		arrayBackwardBorderFrame.push(34);
		arrayBackwardBorderFrame.push(40);
		animation.jump(arrayBackwardBorderFrame);
		
		JsArrayMixed arrayForwardAndBackwardBorderFrame = (JsArrayMixed)JavaScriptObject.createArray();
		arrayForwardAndBackwardBorderFrame.push(0);//0
		arrayForwardAndBackwardBorderFrame.push(40); //40
		
		animation.addMixedArrayProperty("runBorder",arrayForwardAndBackwardBorderFrame);
		
		
		JsArrayString image = (JsArrayString)JavaScriptObject.createArray(); 
		image.push("images/Border_Sprites.png");
		object.addImage(image);
		
		object.addAnimations(animation);
		
		SpriteFrame frame = SpriteFrame.create();
		frame.setNumFrames(41); //41
		frame.setFrameWidth(142);
		frame.setFrameHeight(140);
		frame.setRegX(0);
		frame.setRegY(0);
		object.addFrame(frame);
		
		
		SpriteSheet spriteSheetBorder = SpriteSheet.create(object);	
		return spriteSheetBorder;
	}
	
	
	
	public static native int getWindowInnerWidth() 
	/*-{ 
	      return $wnd.innerWidth; 
	}-*/; 
	public static native int getWindowInnerHeight() 
	/*-{ 
	      return $wnd.innerHeight; 
	}-*/; 

}
