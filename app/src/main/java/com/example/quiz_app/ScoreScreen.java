package com.example.quiz_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

/*
 * this activity receives the final score and user name
 * and displays them on the result screen.
 */
public class ScoreScreen extends AppCompatActivity {

    private TextView txtCongratulations;
    private TextView txtFinalScore;
    private Button btnTakeNewQuiz;
    private Button btnFinishApp;
    private SwitchCompat switchTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_score);

        txtCongratulations = findViewById(R.id.txtCongratulations);
        txtFinalScore = findViewById(R.id.txtFinalScore);
        btnTakeNewQuiz = findViewById(R.id.btnTakeNewQuiz);
        btnFinishApp = findViewById(R.id.btnFinishApp);
        switchTheme = findViewById(R.id.switchTheme);

        String userName = getIntent().getStringExtra("USER_NAME");
        int finalScore = getIntent().getIntExtra("FINAL_SCORE", 0);
        int totalQuestions = getIntent().getIntExtra("TOTAL_QUESTIONS", 5);

        if (userName == null) {
            userName = "";
        }

        txtCongratulations.setText("Congratulations " + userName + "!");
        txtFinalScore.setText(finalScore + "/" + totalQuestions);

        switchTheme.setChecked(ThemeHelper.isDarkMode(this));
        applyThemeToScoreScreen(ThemeHelper.isDarkMode(this));

        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ThemeHelper.saveTheme(ScoreScreen.this, isChecked);
            applyThemeToScoreScreen(isChecked);
        });

        /*
         * this opens the main screen again.
         * the name remains there because it was already saved in sharedpreferences.
         */
        btnTakeNewQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(ScoreScreen.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        /*
         * this closes the full app task.
         * for student assignment purposes, this is acceptable.
         */
        btnFinishApp.setOnClickListener(v -> {
            finishAffinity();
        });
    }

    private void applyThemeToScoreScreen(boolean isDark) {
        if (isDark) {
            findViewById(android.R.id.content).setBackgroundColor(Color.parseColor("#121212"));

            txtCongratulations.setTextColor(Color.WHITE);
            txtFinalScore.setTextColor(Color.WHITE);

            btnTakeNewQuiz.setTextColor(Color.WHITE);
            btnFinishApp.setTextColor(Color.WHITE);

            btnTakeNewQuiz.setBackgroundColor(Color.parseColor("#6750A4"));
            btnFinishApp.setBackgroundColor(Color.parseColor("#6750A4"));
        } else {
            findViewById(android.R.id.content).setBackgroundColor(Color.parseColor("#FFF8E7"));

            txtCongratulations.setTextColor(Color.parseColor("#1C1B1F"));
            txtFinalScore.setTextColor(Color.parseColor("#1C1B1F"));

            btnTakeNewQuiz.setTextColor(Color.WHITE);
            btnFinishApp.setTextColor(Color.WHITE);

            btnTakeNewQuiz.setBackgroundColor(Color.parseColor("#6750A4"));
            btnFinishApp.setBackgroundColor(Color.parseColor("#6750A4"));
        }
    }
}