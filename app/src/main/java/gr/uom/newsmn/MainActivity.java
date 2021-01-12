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

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Rest App";

    String pg_token = "EAADE9NOJtpMBAFPG2SVZAu3zNxvuvynpo8Hy1SZASkfh6ZAEazqMZALH4BPV6Y8WcVPFNpUM7ZA1DyVvUjg5rPLeARmJ3SWMyIf9q98p42PZCfw5jCZBLHzqeDdDxqQS2d6lcCi8DLSjmrgzaMzGZAZAMRkWLSUAv4xVyarVnJ8PpLGZBlXzkgem9kTY5tSN4QZAY7D66pPiER8mtpzocCxFQfz";

    private static boolean PICK_IMAGES_PERM_GRANTED = false;
    private static final int REQUEST_CODE_IMAGES =102;

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

       if( requestCode!= -1){
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

            String[] permissionToAsk = new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE};  //φτιαχνω μονοδιαστατο πινακα και βαζω στην 1η θεση αυτο το permission(READ_CONTACS)

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
