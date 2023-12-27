package com.zero07.rssb.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.zero07.rssb.R;
import com.zero07.rssb.adapters.HomeItemAdapter;
import com.zero07.rssb.adapters.SocialPostAdapter;
import com.zero07.rssb.databinding.ActivitySocialPostBinding;
import com.zero07.rssb.fragment.FragmentAdapter;
import com.zero07.rssb.models.SocialPostModel;

import java.util.ArrayList;
import java.util.List;

public class SocialPostActivity extends AppCompatActivity {

    ActivitySocialPostBinding binding;

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    FragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySocialPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.activity_social_post);

        tabLayout = findViewById(R.id.social_post_tabLayout);
        viewPager2 = findViewById(R.id.social_post_viewPager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentAdapter = new FragmentAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(fragmentAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
//                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });




    }







///**** Under Maintenance Code ****/
//        connectToFirebase();
//    }
//
//    private void connectToFirebase() {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference reference = database.getReference();
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                UnderMaintenance underMaintenance = dataSnapshot.getValue(UnderMaintenance.class);
//                if (underMaintenance == null) return;
//                if (underMaintenance.is_under_maintenance) {
//                    showUnderMaintenanceDialog(underMaintenance.under_maintenance_message);
//                } else {
//                    dismissUnderMaintenanceDialog();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    private void showUnderMaintenanceDialog(String underMaintenanceMessage) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(SocialPostActivity.this);
//        View view = LayoutInflater.from(SocialPostActivity.this).inflate(R.layout.layout_under_maintenance_dialog,
//                (ConstraintLayout) findViewById(R.id.constraint_under_maintenance_dialog));
//        builder.setView(view);
//
//        builder.setCancelable(false);
//        AlertDialog alertDialog = builder.create();
//
//        TextView hindiMessage = view.findViewById(R.id.hindi_message_maintenance);
//        TextView englishMessage = view.findViewById(R.id.english_message_maintenance);
//
//        hindiMessage.setText(underMaintenanceMessage);
//
//        ImageView closeButton = view.findViewById(R.id.closeButton_under_maintenance_dialog);
//
//        closeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//            }
//        });
//
//        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        alertDialog.show();
//
//
//
//        if (dialog == null) {
//            dialog = new AlertDialog.Builder(this).create();
//            dialog.setCancelable(false);
//            dialog.setButton(BUTTON_POSITIVE,
//                    "OK",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                            SocialPostActivity.super.finish();
//                        }
//                    });
//        }
//        dialog.setMessage(underMaintenanceMessage);
//        if (!this.isFinishing()) dialog.show();
//    }
//
//    private void dismissUnderMaintenanceDialog() {
//        if (dialog != null && dialog.isShowing()) dialog.dismiss();
//    }
///**** Under Maintenance Code ****/


}