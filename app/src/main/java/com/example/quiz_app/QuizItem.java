package com.example.quiz_app;

/*
 * this class is storing one question object.
 * each object contains:
 * - question title
 * - question description
 * - three answer options
 * - index of the correct answer
 */
public class QuizItem {

    private String title;
    private String detail;
    private String option1;
    private String option2;
    private String option3;
    private int correctOption; // 1, 2, or 3

    public QuizItem(String title, String detail, String option1, String option2, String option3, int correctOption) {
        this.title = title;
        this.detail = detail;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.correctOption = correctOption;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public int getCorrectOption() {
        return correctOption;
    }
}