package com.supreme.notebook;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Locale;

public class writer extends AppCompatActivity {

    FloatingActionButton more, mic, save, clip;
    EditText editor;
    String fileName;
    TextToSpeech textToSpeech;
    boolean open = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writer);
        more = findViewById(R.id.more);
        mic = findViewById(R.id.mic);
        save = findViewById(R.id.save);
        clip = findViewById(R.id.clipboard);
        editor = findViewById(R.id.editor);
        fileName = getIntent().getStringExtra("file_name");
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (fileName != null && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(fileName);
        }
        textToSpeech = new TextToSpeech(writer.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (open) {
                    mic.show();
                    save.show();
                    clip.show();
                    clip.animate().translationY(-(more.getCustomSize() + save.getCustomSize() + mic.getCustomSize() + 65));
                    mic.animate().translationY(-(more.getCustomSize() + save.getCustomSize() + 55));
                    save.animate().translationY(-(more.getCustomSize() + 45));
                    more.setImageResource(R.drawable.ic_baseline_close_24);
                    open = false;
                }
                else {
                    mic.hide();
                    save.hide();
                    clip.hide();
                    mic.animate().translationY(0);
                    save.animate().translationY(0);
                    clip.animate().translationY(0);
                    more.setImageResource(R.drawable.ic_baseline_more_24);
                    open = true;
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String body = editor.getText().toString();
                FileOutputStream fileOutputStream;
                try {
                    File file = new File(Environment.getExternalStorageDirectory() + "/noteBook/notes/"
                            + fileName + ".txt");
                    fileOutputStream = new FileOutputStream(file);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                    outputStreamWriter.append(body);
                    outputStreamWriter.close();
                    fileOutputStream.close();
                    Toast.makeText(writer.this, "File Saved", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException ignored) {
                } catch (IOException ignored) {
                }
            }
        });
        clip.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                String editorText = editor.getText().toString();
                android.content.ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboardManager.hasPrimaryClip()) {
                    editor.setText(editorText + clipboardManager.getText().toString());
                }
                else {
                    Toast.makeText(writer.this, "Nothing to paste", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String out = editor.getText().toString();
                textToSpeech.speak(out, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(writer.this, MainActivity.class);
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