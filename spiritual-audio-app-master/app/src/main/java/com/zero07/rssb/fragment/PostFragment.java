package com.zero07.rssb.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zero07.rssb.Constants;
import com.zero07.rssb.R;
import com.zero07.rssb.activity.AddPostActivity;
import com.zero07.rssb.adapters.SocialPostAdapter;
import com.zero07.rssb.databinding.FragmentPostBinding;
import com.zero07.rssb.models.SocialPostModel;
import com.zero07.rssb.userModels.UserContext;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PostFragment extends Fragment {

    private static final String TAG = PostFragment.class.getName();

    FragmentPostBinding binding;
    Animation floatingScale;
    FirebaseDatabase database;

    private AlertDialog dialog;

    public PostFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post, container, false);
        binding = FragmentPostBinding.inflate(inflater, container, false);

        database = FirebaseDatabase.getInstance();

        ArrayList<SocialPostModel> socialPostsList = new ArrayList<>();
        SocialPostAdapter adapter = new SocialPostAdapter(getContext(), socialPostsList);

//        socialPostsList.add(new SocialPostModel("https://lh3.googleusercontent.com/a-/AOh14Gg_yHBPTyZH4_60Xtur0EeME6voTniEA63Zyj7OIQ",
//                "Aadarsh Yadav", "Thu, 29 Jul 2021 - 00:48", "Test 1",
//                "https://firebasestorage.googleapis.com/v0/b/rssb-shabad-26-05-2021.appspot.com/o/user_images%2Faadarshyadav95%40gmailcom?alt=media&token=54d40b82-9c55-41ca-bf7d-aa17490c5323"));
//        socialPostsList.add(new SocialPostModel("https://lh3.googleusercontent.com/a-/AOh14Gg_yHBPTyZH4_60Xtur0EeME6voTniEA63Zyj7OIQ",
//                "Aadarsh Yadav", "Thu, 29 Jul 2021 - 00:50", "Test 2",
//                "https://firebasestorage.googleapis.com/v0/b/rssb-shabad-26-05-2021.appspot.com/o/user_images%2Faadarshyadav95%40gmailcom?alt=media&token=5aa14df9-275b-4389-8565-185c14682921"));
//        socialPostsList.add(new SocialPostModel("R.drawable.user_profile", "Aadarsh Yadav", "24-July-2021", "यहाँ कुछ लिखिए....", "R.drawable.ic_musical_note"));
//        socialPostsList.add(new SocialPostModel("R.drawable.user_profile", "Aadarsh Yadav", "24-July-2021", "यहाँ कुछ लिखिए....", "R.drawable.ic_musical_note"));
//        socialPostsList.add(new SocialPostModel("R.drawable.user_profile", "Aadarsh Yadav", "24-July-2021", "यहाँ कुछ लिखिए....", "R.drawable.ic_musical_note"));

//        socialPostLists.add(new SocialPostModel("", "", "", "", ""));

        database.getReference()
                .child(Constants.USER_POST_DATA)
                .child(UserContext.getLoggedInUser().getId())


//                .get()
//                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//                    @Override
//                    public void onSuccess(DataSnapshot dataSnapshot) {
//                        SocialPostModel model = dataSnapshot.getValue(SocialPostModel.class);
//
//                        socialPostsList.add(model);
//
////                        socialPostsList.add(dataSnapshot.getValue(SocialPostModel.class));
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e(TAG, "Error while syncing user post data from firebase: " + e);
//            }
//        });

//                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            socialPostsList.clear();
//                            for (DataSnapshot data : task.getResult().getChildren()) {
//                                socialPostsList.add(data.getValue(SocialPostModel.class));
//                            }
//                        }
//                    }
//                });

//        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
//        WalletWithdrawModel walletWithdrawModel = snapshot.toObject(WalletWithdrawModel.class);
//        walletWithdrawModelArrayList.add(walletWithdrawModel);

//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
////                        socialPostsList.clear();
//                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                            SocialPostModel model = snapshot1.getValue(SocialPostModel.class);
//
//                            socialPostsList.add(model);
//                        }
//                        adapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//                    }
//                });

        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                socialPostsList.clear();
//                for (DataSnapshot snapshot1 = snapshot.)
                SocialPostModel getModelData = snapshot.getValue(SocialPostModel.class);

                socialPostsList.add(getModelData);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getContext(), "Unable to retrieve data", Toast.LENGTH_LONG).show();
            }
        });


        binding.socialPostRv.setLayoutManager(new GridLayoutManager(getContext(), 1));
        binding.socialPostRv.setAdapter(adapter);

        floatingScale = AnimationUtils.loadAnimation(getContext(), R.anim.anim_floating_button_scale);

        binding.addPostFloatButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.viewBgFloatingButn.setVisibility(View.VISIBLE);
                binding.viewBgFloatingButn.startAnimation(floatingScale);

                floatingScale.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
//                        binding.viewBgFloatingButn.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(getContext(), AddPostActivity.class);
                        Toast.makeText(getContext(), "Please Wait...", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

            }
        });


        return binding.getRoot();
//        return inflater.inflate(R.layout.fragment_post, container, false);
    }


    private void addPostEventListener(DatabaseReference mPostReference) {
//        // [START post_value_event_listener]
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get Post object and use the values to update the UI
//                Post post = dataSnapshot.getValue(Post.class);
//                // ..
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//            }
//        };
//        mPostReference.addValueEventListener(postListener);
//        // [END post_value_event_listener]
    }



//    private void fetchUserPost() {
//        ArrayList<SocialPostModel> socialPostsList = new ArrayList<>();
//
//        FirebaseDatabase.getInstance().getReference()
//                .child(Constants.USER_POST_DATA)
////                .child(UserContext.getLoggedInUser().getId())
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//                    @Override
//                    public void onSuccess(DataSnapshot dataSnapshot) {
//                        SocialPostModel model = dataSnapshot.getValue(SocialPostModel.class);
//
//                        socialPostsList.add(model);
//
////                        socialPostsList.add(dataSnapshot.getValue(SocialPostModel.class));
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e(TAG, "Error while syncing user post data from firebase: " + e);
//                    }
//                });
//
//        SocialPostAdapter adapter = new SocialPostAdapter(getContext(), socialPostsList);
//        binding.socialPostRv.setLayoutManager(new GridLayoutManager(getContext(), 1));
//        binding.socialPostRv.setAdapter(adapter);
//
//    }


}



//    public void showBottomSheetDialog() {
//        BottomSheetDialog bsDialog = new BottomSheetDialog(getContext());
//        bsDialog.setContentView(R.layout.layout_add_post_dialog);
//        bsDialog.setCancelable(false);
//        bsDialog.show();
//
//        ImageView dialogUserPhoto = bsDialog.findViewById(R.id.dialog_user_photo);
//        TextView dialogUserName = bsDialog.findViewById(R.id.dialog_user_name);
//        ImageView addPostSelectedPhoto = bsDialog.findViewById(R.id.add_post_user_selected_photo);
//
//        Button postButton = bsDialog.findViewById(R.id.add_post_button);
//        ImageView cancelButton = bsDialog.findViewById(R.id.close_button_post_dialog);
//
//        dialogUserName.setText(UserContext.getLoggedInUser().getUserName());
//
//        RequestOptions loadImage = new RequestOptions()
//                .centerCrop()
//                .circleCrop()  //to crop image in circle view
//                .placeholder(R.drawable.user_profile)
//                .error(R.drawable.user_profile);
//
//        Glide.with(getActivity())
//                .load(UserContext.getLoggedInUser().getUserPhotoUrl())
//                .apply(loadImage)
//                .into(dialogUserPhoto);
//
//        postButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bsDialog.dismiss();
//            }
//        });
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bsDialog.dismiss();
//            }
//        });
//
//        addPostSelectedPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
//                Intent galleryintent = new Intent();
//                galleryintent.setAction(Intent.ACTION_GET_CONTENT);
//                galleryintent.setType("image/*");
//                startActivityForResult(galleryintent, 45);
//            }
//        });
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        BottomSheetDialog bsDialog = new BottomSheetDialog(getContext());
//        bsDialog.setContentView(R.layout.layout_add_post_dialog);
//
//        ImageView addPostSelectedPhoto = bsDialog.findViewById(R.id.add_post_user_selected_photo);
//
//        if (data != null) {
//            if (data.getData() != null) {
//                addPostSelectedPhoto.setImageURI(data.getData());
//
//            }
//        }
//    }



/*** Opening Bottom Sheet Fragment ***/
//                        AddPostBottomSheetFragment addPostBottomSheetFragment = new AddPostBottomSheetFragment();
////                        addPostBottomSheetFragment.setCancelable(false);
//                        addPostBottomSheetFragment.show(getParentFragmentManager(), "AddPostBottomSheet");
/*** Opening Bottom Sheet Fragment ***/