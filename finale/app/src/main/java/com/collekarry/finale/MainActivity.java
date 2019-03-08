package com.collekarry.finale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    SignInButton signInButton;
    FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 2221 ;
    GoogleApiClient mGoogleSignInClient;
    FirebaseAuth.AuthStateListener mAuthListner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);


//        startActivity(new Intent(MainActivity.this, listOfPeople.class));


        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        mAuth = FirebaseAuth.getInstance();

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() !=  null){
                    Toast.makeText(MainActivity.this, "Logged in as "+ mAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                    ProgressDialog pd = ProgressDialog.show(MainActivity.this, "Loading...","Please wait.", true);
                    String uid = firebaseAuth.getUid();
                    final DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("Users").child("Caretakers")
                            .child(uid);
                    System.out.println(uid);
                    df.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if( !dataSnapshot.hasChildren() ){

                                Map<String, String> careTaker = new HashMap<>();
                                careTaker.put("name", firebaseAuth.getCurrentUser().getDisplayName());
                                careTaker.put("email", firebaseAuth.getCurrentUser().getEmail());
                                careTaker.put("phone", firebaseAuth.getCurrentUser().getPhoneNumber());
                                System.out.println(careTaker.toString());
                                df.setValue(careTaker).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        startActivity(new Intent(MainActivity.this,listOfPeople.class));
                                    }
                                });
                            }
                            else{
                                startActivity(new Intent(MainActivity.this,listOfPeople.class));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    //signIn();
                }
                else
                {
                    //Toast.makeText(MainActivity.this, "Failed u idiot", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("7588545672-mlsp6bkubrk5c902bnqk81819bblv973.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this , new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(MainActivity.this, "Failed Man This Shit is failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

            signInButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

    }

    private void signIn()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("request/", Integer.toString(requestCode));
        Log.i("request/rc", Integer.toString(RC_SIGN_IN));
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN)
        {
            Log.i("request/", "Insideeee");

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                Log.i("inside","awdawd");
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                        String uid = firebaseAuth.getUid();
                        Log.i("insideNEW","awdawd");
                        final DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("Users").child("Caretakers")
                                .child(uid);
                        System.out.println(uid);
                        df.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(!dataSnapshot.exists()){

                                    Map<String, String> careTaker = new HashMap<>();
                                    careTaker.put("name", firebaseAuth.getCurrentUser().getDisplayName());
                                    careTaker.put("email", firebaseAuth.getCurrentUser().getEmail());
                                    careTaker.put("phone", firebaseAuth.getCurrentUser().getPhoneNumber());
                                    System.out.println(careTaker.toString());
                                    df.setValue(careTaker).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            startActivity(new Intent(MainActivity.this,listOfPeople.class));
                                        }
                                    });
                                }
                                else{
                                    startActivity(new Intent(MainActivity.this,listOfPeople.class));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                    }
                };

                Log.i("awdawd","awdawdwadwadawdwad");
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.i("request/rc", String.valueOf(e));

                // ...
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthListner);

    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_LONG).show();
//                            updateUI(null);
                        }
                    }
                });
    }

    public void signOut() {
        mGoogleSignInClient.disconnect();

    }
}
