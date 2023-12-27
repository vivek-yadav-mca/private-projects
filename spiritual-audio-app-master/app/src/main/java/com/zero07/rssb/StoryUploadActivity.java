package com.zero07.rssb;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.zero07.rssb.databinding.ActivityStoryUploadBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.zero07.rssb.models.StoryModel;

import java.util.ArrayList;
import java.util.List;

public class StoryUploadActivity extends AppCompatActivity {

    List<StoryModel> storyModels;
    FirebaseFirestore database;
    ActivityStoryUploadBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_upload);
        database = FirebaseFirestore.getInstance();
        binding = ActivityStoryUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.uploadStoryButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadStoryData();
            }
        });
    }

    protected void createStoryData() {
        storyModels = new ArrayList<>();
        /**** Upload Here ****/




/**** 26-05-2021 ****/
    //    storyModels.add(new StoryModel("raja_vikramaditya", "राजा विक्रमादित्य", "https://99story.in/wp-content/uploads/2021/05/Raja-Vikramaditya-min.jpg","https://99story.in/raja-vikramaditya/"));
    //    storyModels.add(new StoryModel("yaadein_bachpan_ki", "यादें बचपन की", "https://99story.in/wp-content/uploads/2021/05/Yaadein-Bachpan-Ki-min.jpg","https://99story.in/yaadein-bachpan-ki/"));

    //    storyModels.add(new StoryModel("guru_ki_khushi", "गुरु की ख़ुशी", "https://99story.in/wp-content/uploads/2021/05/Guru-Ki-Khushi-min.jpg","https://99story.in/guru-ki-khushi/"));
    //    storyModels.add(new StoryModel("ab_main_parmatma_ko_manta_hun", "अब मैं परमात्मा को मानता हूँ", "https://99story.in/wp-content/uploads/2021/05/Ab-Mai-Parmatma-Ko-Manta-Hun.jpg","https://99story.in/ab-mai-parmatma-ko-manta-hun/"));

    //    storyModels.add(new StoryModel("maut_ki_khushi", "मौत की खुशी", "https://99story.in/wp-content/uploads/2021/05/Maut-Ki-Khushi-min.jpg","https://99story.in/maut-ki-khushi-inspirational-story-spiritual-story/"));
    //    storyModels.add(new StoryModel("laalach_ka_fal", "लालच का फल", "https://99story.in/wp-content/uploads/2021/05/Laalach-Ka-Fal-min.jpg","https://99story.in/laalach-ka-fal/"));
    //    storyModels.add(new StoryModel("ek_balti_doodh", "एक बाल्टी दूध", "https://99story.in/wp-content/uploads/2021/05/Ek-Balti-Doodh-min.jpg","https://99story.in/one-bucket-milk-moral-story/"));

    //    storyModels.add(new StoryModel("manushya_ki_keemat","मनुष्य की कीमत","https://99story.in/wp-content/uploads/2021/05/My-Post-min.jpg","https://kahaniya-755.blogspot.com/2020/09/value-of-human-motivational-story.html"));
    //    storyModels.add(new StoryModel("maut-ka-saudagar", "मौत का सौदागर", "https://99story.in/wp-content/uploads/2021/05/Maut-Ka-Saudagar.jpg","https://99story.in/merchant-of-the-death-inspirational-stories/"));
    //    storyModels.add(new StoryModel("teen_prashna", "3 प्रश्न", "https://99story.in/wp-content/uploads/2021/05/3-Prashna-min.jpg","https://99story.in/3-questions-by-leo-tolstoy-motivational-story/"));
    //    storyModels.add(new StoryModel("seth_ji_ki_pariksha", "सेठ जी की परिक्षा", "https://99story.in/wp-content/uploads/2021/05/Seth-Ji-Ki-Pariksha-min.jpg","https://99story.in/seth-ji-ki-pariksha-spiritual-story/"));
    //    storyModels.add(new StoryModel("goat_and_monkey", "बकरा और बन्दर", "https://99story.in/wp-content/uploads/2021/05/Goat-and-Monkey-min.jpg","https://99story.in/goat-and-monkey-inspirational-story-spiritual-story/"));
    //    storyModels.add(new StoryModel("aari_ki_keemat", "आरी की कीमत", "https://99story.in/wp-content/uploads/2021/05/Aari-Ki-Keemat-min.jpg","https://99story.in/the-value-of-hand-saw-inspirational-stories/"));

    //    storyModels.add(new StoryModel("zindagi_poem", "ज़िंदगी - कविता", "https://99story.in/wp-content/uploads/2021/05/My-Post-min-1.jpg","https://kahaniya-755.blogspot.com/2020/09/zindagi-poem-inspirtional-poem.html"));
    //    storyModels.add(new StoryModel("ajnabi_pardesh", "अजनबी परदेश", "https://99story.in/wp-content/uploads/2021/05/My-Post-1-min.jpg","https://kahaniya-755.blogspot.com/2020/09/strange-foreign-land-poem.html"));


//        storyModels.add(new StoryModel("", "", "",""));

    }

    protected void uploadStoryData() {
        createStoryData();
        WriteBatch batch = database.batch();
        for (StoryModel model : storyModels) {
            DocumentReference docRef = database.collection(Constants.STORY_COLLECTION).document(model.getId());
            batch.set(docRef, model);
        }
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(StoryUploadActivity.this, "Story uploaded successsfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StoryUploadActivity.this, "Error while uploading Story", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}