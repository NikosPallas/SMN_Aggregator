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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Rest App";

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


        //Από αυτό το σημείο και κάτω υπάρχει η λειτουργικότητα του Facebook login

        //1.Εχω δημιουργησει καποιους Test Users για να κανω δοκιμες
        //2.Δεν γινεται πλεον να κατεβαζω posts απο το Fb graph api

        login = findViewById(R.id.login_button);
        recyclerView = findViewById(R.id.recyclerView);

        CallbackManager callbackManager = CallbackManager.Factory.create();

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


       GraphRequest graphRequestFriends = GraphRequest.newMyFriendsRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONArrayCallback() {
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

    }
}
