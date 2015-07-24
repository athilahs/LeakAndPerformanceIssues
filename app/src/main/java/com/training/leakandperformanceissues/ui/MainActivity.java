package com.training.leakandperformanceissues.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.training.leakandperformanceissues.R;

public class MainActivity extends AppCompatActivity {

    private Button btnMemoryLeakExample;
    private Button btnPerformanceProblemExample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadUI();
        setListeners();
    }

    private void loadUI() {
        btnPerformanceProblemExample = (Button)findViewById(R.id.btn_performance_problem_example);
        btnMemoryLeakExample = (Button)findViewById(R.id.btn_memory_leak_example);
    }

    private void setListeners() {
        btnMemoryLeakExample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MemoryLeakActivity.class));
            }
        });

        btnPerformanceProblemExample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PerformanceProblemActivity.class));
            }
        });
    }
}
