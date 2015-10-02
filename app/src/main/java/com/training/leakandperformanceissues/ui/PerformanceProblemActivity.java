package com.training.leakandperformanceissues.ui;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.training.leakandperformanceissues.R;
import com.training.leakandperformanceissues.database.Provider;
import com.training.leakandperformanceissues.database.table.WondersTable;

public class PerformanceProblemActivity extends AppCompatActivity {

    private Button btnStartLongOperation;
    private TextView textViewStatus;

    private Button btnStartAnotherOperation;
    private TextView textViewAnotherOperation;

    private ContentResolver resolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);

        resolver = getContentResolver();

        loadUI();
        setListeners();
    }

    private void loadUI() {
        btnStartLongOperation = (Button) findViewById(R.id.btn_execute_long_operation);
        textViewStatus = (TextView) findViewById(R.id.textView_status);

        btnStartAnotherOperation = (Button) findViewById(R.id.btn_execute_another_operation);
        textViewAnotherOperation = (TextView) findViewById(R.id.textView_another_operation);
    }

    private void setListeners() {
        btnStartLongOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final long startTime = System.currentTimeMillis();

                AsyncTask<Void, Integer, Integer> insertTask = new AsyncTask<Void, Integer, Integer>() {
                    @Override
                    protected void onPreExecute() {
                        textViewStatus.setVisibility(View.VISIBLE);
                        textViewStatus.setText("Executing...");
                    }

                    @Override
                    protected Integer doInBackground(Void... voids) {
                        return performInsert();
                    }

                    @Override
                    protected void onPostExecute(Integer linesCount) {
                        long endTime = System.currentTimeMillis();
                        long spentTime = (endTime - startTime) / 1000;
                        textViewStatus.setText("Done! inserted " + linesCount + " items. Time spent: " + spentTime + " secs.");
                    }
                };

                insertTask.execute();
            }
        });

        btnStartAnotherOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewAnotherOperation.setVisibility(View.VISIBLE);
                new CountDownTimer(3000, 1) {
                    @Override
                    public void onTick(long millisUntilFinished) {}

                    @Override
                    public void onFinish() {
                        textViewAnotherOperation.setVisibility(View.INVISIBLE);
                    }
                }.start();
            }
        });
    }

    private int performInsert() {
        int numberOfInsertions = 1000;

        ContentValues[] linesToBeInserted = new ContentValues[numberOfInsertions];
        for (int i = 0; i < numberOfInsertions; i++) {
            ContentValues cv = new ContentValues();
            cv.put(WondersTable.NAME, "WonderTest_"+i);
            cv.put(WondersTable.COUNTRY, "Brasil");
            cv.put(WondersTable.DESCRIPTION, "Apenas uma inserção de teste. Counter = "+i);
            cv.put(WondersTable.IMAGE_URL, "www.google.com");

            linesToBeInserted[i] = cv;
        }

        return resolver.bulkInsert(Provider.WONDER_CONTENT_URI, linesToBeInserted);
    }
}