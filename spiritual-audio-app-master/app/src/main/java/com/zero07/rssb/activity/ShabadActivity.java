package com.zero07.rssb.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinSdkUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.tapdaq.sdk.TMBannerAdView;
import com.tapdaq.sdk.common.TMBannerAdSizes;
import com.tapdaq.sdk.listeners.TMAdListener;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;
import com.zero07.rssb.CustomMediaPlayer;
import com.zero07.rssb.IronsourceBannerListener;
import com.zero07.rssb.R;
import com.zero07.rssb.adapters.ShabadAdapter;
import com.zero07.rssb.databinding.ActivityShabadBinding;
import com.zero07.rssb.models.ShabadModel;
import com.zero07.rssb.userModels.UserContext;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ShabadActivity extends AppCompatActivity {

    private AdView admobBanner1;
    private AdView admobBanner2;
//    private AdManagerAdView admobBanner1;
//    private AdManagerAdView admobBanner2;
    TMBannerAdView tapdaqBanner1;
    TMBannerAdView tapdaqBanner2;
    MaxAdView applovinBanner1;
    MaxAdView applovinBanner2;
    IronSourceBannerLayout ironSourceBanner;
    private BannerView unityBanner1;
    private BannerView unityBanner2;
    private String unityAdUnitId = "Banner_Android";

    private static final String TAG = "ShabadActivity";

    TextView shabad_start_position;
    TextView shabad_end_position;
    SeekBar seekBar;
    ImageView shabad_play_button;
    ImageView shabad_pause_button;
    ImageView shabad_fast_forward_button;
    ImageView shabad_rewind_button;
    ImageView shabad_next_button;
    ImageView shabad_previous_button;
    ImageView shabad_musical_note;

    Handler handler = new Handler();
    Runnable runnable;

    ActivityShabadBinding binding;
    FirebaseFirestore database;

    CustomMediaPlayer customMediaPlayer;

    ObjectAnimator animator = null;
    AnimatorSet animatorSet = new AnimatorSet();
    TextView scrollertextview;
    ArrayList<ShabadModel> shabadAudios;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shabad);
        binding = ActivityShabadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Keep user screen continuously on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Keep user screen continuously on

//        getWindow().addFlags(WindowManager.LayoutParams.PREVENT_POWER_KEY);

        binding.shabadPauseButton.setVisibility(View.INVISIBLE);

//        database = FirebaseFirestore.getInstance();

         scrollertextview = findViewById(R.id.marquee_shabad_name);
        scrollertextview.setSelected(true);

        // Assign variable
        initializeMediaPlayerButtons();

        shabadAudios = populateShabad();
        customMediaPlayer = new CustomMediaPlayer(shabad_play_button);
        ShabadAdapter adapter = new ShabadAdapter(this, shabadAudios, customMediaPlayer);
        mediaPlayer = customMediaPlayer.getMediaPlayer();
        binding.shabadRecyclerView.setLayoutManager(new GridLayoutManager(this, 1) {
            @Override
            public void onLayoutCompleted(RecyclerView.State state) {
                super.onLayoutCompleted(state);
                customMediaPlayer.setbackAudio(getApplicationContext(), Uri.parse(shabadAudios.get(0).getShabadUrl()), 0);
            }
        });
        binding.shabadRecyclerView.setAdapter(adapter);


/******** Start Ads Request *******/

/**** Unity AdView ****/
        loadBannerAdFrom();

/**** Admob Adaptive Banner ****/
//        shabadAdView_50 = new AdView(this);
//        shabadAdView_50.setAdUnitId(getString(R.string.shabad_activity_admob_unit_50));
//        shabadAdView_50.setAdSize(getAdSize());
//        binding.shabadAdContainer1.addView(shabadAdView_50);
//
//        AdRequest adRequest_1 = new AdRequest.Builder().build();
//        shabadAdView_50.loadAd(adRequest_1);
//
//        shabadAdView_50.setAdListener(new AdListener() {
//            @Override
//            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
//                super.onAdFailedToLoad(loadAdError);
//                Log.e(TAG, "satsangAdView_50 - Error : " + loadAdError.getMessage());
//                shabadAdView_50.loadAd(adRequest_1);
//            }
//
////            @Override
////            public void onAdLoaded() {
////                super.onAdLoaded();
////                binding.shabadMusicalNote.getLayoutParams().height = 645;
////                binding.shabadMusicalNote.getLayoutParams().width = 645;
////                binding.shabadMusicalNote.requestLayout();
////
//////                binding.satsangMusicalNote.animate().rotation()
////            }
//        });
//
//        shabadAdView_250 = new AdView(this);
//        shabadAdView_250.setAdUnitId(getString(R.string.shabad_activity_admob_unit_250));
//        shabadAdView_250.setAdSize(getAdSize());
//        binding.shabadAdContainer2.addView(shabadAdView_250);
//
//        AdRequest adRequest_2 = new AdRequest.Builder().build();
//        shabadAdView_250.loadAd(adRequest_2);
//
//        shabadAdView_250.setAdListener(new AdListener() {
//            @Override
//            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
//                super.onAdFailedToLoad(loadAdError);
//                Log.e(TAG, "Error while loading ads: " + loadAdError.getMessage());
//                shabadAdView_250.loadAd(adRequest_2);
//            }
//
////            @Override
////            public void onAdLoaded() {
////                super.onAdLoaded();
////                binding.shabadMusicalNote.getLayoutParams().height = 470;
////                binding.shabadMusicalNote.getLayoutParams().width = 470;
////                binding.shabadMusicalNote.requestLayout();
////            }
//        });

/**** Normal Ad Banner ****/
//        AdRequest adRequest = new AdRequest.Builder().build();
//        binding.adView.loadAd(adRequest);
//
//        binding.adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
//                super.onAdFailedToLoad(loadAdError);
//                Log.e(TAG, "Error while loading ads: " + loadAdError.getMessage());
//                binding.adView.loadAd(adRequest);
//            }
//        });

/******** End Admob Ads Request *******/



//        if (mediaPlayer != null) {
//            if (mediaPlayer.isPlaying()) {
////            // Get current position of media player
////            int currentPosition = mediaPlayer.getCurrentPosition();
////            // Get duration of media player
////            int duration = mediaPlayer.getDuration();
////            mediaPlayer.seekTo(currentPosition);
////            seekBar.setMax(mediaPlayer.getCurrentPosition());
//                binding.shabadPlayButton.setVisibility(View.GONE);
//                binding.shabadPauseButton.setVisibility(View.VISIBLE);
//////            if (mediaPlayer.isPlaying() && duration != currentPosition) {
//////
//////            }
////            }
//            } else {
//                binding.shabadPauseButton.setVisibility(View.INVISIBLE);
//            }
//        }


        // Initialize runnable
        runnable = new Runnable() {
            @Override
            public void run() {
                // Set progress on seekbar
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                // Handler post delay for 0.5 second
                handler.postDelayed(this, 500);
            }
        };

        shabad_play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shabad_play_button.setVisibility(View.GONE);
                shabad_pause_button.setVisibility(View.VISIBLE);
                mediaPlayer.start();

                scrollertextview.setText(shabadAudios.get(customMediaPlayer.getCurrentAudioPosition()).getShabadName());
                // Set max on seek bar
                seekBar.setMax(mediaPlayer.getDuration());
                // Start handler
                handler.postDelayed(runnable, 0);

                // Get duration of media player
                int duration = mediaPlayer.getDuration();
                // Convert millisecond to minute and second
                String sDuration = converFormat(duration);
                // Set duration on text view
                shabad_end_position.setText(sDuration);

                playerAnimation();
            }
        });

        shabad_pause_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shabad_pause_button.setVisibility(View.GONE);
                shabad_play_button.setVisibility(View.VISIBLE);
                mediaPlayer.pause();
                // Stop handler
                handler.removeCallbacks(runnable);

                stopAnimation();
            }
        });

        shabad_fast_forward_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get current position of media player
                int currentPosition = mediaPlayer.getCurrentPosition();
                // Get duration of media player
                int duration = mediaPlayer.getDuration();
                // Check condition
                if (mediaPlayer.isPlaying() && duration != currentPosition) {
                    // When media is playing and duration is not equal to current position
                    // Fast forward for 5 seconds
                    currentPosition = currentPosition + 5000;
                }
                // Set current position on text view
                shabad_start_position.setText(converFormat(currentPosition));
                // Set progress on seekbar
                mediaPlayer.seekTo(currentPosition);
            }
        });

        shabad_rewind_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get current position of media player
                int currentPosition = mediaPlayer.getCurrentPosition();
                // Get duration of media player
                int duration = mediaPlayer.getDuration();
                // Check condition
                if (mediaPlayer.isPlaying() && currentPosition > 5000) {
                    // When media is playing and current position is more than 5 seconds
                    // Rewind for 5 seconds
                    currentPosition = currentPosition - 5000;
                }
                // Set current position on text view
                shabad_start_position.setText(converFormat(currentPosition));
                // Set progress on seekbar
                mediaPlayer.seekTo(currentPosition);
            }
        });

        shabad_next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customMediaPlayer.playNextAudio(v.getContext(), shabadAudios);
            }
        });

        shabad_previous_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customMediaPlayer.playPreviousAudio(v.getContext(), shabadAudios);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
                // Set current position on text view
                shabad_start_position.setText(converFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int bufferingPercent) {
                seekBar.setSecondaryProgress(bufferingPercent);
            }
        });


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.reset();
                shabad_pause_button.setVisibility(View.INVISIBLE);
                shabad_play_button.setVisibility(View.VISIBLE);
                // Set media player to initial position
                mediaPlayer.seekTo(0);
                int duration = mediaPlayer.getDuration();
                // Convert millisecond to minute and second
                String sDuration = converFormat(duration);
                // Set duration on text view
                shabad_start_position.setText(sDuration);
                customMediaPlayer.playNextAudio(getApplicationContext(), shabadAudios);
            }
        });

    }

    private void loadBannerAdFrom() {
        if (UserContext.getIsAdmobEnabled()) {
            loadAdmobBanner();
        }
        if (UserContext.getIsTapdaqEnabled()) {
            loadTapdaqBanner();
        }
        if (UserContext.getIsApplovinEnabled()) {
            loadApplovinBanner();
        }
        if (UserContext.getIsUnityEnabled()) {
            loadUnityBanner();
        }
        if (UserContext.getIsIronsource_banner()) {
            loadIronSourceBanner();
        }
    }

    private void loadUnityBanner() {
/**** Unity Ads ****/
        unityBanner1 = new BannerView(this, unityAdUnitId, new UnityBannerSize(320, 50));
        binding.shabadAdContainer1.addView(unityBanner1);
        unityBanner1.load();

        unityBanner2 = new BannerView(this, unityAdUnitId, new UnityBannerSize(320, 50));
        binding.shabadAdContainer2.addView(unityBanner2);
        unityBanner2.load();
    }

    public void loadAdmobBanner() {
        binding.admobShabadAdContainer.setVisibility(View.VISIBLE);
        binding.shabadMusicalNote.getLayoutParams().height = 200;

        admobBanner1 = findViewById(R.id.admob_shabad_ad_container);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        admobBanner1.loadAd(adRequest1);
    }

    private void loadTapdaqBanner() {
        tapdaqBanner1 = new TMBannerAdView(this);
        binding.shabadAdContainer1.addView(tapdaqBanner1);
        tapdaqBanner1.load(this, getString(R.string.tapdaq_banner_default), TMBannerAdSizes.STANDARD, new TMAdListener());

        tapdaqBanner2 = new TMBannerAdView(this);
        binding.shabadAdContainer2.addView(tapdaqBanner2);
        tapdaqBanner2.load(this, getString(R.string.tapdaq_banner_main), TMBannerAdSizes.STANDARD, new TMAdListener());
    }

    private void loadApplovinBanner() {
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int heightDp = MaxAdFormat.BANNER.getAdaptiveSize(ShabadActivity.this).getHeight();
        int heightPx = AppLovinSdkUtils.dpToPx(ShabadActivity.this, heightDp);

        applovinBanner1 = new MaxAdView(getString(R.string.appl_banner_default), ShabadActivity.this);
        applovinBanner1.setLayoutParams(new FrameLayout.LayoutParams(width, heightPx));
        applovinBanner1.setExtraParameter("adaptive_banner", "true");
        binding.shabadAdContainer1.addView(applovinBanner1);
        applovinBanner1.loadAd();

        applovinBanner2 = new MaxAdView(getString(R.string.appl_banner_default), ShabadActivity.this);
        applovinBanner2.setLayoutParams(new FrameLayout.LayoutParams(width, heightPx));
        applovinBanner2.setExtraParameter("adaptive_banner", "true");
        binding.shabadAdContainer2.addView(applovinBanner2);
        applovinBanner2.loadAd();
    }

    private void loadIronSourceBanner() {
//        IronSource.init(this, getString(R.string.ironSource_app_id), IronSource.AD_UNIT.BANNER);
        ironSourceBanner = IronSource.createBanner(this, ISBannerSize.RECTANGLE);
        binding.shabadAdContainer1.addView(ironSourceBanner);

        binding.shabadAdContainer1.setVisibility(View.VISIBLE);
        ironSourceBanner.setBannerListener(new IronsourceBannerListener(this));
        IronSource.loadBanner(ironSourceBanner);
    }

    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    @Override
    protected void onPause() {
        if (admobBanner1 != null) {
            admobBanner1.pause();
        }
        if (admobBanner2 != null) {
            admobBanner2.pause();
        }
        if(customMediaPlayer != null ) {
            customMediaPlayer.pause();
        }

        IronSource.onPause(this);
        IronSource.destroyBanner(ironSourceBanner);
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (admobBanner1 != null) {
            admobBanner1.resume();
        }
        if (admobBanner2 != null) {
            admobBanner2.resume();
        }

        IronSource.onResume(this);
        loadBannerAdFrom();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (unityBanner1 != null){
            unityBanner1.destroy();
        }
        if (unityBanner2 != null){
            unityBanner2.destroy();
        }

        if (admobBanner1 != null) {
            admobBanner1.destroy();
        }
        if (admobBanner2 != null) {
            admobBanner2.destroy();
        }

        if (tapdaqBanner1 != null) {
            tapdaqBanner1.destroy(this);
        }
        if (tapdaqBanner2 != null) {
            tapdaqBanner2.destroy(this);
        }

        IronSource.destroyBanner(ironSourceBanner);
        super.onDestroy();
    }

    private void playerAnimation() {
        View view = findViewById(R.id.shabad_musical_note);

        animator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        animator.setDuration(10000);
        animator.setRepeatCount(Animation.INFINITE);
        animator.setInterpolator(new LinearInterpolator());

        animatorSet.playTogether(animator);
        animatorSet.start();

    }

    private void stopAnimation() {
        animator.cancel();
        animatorSet.cancel();
    }

    private void initializeMediaPlayerButtons() {
        shabad_start_position = findViewById(R.id.shabad_start_position);
        shabad_end_position = findViewById(R.id.shabad_end_position);
        seekBar = findViewById(R.id.shabad_seekbar);
        shabad_play_button = findViewById(R.id.shabad_play_button);
        shabad_pause_button = findViewById(R.id.shabad_pause_button);
        shabad_fast_forward_button = findViewById(R.id.shabad_fast_forward_button);
        shabad_rewind_button = findViewById(R.id.shabad_rewind_button);
        shabad_next_button = findViewById(R.id.shabad_next_button);
        shabad_previous_button = findViewById(R.id.shabad_previous_button);
        shabad_musical_note = findViewById(R.id.shabad_musical_note);
    }

    @SuppressLint("DefaultLocale")
    private String converFormat(int duration) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        customMediaPlayer.getMediaPlayer().stop();
        customMediaPlayer.getMediaPlayer().reset();
        customMediaPlayer.clear();
//        dialog.dismiss();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
//            customMediaPlayer.getMediaPlayer().stop();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }


    public ArrayList<ShabadModel> populateShabad() {
        ArrayList<ShabadModel> shabadaudio = new ArrayList<>();
        shabadaudio.add(new ShabadModel("has_ke_gujari_ja", "हंस के गुजारी जा", "Has Ke Gujari Ja", "https://99story.in/wp-content/uploads/2021/05/Has-ke-gujari-ja.mp3", 1));
        shabadaudio.add(new ShabadModel("sohne_phula_ve_gulab_deya", "सोहने फुला वे गुलाब दिया", "Sohne Phula Ve Gulab Deya", "https://99story.in/wp-content/uploads/2021/05/sohne-phula-ve-gulab-deya_new.mp3", 2));
        shabadaudio.add(new ShabadModel("de_darshan_guru_mere_ruhan_pukar_diya", "दे दर्शन गुरु मेरे रूहा पुकार दिया", "De Darshan Guru Mere Ruhan Pukar Diya", "https://99story.in/wp-content/uploads/2021/05/de-darshan-guru-mere-ruhan-pukar-diya_new.mp3", 3));
        shabadaudio.add(new ShabadModel("mere_sacheya_guru_ji_meherban", "मेरे सच्चे गुरुजी महरबाना", "Mere Sacheya Guru Ji Meherban", "https://99story.in/wp-content/uploads/2021/05/mere-sacheya-guru-ji-meherban_new.mp3", 4));
        shabadaudio.add(new ShabadModel("amrit_vela_hoya_tu_tah_soya_hoya", "अमृत वेला होया तू ताह सोया होया", "Amrit Vela Hoya Tu Tah Soya Hoya", "https://99story.in/wp-content/uploads/2021/05/Amrit-Vela-Hoya-Tu-Tah-Soya-Hoya.mp3", 5));
        shabadaudio.add(new ShabadModel("guru_charni_chit_la_bandeya", "गुरु चरणी चित ला बंदेया", "Guru Charni Chit La Bandeya", "https://99story.in/wp-content/uploads/2021/05/Guru-Charni-Chit-La-Bandeya.mp3"));
        shabadaudio.add(new ShabadModel("papi_ji_vadiya_lag_ja_guru_di_charni", "लग जा गुरु दी चरणी", "Papi Ji Vadiya Lag Ja Guru Di Charni", "https://99story.in/wp-content/uploads/2021/05/Papi-Ji-Wadeya-Lag-Ja-Guru-Di-Charni.mp3"));
        shabadaudio.add(new ShabadModel("zindagi_de_din_char", "ज़िंदगी दे दिन चार", "Zindagi De Din Char", "https://99story.in/wp-content/uploads/2021/05/Zindagi-De-Din-Char.mp3"));
        shabadaudio.add(new ShabadModel("charan_kamal_tere_dhoye_dhoye_peevan", "चरण कमल तेरे धोए धोए पीवां मेरे", "Charan Kamal Tere Dhoye Dhoye Peevan", "https://99story.in/wp-content/uploads/2021/05/Charan-Kamal-Tere-Dhoye-Dhoye-Peevan.mp3"));
        shabadaudio.add(new ShabadModel("amrit_vele_tu_kyun_soye", "अमृत वेले तू क्यों सोयें", "Amrit Vele Tu Kyun Soye", "https://99story.in/wp-content/uploads/2021/05/Amrit-vele-tu-kyun-soye.mp3"));
        shabadaudio.add(new ShabadModel("meharbaan_hai_saahib_mera", "मेहरबान है साहिब मेरा", "Meharbaan Hai Saahib Mera", "https://99story.in/wp-content/uploads/2021/05/Meharbaan-Hai-Saahib-Mera.mp3"));
        shabadaudio.add(new ShabadModel("tum_sharnaayi_aaya_thakur_female_voice", "तुम शरणाई आया ठाकुर (फीमेल वॉइस)", "Tum Sharnaayi Aaya Thakur (Female Voice)", "https://99story.in/wp-content/uploads/2021/05/Tum-Sharnaayi-Aaya-Thakur_Female-Voice.mp3"));
        shabadaudio.add(new ShabadModel("hum_aise_tu_aisa_madho", "हम ऐसे तू ऐसा माधो", "Hum Aise Tu Aisa Madho", "https://99story.in/wp-content/uploads/2021/05/Hum-Aise-Tu-Aisa-Madho.mp3"));
        shabadaudio.add(new ShabadModel("guru_ke_darshan_kaarne_ham_aaye", "गुरु के दर्शन करने हम आये", "Guru Ke Darshan Kaarne Ham Aaye", "https://99story.in/wp-content/uploads/2021/05/Guru-Ke-Darshan-Kaarne-Ham-Aaye.mp3"));
        shabadaudio.add(new ShabadModel("saahib_main_gulaam_haun_tera", "साहिब मैं गुलाम हूँ तेरा", "Saahib Main Gulaam Haun Tera", "https://99story.in/wp-content/uploads/2021/05/Saahib-Main-Gulaam-Haun-Tera.mp3"));
        shabadaudio.add(new ShabadModel("vekho_ni_shauh_inayat", "वेखो नी शौह इनायत", "Vekho Ni Shauh Inayat", "https://99story.in/wp-content/uploads/2021/05/Vekho-Ni-Shauh-Inayat.mp3"));
        shabadaudio.add(new ShabadModel("darshan_dekh_jeevan_gur_tera", "दर्शन देख जीवन गुर तेरा", "Darshan Dekh Jeevan Gur Tera", "https://99story.in/wp-content/uploads/2021/05/Darshan-Dekh-Jeevan-Gur-Tera.mp3"));
        shabadaudio.add(new ShabadModel("tum_karho_daya_mere_saaeen", "तुम करो दया मेरे साईं", "Tum Karho Daya Mere Saaeen", "https://99story.in/wp-content/uploads/2021/05/Tum-Karho-Daya-Mere-Saaeen.mp3"));
        shabadaudio.add(new ShabadModel("surat_tu_kaun_kahaan_se_aayi", "सूरत तू कौन कहाँ से आयी", "Surat Tu Kaun Kahaan Se Aayi", "https://99story.in/wp-content/uploads/2021/05/Surat-Tu-Kaun-Kahaan-Se-Aayi.mp3"));
        shabadaudio.add(new ShabadModel("sunta_nahi_dhun_ki_khabar", "सुनता नहीं धुन की खबर", "Sunta Nahi Dhun Ki Khabar", "https://99story.in/wp-content/uploads/2021/05/Sunta-Nahi-Dhun-Ki-Khabar.mp3"));
        shabadaudio.add(new ShabadModel("radha_soami_dhara_nar_roop", "राधा स्वामी धरा नर रूप", "Radha Soami Dhara Nar Roop", "https://99story.in/wp-content/uploads/2021/05/Radha-Soami-Dhara-Nar-Roop.mp3"));
        shabadaudio.add(new ShabadModel("mere_satguru_pakri_baanh", "मेरे सतगुरु पकड़ी बांह", "Mere Satguru Pakri Baanh", "https://99story.in/wp-content/uploads/2021/05/Mere-Satguru-Pakri-Baanh.mp3"));
        shabadaudio.add(new ShabadModel("mere_satguru_ji_tussi_mehar_karo", "मेरे सतगुरु जी तुस्सी मेहर करो", "Mere Satguru Ji Tussi Mehar Karo", "https://99story.in/wp-content/uploads/2021/05/Mere-Satguru-Ji-Tussi-Mehar-Karo.mp3"));
        shabadaudio.add(new ShabadModel("mangoon_ek_guru_se_daana", "मांगू एक गुरु से दाना", "Mangoon Ek Guru Se Daana", "https://99story.in/wp-content/uploads/2021/05/Mangoon-Ek-Guru-Se-Daana.mp3"));
        shabadaudio.add(new ShabadModel("kardo_naam_deewana_ji_ab", "कर दो नाम दीवाना जी अब", "Kardo Naam Deewana Ji Ab", "https://99story.in/wp-content/uploads/2021/05/Kardo-Naam-Deewana-Ji-Ab.mp3"));
        shabadaudio.add(new ShabadModel("ehh_vela_tere_hath_nahi_aauna", "एह वेला तेरे हाथ नहीं आना", "Ehh Vela Tere Hath Nahi Aauna", "https://99story.in/wp-content/uploads/2021/05/Ehh-Vela-Tere-Hath-Nahi-Aauna.mp3"));
        shabadaudio.add(new ShabadModel("aaj_divas_leoon_balihaara", "आज दिवस लेऊँ बलिहारा", "Aaj Divas Leoon Balihaara", "https://99story.in/wp-content/uploads/2021/05/Aaj-Divas-Leoon-Balihaara.mp3"));
        shabadaudio.add(new ShabadModel("bahut_janam_bichure_the_madho", "बहुत जनम बिछुरे थे माधो", "Bahut Janam Bichure The Madho", "https://99story.in/wp-content/uploads/2021/05/Bahut-Janam-Bichure-The-Madho.mp3"));
        shabadaudio.add(new ShabadModel("bisar_gayi_sab_tat_parayi", "बिसर गयी सब तात पराई", "Bisar Gayi Sab Tat Parayi", "https://99story.in/wp-content/uploads/2021/05/Bisar-Gayi-Sab-Tat-Parayi.mp3"));
        shabadaudio.add(new ShabadModel("man_maane_jab_taar_prabhu_ji", "मन माने जब तार प्रभु जी", "Man Maane Jab Taar Prabhu Ji", "https://99story.in/wp-content/uploads/2021/05/Man-Maane-Jab-Taar-Prabhu-Ji.mp3"));
        shabadaudio.add(new ShabadModel("chunar_meri_rang_daali", "चुनर मेरी रंग डाली", "Chunar Meri Rang Daali", "https://99story.in/wp-content/uploads/2021/05/Chunar-Meri-Rang-Daali.mp3"));
        shabadaudio.add(new ShabadModel("ehh_mere_malik_sang_tera_ho_gaya", "एह मेरे मालिक संग तेरा हो गया", "Ehh Mere Malik Sang Tera Ho Gaya", "https://99story.in/wp-content/uploads/2021/05/Ehh-Mere-Malik-Sang-Tera-Ho-Gaya.mp3"));
        shabadaudio.add(new ShabadModel("gariba_anatha_tera_mana", "गरीबा अनाथा तेरा मन", "Gariba Anatha Tera Mana", "https://99story.in/wp-content/uploads/2021/05/Gariba-Anatha-Tera-Mana.mp3"));
        shabadaudio.add(new ShabadModel("gur_parmesar_karnaihaar", "गुर परमेसर करनिहार", "Gur Parmesar Karnaihaar", "https://99story.in/wp-content/uploads/2021/05/Gur-Parmesar-Karnaihaar.mp3"));
        shabadaudio.add(new ShabadModel("guru_mere_jaan_praan", "गुरु मेरे जान प्राण", "Guru Mere Jaan Praan", "https://99story.in/wp-content/uploads/2021/05/Guru-Mere-Jaan-Praan.mp3"));
        shabadaudio.add(new ShabadModel("saacha_saahib_ek_tu", "सच्चा साहिब एक तू", "Saacha Saahib Ek Tu", "https://99story.in/wp-content/uploads/2021/05/Saacha-Saahib-Ek-Tu.mp3"));
        shabadaudio.add(new ShabadModel("ham_maile_tum_oojal_karte", "हम मैले तुम ऊजल करते", "Ham Maile Tum Oojal Karte", "https://99story.in/wp-content/uploads/2021/05/Ham-Maile-Tum-Oojal-Karte.mp3"));
        shabadaudio.add(new ShabadModel("he_ri_main_to_prem_deevaani", "हे री मैं तो प्रेम दीवानी", "He Ri Main To Prem Deevaani", "https://99story.in/wp-content/uploads/2021/05/He-Ri-Main-To-Prem-Dewani_2nd-C.mp3"));
        shabadaudio.add(new ShabadModel("karam_gati_kare_na_re_tari", "करम गति करे न रे तेरी", "Karam Gati Kare Na Re Tari", "https://99story.in/wp-content/uploads/2021/05/Karam-Gati-Kare-Na-Re-Tari.mp3"));
        shabadaudio.add(new ShabadModel("karoon_benati_dou_kar_jori", "करूँ बेनती दोउ कर जोरी", "Karoon Benati Dou Kar Jori", "https://99story.in/wp-content/uploads/2021/05/Karoon-Benati-Dou-Kar-Jori.mp3"));
        shabadaudio.add(new ShabadModel("maan_karoon_tudh_oopare_mere_preetam_pyaare", "मान करूँ तुध ऊपरे मेरे प्रीतम प्यारे", "Maan Karoon Tudh Oopare Mere Preetam Pyaare", "https://99story.in/wp-content/uploads/2021/05/Maan-Karoon-Tudh-Oopare-Mere-Preetam-Pyaare.mp3"));
        shabadaudio.add(new ShabadModel("mahaari_sudh_jyoon_jaano", "महारी सुद्ध ज्यूं जानो", "Mahaari Sudh Jyoon Jaano", "https://99story.in/wp-content/uploads/2021/05/Mahaari-Sudh-Jyoon-Jaano.mp3"));
        shabadaudio.add(new ShabadModel("man_laago_mero_yaar", "मन लागो मेरो यार", "Man Laago Mero Yaar", "https://99story.in/wp-content/uploads/2021/05/Man-Laago-Mero-Yaar.mp3"));
        shabadaudio.add(new ShabadModel("man_mere_sukh_sahaj", "मन मेरे सुख सहज", "Man Mere Sukh Sahaj", "https://99story.in/wp-content/uploads/2021/05/Man-Mere-Sukh-Sahaj.mp3"));
        shabadaudio.add(new ShabadModel("mera_naal_sajjan_de_neho_lagga", "मेरा नाल सज्जन दे नेहो लगा", "Mera Naal Sajjan De Neho Lagga", "https://99story.in/wp-content/uploads/2021/05/Mera-Naal-Sajjan-De-Neho-Lagga.mp3"));
        shabadaudio.add(new ShabadModel("mere_saahib_tu_main_maan_nimaani", "मेरे साहिब तू मैं मान निमानी", "Mere Saahib Tu Main Maan Nimaani", "https://99story.in/wp-content/uploads/2021/05/Mere-Saahib-Tu-Main-Maan-Nimaani.mp3"));
        shabadaudio.add(new ShabadModel("meri_nazar_mein_moti_aaya_hai", "मेरी नज़र में मोती आया है", "Meri Nazar Mein Moti Aaya Hai", "https://99story.in/wp-content/uploads/2021/05/Meri-Nazar-Mein-Moti-Aaya-Hai.mp3"));
        shabadaudio.add(new ShabadModel("nij_roop_poore_satgur_ka", "निज रूप पूरे सतगुर का", "Nij Roop Poore Satgur Ka", "https://99story.in/wp-content/uploads/2021/05/Nij-Roop-Poore-Satgur-Ka.mp3"));
        shabadaudio.add(new ShabadModel("preet_lagi_tum_naam_ki", "प्रीत लगी तुम नाम की", "Preet Lagi Tum Naam Ki", "https://99story.in/wp-content/uploads/2021/05/Preet-Lagi-Tum-Naam-Ki.mp3"));
        shabadaudio.add(new ShabadModel("raam_simar_pachhtaayega", "राम सिमर पछतायेगा", "Raam Simar Pachhtaayega", "https://99story.in/wp-content/uploads/2021/05/Raam-Simar-Pachhtaahega.mp3"));
        shabadaudio.add(new ShabadModel("man_tan_tera_dhan_bhi_tera", "मन तन तेरा धन भी तेरा", "Man Tan Tera Dhan Bhi Tera", "https://99story.in/wp-content/uploads/2021/05/Man-Tan-Tera-Dhan-Bhi-Tera.mp3"));
        shabadaudio.add(new ShabadModel("rakh_diya_man_tere_charnon_mein", "रख दिया मन तेरे चरणों में", "Rakh Diya Man Tere Charnon Mein", "https://99story.in/wp-content/uploads/2021/05/Rakh-Diya-Man-Tere-Charnon-Mein.mp3"));
        shabadaudio.add(new ShabadModel("saahib_ke_darbaar_mein", "साहिब के दरबार में", "Saahib Ke Darbaar Mein", "https://99story.in/wp-content/uploads/2021/05/Saahib-Ke-Darbaar-Mein.mp3"));
        shabadaudio.add(new ShabadModel("sant_sanehi_naam_hai", "संत सनेही नाम है", "Sant Sanehi Naam Hai", "https://99story.in/wp-content/uploads/2021/05/Sant-Sanehi-Naam-Hai.mp3"));
        shabadaudio.add(new ShabadModel("satgur_hai_rangrez_chunar_meri", "सतगुर है रंगरेज चुनर मेरी", "Satgur Hai Rangrez Chunar Meri", "https://99story.in/wp-content/uploads/2021/05/Satgur-Hai-Rangrez-Chunar-Meri.mp3"));
        shabadaudio.add(new ShabadModel("satguru_aaye_deeya_jag_hela", "सतगुरु आये दिया जग हेला", "Satguru Aaye Deeya Jag Hela", "https://99story.in/wp-content/uploads/2021/05/Satguru-Aaye-Deeya-Jag-Hela.mp3"));
        shabadaudio.add(new ShabadModel("satguru_meri_suno_pukaar", "सतगुरु मेरी सुनो पुकार", "Satguru Meri Suno Pukaar", "https://99story.in/wp-content/uploads/2021/05/Satguru-Meri-Suno-Pukaar.mp3"));
        shabadaudio.add(new ShabadModel("sevak_ki_ardaas_pyaare", "सेवक की अरदास प्यारे", "Sevak Ki Ardaas Pyaare", "https://99story.in/wp-content/uploads/2021/05/Sevak-Ki-Ardaas-Pyaare.mp3"));
        shabadaudio.add(new ShabadModel("shabad_bina_saara_jag_andha", "शबद बिना सारा जग अँधा", "Shabad Bina Saara Jag Andha", "https://99story.in/wp-content/uploads/2021/05/Shabad-Bina-Saara-Jag-Andha.mp3"));
        shabadaudio.add(new ShabadModel("sun_fariyaad_peeran_deya_peera", "सुन फरियाद पीरा देया पीरा", "Sun Fariyaad Peeran Deya Peera", "https://99story.in/wp-content/uploads/2021/05/Sun-Fariyaad-Peeraan-Deya-Peera.mp3"));
        shabadaudio.add(new ShabadModel("tum_sharnai_aaya_thakur_male_voice", "तुम शरणाई आया ठाकुर (मेल वॉइस)", "Tum Sharnai Aaya Thakur (Male Voice)", "https://99story.in/wp-content/uploads/2021/05/Tum-Sharnai-Aaya-Thakur_Male-Voice.mp3"));
        shabadaudio.add(new ShabadModel("bulleh_nu_samjhaava_aaiyaan", "बुल्लेह नू समझावन आइयां", "Bulleh Nu Samjhaavan Aaiyaan", "https://99story.in/wp-content/uploads/2021/05/Bulleh-Nu-Samjhaavan-Aaiyaan.mp3"));

        return shabadaudio;
    }

    //    @Override
//    protected void onStart() {
//        MediaPlayer mediaPlayer = customMediaPlayer.getMediaPlayer();
//        SharedPreferences prefs = this.getSharedPreferences("<com.your.app>", Context.MODE_PRIVATE);
//        int length = prefs.getInt("length", 0); //0 is the default value
//
//        mediaPlayer.start();
//        mediaPlayer.seekTo(length);
//        super.onStart();
//    }


//    @Override
//    protected void onStart() {
//        MediaPlayer mediaPlayer = customMediaPlayer.getMediaPlayer();
//////        if (mediaPlayer != null) {
////            if (mediaPlayer.isPlaying()) {
//////            // Get current position of media player
//////            int currentPosition = mediaPlayer.getCurrentPosition();
//////            // Get duration of media player
//////            int duration = mediaPlayer.getDuration();
//////            mediaPlayer.seekTo(currentPosition);
//////            seekBar.setMax(mediaPlayer.getCurrentPosition());
////            binding.shabadPlayButton.setVisibility(View.GONE);
////            binding.shabadPauseButton.setVisibility(View.VISIBLE);
////////            if (mediaPlayer.isPlaying() && duration != currentPosition) {
////////
////////            }
//////            }
////        } else {
////            binding.shabadPauseButton.setVisibility(View.INVISIBLE);
////        }
//        super.onStart();
//    }



//    @Override
//    protected void onPause() {
//        MediaPlayer mediaPlayer = customMediaPlayer.getMediaPlayer();
//        SharedPreferences prefs = this.getSharedPreferences("<com.zero07.rssb>", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
////        mediaPlayer.pause();
//        int length = mediaPlayer.getCurrentPosition();
//        int length1 = mediaPlayer.getDuration();
//
//        editor.putInt("length", length); //assuming length is an integer
//        editor.putInt("length1", length1); //you should put different values in different variable
//        editor.commit();
//
//        super.onPause();
//    }

//    @Override
//    protected void onStart() {
//
//        super.onStart();
//    }

//    @Override
//    protected void onResume() {
//        MediaPlayer mediaPlayer = customMediaPlayer.getMediaPlayer();
//
//        if (mediaPlayer.isPlaying()) {
//            int currentPosition = mediaPlayer.getCurrentPosition();
//            int duration = mediaPlayer.getDuration();
//
//            mediaPlayer.seekTo(currentPosition);
//        }
//
////        SharedPreferences prefs = this.getSharedPreferences("<com.zero07.rssb>", Context.MODE_PRIVATE);
////        int length = prefs.getInt("length", 0); //0 is the default value
////
////        mediaPlayer.start();
////        mediaPlayer.seekTo(length);
//
//        super.onResume();
//    }


//    @Override
//    protected void onResume() {
//        binding.shabadPauseButton.setVisibility(View.INVISIBLE);
//        binding.shabadPlayButton.setVisibility(View.INVISIBLE);
//        binding.shabadNextButton.setVisibility(View.INVISIBLE);
//        scrollertextview.setText(shabadAudios.get(customMediaPlayer.getCurrentAudioPosition()).getShabadName());
//        // Set max on seek bar
//        seekBar.setMax(mediaPlayer.getDuration());
//        // Start handler
//        handler.postDelayed(runnable, 0);
//
//        // Get duration of media player
//        int duration = mediaPlayer.getDuration();
//        // Convert millisecond to minute and second
//        String sDuration = converFormat(duration);
//        // Set duration on text view
//        shabad_end_position.setText(sDuration);
//
//        playerAnimation();
//        super.onResume();
//    }


}
