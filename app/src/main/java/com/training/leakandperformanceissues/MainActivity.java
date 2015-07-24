package com.training.leakandperformanceissues;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startLeak();
    }

    private void startLeak() {
        MyLeakThread myLeakThread = new MyLeakThread();
        myLeakThread.start();
    }

    private class MyLeakThread extends Thread {
        private String data;
        private int count;

        public MyLeakThread() {
            data = new String(new char[100000]);
            count = 0;
        }

        @Override
        public void run() {
            while (true) {
                Log.i("TestLeakAndPerfomance", "Thread executed! count = "+(++count));
                SystemClock.sleep(1000);
            }
        }
    }
}
