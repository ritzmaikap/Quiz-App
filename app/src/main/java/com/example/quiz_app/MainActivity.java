package com.example.quiz_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/*
 * this is the first screen of the app.
 * it collects the user's name and stores it in sharedpreferences
 * so the same name can be reused later without typing again.
 */
public class MainActivity extends AppCompatActivity {

    private EditText edtName;
    private Button btnStartQuiz;

    private SharedPreferences preferences;

    public static final String PREF_FILE = "QuizAppPrefs";
    public static final String KEY_USER_NAME = "user_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtName = findViewById(R.id.edtName);
        btnStartQuiz = findViewById(R.id.btnStartQuiz);

        preferences = getSharedPreferences(PREF_FILE, MODE_PRIVATE);

        /*
         * this is restoring the previously saved name
         * so when user comes back for a new quiz, the name is already filled in.
         */
        String savedName = preferences.getString(KEY_USER_NAME, "");
        edtName.setText(savedName);

        btnStartQuiz.setOnClickListener(v -> {
            String enteredName = edtName.getText().toString().trim();

            if (enteredName.isEmpty()) {
                edtName.setError("Please enter your name");
                return;
            }

            /*
             * this stores the name locally for session persistence.
             */
            preferences.edit().putString(KEY_USER_NAME, enteredName).apply();

            Intent intent = new Intent(MainActivity.this, PlayScreen.class);
            intent.putExtra("USER_NAME", enteredName);
            startActivity(intent);
        });
    }
}