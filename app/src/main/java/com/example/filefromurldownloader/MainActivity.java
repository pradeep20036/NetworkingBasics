package com.example.filefromurldownloader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


//   String url ="http://faculty.iiitd.ac.in/~mukulika/s1.mp3";
public class MainActivity extends AppCompatActivity {

    private static final int PERMISSON_STORAGE_CODE =1000 ;
    TextView tv_url;
    Button bt_download;
    String fileUrl="http://faculty.iiitd.ac.in/~mukulika/s1.mp3";
    File external_storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_url=findViewById(R.id.tv_url);
        bt_download=findViewById(R.id.bt_download);

        bt_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                external_storage=getFilesDir();
//                downloadFile(url,external_storage);
                new AsyncCaller().execute(fileUrl);
            }
        });
    }

    private class AsyncCaller extends AsyncTask<String, Void, Void>
    {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//
//            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }
        @Override
        protected Void doInBackground(String... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            int count;
            File file;
            try {
                URL url = new URL(fileUrl);
                URLConnection conection = url.openConnection();
                conection.connect();

                int lenghtOfFile = conection.getContentLength();

                InputStream input = conection.getInputStream();

                File SDCardRoot = getFilesDir();

                File folder = new File(SDCardRoot, "FolderName");
                if (!folder.exists())
                    folder.mkdir();
                file = new File(folder, "FileName");

                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[2000000];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);
                    System.out.println("Success");
                }

                output.flush();

                output.close();
                input.close();
                System.out.println("Done");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


            return null;


        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //this method will be running on UI thread

            pdLoading.dismiss();
        }

    }
}
