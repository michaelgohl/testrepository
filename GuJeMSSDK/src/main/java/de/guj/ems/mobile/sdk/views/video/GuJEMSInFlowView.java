package de.guj.ems.mobile.sdk.views.video;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.ads.interactivemedia.v3.api.AdEvent;
import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer.VideoAdPlayerCallback;

import de.guj.ems.mobile.sdk.R;
import de.guj.ems.mobile.sdk.util.SdkLog;
import de.guj.ems.mobile.sdk.util.SdkUtil;
import de.guj.ems.mobile.sdk.util.ThirdPartyConnector;

public class GuJEMSInFlowView extends LinearLayout {

	private final static String TAG = "GuJEMSInFlowView";

	private GuJEMSVideoPlayer mVPlayer;
	private MediaPlayer mMPlayer;
	private LinearLayout mRootView;
	private ViewGroup mRootPlayerView;
	private int mDisplayHeight = 0;
	private int mDisplayWidth = 0;
	private boolean mPlayable = false;
	private String mAdUnit;
	private Animation closeAnimation;
	private Animation expandAnimation;
    private Context context = null;
	private int iconHeight = 32;
    private int targetHeight = 0;
	private float bottomEnd = -1;
	private float topEnd = -1;
	private boolean videoSoundMuted = true;
	private Boolean mRequested = false;
	private long startTime = -1;

	final class InFlowFocusChangedListener implements ViewTreeObserver.OnWindowFocusChangeListener {
		@Override
		public void onWindowFocusChanged(boolean hasFocus) {
			if (hasFocus) {
				int[] pos = new int[2];
				getLocationOnScreen(pos);
				if (mVPlayer != null) {
					mVPlayer.restorePosition();
				}
				if (!videoSoundMuted) {
					changeSoundState();
				}
				resumeOrStopInFlow(pos);
			}
		}
	};

	/* OnScrollChangedListener */
	final class InFlowScrollChangedListener implements OnScrollChangedListener {
		private boolean mDisplayed = false;
        private View v = null;
        private View parent = null;

		@Override
		public void onScrollChanged() {
			int[] pos = new int[2];
            if (this.parent == null) {
                View current = this.v;
                View max = this.v;
                try {
                    while (current.getParent() != null) {
                        if (((View)current.getParent()).getHeight() < mDisplayHeight && max.getHeight() < ((View)current.getParent()).getHeight()) {
                            max = (View)current.getParent();
                        }
                        current = (View)current.getParent();
                    }
                }catch(Exception e) {}
                this.parent = max;
            }
            getLocationOnScreen(pos);
			if (bottomEnd == -1) {
				bottomEnd = SdkUtil.getScreenHeight() - targetHeight * 0.5f;
			}
			if (topEnd == -1) {
				topEnd = (targetHeight / 2 - (mDisplayHeight - this.parent.getHeight())) * -1;
			}
            if (mPlayable && !this.mDisplayed && pos[1] < bottomEnd) {
				SdkLog.d(TAG, "InFlow has enough space and is playable");
				mVPlayer.resume();
				mRootView.requestLayout();
				mRootView.getLayoutParams().height = 1;
				mRootView.setVisibility(View.VISIBLE);
				mRootView.startAnimation(expandAnimation);
				this.mDisplayed = true;
			} else if (this.mDisplayed) {
				resumeOrStopInFlow(pos);
			} else if (!mRequested && pos[1] < bottomEnd) {
				mRequested = true;
				mVPlayer.requestAndPlayAds();
			}
		}

        public void setParentView(View v) {
            this.parent = v;
        }

        public void setView(View v) {
            this.v = v;
        }

		public void reset() {
			mDisplayed = false;
		}
	}

	final InFlowScrollChangedListener mScrollChangedListener = new InFlowScrollChangedListener();
	final InFlowFocusChangedListener mFocusChangedListener = new InFlowFocusChangedListener();

	public GuJEMSInFlowView(Context context, String adUnit) {
		super(context);
		this.mAdUnit = adUnit;
		init(context);
	}

	public GuJEMSInFlowView(Context context, String adUnit, View parentView) {
		super(context);
		this.mAdUnit = adUnit;
		mScrollChangedListener.setParentView(parentView);
		init(context);
	}

	public GuJEMSInFlowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.ems_inflow, this, true);

		TypedArray tVals = context.obtainStyledAttributes(attrs,
				R.styleable.GuJEMSInFlowView);
		this.mAdUnit = tVals
				.getString(R.styleable.GuJEMSInFlowView_ems_adUnit);

		tVals.recycle();
		init(context);
	}

	public void setAdUnit(String adUnit) {
		this.mAdUnit = adUnit;
		mVPlayer.setAdUnit(this.mAdUnit, false);
	}

	public void setColorToButtons(String hexColor) {
		((ImageButton) findViewById(R.id.ems_inflow_close)).
				setColorFilter(Color.parseColor('#' + hexColor.replace("#", "")));
		((ImageButton) findViewById(R.id.ems_inflow_sound)).
				setColorFilter(Color.parseColor('#' + hexColor.replace("#", "")));
	}

	public void setParentView(View p) {
		mScrollChangedListener.setParentView(p);
	}

	protected void init(final Context context) {

		this.context = context;
		mScrollChangedListener.setView(this);
		/* measurement */
		this.mDisplayHeight = SdkUtil.getScreenHeight();
		this.mDisplayWidth = SdkUtil.getScreenWidth();
        this.targetHeight = (int)(0.67f * 0.9f * mDisplayWidth + iconHeight);

		/* views */
		mRootView = (LinearLayout) this.findViewById(R.id.ems_inflow_view);
		mRootPlayerView = (ViewGroup) this.findViewById(R.id.ems_inflow_view_relative);
		mRootPlayerView.getLayoutParams().height =  targetHeight;
		mRootPlayerView.getLayoutParams().width = (int)(0.9f * mDisplayWidth);
		mRootView.setVisibility(View.INVISIBLE);
		mVPlayer = (GuJEMSVideoPlayer) this
				.findViewById(R.id.ems_inflow_player);
		
		/* Media player callback for volume control */
		mVPlayer.getVideoView().setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        SdkLog.d(TAG,
                                "Getting media player instance, setting to mute.");
                        mMPlayer = mp;
                        mMPlayer.setVolume(0.0f, 0.0f);
                        mPlayable = true;
                    }
                });

		/* Video ad callbacks, immediately pause when started */
		mVPlayer.getVideoAdPlayer().addCallback(new VideoAdPlayerCallback() {

            @Override
            public void onVolumeChanged(int arg0) {
                SdkLog.d(TAG, "video player :: volume " + arg0);
            }

            @Override
            public void onResume() {
                SdkLog.d(TAG, "video player :: resume");
            }

            @Override
            public void onPlay() {
                SdkLog.d(TAG, "video player :: play");
                mPlayable = true;
            }

            @Override
            public void onPause() {
                SdkLog.d(TAG, "video player :: pause");
            }

            @Override
            public void onError() {
                SdkLog.d(TAG, "video player :: error");
            }

            @Override
            public void onEnded() {
                SdkLog.d(TAG, "video player :: end");
                mRootView.startAnimation(closeAnimation);
                mVPlayer.pause();
                getViewTreeObserver().removeOnScrollChangedListener(
                        mScrollChangedListener);
				getViewTreeObserver().removeOnWindowFocusChangeListener(
						mFocusChangedListener);
            }
        });
		
		/* Ad Event Listener to pause when ready */
		mVPlayer.getVideoPlayerController().addListener(new AdEvent.AdEventListener() {

			private com.google.ads.interactivemedia.v3.impl.data.b currentAd;
            @Override
            public void onAdEvent(AdEvent arg0) {
                switch (arg0.getType()) {
                    case AD_BREAK_READY:
                        SdkLog.d(TAG, "InFlow Ad ready.");
                        mVPlayer.getVideoPlayerController().startAds();
                        break;
                    case LOADED:
                        currentAd = (com.google.ads.interactivemedia.v3.impl.data.b)arg0.getAd();
                        break;
					case STARTED:
					case RESUMED:
						if (startTime == -1) {
							startTime = System.currentTimeMillis() + 100;
						}
						break;
					case CLICKED:
						SdkLog.d(TAG, "InFlow Ad clicked.");
						mVPlayer.savePosition();
						break;
					case CONTENT_RESUME_REQUESTED:
						if (currentAd == null) {
							SdkLog.d(TAG, "no InFlow found");
							ThirdPartyConnector.getInstance().callByType(
									ThirdPartyConnector.teads,
									mRootView
							);
							getViewTreeObserver().removeOnScrollChangedListener(
									mScrollChangedListener);
							getViewTreeObserver().removeOnWindowFocusChangeListener(
									mFocusChangedListener);
						}
                }
            }
        });

		/* Close Button */
		((ImageButton) findViewById(R.id.ems_inflow_close))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mVPlayer.getVideoAdPlayer().pauseAd();
						mRootView.startAnimation(closeAnimation);
						getViewTreeObserver().removeOnScrollChangedListener(
								mScrollChangedListener);
						getViewTreeObserver().removeOnWindowFocusChangeListener(
								mFocusChangedListener);
					}
				});
		
		/* Sound button */
		((ImageButton) findViewById(R.id.ems_inflow_sound))
				.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
						if (mMPlayer != null) {
                            mMPlayer.setVolume(videoSoundMuted ? 1.0f : 0.0f,
									videoSoundMuted ? 1.0f : 0.0f);
                            changeSoundState();
                        } else {
                            SdkLog.w(TAG,
                                    "No media player instance to control volume.");
                        }
                    }
                });

		/* view animations */
		this.closeAnimation = this.getCloseAnimation();
		this.expandAnimation = this.getExpandAnimation();
		
		/* Scroll listener */
		getRootView().getViewTreeObserver()
				.addOnScrollChangedListener(mScrollChangedListener);
		getRootView().getViewTreeObserver()
				.addOnWindowFocusChangeListener(mFocusChangedListener);

		/* Ad Call */
		if (this.mAdUnit != null) {
			this.setAdUnit(this.mAdUnit);
		}

		SdkLog.d(TAG, "View, listeners, volume, animations and ad ready.");
	}

	protected void changeSoundState() {
		((ImageButton) findViewById(R.id.ems_inflow_sound)).setImageResource(videoSoundMuted ?
				R.drawable.ems_sound_button_off :
				R.drawable.ems_sound_button_on);
		videoSoundMuted = !videoSoundMuted;
	}

	protected Animation getCloseAnimation() {

		Animation a = new Animation() {

			public String toString() {
				return "InFlow Close";
			}

			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				if (interpolatedTime == 1) {
					mRootView.setVisibility(View.GONE);
				} else {
					mRootView.getLayoutParams().height = targetHeight
							- (int) (targetHeight * interpolatedTime);
					mRootView.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		a.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		a.setDuration((int) ((3 * targetHeight) / mRootView.getContext()
				.getResources().getDisplayMetrics().density));
		return a;
	}

	protected Animation getExpandAnimation() {
        Animation a = new Animation() {

        	public String toString() {
				return "InFlow Expand";
			}

			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				mRootView.getLayoutParams().height = (int)(targetHeight * interpolatedTime);
				mRootView.requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		a.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		a.setFillAfter(true);
		a.setFillEnabled(true);
		a.setDuration((int) ((3 * targetHeight) / mRootView.getContext()
				.getResources().getDisplayMetrics().density));

		return a;
	}

	protected void resumeOrStopInFlow(int[] pos) {
		if (bottomEnd != -1 && topEnd != -1) {
			if (pos[1] < topEnd || pos[1] > bottomEnd) {
				// Pause when running but cropped
				SdkLog.d(TAG,
						"InFlow cropped / out of view, pausing [y = "
								+ pos[1] + "]");
				if (startTime != -1 && startTime < System.currentTimeMillis()) {
					mVPlayer.getVideoAdPlayer().pauseAd();
				}
			} else {
				// Resume when paused but back in view
				SdkLog.d(TAG, "InFlow back in view, resuming [y = "
						+ pos[1] + "]");
				mVPlayer.getVideoAdPlayer().resumeAd();
			}
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		getViewTreeObserver().removeOnScrollChangedListener(
				mScrollChangedListener);
		getViewTreeObserver().removeOnWindowFocusChangeListener(
				mFocusChangedListener);
		mVPlayer.pause();
		mRootView.removeAllViews();
	}

	public void reload() {
		mScrollChangedListener.reset();
		mMPlayer.reset();
		topEnd = -1;
		bottomEnd = -1;
		videoSoundMuted = true;
		mVPlayer.getVideoPlayerController().requestAndPlayAds();
		mPlayable = false;
		getViewTreeObserver()
			.removeOnScrollChangedListener(mScrollChangedListener);
		getViewTreeObserver().removeOnWindowFocusChangeListener(
				mFocusChangedListener);
		getViewTreeObserver().addOnWindowFocusChangeListener(
				mFocusChangedListener);
		getViewTreeObserver()
			.addOnScrollChangedListener(mScrollChangedListener);
	}

}
