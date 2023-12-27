package com.o2games.playwin.android.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.transition.AutoTransition;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.o2games.playwin.android.FirebaseDataService;
import com.o2games.playwin.android.Game;
import com.o2games.playwin.android.R;
import com.o2games.playwin.android.activity.MainActivity;
import com.o2games.playwin.android.databinding.FragmentGameOfferBinding;
import com.o2games.playwin.android.model.User;
import com.o2games.playwin.android.sqlUserGameData.DBHelper;
import com.o2games.playwin.android.userData.UserContext;

import java.util.Random;

public class GameOfferFragment extends Fragment {

    private static final String TAG = "saare_games_ka_fra";
    private FirebaseDataService firebaseDataService;
    public static final String sqlTotalCoinsCOL = Game.TOTAL_CASH_COINS.getId();
    private User loggedInUser;

    View rootView;
    Context context;
    FragmentGameOfferBinding binding;
    DBHelper dbHelper;
    Handler handler;
    Animation scaleDown;
    Animation largeScaleUp;
    Animation blinking;
    Animation rotate_clockwise;
    private AlertDialog progressDialog;

    int cctToolbarColor;
    public static String GAMEZOP_COMMON_URL; // = "https://www.gamezop.com/?id=4114";
    public static String GZOP_MGL_DEDICATED_URL; // = "https://www.gamezop.com/g/<Game URL>?id=4114";
    public static String QUREKA_COMMON_URL; // = "https://633.live.qureka.com";
    public static String QUREKA_PREDCHAMP_DEDICATED_URL; // = "https://633.live.qureka.com/freeentry/?id=<Quiz ID>";

    boolean isCPALeadOfferwallEnabled;
    public static String UNIQUE_CPA_LEAD_OFFERWALL_LINK; // = "https://fastrsrvr.com/list/455436";

    Random random;
    public static int COUNTDOWN_TIME_IN_MINUTES;

    public static long QUIZ_PRED_COUNTDOWN_TIME_IN_MILLIS;
    public static final int MAX_QUIZ_PRED_COUNTDOWN_TIME = 6; // 4 exclusive 6 will not be counted
    public static final int MIN_QUIZ_PRED_COUNTDOWN_TIME = 3; // 2
    public static long QUIZ_PRED_AMOUNT_OF_REWARD;
    //    public static final String QUIZ_AMOUNT_OF_REWARD_TEXT = "+ 75";
    public static String QUIZ_PRED_AMOUNT_OF_REWARD_TEXT; // Also change bsDialog text

    private boolean gamezopClicked = false;
    private int MAX_GAME_QUIZ_REWARD_AMOUNT = 76;  // 151
    private int MIN_GAME_QUIZ_REWARD_AMOUNT = 50;  // 75

    public static  long GAME_COUNTDOWN_TIME_IN_MILLIS;
    public static final int MAX_GAME_COUNTDOWN_TIME = 6;  // 4 6
    public static final int MIN_GAME_COUNTDOWN_TIME = 4;  // 2 3
    public static long GAME_AMOUNT_OF_REWARD;
    //    public static final String GAME_AMOUNT_OF_REWARD_TEXT = "+ 100";
    public static String GAME_AMOUNT_OF_REWARD_TEXT; // Also change bsDialog text

    private long mTimeLeft_in_millis;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;

    private long gameEntryTime;
    private long gameExitTime;

    public WebView mWebView;

    public GameOfferFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_game_offer, container, false);
        binding = FragmentGameOfferBinding.inflate(inflater, container, false);

        context = getActivity();
        loggedInUser = UserContext.getLoggedInUser();
        firebaseDataService = new FirebaseDataService(getActivity());
        random = new Random();
        dbHelper = new DBHelper(context);
        handler = new Handler(Looper.getMainLooper());
        scaleDown = AnimationUtils.loadAnimation(context, R.anim.anim_scale_down);
        largeScaleUp = AnimationUtils.loadAnimation(context, R.anim.anim_large_scale_up);
        blinking = AnimationUtils.loadAnimation(context, R.anim.anim_blinking_repeat);
        rotate_clockwise = AnimationUtils.loadAnimation(context, R.anim.anim_rotate_clockwise);

        MAX_GAME_QUIZ_REWARD_AMOUNT = UserContext.getMaxGameQuizCoins();
        MIN_GAME_QUIZ_REWARD_AMOUNT = UserContext.getMinGameQuizCoins();

//        GAMEZOP_COMMON_URL = UserContext.getGamezopLink();
//        QUREKA_COMMON_URL = UserContext.getQurekaLink();
//        isCPALeadOfferwallEnabled = UserContext.getIsCPALeadOfferwallEnabled();
//        UNIQUE_CPA_LEAD_OFFERWALL_LINK = UserContext.getUniqueUserCpaLeadOfferwallLink();
//
//        isAdmobInterstitialEnabled = UserContext.getIsAdmobInterstitial();
//        isTapdaqInterstitialEnabled = UserContext.getIsTapdaqInterstitial();
//
//        ADMOB_INTERSTITIAL_AD_UNIT_ID = getString(R.string.aM_interstitial_default);
//        TAPDAQ_INTERSTITIAL_AD_UNIT_ID = getString(R.string.tad_inters_video_default);

        updateUICoinsData();
//        loadInterstitialFrom(); // loading ad on main activity

//        binding.gameOfferUserTotalCoins.startAnimation(blinking);

        binding.gameOfferRefreshCoinImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.gameOfferRefreshCoinImage.startAnimation(rotate_clockwise);
                updateUICoinsData();
            }
        });

        binding.lottieAnimationView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.lottieAnimationView7.startAnimation(scaleDown);
                if (isCPALeadOfferwallEnabled) {
                    binding.lottieAnimationView7.startAnimation(largeScaleUp);
                    largeScaleUp.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            openCpaLeadOfferwallURL();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                }
            }
        });

        /**
         * * Qureka Quiz
         * **/
//        binding.qurekaQuizCricketQuiz.startAnimation(blinking);
        binding.qurekaQuizCricketQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.qurekaQuizCricketQuiz.startAnimation(scaleDown);
                sendQuizPredData_to_timerDialog(R.string.qureka_cricket_quiz_url, R.string.qureka_cricket_quiz,
                        R.drawable.qureka_circle_cricket_quiz, R.color.qureka_cricket_quiz);
            }
        });
//        binding.qurekaQuizTechQuiz.startAnimation(blinking);
        binding.qurekaQuizTechQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.qurekaQuizTechQuiz.startAnimation(scaleDown);
                sendQuizPredData_to_timerDialog(R.string.qureka_tech_quiz_url, R.string.qureka_tech_quiz,
                        R.drawable.qureka_circle_tech_quiz, R.color.qureka_tech_quiz);
            }
        });
//        binding.qurekaQuizGkQuiz.startAnimation(blinking);
        binding.qurekaQuizGkQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.qurekaQuizGkQuiz.startAnimation(scaleDown);
                sendQuizPredData_to_timerDialog(R.string.qureka_gk_basic_quiz_url, R.string.qureka_gk_basic_quiz,
                        R.drawable.qureka_circle_gk_quiz, R.color.qureka_gk_basic_quiz);
            }
        });
//        binding.qurekaQuizIplQuiz.startAnimation(blinking);
        binding.qurekaQuizIplQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.qurekaQuizIplQuiz.startAnimation(scaleDown);
                sendQuizPredData_to_timerDialog(R.string.qureka_ipl_quiz_url, R.string.qureka_ipl_quiz,
                        R.drawable.qureka_circle_ipl_quiz, R.color.qureka_ipl_quiz);
            }
        });

/** Commented Qureka Quiz **/
//        binding.qurekaQuizBollywoodQuizCvName.setSelected(true);
//        binding.qurekaQuizBollywoodQuiz.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.qurekaQuizBollywoodQuiz.startAnimation(scaleDown);
//
//                QUIZ_COUNTDOWN_TIME_IN_MILLIS = getRandomQuiz_CountdownTime();
//                COUNTDOWN_TIME_IN_MINUTES = (int) (QUIZ_COUNTDOWN_TIME_IN_MILLIS / 1000) / 60 ;
//                QUIZ_AMOUNT_OF_REWARD = getRandomQuiz_RewardAmount();
//                QUIZ_AMOUNT_OF_REWARD_TEXT = "+ " + QUIZ_AMOUNT_OF_REWARD;
//                QUREKA_DEDICATED_URL = context.getString(R.string.qureka_bollywood_quiz_url);
//
//                String bsDialogTitle = context.getString(R.string.dedicated_timer_bsdialog_qureka_title_play) +
//                        " " + context.getString(R.string.qureka_bollywood_quiz) +
//                        " " + context.getString(R.string.dedicated_timer_bsdialog_qureka_title_for) +
//                        " " + COUNTDOWN_TIME_IN_MINUTES +
//                        " " + context.getString(R.string.dedicated_timer_bsdialog_qureka_title_X_min_and_get) +
//                        " " + QUIZ_AMOUNT_OF_REWARD +
//                        " " + context.getString(R.string.dedicated_timer_bsdialog_qureka_title_coins);
//
//                showTimerBsDialog(1, R.drawable.qureka_sq_bollywood_quiz_80px,
//                        bsDialogTitle, getResources().getColor(R.color.qureka_bollywood_quiz));
//            }
//        });
//        binding.qurekaQuizSportsQuizCvName.setSelected(true);
//        binding.qurekaQuizSportsQuiz.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.qurekaQuizSportsQuiz.startAnimation(scaleDown);
//
//                QUIZ_COUNTDOWN_TIME_IN_MILLIS = getRandomQuiz_CountdownTime();
//                COUNTDOWN_TIME_IN_MINUTES = (int) (QUIZ_COUNTDOWN_TIME_IN_MILLIS / 1000) / 60 ;
//                QUIZ_AMOUNT_OF_REWARD = getRandomQuiz_RewardAmount();
//                QUIZ_AMOUNT_OF_REWARD_TEXT = "+ " + QUIZ_AMOUNT_OF_REWARD;
//                QUREKA_DEDICATED_URL = context.getString(R.string.qureka_sports_quiz_url);
//
//                String bsDialogTitle = context.getString(R.string.dedicated_timer_bsdialog_qureka_title_play) +
//                        " " + context.getString(R.string.qureka_sports_quiz) +
//                        " " + context.getString(R.string.dedicated_timer_bsdialog_qureka_title_for) +
//                        " " + COUNTDOWN_TIME_IN_MINUTES +
//                        " " + context.getString(R.string.dedicated_timer_bsdialog_qureka_title_X_min_and_get) +
//                        " " + QUIZ_AMOUNT_OF_REWARD +
//                        " " + context.getString(R.string.dedicated_timer_bsdialog_qureka_title_coins);
//
//                showTimerBsDialog(1, R.drawable.qureka_sq_sports_quiz_80px,
//                        bsDialogTitle, getResources().getColor(R.color.qureka_sports_quiz));
//            }
//        });
//        binding.qurekaQuizBankingQuizCvName.setSelected(true);
//        binding.qurekaQuizBankingQuiz.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.qurekaQuizBankingQuiz.startAnimation(scaleDown);
//
//                QUIZ_COUNTDOWN_TIME_IN_MILLIS = getRandomQuiz_CountdownTime();
//                COUNTDOWN_TIME_IN_MINUTES = (int) (QUIZ_COUNTDOWN_TIME_IN_MILLIS / 1000) / 60 ;
//                QUIZ_AMOUNT_OF_REWARD = getRandomQuiz_RewardAmount();
//                QUIZ_AMOUNT_OF_REWARD_TEXT = "+ " + QUIZ_AMOUNT_OF_REWARD;
//                QUREKA_DEDICATED_URL = context.getString(R.string.qureka_banking_quiz_url);
//
//                String bsDialogTitle = context.getString(R.string.dedicated_timer_bsdialog_qureka_title_play) +
//                        " " + context.getString(R.string.qureka_banking_quiz) +
//                        " " + context.getString(R.string.dedicated_timer_bsdialog_qureka_title_for) +
//                        " " + COUNTDOWN_TIME_IN_MINUTES +
//                        " " + context.getString(R.string.dedicated_timer_bsdialog_qureka_title_X_min_and_get) +
//                        " " + QUIZ_AMOUNT_OF_REWARD +
//                        " " + context.getString(R.string.dedicated_timer_bsdialog_qureka_title_coins);
//
//                showTimerBsDialog(1, R.drawable.qureka_sq_banking_quiz_80px,
//                        bsDialogTitle, getResources().getColor(R.color.qureka_banking_quiz));
//            }
//        });
//        binding.qurekaQuizGkBulbQuizCvName.setSelected(true);
//        binding.qurekaQuizGkBulbQuiz.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.qurekaQuizGkBulbQuiz.startAnimation(scaleDown);
//
//                QUIZ_COUNTDOWN_TIME_IN_MILLIS = getRandomQuiz_CountdownTime();
//                COUNTDOWN_TIME_IN_MINUTES = (int) (QUIZ_COUNTDOWN_TIME_IN_MILLIS / 1000) / 60 ;
//                QUIZ_AMOUNT_OF_REWARD = getRandomQuiz_RewardAmount();
//                QUIZ_AMOUNT_OF_REWARD_TEXT = "+ " + QUIZ_AMOUNT_OF_REWARD;
//                QUREKA_DEDICATED_URL = context.getString(R.string.qureka_gk_bulb_quiz_url);
//
//                String bsDialogTitle = context.getString(R.string.dedicated_timer_bsdialog_qureka_title_play) +
//                        " " + context.getString(R.string.qureka_gk_bulb_quiz) +
//                        " " + context.getString(R.string.dedicated_timer_bsdialog_qureka_title_for) +
//                        " " + COUNTDOWN_TIME_IN_MINUTES +
//                        " " + context.getString(R.string.dedicated_timer_bsdialog_qureka_title_X_min_and_get) +
//                        " " + QUIZ_AMOUNT_OF_REWARD +
//                        " " + context.getString(R.string.dedicated_timer_bsdialog_qureka_title_coins);
//
//                showTimerBsDialog(1, R.drawable.qureka_sq_gk_bulb_quiz_80px,
//                        bsDialogTitle, getResources().getColor(R.color.qureka_gk_bulb_quiz));
//            }
//        });
/** Commented Qureka Quiz **/

        /***
         *
         * * * * *
         *
         * ***/

//        binding.predchampCricketPrediction1.startAnimation(blinking);
        binding.predchampCricketPrediction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                binding.predchampCricketPrediction1.startAnimation(scaleDown);
                sendQuizPredData_to_timerDialog(R.string.predchamp_predict_cricket_result_url_1,
                        R.string.predchamp_predict_cricket_result_1, R.drawable.predchamp_predict_cricket_result_png_1,
                        R.color.predchamp_predict_cricket_result_1);
            }
        });
//        binding.predchampPredictResultWinCoin.startAnimation(blinking);
        binding.predchampPredictResultWinCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                binding.predchampPredictResultWinCoin.startAnimation(scaleDown);
                sendQuizPredData_to_timerDialog(R.string.predchamp_predict_result_win_coin_url, R.string.predchamp_predict_result_win_coin,
                        R.drawable.predchamp_predict_result_win_coin_png, R.color.predchamp_predict_result_win_coin);
            }
        });
//        binding.predchampMoviePrediction.startAnimation(blinking);
        binding.predchampMoviePrediction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                binding.predchampMoviePrediction.startAnimation(scaleDown);
                sendQuizPredData_to_timerDialog(R.string.predchamp_predict_movie_url, R.string.predchamp_predict_movie,
                        R.drawable.predchamp_predict_movie_png, R.color.predchamp_predict_movie);
            }
        });
//        binding.predchampCricketPrediction2.startAnimation(blinking);
        binding.predchampCricketPrediction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                binding.predchampCricketPrediction2.startAnimation(scaleDown);
                sendQuizPredData_to_timerDialog(R.string.predchamp_predict_cricket_result_url_2, R.string.predchamp_predict_cricket_result_2,
                        R.drawable.predchamp_predict_cricket_result_png_2, R.color.predchamp_predict_cricket_result_2);
            }
        });

        /***
         *
         * * * * *
         *
         * ***/

        /*****
         * * * Show more / Hide *****/
        binding.gameOfferQurekaQuizShowMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideQurekaQuizGames();
            }
        });
        binding.gameOfferQurekaQuizShowMoreIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideQurekaQuizGames();
            }
        });

        /**
         *
         * * * * * GAMEZOP / MGL Gamez
         *
         **/
//        binding.mglGamezEarthHeroCvName.setSelected(true);
        binding.mglGamezEarthHero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.mglGamezEarthHero.startAnimation(scaleDown);
                gamezopClicked = false;
                sendGzopMglData_to_timerDialog(R.string.mglGamez_earth_hero_url, R.string.mglGamez_earth_hero,
                        R.drawable.mgl_gamez_action_earth_hero, R.color.mglGamez_earth_hero);
            }
        });
//        binding.mglGamezKnifeHitCvName.setSelected(true);
        binding.mglGamezKnifeHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.mglGamezKnifeHit.startAnimation(scaleDown);
                gamezopClicked = false;
                sendGzopMglData_to_timerDialog(R.string.mglGamez_knife_hit_url, R.string.mglGamez_knife_hit,
                        R.drawable.mgl_gamez_action_knife_hit, R.color.mglGamez_knife_hit);
            }
        });


//        binding.mglGamezBoxTowerCvName.setSelected(true);
        binding.mglGamezBoxTower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.mglGamezBoxTower.startAnimation(scaleDown);
                gamezopClicked = false;
                sendGzopMglData_to_timerDialog(R.string.mglGamez_box_tower_url, R.string.mglGamez_box_tower,
                        R.drawable.mgl_gamez_arcade_box_tower, R.color.mglGamez_box_tower);
            }
        });
//        binding.mglGamezBubbleShooterCvName.setSelected(true);
        binding.mglGamezBubbleShooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.mglGamezBubbleShooter.startAnimation(scaleDown);
                gamezopClicked = false;
                sendGzopMglData_to_timerDialog(R.string.mglGamez_bubble_shooter_url, R.string.mglGamez_bubble_shooter,
                        R.drawable.mgl_gamez_arcade_bubble_shooter, R.color.mglGamez_bubble_shooter);
            }
        });
//        binding.mglGamezFruitSlashCvName.setSelected(true);
        binding.mglGamezFruitSlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.mglGamezFruitSlash.startAnimation(scaleDown);
                gamezopClicked = false;
                sendGzopMglData_to_timerDialog(R.string.mglGamez_fruit_slash_url, R.string.mglGamez_fruit_slash,
                        R.drawable.mgl_gamez_arcade_fruit_slash, R.color.mglGamez_fruit_slash);
            }
        });


//        binding.mglGamezDontCrashCvName.setSelected(true);
        binding.mglGamezDontCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.mglGamezDontCrash.startAnimation(scaleDown);
                gamezopClicked = false;
                sendGzopMglData_to_timerDialog(R.string.mglGamez_dont_crash_url, R.string.mglGamez_dont_crash,
                        R.drawable.mgl_gamez_sports_racing_dont_crash, R.color.mglGamez_dont_crash);
            }
        });


        /**
         * * Action
         * **/
//        binding.gamezopBlazingBladesCvName.setSelected(true);
        binding.gamezopBlazingBlades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopBlazingBlades.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_blazing_blades_url, R.string.gamezop_blazing_blades,
                        R.drawable.gamezop_action_blazing_blades_webp, R.color.gamezop_blazing_blades);
            }
        });
//        binding.gamezopBottleShootCvName.setSelected(true);
        binding.gamezopBottleShoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                binding.gamezopBottleShoot.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_bottle_shoot_url, R.string.gamezop_bottle_shoot,
                        R.drawable.gamezop_action_bottle_shoot_webp, R.color.gamezop_bottle_shoot);
            }
        });
//        binding.gamezopBoulderBlastCvName.setSelected(true);
        binding.gamezopBoulderBlast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                binding.gamezopBoulderBlast.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_boulder_blast_url, R.string.gamezop_boulder_blast,
                        R.drawable.gamezop_action_boulder_blast_webp, R.color.gamezop_boulder_blast);
            }
        });
//        binding.gamezopSaloonRobberyCvName.setSelected(true);
        binding.gamezopSaloonRobbery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                binding.gamezopSaloonRobbery.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_saloon_robbery_url, R.string.gamezop_saloon_robbery,
                        R.drawable.gamezop_action_saloon_robbery_webp, R.color.gamezop_saloon_robbery);
            }
        });
//        binding.gamezopSavageRevengeCvName.setSelected(true);
        binding.gamezopSavageRevenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                binding.gamezopSavageRevenge.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_savage_revenge_url, R.string.gamezop_savage_revenge,
                        R.drawable.gamezop_action_savage_revenge_webp, R.color.gamezop_savage_revenge);
            }
        });
//        binding.gamezopSlapFestCvName.setSelected(true);
        binding.gamezopSlapFest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                binding.gamezopSlapFest.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_slap_fest_url, R.string.gamezop_slap_fest,
                        R.drawable.gamezop_action_slap_fest_webp, R.color.gamezop_slap_fest);
            }
        });
//        binding.gamezopZombiesCantJumpCvName.setSelected(true);
        binding.gamezopZombiesCantJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                binding.gamezopZombiesCantJump.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_zombies_cant_jump_url, R.string.gamezop_zombies_cant_jump,
                        R.drawable.gamezop_action_zombies_cant_jump_webp, R.color.gamezop_zombies_cant_jump);
            }
        });

        /*****
         * * * Show more / Hide *****/
        binding.gameOfferActionShowMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideActionGames();
            }
        });
        binding.gameOfferActionShowMoreIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideActionGames();
            }
        });

        /**
         *
         * * * * *
         *
         **/

        /**
         * * Adventure
         * **/
        // binding.gamezopDodgeBotCvName.setSelected(true);
        binding.gamezopDodgeBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopDodgeBot.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_dodge_bot_url, R.string.gamezop_dodge_bot,
                        R.drawable.gamezop_adventure_dodge_bot_webp, R.color.gamezop_dodge_bot);
            }
        });
        // binding.gamezopEscapeRunCvName.setSelected(true);
        binding.gamezopEscapeRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopEscapeRun.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_escape_run_url, R.string.gamezop_escape_run,
                        R.drawable.gamezop_adventure_escape_run_webp, R.color.gamezop_escape_run);
            }
        });
        // binding.gamezopFlyingSchoolCvName.setSelected(true);
        binding.gamezopFlyingSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopFlyingSchool.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_flying_school_url, R.string.gamezop_flying_school,
                        R.drawable.gamezop_adventure_flying_school_webp, R.color.gamezop_flying_school);
            }
        });
        // binding.gamezopJimboJumpCvName.setSelected(true);
        binding.gamezopJimboJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopJimboJump.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_jimbo_jump_url, R.string.gamezop_jimbo_jump,
                        R.drawable.gamezop_adventure_jimbo_jump_webp, R.color.gamezop_jimbo_jump);
            }
        });
        // binding.gamezopKnifeFlipCvName.setSelected(true);
        binding.gamezopKnifeFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopKnifeFlip.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_knife_flip_url, R.string.gamezop_knife_flip,
                        R.drawable.gamezop_adventure_knife_flip_webp, R.color.gamezop_knife_flip);
            }
        });
        // binding.gamezopRopeNinjaCvName.setSelected(true);
        binding.gamezopRopeNinja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopRopeNinja.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_rope_ninja_url, R.string.gamezop_rope_ninja,
                        R.drawable.gamezop_adventure_rope_ninja_webp, R.color.gamezop_rope_ninja);
            }
        });
        // binding.gamezopStickyGooCvName.setSelected(true);
        binding.gamezopStickyGoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopStickyGoo.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_sticky_goo_url, R.string.gamezop_sticky_goo,
                        R.drawable.gamezop_adventure_sticky_goo_webp, R.color.gamezop_sticky_goo);
            }
        });
        // binding.gamezopSwayBayCvName.setSelected(true);
        binding.gamezopSwayBay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopSwayBay.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_sway_bay_url, R.string.gamezop_sway_bay,
                        R.drawable.gamezop_adventure_sway_bay_webp, R.color.gamezop_sway_bay);
            }
        });

        /*****
         * * * Show more / Hide *****/
        binding.gameOfferAdventureShowMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideAdventureGames();
            }
        });
        binding.gameOfferAdventureShowMoreIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideAdventureGames();
            }
        });

        /**
         *
         * * * * *
         *
         **/

        /**
         * * Arcade
         * **/
        // binding.gamezopBouncyCvName.setSelected(true);
        binding.gamezopBouncy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopBouncy.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_bouncy_url, R.string.gamezop_bouncy,
                        R.drawable.gamezop_arcade_bouncy_webp, R.color.gamezop_bouncy);
            }
        });
        // binding.gamezopFallingThroughCvName.setSelected(true);
        binding.gamezopFallingThrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopFallingThrough.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_falling_through_url, R.string.gamezop_falling_through,
                        R.drawable.gamezop_arcade_falling_through_webp, R.color.gamezop_falling_through);
            }
        });
        // binding.gamezopFlexiSnakeCvName.setSelected(true);
        binding.gamezopFlexiSnake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopFlexiSnake.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_flexi_snake_url, R.string.gamezop_flexi_snake,
                        R.drawable.gamezop_arcade_flexi_snake_webp, R.color.gamezop_flexi_snake);
            }
        });
        // binding.gamezopFruitChopCvName.setSelected(true);
        binding.gamezopFruitChop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopFruitChop.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_fruit_chop_url, R.string.gamezop_fruit_chop,
                        R.drawable.gamezop_arcade_fruit_chop_webp, R.color.gamezop_fruit_chop);
            }
        });
        // binding.gamezopGrumpyGorillaCvName.setSelected(true);
        binding.gamezopGrumpyGorilla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopGrumpyGorilla.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_grumpy_gorilla_url, R.string.gamezop_grumpy_gorilla,
                        R.drawable.gamezop_arcade_grumpy_gorilla_webp, R.color.gamezop_grumpy_gorilla);
            }
        });
        // binding.gamezopLightTowerCvName.setSelected(true);
        binding.gamezopLightTower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopLightTower.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_light_tower_url, R.string.gamezop_light_tower,
                        R.drawable.gamezop_arcade_light_tower_webp, R.color.gamezop_light_tower);
            }
        });
        // binding.gamezopSheepStackingCvName.setSelected(true);
        binding.gamezopSheepStacking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopSheepStacking.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_sheep_stacking_url, R.string.gamezop_sheep_stacking,
                        R.drawable.gamezop_arcade_sheep_stacking_webp, R.color.gamezop_sheep_stacking);
            }
        });
        // binding.gamezopTowerTwistCvName.setSelected(true);
        binding.gamezopTowerTwist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopTowerTwist.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_tower_twist_url, R.string.gamezop_tower_twist,
                        R.drawable.gamezop_arcade_tower_twist_webp, R.color.gamezop_tower_twist);
            }
        });
        // binding.gamezopTrickyTripCvName.setSelected(true);
        binding.gamezopTrickyTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopTrickyTrip.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_tricky_trip_url, R.string.gamezop_tricky_trip,
                        R.drawable.gamezop_arcade_tricky_trip_webp, R.color.gamezop_tricky_trip);
            }
        });

        /*****
         * * * Show more / Hide *****/
        binding.gameOfferArcadeShowMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideArcadeGames();
            }
        });
        binding.gameOfferArcadeShowMoreIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideArcadeGames();
            }
        });

        /**
         * * Sports & Racing
         * **/
        // binding.gamezopArcheryChampsCvName.setSelected(true);
        binding.gamezopArcheryChamps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopArcheryChamps.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_archery_champs_url, R.string.gamezop_archery_champs,
                        R.drawable.gamezop_sports_racing_archery_champs_webp, R.color.gamezop_archery_champs);
            }
        });
        // binding.gamezopBasketChampsCvName.setSelected(true);
        binding.gamezopBasketChamps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopBasketChamps.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_basket_champs_url, R.string.gamezop_basket_champs,
                        R.drawable.gamezop_sports_racing_basket_champs_webp, R.color.gamezop_basket_champs);
            }
        });
        // binding.gamezopBasketballMasterCvName.setSelected(true);
        binding.gamezopBasketballMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopBasketballMaster.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_basketball_master_url, R.string.gamezop_basketball_master,
                        R.drawable.gamezop_sports_racing_basketball_master_webp, R.color.gamezop_basketball_master);
            }
        });
        // binding.gamezopCityCricketCvName.setSelected(true);
        binding.gamezopCityCricket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopCityCricket.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_city_cricket_url, R.string.gamezop_city_cricket,
                        R.drawable.gamezop_sports_racing_city_cricket_webp, R.color.gamezop_city_cricket);
            }
        });
        // binding.gamezopDribbleKingsCvName.setSelected(true);
        binding.gamezopDribbleKings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopDribbleKings.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_dribble_kings_url, R.string.gamezop_dribble_kings,
                        R.drawable.gamezop_sports_racing_dribble_kings_webp, R.color.gamezop_dribble_kings);
            }
        });
        // binding.gamezopFuriousSpeedCvName.setSelected(true);
        binding.gamezopFuriousSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopFuriousSpeed.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_furious_speed_url, R.string.gamezop_furious_speed,
                        R.drawable.gamezop_sports_racing_furious_speed_webp, R.color.gamezop_furious_speed);
            }
        });
        // binding.gamezopGroovySkiCvName.setSelected(true);
        binding.gamezopGroovySki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopGroovySki.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_groovy_ski_url, R.string.gamezop_groovy_ski,
                        R.drawable.gamezop_sports_racing_groovy_ski_webp, R.color.gamezop_groovy_ski);
            }
        });
        // binding.gamezopHomerunHitCvName.setSelected(true);
        binding.gamezopHomerunHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopHomerunHit.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_homerun_hit_url, R.string.gamezop_homerun_hit,
                        R.drawable.gamezop_sports_racing_homerun_hit_webp, R.color.gamezop_homerun_hit);
            }
        });
        // binding.gamezopQuackHuntCvName.setSelected(true);
        binding.gamezopQuackHunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopQuackHunt.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_quack_hunt_url, R.string.gamezop_quack_hunt,
                        R.drawable.gamezop_sports_racing_quack_hunt_webp, R.color.gamezop_quack_hunt);
            }
        });
        // binding.gamezopSuperGoalieAuditionsCvName.setSelected(true);
        binding.gamezopSuperGoalieAuditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopSuperGoalieAuditions.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_super_goalie_auditions_url, R.string.gamezop_super_goalie_auditions,
                        R.drawable.gamezop_sports_racing_super_goalie_auditions_webp, R.color.gamezop_super_goalie_auditions);
            }
        });
        // binding.gamezopTableTennisShotsCvName.setSelected(true);
        binding.gamezopTableTennisShots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopTableTennisShots.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_table_tennis_shots_url, R.string.gamezop_table_tennis_shots,
                        R.drawable.gamezop_sports_racing_table_tennis_shots_webp, R.color.gamezop_table_tennis_shots);
            }
        });

        /*****
         * * * Show more / Hide *****/
        binding.gameOfferSportsRacingShowMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideSportsRacingGames();
            }
        });
        binding.gameOfferSportsRacingShowMoreIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideSportsRacingGames();
            }
        });

        /**
         *
         * * * * *
         *
         **/

        /**
         * * Strategy
         * **/
        // binding.gamezopBrickPlungeCvName.setSelected(true);
        binding.gamezopBrickPlunge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopBrickPlunge.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_brick_plunge_url, R.string.gamezop_brick_plunge,
                        R.drawable.gamezop_strategy_brick_plunge_webp, R.color.gamezop_brick_plunge);
            }
        });
        // binding.gamezopBubbleWipeoutCvName.setSelected(true);
        binding.gamezopBubbleWipeout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopBubbleWipeout.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_bubble_wipeout_url, R.string.gamezop_bubble_wipeout,
                        R.drawable.gamezop_strategy_bubble_wipeout_webp, R.color.gamezop_bubble_wipeout);
            }
        });
        // binding.gamezopCarromHeroCvName.setSelected(true);
        binding.gamezopCarromHero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopCarromHero.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_carrom_hero_url, R.string.gamezop_carrom_hero,
                        R.drawable.gamezop_strategy_carrom_hero_webp, R.color.gamezop_carrom_hero);
            }
        });
        // binding.gamezopHexBurstCvName.setSelected(true);
        binding.gamezopHexBurst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopHexBurst.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_hex_burst_url, R.string.gamezop_hex_burst,
                        R.drawable.gamezop_strategy_hex_burst_webp, R.color.gamezop_hex_burst);
            }
        });
        // binding.gamezopLudoCvName.setSelected(true);
        binding.gamezopLudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopLudo.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_ludo_url, R.string.gamezop_ludo,
                        R.drawable.gamezop_strategy_ludo_webp, R.color.gamezop_ludo);
            }
        });
        // binding.gamezopRummyCvName.setSelected(true);
        binding.gamezopRummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopRummy.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_rummy_url, R.string.gamezop_rummy,
                        R.drawable.gamezop_strategy_rummy_webp, R.color.gamezop_rummy);
            }
        });
        // binding.gamezopShipItUpCvName.setSelected(true);
        binding.gamezopShipItUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopShipItUp.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_ship_it_up_url, R.string.gamezop_ship_it_up,
                        R.drawable.gamezop_strategy_ship_it_up_webp, R.color.gamezop_ship_it_up);
            }
        });
        // binding.gamezopSolitaireGoldCvName.setSelected(true);
        binding.gamezopSolitaireGold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopSolitaireGold.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_solitaire_gold_url, R.string.gamezop_solitaire_gold,
                        R.drawable.gamezop_strategy_solitaire_gold_webp, R.color.gamezop_solitaire_gold);
            }
        });
        // binding.gamezopTrafficCommandCvName.setSelected(true);
        binding.gamezopTrafficCommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopTrafficCommand.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_traffic_command_url, R.string.gamezop_traffic_command,
                        R.drawable.gamezop_strategy_traffic_command_webp, R.color.gamezop_traffic_command);
            }
        });
        // binding.gamezopYummyTacoCvName.setSelected(true);
        binding.gamezopYummyTaco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopYummyTaco.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_yummy_taco_url, R.string.gamezop_yummy_taco,
                        R.drawable.gamezop_strategy_yummy_taco_webp, R.color.gamezop_yummy_taco);
            }
        });

        /*****
         * * * Show more / Hide *****/
        binding.gameOfferStrategyShowMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideStrategyGames();
            }
        });
        binding.gameOfferStrategyShowMoreIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideStrategyGames();
            }
        });

        /**
         * * Puzzle & Logic
         * **/
        // binding.gamezop2048CvName.setSelected(true);
        binding.gamezop2048.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezop2048.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_2048_url, R.string.gamezop_2048,
                        R.drawable.gamezop_puzzle_logic_2048_webp, R.color.gamezop_2048);
            }
        });
        // binding.gamezopCountdownCalculatorCvName.setSelected(true);
        binding.gamezopCountdownCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopCountdownCalculator.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_countdown_calculator_url, R.string.gamezop_countdown_calculator,
                        R.drawable.gamezop_puzzle_logic_countdown_calculator_webp, R.color.gamezop_countdown_calculator);
            }
        });
        // binding.gamezopHighOrLowCvName.setSelected(true);
        binding.gamezopHighOrLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopHighOrLow.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_high_or_low_url, R.string.gamezop_high_or_low,
                        R.drawable.gamezop_puzzle_logic_high_or_low_webp, R.color.gamezop_high_or_low);
            }
        });
        // binding.gamezopJelloGoRoundCvName.setSelected(true);
        binding.gamezopJelloGoRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopJelloGoRound.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_jello_go_round_url, R.string.gamezop_jello_go_round,
                        R.drawable.gamezop_puzzle_logic_jello_go_round_webp, R.color.gamezop_jello_go_round);
            }
        });
        // binding.gamezopJellySliceCvName.setSelected(true);
        binding.gamezopJellySlice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopJellySlice.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_jelly_slice_url, R.string.gamezop_jelly_slice,
                        R.drawable.gamezop_puzzle_logic_jelly_slice_webp, R.color.gamezop_jelly_slice);
            }
        });
        // binding.gamezopMemoryMatchUpCvName.setSelected(true);
        binding.gamezopMemoryMatchUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopMemoryMatchUp.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_memory_match_up_url, R.string.gamezop_memory_match_up,
                        R.drawable.gamezop_puzzle_logic_memory_match_up_webp, R.color.gamezop_memory_match_up);
            }
        });
        // binding.gamezopSlitSightCvName.setSelected(true);
        binding.gamezopSlitSight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopSlitSight.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_slit_sight_url, R.string.gamezop_slit_sight,
                        R.drawable.gamezop_puzzle_logic_slit_sight_webp, R.color.gamezop_slit_sight);
            }
        });
        // binding.gamezopSudokoClassicCvName.setSelected(true);
        binding.gamezopSudokoClassic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopSudokoClassic.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_sudoko_classic_url, R.string.gamezop_sudoko_classic,
                        R.drawable.gamezop_puzzle_logic_sudoko_classic_webp, R.color.gamezop_sudoko_classic);
            }
        });
        // binding.gamezopTicTacToe11CvName.setSelected(true);
        binding.gamezopTicTacToe11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // binding.gamezopTicTacToe11.startAnimation(scaleDown);
                gamezopClicked = true;
                sendGzopMglData_to_timerDialog(R.string.gamezop_tic_tac_toe_11_url, R.string.gamezop_tic_tac_toe_11,
                        R.drawable.gamezop_puzzle_logic_tic_tac_toe_11_webp, R.color.gamezop_tic_tac_toe_11);
            }
        });

        /*****
         * * * Show more / Hide *****/
        binding.gameOfferPuzzleLogicShowMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHidePuzzleLogicGames();
            }
        });
        binding.gameOfferPuzzleLogicShowMoreIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHidePuzzleLogicGames();
            }
        });


        return binding.getRoot();
    }

    public void showHideQurekaQuizGames() {
//        if (binding.gameOfferQurekaQuizExpandableLayout.getVisibility() == View.GONE) {
//            TransitionManager.beginDelayedTransition(binding.gameOfferQurekaQuizCardView, new AutoTransition());
//            binding.gameOfferQurekaQuizShowMoreText.setText(context.getString(R.string.gameOffer_frag_show_less_game));
//            binding.gameOfferQurekaQuizShowMoreIcon.setRotation(180);
//            binding.gameOfferQurekaQuizExpandableLayout.setVisibility(View.VISIBLE);
//        } else {
//            TransitionManager.beginDelayedTransition(binding.gameOfferQurekaQuizCardView, new AutoTransition());
//            binding.gameOfferQurekaQuizShowMoreText.setText(context.getString(R.string.gameOffer_frag_show_more_game));
//            binding.gameOfferQurekaQuizShowMoreIcon.setRotation(0);
//            binding.gameOfferQurekaQuizExpandableLayout.setVisibility(View.GONE);
//        }
    }

    public void showHideActionGames() {
        if (binding.gameOfferActionGamesExpandableLayout.getVisibility() == View.GONE) {
            TransitionManager.beginDelayedTransition(binding.gameOfferActionGamesCardView, new AutoTransition());
            binding.gameOfferActionShowMoreText.setText(context.getString(R.string.gameOffer_frag_show_less_game));
            binding.gameOfferActionShowMoreIcon.setRotation(180);
            binding.gameOfferActionGamesExpandableLayout.setVisibility(View.VISIBLE);
        } else {
            TransitionManager.beginDelayedTransition(binding.gameOfferActionGamesCardView, new Slide());
            binding.gameOfferActionShowMoreText.setText(context.getString(R.string.gameOffer_frag_show_more_game));
            binding.gameOfferActionShowMoreIcon.setRotation(0);
            binding.gameOfferActionGamesExpandableLayout.setVisibility(View.GONE);
        }
    }

    public void showHideAdventureGames() {
        if (binding.gameOfferAdventureGamesExpandableLayout.getVisibility() == View.GONE) {
            TransitionManager.beginDelayedTransition(binding.gameOfferAdventureGamesCardView, new AutoTransition());
            binding.gameOfferAdventureShowMoreText.setText(context.getString(R.string.gameOffer_frag_show_less_game));
            binding.gameOfferAdventureShowMoreIcon.setRotation(180);
            binding.gameOfferAdventureGamesExpandableLayout.setVisibility(View.VISIBLE);
        } else {
            TransitionManager.beginDelayedTransition(binding.gameOfferAdventureGamesCardView, new Slide());
            binding.gameOfferAdventureShowMoreText.setText(context.getString(R.string.gameOffer_frag_show_more_game));
            binding.gameOfferAdventureShowMoreIcon.setRotation(0);
            binding.gameOfferAdventureGamesExpandableLayout.setVisibility(View.GONE);
        }
    }

    public void showHideArcadeGames() {
        if (binding.gameOfferArcadeGamesExpandableLayout.getVisibility() == View.GONE) {
            TransitionManager.beginDelayedTransition(binding.gameOfferArcadeGamesCardView, new AutoTransition());
            binding.gameOfferArcadeShowMoreText.setText(context.getString(R.string.gameOffer_frag_show_less_game));
            binding.gameOfferArcadeShowMoreIcon.setRotation(180);
            binding.gameOfferArcadeGamesExpandableLayout.setVisibility(View.VISIBLE);
        } else {
            TransitionManager.beginDelayedTransition(binding.gameOfferArcadeGamesCardView, new Slide());
            binding.gameOfferArcadeShowMoreText.setText(context.getString(R.string.gameOffer_frag_show_more_game));
            binding.gameOfferArcadeShowMoreIcon.setRotation(0);
            binding.gameOfferArcadeGamesExpandableLayout.setVisibility(View.GONE);
        }
    }

    public void showHideSportsRacingGames() {
        if (binding.gameOfferSportsRacingGamesExpandableLayout.getVisibility() == View.GONE) {
            TransitionManager.beginDelayedTransition(binding.gameOfferSportsRacingGamesCardView, new AutoTransition());
            binding.gameOfferSportsRacingShowMoreText.setText(context.getString(R.string.gameOffer_frag_show_less_game));
            binding.gameOfferSportsRacingShowMoreIcon.setRotation(180);
            binding.gameOfferSportsRacingGamesExpandableLayout.setVisibility(View.VISIBLE);
        } else {
            TransitionManager.beginDelayedTransition(binding.gameOfferSportsRacingGamesCardView, new Slide());
            binding.gameOfferSportsRacingShowMoreText.setText(context.getString(R.string.gameOffer_frag_show_more_game));
            binding.gameOfferSportsRacingShowMoreIcon.setRotation(0);
            binding.gameOfferSportsRacingGamesExpandableLayout.setVisibility(View.GONE);
        }
    }

    public void showHideStrategyGames() {
        if (binding.gameOfferStrategyGamesExpandableLayout.getVisibility() == View.GONE) {
            TransitionManager.beginDelayedTransition(binding.gameOfferStrategyGamesCardView, new AutoTransition());
            binding.gameOfferStrategyShowMoreText.setText(context.getString(R.string.gameOffer_frag_show_less_game));
            binding.gameOfferStrategyShowMoreIcon.setRotation(180);
            binding.gameOfferStrategyGamesExpandableLayout.setVisibility(View.VISIBLE);
        } else {
            TransitionManager.beginDelayedTransition(binding.gameOfferStrategyGamesCardView, new Slide());
            binding.gameOfferStrategyShowMoreText.setText(context.getString(R.string.gameOffer_frag_show_more_game));
            binding.gameOfferStrategyShowMoreIcon.setRotation(0);
            binding.gameOfferStrategyGamesExpandableLayout.setVisibility(View.GONE);
        }
    }

    public void showHidePuzzleLogicGames() {
        if (binding.gameOfferPuzzleLogicGamesExpandableLayout.getVisibility() == View.GONE) {
            TransitionManager.beginDelayedTransition(binding.gameOfferPuzzleLogicGamesCardView, new AutoTransition());
            binding.gameOfferPuzzleLogicShowMoreText.setText(context.getString(R.string.gameOffer_frag_show_less_game));
            binding.gameOfferPuzzleLogicShowMoreIcon.setRotation(180);
            binding.gameOfferPuzzleLogicGamesExpandableLayout.setVisibility(View.VISIBLE);
        } else {
            TransitionManager.beginDelayedTransition(binding.gameOfferPuzzleLogicGamesCardView, new Slide());
            binding.gameOfferPuzzleLogicShowMoreText.setText(context.getString(R.string.gameOffer_frag_show_more_game));
            binding.gameOfferPuzzleLogicShowMoreIcon.setRotation(0);
            binding.gameOfferPuzzleLogicGamesExpandableLayout.setVisibility(View.GONE);
        }
    }

    private void openCpaLeadOfferwallURL() {
        CustomTabColorSchemeParams setCCTBarColors = new CustomTabColorSchemeParams.Builder()
                .setToolbarColor(cctToolbarColor)
                .build();

        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(setCCTBarColors)
                .build();
        customTabsIntent.launchUrl(context, Uri.parse(UNIQUE_CPA_LEAD_OFFERWALL_LINK));
    }


    private void updateUICoinsData() {
//        String sql_totalCoins = dbHelper.getFreeAdGameDataByUserIdAndGameId(loggedInUser().getId(), sqlTotalCoinsCOL).getCash();
        String sql_totalCoins = firebaseDataService.getCoinBalance();
//        String sql_totalCoins = UserContext.getAllGameInOneMapByGameId(sqlTotalCoinsCOL).getCoins();
        binding.gameOfferUserTotalCoins.setText(sql_totalCoins);
    }

    private long getRandomQuizPredict_CountdownTime() {
        int randomQuizCountdownTime = random.nextInt(MAX_QUIZ_PRED_COUNTDOWN_TIME - MIN_QUIZ_PRED_COUNTDOWN_TIME) + MIN_QUIZ_PRED_COUNTDOWN_TIME;
        long convertRandomTime_to_millis = (randomQuizCountdownTime * 1000) * 60;
        return convertRandomTime_to_millis;
    }
    private long getRandomQuizPredict_RewardAmount() {
        int randomQuizReward = random.nextInt(MAX_GAME_QUIZ_REWARD_AMOUNT - MIN_GAME_QUIZ_REWARD_AMOUNT) + MIN_GAME_QUIZ_REWARD_AMOUNT;
//        long convertRandomToLong = randomQuizReward * ((7 - 5) * (4 + 1));
        return randomQuizReward;
    }

    private long getRandomGame_CountdownTime() {
        int randomGameCountdownTime = random.nextInt(MAX_GAME_COUNTDOWN_TIME - MIN_GAME_COUNTDOWN_TIME) + MIN_GAME_COUNTDOWN_TIME;
        long convertRandomTime_to_millis = (randomGameCountdownTime * 1000) * 60;
        return convertRandomTime_to_millis;
    }
    private long getRandomGame_RewardAmount() {
        int randomGameReward = random.nextInt(MAX_GAME_QUIZ_REWARD_AMOUNT - MIN_GAME_QUIZ_REWARD_AMOUNT) + MIN_GAME_QUIZ_REWARD_AMOUNT;
        return randomGameReward;
    }

    private void sendQuizPredData_to_timerDialog(int stringURL, int stringName,
                                                 int timerDialog_drawableIcon, int timerDialog_colorSpecific) {
        QUIZ_PRED_COUNTDOWN_TIME_IN_MILLIS = getRandomQuizPredict_CountdownTime();
        COUNTDOWN_TIME_IN_MINUTES = (int) (QUIZ_PRED_COUNTDOWN_TIME_IN_MILLIS / 1000) / 60 ;
        QUIZ_PRED_AMOUNT_OF_REWARD = getRandomQuizPredict_RewardAmount();
        QUIZ_PRED_AMOUNT_OF_REWARD_TEXT = "+ " + QUIZ_PRED_AMOUNT_OF_REWARD;
        QUREKA_PREDCHAMP_DEDICATED_URL = context.getString(stringURL);

        String bsDialogTitle = context.getString(R.string.dedicated_timer_bsdialog_qureka_title_play) +
                " " + context.getString(stringName) +
                " " + context.getString(R.string.dedicated_timer_bsdialog_qureka_title_for) +
                " " + COUNTDOWN_TIME_IN_MINUTES +
                " " + context.getString(R.string.dedicated_timer_bsdialog_qureka_title_X_min_and_get) +
                " " + QUIZ_PRED_AMOUNT_OF_REWARD +
                " " + context.getString(R.string.dedicated_timer_bsdialog_qureka_title_coins);

        showTimerBsDialog(1, timerDialog_drawableIcon,
                bsDialogTitle, getResources().getColor(R.color.white));
    }

    private void sendGzopMglData_to_timerDialog(int stringURL, int stringName,
                                                int timerDialog_drawableIcon, int timerDialog_colorSpecific) {
        GAME_COUNTDOWN_TIME_IN_MILLIS = getRandomGame_CountdownTime();
        COUNTDOWN_TIME_IN_MINUTES = (int) (GAME_COUNTDOWN_TIME_IN_MILLIS / 1000) / 60 ;
        GAME_AMOUNT_OF_REWARD = getRandomGame_RewardAmount();
        GAME_AMOUNT_OF_REWARD_TEXT = "+ " + GAME_AMOUNT_OF_REWARD;
//        https://www.gamezop.com/g/SJ3-ELT8p-x?id=4114&sub=aadarshyadav95@gmailcom
        if (gamezopClicked) {
            String userSpecificGameUrl = new StringBuffer()
                    .append(context.getString(stringURL))
                    .append("&sub=")
                    .append(loggedInUser.getAuthUid()).toString();
            GZOP_MGL_DEDICATED_URL = userSpecificGameUrl;
        } else {
            GZOP_MGL_DEDICATED_URL = context.getString(stringURL);
        }

        String bsDialogTitle = new StringBuffer()
                .append(context.getString(R.string.dedicated_timer_bsdialog_game_title_play))
                .append(" ").append(context.getString(stringName))
                .append(" ").append(context.getString(R.string.dedicated_timer_bsdialog_game_title_for))
                .append(" ").append(COUNTDOWN_TIME_IN_MINUTES)
                .append(" ")
                .append(context.getString(R.string.dedicated_timer_bsdialog_game_title_X_min_and_get))
                .append(" ").append(GAME_AMOUNT_OF_REWARD)
                .append(" ").append(context.getString(R.string.dedicated_timer_bsdialog_game_title_coins)).toString();

        showTimerBsDialog(3, timerDialog_drawableIcon,
                bsDialogTitle, getResources().getColor(R.color.white));
    }

    private void openCCT_URL(int QU1_PC1_GZ3_MGL3) {
        String cct_URL = null;
        if (QU1_PC1_GZ3_MGL3 == 1) {
            cct_URL = QUREKA_PREDCHAMP_DEDICATED_URL;
            cctToolbarColor = context.getResources().getColor(R.color.darker_blue_app_theme);
        }

        if (QU1_PC1_GZ3_MGL3 == 3) {
            cct_URL = GZOP_MGL_DEDICATED_URL;
            cctToolbarColor = context.getResources().getColor(R.color.darker_blue_app_theme);
        }

//        private static Handler disconnectHandler = new Handler(new Handler.Callback() {
//        CustomTabsCallback customTabsCallback = new CustomTabsCallback(new CustomTabsCallback().onNavigationEvent());

        CustomTabColorSchemeParams setCCTBarColors = new CustomTabColorSchemeParams.Builder()
                .setToolbarColor(cctToolbarColor)
                .build();

        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(setCCTBarColors)
                .build();
        customTabsIntent.launchUrl(context, Uri.parse(cct_URL));
    }

    private void showTimerBsDialog(int QU1_PC1_GZ3_MGL3, int timerDialog_drawable_int,
                                   String timerDialog_dedi_title, int timerDialog_dedi_title_color) {
        BottomSheetDialog timerBsDialog = new BottomSheetDialog(getActivity(), R.style.bottomSheetDialog);
        timerBsDialog.setContentView(R.layout.layout_bsdialog_game_quiz_url_timer);
        timerBsDialog.getDismissWithAnimation();
        timerBsDialog.setCancelable(false);
        timerBsDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timerBsDialog.show();

        FrameLayout dedURL_design_frame = timerBsDialog.findViewById(R.id.dedicated_url_design_frame);

        FrameLayout timer_stop_btn = timerBsDialog.findViewById(R.id.common_url_timer_stop_btn);
        TextView timer_stop_text = timerBsDialog.findViewById(R.id.common_url_timer_stop_text);
        FrameLayout timer_continue_btn = timerBsDialog.findViewById(R.id.common_url_timer_continue_btn);
        TextView timer_continue_text = timerBsDialog.findViewById(R.id.common_url_timer_continue_text);

        ImageView timer_bdDialog_icon = timerBsDialog.findViewById(R.id.timer_bsDialog_icon);
        TextView timer_bdDialog_title = timerBsDialog.findViewById(R.id.timer_bsDialog_title);
        TextView timer_text = timerBsDialog.findViewById(R.id.dedicated_url_timer_tv);

        /** Common for all **/
        timer_text.startAnimation(blinking);
        timer_text.setText(new StringBuffer()
                .append(COUNTDOWN_TIME_IN_MINUTES)
                .append(" ")
                .append(context.getString(R.string.common_timer_bsdialog_timer_min_text))
                .append(" 00 ")
                .append(context.getString(R.string.common_timer_bsdialog_timer_sec_text)).toString());

        if (QU1_PC1_GZ3_MGL3 == 1) {
            mTimeLeft_in_millis = QUIZ_PRED_COUNTDOWN_TIME_IN_MILLIS;
            timer_bdDialog_icon.setImageResource(timerDialog_drawable_int);
            timer_bdDialog_title.setText(timerDialog_dedi_title);
            timer_bdDialog_title.setTextColor(timerDialog_dedi_title_color);
            timer_text.setTextColor(timerDialog_dedi_title_color);
        }

        if (QU1_PC1_GZ3_MGL3 == 3) {
            mTimeLeft_in_millis = GAME_COUNTDOWN_TIME_IN_MILLIS;
            timer_bdDialog_icon.setImageResource(timerDialog_drawable_int);
            timer_bdDialog_title.setText(timerDialog_dedi_title);
            timer_bdDialog_title.setTextColor(timerDialog_dedi_title_color);
            timer_text.setTextColor(timerDialog_dedi_title_color);
        }

        timer_stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer_stop_text.startAnimation(scaleDown);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        timerBsDialog.dismiss();
                        resetCommonTimer(QU1_PC1_GZ3_MGL3);
                    }
                }, 100);
            }
        });

        timer_continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimerRunning = true;
                timer_continue_text.startAnimation(scaleDown);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        openCCT_URL(QU1_PC1_GZ3_MGL3);

                        startCommonTimer(v, QU1_PC1_GZ3_MGL3, timerBsDialog, timer_text);
                    }
                }, 100);
            }
        });
    }

    private void startCommonTimer(View v, int QU1_PC1_GZ3_MGL3, BottomSheetDialog gamezopBsDialog, TextView gz_timer_text) {
        mCountDownTimer = new CountDownTimer(mTimeLeft_in_millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeft_in_millis = millisUntilFinished;
                int seconds = (int) (mTimeLeft_in_millis / 1000) % 60;
                int minutes = (int) (mTimeLeft_in_millis / 1000) / 60;

//                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
//                gz_timer_text.setText(timeLeftFormatted);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gz_timer_text.setText(new StringBuffer()
                                .append(minutes).append(" ")
                                .append(context.getString(R.string.common_timer_bsdialog_timer_min_text))
                                .append(" ")
                                .append(seconds).append(" ")
                                .append(context.getString(R.string.common_timer_bsdialog_timer_sec_text)).toString());
                    }
                }, 2000);
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                resetCommonTimer(QU1_PC1_GZ3_MGL3);
                gamezopBsDialog.dismiss();
                showAddRewardDialog(v, QU1_PC1_GZ3_MGL3);
            }
        }.start();
    }

    private void pauseCommonTimer() {
        if (mTimerRunning) {
            mCountDownTimer.cancel();
            mTimerRunning = false;
        }
    }

    private void resetCommonTimer(int QU1_PC1_GZ3_MGL3) {
        if (QU1_PC1_GZ3_MGL3 == 1) {
            mTimeLeft_in_millis = QUIZ_PRED_COUNTDOWN_TIME_IN_MILLIS;
        }
        if (QU1_PC1_GZ3_MGL3 == 3) {
            mTimeLeft_in_millis = GAME_COUNTDOWN_TIME_IN_MILLIS;
        }
    }

    private void showAddRewardDialog(View rootView1, int QU1_PC1_GZ3_MGL3) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        rootView1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog_add_reward_game_home,
                (ConstraintLayout) getActivity().findViewById(R.id.constraint_dialog_add_reward_game_home));
        builder.setView(rootView1);
        builder.setCancelable(false);

        AlertDialog dialogAddReward = builder.create();
        dialogAddReward.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogAddReward.show();

        ImageView dialogRewardIcon = rootView1.findViewById(R.id.dialog_add_reward_icon);
        TextView dialogRewardMsg = rootView1.findViewById(R.id.dialog_add_reward_msg);
//        FrameLayout dialogX2Btn = rootView1.findViewById(R.id.dialog_add_reward_btn);
        ImageView dialogCloseBtnIV = rootView1.findViewById(R.id.dialog_add_reward_close_btn);
//        TextView dialogCloseBtnTimer = rootView1.findViewById(R.id.dialog_add_reward_close_timer_tv);

//        TapdaqNativeLargeLayout nativeAdLayout = dialogAddReward.findViewById(R.id.native_ad_container);
//        nativeAdLayout.setVisibility(View.GONE);

        dialogRewardIcon.setImageResource(R.drawable.ic_coin_dollar);
        if (QU1_PC1_GZ3_MGL3 == 1) {
            dialogRewardMsg.setText(QUIZ_PRED_AMOUNT_OF_REWARD_TEXT);
        }
        if (QU1_PC1_GZ3_MGL3 == 3) {
            dialogRewardMsg.setText(GAME_AMOUNT_OF_REWARD_TEXT);
        }
//        dialogX2Btn.setVisibility(View.GONE);
        dialogCloseBtnIV.setVisibility(View.VISIBLE);
//        dialogCloseBtnTimer.setVisibility(View.GONE);

        addTokenInSQL(QU1_PC1_GZ3_MGL3);

        dialogCloseBtnIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                addTokenInSQL(QU1_PC1_GZ3_MGL3);
                updateUICoinsData();
//                showInterstitialFrom(); // calling showAd() from Main activity
                ((MainActivity)getActivity()).showMainAct_interstitialFrom();
                dialogAddReward.dismiss();
            }
        });
    }

    private void addTokenInSQL(int QU1_PC1_GZ3_MGL3) {
        if (QU1_PC1_GZ3_MGL3 == 1) {
            firebaseDataService.updateUserCoin(true, binding.gameOfferUserTotalCoins, QUIZ_PRED_AMOUNT_OF_REWARD);
        }
        if (QU1_PC1_GZ3_MGL3 == 3) {
            firebaseDataService.updateUserCoin(true, binding.gameOfferUserTotalCoins, GAME_AMOUNT_OF_REWARD);
        }

//        AllGameInOne allGameInOne = UserContext.getAllGameInOneMapByGameId(sqlTotalCoinsCOL);
//
//        long coinsBeforeReward = Long.parseLong(allGameInOne.getCoins());
//        long coinsAfterReward = coinsBeforeReward;
//        if (QU1_PC1_GZ3_MGL3 == 1) {
//            coinsAfterReward = coinsBeforeReward + QUIZ_PRED_AMOUNT_OF_REWARD;
//        }
//        if (QU1_PC1_GZ3_MGL3 == 3) {
//            coinsAfterReward = coinsBeforeReward + GAME_AMOUNT_OF_REWARD;
//        }
//
//        allGameInOne.setCoins(String.valueOf(coinsAfterReward));
//        dbHelper.updateFreeAdGameChanceAndCoinsData(allGameInOne);
    }


    private void showProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog_progress_bar_white_short,
                (ConstraintLayout) getActivity().findViewById(R.id.constraint_dialog_progress));
        builder.setView(rootView1);
        builder.setCancelable(false);
        progressDialog = builder.create();
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
    }

    /**
     * Intertitial Ad Code
     **/
//    private void loadInterstitialFrom() {
//        if (isTapdaqInterstitialEnabled || isAdmobInterstitialEnabled) {
//            if (isTapdaqIntersVideoReady() || admobInterstitial != null) {
//                // Do nothing
//                // Do not load ad again
//            } else {
//                if (isTapdaqInterstitialEnabled) {
//                    loadTapdaqIntersVideo();
//                }
//
//                if (isAdmobInterstitialEnabled) {
//                    loadAdmobInterstitial();
//                }
//            }
//        }
//    }
//
//    private void showInterstitialFrom() {
//        if (isTapdaqInterstitialEnabled || isAdmobInterstitialEnabled) {
//            if (isTapdaqIntersVideoReady()) {
//                showTapdaqIntersVideo();
//            }
//
//            if (admobInterstitial != null) {
//                showAdmobInterstitial();
//            }
//        }
//    }
//
//    /**
//     * Tapdaq Intertitial
//     **/
//
//    private void loadTapdaqIntersVideo() {
////        showProgressDialog();
//        Tapdaq.getInstance().loadVideo(getActivity(), TAPDAQ_INTERSTITIAL_AD_UNIT_ID, new TapdaqInterstitialVideoListener());
//    }
//
//    private boolean isTapdaqIntersVideoReady() {
//        boolean isReady = Tapdaq.getInstance().isVideoReady(getActivity(), TAPDAQ_INTERSTITIAL_AD_UNIT_ID);
//        return isReady;
//    }
//
//    private class TapdaqInterstitialVideoListener extends TMAdListener {
//        @Override
//        public void didLoad() {
//            super.didLoad();
////            progressDialog.dismiss();
//        }
//
//        @Override
//        public void didFailToLoad(TMAdError error) {
//            super.didFailToLoad(error);
////            progressDialog.dismiss();
//        }
//
//        @Override
//        public void didClose() {
//            loadTapdaqIntersVideo();
//            super.didClose();
//        }
//    }
//
//    private void showTapdaqIntersVideo() {
//        Tapdaq.getInstance().showVideo(getActivity(), TAPDAQ_INTERSTITIAL_AD_UNIT_ID, new TapdaqInterstitialVideoListener());
//    }
//
//    /**
//     * Admob Interstitial
//     **/
//
//    private void loadAdmobInterstitial() {
////        showProgressDialog();
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//        InterstitialAd.load(context, ADMOB_INTERSTITIAL_AD_UNIT_ID, adRequest, new InterstitialAdLoadCallback() {
//            @Override
//            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
//                admobInterstitial = interstitialAd;
////                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                admobInterstitial = null;
////                Log.e(TAG, "Admob Ad Failed to LOAD" + loadAdError);
////                progressDialog.dismiss();
//            }
//        });
//    }
//
//    private void showAdmobInterstitial() {
//        if (admobInterstitial != null) {
//            admobInterstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
//                @Override
//                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
//                    admobInterstitial = null;
//                }
//
//                @Override
//                public void onAdShowedFullScreenContent() {
//                }
//
//                @Override
//                public void onAdDismissedFullScreenContent() {
//                    admobInterstitial = null;
//                    loadAdmobInterstitial();
//                }
//            });
//            admobInterstitial.show(getActivity());
//        }
//    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUICoinsData();
        pauseCommonTimer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}