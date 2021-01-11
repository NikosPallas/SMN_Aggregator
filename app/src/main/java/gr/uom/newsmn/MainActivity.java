package gr.uom.newsmn;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Rest App";

    String pg_token = "EAADE9NOJtpMBAFPG2SVZAu3zNxvuvynpo8Hy1SZASkfh6ZAEazqMZALH4BPV6Y8WcVPFNpUM7ZA1DyVvUjg5rPLeARmJ3SWMyIf9q98p42PZCfw5jCZBLHzqeDdDxqQS2d6lcCi8DLSjmrgzaMzGZAZAMRkWLSUAv4xVyarVnJ8PpLGZBlXzkgem9kTY5tSN4QZAY7D66pPiER8mtpzocCxFQfz";

    CheckBox TweetBox;
    CheckBox FbBox;
    CheckBox InstaBox;

    LoginButton login;
    RecyclerView recyclerView;
    public FbAdapter fbAdapter;
    public RecyclerView.LayoutManager layoutManager;
    public CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText HashtagTxt = (EditText) findViewById( R.id.HashtagEditText);
        Button buttonSend = findViewById(R.id.btnTxt);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String HashTxt = HashtagTxt.getText().toString();
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("hashtag", HashTxt);

                startActivity(intent);
            }
        });


        Button InstaButton = findViewById(R.id.InstaBtn);

        InstaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String HashTxt = HashtagTxt.getText().toString();
                Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
                intent.putExtra("hashtag", HashTxt);

                startActivity(intent);

            }
        });


        CheckBox TweetBox = findViewById(R.id.TweetBox);
        CheckBox InstaBox = findViewById(R.id.InstaBox);
        
        EditText PostTxt = (EditText) findViewById( R.id.PostToShareTxt);

        TweetBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TweetBox.isChecked()){
                    //Make a Tweet
                    //ΑΣΥΓΧΡΟΝΑ???

                    String StringOfPostTxt = PostTxt.getText().toString();
                    TweetAsync TweetObject = new TweetAsync();
                    TweetObject.execute(valueOf(StringOfPostTxt));

                    Toast.makeText(MainActivity.this, "Your tweet has done", Toast.LENGTH_LONG).show();

                }
            }
        });


        InstaBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InstaBox.isChecked()){
                    //Make an Insta post
                }
            }
        });


        //Από αυτό το σημείο και κάτω υπάρχει η λειτουργικότητα του Facebook login

        //1.Εχω δημιουργησει καποιους Test Users για να κανω δοκιμες
        //2.Δεν γινεται πλεον να κατεβαζω posts απο το Fb graph api
        //3. Τα keys tokens μπορει να εχουν ληξει

        login = findViewById(R.id.login_button);
        recyclerView = findViewById(R.id.recyclerView);

        //CallbackManager callbackManager = CallbackManager.Factory.create();
        callbackManager = CallbackManager.Factory.create();

        login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                login.setVisibility(View.INVISIBLE);
                buttonSend.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }

        });

    }

   @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);


       CheckBox FbBox = findViewById(R.id.FbkBox);

       FbBox.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               CheckBox FbBox = findViewById(R.id.FbkBox);

               if(FbBox.isChecked()){
                   //Post a  fb status

                   AccessToken pg = new AccessToken(pg_token,String.valueOf(R.string.appId),String.valueOf(R.string.userId),null,null,null,null,null,null,null);

                   Bundle parameters=new Bundle();
                   GraphRequest request=GraphRequest.newPostRequest(pg,"/105208004851199/feed",null, new GraphRequest.Callback() {
                       @Override
                       public void onCompleted(GraphResponse response) {
                           Toast.makeText(MainActivity.this, "Your fb status has done successfully", Toast.LENGTH_LONG).show();
                       }
                   });


                   EditText PostTxt = (EditText) findViewById( R.id.PostToShareTxt);
                   String StringOfPostTxt = PostTxt.getText().toString();

                   parameters.putString("message",StringOfPostTxt);
                   request.setParameters(parameters);
                   request.executeAsync();

               }

           }
       });

        /*GraphRequest graphRequestFriends = GraphRequest.newMyFriendsRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONArrayCallback() {
           @Override
           public void onCompleted(JSONArray objects, GraphResponse response) {

               Log.d("TAG", objects.toString());

               ArrayList<FbPost> fbPosts = new ArrayList<>();                       //προσπελαση των JSONObjects που κατεβασαμε
               for(int i=0; i<objects.length(); i++ ) {
                   try{
                       JSONObject object = objects.getJSONObject(i);
                       fbPosts.add(new FbPost(object.getString("post")));

                   }catch(JSONException e){
                       e.printStackTrace();
                   }
               }

               layoutManager = new LinearLayoutManager(MainActivity.this);
               recyclerView.setLayoutManager(layoutManager);

               fbAdapter = new FbAdapter(fbPosts);
               recyclerView.setAdapter(fbAdapter);
           }
       });

       graphRequestFriends.executeAsync(); //η διαδικασια download γινεται ασυγχρονα


*/

    }


}
