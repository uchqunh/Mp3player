package com.example.mp3player;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView songListView;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songListView = findViewById(R.id.song_list_view);

        runtimePermission();
    }

    public void runtimePermission() {
       Dexter.withActivity(this)
               .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
               .withListener(new PermissionListener() {
                   @Override
                   public void onPermissionGranted(PermissionGrantedResponse response) {
                       display();
                   }

                   @Override
                   public void onPermissionDenied(PermissionDeniedResponse response) {

                   }

                   @Override
                   public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                   }
               }).check();
    }

    public ArrayList<File> songFileFind(File file) {

        ArrayList<File> arrayListMp3 = new ArrayList<>();

        File[] files = file.listFiles();

        for (File singleFile : files) {

            if (singleFile.isDirectory() && !singleFile.isHidden()) {

                arrayListMp3.addAll(songFileFind(singleFile));
            } else {
                if (singleFile.getName().endsWith(".mp3")) {

                    arrayListMp3.add(singleFile);
                }
            }
        }
        return arrayListMp3;
    }

    void display() {
        final ArrayList<File> mySongs = songFileFind(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];
        if (mySongs.size() != 0) {
            for (int i = 0; i < mySongs.size(); i++) {
                items[i] = mySongs.get(i).getName();
            }
        }
        else {
            Toast.makeText(this, "MP3 file not found", Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        songListView.setAdapter(myAdapter);
    }
}