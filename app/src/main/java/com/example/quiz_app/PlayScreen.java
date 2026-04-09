package com.example.quiz_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/*
 * this activity is handling:
 * - loading quiz questions
 * - selecting one option
 * - submitting one answer
 * - showing correct and wrong colors
 * - stopping answer changes after submission
 * - moving to the next question
 * - sending final score to the result screen
 */
public class PlayScreen extends AppCompatActivity {

    private TextView txtProgressCount;
    private TextView txtQuestionTitle;
    private TextView txtQuestionDetail;
    private ProgressBar progressQuiz;

    private Button btnOption1;
    private Button btnOption2;
    private Button btnOption3;
    private Button btnAction;

    private ArrayList<QuizItem> quizList;

    private int currentIndex = 0;
    private int selectedOption = 0;
    private int score = 0;

    private boolean isSubmitted = false;

    private String userName = "";

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

        /*
         * this is receiving the user name from the home screen.
         * later this name will also be passed to the score screen.
         */
        userName = getIntent().getStringExtra("USER_NAME");
        if (userName == null) {
            userName = "";
        }

        buildQuestions();
        showQuestion();

        /*
         * each option button is only selecting the answer.
         * it is not checking correctness yet.
         * correctness is checked only after clicking submit.
         */
        btnOption1.setOnClickListener(v -> selectOption(1));
        btnOption2.setOnClickListener(v -> selectOption(2));
        btnOption3.setOnClickListener(v -> selectOption(3));

        /*
         * this action button works in two stages:
         * - before submit: it validates and checks the answer
         * - after submit: it goes to the next question
         */
        btnAction.setOnClickListener(v -> {
            if (!isSubmitted) {
                submitAnswer();
            } else {
                goToNextQuestion();
            }
        });
    }

    /*
     * this method creates the quiz data manually.
     * later, if needed, this can also come from a database or json file.
     */
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

    /*
     * this method shows the current question and resets the screen state.
     * every time a new question opens, selection and colors are reset.
     */
    private void showQuestion() {
        QuizItem currentQuestion = quizList.get(currentIndex);

        txtQuestionTitle.setText(currentQuestion.getTitle());
        txtQuestionDetail.setText(currentQuestion.getDetail());

        btnOption1.setText(currentQuestion.getOption1());
        btnOption2.setText(currentQuestion.getOption2());
        btnOption3.setText(currentQuestion.getOption3());

        txtProgressCount.setText((currentIndex + 1) + "/" + quizList.size());

        /*
         * this is converting question position into percentage for the progress bar.
         */
        int progressValue = (int) (((currentIndex + 1) / (float) quizList.size()) * 100);
        progressQuiz.setProgress(progressValue);

        selectedOption = 0;
        isSubmitted = false;

        btnAction.setText("Submit");

        resetOptionStyles();
        enableOptionButtons(true);
    }

    /*
     * this method visually marks the selected option.
     * only one option stays selected at a time.
     * after submission, changing answer is blocked.
     */
    private void selectOption(int optionNumber) {
        if (isSubmitted) {
            return;
        }

        selectedOption = optionNumber;

        resetOptionStyles();

        if (optionNumber == 1) {
            btnOption1.setAlpha(0.7f);
        } else if (optionNumber == 2) {
            btnOption2.setAlpha(0.7f);
        } else if (optionNumber == 3) {
            btnOption3.setAlpha(0.7f);
        }
    }

    /*
     * this method restores all options to their normal background.
     */
    private void resetOptionStyles() {
        btnOption1.setBackgroundResource(R.drawable.bg_option_idle);
        btnOption2.setBackgroundResource(R.drawable.bg_option_idle);
        btnOption3.setBackgroundResource(R.drawable.bg_option_idle);

        btnOption1.setAlpha(1.0f);
        btnOption2.setAlpha(1.0f);
        btnOption3.setAlpha(1.0f);
    }

    /*
     * this method checks the selected answer against the correct answer.
     * - correct answer becomes green
     * - wrong selected answer becomes red
     * - user cannot change answer anymore
     */
    private void submitAnswer() {
        if (selectedOption == 0) {
            /*
             * this is a simple validation so user must pick one answer first.
             */
            txtQuestionDetail.setError("Please select an answer first");
            return;
        }

        txtQuestionDetail.setError(null);

        isSubmitted = true;

        QuizItem currentQuestion = quizList.get(currentIndex);
        int correctOption = currentQuestion.getCorrectOption();

        /*
         * first, the correct answer is always shown in green.
         */
        if (correctOption == 1) {
            btnOption1.setBackgroundResource(R.drawable.bg_option_correct);
        } else if (correctOption == 2) {
            btnOption2.setBackgroundResource(R.drawable.bg_option_correct);
        } else if (correctOption == 3) {
            btnOption3.setBackgroundResource(R.drawable.bg_option_correct);
        }

        /*
         * if selected answer is wrong, that selected option is shown in red.
         * if selected answer is correct, score is increased.
         */
        if (selectedOption == correctOption) {
            score++;
        } else {
            if (selectedOption == 1) {
                btnOption1.setBackgroundResource(R.drawable.bg_option_wrong);
            } else if (selectedOption == 2) {
                btnOption2.setBackgroundResource(R.drawable.bg_option_wrong);
            } else if (selectedOption == 3) {
                btnOption3.setBackgroundResource(R.drawable.bg_option_wrong);
            }

            /*
             * after showing wrong color, the correct answer is shown green again
             * so that it does not get replaced accidentally.
             */
            if (correctOption == 1) {
                btnOption1.setBackgroundResource(R.drawable.bg_option_correct);
            } else if (correctOption == 2) {
                btnOption2.setBackgroundResource(R.drawable.bg_option_correct);
            } else if (correctOption == 3) {
                btnOption3.setBackgroundResource(R.drawable.bg_option_correct);
            }
        }

        /*
         * option buttons are disabled after submission so answer cannot be changed.
         */
        enableOptionButtons(false);

        /*
         * button text changes from submit to next.
         * on final question, it becomes finish.
         */
        if (currentIndex == quizList.size() - 1) {
            btnAction.setText("Finish");
        } else {
            btnAction.setText("Next");
        }
    }

    /*
     * this method enables or disables the three answer buttons together.
     */
    private void enableOptionButtons(boolean enabled) {
        btnOption1.setEnabled(enabled);
        btnOption2.setEnabled(enabled);
        btnOption3.setEnabled(enabled);
    }

    /*
     * this method moves to the next question,
     * or opens the score screen after the final question.
     */
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
}