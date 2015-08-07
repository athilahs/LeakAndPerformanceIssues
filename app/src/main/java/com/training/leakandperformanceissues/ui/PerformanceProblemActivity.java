package com.training.leakandperformanceissues.ui;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
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
    }

    private void setListeners() {
        btnStartLongOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewStatus.setVisibility(View.VISIBLE);
                textViewStatus.setText("Executing...");
                long startTime = System.currentTimeMillis();
                int linesCount = performInsert();
                long endTime = System.currentTimeMillis();
                long spentTime = (endTime - startTime)/1000;

                textViewStatus.setText("Done! inserted " + linesCount + " items. Time spent: " + spentTime + " secs.");
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