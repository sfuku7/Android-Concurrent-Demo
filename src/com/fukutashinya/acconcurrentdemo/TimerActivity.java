/*
The MIT License (MIT)

Copyright (c) 2013  Fukuta, Shinya

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.fukutashinya.acconcurrentdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Formatter;

public class TimerActivity extends Activity {

    private Button mButton;

    private TextView mTimeText;

    private Timer mTimer = new Timer();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mButton = (Button) findViewById(R.id.start_stop_button);
        mTimeText = (TextView) findViewById(R.id.timer);

        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mTimer.started()) {
                    stop();
                } else {
                    start();
                }
            }
        });

        mTimer.registerListener(new Timer.Listener() {
            public void onUpdate(final long timeMs) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        mTimeText.setText((new Formatter()).format("%02d:%03d", timeMs / 1000, timeMs % 1000).toString());
                    }
                });
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        stop();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
        stop();
    }


    private void stop() {
        mTimer.stop();
        mButton.setText("START");
    }

    private void start() {
        mTimer.start();
        mButton.setText("STOP");
    }
}
