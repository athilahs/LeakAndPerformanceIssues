package com.training.leakandperformanceissues.ui;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.training.leakandperformanceissues.R;

public class MemoryLeakActivity extends AppCompatActivity {

    private MyLeakThread myLeakThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_leak);

        startLeak();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myLeakThread.cancel();
    }

    private void startLeak() {
        myLeakThread = new MyLeakThread();
        myLeakThread.start();
    }

    private static class MyLeakThread extends Thread {
        private boolean cancelRequested = false;
        private String data;

        public MyLeakThread() {
            data = new String(new char[100000]);
        }

        @Override
        public void run() {
            while (!cancelRequested) {
                Log.i("TestLeakAndPerfomance", "Thread executed!");
                SystemClock.sleep(1000);
            }
        }

        public void cancel() {
            cancelRequested = true;
        }
    }
}
