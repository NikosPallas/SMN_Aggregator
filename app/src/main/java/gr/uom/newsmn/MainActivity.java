package gr.uom.newsmn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.List;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Rest App";

    String LongLivedToken = "EAADE9NOJtpMBAB2fHOWzrSzC02jMnwaKntQVNkTeyd4jVkeFBgMICrjzRsJ0uLfsx6xZCfr4c0XsxZAd521LSaqZB6nowOpZAxUXZChBbiMlL1PejF1ZBVkUeV8OMmVRJXYSqf1y98ZBV7KvvICcbfeL5EoAtJeFlLDbGlXSza9rDzh0dFo2bhL";

    String pg_token = "EAADE9NOJtpMBANkvwb67LRhZA6kZChOQF1JiK6TsuCWZCsnqVAQSBei5sZAyZAMdQTnO0C7yDzvOQisFwVQm4XZA5ano9cNlL6xDKwkmq75xeu6H0ZA5IZCYlQ8crTu1XnqAfxDZBrLeNRsKq3cbqwswPogFbmaFyRCMfpZCuYWChVfSk1oZCO1cZCj49SlgtbOMrnUZD";

    List<InstaPostIds> InstagramIdsList = new ArrayList<>();
    InstaPostIds InstaPost = new InstaPostIds();


    private static boolean PICK_IMAGES_PERM_GRANTED = false;
    private static final int REQUEST_CODE_IMAGES =102;

    CheckBox TweetBox;
    CheckBox FbBox;
    CheckBox InstaBox;

    LoginButton login;
   // RecyclerView recyclerView;
   // public FbAdapter fbAdapter;
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


        CheckBox TweetBox = findViewById(R.id.TweetBox);
        CheckBox InstaBox = findViewById(R.id.InstaBox);
        
        EditText PostTxt = (EditText) findViewById( R.id.PostToShareTxt);

        TweetBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TweetBox.isChecked()){
                    //Make a Tweet
                    //ΑΣΥΓΧΡΟΝΑ

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


        //Λειτουργικότητα Share an Image:
        Button imaShare = findViewById(R.id.ImaShareBtn);

        imaShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkPermission();


                Intent PickIntent = new Intent();
                PickIntent.setType("image/*");
                PickIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(PickIntent, " Pick an image to share"), REQUEST_CODE_IMAGES);


            }

        });



        //Από αυτό το σημείο και κάτω υπάρχει η λειτουργικότητα του Facebook login

        //1.Εχω δημιουργησει καποιους Test Users για να κανω δοκιμες
        //2.Δεν γινεται πλεον να κατεβαζω posts απο το Fb graph api
        //3. Τα keys tokens μπορει να εχουν ληξει

        login = findViewById(R.id.login_button);
     //   recyclerView = findViewById(R.id.recyclerView);

        //CallbackManager callbackManager = CallbackManager.Factory.create();
        callbackManager = CallbackManager.Factory.create();

        login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                login.setVisibility(View.INVISIBLE);
                buttonSend.setVisibility(View.INVISIBLE);


                Log.d(TAG, "Downloading  Insta Posts");


                AccessToken lv = new AccessToken(LongLivedToken,String.valueOf(R.string.appId),"105208004851199",null,null,null,null,null,null,null);

                EditText HashTxt = (EditText) findViewById( R.id.HashtagEditText);
                String StringOfHashTxt = HashTxt.getText().toString();

                Bundle parameters = new Bundle();
                parameters.putString("user_id", "17841445397955029");
                //parameters.putString("q", StringOfHashTxt);
                parameters.putString("q", "Ronaldo");

                GraphRequest request = GraphRequest.newGraphPathRequest(
                        lv,
                        "/ig_hashtag_search",
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {

                               // Log.d(TAG, "hashtag:"+ StringOfHashTxt);
                                //Log.d(TAG, response.toString());

                                //προσπελαση των JSONObjects που κατεβασαμε

                                try {
                                    JSONArray jsonUrlArray = response.getJSONObject().getJSONArray("data");
                                    for(int i = 0; i<jsonUrlArray.length(); i++){
                                        JSONObject ig_hashtag_url = jsonUrlArray.getJSONObject(i);

                                        String url_id = ig_hashtag_url.getString("id");

                                        // InstaPostIds InstaPost = new InstaPostIds();
                                        InstaPost.setUrl_id(url_id);

                                        //InstagramIdsList.add(InstaPost);

                                        Log.d(TAG, "ig hashtag id ----------- " + InstaPost.getUrl_id().toString());

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }



                            }
                        });

                request.setParameters(parameters);
                request.executeAsync();


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

       Button InstaButton = findViewById(R.id.InstaBtn);

       InstaButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               AccessToken lv = new AccessToken(LongLivedToken,String.valueOf(R.string.appId),"105208004851199",null,null,null,null,null,null,null);

               GraphRequest request = GraphRequest.newGraphPathRequest(
                       lv,
                       "/"+ InstaPost.getUrl_id().toString()+"/recent_media",
                       new GraphRequest.Callback() {
                           @Override
                           public void onCompleted(GraphResponse response) {

                              // Log.d(TAG, "Recent media: ---------" + response.toString());

                               try {
                                   JSONArray jsonMediaUrlArray = response.getJSONObject().getJSONArray("data");

                                   for(int i = 0; i<jsonMediaUrlArray.length(); i++){
                                       JSONObject hashtag_url = jsonMediaUrlArray.getJSONObject(i);

                                       InstaPostIds InstaPostMediaUrl = new InstaPostIds();

                                       if(hashtag_url.has("media_url")) {

                                           String media_url = hashtag_url.getString("media_url");
                                           InstaPostMediaUrl.setMedia_url(media_url);
                                           //Log.d(TAG," MEDIA ----------" + InstaPostMediaUrl.getMedia_url().toString());
                                       }

                                       InstagramIdsList.add(InstaPostMediaUrl);
                                   }

                                   for( InstaPostIds instaPost : InstagramIdsList ) {
                                       Log.d(TAG, "ids------------------------------"+InstagramIdsList.toString());
                                   }

                                    Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
                                    intent.putParcelableArrayListExtra("InstagramListOf_Urls", (ArrayList<? extends Parcelable>) InstagramIdsList);
                                    startActivity(intent);

                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                           }
                       });

               Bundle parameters = new Bundle();
               parameters.putString("user_id", "17841445397955029");
               parameters.putString("fields", "media_url");
               request.setParameters(parameters);
               request.executeAsync();

           }
       });



       if( requestCode == 102){
           Uri imageUri = data.getData();
           Intent uploadImage = new Intent(Intent.ACTION_SEND);
           uploadImage.setType("image/jpg");
           uploadImage.putExtra(Intent.EXTRA_STREAM, imageUri);
           startActivity(Intent.createChooser(uploadImage, "Share image using"));
       }
    }


    public void checkPermission(){
        int hadImagePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if(hadImagePermission == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "permission check says is already granted!");
            PICK_IMAGES_PERM_GRANTED = true;
        }
        else{
            Log.d(TAG, "Permission does not exist. Requesting now...");     // αν δεν υπαρχει στην μνημη που τσεκαρουμε ζηταμε το αντιστοιχο permission

            String[] permissionToAsk = new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE};  //φτιαχνω μονοδιαστατο πινακα και βαζω στην 1η θεση αυτο το permission(READ_EXTERNAL_STORAGE)

            ActivityCompat.requestPermissions(MainActivity.this, permissionToAsk, REQUEST_CODE_IMAGES);   //αυτη η εντολη κανει  pop-up το παραθυρο που μας ζηταει το  permission που χρειαζεται το  app
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case REQUEST_CODE_IMAGES:{
                //if request denied, the result arrays are empty.
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, "Permission granted :)");
                    PICK_IMAGES_PERM_GRANTED =true;

                }
                else {
                    Log.d(TAG, "Permission denied :(");
                }

            }

            //if other permissions requests exist continue here
        }

    }


}
