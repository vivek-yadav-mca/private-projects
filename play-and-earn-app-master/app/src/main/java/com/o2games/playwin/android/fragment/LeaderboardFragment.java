package com.o2games.playwin.android.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.o2games.playwin.android.Constants;
import com.o2games.playwin.android.FirebaseDataService;
import com.o2games.playwin.android.Game;
import com.o2games.playwin.android.R;
import com.o2games.playwin.android.Utils;
import com.o2games.playwin.android.activity.MainActivity;
import com.o2games.playwin.android.adapter.LeaderboardAdapter;
import com.o2games.playwin.android.databinding.FragmentLeaderboardBinding;
import com.o2games.playwin.android.model.LeaderboardModel;
import com.o2games.playwin.android.userData.UserContext;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class LeaderboardFragment extends Fragment {

    private static LeaderboardFragment instance;
    private static final String TAG = LeaderboardFragment.class.getName();
    private MainActivity mainActivity;
    private FirebaseDataService firebaseDataService;
    private static boolean isDailyWinner;
    private static boolean isWeeklyWinner;
    public static final String sqlTotal_CashCoinsCOL = Game.TOTAL_CASH_COINS.getId();
    FragmentLeaderboardBinding binding;
    private SwipeRefreshLayout refreshLayout;

    private DatabaseReference databaseRef;
    private FirebaseAuth firebaseAuth;
    private List<LeaderboardModel> leaderboard = new ArrayList<>();
    long numberOfChildren_in_leaderboard;
    Context context;
    Animation rotate_clockwise;
    Animation blinking;
    private CountDownTimer countDownTimer;

    double decimal_wallet_bal;
    String sqlTotal_Coins;
    long userTotal_COIN;
    //    String sqlTotal_Cash;
//    long userTotal_CASH;
    int numbers = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static LeaderboardFragment GetInstance() {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        View rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        instance = this;
        context = getActivity();
        mainActivity = MainActivity.GetInstance();
        firebaseDataService = new FirebaseDataService(getActivity());
        rotate_clockwise = AnimationUtils.loadAnimation(context, R.anim.anim_rotate_clockwise);
        blinking = AnimationUtils.loadAnimation(context, R.anim.anim_blinking_repeat);

        databaseRef = FirebaseDatabase.getInstance().getReference();
        firebaseAuth =FirebaseAuth.getInstance();
        isDailyWinner = UserContext.getIsDailyWinnerLeaderboard();
        isWeeklyWinner = UserContext.getIsWeeklyWinnerLeaderboard();

        getWalletBalance();
        startLeaderboardResultTimer();

        LeaderboardAdapter adapter = new LeaderboardAdapter(context, leaderboard, numberOfChildren_in_leaderboard, binding.leaderboardUserRanking);
        databaseRef
                .child(Constants.LEADERBOARD_TABLE)
//                .child(Constants.WALLET_BALANCE)
                .orderByChild(Constants.LEADERBOARD_ORDERING_PARENT)   //userWalletBalance
//                .orderByChild("userTotalCoins")   //userWalletBalance
                .limitToLast(100)  // limiting not giving rank for far players
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        numberOfChildren_in_leaderboard = snapshot.getChildrenCount();
//                        Log.e("firebase children count", "" + numberOfChildren_in_leaderboard);
//                        Log.d("firebase children count", "" + numberOfChildren_in_leaderboard);
                        leaderboard.clear();
                        for (DataSnapshot firebaseData : snapshot.getChildren()) {
                            LeaderboardModel modelData = firebaseData.getValue(LeaderboardModel.class);
                            leaderboard.add(modelData);
                        }
                        Collections.reverse(leaderboard);
                        adapter.notifyDataSetChanged();

                        if (UserContext.getAutoCleanLeaderboard()) {
                            if (numberOfChildren_in_leaderboard >= 10) {
                                // Show leaderboard
                                binding.leaderboardRv.setVisibility(View.VISIBLE);
                                binding.leaderboardCleaningRefreshingMsg.setVisibility(View.GONE);
                            } else {
                                // Hide leaderboard
                                binding.leaderboardRv.setVisibility(View.GONE);
                                binding.leaderboardCleaningRefreshingMsg.setVisibility(View.VISIBLE);
                            }
                        } else {
                            binding.leaderboardRv.setVisibility(View.VISIBLE);
                            binding.leaderboardCleaningRefreshingMsg.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

//        leaderboard.add(new LeaderboardModel("dwd", "fghjkfghjkdfghjkdfghjkddfghjk,l.fghjklfghjkdfghj", "", "dfghj"));
        binding.leaderboardRv.setLayoutManager(new GridLayoutManager(getContext(), 1));
        binding.leaderboardRv.setAdapter(adapter);

        binding.refreshLeaderboardButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.refreshLeaderboardButtonCV.startAnimation(rotate_clockwise);
                refreshLeaderboardData();
            }
        });

//        binding.infoLeaderboardButtonCV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mainActivity.showInfoHowToWin_bsDialog();
//            }
//        });

        /** Setting User Self data, Apart from LeaderBoard Data **/
        RequestOptions loadImage = new RequestOptions()
                .centerCrop()
                .circleCrop()  //to crop image in circle view
                .placeholder(R.drawable.user_color)
                .error(R.drawable.user_color);

        Glide.with(getActivity())
                .load(UserContext.getLoggedInUser().getUserPhotoUrl())
                .apply(loadImage)
                .into(binding.leaderboardUserPhoto);

        binding.leaderboardUserName.setText(UserContext.getLoggedInUser().getUserName());
        /**  Updating wallet balance in getWalletBalance() method  **/


        return binding.getRoot();
    }

    public void setLeaderboardTop3Ranker(LeaderboardModel model, int position) {
        RequestOptions loadImage = new RequestOptions()
                .centerCrop()
                .circleCrop()  //to crop image in circle view
                .placeholder(R.drawable.user_color)
                .error(R.drawable.user_color);

        try {
            if (position == 0) {
                Glide.with(context)
                        .load(model.getUserPhotoUrl())
                        .apply(loadImage)
                        .into(binding.leaderboardRank1PlayerImageV);

                binding.leaderboardRank1PlayerNameTextV.setText(model.getUserName());
                binding.leaderboardRank1PlayerCoinBalanceTextV.setText(String.valueOf(model.getUserTotalCOIN()));

//            marqueeText(binding.leaderboardRank1PlayerNameTextV);
            }
            if (position == 1) {
                Glide.with(context)
                        .load(model.getUserPhotoUrl())
                        .apply(loadImage)
                        .into(binding.leaderboardRank2PlayerImageV);

                binding.leaderboardRank2PlayerNameTextV.setText(model.getUserName());
                binding.leaderboardRank2PlayerCoinBalanceTextV.setText(String.valueOf(model.getUserTotalCOIN()));

//            marqueeText(binding.leaderboardRank2PlayerNameTextV);
            }
            if (position == 2) {
                Glide.with(context)
                        .load(model.getUserPhotoUrl())
                        .apply(loadImage)
                        .into(binding.leaderboardRank3PlayerImageV);

                binding.leaderboardRank3PlayerNameTextV.setText(model.getUserName());
                binding.leaderboardRank3PlayerCoinBalanceTextV.setText(String.valueOf(model.getUserTotalCOIN()));

//            marqueeText(binding.leaderboardRank3PlayerNameTextV);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void marqueeText(TextView textView) {
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSingleLine(true);
        textView.setMarqueeRepeatLimit(10);
        textView.setSelected(true);
    }

    private void startLeaderboardResultTimer() {
        binding.textView1.setSelected(true);
        binding.leaderboardTimeDividerTv1.startAnimation(blinking);
        binding.leaderboardTimeDividerTv2.startAnimation(blinking);
        binding.leaderboardTimeDividerTv3.startAnimation(blinking);

        Date dateTime = new Date(System.currentTimeMillis());
        long currentTime = dateTime.getTime();

        long resultAnnouncementTime = UserContext.getNextWinnerAnnouncementTime();
        long announcementTimeLeft = resultAnnouncementTime - currentTime;

        countDownTimer = new CountDownTimer(announcementTimeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int sec = (int) millisUntilFinished / 1000 % 60;
                String seconds = sec <= 9 ? "0" + sec : String.valueOf(sec);
                int min = (int) millisUntilFinished / (1000 * 60) % 60;
                String minutes = min <= 9 ? "0" + min : String.valueOf(min);
                int hr = (int) millisUntilFinished / (1000 * 60 * 60) % 24;
                String hours = hr <= 9 ? "0" + hr : String.valueOf(hr);
                int d = (int) millisUntilFinished / (1000 * 60 * 60 * 24) % 30; // %30 mean not more than 30 days
                String days = d <= 9 ? "0" + d : String.valueOf(d);

                if (isDailyWinner) {
                    binding.leaderboardTimeDays.setVisibility(View.GONE);
                    binding.leaderboardTimeDividerTv1.setVisibility(View.GONE);
                    binding.leaderboardTimeHours.setText(hours + " h");
                    binding.leaderboardTimeMinutes.setText(minutes + " m");
                    binding.leaderboardTimeSeconds.setText(seconds + " s");
                }
                if (isWeeklyWinner) {
                    binding.leaderboardTimeDays.setVisibility(View.VISIBLE);
                    binding.leaderboardTimeDividerTv1.setVisibility(View.VISIBLE);
                    binding.leaderboardTimeDays.setText(d + " d");
                    binding.leaderboardTimeHours.setText(hours + " h");
                    binding.leaderboardTimeMinutes.setText(minutes + " m");
                    binding.leaderboardTimeSeconds.setText(seconds + " s");
                }
            }
            @Override
            public void onFinish() {

                if (isDailyWinner) {
                    binding.leaderboardTimeDays.setVisibility(View.GONE);
                    binding.leaderboardTimeDividerTv1.setVisibility(View.GONE);
                    binding.leaderboardTimeHours.setText("00 h");
                    binding.leaderboardTimeMinutes.setText("00 m");
                    binding.leaderboardTimeSeconds.setText("00 s");
                }
                if (isWeeklyWinner) {
                    binding.leaderboardTimeDays.setVisibility(View.VISIBLE);
                    binding.leaderboardTimeDividerTv1.setVisibility(View.VISIBLE);
                    binding.leaderboardTimeDays.setText("0 d");
                    binding.leaderboardTimeHours.setText("00 h");
                    binding.leaderboardTimeMinutes.setText("00 m");
                    binding.leaderboardTimeSeconds.setText("00 s");
                }
            }
        }.start();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        refreshLayout = view.findViewById(R.id.leaderboard_swipe_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLeaderboardData();
                refreshLayout.setRefreshing(false);
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void getWalletBalance() {
        sqlTotal_Coins = firebaseDataService.getCoinBalance();
//        sqlTotal_Coins = UserContext.getAllGameInOneMapByGameId(sqlTotal_CashCoinsCOL).getCoins();
        binding.leaderboardUserTotalCoins.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_bg_dollar_coin_stack_32px, 0, 0, 0);
        binding.leaderboardUserTotalCoins.setText(sqlTotal_Coins);

//        sqlTotal_Cash = firebaseDataService.getUserWalletBalance();
////        sqlTotal_Cash = UserContext.getAllGameInOneMapByGameId(sqlTotal_CashCoinsCOL).getCash();
//        binding.leaderboardUserTotalCash.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_bg_dollar_cash_stack_32px, 0, 0, 0);
//        binding.leaderboardUserTotalCash.setText(sqlTotal_Cash);

        userTotal_COIN = Long.parseLong(sqlTotal_Coins);
//        userTotal_CASH = Long.parseLong(sqlTotal_Cash);

        if (UserContext.getCheckFraudUser()) {
            if (userTotal_COIN > UserContext.getUserCoinBalance()) {
                databaseRef
                        .child(Constants.BLOCKED_USER_TABLE)
                        .child(firebaseAuth.getCurrentUser().getUid())
                        .setValue(true)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(@NonNull @NotNull Void unused) {
                                signOut();
                                showAccountBlockedDialog();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                // If this process failed that mean
                                // user is not able to add blocking info,
                                // because user write, read is denied by rtdb
                                signOut();
                                showAccountBlockedDialog();
//                        Log.e("User write option - ", "denied by rtb - " + e.getMessage());
                            }
                        });
            }
        }
    }

    private void refreshLeaderboardData() {
        getWalletBalance();

        String userId = UserContext.getLoggedInUser().getId();
        String userName = UserContext.getLoggedInUser().getUserName();
        String userPhotoUrl = UserContext.getLoggedInUser().getUserPhotoUrl();

//        userTotal_COIN = Long.parseLong(sqlTotal_Coins);
//        userTotal_CASH = Long.parseLong(sqlTotal_Cash);

//        Map<String, AllGameInOne> allGameInOneMap = UserContext.getAllGameInOneMap();
//        if (allGameInOneMap != null) {
//            userTotal_COIN = allGameInOneMap.get(sqlTotal_CashCoinsCOL).getCoins();
//            userTotal_CASH = allGameInOneMap.get(sqlTotal_CashCoinsCOL).getCash();
//        }

        String authUid = UserContext.getLoggedInUser().getAuthUid();
        LeaderboardModel modelData = new LeaderboardModel(userId, authUid, userName, userPhotoUrl, userTotal_COIN);
        databaseRef
                .child(Constants.LEADERBOARD_TABLE)
//                .child(Constants.WALLET_BALANCE)
                .child(UserContext.getLoggedInUser().getAuthUid())
                .setValue(modelData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull @NotNull Void unused) {
//                        Log.e("LeaderBoard_-_Data", "Success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
//                        Log.e("LeaderBoard_-_Data", "Failure" + e);
                    }
                });

//        UserWalletDataModel userWalletDataModel = new UserWalletDataModel(userId, authUid, (int) userTotal_CASH);
//
//        databaseRef.child(Constants.USER_WALLET)
//                .child(UserContext.getLoggedInUser().getAuthUid())
//                .setValue(userWalletDataModel)
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull @NotNull Exception e) {
//                        Log.e(TAG, "Error while adding user_wallet: " + e.getMessage());
//                    }
//                });
    }

    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseAuth.signOut();
                    }
                });
    }

    private void showAccountBlockedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View getLayout_rootView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog_blocked_user,
                (ConstraintLayout) getActivity().findViewById(R.id.constraint_dialog_blocked_user));
        builder.setView(getLayout_rootView);
        builder.setCancelable(false);

        AlertDialog userBlockedDialog = builder.create();
        userBlockedDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        userBlockedDialog.show();

        FrameLayout closeBtn = userBlockedDialog.findViewById(R.id.dialog_blocked_user_btn_frame);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finishAffinity();
                userBlockedDialog.dismiss();
            }
        });
    }


    @Override
    public void onResume() {
        getWalletBalance();
        startLeaderboardResultTimer();
        binding.leaderNoticeWeeklyContestSoonText.startAnimation(blinking);
        super.onResume();
    }
}