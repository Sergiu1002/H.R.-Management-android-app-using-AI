package com.example.hr_management_ai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        Button RecoverAccount = (Button) findViewById(R.id.buttonRecoverAccount);
        Button SignIn = (Button) findViewById(R.id.buttonSignIn);
        EditText EmailField = (EditText) findViewById(R.id.editTextTextEmailAddress);
        EditText PasswordField = (EditText) findViewById(R.id.editTextTextPassword);


        SignIn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String email = EmailField.getText().toString();
                String password = PasswordField.getText().toString();
                if(!email.equals("") && !password.equals("")){
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Signed in successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, feed_page.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(MainActivity.this, "Error while signing in!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "Empty credential fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RecoverAccount.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // Create an intent to start the second activity
                Intent intent = new Intent(MainActivity.this, Recover_account.class);

                // Add any data you want to save as extras to the intent
//                EditText editText = findViewById(R.id.editText);
//                String text = editText.getText().toString();
//                intent.putExtra("data", text);

                // Start the second activity
                startActivity(intent);

            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
    /*
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthListener(mAuthListener);
        }
    }*/
}