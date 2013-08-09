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

import android.util.Log;

import java.lang.InterruptedException;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.List;

public class Timer {

    private List<Listener> mListeners = new ArrayList<Listener>();

    private ExecutorService mThread = Executors.newFixedThreadPool(1);

    private Future mFuture;

    public interface Listener {
        void onUpdate(long timeMs);
    }

    public void start() {
        if (started()) {
            return;
        }
        mFuture = mThread.submit(new CountUpTask(this));
    }

    public void stop() {
        if (!started()) {
            return;
        }
        mFuture.cancel(true);
        mFuture = null;
    }

    public boolean started() {
        return mFuture != null;
    }

    public void registerListener(Listener l) {
        mListeners.add(l);
    }

    public void unregisterListener(Listener l) {
        mListeners.remove(l);
    }

    private void update(long timeMs) {
        for (Listener l : mListeners) {
            l.onUpdate(timeMs);
        }
    }

    static class CountUpTask implements Runnable {

        private Timer mOwner;

        public CountUpTask(Timer owner) {
            mOwner = owner;
        }

        public void run() {
            long startTimeMs = System.currentTimeMillis();
            while (!Thread.interrupted()) {
                try {
                    Thread.sleep(10);
                    mOwner.update(System.currentTimeMillis() - startTimeMs);
                } catch (InterruptedException e) {
                    Log.d("#####", "Sleep Interrputed !!");
                    break;
                }
            }
            Log.d("#####", "BREAK !!");
        }
    }
}
