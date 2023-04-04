package com.example.hr_management_ai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Recover_account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_account);


        Button RecoverAccount = (Button) findViewById(R.id.buttonRecoverAccount2);

        RecoverAccount.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // Create an intent to start the second activity
                Intent intent = new Intent(Recover_account.this, MainActivity.class);

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
