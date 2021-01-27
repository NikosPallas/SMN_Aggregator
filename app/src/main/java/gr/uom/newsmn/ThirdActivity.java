package gr.uom.newsmn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ThirdActivity extends AppCompatActivity {

    private static final String TAG = "InstaProccess";

    RecyclerView recyclerView;
    InstaAdapter myAdapter;
    RecyclerView.LayoutManager layoutManager;

    public ArrayList<String> ListOfMediaUrls = new ArrayList<>();

    public String ig_hash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Intent intent = getIntent();

        if (intent != null) {
            final ArrayList<InstaPostIds> InstaListOf_Urls = intent.getParcelableArrayListExtra("InstagramListOf_Urls");
            // Log.d(TAG, "Third activity--------" + InstaListOf_Urls.toString());

            for(InstaPostIds instapost : InstaListOf_Urls) {
                if(instapost!=null&&instapost.getMedia_url()!=null) {
                    Log.d(TAG,"Media urls-------------------" + instapost.getMedia_url().toString());

                    ListOfMediaUrls.add(instapost.getMedia_url());
                    }

            }


           // for(String mediaUrl : ListOfMediaUrls) {
                //Log.d(TAG,"this media url is: ---------------------------- " + mediaUrl.toString());
           // }

        }

        recyclerView = findViewById(R.id.RecyclerView);

        layoutManager = new LinearLayoutManager(ThirdActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        myAdapter = new InstaAdapter(ListOfMediaUrls);
        recyclerView.setAdapter(myAdapter);
    }

}