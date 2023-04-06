package com.example.hr_management_ai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Button RecoverAccount = (Button) findViewById(R.id.buttonRecoverAccount);

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
}