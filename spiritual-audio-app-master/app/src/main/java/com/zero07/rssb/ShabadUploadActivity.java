package com.zero07.rssb;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.zero07.rssb.databinding.ActivityShabadUploadBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.zero07.rssb.models.ShabadModel;

import java.util.ArrayList;
import java.util.List;

public class ShabadUploadActivity extends AppCompatActivity {

    List<ShabadModel> shabadModels;
    FirebaseFirestore database;
    ActivityShabadUploadBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shabad_upload);
        database = FirebaseFirestore.getInstance();
        binding = ActivityShabadUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.uploadShabadButn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadShabadData();
            }
        });
    }

    protected void createShabadData() {
        shabadModels = new ArrayList<>();
        shabadModels.add(new ShabadModel("has_ke_gujari_ja", "हंस के गुजारी जा", "Has Ke Gujari Ja", "https://99story.in/wp-content/uploads/2021/05/Has-ke-gujari-ja.mp3", 1));
        shabadModels.add(new ShabadModel("sohne_phula_ve_gulab_deya", "सोहने फुला वे गुलाब दिया", "Sohne Phula Ve Gulab Deya", "https://99story.in/wp-content/uploads/2021/05/sohne-phula-ve-gulab-deya_new.mp3", 2));
        shabadModels.add(new ShabadModel("de_darshan_guru_mere_ruhan_pukar_diya", "दे दर्शन गुरु मेरे रूहा पुकार दिया", "De Darshan Guru Mere Ruhan Pukar Diya", "https://99story.in/wp-content/uploads/2021/05/de-darshan-guru-mere-ruhan-pukar-diya_new.mp3", 3));
        shabadModels.add(new ShabadModel("mere_sacheya_guru_ji_meherban", "मेरे सच्चे गुरुजी महरबाना", "Mere Sacheya Guru Ji Meherban", "https://99story.in/wp-content/uploads/2021/05/mere-sacheya-guru-ji-meherban_new.mp3", 4));
        shabadModels.add(new ShabadModel("bulleh_nu_samjhaava_aaiyaan", "बुल्लेह नू समझावन आइयां", "Bulleh Nu Samjhaavan Aaiyaan", "https://99story.in/wp-content/uploads/2021/05/Bulleh-Nu-Samjhaavan-Aaiyaan.mp3"));

//        shabadModels.add(new ShabadModel("has_ke_gujari_ja", "हंस के गुजारी जा", "Has Ke Gujari Ja", "https://99story.in/wp-content/uploads/2021/05/Has-ke-gujari-ja.mp3", 1));
//        shabadModels.add(new ShabadModel("sohne_phula_ve_gulab_deya", "सोहने फुला वे गुलाब दिया", "Sohne Phula Ve Gulab Deya", "https://99story.in/wp-content/uploads/2021/05/sohne-phula-ve-gulab-deya_new.mp3"));
//        shabadModels.add(new ShabadModel("de_darshan_guru_mere_ruhan_pukar_diya", "दे दर्शन गुरु मेरे रूहा पुकार दिया", "De Darshan Guru Mere Ruhan Pukar Diya", "https://99story.in/wp-content/uploads/2021/05/de-darshan-guru-mere-ruhan-pukar-diya_new.mp3"));
//        shabadModels.add(new ShabadModel("mere_sacheya_guru_ji_meherban", "मेरे सच्चे गुरुजी महरबाना", "Mere Sacheya Guru Ji Meherban", "https://99story.in/wp-content/uploads/2021/05/mere-sacheya-guru-ji-meherban_new.mp3"));
    }

    protected void uploadShabadData() {
        createShabadData();
        WriteBatch batch = database.batch();
        for (ShabadModel model : shabadModels) {
            DocumentReference docRef = database.collection("Shabad").document(model.getShabadId());
            batch.set(docRef, model);
        }
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(ShabadUploadActivity.this, "Shabad uploaded successsfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ShabadUploadActivity.this, "Error while uploading Shabad", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}