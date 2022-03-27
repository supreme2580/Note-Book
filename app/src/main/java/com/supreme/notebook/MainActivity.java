package com.supreme.notebook;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.supreme.notebook.fragments.content_list;
import com.supreme.notebook.fragments.empty;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
    int permissionCode = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, permissions, permissionCode);
        File fileDir = new File(Environment.getExternalStorageDirectory() + "/noteBook/");
        if (!fileDir.exists()){
            fileDir.mkdirs();
        }
        File notes = new File(Environment.getExternalStorageDirectory() + "/noteBook/notes/");
        if (!notes.exists()) {
            notes.mkdirs();
        }
        File notesLocation = new File(Environment.getExternalStorageDirectory() + "/noteBook/notes");
        String[] files = notesLocation.list();
        List list = new ArrayList<>();
        if (files != null) {
            for (String i : files) {
                if (i.endsWith(".txt")) {
                    list.add(i);
                }
            }
        }
        if (list.isEmpty()) {
            fragmentLoader(new empty());
        }
        else {
            fragmentLoader(new content_list());
        }
        FloatingActionButton add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                addDialog();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void addDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_card);
        dialog.setCancelable(true);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(layoutParams);

        dialog.findViewById(R.id.create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText fileName = dialog.findViewById(R.id.fileName);
                String name = fileName.getText().toString();
                File filePath = new File(Environment.getExternalStorageDirectory() + "/noteBook/notes/" + name + ".txt");
                if (filePath.exists()) {
                    fileName.setError("This file already exists");
                }
                else {
                    if (name.equals("")) {
                        Toast.makeText(MainActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        try {
                            filePath.createNewFile();
                        } catch (IOException ignored) {
                        }
                        Intent i = new Intent(MainActivity.this, writer.class);
                        i.putExtra("file_name", name);
                        startActivity(i);
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int permissionCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(permissionCode, permissions, grantResults);
        if (permissionCode == 10) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thank you!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Please grant all permissions for this app to work properly.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void fragmentLoader(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to exit this app?");
        alertDialogBuilder.setTitle("Exit app");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent exit = new Intent(Intent.ACTION_MAIN);
                exit.addCategory(Intent.CATEGORY_HOME);
                exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(exit);
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent reload = new Intent(MainActivity.this, MainActivity.class);
                startActivity(reload);
                alertDialogBuilder.create().cancel();
            }
        });
        alertDialogBuilder.create().show();
    }
}