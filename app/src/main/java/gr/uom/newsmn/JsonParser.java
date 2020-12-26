package gr.uom.newsmn;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    public static final String TAG = "JsonParser";

    private static final String TWEET_ID_LITERAL = "id";
    private static final String BODY_ID_LITERAL = "text";

    public List<FbPost> parsePostData(String postJsonData){
        List<FbPost> TweetList = new ArrayList<>();

        try{
            JSONArray jsonPostArray = new JSONArray(postJsonData);

            for(int i=0; i< jsonPostArray.length(); i++) {
                JSONObject postJsonObject = jsonPostArray.getJSONObject(i);

                int id = postJsonObject.getInt(TWEET_ID_LITERAL);
                String text = postJsonObject.getString(BODY_ID_LITERAL);

               // FbPost tweet = new FbPost();
               // tweet.setId(id);
                //tweet.setText(text);

               // TweetList.add(tweet);
            }

        }catch (JSONException ex){
            Log.e(TAG, "Error in json parsing", ex);
        }

        return TweetList;
    }

}


