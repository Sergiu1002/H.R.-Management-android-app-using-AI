package com.example.hr_management_ai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
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
    private FirebaseAuth mAuth; // Autentificarea Firebase
    private static final String TAG = "MainActivity"; // Etichetă pentru utilizare în logare

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ascunde bara de titlu și afișează activitatea în modul ecran complet
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main); // Setează aspectul pentru această activitate

        mAuth = FirebaseAuth.getInstance(); // Inițializează autentificarea Firebase

        // Inițializarea elementelor de interfață
        Button RecoverAccount = findViewById(R.id.buttonRecoverAccount);
        Button SignIn = findViewById(R.id.buttonSignIn);
        EditText EmailField = findViewById(R.id.editTextTextEmailAddress);
        EditText PasswordField = findViewById(R.id.editTextTextPassword);

        // Acțiunea de autentificare atunci când butonul "Sign In" este apăsat
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
                                startActivity(intent); // Navighează la activitatea feed_page
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

        // Acțiunea de recuperare a contului atunci când butonul "Recover Account" este apăsat
        RecoverAccount.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Recover_account.class);
                startActivity(intent); // Navighează la activitatea Recover_account
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser(); // Obține utilizatorul curent autentificat
    }
}
