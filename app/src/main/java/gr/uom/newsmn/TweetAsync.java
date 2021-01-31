package gr.uom.newsmn;

import android.os.AsyncTask;
import android.util.Log;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TweetAsync extends AsyncTask<String, Void, String> {

    private static final String TAG = "Making a tweet";

    public String MakeTweet(String PostTxt) {

        ConfigurationBuilder sb = new ConfigurationBuilder();
        sb.setDebugEnabled(true)
                .setOAuthConsumerKey("JQSI85ziTRoCHZvItYMcEe74d")
                .setOAuthConsumerSecret("IyjOWe4XgcJsacXROoepAWtM4cL0PTcrJYtJzXSiitdqBFG0OC")
                .setOAuthAccessToken("1330830428555587584-Fb8Qx7S6NTXUvWQaXxfFscyWzxOkpd")
                .setOAuthAccessTokenSecret("VB02SQ8p5Ek7FYLOUgZzlnmBo1VdgOa6jj4rmZsovmbdi");
        TwitterFactory tf = new TwitterFactory(sb.build());
        Twitter twitter = tf.getInstance();

        try {
            twitter4j.Status status = twitter.updateStatus(PostTxt);
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        return PostTxt;
    }

        @Override
        protected String doInBackground (String...strings){

            String PostTxt = strings[0];
            Log.d(TAG, " Doing the Tweet");
            return MakeTweet(PostTxt);
        }

        @Override
        protected void onPostExecute (String s){
            String twitter = s;

            Log.d(TAG,"Made the tweet: " + twitter);
        }


}
