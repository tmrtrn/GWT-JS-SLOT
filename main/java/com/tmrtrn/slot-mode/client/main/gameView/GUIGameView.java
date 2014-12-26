package com.tmrtrn.mrgood.client.main.gameView;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.sinewavemultimedia.easeljs.framework.js.CallBackTween;
import com.sinewavemultimedia.easeljs.framework.js.display.Bitmap;
import com.sinewavemultimedia.easeljs.framework.js.display.Container;
import com.sinewavemultimedia.easeljs.framework.js.display.Text;
import com.sinewavemultimedia.easeljs.framework.js.tween.Property;
import com.sinewavemultimedia.easeljs.framework.js.tween.Tween;
import com.tmrtrn.mrgood.client.main.Activity;
import com.tmrtrn.mrgood.client.main.GameStatics;
import com.tmrtrn.mrgood.client.main.GameUtility;
import com.tmrtrn.mrgood.client.main.MrgoodController;
import com.tmrtrn.mrgood.client.main.TimerCallback;
import com.tmrtrn.mrgood.client.main.TotalBetListener;
import com.tmrtrn.mrgood.client.main.rpc.DataStorage;
import com.tmrtrn.mrgood.client.main.rpc.JsonAnalyzer;
import com.tmrtrn.mrgood.client.main.sounds.SoundEffects;
import com.tmrtrn.mrgood.client.main.spinner.OnSpinEndedListener;
import com.tmrtrn.mrgood.client.main.spinner.OnSpinRequestedListener;
import com.tmrtrn.mrgood.client.main.spinner.StripController;
import com.tmrtrn.mrgood.client.main.tools.CallbackButton;
import com.tmrtrn.mrgood.client.main.tools.GameButton;
import com.tmrtrn.mrgood.client.main.tools.GameCounterText;
import com.tmrtrn.mrgood.client.main.utility.CallbackUpdateTween;
import com.tmrtrn.mrgood.client.main.utility.CallbackUtilityTimer;
import com.tmrtrn.mrgood.client.main.utility.GameParameters;
import com.tmrtrn.mrgood.client.main.utility.NativeHelper;
import com.tmrtrn.mrgood.client.main.utility.Utility;

public class GUIGameView  implements TotalBetListener, OnSpinEndedListener{
	
	public GUIGameView()
	{
		
	}
	
	public Element el_gamefooter_base;
	public Element el_footerlargepanel;
	public Element el_footersmallpanel;
	Element el_large_panel_extension;
	Element el_lpe_txtWrapper_num;
	Element el_detailWin_txtWrapper_num;
	Element el_detailBet_txtWrapper_num;
	Element el_ldp_bet_txtWrapper_num;
	Element el_ldp_balance_txtWrapper_num;
	Element el_detailBalance_txtWrapper_num;
	
	LinebetIndicatorControl indicatorControl;
	
	public void display(Element el)
	{
		el_gamefooter_base = Document.get().createElement("div");
		el_gamefooter_base.addClassName("gamefooterbase");
		el.appendChild(el_gamefooter_base);
		
		initializeCurrencyFormat(this);
		
		el_large_panel_extension = Document.get().createDivElement();
		el_large_panel_extension.addClassName("large_panel_extension hidden");
		el_gamefooter_base.appendChild(el_large_panel_extension);
		
		Element el_lpe_sum = Document.get().createDivElement();
		el_lpe_sum.addClassName("sum");
		el_large_panel_extension.appendChild(el_lpe_sum);
		
		Element el_lpe_txtWrapper = Document.get().createSpanElement();
		el_lpe_txtWrapper.addClassName("txtWrapper");
		el_lpe_sum.appendChild(el_lpe_txtWrapper);
		
		Element el_lpe_txtWrapper_txt = Document.get().createSpanElement();
		el_lpe_txtWrapper_txt.addClassName("txt");
		el_lpe_txtWrapper.appendChild(el_lpe_txtWrapper_txt);
		el_lpe_txtWrapper_txt.setInnerHTML(JsonAnalyzer.getJsonDataTypeofString(DataStorage.get().jsoLocale, "YOU_WON")+" ");
		
		 NativeHelper.textfill(38, 15, el_lpe_txtWrapper_txt, 170, 70, false);
		
		el_lpe_txtWrapper_num = Document.get().createSpanElement();
		el_lpe_txtWrapper_num.addClassName("sum");
		el_lpe_txtWrapper.appendChild(el_lpe_txtWrapper_num);
		el_lpe_txtWrapper_num.setInnerText(" "+0);
		
		
		
		btnHome = new GameButton("gamefooterbase utility left home");
		el_gamefooter_base.appendChild(btnHome.elmButton);
		Element el_homeicon = Document.get().createSpanElement();
		el_homeicon.addClassName("icon home");
		btnHome.elmButton.appendChild(el_homeicon);
		
		btnHome.addClickListener(new pressedHomeButton());
		
		btnSettings = new GameButton("gamefooterbase utility right");
		el_gamefooter_base.appendChild(btnSettings.elmButton);
		Element el_settingsicon = Document.get().createSpanElement();
		el_settingsicon.addClassName("icon settings");
		btnSettings.elmButton.appendChild(el_settingsicon);
		
	
		/*
		 * Parent : <div class="small_panel">
		 */
		el_footersmallpanel = Document.get().createDivElement();
		el_footersmallpanel.addClassName("gamefooterbase small_panel");
		el_gamefooter_base.appendChild(el_footersmallpanel);
		
		
	
		/*
		 * <div class="detail balance">
		 */
		el_detailBalance = Document.get().createDivElement(); 
		el_detailBalance.addClassName("detail balance");
		el_footersmallpanel.appendChild(el_detailBalance);
		
		Element el_detailBalance_txtWrapper = Document.get().createSpanElement();
		el_detailBalance_txtWrapper.addClassName("txtWrapper");
		el_detailBalance.appendChild(el_detailBalance_txtWrapper);
		Element el_detailBalance_txtWrapper_txt =  Document.get().createSpanElement();
	//	el__detailBalance_txtWrapper_txt.addClassName("txt"); TODO : language support
		el_detailBalance_txtWrapper.appendChild(el_detailBalance_txtWrapper_txt);
		el_detailBalance_txtWrapper_txt.setInnerText(JsonAnalyzer.getJsonDataTypeofString(DataStorage.get().jsoLocale, "BALANCE")+":");
		el_detailBalance_txtWrapper_num = Document.get().createSpanElement();
		el_detailBalance_txtWrapper_num.addClassName("num");
		el_detailBalance.appendChild(el_detailBalance_txtWrapper_num);
		el_detailBalance_txtWrapper_num.setInnerHTML("<span>"+ format(this,(int)DataStorage.get().dBalanceValue,false) + "</span>"); //\u20AC
		
		NativeHelper.textfillByParentProp(35, 10, el_detailBalance_txtWrapper_num.getElementsByTagName("span").getItem(0), false);
		
		/*
		 * <div class="detail bet">
		 */
		el_detailBet = Document.get().createDivElement(); 
		el_detailBet.addClassName("detail bet");
		el_footersmallpanel.appendChild(el_detailBet);
		
		Element el_detailBet_txtWrapper = Document.get().createSpanElement();
		el_detailBet_txtWrapper.addClassName("txtWrapper");
		el_detailBet.appendChild(el_detailBet_txtWrapper);
		
		el_detailBet_txtWrapper_txt =  Document.get().createSpanElement();
//		el_detailBet_txtWrapper_txt.addClassName("txt"); TODO : language support
		el_detailBet_txtWrapper.appendChild(el_detailBet_txtWrapper_txt);
		el_detailBet_txtWrapper_txt.setInnerHTML("<span>"+JsonAnalyzer.getJsonDataTypeofString(DataStorage.get().jsoLocale, "BET")+":" +"</span>");
		el_detailBet_txtWrapper_num = Document.get().createSpanElement();
		el_detailBet_txtWrapper_num.addClassName("num");
		el_detailBet.appendChild(el_detailBet_txtWrapper_num);
		
		
		
		/*
		 * <div class="detail win">
		 */
		el_detailWin = Document.get().createDivElement(); 
		el_detailWin.addClassName("detail win");
		el_footersmallpanel.appendChild(el_detailWin);
		
		Element el_detailWin_txtWrapper = Document.get().createSpanElement();
		el_detailWin_txtWrapper.addClassName("txtWrapper");
		el_detailWin.appendChild(el_detailWin_txtWrapper);
		
		Element el_detailWin_txtWrapper_txt =  Document.get().createSpanElement();
//		el_detailWin_txtWrapper_txt.addClassName("txt"); TODO : language support
		el_detailWin_txtWrapper.appendChild(el_detailWin_txtWrapper_txt);
		el_detailWin_txtWrapper_txt.setInnerText(JsonAnalyzer.getJsonDataTypeofString(DataStorage.get().jsoLocale, "WIN")+":");
		el_detailWin_txtWrapper_num = Document.get().createSpanElement();
		el_detailWin_txtWrapper_num.addClassName("num");
		el_detailWin.appendChild(el_detailWin_txtWrapper_num);
		el_detailWin_txtWrapper_num.setInnerText(format(this,convertWinNumberOnSmallPanelToString(0,0),false)); //"\u20AC"
		
		
		
		/*
		 * <div class="large_panel">
		 */
		el_footerlargepanel = Document.get().createDivElement();
		el_footerlargepanel.addClassName("gamefooterbase large_panel");
		el_gamefooter_base.appendChild(el_footerlargepanel);
		
		/*
		 * <div class="detail balance">
		 */
		el_ldp_balance = Document.get().createDivElement(); 
		el_ldp_balance.addClassName("detail balance");
		el_footerlargepanel.appendChild(el_ldp_balance);
		
		Element el_ldp_balance_txtWrapper = Document.get().createSpanElement();
		el_ldp_balance_txtWrapper.addClassName("txtWrapper");
		el_ldp_balance.appendChild(el_ldp_balance_txtWrapper);
		Element el_ldp_balance_txtWrapper_txt =  Document.get().createSpanElement();
	//	el_ldp_balance_txtWrapper_txt.addClassName("txt"); TODO : language support
		el_ldp_balance_txtWrapper.appendChild(el_ldp_balance_txtWrapper_txt);
		el_ldp_balance_txtWrapper_txt.addClassName("txt");
		el_ldp_balance_txtWrapper_txt.setInnerHTML(JsonAnalyzer.getJsonDataTypeofString(DataStorage.get().jsoLocale, "COINS")+":");
		NativeHelper.textfillByParentProp(29, 15, el_ldp_balance_txtWrapper_txt, false);
		el_ldp_balance_txtWrapper_num = Document.get().createSpanElement();
		el_ldp_balance_txtWrapper_num.addClassName("num");
		el_ldp_balance.appendChild(el_ldp_balance_txtWrapper_num);
		el_ldp_balance_txtWrapper_num.setInnerText(format(this,(int)DataStorage.get().dBalanceValue,true));
		
		/*
		 * <div class="detail bet">
		 */
		Element el_ldp_bet = Document.get().createDivElement(); 
		el_ldp_bet.addClassName("detail bet");
		el_footerlargepanel.appendChild(el_ldp_bet);
		
		Element el_ldp_bet_txtWrapper = Document.get().createSpanElement();
		el_ldp_bet_txtWrapper.addClassName("txtWrapper");
		el_ldp_bet.appendChild(el_ldp_bet_txtWrapper);
		Element el_ldp_bet_txtWrapper_txt =  Document.get().createSpanElement();
	//	el_ldp_bet_txtWrapper_txt.addClassName("txt"); TODO : language support
		el_ldp_bet_txtWrapper.appendChild(el_ldp_bet_txtWrapper_txt);
		el_ldp_bet_txtWrapper_txt.setInnerHTML("<span>"+JsonAnalyzer.getJsonDataTypeofString(DataStorage.get().jsoLocale, "BET")+":"+"</span>");
		NativeHelper.textfillByParentProp(29, 15, el_ldp_bet_txtWrapper_txt, false);
		el_ldp_bet_txtWrapper_num = Document.get().createSpanElement();
		el_ldp_bet_txtWrapper_num.addClassName("num");
		el_ldp_bet.appendChild(el_ldp_bet_txtWrapper_num);
	//	el_ldp_bet_txtWrapper_num.setInnerText("\u20AC"+toFixed(dBetInCash/100));
		
		
		
//		update();
		
		
		//TODO : line Bet indicators (1-5)
	//	UpdateLineBetIndicator();
		indicatorControl = new LinebetIndicatorControl();
		indicatorControl.createIndicatorElements();
		
		
		
		MrgoodController.getInstance().addTotalBetChangedListener(this);
		MrgoodController.getInstance().changedBetValueNotification();
		
		MrgoodController.getInstance().addOnSpinEndedListener(this);
		
	}
	
	Element el_detailBet_txtWrapper_txt;
	Element el_ldp_balance;
	Element el_detailWin;
	Element el_detailBet;
	Element el_detailBalance;
	
	private Container containerMessageBoard;
	private GameCounterText txtBonusScore;
	public GameButton btnSettings;
	protected GameButton btnHome;
	
	
	
	
	private Text[] txtArrayLineBetIndicator = null;
	
	public void UpdateLineBetIndicator()
	{
	/*	if(txtArrayLineBetIndicator == null)
		{
			txtArrayLineBetIndicator = new Text[30];
			for(int i=0; i<30; i++)
			{
				Text txtIndicator = Text.create(""+DataStorage.get().lines_bet, "18px Arial bold", "#ceac2b");
				int iLeft = i/15;
				
				txtIndicator.setX(42+ (iLeft*835));
				txtIndicator.setY(102+(i%15)*35);
				MrgoodController.getInstance().gameView.stage.addChild(txtIndicator);
			}
		}
		*/
	}
	
	class pressedHomeButton implements CallbackButton
	{

		@Override
		public void pressed() {
		//	historyback();
			NativeHelper.href(DataStorage.get().sHome_url);
		}
		
	}
	
	public void playYouWonBarAnimation(final double dInitDuration,final int iScore,final GuiAnimationCallback endCallback)
	{
		if(!MrgoodController.getInstance().sActiveScene.equalsIgnoreCase("main") )
		{
			if(endCallback != null)
				endCallback.success();
			return;
		}
		el_large_panel_extension.removeClassName("hidden");
		
		Utility.UpdatePoint(iScore, (int) dInitDuration, new CallbackUpdateTween() {
			
			@Override
			public void update(int iPoint) {
				
				el_lpe_txtWrapper_num.setInnerText(" "+iPoint);
			}
			
			@Override
			public void end() {
				
				el_lpe_txtWrapper_num.setInnerText(" "+iScore);
				Utility.WaitForSeconds(1000, new CallbackUtilityTimer() {
					
					@Override
					public void onCompletedWaiting() {
						el_large_panel_extension.addClassName("hidden");
						StopYouWonBarAnimationIfActive();
						if(endCallback != null)
							endCallback.success();
					}
				});
			}
		});
		
	}
	
	public void StopYouWonBarAnimationIfActive()
	{
		el_large_panel_extension.addClassName("hidden");
	}

	
	public void showMessageBoard(final int iTotalBonusScore)
	{
		GWT.log("showMessageBoard");
		containerMessageBoard = Container.createContainer();
		Container contMessageBoxFrame = Container.createContainer();
		contMessageBoxFrame.addChild(MrgoodController.getInstance().utility.GetGuiImage("imgMessageBoardFrame",false));
		
		Container contMessageBoxInView = Container.createContainer();
		contMessageBoxInView.addChild(MrgoodController.getInstance().utility.GetGuiImage("imgMessageBoard",false));
		contMessageBoxInView.setX(21);
		contMessageBoxInView.setY(21);
		
		contMessageBoxFrame.addChild(contMessageBoxInView);
		containerMessageBoard.addChild(contMessageBoxFrame);
		MrgoodController.getInstance().gameView.stage.addChild(containerMessageBoard);
		containerMessageBoard.setX(240);
		containerMessageBoard.setY(83);
		
		
		txtBonusScore = new GameCounterText();//Text.create(""+iTotalBonusScore, "100px Arial bold", "#ceac2b");
	//	txtBonusScore.setTextAlign("center");
		int iLeftOffsetCounter = 0;
		if(iTotalBonusScore >= 10 && iTotalBonusScore <100)
			iLeftOffsetCounter = 1;
		else if(iTotalBonusScore >= 100 && iTotalBonusScore <1000)
			iLeftOffsetCounter = 2;
		else if(iTotalBonusScore >= 1000)
			iLeftOffsetCounter = 3;
		
		
		txtBonusScore.setX(235 - iLeftOffsetCounter*31.5);
		txtBonusScore.setY(208);
		txtBonusScore.setText(""+iTotalBonusScore,false);
		txtBonusScore.setScaleX(1.8);
		txtBonusScore.setScaleY(1.8);
		containerMessageBoard.addChild(txtBonusScore.containerText);
		MrgoodController.getInstance().gameView.stage.update();
		
	}
	
	
	int iLastValueMessageBoard = 0;
	
	public void startMessageBoardAnimation(final int iTotalBonusScore,final TimerCallback callbackEnd)
	{
		SoundEffects.get().playSound("bonuscount");
		iLastValueMessageBoard = 0;
		Utility.UpdatePoint(iTotalBonusScore, 2000, new CallbackUpdateTween() {
			
			@Override
			public void update(int iPoint) {
				
				if(iPoint - iLastValueMessageBoard > 18 || iPoint == iTotalBonusScore)
				{
					int iPrint = iTotalBonusScore-iPoint;
					int iLeftOffsetCounter = 0;
					if(iPrint >= 10 && iPrint <100)
						iLeftOffsetCounter = 1;
					else if(iPrint >= 100 && iPrint <1000)
						iLeftOffsetCounter = 2;
					else if(iPrint >= 1000)
						iLeftOffsetCounter = 3;
					
					txtBonusScore.setX(240 - iLeftOffsetCounter*31.5); //- iLeftOffsetCounter*17.5
					
					txtBonusScore.setText(""+iPrint,false);
				}
				
				
			}
			
			@Override
			public void end() {
				SoundEffects.get().playSound("slience");
				Utility.WaitForSeconds(100, new CallbackUtilityTimer() {
					
					@Override
					public void onCompletedWaiting() {
						
						containerMessageBoard.removeAllChildren();
						MrgoodController.getInstance().gameView.stage.removeChild(containerMessageBoard);
						callbackEnd.fire();
					}
				});
			}
		});
	}

	@Override
	public void onSpinEnded() {
		
	

	}
	
	public void playWinBarAndUpdate()
	{
		
		el_detailWin_txtWrapper_num.setInnerText(""+format(this,convertWinNumberOnSmallPanelToString(+DataStorage.get().iWin, DataStorage.get().dBet_saved),false));
		
		
		GameStatics.setScoreBalance(true, new GuiAnimationCallback() {

			@Override
			public void success() {
				
				update();
				
			}
			
		}); 
	}
	
	public void CallbackAnimationRepeat()
	{
		if(DataStorage.get().isBonusActive) // || DataStorage.get().isClickmeActive
			return;
		
	}
	
	

	
	@Override
	public void ChangedBetValue() {

		updateBet();
	
	}
	
	private native String toFixed(double dVal)
	/*-{
			var ret = dVal.toFixed(2);
			return ret;
	}-*/;




	public void receivedSpinRequested() {
		
		StopYouWonBarAnimationIfActive();
		
		
		update();
		
	}
	
	
	
	public void update()
	{
		updateBet();
		updateBalance();
		el_detailWin_txtWrapper_num.setInnerText(""+format(this,convertWinNumberOnSmallPanelToString(DataStorage.get().iWin, DataStorage.get().dBet_saved),false));
	}
	
	private native String convertWinNumberOnSmallPanelToString(int iWin,double dBet)
	/*-{
			return (iWin * dBet / 100).toFixed(2);
	}-*/;
	
	private void updateBalance()
	{
		el_ldp_balance_txtWrapper_num.setInnerText(""+format(this,convertCoinNumberOnLargePanelToString(DataStorage.get().dBalanceValue, DataStorage.get().dBet),true));
		el_detailBalance_txtWrapper_num.setInnerHTML("<span>"+format(this,convertBalanceNumberOnSmallPanelToString(DataStorage.get().dBalanceValue),false)+"</span>");
	}
	
	//return Math.floor(Math.max(dBalance,0) / dBet).toString();
	private native String convertCoinNumberOnLargePanelToString(double dBalance,double dBet)
	/*-{
//		$wnd.alert('dBalance '+dBalance+' dBet: '+dBet +' : '+(dBalance / dBet));
			return Math.floor(Math.max(dBalance,0) / dBet).toString();
	}-*/;
	
	private native String convertBalanceNumberOnSmallPanelToString(double dBalance)
	/*-{
			return (Math.max(dBalance,0) / 100).toFixed(2);
	}-*/;
	

	private void updateBet()
	{
		el_ldp_bet_txtWrapper_num.setInnerHTML("<span>"+format(this, DataStorage.get().lines_bet * DataStorage.get().lines, true)+"</span>");
		el_detailBet_txtWrapper_num.setInnerHTML("<span>"+format(this,toFixed(DataStorage.get().dBet*DataStorage.get().lines_bet * DataStorage.get().lines/100),false)+"</span>");
		updateBalance();
		adjustBottomBarTexts();
	}
	
	private void adjustBottomBarTexts()
	{
//		Window.alert(""+NativeHelper.getElementPositionLeft(el_ldp_bet)+" width "+NativeHelper.getElementWidth(el_ldp_bet) +" , "+NativeHelper.getElementPositionLeft(el_detailWin));
		double dLeftBetNow = NativeHelper.getElementPositionLeft(el_detailBet);		
		double dLeftWinNow = NativeHelper.getElementPositionLeft(el_detailWin);
		
		double dWidthBet = NativeHelper.getElementWidth(el_detailBet);
		double dBetEndPos = dLeftBetNow + dWidthBet;
		double dLeftBet = dLeftBetNow;
		while(dBetEndPos > dLeftWinNow)
		{
			dLeftBet -= 8;
			NativeHelper.setElementPositionLeft(el_detailBet, dLeftBet);
			dLeftBetNow = NativeHelper.getElementPositionLeft(el_detailBet);	
			dBetEndPos = dLeftBetNow + dWidthBet;
		}
		
		double dLeftBalance = NativeHelper.getElementPositionLeft(el_detailBalance);
		double dWidthBalance = NativeHelper.getElementWidth(el_detailBalance);
		double dBalanceEndPos = dLeftBalance + dWidthBalance;
		while(dBalanceEndPos > dLeftBetNow)
		{
			dLeftBalance -=3;
			NativeHelper.setElementPositionLeft(el_detailBalance, dLeftBalance);
			dBalanceEndPos =  NativeHelper.getElementPositionLeft(el_detailBalance)+dWidthBalance;	
		}
	}
	
	private String sCurrencyName = "";
	private String sCurrencySign = "";
	
	private native void initializeCurrencyFormat(GUIGameView x)
	/*-{
			var currencySign = $wnd.location.search.match(/currency_symbol=([^&]*)/);
			var sign = currencySign ? decodeURIComponent(currencySign[1]) : '';
			x.@com.tmrtrn.mrgood.client.main.gameView.GUIGameView::sCurrencyName = sign;
			x.@com.tmrtrn.mrgood.client.main.gameView.GUIGameView::sCurrencySign = sign;
			
	}-*/;
	
	
	private native String format(GUIGameView x,int num,boolean removeSign)
	/*-{
			return [removeSign ? "" : x.@com.tmrtrn.mrgood.client.main.gameView.GUIGameView::sCurrencySign + ' ', num.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,")].join('');
	}-*/;
	
	private native String format(GUIGameView x,String num,boolean removeSign)
	/*-{
			return [removeSign ? "" : x.@com.tmrtrn.mrgood.client.main.gameView.GUIGameView::sCurrencySign + ' ', num.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,")].join('');
	}-*/;
	
	
	private final native void historyback()
	/*-{ 
		history.back();
	}-*/; 
	
	
}
