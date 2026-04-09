package com.example.quiz_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

/*
 * this is the first screen of the app.
 * it collects the user's name and stores it in sharedpreferences
 * so the same name can be reused later without typing again.
 */
public class MainActivity extends AppCompatActivity {

    private EditText edtName;
    private Button btnStartQuiz;
    private SwitchCompat switchTheme;

    private SharedPreferences preferences;

    public static final String PREF_FILE = "QuizAppPrefs";
    public static final String KEY_USER_NAME = "user_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtName = findViewById(R.id.edtName);
        btnStartQuiz = findViewById(R.id.btnStartQuiz);
        switchTheme = findViewById(R.id.switchTheme);

        preferences = getSharedPreferences(PREF_FILE, MODE_PRIVATE);

        String savedName = preferences.getString(KEY_USER_NAME, "");
        edtName.setText(savedName);

        // theme setup
        switchTheme.setChecked(ThemeHelper.isDarkMode(this));
        applyThemeToMainScreen(ThemeHelper.isDarkMode(this));

        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ThemeHelper.saveTheme(MainActivity.this, isChecked);
            applyThemeToMainScreen(isChecked);
        });

        btnStartQuiz.setOnClickListener(v -> {
            String enteredName = edtName.getText().toString().trim();

            if (enteredName.isEmpty()) {
                edtName.setError("Please enter your name");
                return;
            }

            preferences.edit().putString(KEY_USER_NAME, enteredName).apply();

            Intent intent = new Intent(MainActivity.this, PlayScreen.class);
            intent.putExtra("USER_NAME", enteredName);
            startActivity(intent);
        });
    }

    private void applyThemeToMainScreen(boolean isDark) {
        if (isDark) {
            findViewById(android.R.id.content).setBackgroundColor(Color.parseColor("#121212"));

            edtName.setTextColor(Color.BLACK);
            edtName.setHintTextColor(Color.GRAY);

            btnStartQuiz.setTextColor(Color.WHITE);
            btnStartQuiz.setBackgroundColor(Color.parseColor("#6750A4"));
        } else {
            findViewById(android.R.id.content).setBackgroundColor(Color.parseColor("#FFF8E7"));

            edtName.setTextColor(Color.parseColor("#1C1B1F"));
            edtName.setHintTextColor(Color.GRAY);

            btnStartQuiz.setTextColor(Color.WHITE);
            btnStartQuiz.setBackgroundColor(Color.parseColor("#6750A4"));
        }
    }
}