package com.tmrtrn.mrgood.client.main.gameView;

import java.util.ArrayList;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.sinewavemultimedia.easeljs.framework.js.Image;
import com.sinewavemultimedia.easeljs.framework.js.display.Bitmap;
import com.sinewavemultimedia.easeljs.framework.js.display.Container;
import com.sinewavemultimedia.easeljs.framework.js.display.Graphics;
import com.sinewavemultimedia.easeljs.framework.js.display.Shape;
import com.tmrtrn.mrgood.client.main.Activity;
import com.tmrtrn.mrgood.client.main.GameStatics;
import com.tmrtrn.mrgood.client.main.MrgoodController;
import com.tmrtrn.mrgood.client.main.animationtool.AnimationSquence;
import com.tmrtrn.mrgood.client.main.animationtool.CallbackAnimationSquence;
import com.tmrtrn.mrgood.client.main.rpc.CallbackAction;
import com.tmrtrn.mrgood.client.main.rpc.DataStorage;
import com.tmrtrn.mrgood.client.main.sounds.SoundEffects;
import com.tmrtrn.mrgood.client.main.spinner.CallbackStartSpin;
import com.tmrtrn.mrgood.client.main.spinner.CallbackStopSpin;
import com.tmrtrn.mrgood.client.main.spinner.Poster;
import com.tmrtrn.mrgood.client.main.spinner.StripPair;
import com.tmrtrn.mrgood.client.main.tools.CallbackClickmeButton;
import com.tmrtrn.mrgood.client.main.tools.ClickmeButton;
import com.tmrtrn.mrgood.client.main.tools.ClickmeButton.CallbackIncreaseClikmeCount;
import com.tmrtrn.mrgood.client.main.tools.GameButton;
import com.tmrtrn.mrgood.client.main.tools.GameCounterText;
import com.tmrtrn.mrgood.client.main.tools.GameLabel;
import com.tmrtrn.mrgood.client.main.utility.CallbackUtilityTimer;
import com.tmrtrn.mrgood.client.main.utility.Utility;

public class Clickme {
	
	ClickmeButton[] clickmeButtons;
	int iSelectedButton;
	
	public Clickme()
	{
		initialize();
	}

	void initialize()
	{
		//MrgoodController.getInstance().gameView.disableGameButtons(false);
		
		
		iaDefaultReels = new int[5][3];//DataStorage.get().iaReels;
		for(int i=0; i<iaDefaultReels.length; i++)
		{
			for(int j=0; j<iaDefaultReels[i].length; j++)
			{
				iaDefaultReels[i][j] = DataStorage.get().iaReels[i][j];
			}
		}
		
		Poster[][] posters = MrgoodController.getInstance().gameView.stripController.getPostersOnReels();
		final ArrayList<Poster> clickmeIcons = findclickmeIcons(posters);
		
		if(clickmeIcons.size() < 3)
		{
			addClickmeButtonToIcons();
			return;
		}
		final AnimationSquence anim = new AnimationSquence(-1, -1);
		for(int i=0; i<clickmeIcons.size(); i++)
		{
			anim.addPoster(clickmeIcons.get(i));
		}
		
		clickmeButtons = new ClickmeButton[clickmeIcons.size()];
		for(int i=0; i<clickmeIcons.size(); i++)
		{
			Poster p = clickmeIcons.get(i);
			clickmeButtons[i] = new ClickmeButton(i,"ButtonOnClickmeIcon");
			clickmeButtons[i].addClickmeTextImage();
			clickmeButtons[i].setPosition(p.dX+66, p.dY+97);
			clickmeButtons[i].rotateClickmeText(null,-1);
			clickmeButtons[i].poster = clickmeIcons.get(i);
		}
		
		
		startAdjustments(anim,new CallbackStopSpin() {
			
			@Override
			public void success(int iReelId) {
				
				//Ready click me
				if(iReelId == 4)
					addClickmeButtonToIcons();
			}
		});
		
		
	}
	
	
	private ArrayList<Poster> findclickmeIcons(Poster[][] posters)
	{
		ArrayList<Poster> clickmeIcons = new ArrayList<Poster>();
		for(int i=0; i<posters.length; i++ )
		{
			for(int j=0; j<posters[i].length; j++)
			{
				if(posters[i][j].getAttributionId() == 11)
				{
					
					clickmeIcons.add(posters[i][j]);
					break;
				}
			}
		}
		return clickmeIcons;
	}
	
	int[][] iaDefaultReels ;
	
	private void startAdjustments(final AnimationSquence anim, final CallbackStopSpin cbOK)
	{
		anim.Run(0,new CallbackAnimationSquence() {
			
			@Override
			public void sucess() {
				
				for(int i=0; i<clickmeButtons.length; i++)
				{
					clickmeButtons[i].removeClikmeText();
					clickmeButtons[i].remove();
				}
				destroyAnim(anim);
				
				int[][] iaNewReels = DataStorage.get().iaReels;
		//		iaNewReels = arrangeReelsByClickmeMiddlePositionOnReel(iaNewReels);
				DataStorage.get().iaReels = iaNewReels;
				cbOK.success(4);
		/*		MrgoodController.getInstance().gameView.stripController.startSpin(cbOK);
				Utility.WaitForSeconds(500, new CallbackUtilityTimer() {
					
					@Override
					public void onCompletedWaiting() {
						MrgoodController.getInstance().gameView.stripController.stopSpin();
					}
				});  */
			} 
		}); 
	}
	
	private int[][] arrangeReelsByClickmeMiddlePositionOnReel(int[][] iaReels)
	{
		int[][] iaNewReels = iaReels;
		for(int i=0; i<iaNewReels.length; i++)
		{
			for(int j=0; j<iaNewReels[i].length; j++)
			{
				if(iaNewReels[i][j] == 11)
				{
					if(j != 1) //2 is Top, 1 is middle, 0 is bottom
					{
					//	GWT.log("column i "+i+" j: "+j);
						int jMiddle = iaNewReels[i][1];
						iaNewReels[i][1] = iaNewReels[i][j];
						iaNewReels[i][j] = jMiddle;
					}
					break;
				}
			}
		}
		return iaNewReels;
	}
	
	
	
	private void addClickmeButtonToIcons()
	{
		Poster[][] posters = MrgoodController.getInstance().gameView.stripController.getPostersOnReels();
		ArrayList<Poster> clickmeIcons = findclickmeIcons(posters);
		
		if(clickmeIcons.size() < 3)
		{
			clickmeIcons = new ArrayList<Poster>();
			posters[0][1].setAttribution(11);
			Bitmap displayObj0 = MrgoodController.getInstance().utility.createPosterBitmap(posters[0][1], false);
			posters[0][1].containerBitmap.addChild(displayObj0);
			clickmeIcons.add(posters[0][1]);
			
			posters[2][1].setAttribution(11);
			Bitmap displayObj1 = MrgoodController.getInstance().utility.createPosterBitmap(posters[2][1], false);
			posters[2][1].containerBitmap.addChild(displayObj1);
			clickmeIcons.add(posters[2][1]);
			
			posters[4][1].setAttribution(11);
			Bitmap displayObj2 = MrgoodController.getInstance().utility.createPosterBitmap(posters[4][1], false);
			posters[4][1].containerBitmap.addChild(displayObj2);
			clickmeIcons.add(posters[4][1]);
		}
		
		clickmeButtons = new ClickmeButton[clickmeIcons.size()];
		for(int i=0; i<clickmeIcons.size(); i++)
		{
			Poster p = clickmeIcons.get(i);
			clickmeButtons[i] = new ClickmeButton(i,"ButtonOnClickmeIcon");
			clickmeButtons[i].addClickmeTextImage();
			clickmeButtons[i].setPosition(p.dX+66, p.dY+97);
			clickmeButtons[i].addClickListener(new PressedClickmeButton());
			clickmeButtons[i].startClickAndMeAnimation();
		}
	}
	
	class PressedClickmeButton implements CallbackClickmeButton
	{
		int iTotalReceivedConterEnded = 0;
		
		class onCounterEnded implements CallbackIncreaseClikmeCount
		{

			@Override
			public void success(int iButtonId) {
				
				if(iButtonId == iSelectedButton)
					SoundEffects.get().stopSound();
				
				iTotalReceivedConterEnded += 1;
				if(iTotalReceivedConterEnded == clickmeButtons.length)
				{
					Utility.WaitForSeconds(1000, new CallbackUtilityTimer() {
						
						@Override
						public void onCompletedWaiting() {
							
							
							int iTotalWin = DataStorage.get().iaClickmePayouts[DataStorage.get().iClickmeChoice -1];//DataStorage.get().iaClickmePayouts[0] + DataStorage.get().iaClickmePayouts[1] + DataStorage.get().iaClickmePayouts[2];
							DataStorage.get().dBalanceValue += iTotalWin*DataStorage.get().dBet_saved;
							DataStorage.get().iWin += iTotalWin;
							
				//			GameStatics.isEnableAnimations = true;
				//			MrgoodController.getInstance().animationController.RequestNewAnimation();
							
				//			MrgoodController.getInstance().gameView.guiGameView.update();
							
							new onEndClikMeSpin().success(4);
				/*			
							MrgoodController.getInstance().gameView.guiGameView.playYouWonBarAnimation(800, iTotalWin, new GuiAnimationCallback() {
								
								@Override
								public void success() {

									
				
								}
							});
							*/
							
							
						}
					});
				}
				
			}
			
		}

		@Override
		public void success(final int iId) {
			

			SoundEffects.get().playSound("clickmeposter");
			
			for(int i=0; i<clickmeButtons.length; i++)
			{
				clickmeButtons[i].removeClickListener();
			}
			
			Utility.WaitForSeconds(700, new CallbackUtilityTimer() {
				
				@Override
				public void onCompletedWaiting() {
					
					
					
					iSelectedButton = iId;
					DataStorage.get().iClickmeChoice = iSelectedButton + 1;
					MrgoodController.getInstance().getConnection().call("click_symbol", new CallbackAction() {
						
						@Override
						public void success() {
							
							final int[] iaClikmePayouts = DataStorage.get().iaClickmePayouts;
							iTotalReceivedConterEnded = 0;
							for(int i=0; i<clickmeButtons.length; i++)
							{
								
								clickmeButtons[i].setPosterOffIcon();
							}
							
							int iWaitForSound = SoundEffects.get().get_audio_enabled() ? 1000 : 0;
							Utility.WaitForSeconds(iWaitForSound, new CallbackUtilityTimer() {
								
								@Override
								public void onCompletedWaiting() {
									
									for(int i=0; i<clickmeButtons.length; i++)
									{
										if(i != iId)
										{
											clickmeButtons[i].addShadow(true);
											clickmeButtons[i].setInnerText(""+iaClikmePayouts[i], "#f1e9c9",new onCounterEnded(),i);
										}
										else
										{
											SoundEffects.get().playSound("clickmecounter");
											clickmeButtons[i].setInnerText(""+iaClikmePayouts[i], "#ceac2b",new onCounterEnded(),i);
										}
									}
								}
							});
							
						}
					});
					
					
				}
			});
			
			
			
			
			
			
		}
		
	}
	
	
	AnimationSquence animSincePlayerPressSpin;
	
	class onEndClikMeSpin implements CallbackStopSpin
	{

		@Override
		public void success(int iReelId) {
			
			if(iReelId != 4)
				return;
			
			DataStorage.get().iaReels = iaDefaultReels;
			
			for(int i=0; i<clickmeButtons.length; i++)
			{
				clickmeButtons[i].remove();
			}
			
			
			Poster[][] posters = MrgoodController.getInstance().gameView.stripController.getPostersOnReels();
			ArrayList<Poster> clickmeIcons = findclickmeIcons(posters);
			
			animSincePlayerPressSpin = new AnimationSquence(-1, -1);
			animSincePlayerPressSpin.saScoresColorToAllIcons = new boolean[clickmeIcons.size()];
			animSincePlayerPressSpin.iaScoresToAllIcons = new int[clickmeIcons.size()];
			for(int i=0; i<clickmeIcons.size(); i++)
			{
				animSincePlayerPressSpin.addPoster(clickmeIcons.get(i));
				
				
				if(iSelectedButton == i)
					animSincePlayerPressSpin.saScoresColorToAllIcons[i] = false;//"#ceac2b";
				else
				{
					animSincePlayerPressSpin.saScoresColorToAllIcons[i] = true;//"#f1e9c9";
				}
				if(i == iSelectedButton)
					animSincePlayerPressSpin.iaScoresToAllIcons[i] = DataStorage.get().iaClickmePayouts[i];
				else
					animSincePlayerPressSpin.iaScoresToAllIcons[i] = DataStorage.get().iaClickmePayouts[i];//-1;
				
				
			}
			
			
			clickmeButtons = new ClickmeButton[clickmeIcons.size()];
			for(int i=0; i<clickmeIcons.size(); i++)
			{
				Poster p = clickmeIcons.get(i);
				clickmeButtons[i] = new ClickmeButton(i,"ButtonOnClickmeIcon");
				
				clickmeButtons[i].addClickmeTextImage();
		//		if(iSelectedButton != i)
		//			clickmeButtons[i].addShadow();
				clickmeButtons[i].setPosition(p.dX+65, p.dY+97);
				
				
			}
			
			bCallNewAnim = true;
			MrgoodController.getInstance().gameView.cbSpinstart = new onSpinstarted();
			
			for(int i=0; i<clickmeIcons.size(); i++)
			{
				if(iSelectedButton != i)
				{
					Container contShadow = Container.createContainer();
					Graphics shadow = Graphics.createGraphics();//.beginFill("#FF0000").rect(0, 0, 170, 170);
					shadow = beginBitmapFill(shadow,MrgoodController.getInstance().utility.GetGuiImageAsImage("imgClikmeShadow",false), "repeat").rect(0, 0, 171, 171);
					Shape sShadow = Shape.createShape(shadow);
					sShadow.setAlpha(0.5);
					contShadow.addChild(sShadow);
					MrgoodController.getInstance().gameView.containerSpinPanel.addChild(contShadow);
					contShadow.setX(clickmeIcons.get(i).dX );
					contShadow.setY(clickmeIcons.get(i).dY + 97);
				}
				
			}
			
			GameStatics.setSpinResult(MrgoodController.getInstance().gameView.stripController.retStrips);
			DataStorage.get().isClickmeActive = false;
			MrgoodController.getInstance().bAutoSpinPaused = false;
			
			Utility.WaitForSeconds(1000, new CallbackUtilityTimer() {
				
				@Override
				public void onCompletedWaiting() {
					
					MrgoodController.getInstance().gameView.enableGameButtons();
					
					if(GameStatics.getRegisteredSpin() == null)
					{
						MrgoodController.getInstance().gameView.guiGameView.update();
						MrgoodController.getInstance().gameView.guiGameView.playYouWonBarAnimation(800, DataStorage.get().iWin, new GuiAnimationCallback() {
							
							@Override
							public void success() {
								
								MrgoodController.getInstance().gameView.canCancelAnimationsCallback();
								
								if(MrgoodController.getInstance().bAutoSpinEnabled)
								{
									MrgoodController.getInstance().bAutoSpinPaused = false;
									MrgoodController.getInstance().gameView.canRequestSpin();
									return;
								}
								
							}
						});
						return;
					}
					if(GameStatics.getRegisteredSpin().alWinLines.size() == 0)
					{
						
						MrgoodController.getInstance().gameView.guiGameView.playYouWonBarAnimation(800, DataStorage.get().iWin, new GuiAnimationCallback() {
							
							@Override
							public void success() {
								MrgoodController.getInstance().gameView.guiGameView.update();
								MrgoodController.getInstance().gameView.canCancelAnimationsCallback();
								
								if(MrgoodController.getInstance().bAutoSpinEnabled)
								{
									Utility.WaitForSeconds(2500, new CallbackUtilityTimer() {
										
										@Override
										public void onCompletedWaiting() {
											MrgoodController.getInstance().bAutoSpinPaused = false;
											MrgoodController.getInstance().gameView.canRequestSpin();
										}
									});
									
									return;
								}
								
							}
						});
					}
					else
					{
						MrgoodController.getInstance().gameView.guiGameView.playYouWonBarAnimation(500, DataStorage.get().iWin, new GuiAnimationCallback() {
							
							@Override
							public void success() {
								MrgoodController.getInstance().gameView.guiGameView.update();
							}
						});
						GameStatics.isEnableAnimations = true;
						MrgoodController.getInstance().animationController.RunWinlineAnimation(new CallbackAnimationSquence() {
							
							@Override
							public void sucess() {
								
								if(MrgoodController.getInstance().bAutoSpinEnabled && 
										!MrgoodController.getInstance().bAutoSpinPaused)
								{
									
									MrgoodController.getInstance().gameView.canRequestSpin();
									return;
								}
								//RequestNewAnim();
								MrgoodController.getInstance().animationController.RequestNewAnimation();
								
							}
						});
						MrgoodController.getInstance().gameView.canCancelAnimationsCallback();
						
					
						
					}
					
					
					
				}
			});
			
			
			
				
			
			
			
		}
		
		
		
	}
	
	boolean bCallNewAnim = true;
	
	
	
	
	public void destroy()
	{
		bCallNewAnim = false;
		for(int i=0; i<clickmeButtons.length; i++)
		{
			clickmeButtons[i].remove();
		}
		destroyAnim(animSincePlayerPressSpin);
	}
	
	private void destroyAnim(final AnimationSquence anim)
	{
		Utility.WaitForSeconds(50, new CallbackUtilityTimer() {
			
			@Override
			public void onCompletedWaiting() {
				anim.Destroy();
				
			}
		});
	}
	
	class onSpinstarted implements CallbackStartSpin
	{

		@Override
		public void success() {

			destroy();
			MrgoodController.getInstance().gameView.cbSpinstart = null;
		}
		
	}
	
	public final native Graphics beginBitmapFill(Graphics graph,Image image,String repetition)/*-{
	
	return graph.beginBitmapFill(image,repetition);
}-*/;
	
}
