package com.example.quiz_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private TextView tvCongrats, tvScore;
    private Button btnTakeNewQuiz, btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvCongrats = findViewById(R.id.tvCongrats);
        tvScore = findViewById(R.id.tvScore);
        btnTakeNewQuiz = findViewById(R.id.btnTakeNewQuiz);
        btnFinish = findViewById(R.id.btnFinish);

        // receiving values passed from quiz screen
        String userName = getIntent().getStringExtra("userName");
        int score = getIntent().getIntExtra("score", 0);
        int totalQuestions = getIntent().getIntExtra("totalQuestions", 0);

        if (userName == null || userName.isEmpty()) {
            userName = "User";
        }

        // showing personalized congratulation text and final score
        tvCongrats.setText("Congratulations " + userName + "!");
        tvScore.setText(score + "/" + totalQuestions);

        btnTakeNewQuiz.setOnClickListener(v -> {
            // returning to main screen
            // username remains visible because it was already saved in SharedPreferences
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        btnFinish.setOnClickListener(v -> {
            // closing the app completely
            finishAffinity();
        });
    }
}