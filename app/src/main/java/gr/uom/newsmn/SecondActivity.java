package gr.uom.newsmn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import twitter4j.Location;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
 //package gr.uom.newsmn;
import java.util.ArrayList;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.textclassifier.ConversationActions;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.widget.ArrayAdapter;
import android.widget.Toast;


public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "Rest App";

    private ListView listOfPosts;
    public long UserId ;
    public String UserLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        String HashtagTxt = intent.getStringExtra("hashtag");

        listOfPosts = findViewById(R.id.listOfPosts);

        GetDataTask getDataTaskObject = new GetDataTask();
        getDataTaskObject.execute(String.valueOf(HashtagTxt));

        Log.d(TAG, " Start async request execution for web service data");


    }



    public class GetDataTask extends AsyncTask<String, Void, ArrayList<String>> {

        public static final String TAG = "RestApp";

        public ArrayList<String> ArrayListToPass = new ArrayList<>();
        public  ArrayList<String> ListOfTweets = new ArrayList<>();



        public ArrayList<String> downloadRestData(String hashtag) {  //Υπαρχουν αντιστοιχες μεθοδοι getToken για το καθενα απο αυτα τα credentials
            Log.d(TAG, "Downloading Tweets.....");

            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey("JQSI85ziTRoCHZvItYMcEe74d")
                    .setOAuthConsumerSecret("IyjOWe4XgcJsacXROoepAWtM4cL0PTcrJYtJzXSiitdqBFG0OC")
                    .setOAuthAccessToken("1330830428555587584-Fb8Qx7S6NTXUvWQaXxfFscyWzxOkpd")
                    .setOAuthAccessTokenSecret("VB02SQ8p5Ek7FYLOUgZzlnmBo1VdgOa6jj4rmZsovmbdi");
            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();

            Query query = new Query("#"+hashtag);
            query.setCount(100);
            QueryResult result = null;
            try {
                result = twitter.search(query);
            } catch (TwitterException e) {
                e.printStackTrace();
                // return  "";
            }


            for (twitter4j.Status status : result.getTweets()) {
               // for(int i=0; i<1 ; i++) {
                    //System.out.println(status);
                    //System.out.println("Value1--------------------" + status.getUser().getId());
                    //UserId = status.getUser().getId();
                    //UserLocation = status.getUser().getLocation();
                   // System.out.println("Value2--------------------" + status.getUser().getLocation());
               // }
                System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
                ListOfTweets.add("@" + status.getUser().getScreenName() + ":" + status.getText());
            }

            return ListOfTweets;

        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            String hashtag = strings[0];
            Log.d(TAG, "Doing task in background for hashtag "+ hashtag);

            return downloadRestData(hashtag);

        }

        @Override
        protected void onPostExecute(ArrayList<String> ListOfTweets) {

            ArrayListToPass = ListOfTweets;

            Log.d(TAG, "Just got results!");


                ArrayAdapter<String> adapter = new ArrayAdapter(SecondActivity.this, R.layout.post_line, R.id.txtOfEveryPost, ArrayListToPass);
                listOfPosts.setAdapter(adapter);


                //Μηνυμα Toast με τις λεπτομερειες(id και Location) καθε φορα που πατας σε καποιο tweet

            listOfPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    // TODO Auto-generated method stub
                    Toast.makeText(SecondActivity.this, "id: " + UserId + "Location: " + UserLocation , Toast.LENGTH_SHORT).show();
                }

            });


                Log.d(TAG, String.valueOf(ListOfTweets));


        }


        //public ArrayList<String> getArrayListToPass(){
        // return ArrayListToPass;
        // }

        //fb 1st ID 990704334791650
        //<uses-feature android:name="android.hardware.type.watch" /> problem with Feature WATCH
        //<uses-permission android:name="android.permission.WAKE_LOCK" />

        /*<uses-library
            android:name="com.google.android.wearable"
            android:required="true" />
        <!--
               Set to true if your app is Standalone, that is, it does not require the handheld
               app to run.
        -->
        <meta-data
            android:name="com.google.android.wearable.standalone"
            android:value="true" />


            <activity
            android:name=".ThirdActivity"
            android:label="@string/title_activity_third">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

            */


    }


}