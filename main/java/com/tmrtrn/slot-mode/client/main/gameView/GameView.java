package com.tmrtrn.mrgood.client.main.gameView;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window;
import com.sinewavemultimedia.easeljs.framework.js.CallBack;
import com.sinewavemultimedia.easeljs.framework.js.CreateJSObject;
import com.sinewavemultimedia.easeljs.framework.js.display.Bitmap;
import com.sinewavemultimedia.easeljs.framework.js.display.Container;
import com.sinewavemultimedia.easeljs.framework.js.display.Stage;
import com.sinewavemultimedia.easeljs.framework.js.utils.Ticker;
import com.tmrtrn.mrgood.client.main.Activity;
import com.tmrtrn.mrgood.client.main.GameStatics;
import com.tmrtrn.mrgood.client.main.MessageView;
import com.tmrtrn.mrgood.client.main.MessageView.MessageType;
import com.tmrtrn.mrgood.client.main.MrgoodController;
import com.tmrtrn.mrgood.client.main.TimerCallback;
import com.tmrtrn.mrgood.client.main.animationtool.AnimationController;
import com.tmrtrn.mrgood.client.main.animationtool.CallbackAnimationSquence;
import com.tmrtrn.mrgood.client.main.rpc.CallbackAction;
import com.tmrtrn.mrgood.client.main.rpc.DataStorage;
import com.tmrtrn.mrgood.client.main.sounds.SoundEffects;
import com.tmrtrn.mrgood.client.main.spinner.CallbackStartSpin;
import com.tmrtrn.mrgood.client.main.spinner.CallbackStopSpin;
import com.tmrtrn.mrgood.client.main.spinner.OnSpinEndedListener;
import com.tmrtrn.mrgood.client.main.spinner.Strip;
import com.tmrtrn.mrgood.client.main.spinner.StripController;
import com.tmrtrn.mrgood.client.main.tools.CallbackButton;
import com.tmrtrn.mrgood.client.main.tools.GameButton;
import com.tmrtrn.mrgood.client.main.utility.CallbackUtilityTimer;
import com.tmrtrn.mrgood.client.main.utility.GameParameters;
import com.tmrtrn.mrgood.client.main.utility.NativeHelper;
import com.tmrtrn.mrgood.client.main.utility.Utility;

public class GameView implements OnSpinEndedListener, CallbackButton{
	
	public GUIGameView guiGameView;
	public StripController stripController;
	public Container containerSpinPanel;
	public Container containerAnimationPanel;
	
	public Element elmGameView;
	public CreateJSObject stageJSObject=null;
	public Stage stage =null;
	
	public GameButton btnAction;
	public GameButton btnGambling;
	
	public CallbackStartSpin cbSpinstart;
	
	public GameView()
	{
		elmGameView = Document.get().createElement("div");
		elmGameView.setClassName("GameViewElement");
		
		stageJSObject = new CreateJSObject(""+1024, ""+768);
		stage = stageJSObject.getStage();
		stageJSObject.getElement().setClassName("StageGameView");
		
		MrgoodController.getInstance().addOnSpinEndedListener(this);
	}
	
	
	
	public void changeActionButtonStyle(String sType)
	{
		
		if(sType == "auto_spin")
		{
			removeClassName(btnAction.elmButton, "spin_button auto_spin auto_spin_stop");
			addClassName(btnAction.elmButton,"spin_button auto_spin");
			String sAutoSpinActionInnerHtml = "<div class=\"auto_spin_counter\">"+MrgoodController.getInstance().iAutoSpinCount+"</div><span class=\"icon\"></span><span class=\"icon over\"></span>";
			btnAction.elmButton.setInnerHTML(sAutoSpinActionInnerHtml);
		}
		if(sType == "spin")
		{
			removeClassName(btnAction.elmButton, "spin_button auto_spin auto_spin_stop");
			addClassName(btnAction.elmButton,"spin_button");
			String sSpinActionInnerHtml = "<span class=\"icon\"></span><span class=\"icon over\"></span>";
			btnAction.elmButton.setInnerHTML(sSpinActionInnerHtml);
		}
		if(sType == "stop_auto_spin")
		{
			removeClassName(btnAction.elmButton, "spin_button auto_spin auto_spin_stop");
			addClassName(btnAction.elmButton,"spin_button auto_spin_stop");
			String sAutoSpinActionInnerHtml = "<div class=\"auto_spin_counter\">"+MrgoodController.getInstance().iAutoSpinCount+"</div><span class=\"icon\"></span><span class=\"icon over\"></span>";
			btnAction.elmButton.setInnerHTML(sAutoSpinActionInnerHtml);
		}
	}
	
	public void showGameView()
	{
		GWT.log("showGameViewshowGameView");
		MrgoodController.getInstance().elmMain.appendChild(elmGameView);
		
		Bitmap bmpBackgroundimage = MrgoodController.getInstance().utility.GetGuiImage("imgBackgroundGameScene",false);
		stage.addChild(bmpBackgroundimage);
		
		
		elmGameView.appendChild(stageJSObject.getElement());
		Element el_house_logo = Document.get().createDivElement();
		el_house_logo.addClassName("house_logo");
		stageJSObject.getElement().appendChild(el_house_logo);

		btnAction = new GameButton("action spin_button play","a");
		changeActionButtonStyle("spin");

		btnGambling = new GameButton("action double_up_button","a");
		btnGambling.elmButton.setInnerHTML("<span class=\"icon\"></span><span class=\"icon over\"></span>");
		
		elmGameView.appendChild(btnAction.elmButton);

		containerSpinPanel = Container.createContainer();
		containerSpinPanel.setX(GameParameters.dStripLeftPosition);
		containerSpinPanel.setY(0);
		stage.addChild(containerSpinPanel);
		
		containerAnimationPanel = Container.createContainer();
		containerAnimationPanel.setX(GameParameters.dStripLeftPosition);
		containerAnimationPanel.setY(0);
		stage.addChild(containerAnimationPanel);
		
		
		guiGameView = new GUIGameView();
		guiGameView.display(elmGameView);
		
		
		stripController = new StripController();
		

		
		
		runMainGameUpdate();
		
		if(SoundEffects.bAskedUserPermission)
		{
		//	enableGameButtons();
		}
		
		
		
		
	}
	
	public void enableGameButtons()
	{
		DataStorage.get().bDoubleUpActive = false;
		DataStorage.get().bDoubleUpEnable = false;
		
		if(MrgoodController.getInstance().sActiveScene.equals("settings") || 
				MrgoodController.getInstance().sActiveScene.equals("double") || 
				DataStorage.get().isBonusActive )
		{
			
			return;
		}
			
		
		if(btnAction == null || btnGambling == null)
			return;
		
		/*if(DataStorage.get().bDoubleUpActive && DataStorage.get().bDoubleUpEnable && 
				((MrgoodController.getInstance().bAutoSpinEnabled && !MrgoodController.getInstance().bAutoSpinIgnoreDoubleup)
						|| !MrgoodController.getInstance().bAutoSpinEnabled))
		{
			btnGambling.addClickListener(new GamblingButtonPressed());
			if(!btnGambling.elmButton.hasParentElement())
				elmGameView.appendChild(btnGambling.elmButton);
		}
		else if(!DataStorage.get().bDoubleUpEnable || !DataStorage.get().bDoubleUpActive || !((MrgoodController.getInstance().bAutoSpinEnabled && !MrgoodController.getInstance().bAutoSpinIgnoreDoubleup)
				|| !MrgoodController.getInstance().bAutoSpinEnabled))
		{
			if(btnGambling.elmButton.hasParentElement())
			{
				elmGameView.removeChild(btnGambling.elmButton);
				btnGambling.removeClickListener();
			}
				
		}*/
		btnAction.addClickListener(this);
		if(!btnAction.elmButton.hasParentElement())
			elmGameView.appendChild(btnAction.elmButton);
		
		if(MrgoodController.getInstance().bAutoSpinEnabled && MrgoodController.getInstance().bAutoSpinPaused)
			changeActionButtonStyle("auto_spin");
		else if(MrgoodController.getInstance().bAutoSpinEnabled && !MrgoodController.getInstance().bAutoSpinPaused)
			changeActionButtonStyle("stop_auto_spin");
		else
			changeActionButtonStyle("spin"); 
		
		guiGameView.btnSettings.addClickListener(new SettingsButtonPressed());
	}
	
	public void disableGameButtons(boolean bRemove)
	{
		if(btnAction == null || btnGambling == null)
			return;
		btnGambling.removeClickListener();
		btnAction.removeClickListener();
		guiGameView.btnSettings.removeClickListener();
		guiGameView.btnSettings.removeClickListener();
		
		if(bRemove)
		{
			elmGameView.removeChild(btnAction.elmButton);
			if(btnGambling.elmButton.hasParentElement())
				elmGameView.removeChild(btnGambling.elmButton);
		}
		MrgoodController.getInstance().bAutoSpinPaused = true;
	}
	
	
	
	
	
	
	//ActionButtonPressed
	
	@Override
	public void pressed() {
	
		if(MrgoodController.getInstance().bAutoSpinEnabled && !MrgoodController.getInstance().bAutoSpinPaused)
		{
			MrgoodController.getInstance().bAutoSpinEnabled = false;
			changeActionButtonStyle("spin");
			if(StripController.bSpinRunning || DataStorage.get().isClickmeActive)
				disableGameButtons(true);
			return;
			
		}
		if(DataStorage.get().isClickmeActive)
		{
			disableGameButtons(true);
			return;
		}
			
	
		
		if(StripController.bSpinRunning || MrgoodController.getInstance().sActiveScene != "main")
			return;
		
		
		if(MrgoodController.getInstance().bAutoSpinEnabled && !MrgoodController.getInstance().bAutoSpinPaused)
		{
			MrgoodController.getInstance().bAutoSpinEnabled = false;
			changeActionButtonStyle("spin");
			GWT.log("changeActionButtonStyle to spin");
		}
		else
			prepareAndStartSpin();
	} 
	
	MessageView mw2;
	
	
	MessageView mwBaking;
	
	private void prepareAndStartSpin()
	{
		if(StripController.bSpinRunning || MrgoodController.getInstance().sActiveScene != "main")
		{
			return;
		}
			
		
		int iNewBalance = (int) (DataStorage.get().dBalanceValue - (DataStorage.get().dBet * DataStorage.get().lines * DataStorage.get().lines_bet));
		DataStorage.get().iWin = 0;
		
		if(iNewBalance < 0)
		{
			MrgoodController.getInstance().bAutoSpinEnabled = false;
			changeActionButtonStyle("spin");
			MrgoodController.getInstance().bBlockAllInputs = true;
			//TODO Show bank notification
			mwBaking = new MessageView("MBL_INSUFFICIENT_FUNDS", "MBL_CLICK_BANK", new CallbackButton() {
				
				@Override
				public void pressed() {
					mwBaking.destroy();
					NativeHelper.open_new_blank(DataStorage.get().sBanking_url);
					MrgoodController.getInstance().bBlockAllInputs = false;
					return;
				}
			}, new CallbackButton() {
				
				@Override
				public void pressed() {
					mwBaking.destroy();
					MrgoodController.getInstance().bBlockAllInputs = false;
				}
			}, MessageType.Bank);
			
			mwBaking.setFirstButtonName("MBL_BANKING");
			mwBaking.setSecondButtonName("MBL_CANCEL");
			mwBaking.render(NativeHelper.getMessageElement());
			return;
		}
		
		DataStorage.get().dBalanceValue = iNewBalance;
		guiGameView.receivedSpinRequested();
		StripController.bSpinRunning = true;

		disableGameButtons(true);
		
		
		MrgoodController.getInstance().bAutoSpinPaused = false;
		if(MrgoodController.getInstance().bAutoSpinEnabled && !MrgoodController.getInstance().bAutoSpinPaused)
		{
			MrgoodController.getInstance().bAutoSpinPaused = false;
			MrgoodController.getInstance().iAutoSpinCount -= 1;
			changeActionButtonStyle("stop_auto_spin");
			btnAction.removeClickListener();
			btnAction.addClickListener(this);
		}
		if(MrgoodController.getInstance().bAutoSpinEnabled)
			elmGameView.appendChild(btnAction.elmButton);
		
		
		
		if(cbSpinstart != null)
			cbSpinstart.success();
		
		if(DataStorage.get().isBonusActive)
		{
			MrgoodController.getInstance().bonusView.destroyWinLine();
		}
			
		
		stripController.startSpinRequest(); 
		
		/*
		if(DataStorage.get().dBalanceValue < 0)
		{
			MessageView mw = new MessageView("ERROR", "NOTE_NO_BALANCE", null, null, MessageType.Error);
			mw.render(NativeHelper.getMessageElement());
			disableGameButtons(false);
			return;
		}*/

		MrgoodController.getInstance().getConnection().call("start", new CallbackAction() {
			
			@Override
			public void success() {
				
				stripController.stopSpin();
			}
		});
		
	}
	
	
	class GamblingButtonPressed implements CallbackButton
	{

		@Override
		public void pressed() {

			if(MrgoodController.getInstance().bBackgroundAssetsLoaded)
			{
				
				MrgoodController.getInstance().showDoubleUpView();
				
			}
				
			
		}
		
	}
	
	
	class SettingsButtonPressed implements CallbackButton
	{
		@Override
		public void pressed() {

			GWT.log("bBackgroundAssetsLoaded : "+MrgoodController.getInstance().bBackgroundAssetsLoaded);
			if(MrgoodController.getInstance().bBackgroundAssetsLoaded)
			{
				disableGameButtons(true);
				MrgoodController.getInstance().showSettingsView();
			}
		}
	}
	
	@Override
	public void onSpinEnded() {
		SoundEffects.get().stopSound();
		MrgoodController.getInstance().addEntry(" onSpinEnded in gameview");
		StripController.bSpinRunning = false;
		try{
			GameStatics.setSpinResult(stripController.retStrips);
			callAutoSpin();
			MrgoodController.getInstance().addEntry(" loadGameType");
			loadGameType();
			MrgoodController.getInstance().addEntry(" loadGameType OK");
			
			MrgoodController.getInstance().animationController.PrepareAndRunAnimations();
		}
		catch(Exception ex){
			MrgoodController.getInstance().addEntry(" onSpinEnded catch !!!!!");
			callAutoSpin();
		}
		
		
		
		
		
	} 	
	
	void callAutoSpin()
	{
		if(MrgoodController.getInstance().bAutoSpinEnabled && !MrgoodController.getInstance().bAutoSpinPaused)
		{
			
			double dWaitForSpin =  GameStatics.getRegisteredSpin().alWinLines.size() > 0 ? (2000 + (GameStatics.getRegisteredSpin().alWinLines.size() * 500)) : 1400;
			
			
					NativeHelper.console("dWaitForSpin "+dWaitForSpin+" winlinesize "+GameStatics.getRegisteredSpin().alWinLines.size()
							+"bautoSpinEnabled "+ MrgoodController.getInstance().bAutoSpinEnabled
							+"bAutospinPaused "+MrgoodController.getInstance().bAutoSpinPaused
							);		
					Utility.WaitForSeconds(dWaitForSpin, new CallbackUtilityTimer() {
				
				@Override
				public void onCompletedWaiting() {
					canRequestSpin();
				}
			});
			if( dWaitForSpin != 0)
				MrgoodController.getInstance().animationController.ResumeAnimationsFromCallback();
			
		}
	}
	
	
	public void loadGameType()
	{
		if(MrgoodController.getInstance().bAutoSpinEnabled )
		{
			if(MrgoodController.getInstance().iAutoSpinCount == 0)
			{
				MrgoodController.getInstance().iAutoSpinCount = MrgoodController.getInstance().iDefaultAutoSpinCount;
				MrgoodController.getInstance().bAutoSpinPaused = true;
				changeActionButtonStyle("auto_spin");
			}
			
		}
		
		if(DataStorage.get().isBonusActive)
		{
			GWT.log("isBonusActive TRUE ");
			
			//Bonus Active
			double dWait_winkickOff = SoundEffects.get().get_audio_enabled() ? 800 : 300;
			Utility.WaitForSeconds(dWait_winkickOff, new CallbackUtilityTimer() {
				
				@Override
				public void onCompletedWaiting() {
					SoundEffects.get().playSound("win_kickoff");
				}
			});
			
			Utility.WaitForSeconds(dWait_winkickOff + 800, new CallbackUtilityTimer() {
				
				@Override
				public void onCompletedWaiting() {
					MrgoodController.getInstance().showBonusView();
				}
			});
			return;
			
		}
		else if(DataStorage.get().isClickmeActive)
		{
		//	disableGameButtons(false);
			//changeActionButtonStyle("auto_spin");
			GWT.log("isClickmeActive TRUE ");
			
			Utility.WaitForSeconds(300, new CallbackUtilityTimer() {
				
				@Override
				public void onCompletedWaiting() {
					SoundEffects.get().playSound("win_kickoff");
				}
			});
			
			
			
			Utility.WaitForSeconds(500, new CallbackUtilityTimer() {
				
				@Override
				public void onCompletedWaiting() {
					Clickme clickme = new Clickme();
				}
			});
		
		}
		else
		{
			if(GameStatics.getRegisteredSpin() != null) // When Game View first loaded, registered spin is null 
				guiGameView.playWinBarAndUpdate();
			
			if(MrgoodController.getInstance().getConnection().isOffline())
			{
				if(GameStatics.getRegisteredSpin() == null) // When Game View first loaded, registered spin is null 
					return;
		//		DataStorage.get().dBalanceValue += GameStatics.getRegisteredSpin().iWinSum;
		//		DataStorage.get().iWin = GameStatics.getRegisteredSpin().iWinSum;
				
			}
		}
		
	}
	
	
	public void canCancelAnimationsCallback()
	{
		if(StripController.bSpinRunning)
			return;
		StripController.bSpinRunning = false;
		if(DataStorage.get().iWin == 0 && MrgoodController.getInstance().bAutoSpinEnabled)
		{
			/*
			int iDelay = 1400;
			if( MrgoodController.getInstance().bAutoSpinIgnoreDoubleup)
				iDelay = 1400;
			if(MrgoodController.getInstance().bGamblingEnabled)
				iDelay = 1400;
			
			Utility.WaitForSeconds(iDelay, new CallbackUtilityTimer() { //2000
				
				@Override
				public void onCompletedWaiting() {
					canRequestSpin();
				}
			});
			*/
		}
		else
			enableGameButtons();
	}
	
	public void AnimationRepeatCallback()
	{
		if(StripController.bSpinRunning)
			return;
		
		guiGameView.CallbackAnimationRepeat();
		
		if(DataStorage.get().iWin > 0)
		{
			/*
			double dWaitForSpin = MrgoodController.getInstance().bAutoSpinEnabled && !MrgoodController.getInstance().bAutoSpinPaused
					&& GameStatics.getRegisteredSpin().alWinLines.size() > 2 ? 0 : 2000;
			Utility.WaitForSeconds(dWaitForSpin, new CallbackUtilityTimer() {
				
				@Override
				public void onCompletedWaiting() {
					canRequestSpin();
				}
			});
			if( dWaitForSpin != 0)
				MrgoodController.getInstance().animationController.ResumeAnimationsFromCallback();
			*/
		}
		else
			MrgoodController.getInstance().animationController.ResumeAnimationsFromCallback();
		
		/*
		GameStatics.setScoreBalance(true, new GuiAnimationCallback() {
			
			@Override
			public void success() {
				
				
			}
		}); */
		
	}
	
	void canRequestSpin()
	{
		
		if(StripController.bSpinRunning)
			return;
		if(MrgoodController.getInstance().sActiveScene != "main")
			return;
		
		if(MrgoodController.getInstance().bAutoSpinEnabled )
		{
			if( MrgoodController.getInstance().bAutoSpinPaused)
				return;
			
			if(MrgoodController.getInstance().iAutoSpinCount == 0)
			{
				GWT.log("iAutoSpinCount is 0");
				MrgoodController.getInstance().iAutoSpinCount = MrgoodController.getInstance().iDefaultAutoSpinCount;
				MrgoodController.getInstance().bAutoSpinPaused = true;
				enableGameButtons();
			}
			
			enableGameButtons();
			prepareAndStartSpin();
			
		
		}
		else
			enableGameButtons();
		
			
	}
	
	
	public void runMainGameUpdate()
	{

		CallBack back = new CallBack() {
			
			@Override
			public void onSuccess() {
				
				stage.update();
			}
		};
		dotick(back);
		
		Ticker.addListener(getWindow());
		Ticker.useRAF = true;
		Ticker.setFPS(30); 
	}
	
	public void stopGameViewUpdate()
	{
		Ticker.removeListener(getWindow());
	}
	
	public void disableGameView(boolean bRemoveElmView)
	{
		
		MrgoodController.getInstance().animationController.StopAnimations();
		guiGameView.StopYouWonBarAnimationIfActive();
		disableGameButtons(false);
	//	if(bRemoveElmView)
		//	MrgoodController.getInstance().elmMain.removeChild(elmGameView);
		
		
	}
	
	public void showBonusMessageBoard(int iTotalBonusScore)
	{
		guiGameView.showMessageBoard(iTotalBonusScore);
	}
	public void startBonusMessageBoardAnimation(final int iTotalBonusScore)
	{
		DataStorage.get().iWin += MrgoodController.getInstance().bonusView.iTotalBonusScore;
		DataStorage.get().dBalanceValue += (int)(MrgoodController.getInstance().bonusView.iTotalBonusScore*DataStorage.get().dBet_saved);//(int)(DataStorage.get().iWin*DataStorage.get().dBet_saved);
		GameStatics.setSpinResult(stripController.retStrips);
		guiGameView.startMessageBoardAnimation(iTotalBonusScore,new TimerCallback() {
			
			@Override
			public void fire() {
		//		MrgoodController.getInstance().bAutoSpinPaused = false;
		//		enableGameButtons();
				
				
				DataStorage.get().isBonusActive = false;
				MrgoodController.getInstance().bAutoSpinPaused = false;
				enableGameButtons();
				
				guiGameView.playYouWonBarAnimation(1500, DataStorage.get().iWin, new GuiAnimationCallback() {
					
					@Override
					public void success() {
					
						guiGameView.update();	
							
					}
				});
				
				GameStatics.isEnableAnimations = true;
				MrgoodController.getInstance().animationController.RunWinlineAnimation(new CallbackAnimationSquence() {
					
					@Override
					public void sucess() {
						
						
						MrgoodController.getInstance().animationController.RequestNewAnimation();
						
						MrgoodController.getInstance().bAutoSpinPaused = false;
						canRequestSpin();
					}
				});
				
				StripController.bSpinRunning = false;
				
				
				
			}
		});
	}
	
	private static native void setAutoSpinCount(Element el,String sCount)/*-{
	
	  $wnd.$('.auto_spin_counter').text(sCount);
	}-*/;
	
	private static native void removeClassName(Element el,String sClass)/*-{
	
	  $wnd.$(el).removeClass(sClass)
	}-*/;
	
	private static native void addClassName(Element el,String sClass)/*-{
	
	  $wnd.$(el).addClass(sClass)
	}-*/;
	
	private static native JavaScriptObject getWindow()/*-{
		return $wnd;
	}-*/;

	private static native void dotick(CallBack callBack) /*-{
		$wnd.tick=function(){
			callBack.@com.sinewavemultimedia.easeljs.framework.js.CallBack::onSuccess()();
	}}-*/;





	


}
