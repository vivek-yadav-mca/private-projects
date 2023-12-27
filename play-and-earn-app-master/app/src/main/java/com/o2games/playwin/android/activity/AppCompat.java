package com.o2games.playwin.android.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.o2games.playwin.android.LanguageManager;

//@Keep
public class AppCompat extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LanguageManager languageManager = new LanguageManager(this);
        languageManager.updateLanguage(languageManager.getLanguageSPref());
    }
}
