package com.o2games.playwin.android.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.o2games.playwin.android.Constants;
import com.o2games.playwin.android.FirebaseDataService;
import com.o2games.playwin.android.R;
import com.o2games.playwin.android.fragment.LeaderboardFragment;
import com.o2games.playwin.android.model.LeaderboardModel;
import com.o2games.playwin.android.model.SpUserModel;
import com.o2games.playwin.android.model.User;
import com.o2games.playwin.android.userData.UserContext;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    Context context;
    List<LeaderboardModel> leaderboardModels;
    long numberOfChildren_in_leaderboard;
    private TextView userRank;

    public LeaderboardAdapter(Context context, List<LeaderboardModel> leaderboardModels, long getChildrenCount, TextView userRank) {
        this.context = context;
        this.leaderboardModels = leaderboardModels;
        this.numberOfChildren_in_leaderboard = getChildrenCount;
        this.userRank = userRank;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_rv_leaderboard, null);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardAdapter.LeaderboardViewHolder holder, int position) {
        LeaderboardModel model = leaderboardModels.get(position);
        String rank = position <= 8 ? "0" + String.valueOf(position+1) : String.valueOf(position+1);

        LeaderboardFragment leaderboardFragment = LeaderboardFragment.GetInstance();
        leaderboardFragment.setLeaderboardTop3Ranker(model, position);

        SpUserModel spUserModel = getUserData_sPref();
        String UserContext_userId = spUserModel.getUserId();

        int bg_darker_blue_app_theme = Color.parseColor("#091F32");
        int bg_white = Color.parseColor("#FFFFFF");
        int bg_transparent = Color.parseColor("#00000000");

        if (model.getUserId().equals(UserContext_userId)) {
            FirebaseDataService firebaseDataService = new FirebaseDataService(context);
            firebaseDataService.updateSqlCoinData(model.getUserTotalCOIN());

            userRank.setText(new StringBuilder()
                    .append(context.getString(R.string.rv_leaderboard_rank))
                    .append(": ").append(rank).toString());
            holder.playerCardFrameLayout.setBackgroundResource(R.drawable.bg_round_all_darker_blue_app_theme);
            holder.userName.setTextColor(bg_white);
            holder.userRank.setTextColor(bg_white);
            holder.userTotal_COIN.setTextColor(bg_white);
//            holder.userTotal_CASH.setTextColor(bg_white);
        } else {
            holder.playerCardFrameLayout.setBackgroundColor(bg_transparent);
            holder.userName.setTextColor(bg_darker_blue_app_theme);
            holder.userRank.setTextColor(bg_darker_blue_app_theme);
            holder.userTotal_COIN.setTextColor(bg_darker_blue_app_theme);
//            holder.userTotal_CASH.setTextColor(bg_darker_blue_app_theme);
        }

        holder.userName.setText(model.getUserName());
//        holder.userName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//        holder.userName.setSingleLine(true);
//        holder.userName.setMarqueeRepeatLimit(10);
//        holder.userName.setSelected(true);

        RequestOptions loadImage = new RequestOptions()
                .centerCrop()
                .circleCrop()  //to crop image in circle view
                .placeholder(R.drawable.user_color)
                .error(R.drawable.user_color);

        Glide.with(context)
                .load(model.getUserPhotoUrl())
                .apply(loadImage)
                .into(holder.userProfilePhoto);

        if (position <= 2) {
            holder.playerCardFrameLayout.setVisibility(View.GONE);
        }
        if (position >= 3) {
            holder.top3RankerFrame.setVisibility(View.GONE);
            holder.userRank.setText(rank);
        } else {
            holder.userRank.setText("");
        }
        if (position >= 100) {
            holder.playerCardFrameLayout.setVisibility(View.GONE);
        }

        holder.userTotal_COIN.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_bg_dollar_coin_stack_24px, 0 ,0, 0);
        holder.userTotal_COIN.setText(String.valueOf(model.getUserTotalCOIN()));

    }

    @Override
    public int getItemCount() {
        return leaderboardModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        FrameLayout playerCardFrameLayout;

        ImageView userProfilePhoto;
        //        ImageView rankMedal;
        TextView userName;
        TextView userRank;
        TextView userTotal_COIN;
//        TextView userCoins;
//        TextView userTotal_CASH;

        FrameLayout top3RankerFrame;

        ImageView leaderboardRank1PlayerImageV;
        TextView leaderboardRank1PlayerNameTextV;
        TextView leaderboardRank1PlayerCoinBalanceTextV;

        ImageView leaderboardRank2PlayerImageV;
        TextView leaderboardRank2PlayerNameTextV;
        TextView leaderboardRank2PlayerCoinBalanceTextV;

        ImageView leaderboardRank3PlayerImageV;
        TextView leaderboardRank3PlayerNameTextV;
        TextView leaderboardRank3PlayerCoinBalanceTextV;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            playerCardFrameLayout = itemView.findViewById(R.id.leaderboard_player_cardView);

            userProfilePhoto = itemView.findViewById(R.id.leaderboard_userPhoto);
//            rankMedal = itemView.findViewById(R.id.leaderboard_rank_medals);
            userName = itemView.findViewById(R.id.leaderboard_userName);
            userRank = itemView.findViewById(R.id.leaderboard_userRanking);
            userTotal_COIN = itemView.findViewById(R.id.leaderboard_userTotalCoins);
//            userCoins = itemView.findViewById(R.id.leaderboard_userCoins);
//            userTotal_CASH = itemView.findViewById(R.id.leaderboard_userTotalCash);

            top3RankerFrame = itemView.findViewById(R.id.leaderboard_top3_player_frameL);

            leaderboardRank1PlayerImageV = itemView.findViewById(R.id.leaderboard_rank_1_player_imageV);
            leaderboardRank1PlayerNameTextV = itemView.findViewById(R.id.leaderboard_rank_1_player_name_textV);
            leaderboardRank1PlayerCoinBalanceTextV = itemView.findViewById(R.id.leaderboard_rank_1_player_coinBalance_textV);

            leaderboardRank2PlayerImageV = itemView.findViewById(R.id.leaderboard_rank_2_player_imageV);
            leaderboardRank2PlayerNameTextV = itemView.findViewById(R.id.leaderboard_rank_2_player_name_textV);
            leaderboardRank2PlayerCoinBalanceTextV = itemView.findViewById(R.id.leaderboard_rank_2_player_coinBalance_textV);

            leaderboardRank3PlayerImageV = itemView.findViewById(R.id.leaderboard_rank_3_player_imageV);
            leaderboardRank3PlayerNameTextV = itemView.findViewById(R.id.leaderboard_rank_3_player_name_textV);
            leaderboardRank3PlayerCoinBalanceTextV = itemView.findViewById(R.id.leaderboard_rank_3_player_coinBalance_textV);
        }
    }

    private SpUserModel getUserData_sPref() {
        SharedPreferences readSPref_forUserData = context.getSharedPreferences(Constants.SHARED_PREF_COMMON, Context.MODE_PRIVATE);

        String userId = readSPref_forUserData.getString(Constants.SP_USER_ID, UserContext.getLoggedInUser().getId());
        String userName = readSPref_forUserData.getString(Constants.SP_USER_NAME, UserContext.getLoggedInUser().getUserName());
        String userEmail = readSPref_forUserData.getString(Constants.SP_USER_EMAIL, UserContext.getLoggedInUser().getUserEmail());
        String userGoogleId = readSPref_forUserData.getString(Constants.SP_USER_GOOGLE_ID, UserContext.getLoggedInUser().getUserGoogleAccountId());
        String userPhotoUrl = readSPref_forUserData.getString(Constants.SP_USER_PHOTO_URL, UserContext.getLoggedInUser().getUserPhotoUrl());
        String userAdverId = readSPref_forUserData.getString(Constants.SP_USER_ADVER_ID, UserContext.getUserAdverId());
        String userAuthId = readSPref_forUserData.getString(Constants.SP_USER_AUTH_UID, UserContext.getLoggedInUser().getAuthUid());

        User loggedInUser = new User(userId, userName, userEmail, userGoogleId, userPhotoUrl, userAdverId, userAuthId);
        UserContext.setLoggedInUser(loggedInUser);

        SpUserModel spUserModel = new SpUserModel(userId, userName, userEmail, userGoogleId, userPhotoUrl, userAuthId, userAdverId);
        return spUserModel;
    }

}