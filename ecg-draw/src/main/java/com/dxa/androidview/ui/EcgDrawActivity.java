package com.dxa.androidview.ui;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.dxa.androidview.R;
import com.dxa.android.logger.DLogger;
import com.dxa.android.timer.Timer;
import com.dxa.android.timer.TimerTask;
import com.dxa.android.ui.ActivityPresenter;
import com.dxa.android.ui.SuperActivity;
import com.dxa.androidview.widget.DrawWaveView;
import com.dxa.common.CloseQuietly;
import com.google.common.reflect.ClassPath;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EcgDrawActivity extends SuperActivity {

    @BindView(R.id.ecg_view)
    DrawWaveView waveView;

    private final AtomicInteger count = new AtomicInteger();
    private List<String> pointLines = new ArrayList<>();
    private final Timer timer = new Timer(true);
    private final TimerTask task = new TimerTask("EcgDrawTask") {

        @Override
        public void run() {
            if (execState.get()) {
                if (count.getAndIncrement() % 3 != 0) {
                    return;
                }

                if (count.get() >= pointLines.size()) {
                    cancel();
                    return;
                }

                String line = pointLines.get(count.get());
                String[] split = line.split(", ");
                if (split.length != 3) {
                    return;
                }
                Message msg = Message.obtain();
                msg.what = what;
                msg.arg1 = Integer.parseInt(split[2]) / 2;
                drawHandler.sendMessage(msg);
            }
        }
    };

    private final AtomicBoolean execState = new AtomicBoolean(true);

    private final int what = 1;

    @SuppressLint("HandlerLeak")
    private final Handler drawHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (execState.get()) {
                waveView.updatePoint(msg.arg1);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setDebug(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecg_draw);
        ButterKnife.bind(this);

        if (isLandscape()) {
            logger.i("横屏");
            waveView.updateDensity();
            waveView.setValue(1024, 4000, -4000);
//            waveView.setValue(2048, 8000, -8000);;
//            waveView.setValue(2048, 8000, -8000);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    InputStream stream = null;
                    try {
//                        stream = getAssets().open("2018年05月23日16时54分03秒.txt");
                        stream = getAssets().open("2018年06月05日17时13分44秒.txt");
                        pointLines = IOUtils.readLines(stream, "UTF-8");
                        logger.i("读取数据, size[", pointLines.size(), "], first line: ", pointLines.get(0));
                        timer.schedule(task, 3000, 10);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        CloseQuietly.close(stream);
                        cancel();
                    }
                }
            }, 1, 1);
        } else {
            logger.i("竖屏");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        drawHandler.removeMessages(what);
        if (!timer.isCanceled()) {
            execState.set(false);
            timer.cancel();
            logger.i("取消task: ", task.getTaskName());
        }
    }

    @Nullable
    @Override
    protected ActivityPresenter buildPresenter() {
        return null;
    }

    public boolean isLandscape() {
        return Configuration.ORIENTATION_LANDSCAPE
                == getResources().getConfiguration().orientation;
    }
}
