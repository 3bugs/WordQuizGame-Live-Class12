package com.example.wordquizgame;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = GameActivity.class.getSimpleName();

    private int mDifficulty;
    private int mNumChoices;

    private ArrayList<String> mFileNameList = new ArrayList<>();
    private ArrayList<String> mQuizWordList = new ArrayList<>();
    private ArrayList<String> mChoiceWordList = new ArrayList<>();

    private String mAnswerFileName;
    private int mTotalGuesses;
    private int mScore;

    private Random mRandom = new Random();
    private Handler mHandler = new Handler();

    private TextView mQuestionNumberTextView;
    private ImageView mQuestionImageView;
    private TextView mAnswerTextView;
    private TableLayout mButtonTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setupViews();

        Intent i = getIntent();
        mDifficulty = i.getIntExtra("diff", 0);

        switch (mDifficulty) {
            case 0:
                mNumChoices = 2;
                break;
            case 1:
                mNumChoices = 4;
                break;
            case 2:
                mNumChoices = 6;
                break;
        }

        getImageFileNames();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putStringArrayList("QuizWordList", mQuizWordList);
    }

    private void setupViews() {
        mQuestionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
        mQuestionImageView = (ImageView) findViewById(R.id.questionImageView);
        mAnswerTextView = (TextView) findViewById(R.id.answerTextView);
        mButtonTableLayout = (TableLayout) findViewById(R.id.buttonTableLayout);
    }

    private void getImageFileNames() {
        String[] categories = new String[]{"animals", "body", "colors", "numbers", "objects"};

        AssetManager am = getAssets();
        for (String c : categories) {
            try {
                String[] fileNames = am.list(c);

                for (String f : fileNames) {
                    mFileNameList.add(f.replace(".png", ""));
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Error listing file name in " + c);
            }
        }

        Log.i(TAG, "************************");
        Log.i(TAG, "รายชื่อไฟล์ภาพทั้งหมด");
        for (String f : mFileNameList) {
            Log.i(TAG, f);
        }
        Log.i(TAG, "************************");

        startQuiz();
    }

    private void startQuiz() {
        mScore = 0;
        mTotalGuesses = 0;
        mQuizWordList.clear();

        while (mQuizWordList.size() < 3) {
            int randomIndex = mRandom.nextInt(mFileNameList.size());
            String fileName = mFileNameList.get(randomIndex);

            if (mQuizWordList.contains(fileName) == false) {
                mQuizWordList.add(fileName);
            }
        }

        Log.i(TAG, "************************");
        Log.i(TAG, "รายชื่อไฟล์คำถามที่สุ่มได้");
        for (String f : mQuizWordList) {
            Log.i(TAG, f);
        }
        Log.i(TAG, "************************");

        loadNextQuestion();
    }

    private void loadNextQuestion() {
        mAnswerTextView.setText(null);
        mAnswerFileName = mQuizWordList.remove(0);

        String msg = String.format("คำถาม %d จาก 3", mScore + 1);
        mQuestionNumberTextView.setText(msg);

        Log.i(TAG, "************************");
        Log.i(TAG, "ชื่อไฟล์รูปภาพคำถามคือ " + mAnswerFileName);
        Log.i(TAG, "************************");

        loadQuestionImage();
        prepareChoiceWords();
    }

    private void loadQuestionImage() {
        String category = mAnswerFileName.substring(
                0,
                mAnswerFileName.indexOf('-')
        );

        String filePath = category + "/" + mAnswerFileName + ".png";

        AssetManager am = getAssets();
        InputStream stream;

        try {
            stream = am.open(filePath);
            Drawable image = Drawable.createFromStream(stream, filePath);
            mQuestionImageView.setImageDrawable(image);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error loading image file: " + filePath);
        }
    }

    private void prepareChoiceWords() {
        mChoiceWordList.clear();
        String answerWord = getWord(mAnswerFileName);

/*
        int i = 0;
        Collections.shuffle(mFileNameList);
        while (mChoiceWordList.size() < mNumChoices) {
            String word = mFileNameList.get(i);
            if (mChoiceWordList.contains(word) == false) {
                mChoiceWordList.add(word);
            }
            i++;
        }
*/

        while (mChoiceWordList.size() < mNumChoices) {
            int randomIndex = mRandom.nextInt(mFileNameList.size());
            String randomWord = getWord(mFileNameList.get(randomIndex));

            if (mChoiceWordList.contains(randomWord) == false &&
                    answerWord.equals(randomWord) == false) {
                mChoiceWordList.add(randomWord);
            }
        }

        int randomIndex = mRandom.nextInt(mChoiceWordList.size());
        mChoiceWordList.set(randomIndex, answerWord);

        Log.i(TAG, "************************");
        Log.i(TAG, "คำศัพท์ตัวเลือกที่สุ่มได้");
        for (String w : mChoiceWordList) {
            Log.i(TAG, w);
        }
        Log.i(TAG, "************************");

        createChoiceButtons();
    }

    private String getWord(String fileName) {
        return fileName.substring(fileName.indexOf('-') + 1);
    }

    private void createChoiceButtons() {
        for (int row = 0; row < mButtonTableLayout.getChildCount(); row++) {
            TableRow tr = (TableRow) mButtonTableLayout.getChildAt(row);
            tr.removeAllViews();
        }

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int row = 0; row < mNumChoices / 2; row++) {
            TableRow tr = (TableRow) mButtonTableLayout.getChildAt(row);

            for (int column = 0; column < 2; column++) {
                Button guessButton = (Button) inflater.inflate(R.layout.guess_button, tr, false);
                guessButton.setText(mChoiceWordList.remove(0));
                tr.addView(guessButton);
            }
        }

    }
}














