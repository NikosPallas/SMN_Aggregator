package gr.uom.newsmn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

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


public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "Rest App";

    private ListView listOfPosts;

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



        public ArrayList<String> downloadRestData(String hashtag) {  //remoteUrl
            Log.d(TAG, "Downloading data.....");

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


                Log.d(TAG, String.valueOf(ListOfTweets));


        }


        //public ArrayList<String> getArrayListToPass(){
        // return ArrayListToPass;
        // }


    }


}