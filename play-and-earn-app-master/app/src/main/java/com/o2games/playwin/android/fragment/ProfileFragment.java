package com.o2games.playwin.android.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.o2games.playwin.android.BuildConfig;
import com.o2games.playwin.android.Constants;
import com.o2games.playwin.android.Game;
import com.o2games.playwin.android.LanguageManager;
import com.o2games.playwin.android.R;
import com.o2games.playwin.android.activity.MainActivity;
import com.o2games.playwin.android.activity.SignupActivity;
import com.o2games.playwin.android.activity.SplashScreenActivity;
import com.o2games.playwin.android.databinding.FragmentProfileBinding;
import com.o2games.playwin.android.model.SpUserModel;
import com.o2games.playwin.android.sqlUserGameData.DBHelper;
import com.o2games.playwin.android.userData.UserContext;

import java.util.Calendar;
import java.util.Date;

public class ProfileFragment extends Fragment {

    View rootView;
    Context context;
    private static final String TAG = ProfileFragment.class.getName();
    public static final String sqlTotal_CashCoinsCOL = Game.TOTAL_CASH_COINS.getId();
    private String app_name;
    private String app_gplay_store_url;
    private String app_privacy_policy_url;

    FragmentProfileBinding binding;
    DatabaseReference databaseRef;
    FirebaseAuth firebaseAuth;
    FirebaseRemoteConfig remoteConfig;

    private LanguageManager languageManager;
    private Spinner lang_spinner;
    private ArrayAdapter<CharSequence> adapter;

    private SharedPreferences writeSPref;
    private SharedPreferences.Editor editorSPref;
    private SharedPreferences readSPref;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        context = getActivity();
        databaseRef = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        writeSPref = getActivity().getSharedPreferences(Constants.SHARED_PREF_COMMON, Context.MODE_PRIVATE);
        editorSPref = writeSPref.edit();
        readSPref = getActivity().getSharedPreferences(Constants.SHARED_PREF_COMMON, Context.MODE_PRIVATE);

        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings);

        app_name = context.getString(R.string.app_name);
        String app_version = BuildConfig.VERSION_NAME;
        binding.profileAppVersion.setText(new StringBuilder().append(getString(R.string.pro_frag_app_version)).append(" ").append(app_version).toString());

//        SpUserModel spUserModel = getUserData_sPref();
        String userName = UserContext.getLoggedInUser().getUserName();
        binding.profileUserName.setText(new StringBuilder().append(getString(R.string.pro_frag_hello_user)).append(", ").append(userName));
        binding.profileGameId.setText(UserContext.getLoggedInUser().getUserGoogleAccountId());
        binding.profileUserEmail.setSelected(true);
        binding.profileUserEmail.setText(UserContext.getLoggedInUser().getUserEmail());
        binding.profileUserEmail.setSelected(true);

        RequestOptions loadImageConfig = new RequestOptions()
                .centerCrop()
                .circleCrop()  //to crop image in circle view
                .placeholder(R.drawable.user_color)
                .error(R.drawable.user_color);

        Glide.with(getActivity())
                .load(UserContext.getLoggedInUser().getUserPhotoUrl())
                .apply(loadImageConfig)
                .into(binding.profileUserPhotoBox);

//        binding.profileJoinTelegramLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(telegram_channel_link)));
//            }
//        });

        remoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    String fbPageLink = remoteConfig.getString("facebook_page_link");
                    String instaPageLink = remoteConfig.getString("instagram_page_link");
                    String telegramLink = remoteConfig.getString("telegram_channel_link");

                        binding.proFragFacebookBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(fbPageLink)));
                            }
                        });
                        binding.proFragInstagramBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(instaPageLink)));
                            }
                        });
                        binding.proFragTelegramBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(telegramLink)));
                            }
                        });
                } else {
                    Log.e(TAG, "Exception while remote config is fetched.");
                }
            }
        });

        languageManager = new LanguageManager(getActivity());
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.spinner_language, R.layout.layout_spinner_custom);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.profileLanguageSpinner.setAdapter(adapter);
        binding.profileLanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = parent.getItemAtPosition(position).toString();
                if (selectedLanguage.equals("English")) {
                    languageManager.setLanguageSPref("en");
                    languageManager.updateLanguage("en");
//                    getActivity().overridePendingTransition(0, R.anim.anim_exit_to_right);
                    getActivity().finishAffinity();
//                    getActivity().overridePendingTransition(R.anim.anim_enter_from_left, 0);
                    startActivity(new Intent(getActivity(), SplashScreenActivity.class));
                }
                if (selectedLanguage.equals("Español")) {
                    languageManager.setLanguageSPref("es");
                    languageManager.updateLanguage("es");
                    getActivity().finishAffinity();
                    startActivity(new Intent(getActivity(), SplashScreenActivity.class));
                }
                if (selectedLanguage.equals("Português")) {
                    languageManager.setLanguageSPref("pt");
                    languageManager.updateLanguage("pt");
                    getActivity().finishAffinity();
                    startActivity(new Intent(getActivity(), SplashScreenActivity.class));
                }
                if (selectedLanguage.equals("Deutsch")) {
                    showCustomToast(getString(R.string.lang_spinner_toast_german_Deutsch_msg));
                }
//                if (selectedLanguage.equals("Pусский")) {
//                    showCustomToast(context.getString(R.string.lang_spinner_toast_russian_Pусский_msg));
//                }
                if (selectedLanguage.equals("Français")) {
                    showCustomToast(getString(R.string.lang_spinner_toast_french_Français_msg));
                }
                if (selectedLanguage.equals("Italiano")) {
                    showCustomToast(getString(R.string.lang_spinner_toast_italian_Italiano_msg));
                }
//                if (selectedLanguage.equals("Yкраїнський")) {
//                    showCustomToast(context.getString(R.string.lang_spinner_toast_ukrianian_Yкраїнський_msg));
//                }
                if (selectedLanguage.equals("Indonesia")) {
                    showCustomToast(getString(R.string.lang_spinner_toast_indonesian_Indonesia_msg));
                }
                if (selectedLanguage.equals("日本")) {
                    showCustomToast(getString(R.string.lang_spinner_toast_japanese_日本_msg));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.profileShareAppLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Play " + app_name + " & Earn Real Money");
                    String shareMessage = "\nDownload this exciting app which allow you to earn money in free time.\n\n";
                    shareMessage = shareMessage + app_gplay_store_url + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Choose an application to share this app"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });

        binding.profileRateAppLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingBsDialog();
            }
        });

        binding.profileContactUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.profile_frag_container, new ContactUsFragment(), "fragment_screen");
//                fragmentTransaction.commit();

//                showContactBsDialog();

                ((MainActivity) getActivity()).showMainAct_interstitialFrom();
                disableContactUsBtn();
                contactUs_SendEmail1();
            }
        });

        binding.profileDeleteUserFL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog(1);
            }
        });

        binding.profileLogoutFL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog(2);
            }
        });

        binding.profileTermsPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTermsPrivacy_bsDialog();
            }
        });


        return binding.getRoot();
    }

    private void showTermsPrivacy_bsDialog() {
        BottomSheetDialog default_bsDialog = new BottomSheetDialog(context, R.style.bottomSheetDialog);
        default_bsDialog.setContentView(R.layout.layout_bsdialog_default);
        default_bsDialog.getDismissWithAnimation();
        default_bsDialog.setCancelable(true);
        default_bsDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        default_bsDialog.show();

        TextView terms_of_service = default_bsDialog.findViewById(R.id.default_bsdialog_textView1);
        TextView privacy_policy = default_bsDialog.findViewById(R.id.default_bsdialog_textView2);

        terms_of_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String termUrl = getString(R.string.app_terms_of_service_url);
                openURL_in_CCT(termUrl);
            }
        });

        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String privacyUrl = getString(R.string.app_privacy_policy_url);
                openURL_in_CCT(privacyUrl);
            }
        });
    }

    private void openURL_in_CCT(String cctUrl) {
        int cctToolbarColor = getResources().getColor(R.color.darker_blue_app_theme);

        CustomTabColorSchemeParams setCCTBarColors = new CustomTabColorSchemeParams.Builder()
                .setToolbarColor(cctToolbarColor)
                .build();

        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(setCCTBarColors)
                .build();
        customTabsIntent.launchUrl(context, Uri.parse(cctUrl));
    }


    private void logoutDialog(int delete1_logout2) {
        BottomSheetDialog delete_logoutDialog = new BottomSheetDialog(getActivity(), R.style.bottomSheetDialog);
        delete_logoutDialog.setContentView(R.layout.layout_dialog_delete_logout);
        delete_logoutDialog.getDismissWithAnimation();
        delete_logoutDialog.setCancelable(true);
        delete_logoutDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        delete_logoutDialog.show();

        TextView dialog_title = delete_logoutDialog.findViewById(R.id.delete_logout_dialog_title);
        TextView dialog_msg = delete_logoutDialog.findViewById(R.id.delete_logout_dialog_msg);
        TextView delete_logout_btn_text = delete_logoutDialog.findViewById(R.id.delete_logout_dialog_button_text);

        FrameLayout cancelButn = delete_logoutDialog.findViewById(R.id.delete_logout_dialog_cancel_button);
        FrameLayout delete_logout_btn = delete_logoutDialog.findViewById(R.id.delete_logout_dialog_button);

        if (delete1_logout2 == 1) {
            dialog_title.setText(context.getString(R.string.pro_frag_delete_bsdialog_title));
            dialog_msg.setText(context.getString(R.string.pro_frag_delete_bsdialog_msg));
            delete_logout_btn_text.setText(context.getString(R.string.dialog_delete_button));
        }
        if (delete1_logout2 == 2) {
            dialog_title.setText(context.getString(R.string.pro_frag_logout_bsdialog_title));
            dialog_msg.setText(context.getString(R.string.pro_frag_logout_bsdialog_msg));
            delete_logout_btn_text.setText(context.getString(R.string.dialog_logout_button));
        }

        cancelButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_logoutDialog.dismiss();
            }
        });
        delete_logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delete1_logout2 == 1) {
                    delete_signOut(delete1_logout2, delete_logoutDialog);
                }
                if (delete1_logout2 == 2) {
                    delete_signOut(delete1_logout2, delete_logoutDialog);
                }
            }
        });

    }

    private void delete_signOut(int delete1_logout2, BottomSheetDialog logoutDialog) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().signOut();

                        if (delete1_logout2 == 1) {
                            DBHelper dbHelper = new DBHelper(context);
                            dbHelper.resetAllGameDataByValue(UserContext.getLoggedInUser().getId(), "0");
                            databaseRef
                                    .child(Constants.LEADERBOARD_TABLE)
                                    .child(UserContext.getLoggedInUser().getAuthUid())
                                    .removeValue();
                            databaseRef
                                    .child(Constants.USER_WALLET)
                                    .child(UserContext.getLoggedInUser().getAuthUid())
                                    .removeValue();
                            databaseRef
                                    .child(Constants.DELETED_ACCOUNT)
                                    .child(UserContext.getLoggedInUser().getId())
                                    .setValue(true);

                            showCustomToast(context.getString(R.string.pro_frag_delete_toast_msg));
                        }
                        if (delete1_logout2 == 2) {
                            showCustomToast(context.getString(R.string.pro_frag_logout_toast_msg));
                        }

                        logoutDialog.dismiss();
                        getActivity().finish();
                        startActivity(new Intent(getActivity(), SignupActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showCustomToast(String toastMsg) {
        LayoutInflater inflater = getLayoutInflater();
        rootView = inflater.inflate(R.layout.layout_toast_custom,
                (ConstraintLayout) rootView.findViewById(R.id.custom_toast_constraint));

        TextView toastText = rootView.findViewById(R.id.custom_toast_text);

        toastText.setText(toastMsg);

        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(rootView);

        toast.show();
    }

    private void showRatingBsDialog() {
//        ReviewManager manager = ReviewManagerFactory.create(context);
////        ReviewManager manager = new FakeReviewManager(this);
//
//        com.google.android.play.core.tasks.Task<ReviewInfo> request = manager.requestReviewFlow();
//        request.addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                // We can get the ReviewInfo object
//                ReviewInfo reviewInfo = task.getResult();
//                com.google.android.play.core.tasks.Task<Void> flow = manager.launchReviewFlow(getActivity(), reviewInfo);
//                flow.addOnCompleteListener(task1 -> {
//                    // The flow has finished. The API does not indicate whether the user
//                    // reviewed or not, or even whether the review dialog was shown. Thus, no
//                    // matter the result, we continue our app flow.
//                });
//            } else {
//                // There was some problem, log or handle the error code.
////                @ReviewErrorCode int reviewErrorCode = ((TaskException) task.getException()).getErrorCode();
//            }
//        });

        BottomSheetDialog ratingBsDialog = new BottomSheetDialog(getActivity(), R.style.bottomSheetDialog);
        ratingBsDialog.setContentView(R.layout.layout_bsdialog_rating_playstore);
        ratingBsDialog.getDismissWithAnimation();
//        ratingBsDialog.setCancelable(false);
//        ratingBsDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ratingBsDialog.show();

        RatingBar ratingBar = ratingBsDialog.findViewById(R.id.rating_dialog_ratingBar);
        CheckBox ratingCheckBox = ratingBsDialog.findViewById(R.id.rating_dialog_checkBox);
        FrameLayout closeBtn = ratingBsDialog.findViewById(R.id.upstox_dialog_ac_open_btn);

        ratingCheckBox.setVisibility(View.GONE);
        closeBtn.setVisibility(View.GONE);

        ratingBar.setRating(1);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(app_gplay_store_url)));
                        ratingBsDialog.dismiss();
                    }
                }, 500);
            }
        });
    }

    private void showContactBsDialog() {
        BottomSheetDialog contactBsDialog = new BottomSheetDialog(getActivity());
        contactBsDialog.setContentView(R.layout.layout_bsdialog_contact_us);
        contactBsDialog.getDismissWithAnimation();
        contactBsDialog.setCancelable(false);
        contactBsDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        contactBsDialog.show();

        ImageView backBtn = contactBsDialog.findViewById(R.id.contact_bsDialog_back_btn);
        EditText contactName = contactBsDialog.findViewById(R.id.contact_dialog_editText_name);
        EditText contactEmail = contactBsDialog.findViewById(R.id.contact_dialog_editText_email);
        EditText contactMobile = contactBsDialog.findViewById(R.id.contact_dialog_editText_mobile);
        EditText contactIssueDetail = contactBsDialog.findViewById(R.id.contact_dialog_editText_issueDetail);
        TextView issueTextLength = contactBsDialog.findViewById(R.id.contact_bsDialog_issue_text_length);
        FrameLayout submitBtn = contactBsDialog.findViewById(R.id.contact_bsDialog_submit_btn);

        String name = contactName.getText().toString();
        String email = UserContext.getLoggedInUser().getUserEmail();
        String mobile = contactMobile.getText().toString();
        String gameId = UserContext.getLoggedInUser().getId();
        String msg = contactIssueDetail.getText().toString();
        contactEmail.setText(email);

//        ContactUsModel contactUsModel = new ContactUsModel(name, email, mobile, gameId, msg);

        contactIssueDetail.addTextChangedListener(new TextWatcher() {
            int textLeft;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textLeft = 350 - count;
                issueTextLength.setText(String.valueOf(textLeft));
            }

            @Override
            public void afterTextChanged(Editable s) {
                issueTextLength.setText(String.valueOf(textLeft));
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactBsDialog.dismiss();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableContactUsBtn();
                contactUs_SendEmail(name, email, mobile, gameId, msg);
//                contactBsDialog.dismiss();
            }
        });
    }

    private void disableContactUsBtn() {
        Date dateTime = new Date(System.currentTimeMillis());
        long btnCurrentTime = dateTime.getTime();

        Calendar calFutureTime = Calendar.getInstance();
        calFutureTime.setTimeInMillis(btnCurrentTime);
        calFutureTime.add(Calendar.HOUR_OF_DAY, 2);
        calFutureTime.set(Calendar.SECOND, 0);
        long btnEnable_futureTime = calFutureTime.getTimeInMillis();

        editorSPref.putLong(Constants.SP_CONTACT_US_FUTURE_TIME, btnEnable_futureTime);
        editorSPref.commit();

        checkAndStart_earnBtnTimer();
    }

    private long getContactUsFutureTime() {
        long contactBtnFutureTime = readSPref.getLong(Constants.SP_CONTACT_US_FUTURE_TIME, 0);
        return contactBtnFutureTime;
    }

    private void checkAndStart_earnBtnTimer() {
        Date dateTime = new Date(System.currentTimeMillis());
        long currentTime = dateTime.getTime();

        long futureTime = getContactUsFutureTime();
        long leftTime = futureTime - currentTime;

        if (currentTime <= futureTime) {
            binding.profileContactUsText.setTextColor(getResources().getColor(R.color.greyout_disable2));
            binding.profileContactUsLayout.setClickable(false);

            new CountDownTimer(leftTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long secondsInMilli = 1000;
                    long minutesInMilli = 60 * 1000;
                    long hoursInMilli = 60 * 60 * 1000;
                    long dayInMilli = 24 * 60 * 60 * 1000;

//                    long secondsInMilli = 1000;
//                    long minutesInMilli = secondsInMilli * 60;
//                    long hoursInMilli = minutesInMilli * 60;

                    long elapsedHours = millisUntilFinished / hoursInMilli;
                    millisUntilFinished = millisUntilFinished % hoursInMilli;

                    long elapsedMinutes = millisUntilFinished / minutesInMilli;
                    millisUntilFinished = millisUntilFinished % minutesInMilli;

                    long elapsedSeconds = millisUntilFinished / secondsInMilli;

                    binding.profileContactusCountdownTimer.setText("" + elapsedHours + "h " + elapsedMinutes + "m " + elapsedSeconds + "s");
                }

                @Override
                public void onFinish() {
                    binding.profileContactusCountdownTimer.setText("");
                    binding.profileContactUsText.setTextColor(getResources().getColor(R.color.white));
                    binding.profileContactUsLayout.setClickable(true);
                }
            }.start();
        } else {
            binding.profileContactusCountdownTimer.setText("");
            binding.profileContactUsText.setTextColor(getResources().getColor(R.color.white));
            binding.profileContactUsLayout.setClickable(true);
        }
    }

    private void contactUs_SendEmail1() {
        String name1 = UserContext.getLoggedInUser().getUserName();
        String email1 = UserContext.getLoggedInUser().getUserEmail();
        String gameId1 = UserContext.getLoggedInUser().getId();
        String app_version = BuildConfig.VERSION_NAME;

        String subject = "Payment related issue";
        String message = "Name: " + name1 + "\n" +
                "Email ID: " + email1 + "\n" +
                "Game ID: " + gameId1 + "\n" +
                "App Version: " + app_version + "\n" +
                "\n";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:contact.playwin.o2@gmail.com?subject=" + Uri.encode(subject) + "&body=" + Uri.encode(message));
        intent.setData(data);
        startActivity(intent);
    }

    public void contactUs_SendEmail(String name, String email, String mobile, String gameId, String msg) {

        String subject = "Payment related issue";
        String message = "Name: " + name + "\n" +
                "Email ID: " + email + "\n" +
                "Mobile No.: " + mobile + "\n" +
                "Game ID: " + gameId + "\n" +
                "\n"
                + msg;

//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.putExtra(Intent.EXTRA_EMAIL, "contact.spinwin.o2@gmail.com");
//        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
//        intent.putExtra(Intent.EXTRA_TEXT, message);
//        intent.setType("message/rfc822");
//        startActivity(Intent.createChooser(intent, "Choose an application to send your message"));

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:contact.spinwin.o2@gmail.com?subject=" + Uri.encode(subject) + "&body=" + Uri.encode(message));
        intent.setData(data);
        startActivity(intent);
    }


    private void test(SpUserModel testModel) {
        String test = testModel.getUserName();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        checkAndStart_earnBtnTimer();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}