package com.supreme.notebook.customClasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.supreme.notebook.MainActivity;
import com.supreme.notebook.R;
import com.supreme.notebook.editor;
import com.supreme.notebook.viewer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class CustomAdapter extends BaseAdapter {

    String[] names;
    Context context;
    LayoutInflater layoutInflater;

    public CustomAdapter(Context context, String[] names) {

        this.context = context;
        this.names = names;
        layoutInflater = (LayoutInflater.from(context));

    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        view = layoutInflater.inflate(R.layout.card_item,null);
        TextView title = view.findViewById(R.id.file_name);
        WebView preview = view.findViewById(R.id.preview);
        RelativeLayout card_item = view.findViewById(R.id.card_item);
        final File noteLocation = new File(Environment.getExternalStorageDirectory() + "/noteBook/notes/" + names[position]);
        String stored;
        final StringBuilder output = new StringBuilder();
        try {
            FileInputStream fileInputStream = new FileInputStream(noteLocation);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            while ((stored = bufferedReader.readLine()) != null) {
                output.append(stored).append("\n");
            }
            bufferedReader.close();
        } catch (FileNotFoundException ignored) {
        } catch (IOException ignored) {}
        final String cleaned = names[position].substring(0, names[position].length() - 4);
        final String construct = "<!Doctype html><html><style>html{text-align:center;}</style><body><h1>" + cleaned + "</h1>" +
                output + "</body></html>";
        preview.loadData(construct, "text/html", "utf-8");
        title.setText(cleaned);
        final ImageButton more = view.findViewById(R.id.more_items);
        final View finalView = view;
        final String finalOutput = output.toString();
        card_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open = new Intent(finalView.getContext(), viewer.class);
                open.putExtra("view", construct);
                open.putExtra("raw", output.toString());
                finalView.getContext().startActivity(open);
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), more);
                popupMenu.getMenuInflater().inflate(R.menu.more_options, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                Intent i = new Intent(finalView.getContext(), editor.class);
                                i.putExtra("file_name", cleaned);
                                i.putExtra("content", finalOutput);
                                finalView.getContext().startActivity(i);
                                break;
                            case R.id.delete:
                                if (noteLocation.delete()) {
                                    if (noteLocation.exists()) {
                                        Toast.makeText(context, "File not deleted", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(context, "File deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent m = new Intent(finalView.getContext(), MainActivity.class);
                                        finalView.getContext().startActivity(m);
                                    }
                                }, 250);
                                break;
                            case R.id.view:
                                Intent v = new Intent(finalView.getContext(), viewer.class);
                                v.putExtra("view", construct);
                                v.putExtra("raw", output.toString());
                                finalView.getContext().startActivity(v);
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        return view;
    }
}
