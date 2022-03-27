package com.supreme.notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class viewer extends AppCompatActivity {

    TextToSpeech textToSpeech;
    FloatingActionButton mic_viewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        String view = getIntent().getStringExtra("view");
        final String raw = getIntent().getStringExtra("raw");
        WebView viewer = findViewById(R.id.viewer);
        mic_viewer = findViewById(R.id.mic_viewer);
        assert view != null;
        viewer.loadData(view, "text/html", "utf-8");
        textToSpeech = new TextToSpeech(viewer.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });
        mic_viewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.speak(raw, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(viewer.this, MainActivity.class);
        startActivity(i);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}