package com.example.quiz_app;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.util.ArrayList;

public class PlayScreen extends AppCompatActivity {

    private TextView txtProgressCount;
    private TextView txtQuestionTitle;
    private TextView txtQuestionDetail;
    private ProgressBar progressQuiz;

    private Button btnOption1;
    private Button btnOption2;
    private Button btnOption3;
    private Button btnAction;

    private SwitchCompat switchTheme;

    private ArrayList<QuizItem> quizList;

    private int currentIndex = 0;
    private int selectedOption = 0;
    private int score = 0;
    private boolean isSubmitted = false;
    private String userName = "";

    private final int COLOR_IDLE = Color.parseColor("#6750A4");
    private final int COLOR_SELECTED = Color.parseColor("#7E57C2");
    private final int COLOR_CORRECT = Color.parseColor("#4CAF50");
    private final int COLOR_WRONG = Color.parseColor("#F44336");
    private final int COLOR_TEXT = Color.WHITE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_play);

        txtProgressCount = findViewById(R.id.txtProgressCount);
        txtQuestionTitle = findViewById(R.id.txtQuestionTitle);
        txtQuestionDetail = findViewById(R.id.txtQuestionDetail);
        progressQuiz = findViewById(R.id.progressQuiz);

        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);
        btnAction = findViewById(R.id.btnAction);
        switchTheme = findViewById(R.id.switchTheme);

        if (getIntent() != null) {
            userName = getIntent().getStringExtra("USER_NAME");
            if (userName == null) {
                userName = "";
            }
        }

        switchTheme.setChecked(ThemeHelper.isDarkMode(this));
        applyThemeToCurrentScreen(ThemeHelper.isDarkMode(this));

        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ThemeHelper.saveTheme(PlayScreen.this, isChecked);
            applyThemeToCurrentScreen(isChecked);
        });

        buildQuestions();
        showQuestion();

        btnOption1.setOnClickListener(v -> selectOption(1));
        btnOption2.setOnClickListener(v -> selectOption(2));
        btnOption3.setOnClickListener(v -> selectOption(3));

        btnAction.setOnClickListener(v -> {
            if (!isSubmitted) {
                submitAnswer();
            } else {
                goToNextQuestion();
            }
        });
    }

    private void buildQuestions() {
        quizList = new ArrayList<>();

        quizList.add(new QuizItem(
                "Android Basics",
                "Which file is mainly used for designing the layout of an Android screen?",
                "Java file",
                "XML file",
                "Gradle file",
                2
        ));

        quizList.add(new QuizItem(
                "Java Concept",
                "Which keyword is used to create an object in Java?",
                "new",
                "class",
                "void",
                1
        ));

        quizList.add(new QuizItem(
                "Android Studio",
                "Which folder usually stores drawable resources?",
                "java",
                "manifests",
                "drawable",
                3
        ));

        quizList.add(new QuizItem(
                "UI Components",
                "Which widget is commonly used to display clickable text options?",
                "Button",
                "Intent",
                "Manifest",
                1
        ));

        quizList.add(new QuizItem(
                "Quiz Logic",
                "What should happen after the final quiz question is completed?",
                "App should freeze",
                "Result screen should open",
                "Question should repeat",
                2
        ));
    }

    private void showQuestion() {
        QuizItem currentQuestion = quizList.get(currentIndex);

        txtQuestionTitle.setText(currentQuestion.getTitle());
        txtQuestionDetail.setText(currentQuestion.getDetail());

        btnOption1.setText(currentQuestion.getOption1());
        btnOption2.setText(currentQuestion.getOption2());
        btnOption3.setText(currentQuestion.getOption3());

        txtProgressCount.setText((currentIndex + 1) + "/" + quizList.size());

        int progressValue = (int) (((currentIndex + 1) / (float) quizList.size()) * 100);
        progressQuiz.setProgress(progressValue);

        selectedOption = 0;
        isSubmitted = false;
        btnAction.setText("Submit");

        resetOptionStyles();
        applyThemeToCurrentScreen(ThemeHelper.isDarkMode(this));
    }

    private void selectOption(int optionNumber) {
        if (isSubmitted) {
            return;
        }

        selectedOption = optionNumber;
        resetOptionStyles();

        if (optionNumber == 1) {
            styleSelectedButton(btnOption1);
        } else if (optionNumber == 2) {
            styleSelectedButton(btnOption2);
        } else if (optionNumber == 3) {
            styleSelectedButton(btnOption3);
        }
    }

    private void resetOptionStyles() {
        styleIdleButton(btnOption1);
        styleIdleButton(btnOption2);
        styleIdleButton(btnOption3);
    }

    private void styleIdleButton(Button button) {
        button.setBackgroundTintList(null);
        button.setBackgroundColor(COLOR_IDLE);
        button.setTextColor(COLOR_TEXT);
        button.setAlpha(1.0f);
    }

    private void styleSelectedButton(Button button) {
        button.setBackgroundTintList(null);
        button.setBackgroundColor(COLOR_SELECTED);
        button.setTextColor(COLOR_TEXT);
        button.setAlpha(1.0f);
    }

    private void styleCorrectButton(Button button) {
        button.setBackgroundTintList(null);
        button.setBackgroundColor(COLOR_CORRECT);
        button.setTextColor(COLOR_TEXT);
        button.setAlpha(1.0f);
    }

    private void styleWrongButton(Button button) {
        button.setBackgroundTintList(null);
        button.setBackgroundColor(COLOR_WRONG);
        button.setTextColor(COLOR_TEXT);
        button.setAlpha(1.0f);
    }

    private void submitAnswer() {
        if (selectedOption == 0) {
            txtQuestionDetail.setError("Please select an answer first");
            return;
        }

        txtQuestionDetail.setError(null);
        isSubmitted = true;

        QuizItem currentQuestion = quizList.get(currentIndex);
        int correctOption = currentQuestion.getCorrectOption();

        if (correctOption == 1) {
            styleCorrectButton(btnOption1);
        } else if (correctOption == 2) {
            styleCorrectButton(btnOption2);
        } else if (correctOption == 3) {
            styleCorrectButton(btnOption3);
        }

        if (selectedOption == correctOption) {
            score++;
        } else {
            if (selectedOption == 1) {
                styleWrongButton(btnOption1);
            } else if (selectedOption == 2) {
                styleWrongButton(btnOption2);
            } else if (selectedOption == 3) {
                styleWrongButton(btnOption3);
            }

            if (correctOption == 1) {
                styleCorrectButton(btnOption1);
            } else if (correctOption == 2) {
                styleCorrectButton(btnOption2);
            } else if (correctOption == 3) {
                styleCorrectButton(btnOption3);
            }
        }

        if (currentIndex == quizList.size() - 1) {
            btnAction.setText("Finish");
        } else {
            btnAction.setText("Next");
        }
    }

    private void goToNextQuestion() {
        if (currentIndex < quizList.size() - 1) {
            currentIndex++;
            showQuestion();
        } else {
            Intent intent = new Intent(PlayScreen.this, ScoreScreen.class);
            intent.putExtra("USER_NAME", userName);
            intent.putExtra("FINAL_SCORE", score);
            intent.putExtra("TOTAL_QUESTIONS", quizList.size());
            startActivity(intent);
            finish();
        }
    }

    private void applyThemeToCurrentScreen(boolean isDark) {
        if (isDark) {
            findViewById(android.R.id.content).setBackgroundColor(Color.parseColor("#121212"));

            txtProgressCount.setTextColor(Color.WHITE);
            txtQuestionTitle.setTextColor(Color.WHITE);
            txtQuestionDetail.setTextColor(Color.WHITE);

            progressQuiz.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#BB86FC")));
        } else {
            findViewById(android.R.id.content).setBackgroundColor(Color.parseColor("#FFF8E7"));

            txtProgressCount.setTextColor(Color.parseColor("#1C1B1F"));
            txtQuestionTitle.setTextColor(Color.parseColor("#1C1B1F"));
            txtQuestionDetail.setTextColor(Color.parseColor("#1C1B1F"));

            progressQuiz.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#6750A4")));
        }
    }
}