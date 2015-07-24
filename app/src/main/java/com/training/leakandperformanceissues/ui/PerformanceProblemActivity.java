package com.training.leakandperformanceissues.ui;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
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
    private TextView textViewExecuting;
    private TextView textViewDone;

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
        btnStartLongOperation = (Button)findViewById(R.id.btn_execute_long_operation);
        textViewExecuting = (TextView)findViewById(R.id.textView_executing);
        textViewDone = (TextView)findViewById(R.id.textView_done);
    }

    private void setListeners() {
        btnStartLongOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long insertedId = performInsert();
                textViewDone.setText("Done! Inserted id = "+insertedId);
                textViewDone.setVisibility(View.VISIBLE);
            }
        });
    }

    private long performInsert() {
        ContentValues values = new ContentValues();
        values.put(WondersTable.NAME, "WonderTest");
        values.put(WondersTable.COUNTRY, "Brasil");
        values.put(WondersTable.DESCRIPTION, "Apenas uma inserção de teste.");
        values.put(WondersTable.IMAGE_URL, "www.google.com");

        Uri insertedUri = resolver.insert(Provider.WONDER_CONTENT_URI, values);

        return ContentUris.parseId(insertedUri);
    }
}