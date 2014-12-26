package com.tmrtrn.mrgood.client.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.tmrtrn.mrgood.client.main.Preloader.CallbackLoadingView;
import com.tmrtrn.mrgood.client.main.Preloader.LoadingGameComponents;
import com.tmrtrn.mrgood.client.main.Preloader.LoadingScreen;
import com.tmrtrn.mrgood.client.main.animationtool.AnimationController;
import com.tmrtrn.mrgood.client.main.animationtool.CallbackAnimationSquence;
import com.tmrtrn.mrgood.client.main.bonusgame.BonusView;
import com.tmrtrn.mrgood.client.main.doubleup.DoubleUpView;
import com.tmrtrn.mrgood.client.main.gameView.GameView;
import com.tmrtrn.mrgood.client.main.rpc.Connection;
import com.tmrtrn.mrgood.client.main.rpc.DataStorage;
import com.tmrtrn.mrgood.client.main.settings.SettingsView;
import com.tmrtrn.mrgood.client.main.sounds.AudioSprite;
import com.tmrtrn.mrgood.client.main.sounds.SoundEffects;
import com.tmrtrn.mrgood.client.main.spinner.OnSpinEndedListener;
import com.tmrtrn.mrgood.client.main.spinner.OnSpinRequestedListener;
import com.tmrtrn.mrgood.client.main.tools.CallbackButton;
import com.tmrtrn.mrgood.client.main.utility.GameParameters;
import com.tmrtrn.mrgood.client.main.utility.NativeHelper;

public class MrgoodController {
	
	private static MrgoodController mrgoodInstance;
	
	public Element elmMain;
	public GameUtility utility;
	public LoadingScreen loadingScreen;
	public GameView gameView;
	public BonusView bonusView;
	public SettingsView settingsView;
	public DoubleUpView doubleUpView;
	public AnimationController animationController;
	
	private Connection conn;
	
	public boolean bBackgroundAssetsLoaded = false;
	
	public boolean bGamblingEnabled = true;
	public String sActiveScene = "main";
	
	public boolean bAutoSpinEnabled = false;
	public boolean bAutoSpinPaused = true;
	public int iAutoSpinCount = 0;
	public int iDefaultAutoSpinCount = 5;
	public boolean bAutoSpinIgnoreDoubleup = false;
	
	
//	public Element elmOrientationLock;
	
	public static MrgoodController getInstance()
	{
		if(mrgoodInstance == null)
		{
			mrgoodInstance = new MrgoodController();
			
		}
		return mrgoodInstance;
	}
	//init.initMobileProfile();
	public native void initJS()/*-{
	
			 $wnd.doStuff();
			
	}-*/;
	
	public MrgoodController()
	{
		GWT.log("new Mr good controller");
		
		GameParameters.PrepareLineData();
		
		elmMain = NativeHelper.getMainElement();//Document.get().createElement("div");
	//	elmMain.setClassName("MainCanvas");
		RootPanel.get().getElement().appendChild(elmMain);
		
	//	elmOrientationLock = Document.get().createElement("div");
	//	elmOrientationLock.setClassName("orientationLock");
	//	elmOrientationLock.getStyle().setVisibility(Visibility.HIDDEN);
	//	RootPanel.get().getElement().appendChild(elmOrientationLock);

		LoadingGameComponents.PrepareLoadingObjects();
	/*	
		if(MGWT.getOrientation() == ORIENTATION.LANDSCAPE)
			elmOrientationLock.getStyle().setVisibility(Visibility.HIDDEN);
		if(MGWT.getOrientation() == ORIENTATION.PORTRAIT)
			elmOrientationLock.getStyle().setVisibility(Visibility.VISIBLE);
		*/
		utility = new GameUtility();
	//	utility.lockOrientation(elmOrientationLock);
		
		bonusView = new BonusView();
		settingsView = new SettingsView();
		doubleUpView = new DoubleUpView();
		
		animationController = new AnimationController();
		addOnSpinEndedListener(animationController);
		addOnSpinRequestedListener(animationController);
		
		
		
	}
	boolean bLoadingcompleted = false;
	
	public void showLoadingView()
	{
		
		bLoadingcompleted = false;
		loadingScreen = new LoadingScreen();
		loadingScreen.loadingCompleted(new LoadGameView());
		loadingScreen.load(elmMain,LoadingGameComponents.images);
		
	/*	SoundEffects.get().soundTool.askEnableSounds(new CallbackButton() {
			
			@Override
			public void pressed() {
				if(bLoadingcompleted)
					gameView.enableGameButtons();
			}
		},loadingScreen.stageObject.getElement());
		*/
	}
	
	String allEntry = "";
	
	public void addEntry(String txt)
	{
	//	allEntry = txt +"</br>" + allEntry;
	//	NativeHelper.getLogElement().setInnerHTML(allEntry);
	}
	
	
	private class LoadGameView implements CallbackLoadingView
	{
		@Override
		public void success(Map<String, JavaScriptObject> loadedComponents) {
			
			utility.initialize(loadedComponents);
			gameView = new GameView();
			gameView.showGameView();
			bLoadingcompleted = true;
		
			SoundEffects.get().render(new CallbackButton() {
				
				@Override
				public void pressed() {
					gameView.loadGameType();
					if(!DataStorage.get().isClickmeActive)
						gameView.enableGameButtons();
					
				}
			},gameView.stageJSObject.getElement());
		} 
	}
	
	
	
	
	public void showBonusView()
	{
		MrgoodController.getInstance().sActiveScene ="bonus";
		bonusView.runWinLineAnimation(new CallbackAnimationSquence() {
			
			@Override
			public void sucess() {
				bonusView.destroyWinLine();
				gameView.disableGameView(true);
				gameView.elmGameView.getStyle().setOpacity(0);
				bonusView.showBonusView();
				
			}
		});
		
	}

	public void endBonusViewCalled(int iTotalBonusScore)
	{
		gameView.runMainGameUpdate();
		gameView.startBonusMessageBoardAnimation(iTotalBonusScore);
		MrgoodController.getInstance().sActiveScene ="main";
	}

	
	public void endPaytableViewCalled()
	{
		gameView.enableGameButtons();
	}
	
	public void endSettingsView()
	{
		MrgoodController.getInstance().sActiveScene ="main";
		gameView.enableGameButtons();
		gameView.runMainGameUpdate();
		
		if(MrgoodController.getInstance().bAutoSpinEnabled)
		{
			 MrgoodController.getInstance().bAutoSpinPaused = true;
			 gameView.changeActionButtonStyle("auto_spin");
		}
	}
	
	public void showSettingsView()
	{
		MrgoodController.getInstance().sActiveScene ="settings";
		SoundEffects.get().playSound("finger_click");
		settingsView.showSettingsView();
	}
	
	
	public void showDoubleUpView()
	{
		MrgoodController.getInstance().sActiveScene ="double";
		gameView.disableGameButtons(true);
		doubleUpView.showDoubleUpView();
		doubleUpView.balance_saved = DataStorage.get().dBalanceValue;
		DataStorage.get().dBalanceValue -= DataStorage.get().iWin * DataStorage.get().dBet;
		gameView.guiGameView.update();
	}
	
	public void endDoubleUpView(boolean active_doubleup, int win)
	{
		MrgoodController.getInstance().sActiveScene ="main";
		DataStorage.get().dBalanceValue = doubleUpView.balance_saved;
		DataStorage.get().iWin = win;
		gameView.guiGameView.update();
		DataStorage.get().bDoubleUpActive = active_doubleup;
		MrgoodController.getInstance().bAutoSpinPaused = true;
		gameView.enableGameButtons();
		
	}
	
	private List<TotalBetListener> totalBetListeners = new ArrayList<TotalBetListener>();
	
	public void addTotalBetChangedListener(TotalBetListener toAdd)
	{
		totalBetListeners.add(toAdd);
	}
	
	public void removeTotalBetChangedListener(TotalBetListener toRemove)
	{
		totalBetListeners.remove(toRemove);
	}
	
	 public void changedBetValueNotification() 
	 {
	    for (TotalBetListener tb : totalBetListeners)
	            tb.ChangedBetValue();
	 }
	 
	 private List<OnSpinRequestedListener> spinRequestedListeners = new ArrayList<OnSpinRequestedListener>();
	 
	 public void addOnSpinRequestedListener(OnSpinRequestedListener toAdd)
	 {
		 spinRequestedListeners.add(toAdd);
	 }
	 
	 public void removeOnSpinRequestedListener(OnSpinRequestedListener toRemove)
	 {
		 spinRequestedListeners.remove(toRemove);
	 }
	 
	 public void triggerOnSpinRequested()
	 {
		 for(OnSpinRequestedListener sr : spinRequestedListeners)
			 sr.onSpinRequested();
	 }
	 
	 
	 private List<OnSpinEndedListener> spinEndedListeners = new ArrayList<OnSpinEndedListener>();
	 
	 public void addOnSpinEndedListener(OnSpinEndedListener toAdd)
	 {
		 spinEndedListeners.add(toAdd);
	 }
	 
	 public void removeOnSpinEndedListener(OnSpinEndedListener toRemove)
	 {
		 spinEndedListeners.remove(toRemove);
	 }
	 
	 public void triggerOnSpinEnded()
	 {
		 for(OnSpinEndedListener se : spinEndedListeners)
			 se.onSpinEnded();
	 }
	 
	 
	 
	 public Connection getConnection()
	 {
		 if(conn == null)
			 conn = new Connection();
		 return conn;
	 }
	 
	 public boolean bBlockAllInputs = false;
	 public void disableAllControls()
	 {
		 bBlockAllInputs = true;
		 if(gameView != null)
		 {
			gameView.disableGameButtons(false);
			 if(gameView.guiGameView != null)
			 {
				 gameView.guiGameView.btnSettings.removeClickListener();
			 }
			 
		 }
		 if(bonusView != null)
		 {
			 bonusView.bBlockAllInput = true;
		 }
		
	 }
	
}
